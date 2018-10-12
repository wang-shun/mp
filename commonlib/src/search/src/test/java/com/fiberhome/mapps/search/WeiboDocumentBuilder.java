package com.fiberhome.mapps.search;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.document.Document;

public class WeiboDocumentBuilder extends AbstractDocumentBuilder {
	private ArrayList<FieldDefine> schema = new ArrayList<FieldDefine>();
	private SourceAccessor<String> sourceAccessor;
	
	public WeiboDocumentBuilder() {
		schema.add(FieldDefine.txtField("id", false, true));
		schema.add(FieldDefine.tsField("ts"));
		schema.add(FieldDefine.txtField("userId", false, true));
		schema.add(FieldDefine.txtField("userName", true, true));
		schema.add(FieldDefine.txtField("content", true, false));
		
		sourceAccessor = new WeiboTextAccessor();
	}

	@Override
	public List<FieldDefine> getSchema() {
		return schema;
	}

	@Override
	public SourceAccessor getSourceAccessor() {
		return sourceAccessor;
	}

	public class WeiboTextAccessor implements SourceAccessor<String> {
		private HashMap<String, Weibo> objMap = new HashMap<String, Weibo>();
		private ArrayList<String> idList = new ArrayList<String>();
		
		public WeiboTextAccessor() {
			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("weibo_data.txt"));
				BufferedReader br = new BufferedReader(isr);
				
				while (true) {
					String txt = br.readLine();
					
					if (txt == null || txt.length() < 10) {
						break;
					}
					
					Weibo weibo = new Weibo(txt);
					objMap.put(weibo.getId(), weibo);
					idList.add(weibo.getId());
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (isr != null) {
						isr.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

		@Override
		public Object getEntity(String id) {
			return objMap.get(id);
		}

		@Override
		public List<Object> getEntities(List<String> ids) {
			ArrayList<Object> list = new ArrayList<Object>();
			
			for (Serializable id : ids) {
				list.add(objMap.get(id));
			}
			return list;
		}

		@Override
		public List<Object> getEntities(int limit, int offset) {
			ArrayList<Object> result = new ArrayList<Object>();
			
			for (int i = offset; i < offset + limit && i < idList.size(); i++) {
				result.add(objMap.get(idList.get(i)));
			}
			return result;
		}
	}

	public class Weibo {
		private String id;
		private long ts;
		private String userId;
		private String userName;
		private String content;
		
		public Weibo(String txt) {
			String[] splits = txt.split("====");
			id = splits[0];
			ts = Long.parseLong(splits[1]);
			userId = splits[2];
			userName = splits[3];
			content = splits[9];
		}		

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public long getTs() {
			return ts;
		}

		public void setTs(long ts) {
			this.ts = ts;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

	}
}
