package com.fiberhome.mapps.servicemanager.utils;

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
     * 创建数据库失败
     */
    public static final String              CODE_180001 = "180001";
    /**
     * 数据库用户已存在
     */
    public static final String              CODE_180002 = "180002";
    
    /**
     * 数据库连接测试失败
     */
    public static final String              CODE_180003 = "180003";
    /**
     * 路由路径已经存在或冲突
     */
    public static final String              CODE_300010 = "300010";
    /**
     * 服务已经添加路由
     */
    public static final String              CODE_300011 = "300011";
    /**
     * 服务未注册,请添加注册路由
     */
    public static final String              CODE_300012 = "300012";
    /**
     * redis数据库测试失败
     */
    public static final String              CODE_300013 = "300013";
    /**
     * 值域已存在
     */
    public static final String              CODE_300050 = "300050";
    /**
     * 采样指标已存在
     */
    public static final String              CODE_300051 = "300051";
    /**
     * 保留策略已存在
     */
    public static final String              CODE_300052 = "300052";
    /**
     * 指标已存在
     */
    public static final String              CODE_300053 = "300053";
    /**
     * 缺省策略无法删除
     */
    public static final String              CODE_300054 = "300054";
    /**
     * 缺省策略无法更改缺省状态
     */
    public static final String              CODE_300055 = "300055";
    /**
     * 资源名称已存在
     */
    public static final String              CODE_300056 = "300056";
    /**
     * 预警规则名称已存在
     */
    public static final String              CODE_300057 = "300057";
    
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
                                                                put(CODE_180001, "创建数据库失败");
                                                                put(CODE_180002, "数据库用户已存在");
                                                                put(CODE_180003, "数据库连接测试失败");
                                                                put(CODE_300010, "路由路径已经存在或冲突");
                                                                put(CODE_300011, "服务已经添加路由");
                                                                put(CODE_300012, "服务未注册,请添加注册路由");
                                                                put(CODE_300013, "redis数据库测试失败");
                                                                put(CODE_300050, "值域已存在");
                                                                put(CODE_300051, "采样指标已存在");
                                                                put(CODE_300052, "保留策略已存在");
                                                                put(CODE_300053, "指标已存在");
                                                                put(CODE_300054, "缺省策略无法删除");
                                                                put(CODE_300055, "缺省策略无法更改缺省状态");
                                                                put(CODE_300056, "资源名称已存在");
                                                                put(CODE_300057, "预警规则名称已存在");
                                                            }
                                                        };

    public static void fail(BaseResponse response, String code)
    {
        response.setCode(code);
        response.setError_message(errorMap.get(code));
    }
}
