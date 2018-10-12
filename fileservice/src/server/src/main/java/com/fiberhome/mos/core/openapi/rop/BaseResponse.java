package com.fiberhome.mos.core.openapi.rop;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class BaseResponse
{
    public final static String SUCCESS = "1";
    public final static String FAIL    = "0";
    
    /**
     * 返回结果状态码，1 成功，0 失败
     */
    @XmlElement
    private String             code = SUCCESS;
    /**
     * code为0时，错误信息描述
     */
    @XmlElement(name = "message")
    private String             error_message = "";

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getError_message()
    {
        return error_message;
    }
    
    public void setError_message(String error_message)
    {
        this.error_message = error_message;
    }

    /**
     * 设置成功状态
     */
    public void success()
    {
        this.code = SUCCESS;
    }

    /**
     * 设置失败状态
     */
    public void fail()
    {
        this.code = FAIL;
    }
    
    /**
     * 设置失败状态
     * 
     * @param errorMsg 设置错误消息
     */
    public void fail(String errorMsg)
    {
        this.code = FAIL;
        this.error_message = errorMsg;
    }
}