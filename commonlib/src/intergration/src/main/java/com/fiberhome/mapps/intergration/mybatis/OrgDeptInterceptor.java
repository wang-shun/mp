package com.fiberhome.mapps.intergration.mybatis;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fiberhome.mapps.intergration.session.SessionContext;

@SuppressWarnings("rawtypes")
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }), })
public class OrgDeptInterceptor implements Interceptor {

	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("serial")
	class AuthorityDenyException extends NullPointerException {
		public AuthorityDenyException() {
			super();
		}
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if(SessionContext.getOrgDeptId() != null && SessionContext.getOrgDeptOrder() != null){
			Object parameter = invocation.getArgs()[1];
			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			BoundSql boundSql = mappedStatement.getBoundSql(parameter);
			String sql = boundSql.getSql();
			if(parameter.getClass().isAnnotationPresent(OrgDept.class)){//判断sql入参是否含有OrgDept注解
				Field[] fields = parameter.getClass().getDeclaredFields();
				Field orgDepIdField = null;
				Field orgDepOrderField = null;
				String orgDepId = "";
				String orgDepOrder = "";
				for (Field field : fields) {
					String name = field.getName(); // 获取成员变量的名字，此处为id，name,age
					Class<?> classType = parameter.getClass();
					// 获取get和set方法的名字
					String firstLetter = name.substring(0, 1).toUpperCase(); // 将属性的首字母转换为大写
					String getMethodName = "get" + firstLetter + name.substring(1);
					//String setMethodName = "set" + firstLetter + name.substring(1);
					// 获取方法对象
					Method getMethod = classType.getMethod(getMethodName, new Class[] {});
					// Method setMethod = classType.getMethod(setMethodName, new
					// Class[]{field.getType()});//注意set方法需要传入参数类型
					if (parameter.getClass().getAnnotation(OrgDept.class).orgDepId().equals(name)) {
						
						orgDepIdField = field;
						// 调用get方法获取旧的对象的值
						orgDepId = (String) getMethod.invoke(parameter);
						orgDepId = (orgDepId == null)?"":orgDepId;
					}else if(parameter.getClass().getAnnotation(OrgDept.class).orgDeptOrder().equals(name)){
						orgDepOrderField = field;
						// 调用get方法获取旧的对象的值
						orgDepOrder = (String) getMethod.invoke(parameter);
						orgDepOrder = (orgDepOrder == null)?"":orgDepOrder;
					}
				}
				if(orgDepIdField != null && orgDepId.equals("")){
					orgDepId = SessionContext.getOrgDeptId();
					//orgDepId = "testorgDepId";
					//LOGGER.info("=============orgDepId==="+orgDepId);
					if (orgDepId != null) {
						BeanUtils.setProperty(parameter, orgDepIdField.getName(), orgDepId);
					}
				}
				if(orgDepOrderField != null && orgDepOrder.equals("")){
					orgDepOrder = SessionContext.getOrgDeptOrder();
					//orgDepOrder = "testorgDepOrder";
					//LOGGER.info("=============orgDepOrder==="+orgDepOrder);
					if (orgDepOrder != null) {
						BeanUtils.setProperty(parameter, orgDepOrderField.getName(), orgDepOrder);
					}
				}
			}
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {

	}

}
