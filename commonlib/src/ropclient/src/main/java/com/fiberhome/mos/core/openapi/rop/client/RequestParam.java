
package com.fiberhome.mos.core.openapi.rop.client;

/**
 * @ClassName: RequestParam
 * @Description: API 参数对象
 * 
 */

public class RequestParam {

    /**
     * 参数名称
     */
    private String  key;

    /**
     * 参数值
     */
    private Object  value;

    /**
     * 参数类型
     */
    private Class   type;

    /***
     * 对象是否有
     */
    private boolean isXmlRootElement;

    /**
     * 参数是否签名
     */
    private boolean ignoreSign;

    public RequestParam(String key, Object value) {
        this.key = key;
        this.value = value;
        if (value != null) {
        	this.type = value.getClass();
        } else {
        	this.type = String.class;
        }
        this.ignoreSign = false;
    }

    public RequestParam(String key, Object value, Class type) {
        this.key = key;
        this.value = value;
        this.type = type;
        this.ignoreSign = false;
    }

    public RequestParam(String key, Object value, boolean ignoreSign) {
        this.key = key;
        this.value = value;
        this.type = String.class;
        this.ignoreSign = ignoreSign;
    }

    public RequestParam(String key, Object value, Class type, boolean ignoreSign) {
        this.key = key;
        this.value = value;
        this.type = type;
        this.ignoreSign = ignoreSign;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isIgnoreSign() {
        return ignoreSign;
    }

    public void setIgnoreSign(boolean ignoreSign) {
        this.ignoreSign = ignoreSign;
    }

    public boolean isXmlRootElement() {
        return isXmlRootElement;
    }

    public void setXmlRootElement(boolean isXmlRootElement) {
        this.isXmlRootElement = isXmlRootElement;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
