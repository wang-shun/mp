package com.fiberhome.mapps.survey.utils;

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

    public static final String              CODE_300001 = "300001";

    public static final String              CODE_300002 = "300002";

    public static final String              CODE_300003 = "300003";

    @SuppressWarnings("serial")
    public final static Map<String, String> errorMap    = new HashMap<String, String>()
                                                        {
                                                            {
                                                                put(CODE_100001, "数据处理失败");
                                                                put(CODE_100002, "图片存储失败");
                                                                put(CODE_300001, "问卷已关闭，请返回！");
                                                                put(CODE_300002, "无权填写问卷，请返回！");
                                                                put(CODE_300003, "已经完成问卷，请返回");
                                                            }
                                                        };

    public static void fail(BaseResponse response, String code)
    {
        response.setCode(code);
        response.setError_message(errorMap.get(code));
    }
}
