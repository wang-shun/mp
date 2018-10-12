package com.fiberhome.mapps.meetingroom.utils;

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
     * 文件上传失败
     */
    public static final String              CODE_100002 = "100002";
    /**
     * 日期格式解析异常 例2017-06-05
     */
    public static final String              CODE_300001 = "300001";
    /**
     * 时间格式解析异常 例08:30
     */
    public static final String              CODE_300002 = "300002";
    /**
     * 开始时间不可以大于结束时间
     */
    public static final String              CODE_300003 = "300003";
    /**
     * 会议室名称重复
     */
    public static final String              CODE_300004 = "300004";
    /**
     * 会议室处于预定中
     */
    public static final String              CODE_300005 = "300005";
    /**
     * 预约时间无效，请选择未来的时间
     */
    public static final String              CODE_300006 = "300006";
    /**
     * 该会议室已被预定
     */
    public static final String              CODE_300007 = "300007";
    /**
     * 该会议室已被收藏
     */
    public static final String              CODE_300008 = "300008";
    /**
     * 该会议室未被收藏
     */
    public static final String              CODE_300009 = "300009";
    /**
     * 会议室预约状态已变更
     */
    public static final String              CODE_300010 = "300010";
    /**
     * 会议室不存在
     */
    public static final String              CODE_300011 = "300011";

    /**
     * 时间格式解析异常 例yyyy-mm-dd hh24:mi
     */
    public static final String              CODE_300012 = "300012";

    /**
     * 此预约不存在
     */
    public static final String              CODE_300013 = "300013";
    /**
     * 请选择上传的文件
     */
    public static final String              CODE_300014 = "300014";
    /**
     * 上传的文件大小超过限制
     */
    public static final String              CODE_300015 = "300015";
    /**
     * 不支持的文件格式
     */
    public static final String              CODE_300016 = "300016";
    /**
     * 预约时间已过期
     */
    public static final String              CODE_300017 = "300017";

    @SuppressWarnings("serial")
    public final static Map<String, String> errorMap    = new HashMap<String, String>()
                                                        {
                                                            {
                                                                put(CODE_100001, "数据处理失败");
                                                                put(CODE_100002, "图片上传失败");
                                                                put(CODE_300001, "日期格式解析异常");
                                                                put(CODE_300002, "时间格式解析异常");
                                                                put(CODE_300003, "开始时间不可以大于结束时间");
                                                                put(CODE_300004, "会议室名称重复");
                                                                put(CODE_300005, "会议室处于预定中");
                                                                put(CODE_300006, "预约时间无效");
                                                                put(CODE_300007, "该会议室已被预定");
                                                                put(CODE_300008, "该会议室已被收藏");
                                                                put(CODE_300009, "该会议室未被收藏");
                                                                put(CODE_300010, "会议室预约状态已变更");
                                                                put(CODE_300011, "会议室不存在");
                                                                put(CODE_300012, "时间格式解析异常 例:yyyy-mm-dd hh24:mi");
                                                                put(CODE_300013, "此预约不存在");
                                                                put(CODE_300014, "请选择上传的文件");
                                                                put(CODE_300015, "上传的文件大小超过限制");
                                                                put(CODE_300016, "不支持的文件格式");
                                                                put(CODE_300017, "预约时间已过期");
                                                            }
                                                        };

    public static void fail(BaseResponse response, String code)
    {
        response.setCode(code);
        response.setError_message(errorMap.get(code));
    }
}
