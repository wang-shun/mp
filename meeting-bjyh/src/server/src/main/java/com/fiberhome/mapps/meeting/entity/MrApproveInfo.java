package com.fiberhome.mapps.meeting.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "mr_approve_info")
public class MrApproveInfo
{
    @Id
    @Column(name = "reserved_id")
    private String reservedId;

    @Id
    @Column(name = "user_id")
    private String userId;

    private String approved;

    @Column(name = "approve_time")
    private Date   approveTime;

    @Column(name = "approve_result")
    private String approveResult;

    public String getApproveResult()
    {
        return approveResult;
    }

    public void setApproveResult(String approveResult)
    {
        this.approveResult = approveResult;
    }

    /**
     * @return reserved_id
     */
    public String getReservedId()
    {
        return reservedId;
    }

    /**
     * @param reservedId
     */
    public void setReservedId(String reservedId)
    {
        this.reservedId = reservedId;
    }

    /**
     * @return user_id
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * @return approved
     */
    public String getApproved()
    {
        return approved;
    }

    /**
     * @param approved
     */
    public void setApproved(String approved)
    {
        this.approved = approved;
    }

    /**
     * @return approve_time
     */
    public Date getApproveTime()
    {
        return approveTime;
    }

    /**
     * @param approveTime
     */
    public void setApproveTime(Date approveTime)
    {
        this.approveTime = approveTime;
    }
}