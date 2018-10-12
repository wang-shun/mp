package com.fiberhome.mapps.ydzf.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.ydzf.entity.LawExpFoodCosmetics;
import com.fiberhome.mapps.ydzf.entity.LawFile;
import com.fiberhome.mapps.ydzf.entity.LawFoodCosmetics;
import com.fiberhome.mapps.ydzf.entity.LawImFoodCosmetics;
import com.fiberhome.mapps.ydzf.entity.LawSampling;
import com.fiberhome.mapps.ydzf.entity.LawSamplingList;

public interface LawImFoodMapper extends MyMapper<LawImFoodCosmetics>{
	/**
	 * 列表查询
	 * @param map
	 * @return
	 */
	public List<LawFoodCosmetics> queryLawlList(Map<String,Object> map);
	/**
	 * 取样查询
	 * @param map
	 * @return
	 */
	public List<LawSampling> getLawSamlpings(Map<String,Object> map);
	/**
	 * 取样明细查询
	 * @param map
	 * @return
	 */
	public List<LawSamplingList> getLawSamplingLists(Map<String,Object> map);
	/**
	 * 出口详情查询
	 * @param map
	 * @return
	 */
	public LawExpFoodCosmetics getLawOutDetail(Map<String,Object> map);
	/**
	 * 获取law 文件
	 * @param map
	 * @return
	 */
	public List<LawFile> getLawFile(Map<String,Object> map);
}
