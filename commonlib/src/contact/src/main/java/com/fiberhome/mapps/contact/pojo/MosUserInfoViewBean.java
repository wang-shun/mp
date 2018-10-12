package com.fiberhome.mapps.contact.pojo;

import java.util.List;

public class MosUserInfoViewBean
{
    private String             code;
    private String             message;
    private List<UserInfoView> list;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<UserInfoView> getList()
    {
        return list;
    }

    public void setList(List<UserInfoView> list)
    {
        this.list = list;
    }

}
