package com.fiberhome.mapps.search;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;

public interface IndexAccessor<T> {
	/**
	 * 更新文档索引
	 * @param id
	 * @throws IOException
	 */
	void updateDocument(T id) throws IOException;
	
	/**
	 * 更新多个文档索引
	 * @param ids
	 * @throws IOException
	 */
	void updateDocument(List<T> ids) throws IOException;
	
	/**
	 * 从索引中删除文档
	 * @param id
	 * @throws IOException
	 */
	void deleteDocument(T id) throws IOException;
	
	/**
	 * 从索引中删除多个文档
	 * @param ids
	 * @throws IOException
	 */
	void deleteDocument(List<T> ids) throws IOException;
	
	/**
	 * 添加文档到索引
	 * @param ids
	 * @throws IOException
	 */
	void addDocument(T id) throws IOException;
	
	/**
	 * 添加多个文档到索引
	 * @param ids
	 * @throws IOException
	 */
	void addDocument(List<T> ids) throws IOException;
	
	/**
	 * 重新从数据源获取数据，重建索引
	 * @throws IOException
	 */
	void resync() throws IOException;
	
	/**
	 * 搜索
	 * @param fields 搜索关键字匹配字段
	 * @param keywords 关键字
	 * @param filter 过滤器，(field, [vlaue list])的形式，支持多个字段，多个字段的关系是与的关系，单个字段多值为或关系，如feild以“!”开头，则为不包含值列中的文档
	 * @param limit 分页数
	 * @param offset 记录偏移量
	 * @return
	 * @throws IOException
	 */
	DocumentPage searchPage(List<String> fields, String keywords, Map<String, List<String>> filter, int limit,
			int offset) throws IOException;
	
	/**
	 * 搜索
	 * @param fields 搜索关键字匹配字段
	 * @param keywords 关键字
	 * @param filter 过滤器，(field, [vlaue list])的形式，支持多个字段，多个字段的关系是与的关系，单个字段多值为或关系，如feild以“!”开头，则为不包含值列中的文档
	 * @param limit 分页数
	 * @param offset 记录偏移量
	 * @return
	 * @throws IOException
	 */
	List<Document> search(List<String> fields, String keywords, Map<String, List<String>> filter, int limit,
			int offset) throws IOException;	
	
	/**
	 * 搜索
	 * @param fields 搜索关键字匹配字段
	 * @param keywords 关键字
	 * @param filter 过滤器，(field, [vlaue list])的形式，支持多个字段，多个字段的关系是与的关系，单个字段多值为或关系，如feild以“!”开头，则为不包含值列中的文档
	 * @param limit 分页数
	 * @param offset 记录偏移量
	 * @param sortField 排序字段，默认倒序排列，如字段名以“!”开头，则正序排列
	 * @return
	 * @throws IOException
	 */
	DocumentPage searchPage(List<String> fields, String keywords, Map<String, List<String>> filter, int limit,
			int offset, String sortField) throws IOException;
	
	/**
	 * 搜索
	 * @param fields 搜索关键字匹配字段
	 * @param keywords 关键字
	 * @param filter 过滤器，(field, [vlaue list])的形式，支持多个字段，多个字段的关系是与的关系，单个字段多值为或关系，如feild以“!”开头，则为不包含值列中的文档
	 * @param limit 分页数
	 * @param offset 记录偏移量
	 * @param sortField 排序字段，默认倒序排列，如字段名以“!”开头，则正序排列
	 * @return
	 * @throws IOException
	 */
	List<Document> search(List<String> fields, String keywords, Map<String, List<String>> filter, int limit, int offset,
			String sortField) throws IOException;
}
