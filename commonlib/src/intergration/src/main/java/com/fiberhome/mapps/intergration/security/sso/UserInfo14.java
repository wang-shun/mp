package com.fiberhome.mapps.intergration.security.sso;

public class UserInfo14 extends UserInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8917798846238994098L;
	
	
	public final static String DEPT_SCOPE_DEP = "0";
	public final static String DEPT_SCOPE_ORG = "1";
	
	/**
	 * 管理部门的属性，机构级部门还是普通部门，0-部门范围，1-机构范围
	 */
	private String deptScopeType;
	
	/**
	 * 管理的部门范围，用户为应用管理员时所有的属性
	 */
	private DeptInfo[] deptScope;
	
	/**
	 * 用户所在直属机构部门
	 */
	private DeptInfo dept;
	
	/**
	 *  上级所属的机构级部门信息, null时为机构
	 */
	private DeptInfo orgDept;

	public String getDeptScopeType() {
		return deptScopeType;
	}

	public void setDeptScopeType(String deptScopeType) {
		this.deptScopeType = deptScopeType;
	}

	public DeptInfo[] getDeptScope() {
		return deptScope;
	}

	public void setDeptScope(DeptInfo[] deptScope) {
		this.deptScope = deptScope;
	}

	public DeptInfo getDept() {
		return dept;
	}

	public void setDept(DeptInfo dept) {
		this.dept = dept;
	}

	public DeptInfo getOrgDept() {
		return orgDept;
	}

	public void setOrgDept(DeptInfo orgDept) {
		this.orgDept = orgDept;
	}
	
}
