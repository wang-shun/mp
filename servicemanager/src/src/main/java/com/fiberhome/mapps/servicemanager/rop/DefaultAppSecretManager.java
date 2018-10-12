package com.fiberhome.mapps.servicemanager.rop;

import com.rop.security.AppSecretManager;

public class DefaultAppSecretManager implements AppSecretManager {

	@Override
	public String getSecret(String appKey) {
		return "FHuma025";
	}

	@Override
	public boolean isValidAppKey(String appKey) {
		return "5432".equals(appKey);
	}

}
