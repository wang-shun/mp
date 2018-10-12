package com.fiberhome.mapps.meeting.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "mr_favorite")
public class MrFavorite
{
    @Id
    private String id;

    private String ecid;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 会议室ID
     */
    @Column(name = "room_id")
    private String roomId;

    /**
     * 收藏时间
     */
    @Column(name = "fav_time")
    private Date   favTime;

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
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * 获取会议室ID
     *
     * @return room_id - 会议室ID
     */
    public String getRoomId()
    {
        return roomId;
    }

    /**
     * 设置会议室ID
     *
     * @param roomId 会议室ID
     */
    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
    }

    /**
     * 获取收藏时间
     *
     * @return fav_time - 收藏时间
     */
    public Date getFavTime()
    {
        return favTime;
    }

    /**
     * 设置收藏时间
     *
     * @param favTime 收藏时间
     */
    public void setFavTime(Date favTime)
    {
        this.favTime = favTime;
    }
}