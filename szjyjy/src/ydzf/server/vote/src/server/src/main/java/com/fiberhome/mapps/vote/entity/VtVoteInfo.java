package com.fiberhome.mapps.vote.entity;

import java.util.Date;
import javax.persistence.*;

import com.fiberhome.mapps.intergration.mybatis.OrgDept;

@OrgDept(orgDepId = "orgDeptId", orgDeptOrder = "orgDeptOrder")
@Table(name = "vt_vote_info")
public class VtVoteInfo {
    /**
     * 投票id
     */
    @Id
    private String id;

    /**
     * 所属机构id
     */
    @Column(name = "org_id")
    private String orgId;

    /**
     * 投票标题
     */
    private String title;

    /**
     * 投票内容描述
     */
    private String content;

    /**
     * 投票图片id
     */
    private String image;

    /**
     * 是否多选:1是,0否,默认1
     */
    private String multiple;

    /**
     * 如果是多选时,最多选几项
     */
    @Column(name = "max_choose")
    private Long maxChoose;

    /**
     * 是否匿名投票:1是,0否,默认0(不记名投票)
     */
    private String anonymous;

    /**
     * 投票截止日期
     */
    private Date expire;

    /**
     * 浏览阅读数
     */
    @Column(name = "read_count")
    private Long readCount;

    /**
     * 创建人id
     */
    private String creator;

    /**
     * 创建人名称
     */
    @Column(name = "creator_name")
    private String creatorName;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 状态:0无效,1有效
     */
    private String state;

    /**
     * 是否已过期,0表示未过期,1表示已过期,每天凌晨定时刷新
     */
    @Column(name = "is_expired")
    private String isExpired;
    
    
    @Column(name = "org_dept_id")
    private String       orgDeptId;     //所属部门机构id
    @Column(name = "org_dept_order")
    private String       orgDeptOrder; //所属部门机构order
    
    public String getOrgDeptId() {
		return orgDeptId;
	}

	public void setOrgDeptId(String orgDeptId) {
		this.orgDeptId = orgDeptId;
	}

	public String getOrgDeptOrder() {
		return orgDeptOrder;
	}

	public void setOrgDeptOrder(String orgDeptOrder) {
		this.orgDeptOrder = orgDeptOrder;
	}
    
    

    /**
     * 获取投票id
     *
     * @return id - 投票id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置投票id
     *
     * @param id 投票id
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
     * 获取投票标题
     *
     * @return title - 投票标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置投票标题
     *
     * @param title 投票标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取投票内容描述
     *
     * @return content - 投票内容描述
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置投票内容描述
     *
     * @param content 投票内容描述
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取投票图片id
     *
     * @return image - 投票图片id
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置投票图片id
     *
     * @param image 投票图片id
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 获取是否多选:1是,0否,默认1
     *
     * @return multiple - 是否多选:1是,0否,默认1
     */
    public String getMultiple() {
        return multiple;
    }

    /**
     * 设置是否多选:1是,0否,默认1
     *
     * @param multiple 是否多选:1是,0否,默认1
     */
    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    /**
     * 获取如果是多选时,最多选几项
     *
     * @return max_choose - 如果是多选时,最多选几项
     */
    public Long getMaxChoose() {
        return maxChoose;
    }

    /**
     * 设置如果是多选时,最多选几项
     *
     * @param maxChoose 如果是多选时,最多选几项
     */
    public void setMaxChoose(Long maxChoose) {
        this.maxChoose = maxChoose;
    }

    /**
     * 获取是否匿名投票:1是,0否,默认0(不记名投票)
     *
     * @return anonymous - 是否匿名投票:1是,0否,默认0(不记名投票)
     */
    public String getAnonymous() {
        return anonymous;
    }

    /**
     * 设置是否匿名投票:1是,0否,默认0(不记名投票)
     *
     * @param anonymous 是否匿名投票:1是,0否,默认0(不记名投票)
     */
    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    /**
     * 获取投票截止日期
     *
     * @return expire - 投票截止日期
     */
    public Date getExpire() {
        return expire;
    }

    /**
     * 设置投票截止日期
     *
     * @param expire 投票截止日期
     */
    public void setExpire(Date expire) {
        this.expire = expire;
    }

    /**
     * 获取浏览阅读数
     *
     * @return read_count - 浏览阅读数
     */
    public Long getReadCount() {
        return readCount;
    }

    /**
     * 设置浏览阅读数
     *
     * @param readCount 浏览阅读数
     */
    public void setReadCount(Long readCount) {
        this.readCount = readCount;
    }

    /**
     * 获取创建人id
     *
     * @return creator - 创建人id
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人id
     *
     * @param creator 创建人id
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取创建人名称
     *
     * @return creator_name - 创建人名称
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * 设置创建人名称
     *
     * @param creatorName 创建人名称
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     * 获取创建日期
     *
     * @return create_time - 创建日期
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取状态:0无效,1有效
     *
     * @return state - 状态:0无效,1有效
     */
    public String getState() {
        return state;
    }

    /**
     * 设置状态:0无效,1有效
     *
     * @param state 状态:0无效,1有效
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取是否已过期,0表示未过期,1表示已过期,每天凌晨定时刷新
     *
     * @return is_expired - 是否已过期,0表示未过期,1表示已过期,每天凌晨定时刷新
     */
    public String getIsExpired() {
        return isExpired;
    }

    /**
     * 设置是否已过期,0表示未过期,1表示已过期,每天凌晨定时刷新
     *
     * @param isExpired 是否已过期,0表示未过期,1表示已过期,每天凌晨定时刷新
     */
    public void setIsExpired(String isExpired) {
        this.isExpired = isExpired;
    }
}