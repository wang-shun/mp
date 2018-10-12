package com.fiberhome.mos.core.openapi.rop.client;

@SuppressWarnings("serial")
public class ClientException extends Exception {
    public ClientException(String msg) {
        super(msg);
    }

    public ClientException(String msg, Throwable tr) {
        super(msg, tr);
    }
}
