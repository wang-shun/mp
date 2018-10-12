package com.fiberhome.mapps.servicemanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fiberhome.mapps.servicemanager.dao.McSystemMapper;
import com.fiberhome.mapps.servicemanager.entity.McSystem;

@Component
public class McSystemService {
	@Autowired
	McSystemMapper systemMapper;
	
	public McSystem getCurrentSystem() {
		String dbName = "mydb";
		McSystem sample = new McSystem();
		sample.setDb(dbName);
		McSystem result = systemMapper.selectOne(sample);
		return result;
	}
	
	public static int getReplication() {
		int replication = 1;
		return replication;
	}
}
