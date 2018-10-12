package com.fiberhome.mapps.servicemanager.rop;

import com.rop.security.ServiceAccessController;
import com.rop.session.Session;

public class RopServiceAccessController implements ServiceAccessController {

	@Override
	public boolean isAppGranted(String appKey, String method, String version) {
		return true;
	}

	@Override
	public boolean isUserGranted(Session session, String method, String version) {
		return true;
	}

}
