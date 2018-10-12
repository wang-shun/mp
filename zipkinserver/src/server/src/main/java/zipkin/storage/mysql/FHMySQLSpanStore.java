/**
 * Copyright 2015-2016 The OpenZipkin Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package zipkin.storage.mysql;

import static zipkin.Constants.CLIENT_ADDR;
import static zipkin.Constants.CLIENT_SEND;
import static zipkin.Constants.SERVER_ADDR;
import static zipkin.Constants.SERVER_RECV;
import static zipkin.internal.Util.getDays;
import static zipkin.storage.mysql.internal.generated.tables.ZipkinAnnotations.ZIPKIN_ANNOTATIONS;
import static zipkin.storage.mysql.internal.generated.tables.ZipkinDependencies.ZIPKIN_DEPENDENCIES;
import static zipkin.storage.mysql.internal.generated.tables.ZipkinSpans.ZIPKIN_SPANS;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SelectOffsetStep;
import org.jooq.TableField;
import org.jooq.TableOnConditionStep;

import zipkin.DependencyLink;
import zipkin.Span;
import zipkin.internal.DependencyLinkSpan;
import zipkin.internal.DependencyLinker;
import zipkin.internal.Nullable;
import zipkin.storage.QueryRequest;
import zipkin.storage.SpanStore;
import zipkin.storage.mysql.internal.generated.tables.ZipkinAnnotations;

final class FHMySQLSpanStore implements SpanStore {

  private final DataSource datasource;
  private final DSLContexts context;
  private final Schema schema;
  //private final boolean strictTraceId;
  private MySQLSpanStore mySqlSpanStore;

  FHMySQLSpanStore(DataSource datasource, DSLContexts context, Schema schema, boolean strictTraceId) {
    this.datasource = datasource;
    this.context = context;
    this.schema = schema;
    //this.strictTraceId = strictTraceId;
    this.mySqlSpanStore = new MySQLSpanStore(datasource, context, schema, strictTraceId);
  }

//  private Endpoint endpoint(Record a) {
//    String serviceName = a.getValue(ZIPKIN_ANNOTATIONS.ENDPOINT_SERVICE_NAME);
//    if (serviceName == null) return null;
//    return Endpoint.builder()
//        .serviceName(serviceName)
//        .port(a.getValue(ZIPKIN_ANNOTATIONS.ENDPOINT_PORT))
//        .ipv4(a.getValue(ZIPKIN_ANNOTATIONS.ENDPOINT_IPV4))
//        .ipv6(maybeGet(a, ZIPKIN_ANNOTATIONS.ENDPOINT_IPV6, null)).build();
//  }

  SelectOffsetStep<? extends Record> toTraceIdQuery(DSLContext context, QueryRequest request) {
	  return mySqlSpanStore.toTraceIdQuery(context, request);
  }

  static TableOnConditionStep<?> maybeOnService(TableOnConditionStep<Record> table,
      ZipkinAnnotations aTable, String serviceName) {
    if (serviceName == null) return table;
    return table.and(aTable.ENDPOINT_SERVICE_NAME.eq(serviceName));
  }

  List<List<Span>> getTraces(@Nullable QueryRequest request, @Nullable Long traceIdHigh,
      @Nullable Long traceIdLow, boolean raw) {
	  return mySqlSpanStore.getTraces(request, traceIdHigh, traceIdLow, raw);
  }

  static <T> T maybeGet(Record record, TableField<Record, T> field, T defaultValue) {
    if (record.fieldsRow().indexOf(field) < 0) {
      return defaultValue;
    } else {
      return record.get(field);
    }
  }

  @Override
  public List<List<Span>> getTraces(QueryRequest request) {
	  return mySqlSpanStore.getTraces(request);
  }

  @Override
  public List<Span> getTrace(long traceId) {
	  return mySqlSpanStore.getTrace(traceId);
  }

  @Override public List<Span> getTrace(long traceIdHigh, long traceIdLow) {
	  return mySqlSpanStore.getTrace(traceIdHigh, traceIdLow);
  }

  @Override
  public List<Span> getRawTrace(long traceId) {
	  return mySqlSpanStore.getRawTrace(traceId);
  }

  @Override public List<Span> getRawTrace(long traceIdHigh, long traceIdLow) {
	  return mySqlSpanStore.getRawTrace(traceIdHigh, traceIdLow);
  }

  @Override
  public List<String> getServiceNames() {
	  return mySqlSpanStore.getServiceNames();
  }

  @Override
  public List<String> getSpanNames(String serviceName) {
	  return mySqlSpanStore.getSpanNames(serviceName);
  }

  @Override
  public List<DependencyLink> getDependencies(long endTs, @Nullable Long lookback) {
	  try (Connection conn = datasource.getConnection()) {
	      if (schema.hasPreAggregatedDependencies) {
	        List<Date> days = getDays(endTs, lookback);
	        List<DependencyLink> unmerged = context.get(conn)
	            .selectFrom(ZIPKIN_DEPENDENCIES)
	            .where(ZIPKIN_DEPENDENCIES.DAY.in(days))
	            .fetch(new RecordMapper<Record, DependencyLink>() {
					@Override
					public DependencyLink map(Record l) {
						return DependencyLink.create(
						    l.get(ZIPKIN_DEPENDENCIES.PARENT),
						    l.get(ZIPKIN_DEPENDENCIES.CHILD),
						    l.get(ZIPKIN_DEPENDENCIES.CALL_COUNT));
					}
				}
	            );
	        return DependencyLinker.merge(unmerged);
	      } else {
	        return aggregateDependencies(endTs, lookback, conn);
	      }
	    } catch (SQLException e) {
	      throw new RuntimeException("Error querying dependencies for endTs "
	          + endTs + " and lookback " + lookback + ": " + e.getMessage());
	    }
  }

  List<DependencyLink> aggregateDependencies(long endTs, @Nullable Long lookback, Connection conn) {
    endTs = endTs * 1000;
    // Lazy fetching the cursor prevents us from buffering the whole dataset in memory.
    Cursor<Record> cursor = context.get(conn)
        .selectDistinct(schema.dependencyLinkFields)
        // left joining allows us to keep a mapping of all span ids, not just ones that have
        // special annotations. We need all span ids to reconstruct the trace tree. We need
        // the whole trace tree so that we can accurately skip local spans.
        .from(ZIPKIN_SPANS.leftJoin(ZIPKIN_ANNOTATIONS)
            // NOTE: we are intentionally grouping only on the low-bits of trace id. This buys time
            // for applications to upgrade to 128-bit instrumentation.
            .on(ZIPKIN_SPANS.TRACE_ID.eq(ZIPKIN_ANNOTATIONS.TRACE_ID).and(
                ZIPKIN_SPANS.ID.eq(ZIPKIN_ANNOTATIONS.SPAN_ID)))
            .and(ZIPKIN_ANNOTATIONS.A_KEY.in(CLIENT_SEND, CLIENT_ADDR, SERVER_RECV, SERVER_ADDR)))
        .where(lookback == null ?
            ZIPKIN_SPANS.START_TS.lessOrEqual(endTs) :
            ZIPKIN_SPANS.START_TS.between(endTs - lookback * 1000, endTs))
        // Grouping so that later code knows when a span or trace is finished.
        .groupBy(schema.dependencyLinkGroupByFields).fetchLazy();

    Iterator<Iterator<DependencyLinkSpan>> traces =
        new DependencyLinkSpanIterator.ByTraceId(cursor.iterator(), schema.hasTraceIdHigh);

    if (!traces.hasNext()) return Collections.emptyList();

    DependencyLinker linker = new DependencyLinker();
    
    while (traces.hasNext()) {
      linker.putTraceExceptGateway(traces.next());
    }

    return linker.link();
  }
}
