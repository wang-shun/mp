/**
 * @Title: ClientUtil.java
 * @Description: API 客户端工具类
 * 
 */
package com.fiberhome.mos.core.openapi.rop.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.AbstractResource;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @ClassName: ClientUtil
 * @Description: API 客户端工具类
 * 
 */

public class ClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientUtil.class);
    
    private static final ResourceConvertor resConvertor = new ResourceConvertor();

    private ClientUtil(){
        
    }
    
    /***
     * 获取不需要签名的属性
     * 
     * @param param
     * @return
     */
    public static List<String> getIgnoreSignFieldNames(RequestParams param) {
        LOGGER.debug("获取不需要签名的属性。");
        List<String> ignoreSignList = new ArrayList<String>();
        if (!param.isIgnoreSign()) {
            List<RequestParam> list = param.getParams();
            for (RequestParam par : list) {
                if (par.isIgnoreSign()) {
                    ignoreSignList.add(par.getKey());
                }
            }
        } else {
            List<RequestParam> list = param.getParams();
            for (RequestParam par : list) {
                ignoreSignList.add(par.getKey());
            }
        }

        if (ignoreSignList.size() > 1 && LOGGER.isDebugEnabled()) {
            LOGGER.debug("不需要签名的属性:" + ignoreSignList.toString());
        }
        return ignoreSignList;
    }

    /***
     * 将参数转为 Map
     * 
     * @param param
     * @return
     */
    public static Map<String, Object> toParamValueMap(RequestParams param) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        List<RequestParam> list = param.getParams();
        for (RequestParam para : list) {
        	ResourceConvertor convertor = getConvertor(para.getType());
            Object fieldValue = para.getValue();
            if (fieldValue != null) {
                if (convertor != null) {// 有对应转换器
                    paramMap.put(para.getKey(), convertor.unconvert((AbstractResource)fieldValue));
                } else {
                    paramMap.put(para.getKey(), fieldValue.toString());
                }
            }
        }
        return paramMap;
    }

    private static ResourceConvertor getConvertor(Class<?> fieldType) {
    	if (ClassUtils.isAssignable(AbstractResource.class, fieldType) ) {
    		return resConvertor;
    	}
        return null;
    }

    public static Map<String, Object> getParamFields(RequestParams param) {
        return toParamValueMap(param);
    }

    /**
     * 对请求参数进行签名
     * 
     * @param ignoreFieldNames
     * @param appSecret
     * @param form
     * @return
     */
    public static String sign(List<String> ignoreFieldNames, String appSecret, Map<String, String> form) {
        return RopUtils.sign(form, ignoreFieldNames, appSecret);
    }

    /***
     * 将 Map 转换为 MultiValueMap
     * 
     * @param form
     * @return
     */
    public static MultiValueMap<String, Object> toMultiValueMap(Map<String, Object> form) {
        MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<String, Object>();
        for (Map.Entry<String, Object> entry : form.entrySet()) {
            mvm.add(entry.getKey(), entry.getValue());
        }
        return mvm;
    }
    
}
