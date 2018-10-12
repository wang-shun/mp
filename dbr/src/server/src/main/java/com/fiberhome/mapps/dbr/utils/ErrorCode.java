package com.fiberhome.mapps.dbr.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

public class ErrorCode
{
    protected final Logger                  LOGGER      = LoggerFactory.getLogger(getClass());
    /**
     * 数据处理失败
     */
    public static final String              CODE_100001 = "100001";
    /**
     * 文件存储失败
     */
    public static final String              CODE_100002 = "100002";
    /**
     * 设备已存在
     */
    public static final String              CODE_300001 = "300001";
    /**
     * 设备未录入
     */
    public static final String              CODE_300002 = "300002";
    /**
     * 设备记录异常
     */
    public static final String              CODE_300003 = "300003";
    /**
     * 设备状态已变更
     */
    public static final String              CODE_300004 = "300004";
    /**
     * 您是设备管理员
     */
    public static final String              CODE_300005 = "300005";
    /**
     * 请归还给管理员
     */
    public static final String              CODE_300006 = "300006";
    /**
     * 您确认管理此设备吗？
     */
    public static final String              CODE_300007 = "300007";
    /**
     * 您确认借取此设备吗？
     */
    public static final String              CODE_300008 = "300008";
    /**
     * 您确认回收此设备吗？
     */
    public static final String              CODE_300009 = "300009";
    /**
     * 设备已被您借走
     */
    public static final String              CODE_300010 = "300010";

    @SuppressWarnings("serial")
    public final static Map<String, String> errorMap    = new HashMap<String, String>()
                                                        {
                                                            {
                                                                put(CODE_100001, "数据处理失败");
                                                                put(CODE_100002, "图片存储失败");
                                                                put(CODE_300001, "设备已存在");
                                                                put(CODE_300002, "设备未录入");
                                                                put(CODE_300003, "设备记录异常");
                                                                put(CODE_300004, "设备状态已变更");
                                                                put(CODE_300005, "您是设备管理员");
                                                                put(CODE_300006, "请归还给管理员");
                                                                put(CODE_300007, "您确认管理此设备吗？");
                                                                put(CODE_300008, "您确认借取此设备吗？");
                                                                put(CODE_300009, "您确认回收此设备吗？");
                                                                put(CODE_300010, "设备已被您借走");
                                                            }
                                                        };

    public static void fail(BaseResponse response, String code)
    {
        response.setCode(code);
        response.setError_message(errorMap.get(code));
    }
}
