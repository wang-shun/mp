/**
 * 日 期：12-2-13
 */
package com.fiberhome.mapps.intergration.security.priv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fiberhome.mapps.intergration.security.sso.UserInfo;
import com.rop.security.ServiceAccessController;
import com.rop.session.Session;
import com.rop.session.SimpleSession;

/**
 * <pre>
 * 功能说明：对调用的方法进行安全性检查
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class RopServiceAccessController implements ServiceAccessController {
	private static final Logger LOG = LoggerFactory.getLogger(RopServiceAccessController.class);

	@Autowired
	PrivilegeConfig privilegeConfig;

	/**
	 * 不做appKey验证，只做登录用户的method校验
	 */
	@Override
	public boolean isAppGranted(String appKey, String method, String version) {
		return true;
	}

	@Override
	public boolean isUserGranted(Session session, String method, String version) {
		LOG.debug("ROP进行权限判断Session={},method={},version={}", session, method, version);
		
		if (null == session) {
			LOG.debug("无session校验接口不需要验证权限。");
			return true;
		}

		// 判断权限
		UserInfo user = (UserInfo) ((SimpleSession) session).getAttribute("user");

		return privilegeConfig.hasPrivilege(user, method);
		// LogUtil.addActionLog("api", "api接口调用", "", "接口操作 method=" + method +
		// " version=" + version, method, true);

	}
}
