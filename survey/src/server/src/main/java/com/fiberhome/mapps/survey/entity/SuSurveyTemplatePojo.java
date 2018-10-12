package com.fiberhome.mapps.survey.entity;

import java.util.Date;

import com.fiberhome.mapps.survey.utils.DateUtil;

public class SuSurveyTemplatePojo
{
    private String id;

    private String ecid;

    private String title;

    private String groupId;

    private String groupName;

    private String type;

    private String owner;

    private String pager;

    private String status;

    private Long   useTimes;

    private Long   questions;

    private Date   modifiedTime;

    private String modifiedTimeStr;

    /**
     * @return id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return ecid
     */
    public String getEcid()
    {
        return ecid;
    }

    /**
     * @param ecid
     */
    public void setEcid(String ecid)
    {
        this.ecid = ecid;
    }

    /**
     * @return title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * ��ȡ����ģ��û�з���
     *
     * @return group_id - ����ģ��û�з���
     */
    public String getGroupId()
    {
        return groupId;
    }

    /**
     * ���ø���ģ��û�з���
     *
     * @param groupId
     *            ����ģ��û�з���
     */
    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    /**
     * ��ȡ1 ����ģ�� 2 ����ģ��
     *
     * @return type - 1 ����ģ�� 2 ����ģ��
     */
    public String getType()
    {
        return type;
    }

    /**
     * ����1 ����ģ�� 2 ����ģ��
     *
     * @param type
     *            1 ����ģ�� 2 ����ģ��
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * ��ȡ���ǹ���ģ�壬�ǿ�Ϊ��Ӧ�û�id��ģ��
     *
     * @return owner - ���ǹ���ģ�壬�ǿ�Ϊ��Ӧ�û�id��ģ��
     */
    public String getOwner()
    {
        return owner;
    }

    /**
     * ���ÿ��ǹ���ģ�壬�ǿ�Ϊ��Ӧ�û�id��ģ��
     *
     * @param owner
     *            ���ǹ���ģ�壬�ǿ�Ϊ��Ӧ�û�id��ģ��
     */
    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    /**
     * ��ȡ���ŷָ�����ʾ��ҳ���ڵ�N��������棨����ͳ�Ʋ����ҳ��
     *
     * @return pager - ���ŷָ�����ʾ��ҳ���ڵ�N��������棨����ͳ�Ʋ����ҳ��
     */
    public String getPager()
    {
        return pager;
    }

    /**
     * ���ö��ŷָ�����ʾ��ҳ���ڵ�N��������棨����ͳ�Ʋ����ҳ��
     *
     * @param pager
     *            ���ŷָ�����ʾ��ҳ���ڵ�N��������棨����ͳ�Ʋ����ҳ��
     */
    public void setPager(String pager)
    {
        this.pager = pager;
    }

    /**
     * @return status
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * @return use_times
     */
    public Long getUseTimes()
    {
        return useTimes;
    }

    /**
     * @param useTimes
     */
    public void setUseTimes(Long useTimes)
    {
        this.useTimes = useTimes;
    }

    /**
     * @return questions
     */
    public Long getQuestions()
    {
        return questions;
    }

    /**
     * @param questions
     */
    public void setQuestions(Long questions)
    {
        this.questions = questions;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public Date getModifiedTime()
    {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime)
    {
        this.modifiedTime = modifiedTime;
        if (modifiedTime != null)
        {
            this.modifiedTimeStr = DateUtil.sdfHM().format(modifiedTime);
        }
    }

    public String getModifiedTimeStr()
    {
        return modifiedTimeStr;
    }

    public void setModifiedTimeStr(String modifiedTimeStr)
    {
        this.modifiedTimeStr = modifiedTimeStr;
    }

}