package com.fiberhome.mapps.trip.utils;

import java.lang.reflect.Field;

public class LogUtil
{
    @SuppressWarnings("rawtypes")
    public static String getObjectInfo(Object obj)
    {
        try
        {
            StringBuffer sb = new StringBuffer();
            // 得到类对象
            Class userCla = (Class) obj.getClass();
            /*
             * 得到类中的所有属性集合
             */
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++)
            {
                Field f = fs[i];
                f.setAccessible(true); // 设置些属性是可以访问的
                Object val = f.get(obj);// 得到此属性的值
                sb.append(f.getName() + " : " + val + ";");
            }
            return sb.toString();
        }
        catch (Exception e)
        {

        }
        return "";
    }
}
