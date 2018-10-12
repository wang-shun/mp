package com.fiberhome.mapps.feedback.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.feedback.entity.FdFeedback;
import com.fiberhome.mapps.intergration.mybatis.MyMapper;

public interface FdFeedbackMapper extends MyMapper<FdFeedback>
{

    public List<FdFeedback> getFdFeedback(Map<String, Object> map);
}