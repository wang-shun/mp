package com.fiberhome.mapps.meeting.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "mt_signin_sequ")
public class MtSigninSequ
{
    @Id
    private String id;

    @Column(name = "meeting_id")
    private String meetingId;

    private String remarks;

    /**
     * 升序
     */
    private Long   sequ;

    private Long   totalNum;

    private Long   signedNum;

    private String sequStr;

    public String getSequStr()
    {
        return sequStr;
    }

    public void setSequStr(String sequStr)
    {
        this.sequStr = sequStr;
    }

    public Long getTotalNum()
    {
        return totalNum;
    }

    public void setTotalNum(Long totalNum)
    {
        this.totalNum = totalNum;
    }

    public Long getSignedNum()
    {
        return signedNum;
    }

    public void setSignedNum(Long signedNum)
    {
        this.signedNum = signedNum;
    }

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
     * @return meeting_id
     */
    public String getMeetingId()
    {
        return meetingId;
    }

    /**
     * @param meetingId
     */
    public void setMeetingId(String meetingId)
    {
        this.meetingId = meetingId;
    }

    /**
     * @return remarks
     */
    public String getRemarks()
    {
        return remarks;
    }

    /**
     * @param remarks
     */
    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    /**
     * 获取升序
     *
     * @return sequ - 升序
     */
    public Long getSequ()
    {
        return sequ;
    }

    /**
     * 设置升序
     *
     * @param sequ 升序
     */
    public void setSequ(Long sequ)
    {
        this.sequ = sequ;
    }
}