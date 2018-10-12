package com.fiberhome.mapps.vote.entity;

import javax.persistence.*;

@Table(name = "VT_VOTE_SCOPE")
public class VtVoteScope {
    /**
     * 投票信息id
     */
    @Id
    @Column(name = "vote_info_id")
    private String voteInfoId;

    /**
     * 所属机构id
     */
    @Id
    @Column(name = "org_id")
    private String orgId;

    /**
     * 可参与人id
     */
    @Id
    @Column(name = "user_id")
    private String userId;

    /**
     * 获取投票信息id
     *
     * @return vote_info_id - 投票信息id
     */
    public String getVoteInfoId() {
        return voteInfoId;
    }

    /**
     * 设置投票信息id
     *
     * @param voteInfoId 投票信息id
     */
    public void setVoteInfoId(String voteInfoId) {
        this.voteInfoId = voteInfoId;
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
     * 获取可参与人id
     *
     * @return user_id - 可参与人id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置可参与人id
     *
     * @param userId 可参与人id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}