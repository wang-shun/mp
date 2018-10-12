package com.fiberhome.mapps.trip.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.fiberhome.mapps.intergration.rop.BaseResponse;

public class TripRequestUtil {

	
	// Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map  
    public static Map<String, Object> transRopRequest2Map(Object obj) {  
  
        if(obj == null){  
            return null;  
        }          
        Map<String, Object> map = new HashMap<String, Object>();  
        try {  
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
            for (PropertyDescriptor property : propertyDescriptors) {  
                String key = property.getName();  
  
                // 过滤class属性  
                if (!key.equals("class") && !key.equals("ropRequestContext")) {  
                    // 得到property对应的getter方法  
                    Method getter = property.getReadMethod();  
                    Object value = getter.invoke(obj);  
                    if(value != null){                    	
                    	map.put(key, value);  
                    }
                }  
  
            }  
        } catch (Exception e) {  
            System.out.println("transBean2Map Error " + e);  
		}
        
		if (map.size() == 0) {
			return null;
		} else {
			return map;
		}
    } 
	
	public static void setErrorInfo(BaseResponse res,JSONObject result){
		if(result!= null){
			String msg = result.getString("msg");
			res.setError_message("接口调用失败:"+msg);
		}else{
			res.setError_message("接口调用失败！");
		}
		res.setCode("0");
	}
    
}
