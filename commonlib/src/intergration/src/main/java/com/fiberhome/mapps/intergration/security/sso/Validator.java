package com.fiberhome.mapps.intergration.security.sso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fiberhome.mos.core.openapi.rop.client.RopClient;
import com.fiberhome.mos.core.openapi.rop.client.RopClientException;

@Component
@ConfigurationProperties(prefix = "mplus.sso")
public class Validator {
	private static final Logger LOG = LoggerFactory.getLogger(Validator.class);
	
	private static final String SSOAPI_METHOD = "mobileark.ssocheck";
	private static final String SSOAPI_VERSION = "1.3";
	private static final String SSOAPI_VERSION_14 = "1.4";
	private static final String FORMAT = "json";
	
	private static final String USERAPI_METHOD = "mobileark.getuser";
	private static final String USERAPI_VERSION = "1.3";
	
	private static final String MNG_SSOAPI_METHOD = "mobileark.mngssocheck";
	private static final String MNG_SSOAPI_VERSION = "1.0";

	private String serviceUrl;
	private String appKey = "";
	private String secret = "";
	
	private String appId;
	private String appType;
	
	private RopClient client;

//	@NotNull(message="请检查application.yml, sso.serviceUrl是否正确配置")
	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}	
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public UserInfo validate(String sessionId, boolean bClient, String ecid, String orgId) {
		initClient();
		
		if (appId != null && appType != null) {
			return validate14(sessionId, bClient, ecid, orgId);
		} else {
			return validate13(sessionId, bClient, ecid, orgId);
		}
	}

	private void initClient() {
		if (client == null) {
			client = new RopClient(serviceUrl, appKey, secret, FORMAT);
		}
	}

	public UserInfo validate13(String sessionId, boolean bClient, String ecid, String orgId) {
		assert(sessionId != null && sessionId.length() > 0);
		client = new RopClient(serviceUrl, appKey, secret, FORMAT);
		
		try {
			UserInfo user = ssoValidate(sessionId, bClient, ecid, orgId);
			// 补全部门信息
			if (user != null) {
				retriveDeptInfo(sessionId, user);
			}
			LOG.debug("登录用户信息：{}", user);
			return user;
		} catch (RopClientException e) {
			LOG.error("接口调用失败", e);
		}
		return null;
	}
	
	public UserInfo14 validate14(String sessionId, boolean bClient, String ecid, String orgId) {
		assert(sessionId != null && sessionId.length() > 0);
		
		try {
			UserInfo14 user = ssoValidate14(sessionId, bClient, ecid, orgId);
			// 补全部门信息
			if (user != null) {
				retriveDeptInfo(sessionId, user);
			}
			LOG.debug("登录用户信息：{}", user);
			return user;
		} catch (RopClientException e) {
			LOG.error("Mplus 接口调用失败", e);
		}
		return null;
	}
	
	private UserInfo14 ssoValidate14(String sessionId, boolean bClient, String ecid, String orgId) throws RopClientException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", sessionId);
		params.put("type", bClient ? "1" : "2");  // 1.终端 2、管理端
		params.put("appId", appId);
		params.put("appType", appType);
		
		Map<String, Object> response = client.requestForMap(SSOAPI_METHOD, SSOAPI_VERSION_14, params);
		LOG.debug("SSO(V1.4)校验结果：{}", response);
		
		if (response != null && "0".equals(response.get("resultCode"))) { // 返回码，0：成功，1：无效
			List<Map> orginfo = (List<Map>) response.get("orgInfos");
			Map userinfo = (Map)response.get("userInfo");
			List<String> roles = (List<String>)response.get("roles");
			
			String userType = (String)response.get("userType");
			String scope = (String)response.get("appUserDepScope");
			List<Map> appUserDeps = (List<Map>) response.get("appUserDeps");
			Map deptInfo = (Map)response.get("userDep");
			Map orgDeptInfo = (Map)response.get("userOrgDep");
			
			UserInfo14 user = new UserInfo14();
			// 由于mplus所有的第三方接口基本基于loginId来进行处理，故将loginId作为userId处理
			user.setUserId((String)userinfo.get("userUuid"));
//			user.setUserId((String)userinfo.get("loginId"));
			user.setLoginId((String)userinfo.get("loginId"));
			user.setUserName((String)userinfo.get("userName"));
			user.setPhoneNumber((String)userinfo.get("phoneNumber"));
			user.setEmailAddress((String)userinfo.get("emailAddress"));
			
			if (orginfo == null || orginfo.size() == 0) {
				LOG.warn("SSO校验失败，没有机构信息！！");
			} else {
				if (bClient || orginfo.size() == 1) {
					Map map = orginfo.get(0);
					user.setEcid((String)map.get("orgCode"));                          
					user.setOrgId((String)map.get("orgUuid"));
					user.setOrgName((String)map.get("orgName"));
				} else { // web端管理员取机构
					for (Map map : orginfo) {
						if (ecid.equals(map.get("orgCode"))) {
							user.setEcid((String)map.get("orgCode"));                          
							user.setOrgId((String)map.get("orgUuid"));
							user.setOrgName((String)map.get("orgName"));
						}
					}
					
					if (user.getEcid() == null) {
						LOG.warn("SSO校验失败，接口返回部门信息中没有ecid为『{}』的机构！！", ecid);
						return null;
					}
				}
			}

			boolean isAdmin = (roles != null && (roles.contains("0001") || roles.contains("0002"))) || "1".equals(userType);
			user.setAdmin(isAdmin);
			
			user.setDeptScopeType(scope);
			if (appUserDeps != null && appUserDeps.size() > 0) {
				user.setDeptScope(getgetDeptInfoFromMap(appUserDeps));
			}
			if(deptInfo != null){
				user.setDept(getDeptInfoFromMap(deptInfo));
			}
			if(orgDeptInfo != null) {
				user.setOrgDept(getDeptInfoFromMap(orgDeptInfo));
			}
			
			return user;
		}
		return null;
	}
	
	public DeptInfo[] getgetDeptInfoFromMap(List<Map> depts) {
		DeptInfo[] result = new DeptInfo[depts.size()];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = getDeptInfoFromMap(depts.get(i));
		}
		
		return result;
	}
	
	public DeptInfo getDeptInfoFromMap(Map map) {
		assert(map != null);
		
		if (map.get("depUuid") == null) return null;
		
		DeptInfo dept = new DeptInfo();
		dept.setDepUuid((String)map.get("depUuid"));
		dept.setDepName((String)map.get("depName"));
		dept.setParentId((String)map.get("parentId"));
		dept.setEmail((String)map.get("email"));
		dept.setDepWeight((int)map.get("depWeight"));
		dept.setUpdateTime((long)map.get("updateTime"));
		dept.setMode((int)map.get("mode"));
		dept.setDepOrder((String)map.get("depOrder"));
		
		return dept;
	}

	private UserInfo ssoValidate(String sessionId, boolean bClient, String ecid, String orgId) throws RopClientException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", sessionId);
		params.put("type", bClient ? "1" : "2");  // 1.终端 2、管理端
		Map<String, Object> response = client.requestForMap(SSOAPI_METHOD, SSOAPI_VERSION, params);
		LOG.debug("SSO(V1.3)校验结果：{}", response);
		
		if (response != null && "0".equals(response.get("resultCode"))) { // 返回码，0：成功，1：无效
			List<Map> orginfo = (List<Map>) response.get("orgInfos");
			Map userinfo = (Map)response.get("userInfo");
			List<String> roles = (List<String>)response.get("roles");
			
			UserInfo user = new UserInfo();
			// 由于mplus所有的第三方接口基本基于loginId来进行处理，故将loginId作为userId处理
			user.setUserId((String)userinfo.get("userUuid"));
//			user.setUserId((String)userinfo.get("loginId"));
			user.setLoginId((String)userinfo.get("loginId"));
			user.setUserName((String)userinfo.get("userName"));
			user.setPhoneNumber((String)userinfo.get("phoneNumber"));
			user.setEmailAddress((String)userinfo.get("emailAddress"));
			
			if (orginfo == null || orginfo.size() == 0) {
				LOG.warn("SSO校验失败，没有机构信息！！");
			} else {
				if (bClient) { // 手机端取机构
					Map map = orginfo.get(0);
					user.setEcid((String)map.get("orgCode"));                          
					user.setOrgId((String)map.get("orgUuid"));
					user.setOrgName((String)map.get("orgName"));
				} else { // web端取机构
					for (Map map : orginfo) {
						if (ecid.equals(map.get("orgCode"))) {
							user.setEcid((String)map.get("orgCode"));                          
							user.setOrgId((String)map.get("orgUuid"));
							user.setOrgName((String)map.get("orgName"));
							
							break;
						}
					}
					
					if (user.getEcid() == null) {
						LOG.warn("SSO校验失败，接口返回部门信息中没有ecid为『{}』的机构！！", ecid);
						return null;
					}
				}
			}

			boolean isAdmin = roles != null && (roles.contains("0001") || roles.contains("0002"));
			user.setAdmin(isAdmin);
			
			return user;
		}
		return null;
	}

	/**
	 * 补全部门信息
	 * @param sessionId
	 * @param user
	 * @return
	 * @throws RopClientException 
	 */
	public void retriveDeptInfo(String sessionId, UserInfo user) throws RopClientException {
		assert(user != null);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", sessionId);
		params.put("orgUuid", user.getOrgId());
		params.put("loginIds", user.getLoginId());  // 1.终端 2、管理端
		
		Map<String, Object> response = client.requestForMap(USERAPI_METHOD, USERAPI_VERSION, params);
		LOG.debug("用户信息获取结果：{}", response);
		
		if (response != null && "1".equals(response.get("userSize"))) { // 返回码，0：成功，1：无效
			List<Map> userInfos = (List<Map>) response.get("userInfos");
			if (userInfos != null && userInfos.size() == 1) {
				Map userInfo = userInfos.get(0);
				String deptId = (String)userInfo.get("depUuid");
				String deptFullName = (String)userInfo.get("department");
				String avatarUrl = (String)userInfo.get("avatarUrl");
				String deptOrder = (String)userInfo.get("depOrder");
				
				String deptName = "";
				if (deptFullName != null) {
					deptName = deptFullName.substring(deptFullName.lastIndexOf('/') + 1);
				}
				
				user.setDeptId(deptId);
				user.setDeptName(deptName);
				user.setDeptFullName(deptFullName);
				user.setAvatarUrl(avatarUrl);
				user.setDeptOrder(deptOrder);
			} else {
				LOG.warn("接口返回的部门信息:{}有误, 不能获取部门信息。", userInfos);
			}
		}
	}

	public UserInfo validateMng(String sessionId, String ecid, String orgId) {
		initClient();
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", sessionId);
		try {
			Map<String, Object> response = client.requestForMap(MNG_SSOAPI_METHOD, MNG_SSOAPI_VERSION, params);
			LOG.debug("MNG-SSO(V1.0)校验结果：{}", response);
			
			if (response != null && "0".equals(response.get("resultCode"))) { // 返回码，0：成功，1：无效
				Map userinfo = (Map)response.get("userInfo");
				String userType = (String)response.get("userType");
				
				UserInfo user = new UserInfo();
				// 由于mplus所有的第三方接口基本基于loginId来进行处理，故将loginId作为userId处理
				user.setUserId((String)userinfo.get("userUuid"));
//				user.setUserId((String)userinfo.get("loginId"));
				user.setLoginId((String)userinfo.get("loginId"));
				user.setUserName((String)userinfo.get("userName"));
				user.setPhoneNumber((String)userinfo.get("phoneNumber"));
				user.setEmailAddress((String)userinfo.get("emailAddress"));
				
				user.setUserType(userType);

				boolean isAdmin = "1".equals(userType);
				user.setAdmin(isAdmin);
				
				return user;
			}
		}
		catch (RopClientException ex) {
			LOG.error("Mplus 接口调用失败", ex);
		}
		return null;
	}

}
