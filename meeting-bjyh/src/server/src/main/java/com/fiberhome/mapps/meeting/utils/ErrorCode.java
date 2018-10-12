package com.fiberhome.mapps.meeting.utils;

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
     * 提交数据校验
     */
    public static final String              CODE_300001 = "300001";
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

    @SuppressWarnings("serial")
    public final static Map<String, String> errorMap    = new HashMap<String, String>()
                                                        {
                                                            {
                                                                put(CODE_100001, "数据处理失败");
                                                                put(CODE_100002, "图片上传失败");
                                                                put(CODE_300014, "请选择上传的文件");
                                                                put(CODE_300015, "上传的文件大小超过限制");
                                                                put(CODE_300016, "不支持的文件格式");
                                                                put(CODE_300018, "创建群组失败");
                                                            }
                                                        };

    public static void fail(BaseResponse response, String code)
    {
        response.setCode(code);
        response.setError_message(errorMap.get(code));
    }
}
