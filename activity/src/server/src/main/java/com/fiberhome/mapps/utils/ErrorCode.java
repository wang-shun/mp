package com.fiberhome.mapps.utils;

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
     * 报名截止时间不可以大于开始时间
     */
    public static final String              CODE_300004 = "300004";
    /**
     * 活动不存在
     */
    public static final String              CODE_300011 = "300011";
    /**
     * 时间格式解析异常 例yyyy-mm-dd hh24:mi
     */
    public static final String              CODE_300012 = "300012";
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
     * 创建群组失败
     */
    public static final String              CODE_300018 = "300018";
    /**
     * 活动报名人数已满
     */
    public static final String              CODE_300019 = "300019";
    /**
     * 已报名
     */
    public static final String              CODE_300020 = "300020";
    /**
     * 邮箱功能未配置
     */
    public static final String              CODE_300021 = "300021";
    /**
     * 活动报名已结束
     */
    public static final String              CODE_300022 = "300022";
    /**
     * 圈子功能未配置
     */
    public static final String              CODE_300023 = "300023";

    @SuppressWarnings("serial")
    public final static Map<String, String> errorMap    = new HashMap<String, String>()
                                                        {
                                                            {
                                                                put(CODE_100001, "数据处理失败");
                                                                put(CODE_100002, "图片上传失败");
                                                                put(CODE_300001, "日期格式解析异常");
                                                                put(CODE_300002, "时间格式解析异常");
                                                                put(CODE_300003, "开始时间不可以大于结束时间");
                                                                put(CODE_300004, "报名截止时间不可以大于开始时间");
                                                                put(CODE_300011, "活动不存在");
                                                                put(CODE_300012, "时间格式解析异常 例:yyyy-mm-dd hh24:mi");
                                                                put(CODE_300014, "请选择上传的文件");
                                                                put(CODE_300015, "上传的文件大小超过限制");
                                                                put(CODE_300016, "不支持的文件格式");
                                                                put(CODE_300018, "创建群组失败");
                                                                put(CODE_300019, "活动报名人数已满");
                                                                put(CODE_300020, "您已报名");
                                                                put(CODE_300021, "邮箱功能未配置");
                                                                put(CODE_300022, "活动报名已结束");
                                                                put(CODE_300023, "圈子功能未配置");
                                                            }
                                                        };

    public static void fail(BaseResponse response, String code)
    {
        response.setCode(code);
        response.setError_message(errorMap.get(code));
    }
}
