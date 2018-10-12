package com.fiberhome.mapps.szzj.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class QueryVChannelListRequest extends AbstractRopRequest{
		@NotNull
	    private long   timestamp;
	    @NotNull
	    @Min(value = 1, message = "分页页数最小为1")
	    private int    offset;
	    @NotNull
	    @Min(value = 1, message = "每页记录数最小为1")
	    @Max(value = 100, message = "每页记录数最大为100")
	    private int    limit;

	    private String sort;
	    
	    private String channel;

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public int getOffset() {
			return offset;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		public int getLimit() {
			return limit;
		}

		public void setLimit(int limit) {
			this.limit = limit;
		}

		public String getSort() {
			return sort;
		}

		public void setSort(String sort) {
			this.sort = sort;
		}

		public String getChannel() {
			return channel;
		}

		public void setChannel(String channel) {
			this.channel = channel;
		}
}
