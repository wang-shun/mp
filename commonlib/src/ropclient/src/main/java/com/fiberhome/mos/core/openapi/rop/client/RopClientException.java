package com.fiberhome.mos.core.openapi.rop.client;

@SuppressWarnings("serial")
public class RopClientException extends Exception {
    public RopClientException(String msg) {
        super(msg);
    }

    public RopClientException(String msg, Throwable tr) {
        super(msg, tr);
    }
}
