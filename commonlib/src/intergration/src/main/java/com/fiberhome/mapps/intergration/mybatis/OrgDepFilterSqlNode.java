package com.fiberhome.mapps.intergration.mybatis;

import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.ExpressionEvaluator;
import org.apache.ibatis.scripting.xmltags.SqlNode;

import com.fiberhome.mapps.intergration.session.SessionContext;

public class OrgDepFilterSqlNode implements SqlNode {
	private ExpressionEvaluator evaluator;
	private String prefix;
	private String deporder;
	private SqlNode contents;

	public OrgDepFilterSqlNode(SqlNode contents, String prefix,String deporder ) {
		this.prefix = prefix;
		this.deporder = deporder;
		this.contents = contents;
		this.evaluator = new ExpressionEvaluator();
	}

	@Override
	public boolean apply(DynamicContext context) {
		if(SessionContext.getOrgDeptId() != null && SessionContext.getOrgDeptOrder() != null){
			if("".equals(prefix)){
				prefix = "and";
			}
			if(!"".equals(deporder)){
				//context.appendSql(" "+prefix+" ( "+deporder+" is null or "+deporder+" LIKE '"+SessionContext.getOrgDeptOrder()+"%' ) ");
				context.appendSql(" "+prefix+" ( "+deporder+" LIKE '"+SessionContext.getOrgDeptOrder()+"%' ) ");
			}
		}
		contents.apply(context);
		return true;
	}
}
