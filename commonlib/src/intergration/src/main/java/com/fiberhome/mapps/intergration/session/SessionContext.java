package com.fiberhome.mapps.intergration.session;

import com.fiberhome.mapps.intergration.security.sso.DeptInfo;
import com.fiberhome.mapps.intergration.security.sso.UserInfo;
import com.fiberhome.mapps.intergration.security.sso.UserInfo14;

public final class SessionContext {
	private final static ThreadLocal<RopSession> SESSION = new ThreadLocal<RopSession>();
	
	
	private String deptScopeType;
	
	
	private DeptInfo[] deptScope;
	
	
	private DeptInfo dept;
	
	/**
	 *  上级所属的机构级部门信息, null时为机构
	 */
	private DeptInfo orgDept;
	
	
	public static void set(RopSession session) {
		SESSION.set(session);
	}
	
	public static RopSession get() {
		return SESSION.get();
	}
	
	public static void clear() {
		SESSION.remove();
	}
	
	public static String getUserId() {
		UserInfo user = getUser();
		return user != null ? user.getLoginId() : null;
	}
	
	public static String getUserUuid() {
		UserInfo user = getUser();
		return user != null ? user.getUserId() : null;
	}
	
	public static String getUserName() {
		UserInfo user = getUser();
		return user != null ? user.getUserName() : null;
	}
	
	public static String getEcId() {
		UserInfo user = getUser();
		return user != null ? user.getEcid() : null;
	}
	
	public static UserInfo getUser() {
		RopSession ss =  get();
		if (ss != null) {
			return ss.getUser();
		}
		return null;
	}
	
	public static UserInfo14 getUser14() {
		UserInfo userInfo = getUser();
		
		if (userInfo != null && userInfo instanceof UserInfo14) {
			return (UserInfo14)userInfo;
		} else {
			return null;
		}
	}
	
	public static String getOrgId() {
		UserInfo user = getUser();
		return user != null ? user.getOrgId() : null;
	}
	
	public static boolean isAdmin() {
		UserInfo user = getUser();
		return user != null ? user.isAdmin() : false;
	}
	
	public static String getDeptId() {
		UserInfo user = getUser();
		return user != null ? user.getDeptId() : null;
	}
	
	public static String getDeptName() {
		UserInfo user = getUser();
		return user != null ? user.getDeptName() : null;
	}
	
	public static String getDeptFullName() {
		UserInfo user = getUser();
		return user != null ? user.getDeptFullName() : null;
	}
	
	public static String getDeptOrder() {
		UserInfo user = getUser();
		return user != null ? user.getDeptOrder() : null;
	}
	
	public static String getOrgDeptOrder() {
		UserInfo14 user = getUser14();
		if(user != null){
			if(user.getOrgDept() == null){
				return null;
			}else{
				return user.getOrgDept().getDepOrder();
			}
		}else{
			return null;
		}
	}
	
	public static String getOrgDeptId() {
		UserInfo14 user = getUser14();
		if(user != null){
			if(user.getOrgDept() == null){
				return null;
			}else{
				return user.getOrgDept().getDepUuid();
			}
		}else{
			return null;
		}
	}

	/**
	 * 管理部门的属性，机构级部门还是普通部门，0-部门范围，1-机构范围
	 */
	public static String getDeptScopeType() {
		UserInfo14 u14 = getUser14();
		if (u14 != null) {
			return u14.getDeptScopeType();
		}
		return null;
	}

	/**
	 * 管理的部门范围，用户为应用管理员时所有的属性
	 */
	public static DeptInfo[] getDeptScope() {
		UserInfo14 u14 = getUser14();
		if (u14 != null) {
			return u14.getDeptScope();
		}
		return null;
	}

	/**
	 * 用户所在直属机构部门
	 */
	public static DeptInfo getDept() {
		UserInfo14 u14 = getUser14();
		if (u14 != null) {
			return u14.getDept();
		}
		return null;
	}

	/**
	 *  上级所属的机构级部门信息, null时为机构
	 */
	public static DeptInfo getOrgDept() {
		UserInfo14 u14 = getUser14();
		if (u14 != null) {
			return u14.getOrgDept();
		}
		return null;
	}
	
	
}
