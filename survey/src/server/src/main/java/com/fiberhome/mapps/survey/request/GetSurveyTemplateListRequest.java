package com.fiberhome.mapps.survey.request;

import com.rop.AbstractRopRequest;

public class GetSurveyTemplateListRequest extends AbstractRopRequest
{
    private String title;

    // 1表示公共，2表示个人
    private String type;

    private String defaultid;

    private int    offset;

    private int    limit;

    private String sort;

    public String getDefaultid()
    {
        return defaultid;
    }

    public void setDefaultid(String defaultid)
    {
        this.defaultid = defaultid;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getOffset()
    {
        return offset;
    }

    public void setOffset(int offset)
    {
        this.offset = offset;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }

}
