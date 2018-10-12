package com.fiberhome.mapps.intergration.mybatis;

import java.lang.reflect.Field;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.fiberhome.mapps.intergration.session.SessionContext;

@SuppressWarnings("rawtypes")
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class EcidUpdateInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object parameter = invocation.getArgs()[1];
		
		Field ecidField = null;		
		Field[] fields = parameter.getClass().getDeclaredFields();
		
		for (Field field : fields) {
			if (!(field.getType().equals(String.class)) ) {
				continue;
			}
			if ("ecid".equals(field.getName())){
				ecidField = field;
				break;
			} else if (field.isAnnotationPresent(Ecid.class)) {
				ecidField = field;
				break;
			}
		}
		
		if (ecidField != null) {
			String ecid = SessionContext.getEcId();
			
			if (ecid != null) {
				BeanUtils.setProperty(parameter, ecidField.getName(), ecid);
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
