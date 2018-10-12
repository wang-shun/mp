package com.fiberhome.mapps.vote.dao;

import java.util.List;
import java.util.Map;

import com.fiberhome.mapps.intergration.mybatis.MyMapper;
import com.fiberhome.mapps.vote.entity.VoteInfo;
import com.fiberhome.mapps.vote.entity.VoteItemInfo;
import com.fiberhome.mapps.vote.entity.VtVoteAnswer;
import com.fiberhome.mapps.vote.entity.VtVoteInfo;

public interface VtVoteInfoMapper extends MyMapper<VtVoteInfo>
{
    public List<VoteInfo> selectCanInvolveVote(Map<String, Object> map);

    public List<VoteInfo> selectInvolvedVote(Map<String, Object> map);

    public List<VoteInfo> selectCreatedVote(Map<String, Object> map);

    public VoteInfo selectVoteById(Map<String, Object> map);

    public List<VoteItemInfo> selectVoteItems(Map<String, Object> map);

    public List<VtVoteAnswer> selectVoteAnswer(Map<String, Object> map);

    public void incReadCount(Map<String, Object> map);

    public void scanExpired(Map<String, Object> map);
}