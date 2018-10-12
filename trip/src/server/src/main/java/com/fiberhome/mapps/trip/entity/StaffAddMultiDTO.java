package com.fiberhome.mapps.trip.entity;

public class StaffAddMultiDTO {
	
	private Integer staffId ;

	private 	String	outStaffCode	;//		员工外部编码
	
	private 	String	mobile	;//	是	手机
	
	private 	String	realName	;//	是	真实姓名
	
	private 	String	idcard	;//		身份证号
	
	private 	String	birthday	;//		生日
	
	private 	String	nick	;//		昵称
	
	private 	Integer	position	;//		岗位1普通员工2主管3经理4总监100助理
	
	private 	String	email	;//		邮箱
	
	private 	String	gender	;//		性别
	
	private 	String	employeeId	;//		员工编码
	
	private 	Integer	payForOther	;//		代订权限（1：启用，2：禁用）默认：2

	private Integer levelId ;
	
	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public Integer getStaffId() {
		return staffId;
	}

	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}
	
	public String getOutStaffCode() {
		return outStaffCode;
	}

	public void setOutStaffCode(String outStaffCode) {
		this.outStaffCode = outStaffCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getPayForOther() {
		return payForOther;
	}

	public void setPayForOther(Integer payForOther) {
		this.payForOther = payForOther;
	}
	
	
}
