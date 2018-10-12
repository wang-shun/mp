package com.fiberhome.mos.core.openapi.rop.client;

import java.util.List;

/**
 * @ClassName: RequestParams
 * @Description: API 参数
 * 
 */

public class RequestParams {

    /***
     * 参数集合
     */
    private List<RequestParam> params;

    /***
     * 参数集合是否需要签名
     */
    private boolean          ignoreSign;

    public RequestParams(List<RequestParam> params) {
        this.params = params;
        this.ignoreSign = false;
    }

    public RequestParams(List<RequestParam> params, boolean ignoreSign) {
        this.params = params;
        this.ignoreSign = ignoreSign;
    }

    public List<RequestParam> getParams() {
        return params;
    }

    public void setParams(List<RequestParam> params) {
        this.params = params;
    }

    public boolean isIgnoreSign() {
        return ignoreSign;
    }

    public void setIgnoreSign(boolean ignoreSign) {
        this.ignoreSign = ignoreSign;
    }
}
