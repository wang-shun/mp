package com.fiberhome.mapps.search;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstact Index Operation Class How to make searching faster see
 * https://wiki.apache.org/lucene-java/ImproveSearchingSpeed
 * 
 * @author Administrator
 *
 */
public class DefaultIndexAccessor implements IndexAccessor<String> {
	private static Logger LOG = LoggerFactory.getLogger(DefaultIndexAccessor.class);

	private int maxResults = 1000;

	private static ConcurrentHashMap<String, IndexSearcher> instanceMap = new ConcurrentHashMap<String, IndexSearcher>();

	private String docRoot;
	private String indexName;
	private DocumentBuilder<String> documentBuilder;

	public DefaultIndexAccessor() {

	}

	public DefaultIndexAccessor(int maxResults) {
		this.maxResults = maxResults;
	}

	public String getDocRoot() {
		return docRoot;
	}

	public void setDocRoot(String docRoot) {
		this.docRoot = docRoot;
	}

	public String getIndexName() {
		return this.indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public DocumentBuilder<String> getDocumentBuilder() {
		return this.documentBuilder;
	}

	public void setDocumentBuilder(DocumentBuilder<String> documentBuilder) {
		this.documentBuilder = documentBuilder;
	}

	private IndexWriter getIndexWriter(boolean clean) throws IOException {
		Directory indexDir = null;

		indexDir = getIndexDir();

		// Use Smartcn 分词
		// Analyzer analyzer = new SmartChineseAnalyzer();

		// Use Mmseg4j
		Analyzer analyzer = getAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setCommitOnClose(true);

		if (clean) {
			// Create a new index in the directory, removing any
			// previously indexed documents:
			iwc.setOpenMode(OpenMode.CREATE);
		} else {
			// Add new documents to an existing index:
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}

		// Optional: for better indexing performance, if you
		// are indexing many documents, increase the RAM
		// buffer. But if you do this, increase the max heap
		// size to the JVM (eg add -Xmx512m or -Xmx1g):
		//
		iwc.setRAMBufferSizeMB(128.0);

		IndexWriter writer = new IndexWriter(indexDir, iwc);
		return writer;
	}

	public Analyzer getAnalyzer() {
		return new StandardAnalyzer(CharArraySet.EMPTY_SET);
	}

	private Directory getIndexDir() throws IOException {
		Path path = getIndexPath();
		Directory indexDir;

		// 非windows使用NIO的FileSystem
		if (System.getProperty("os.name").contains("Windows")) {
			indexDir = FSDirectory.open(path);
		} else {
			indexDir = NIOFSDirectory.open(path);
		}
		return indexDir;
	}

	private Path getIndexPath() {
		String indexDirectoryPath = getDocRoot() + File.separator + getIndexName();

		Path path = Paths.get(indexDirectoryPath);
		return path;
	}

	private synchronized IndexSearcher getSearcher() throws IOException {
		String idxName = this.getIndexName();
		assert (idxName != null);
		IndexSearcher searcher = instanceMap.get(idxName);

		if (searcher == null) {
			Directory indexDir = getIndexDir();

			IndexReader indexReader = DirectoryReader.open(indexDir);
			searcher = new IndexSearcher(indexReader);

			instanceMap.put(idxName, searcher);
		}
		return searcher;
	}

	@Override
	public void updateDocument(String id) throws IOException {
		DocumentBuilder<String> db = this.getDocumentBuilder();
		IndexWriter writer = this.getIndexWriter(false);
		try {
			LOG.debug("X-X-X----update documents:{}", id);
			Document doc = db.build(id);
			if (doc != null) {
				writer.updateDocument(getTerm(id), doc);
			} else {
				LOG.warn("Can't get entity from id: {}", id);
			}

			clearSearcherCache();
		} finally {
			writer.close();
		}
	}

	private void clearSearcherCache() {
		instanceMap.remove(indexName);
	}

	@Override
	public void updateDocument(List<String> ids) throws IOException {
		DocumentBuilder<String> db = this.getDocumentBuilder();
		IndexWriter writer = this.getIndexWriter(false);
		try {
			LOG.debug("X-X-X----update documents:{}", ids);
			for (String id : ids) {
				Document doc = db.build(id);
				if (doc != null) {
					writer.updateDocument(getTerm(id), doc);
				}
			}
			clearSearcherCache();
		} finally {
			writer.close();
		}
	}

	@Override
	public void deleteDocument(String id) throws IOException {
		IndexWriter writer = this.getIndexWriter(false);
		try {
			LOG.debug("X-X-X----delete document:{}", id);
			writer.deleteDocuments(getTerm(id));
			clearSearcherCache();
		} finally {
			writer.close();
		}
	}

	@Override
	public void deleteDocument(List<String> ids) throws IOException {
		IndexWriter writer = this.getIndexWriter(false);

		try {
			Term[] terms = new Term[ids.size()];
			int index = 0;
			for (String id : ids) {
				terms[index++] = getTerm(id);
			}
			LOG.debug("X-X-X----delete documents:{}", ids);
			writer.deleteDocuments(terms);
			clearSearcherCache();
		} finally {
			writer.close();
		}
	}

	private Term getTerm(Serializable id) {
		return new Term("id", id.toString());
	}

	@Override
	public void addDocument(String id) throws IOException {
		DocumentBuilder<String> db = this.getDocumentBuilder();
		IndexWriter writer = this.getIndexWriter(false);

		try {
			LOG.debug("X-X-X----add document:{}", id);
			Document doc = db.build(id);

			if (doc != null) {
				writer.addDocument(doc);
			} else {
				LOG.warn("Can't get entity from id: {}", id);
			}
			clearSearcherCache();
		} finally {
			writer.close();
		}
	}

	@Override
	public void addDocument(List<String> ids) throws IOException {
		DocumentBuilder<String> db = this.getDocumentBuilder();
		IndexWriter writer = this.getIndexWriter(false);

		try {
			LOG.debug("X-X-X----add documents:{}", ids);
			List<Document> docs = db.build(ids);
			if (docs != null) {
				writer.addDocuments(docs);
				clearSearcherCache();
			}

			if (docs == null || docs.size() != ids.size()) {
				LOG.warn("Can't get all entity from ids: {}, some entity is not exists.", ids);
			}
		} finally {
			writer.close();
		}
	}

	@Override
	public void resync() throws IOException {
		DocumentBuilder<String> db = this.getDocumentBuilder();
		IndexWriter writer = this.getIndexWriter(true);

		try {
			int limit = 1000;
			int offset = 0;
			List<Document> docs;
			do {
				docs = db.fetch(limit, offset);
				if (docs == null || docs.size() == 0) {
					break;
				}
				LOG.debug("X-X-X----resync documents:{}", docs.size());
				writer.addDocuments(docs);
				offset += limit;
			} while (true);
		} finally {
			writer.close();
		}
		clearSearcherCache();
	}

	@Override
	public DocumentPage searchPage(List<String> fields, String keywords, Map<String, List<String>> filter, int limit,
			int offset) throws IOException {
		DocumentPage page = new DocumentPage();
		IndexSearcher searcher = getSearcher();

		Query query = buildQuery(fields, keywords, filter);

		TopScoreDocCollector collector = TopScoreDocCollector.create(maxResults);
		searcher.search(query, collector);

		page.setTotalhits(Math.min(collector.getTotalHits(), maxResults));

		ArrayList<Document> result = new ArrayList<Document>();

		for (ScoreDoc scoreDoc : collector.topDocs(offset, limit).scoreDocs) {
			result.add(searcher.doc(scoreDoc.doc));
		}

		page.setDocs(result);
		return page;
	}

	@Override
	public DocumentPage searchPage(List<String> fields, String keywords, Map<String, List<String>> filter, int limit,
			int offset, String sortField) throws IOException {
		DocumentPage page = new DocumentPage();

		IndexSearcher searcher = getSearcher();

		Query query = buildQuery(fields, keywords, filter);

		SortField sort;
		boolean desc = true;
		String sf = sortField;
		if (sortField.startsWith("!")) {
			desc = false;
			sf = sortField.substring(1);
		}

		FieldDefine fd = this.getFieldDefine(sf);
		if (fd.isTs()) {
			// 第三个参数为true，表示从大到小
			sort = new SortField(sf, SortField.Type.LONG, desc);
		} else {
			sort = new SortField(sf, SortField.Type.STRING, desc);
		}

		TopFieldCollector collector = TopFieldCollector.create(new Sort(sort), maxResults, false, false, false);

		// TopScoreDocCollector collector =
		// TopScoreDocCollector.create(MAX_RESULTS);
		searcher.search(query, collector);
		page.setTotalhits(Math.min(collector.getTotalHits(), maxResults));

		ArrayList<Document> result = new ArrayList<Document>();

		for (ScoreDoc scoreDoc : collector.topDocs(offset, limit).scoreDocs) {
			result.add(searcher.doc(scoreDoc.doc));
		}
		page.setDocs(result);
		return page;
	}

	@Override
	public List<Document> search(List<String> fields, String keywords, Map<String, List<String>> filter, int limit,
			int offset) throws IOException {
		DocumentPage page = searchPage(fields, keywords, filter, limit, offset);

		return page != null ? page.getDocs() : null;
	}

	@Override
	public List<Document> search(List<String> fields, String keywords, Map<String, List<String>> filter, int limit,
			int offset, String sortField) throws IOException {
		DocumentPage page = searchPage(fields, keywords, filter, limit, offset, sortField);

		return page != null ? page.getDocs() : null;
	}

	public List<HighLighterDocument> search(List<String> fields, String keywords, Map<String, List<String>> filter,
			int limit, int offset, String sortField, String highField) throws IOException {
		IndexSearcher searcher = getSearcher();

		Query query = buildQuery(fields, keywords, filter);

		SortField sort;
		boolean desc = true;
		String sf = sortField;
		if (sortField.startsWith("!")) {
			desc = false;
			sf = sortField.substring(1);
		}

		FieldDefine fd = this.getFieldDefine(sf);
		if (fd.isTs()) {
			// 第三个参数为true，表示从大到小
			sort = new SortField(sf, SortField.Type.LONG, desc);
		} else {
			sort = new SortField(sf, SortField.Type.STRING, desc);
		}

		TopFieldCollector collector = TopFieldCollector.create(new Sort(sort), maxResults, false, false, false);

		// TopScoreDocCollector collector =
		// TopScoreDocCollector.create(MAX_RESULTS);
		searcher.search(query, collector);

		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("", "");
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));
		highlighter.setTextFragmenter(new SimpleFragmenter(30));

		ArrayList<HighLighterDocument> result = new ArrayList<HighLighterDocument>();

		Analyzer analyzer = this.getAnalyzer();
		for (ScoreDoc scoreDoc : collector.topDocs(offset, limit).scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			HighLighterDocument hlDocument = new HighLighterDocument(doc);
			TokenStream tokenStream = analyzer.tokenStream(highField, new StringReader(doc.get(highField)));
			try {
				String str = highlighter.getBestFragment(tokenStream, doc.get(highField));
				hlDocument.setHlStr(str);
			} catch (InvalidTokenOffsetsException e) {
				LOG.warn(e.getMessage(), e);
			}
			result.add(hlDocument);
		}
		return result;
	}

	private Query buildQuery(List<String> fields, String keywords, Map<String, List<String>> filter) {
		Analyzer analyzer = getAnalyzer();

		QueryParser parser = new QueryParser(fields.get(0), analyzer);
		String kw = QueryParser.escape(keywords);
		Query query = null;

		Builder builder = new BooleanQuery.Builder();

		for (String field : fields) {
			Query fieldQuery;
			FieldDefine fd = getFieldDefine(field);
			if (fd.isTokenized()) {
				fieldQuery = parser.createPhraseQuery(field, kw, 3);
			} else {
				fieldQuery = new WildcardQuery(new Term(field, "*" + kw + "*"));
			}
			if (fieldQuery != null) {
				builder.add(fieldQuery, BooleanClause.Occur.SHOULD);
			}
		}

		query = builder.build();

		Builder queryBuilder = new BooleanQuery.Builder().add(query, BooleanClause.Occur.MUST);

		if (filter != null) {
			for (String key : filter.keySet()) {
				String field = key;
				Builder subBuilder = new BooleanQuery.Builder();
				boolean not = false;
				if (field.startsWith("!")) {
					not = true;
					field = key.substring(1);
				}
				List<String> values = filter.get(key);
				if (values == null || values.size() == 0) {
					throw new IllegalArgumentException("传入过滤参数不能为空！");
				}

				FieldDefine fd = getFieldDefine(field);

				if (fd.isTs()) {
					if (values.size() > 1) {
						throw new IllegalArgumentException("时间戳过滤参数只能有一个！");
					}
					long ts = Long.parseLong(values.get(0));
					if (ts == 0)
						break; // 传入的时间戳为零则不处理
					long min = 0;
					long max = Long.MAX_VALUE;

					if (not) {
						min = ts;
					} else {
						max = ts;
					}
					NumericRangeQuery<Long> rangeQuery = NumericRangeQuery.newLongRange(fd.getName(), min, max, false,
							false);
					queryBuilder.add(rangeQuery, BooleanClause.Occur.MUST);
				} else {
					for (String val : values) {
						Query q = null;

						if (fd.isTokenized()) {
							q = parser.createPhraseQuery(field, val);
						} else {
							q = new TermQuery(new Term(field, val));
						}
						subBuilder.add(q, BooleanClause.Occur.SHOULD);
					}

					queryBuilder.add(subBuilder.build(), not ? BooleanClause.Occur.MUST_NOT : BooleanClause.Occur.MUST);
				}

			}
		}

		query = queryBuilder.build();
		return query;
	}

	private FieldDefine getFieldDefine(String field) {
		List<FieldDefine> fdList = documentBuilder.getSchema();

		for (FieldDefine fd : fdList) {
			if (fd.getName().equals(field)) {
				return fd;
			}
		}

		return null;
	}

	public void init() {
		Path path = getIndexPath();
		if (!path.toFile().exists()) {
			try {
				this.resync();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
