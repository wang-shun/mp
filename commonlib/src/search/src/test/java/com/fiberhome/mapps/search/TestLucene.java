package com.fiberhome.mapps.search;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.junit.Before;
import org.junit.Test;

import com.fiberhome.mapps.search.WeiboDocumentBuilder.Weibo;

//@RunWith(SpringJUnit4ClassRunner.class)
public class TestLucene {
	DefaultIndexAccessor index = null;
	WeiboDocumentBuilder docBuilder = null;

	@Before
	public void init() throws IOException {
		index = new DefaultIndexAccessor();
		index.setDocRoot("e:/index");
		index.setIndexName("weibo");
		docBuilder = new WeiboDocumentBuilder();
		index.setDocumentBuilder(docBuilder);

		// 重建索引，实际使用中，特殊情况才需要重建
		index.resync();
	}

//	@Test
	public void testSearch() throws IOException {
		String keywords = "黄怒波";
		String id1 = "3598967218672265";
		String id2 = "3598969467164303";
		String userName = "中国企业家杂志";
		HashMap<String, List<String>> filter = new HashMap<String, List<String>>();
		filter.put("userName", Arrays.asList(userName));
		int total = search(keywords, 100, 0, filter).size();
		
		System.out.println("Search by sort");
		search(keywords, 100, 0, filter, "ts");

		System.out.println("Search id match documents ......");
		HashMap<String, List<String>> must = new HashMap<String, List<String>>();
		
		must.put("id", Arrays.asList(id1, id2));
		must.put("userName", Arrays.asList(userName));
		int match = search(keywords, 100, 0, must).size();

		System.out.println("Search id not match documents ......");
		HashMap<String, List<String>> mustnot = new HashMap<String, List<String>>();
		// 不包含测试
		mustnot.put("!id", Arrays.asList(id1, id2));
		mustnot.put("userName", Arrays.asList(userName));
		int notmatch = search(keywords, 100, 0, mustnot).size();

		assertTrue(total == notmatch + match);
		
		System.out.println("Search id match documents ......");
		must = new HashMap<String, List<String>>();
		must.put("id", Arrays.asList(id1));
		must.put("userName", Arrays.asList(userName));
		match = search(keywords, 100, 0, must).size();

		System.out.println("Search id not match documents ......");
		mustnot = new HashMap<String, List<String>>();
		// 不包含测试
		mustnot.put("!id", Arrays.asList(id1));
		mustnot.put("userName", Arrays.asList(userName));
		notmatch = search(keywords, 100, 0, mustnot).size();

		assertTrue(total == notmatch + match);
		
	}
	
	@Test
	public void testEnglishSearch() throws IOException {
		String keywords = "com";
		HashMap<String, List<String>> must = new HashMap<String, List<String>>();
		int size = search(keywords, 100, 0, must).size();
		
		assertTrue(size > 0);
	}
	
	private List<Document> search(String keyword, int limit, int offset, Map<String, List<String>> filter) throws IOException {
		return this.search(keyword, limit, offset, filter, null);
	}

	private List<Document> search(String keyword, int limit, int offset, Map<String, List<String>> filter, String sort) throws IOException {
		long begin = System.currentTimeMillis();
		List<Document> docs = null;
		if (sort == null) {
			docs = index.search(Arrays.asList("userName", "content"), keyword, filter, limit, offset);
		} else {
			docs = index.search(Arrays.asList("userName", "content"), keyword, filter, limit, offset, sort);
		}
		System.out.println("query times(ms):" + (System.currentTimeMillis() - begin));
		for (Document doc : docs) {
			String id = doc.get("id");
			Weibo weibo = (Weibo) docBuilder.getSourceAccessor().getEntity(id);
			System.out.println(doc.get("id") + ":" + doc.get("userName") + " ：" + weibo.getContent());
		}
		System.out.println("--------------------");
		return docs;

	}
}
