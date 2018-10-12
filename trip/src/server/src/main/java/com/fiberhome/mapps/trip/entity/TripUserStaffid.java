
/**
 * @file  TripOrder.java
 * @brief 简单描述 福利管理对象VO
 *
 * - 版权: Copyright(C) 烽火星空通信发展有限有限公司(Starrysky)
 * - 功能: 本文件归属于【WelfarePackage】子系统【welfarepkg】模块
 *     -# 功能描述示例 福利管理对象VO
 * - 修订历史:
 * - --版本-----月/日/年---修订者-----------------------------------------
 * - v0.9.1.0    2016-08-04   wuj
 *     -# 修改 文件注释规范
 * - v0.9.0.0    2016-08-04   wuj
 *     -# 创建
 *
 * @bug TD-xx: 缺陷示例
 */
 
package com.fiberhome.mapps.trip.entity;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author  wuj
 */
@Table(name = "trip_user_staffid")
public class TripUserStaffid implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "staff_id")
    private Integer staffId;

    @Column(name = "create_date")
    private Date createDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getStaffId() {
		return staffId;
	}

	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
    
 
}

