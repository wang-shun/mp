package com.fiberhome.mapps.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fiberhome.mapps.intergration.rop.BaseResponse;

public class ImErrorCode
{
    protected final Logger                  LOGGER    = LoggerFactory.getLogger(getClass());
    /**
     * 5001：IM安全码错误
     */
    public static final String              CODE_5001 = "5001";
    public static final String              MSG_5001  = "IM安全码错误";
    /**
     * 5002：媒体文件不存在
     */
    public static final String              CODE_5002 = "5002";
    public static final String              MSG_5002  = "媒体文件不存在";
    /**
     * 5003：请求格式不合法
     */
    public static final String              CODE_5003 = "5003";
    public static final String              MSG_5003  = "请求格式不合法";
    /**
     * 5004：超过系统容量限制
     */
    public static final String              CODE_5004 = "5004";
    public static final String              MSG_5004  = "超过系统容量限制";
    /**
     * 5005: 用户不存在
     */
    public static final String              CODE_5005 = "5005";
    public static final String              MSG_5005  = "用户不存在";
    /**
     * 5006: api_key不正确
     */
    public static final String              CODE_5006 = "5006";
    public static final String              MSG_5006  = "api_key不正确";
    /**
     * 5007: 群组不存在
     */
    public static final String              CODE_5007 = "5007";
    public static final String              MSG_5007  = "群组不存在";
    /**
     * 5008: 用户不在群组内
     */
    public static final String              CODE_5008 = "5008";
    public static final String              MSG_5008  = "用户不在群组内";
    /**
     * 5009: 读写文件失败
     */
    public static final String              CODE_5009 = "5009";
    public static final String              MSG_5009  = "读写文件失败 ";
    /**
     * 5010： 用户取消文档上传
     */
    public static final String              CODE_5010 = "5010";
    public static final String              MSG_5010  = "用户取消文档上传";
    /**
     * 5011： 超时
     */
    public static final String              CODE_5011 = "5011";
    public static final String              MSG_5011  = "超时";
    /**
     * 5012： 上传文件失败
     */
    public static final String              CODE_5012 = "5012";
    public static final String              MSG_5012  = "上传文件失败";
    /**
     * 5013： 下载文件失败
     */
    public static final String              CODE_5013 = "5013";
    public static final String              MSG_5013  = "下载文件失败";
    /**
     * 5014： 用户取消文档下载
     */
    public static final String              CODE_5014 = "5014";
    public static final String              MSG_5014  = "用户取消文档下载";
    /**
     * 5015： 数据已经被清理
     */
    public static final String              CODE_5015 = "5015";
    public static final String              MSG_5015  = "数据已经被清理";

    @SuppressWarnings("serial")
    public final static Map<String, String> errorMap  = new HashMap<String, String>()
                                                      {
                                                          {
                                                              put(CODE_5001, MSG_5001);
                                                              put(CODE_5002, MSG_5002);
                                                              put(CODE_5003, MSG_5003);
                                                              put(CODE_5004, MSG_5004);
                                                              put(CODE_5005, MSG_5005);
                                                              put(CODE_5006, MSG_5006);
                                                              put(CODE_5007, MSG_5007);
                                                              put(CODE_5008, MSG_5008);
                                                              put(CODE_5009, MSG_5009);
                                                              put(CODE_5010, MSG_5010);
                                                              put(CODE_5011, MSG_5011);
                                                              put(CODE_5012, MSG_5012);
                                                              put(CODE_5013, MSG_5013);
                                                              put(CODE_5014, MSG_5014);
                                                              put(CODE_5015, MSG_5015);
                                                          }
                                                      };

    public static void fail(BaseResponse response, String code)
    {
        response.setCode(code);
        response.setError_message(errorMap.get(code));
    }
}
