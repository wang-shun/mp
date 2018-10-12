package com.fiberhome.mapps.intergration.security.sso;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.fiberhome.mapps.intergration.utils.JsonUtil;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = -593329288706343820L;
	
	private String userId;
	
	private String loginId;
	
	private String userName;
	
	private String phoneNumber;
	
	private String emailAddress;
	
	private String orgId;
	
	private String ecid;
	
	private String orgName;
	
	private String[] role;
	
	private String deptId;
	
	private String deptName;
	
	private String deptFullName;
	
	private String deptOrder;
	
	private String userType;
	
	private boolean admin;
	
	// 头像url
	private String avatarUrl;

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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getEcid() {
		return ecid;
	}

	public void setEcid(String ecid) {
		this.ecid = ecid;
	}

	public String[] getRole() {
		return role;
	}

	public void setRole(String[] role) {
		this.role = role;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	} 	 
	
	public String toString() {
		return JsonUtil.toJson(this);
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getDeptFullName() {
		return deptFullName;
	}

	public void setDeptFullName(String deptFullName) {
		this.deptFullName = deptFullName;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getDeptOrder() {
		return deptOrder;
	}

	public void setDeptOrder(String deptOrder) {
		this.deptOrder = deptOrder;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
}
