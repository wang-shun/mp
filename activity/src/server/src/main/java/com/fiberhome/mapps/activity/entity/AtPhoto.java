package com.fiberhome.mapps.activity.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "AT_PHOTO")
public class AtPhoto {
    @Id
    @Column(name = "phone_id")
    private String phoneId;

    @Column(name = "act_id")
    private String actId;

    @Column(name = "phone_route")
    private String phoneRoute;

    private Long sort;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * @return phone_id
     */
    public String getPhoneId() {
        return phoneId;
    }

    /**
     * @param phoneId
     */
    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    /**
     * @return act_id
     */
    public String getActId() {
        return actId;
    }

    /**
     * @param actId
     */
    public void setActId(String actId) {
        this.actId = actId;
    }

    /**
     * @return phone_route
     */
    public String getPhoneRoute() {
        return phoneRoute;
    }

    /**
     * @param phoneRoute
     */
    public void setPhoneRoute(String phoneRoute) {
        this.phoneRoute = phoneRoute;
    }

    /**
     * @return sort
     */
    public Long getSort() {
        return sort;
    }

    /**
     * @param sort
     */
    public void setSort(Long sort) {
        this.sort = sort;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}