package com.fiberhome.mapps.szzj.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.szzj.entity.VChannel;
import com.fiberhome.mapps.szzj.entity.VChannelDetail;

public interface VChannelMapper extends MyMapper<VChannel>{
	public List<VChannelDetail> queryChannelList(Map<String, Object> map);
}
