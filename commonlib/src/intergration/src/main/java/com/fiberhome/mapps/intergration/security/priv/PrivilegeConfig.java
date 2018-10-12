package com.fiberhome.mapps.intergration.security.priv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fiberhome.mapps.intergration.security.sso.UserInfo;

@Component
@ConfigurationProperties(prefix = "priv")
public class PrivilegeConfig  {
	private Map<String, List<String>> roles;
	
	public Map<String, List<String>> getRoles() {
		return roles;
	}

	public void setRoles(Map<String, List<String>> roles) {
		this.roles = roles;
	}

	public List<String> getRolePrivileges(String role) {
		return roles.get(role);
	}
	
	public List<String> getRoleNames() {
		ArrayList<String> names = new ArrayList<String>();
		names.addAll(roles.keySet());
		
		return names;
	}
	
	public boolean hasPrivilege(String role, String priv) {
		List<String> privs = this.getRolePrivileges(role);
		
		return privs != null && privs.contains(priv);
	}
	
	public boolean hasPrivilege(UserInfo user, String priv) {
		String role = user.isAdmin() ? "admin" : "user";
		if (user.isAdmin()) {
			return hasPrivilege("admin", priv) || hasPrivilege("user", priv); 
		} else {
			return hasPrivilege(role, priv);
		}
	}
}
