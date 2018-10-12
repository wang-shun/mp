package com.fiberhome.mapps.meeting.entity;

import javax.persistence.*;

@Table(name = "MT_SIGNIN_SERV")
public class MtSigninServ {
    @Id
    private String id;

    @Column(name = "meeting_id")
    private String meetingId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

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
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return user_name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}