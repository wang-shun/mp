/**
 * 版权声明：中图一购网络科技有限公司 版权所有 违者必究 2012 日 期：12-7-17
 */
package com.fiberhome.mapps.intergration.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fiberhome.mapps.intergration.security.sso.UserInfo;
import com.fiberhome.mapps.intergration.security.sso.Validator;
import com.rop.session.Session;
import com.rop.session.SessionManager;
import com.rop.session.SimpleSession;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author hlj
 * @version 1.0
 */
public class DefaultSessionManager implements SessionManager {
	private static Logger LOG = LoggerFactory.getLogger(DefaultSessionManager.class);

	@Override
	public void addSession(String sessionId, Session session) {
	}

	@Override
	public Session getSession(String sessionId) {
		RopSession ropSession = SessionContext.get();
		
		if(ropSession == null) {
//			LOG.debug("从上下文中获取Session: {}失败，session不存在", sessionId);
			return null;
		}
		
		String ropSessionId = ropSession.getSessionId();
		if (!sessionId.equals(ropSessionId)) {
			LOG.warn("Session id isn't match, request: {}, session:{}", sessionId, ropSessionId);
			return null;
		}
		UserInfo user = ropSession.getUser();
		if(user != null) {
			SimpleSession ss = new SimpleSession();
			// 设置SessionID
			ss.setAttribute("sessionId", sessionId);
			ss.setAttribute("user", user);
			return ss;
		}
		
		return null;
	}

	@Override
	public void removeSession(String sessionId) {
	}
}
