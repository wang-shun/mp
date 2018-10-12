package com.fiberhome.mapps.gateway.utils;

import java.util.Map;

import org.springframework.cloud.client.discovery.DiscoveryClient;

import com.fiberhome.mos.core.openapi.rop.client.RopClient;

public class EurekaClientUtils {
	DiscoveryClient discovery;
	
	public EurekaClientUtils(DiscoveryClient discovery) {
		this.discovery = discovery;
	}
	
	public RopClient getServiceManagerClient() {
//		List<ServiceInstance> smList = discovery.getInstances("MAPPS-SERVICEMANAGER");
//		
//		if (smList != null && smList.size() > 0) {
//			ServiceInstance si = smList.get(0);
//			String url = si.getUri() + "/api";
//    		RopClient client = new RopClient(url, "5432", "FHuma025", "json");
//    		
//    		return client;
//		} else {
//			throw new IllegalComponentStateException("没有可用的服务管理节点，不能获取到服务列表。") ;
//		}
		
		//servicemanager不向自己注册后修改gateway生成eruekaclient方式
		Map<String, Object> map = YamlUtils.yaml2Map("application.yml");
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Map<String, Object> smap = (Map)map.get("servicemanager");
		String url = smap.get("endpoint") + "/api";
		
		RopClient client = new RopClient(url, "5432", "FHuma025", "json");
		
		return client;
	}
}
