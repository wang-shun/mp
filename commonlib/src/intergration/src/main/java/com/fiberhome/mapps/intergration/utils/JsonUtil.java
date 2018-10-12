package com.fiberhome.mapps.intergration.utils;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil
{
    /**
     * 根据JSON字符串返回map对象、要求传入的text形式：{"a":"1","b":"2","c":"3","4":"d"}
     * 
     * @param text
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapFromJsonStr(String text)
    {
        Map<String, Object> map = null;
        if (text != null && !text.equals("") && text.startsWith("{"))
        {
            map = (Map<String, Object>) JSONObject.parse(text);
        }
        return map;
    }

    /**
     * json字符串转换为java对象
     * 
     * @author wangyao
     * @param text json字符串
     * @param clazz 需要转换的类对象
     * @return
     */
    @SuppressWarnings(
    {
        "rawtypes", "unchecked"
    })
    public static Object jsonToObject(String text, Class clazz)
    {
        Object retObject = null;
        if (text.startsWith("["))
        {
            // 数组形式的json字符串
            List returnList = JSONArray.parseArray(text, clazz);
            retObject = returnList;
        }
        else if (text.startsWith("{"))
        {
            // 对象形式的json字符串
            retObject = JSONObject.parseObject(text, clazz);
        }
        return retObject;
    }
    
    public static String toJson(Object obj) {
    	return JSON.toJSONString(obj);
    }
}
