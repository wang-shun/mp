package com.fiberhome.mapps.servicemanager.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.servicemanager.entity.ClientServiceInfo;
import com.fiberhome.mapps.servicemanager.entity.SmSvcAuth;

public interface SmSvcAuthMapper extends MyMapper<SmSvcAuth> {
	public List<ClientServiceInfo> getServiceInfoByAppId(Map<String, Object> map);
}