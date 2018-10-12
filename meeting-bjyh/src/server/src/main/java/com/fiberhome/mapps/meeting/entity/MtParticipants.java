package com.fiberhome.mapps.meeting.entity;

import javax.persistence.*;

@Table(name = "mt_participants")
public class MtParticipants {
    @Id
    private String id;

    @Column(name = "meeting_id")
    private String meetingId;

    /**
     * inner：内部人员 outer：外部人员
     */
    @Column(name = "person_type")
    private String personType;

    /**
     * 内容人员为用户id，外部人员为手机号
     */
    @Column(name = "person_id")
    private String personId;

    @Column(name = "person_name")
    private String personName;

    /**
     * 签到二维码图片的path
     */
    private String qrcode;

    /**
     * 0：未通知 1：已发送 2：已送达
     */
    @Column(name = "notice_status")
    private String noticeStatus;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return meeting_id
     */
    public String getMeetingId() {
        return meetingId;
    }

    /**
     * @param meetingId
     */
    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    /**
     * 获取inner：内部人员 outer：外部人员
     *
     * @return person_type - inner：内部人员 outer：外部人员
     */
    public String getPersonType() {
        return personType;
    }

    /**
     * 设置inner：内部人员 outer：外部人员
     *
     * @param personType inner：内部人员 outer：外部人员
     */
    public void setPersonType(String personType) {
        this.personType = personType;
    }

    /**
     * 获取内容人员为用户id，外部人员为手机号
     *
     * @return person_id - 内容人员为用户id，外部人员为手机号
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * 设置内容人员为用户id，外部人员为手机号
     *
     * @param personId 内容人员为用户id，外部人员为手机号
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * @return person_name
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * @param personName
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /**
     * 获取签到二维码图片的path
     *
     * @return qrcode - 签到二维码图片的path
     */
    public String getQrcode() {
        return qrcode;
    }

    /**
     * 设置签到二维码图片的path
     *
     * @param qrcode 签到二维码图片的path
     */
    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    /**
     * 获取0：未通知 1：已发送 2：已送达
     *
     * @return notice_status - 0：未通知 1：已发送 2：已送达
     */
    public String getNoticeStatus() {
        return noticeStatus;
    }

    /**
     * 设置0：未通知 1：已发送 2：已送达
     *
     * @param noticeStatus 0：未通知 1：已发送 2：已送达
     */
    public void setNoticeStatus(String noticeStatus) {
        this.noticeStatus = noticeStatus;
    }
}