package com.fiberhome.mapps.vote.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.utils.IDGen;
import com.fiberhome.mapps.vote.dao.VtVoteAnswerMapper;
import com.fiberhome.mapps.vote.dao.VtVoteInfoMapper;
import com.fiberhome.mapps.vote.dao.VtVoteItemMapper;
import com.fiberhome.mapps.vote.dao.VtVoteScopeMapper;
import com.fiberhome.mapps.vote.entity.MyUser;
import com.fiberhome.mapps.vote.entity.VoteInfo;
import com.fiberhome.mapps.vote.entity.VoteItemInfo;
import com.fiberhome.mapps.vote.entity.VtVoteAnswer;
import com.fiberhome.mapps.vote.entity.VtVoteInfo;
import com.fiberhome.mapps.vote.entity.VtVoteItem;
import com.fiberhome.mapps.vote.entity.VtVoteScope;
import com.fiberhome.mapps.vote.request.GetUsersRequest;
import com.fiberhome.mapps.vote.request.GetVoteInfoRequest;
import com.fiberhome.mapps.vote.request.QueryVoteRequest;
import com.fiberhome.mapps.vote.request.SaveVoteRequest;
import com.fiberhome.mapps.vote.response.FileInfoResponse;
import com.fiberhome.mapps.vote.response.GetUsersResponse;
import com.fiberhome.mapps.vote.response.QueryVoteResponse;
import com.fiberhome.mapps.vote.utils.DateUtil;
import com.fiberhome.mapps.vote.utils.ErrorCode;
import com.fiberhome.mapps.vote.utils.LogUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean(version = "1.0")
public class VtVoteService
{
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Autowired
    VtVoteInfoMapper       vtVoteInfoMapper;
    @Autowired
    VtVoteAnswerMapper     vtVoteAnswerMapper;
    @Autowired
    VtVoteScopeMapper      vtVoteScopeMapper;
    @Autowired
    VtVoteItemMapper       vtVoteItemMapper;
    @Autowired
    ThirdPartAccessService thirdPartAccessService;
    @Autowired
    FileService            fileService;
    
    @Value("${flywaydb.locations}")
	String databaseType;

    /**
     * 获取投票列表
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.vote.query.caninvolve", group = "vote", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryVoteResponse queryCanInvolveVote(QueryVoteRequest req)
    {
        LOGGER.info("获取参与投票列表接口(mapps.vote.query.caninvolve)入口,请求参数==" + LogUtil.getObjectInfo(req));
        QueryVoteResponse res = new QueryVoteResponse();
        try
        {
            // 组织查询条件
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("orgId", SessionContext.getOrgId());
            map.put("userId", SessionContext.getUserId());
            map.put("userIdLowerCase", SessionContext.getUserId().toLowerCase());
            map.put("databaseType", databaseType);
            if (StringUtil.isNotEmpty(req.getTitle()))
            {
                map.put("title", "%" + req.getTitle() + "%");
            }
            // 进行分页查询
            PageHelper.startPage(req.getOffset(), req.getLimit());
            List<VoteInfo> list = vtVoteInfoMapper.selectCanInvolveVote(map);
            for (VoteInfo info : list)
            {
                if (StringUtil.isNotEmpty(info.getImage()))
                {
                    info.setImage(fileService.getWebRoot() + info.getImage());
                }
            }
            PageInfo<VoteInfo> page = new PageInfo<VoteInfo>(list);
            res.setTotal(page.getTotal());
            res.setVoteList(list);
        }
        catch (Exception e)
        {
            LOGGER.error("获取参与投票列表异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    @ServiceMethod(method = "mapps.vote.query.involve", group = "vote", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryVoteResponse queryInvolvedVote(QueryVoteRequest req)
    {
        LOGGER.info("获取投票列表接口(mapps.vote.query.involve)入口,请求参数==" + LogUtil.getObjectInfo(req));
        QueryVoteResponse res = new QueryVoteResponse();
        try
        {
            // 组织查询条件
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("orgId", SessionContext.getOrgId());
            map.put("userId", SessionContext.getUserId());
            map.put("databaseType", databaseType);
            if (StringUtil.isNotEmpty(req.getTitle()))
            {
                map.put("title", "%" + req.getTitle() + "%");
            }
            // 进行分页查询
            PageHelper.startPage(req.getOffset(), req.getLimit());
            List<VoteInfo> list = vtVoteInfoMapper.selectInvolvedVote(map);
            for (VoteInfo info : list)
            {
                if (StringUtil.isNotEmpty(info.getImage()))
                {
                    info.setImage(fileService.getWebRoot() + info.getImage());
                }
            }
            PageInfo<VoteInfo> page = new PageInfo<VoteInfo>(list);
            res.setTotal(page.getTotal());
            res.setVoteList(list);
        }
        catch (Exception e)
        {
            LOGGER.error("获取投票列表异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    @ServiceMethod(method = "mapps.vote.query.created", group = "vote", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryVoteResponse queryCreatedVote(QueryVoteRequest req)
    {
        LOGGER.info("获取我创建的投票列表接口(mapps.vote.query.created)入口,请求参数==" + LogUtil.getObjectInfo(req));
        QueryVoteResponse res = new QueryVoteResponse();
        try
        {
            // 组织查询条件
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("orgId", SessionContext.getOrgId());
            map.put("userId", SessionContext.getUserId());
            map.put("databaseType", databaseType);
            if (StringUtil.isNotEmpty(req.getTitle()))
            {
                map.put("title", "%" + req.getTitle() + "%");
            }
            // 进行分页查询
            PageHelper.startPage(req.getOffset(), req.getLimit());
            List<VoteInfo> list = vtVoteInfoMapper.selectCreatedVote(map);
            for (VoteInfo info : list)
            {
                if (StringUtil.isNotEmpty(info.getImage()))
                {
                    info.setImage(fileService.getWebRoot() + info.getImage());
                }
            }
            PageInfo<VoteInfo> page = new PageInfo<VoteInfo>(list);
            res.setTotal(page.getTotal());
            res.setVoteList(list);
        }
        catch (Exception e)
        {
            LOGGER.error("获取我创建的投票列表异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    @ServiceMethod(method = "mapps.vote.delete.votebyid", group = "vote", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional
    public QueryVoteResponse deleteVoteById(GetVoteInfoRequest req)
    {
        LOGGER.info("删除投票接口(mapps.vote.delete.votebyid)入口,请求参数==" + LogUtil.getObjectInfo(req));
        QueryVoteResponse res = new QueryVoteResponse();
        try
        {
            vtVoteInfoMapper.deleteByPrimaryKey(req.getId());
        }
        catch (Exception e)
        {
            LOGGER.error("删除投票异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    @ServiceMethod(method = "mapps.vote.query.votebyid", group = "vote", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryVoteResponse getVoteById(GetVoteInfoRequest req)
    {
        LOGGER.info("获取投票详情接口(mapps.vote.query.votebyid)入口,请求参数==" + LogUtil.getObjectInfo(req));
        QueryVoteResponse res = new QueryVoteResponse();
        try
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("voteInfoId", req.getId());
            map.put("orgId", SessionContext.getOrgId());
            map.put("userId", SessionContext.getUserId());
            map.put("databaseType", databaseType);
            VoteInfo voteInfo = vtVoteInfoMapper.selectVoteById(map);
            if (voteInfo == null)
            {// 投票记录不存在
                res.fail("投票不存在");
                return res;
            }
            if (StringUtil.isNotEmpty(voteInfo.getImage()))
            {
                voteInfo.setImage(fileService.getWebRoot() + voteInfo.getImage());
            }
            res.setVoteInfo(voteInfo);
            List<VoteItemInfo> list = vtVoteInfoMapper.selectVoteItems(map);
            for (VoteItemInfo info : list)
            {
                if (StringUtil.isNotEmpty(info.getImage()))
                {
                    info.setImage(fileService.getWebRoot() + info.getImage());
                }
            }
            res.setVoteItems(list);
            // 投票阅读加一
            // if (req.isInc())
            // {
            vtVoteInfoMapper.incReadCount(map);
            // }
        }
        catch (Exception e)
        {
            LOGGER.error("获取投票详情异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    @ServiceMethod(method = "mapps.vote.query.voteing", group = "vote", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional
    public QueryVoteResponse voting(GetVoteInfoRequest req)
    {
        LOGGER.info("获取投票列表接口(mapps.vote.query.voteing)入口,请求参数==" + LogUtil.getObjectInfo(req));
        QueryVoteResponse res = new QueryVoteResponse();
        try
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("voteInfoId", req.getId());
            map.put("orgId", SessionContext.getOrgId());
            map.put("userId", SessionContext.getUserId());
            map.put("databaseType", databaseType);
            VoteInfo voteInfo = vtVoteInfoMapper.selectVoteById(map);
            if (voteInfo == null)
            {// 投票记录不存在
                res.fail("投票不存在");
                return res;
            }
            else if (voteInfo.isInvolved())
            {
                res.fail("已经投过票,请勿重复投票");
                return res;
            }
            else if (voteInfo.isExpired())
            {
                res.fail("投票已过期");
                return res;
            }
            String itemIds = req.getItemIds();
            if (!StringUtil.isEmpty(itemIds))
            {
                // 提交了选项
                String[] items = itemIds.split(",");
                for (String i : items)
                {
                    VtVoteAnswer va = new VtVoteAnswer();
                    va.setCreator(SessionContext.getUserId());
                    va.setCreatorName(SessionContext.getUserName());
                    va.setOrgId(SessionContext.getOrgId());
                    va.setCreateTime(new Date());
                    va.setVoteInfoId(voteInfo.getId());
                    va.setVoteItemId(i);
                    va.setId(IDGen.uuid());
                    vtVoteAnswerMapper.insertSelective(va);
                }
            }
            else
            {
                res.fail("请提交选项");
                return res;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("获取投票列表异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    @ServiceMethod(method = "mapps.vote.query.usersize", group = "vote", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryVoteResponse countDepartmentUser(GetVoteInfoRequest req)
    {
        LOGGER.info("获取部门包含用户数接口(mapps.vote.query.usersize)入口,请求参数==" + LogUtil.getObjectInfo(req));
        QueryVoteResponse res = new QueryVoteResponse();
        try
        {
            // 根据提交的部门id,去ark接口查询出所有人员数量
            if (StringUtil.isEmpty(req.getDepIds()))
            {
                res.fail("请提交要查询的部门id");
                return res;
            }
            int size = 0;
            String[] typeDataArr = req.getDepIds().split("\\|");
            if (StringUtil.isNotEmpty(typeDataArr[0]))
            {
                size += thirdPartAccessService.getUserSizeFormDept(typeDataArr[0]);
            }
            if (typeDataArr.length > 1 && StringUtil.isNotEmpty(typeDataArr[1]))
            {
                size += typeDataArr[1].split(",").length;
            }
            res.setData(size);
        }
        catch (Exception e)
        {
            LOGGER.error("获取部门包含用户数异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    @ServiceMethod(method = "mapps.vote.save.vote", group = "vote", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional
    public QueryVoteResponse saveVote(SaveVoteRequest req)
    {
        LOGGER.info("保存投票接口(mapps.vote.save.vote)入口,请求参数==" + LogUtil.getObjectInfo(req));
        QueryVoteResponse res = new QueryVoteResponse();
        try
        {
            List<String> users = new ArrayList<String>();
            // 2.获取当前操作用户,投票范围的用户
            if (!StringUtil.isEmpty(req.getDepIds()))
            {
                // orgid+loginId作为用户的唯一标识,应用是多企业共用
                Map<String, Object> params = new HashMap<String, Object>();
                // 企业id
                params.put("orgUuid", SessionContext.getOrgId());
                // 级联下级所有人员
                params.put("depScope", "1");
                String[] typeDataArr = req.getDepIds().split("\\|");
                if (StringUtil.isNotEmpty(typeDataArr[0]))
                {
                    String[] dida = typeDataArr[0].split(",");// dida means Department ID Array
                    for (String did : dida)
                    {
                        // 部门id
                        params.put("depUuid", did);

                        GetUsersRequest request = new GetUsersRequest();
                        request.setDepUuid(did);
                        request.setDepScope("1");
                        GetUsersResponse response = thirdPartAccessService.getUsers(request);
                        if (response == null || !"1".equals(response.getCode()))
                        {
                            res.fail("查询部门人员出错");
                            return res;
                        }
                        for (MyUser mu : response.getUserInfos())
                        {
                            users.add(mu.getLoginId());
                        }
                    }
                }
                if (typeDataArr.length > 1 && StringUtil.isNotEmpty(typeDataArr[1]))
                {
                    for (String loginId : typeDataArr[1].split(","))
                    {
                        users.add(loginId);
                    }
                }
            }
            // 群组人员id
            if (!StringUtil.isEmpty(req.getUserIds()))
            {
                String[] uida = req.getUserIds().split(",");// uida means User ID Array
                for (String uid : uida)
                {
                    users.add(uid);
                }
            }
            // 4.保存入库
            String voteId = saveVote(req.getVoteInfo(), req.getVoteItems(), users);
            // 6.给参选人发代办提示,从选择部门过来的,从群过来的不发
            if (!StringUtil.isEmpty(req.getDepIds()))
            {
                if (users != null && users.size() > 0)
                {
                    if (!thirdPartAccessService.pushReceiveEvent(SessionContext.getEcId(),
                            StringUtils.join(users.toArray(), ","), "您有新的投票", req.getVoteInfo().getTitle(), voteId))
                    {
                        LOGGER.debug("推送代办失败");
                        res.fail();
                        return res;
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("保存投票异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public String saveVote(VoteInfo vote, List<VtVoteItem> items, List<String> users) throws Exception
    {
        VtVoteInfo v = new VtVoteInfo();
        v.setTitle(vote.getTitle());
        v.setContent(vote.getContent());
        v.setMultiple(vote.getMultiple());
        v.setMaxChoose(vote.getMaxChoose());
        v.setAnonymous(vote.getAnonymous());
        v.setImage(vote.getImage());
        // step1,预处理数据,添加创建者,创建时间,有效性
        v.setOrgId(SessionContext.getOrgId());
        v.setCreator(SessionContext.getUserId());
        v.setCreatorName(SessionContext.getUserName());
        v.setCreateTime(new Date());
        v.setState("1");
        // 过期时间 添加23:59:59
        Calendar c = Calendar.getInstance();
        c.setTime(DateUtil.convertToDate(vote.getExpireStr()));
        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        v.setExpire(c.getTime());
        // 保存图片到本服务器
        if (!StringUtil.isEmpty(v.getImage()))
        {
            FileInfoResponse upload = fileService.downAndReadFile(v.getImage());
            if (upload == null || BaseResponse.FAIL.equals(upload.getCode()))
            {
                throw new IOException("图片下载失败.");
            }
            v.setImage(upload.getPath());
        }
        // step2,构建关联
        String vid = IDGen.uuid();
        v.setId(vid);
        for (int i = 0; i < items.size(); i++)
        {
            items.get(i).setId(IDGen.uuid());
            items.get(i).setVoteInfoId(vid);
            items.get(i).setItemOrder((long) i);
            // 保存选项图片到本服务器
            if (!StringUtil.isEmpty(items.get(i).getImage()))
            {
                FileInfoResponse upload = fileService.downAndReadFile(items.get(i).getImage());
                if (upload == null || BaseResponse.FAIL.equals(upload.getCode()))
                {
                    throw new IOException("图片下载失败.");
                }
                items.get(i).setImage(upload.getPath());
            }
            vtVoteItemMapper.insertSelective(items.get(i));
        }
        for (String uid : users)
        {
            VtVoteScope vvs = new VtVoteScope();
            vvs.setOrgId(v.getOrgId());
            vvs.setUserId(uid);
            vvs.setVoteInfoId(vid);
            vtVoteScopeMapper.insertSelective(vvs);
        }

        vtVoteInfoMapper.insertSelective(v);
        return vid;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleChangeStatus()
    {
        Date now = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("time", now);
        vtVoteInfoMapper.scanExpired(map);
        LOGGER.debug("刷新投票状态定时任务执行完成,执行时间:{}", now);
    }
}
