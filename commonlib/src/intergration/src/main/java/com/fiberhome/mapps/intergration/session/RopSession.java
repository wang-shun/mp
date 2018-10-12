package com.fiberhome.mapps.intergration.session;

import com.fiberhome.mapps.intergration.security.sso.UserInfo;

public class RopSession implements com.rop.session.Session {
	private String sessionId;

	private UserInfo user;

	private boolean mobile;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public boolean isMobile() {
		return mobile;
	}

	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}
}
