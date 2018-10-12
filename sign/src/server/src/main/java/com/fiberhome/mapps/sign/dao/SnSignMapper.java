package com.fiberhome.mapps.sign.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.sign.entity.Sign;
import com.fiberhome.mapps.sign.entity.SnSign;

public interface SnSignMapper extends MyMapper<SnSign>
{
    public List<Sign> querySign(Map<String, Object> map);

    public List<Sign> queryRankSign(Map<String, Object> map);

    public List<Sign> querySignSum(Map<String, Object> map);

    public List<Sign> querySignStat(Map<String, Object> map);
    
    public Date queryRecentSignTime(Map<String, Object> map);
}