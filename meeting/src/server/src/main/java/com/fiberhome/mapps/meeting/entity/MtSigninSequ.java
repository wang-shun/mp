package com.fiberhome.mapps.meeting.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "MT_SIGNIN_SEQU")
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

    private String qrcode;

    public String getQrcode()
    {
        return qrcode;
    }

    public void setQrcode(String qrcode)
    {
        this.qrcode = qrcode;
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