package com.fiberhome.mapps.vote.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "VT_VOTE_ANSWER")
public class VtVoteAnswer {
    /**
     * 投选id
     */
    @Id
    private String id;

    /**
     * 所属机构id
     */
    @Column(name = "org_id")
    private String orgId;

    /**
     * 所投投票id
     */
    @Column(name = "vote_info_id")
    private String voteInfoId;

    /**
     * 所投投票项id
     */
    @Column(name = "vote_item_id")
    private String voteItemId;

    /**
     * 参与投票人id
     */
    private String creator;

    /**
     * 参与投票人名称
     */
    @Column(name = "creator_name")
    private String creatorName;

    /**
     * 投选时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 获取投选id
     *
     * @return id - 投选id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置投选id
     *
     * @param id 投选id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取所属机构id
     *
     * @return org_id - 所属机构id
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * 设置所属机构id
     *
     * @param orgId 所属机构id
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
     * 获取所投投票id
     *
     * @return vote_info_id - 所投投票id
     */
    public String getVoteInfoId() {
        return voteInfoId;
    }

    /**
     * 设置所投投票id
     *
     * @param voteInfoId 所投投票id
     */
    public void setVoteInfoId(String voteInfoId) {
        this.voteInfoId = voteInfoId;
    }

    /**
     * 获取所投投票项id
     *
     * @return vote_item_id - 所投投票项id
     */
    public String getVoteItemId() {
        return voteItemId;
    }

    /**
     * 设置所投投票项id
     *
     * @param voteItemId 所投投票项id
     */
    public void setVoteItemId(String voteItemId) {
        this.voteItemId = voteItemId;
    }

    /**
     * 获取参与投票人id
     *
     * @return creator - 参与投票人id
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置参与投票人id
     *
     * @param creator 参与投票人id
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取参与投票人名称
     *
     * @return creator_name - 参与投票人名称
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * 设置参与投票人名称
     *
     * @param creatorName 参与投票人名称
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     * 获取投选时间
     *
     * @return create_time - 投选时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置投选时间
     *
     * @param createTime 投选时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}