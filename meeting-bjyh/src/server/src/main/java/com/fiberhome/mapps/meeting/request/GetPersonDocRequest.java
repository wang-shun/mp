package com.fiberhome.mapps.meeting.request;

import com.rop.AbstractRopRequest;

public class GetPersonDocRequest extends AbstractRopRequest
{
    private String searchParam;

    private String folderId;
    /**
     * 只查询出fileIds参数里的数据 功能控制开关 1开启 0关闭
     */
    private String searchExistFlag;
    /**
     * 文件唯一标示 a,b,c
     */
    private String fileIds;

    public String getFolderId()
    {
        return folderId;
    }

    public void setFolderId(String folderId)
    {
        this.folderId = folderId;
    }

    public String getSearchParam()
    {
        return searchParam;
    }

    public void setSearchParam(String searchParam)
    {
        this.searchParam = searchParam;
    }

    public String getSearchExistFlag()
    {
        return searchExistFlag;
    }

    public void setSearchExistFlag(String searchExistFlag)
    {
        this.searchExistFlag = searchExistFlag;
    }

    public String getFileIds()
    {
        return fileIds;
    }

    public void setFileIds(String fileIds)
    {
        this.fileIds = fileIds;
    }
}
