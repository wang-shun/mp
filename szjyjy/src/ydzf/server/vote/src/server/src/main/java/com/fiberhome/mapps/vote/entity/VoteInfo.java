package com.fiberhome.mapps.vote.entity;

import java.util.Date;

public class VoteInfo
{
    /**
     * 投票id
     */
    private String  id;

    /**
     * 所属机构id
     */
    private String  orgId;

    /**
     * 投票标题
     */
    private String  title;

    /**
     * 投票内容描述
     */
    private String  content;

    /**
     * 投票图片id
     */
    private String  image;

    /**
     * 是否多选:1是,0否,默认1
     */
    private String  multiple;

    /**
     * 如果是多选时,最多选几项
     */
    private Long    maxChoose;

    /**
     * 是否匿名投票:1是,0否,默认0(不记名投票)
     */
    private String  anonymous;

    /**
     * 投票截止日期
     */
    private Date    expire;
    private String  expireStr;

    /**
     * 浏览阅读数
     */
    private Long    readCount;

    /**
     * 创建人id
     */
    private String  creator;

    /**
     * 创建人名称
     */
    private String  creatorName;

    /**
     * 创建日期
     */
    private Date    createTime;
    private String  createTimeStr;

    /**
     * 状态:0无效,1有效
     */
    private String  state;

    /**
     * 是否已过期,0表示未过期,1表示已过期,每天凌晨定时刷新
     */
    private boolean isExpired;    // 是否过期字段

    private int     voteCount;    // 参与投票的总数量,统计字段

    private boolean involved;     // 当前用户是否投过票,统计字段

    private int     scopeCount;   // 可参与该投票的总人数,统计字段

    public String getCreateTimeStr()
    {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr)
    {
        this.createTimeStr = createTimeStr;
    }

    public String getExpireStr()
    {
        return expireStr;
    }

    public void setExpireStr(String expireStr)
    {
        this.expireStr = expireStr;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getOrgId()
    {
        return orgId;
    }

    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getMultiple()
    {
        return multiple;
    }

    public void setMultiple(String multiple)
    {
        this.multiple = multiple;
    }

    public Long getMaxChoose()
    {
        return maxChoose;
    }

    public void setMaxChoose(Long maxChoose)
    {
        this.maxChoose = maxChoose;
    }

    public String getAnonymous()
    {
        return anonymous;
    }

    public void setAnonymous(String anonymous)
    {
        this.anonymous = anonymous;
    }

    public Date getExpire()
    {
        return expire;
    }

    public void setExpire(Date expire)
    {
        this.expire = expire;
    }

    public Long getReadCount()
    {
        return readCount;
    }

    public void setReadCount(Long readCount)
    {
        this.readCount = readCount;
    }

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public String getCreatorName()
    {
        return creatorName;
    }

    public void setCreatorName(String creatorName)
    {
        this.creatorName = creatorName;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public boolean isExpired()
    {
        return isExpired;
    }

    public void setExpired(boolean isExpired)
    {
        this.isExpired = isExpired;
    }

    public int getVoteCount()
    {
        return voteCount;
    }

    public void setVoteCount(int voteCount)
    {
        this.voteCount = voteCount;
    }

    public boolean isInvolved()
    {
        return involved;
    }

    public void setInvolved(boolean involved)
    {
        this.involved = involved;
    }

    public int getScopeCount()
    {
        return scopeCount;
    }

    public void setScopeCount(int scopeCount)
    {
        this.scopeCount = scopeCount;
    }
}