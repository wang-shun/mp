package com.fiberhome.mapps.activity.entity;

public class BaseOpLogContent
{
    private String message;

    public String toString()
    {
        return "message=" + this.getMessage();
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}