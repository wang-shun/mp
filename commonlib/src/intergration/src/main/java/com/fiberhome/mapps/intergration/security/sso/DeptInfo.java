package com.fiberhome.mapps.intergration.security.sso;

/**
 * depUuid	String	部门唯一标识</br>
 * depName	String	部门名称</br>
 * parentId	String	上级部门唯一标识</br>
 * email	String	部门邮箱地址</br>
 * depWeight	Integer	部门权重，默认99999999</br>
 * updateTime	long	修改时间</br>
 * mode	Integer	授权方式（0：继承，1：独立授权）</br>
 * depOrder	String	部门序列，4位表示层级，如0006001000800044
 * @author Administrator
 *
 */
public class DeptInfo {
	
	private String depUuid;
	
	private String depName;
	
	private String parentId;
	
	private String email;
	
	private int depWeight;
	
	private long updateTime;
	
	private int mode;
	
	private String depOrder;

	public String getDepUuid() {
		return depUuid;
	}

	public void setDepUuid(String depUuid) {
		this.depUuid = depUuid;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getDepWeight() {
		return depWeight;
	}

	public void setDepWeight(int depWeight) {
		this.depWeight = depWeight;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getDepOrder() {
		return depOrder;
	}

	public void setDepOrder(String depOrder) {
		this.depOrder = depOrder;
	} 
	
	public String toString() {
		return "";
	}
}
