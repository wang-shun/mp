package com.fiberhome.mapps.sign.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.sign.dao.SnSignImageMapper;
import com.fiberhome.mapps.sign.dao.SnSignMapper;
import com.fiberhome.mapps.sign.entity.Sign;
import com.fiberhome.mapps.sign.entity.SnSign;
import com.fiberhome.mapps.sign.entity.SnSignImage;
import com.fiberhome.mapps.sign.entity.StatSign;
import com.fiberhome.mapps.sign.request.AddSignRequest;
import com.fiberhome.mapps.sign.request.GetUserIconRequest;
import com.fiberhome.mapps.sign.request.QueryRankSignRequest;
import com.fiberhome.mapps.sign.request.QueryServerTimeRequest;
import com.fiberhome.mapps.sign.request.QuerySignDetailRequest;
import com.fiberhome.mapps.sign.request.QuerySignRequest;
import com.fiberhome.mapps.sign.request.QueryStatRequest;
import com.fiberhome.mapps.sign.response.AddSignResponse;
import com.fiberhome.mapps.sign.response.FileInfoResponse;
import com.fiberhome.mapps.sign.response.GetUserIconResponse;
import com.fiberhome.mapps.sign.response.QueryRankSignResponse;
import com.fiberhome.mapps.sign.response.QueryServerTimeResponse;
import com.fiberhome.mapps.sign.response.QuerySignDetailResponse;
import com.fiberhome.mapps.sign.response.QuerySignResponse;
import com.fiberhome.mapps.sign.response.QueryStatResponse;
import com.fiberhome.mapps.sign.utils.DateUtil;
import com.fiberhome.mapps.sign.utils.ErrorCode;
import com.fiberhome.mapps.sign.utils.LogUtil;
import com.fiberhome.mapps.utils.IDGen;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean(version = "1.0")
public class SnSignService
{
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Autowired
    SnSignMapper           snSignMapper;
    @Autowired
    SnSignImageMapper      snSignImageMapper;
    @Autowired
    FileService            fileService;
    @Autowired
    ThirdPartAccessService thirdPartAccessService;
    
    @Value("${flywaydb.locations}")
	String databaseType;

    /**
     * 新增签到
     * 
     * @param req
     * @return
     * @throws Exception
     */
    @ServiceMethod(method = "mapps.sign.add", group = "sign", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public AddSignResponse save(AddSignRequest req) throws Exception
    {
        LOGGER.info("新增签到接口(mapps.sign.add)入口,请求参数==" + LogUtil.getObjectInfo(req));
        AddSignResponse res = new AddSignResponse();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("userId", SessionContext.getUserId());
        map.put("ecid", SessionContext.getEcId());
        Date recentSignTime = snSignMapper.queryRecentSignTime(map);
        Date currentTime = new Date();
        if(recentSignTime != null){
        	if((currentTime.getTime()-recentSignTime.getTime()) < (1000*60*5)){
            	ErrorCode.fail(res, ErrorCode.CODE_100011);
            	return res;
            }
        }
        try
        {
            Sign sign = req.getSign();
            // 签到信息补充
            sign.setSignId(IDGen.uuid());
            sign.setEcid(SessionContext.getEcId());
            sign.setSignTime(new Date());// 设置签到时间,不再由前台提交,防止用户作弊 Totti@2016-07-22
            sign.setCreator(SessionContext.getUserId());
            sign.setCreatorName(SessionContext.getUserName());
            sign.setCreateTime(new Date());
            sign.setDepId(SessionContext.getDeptId());
            sign.setState("1");// 状态默认1有效,暂时未使用
            sign.setDeptOrder(SessionContext.getDeptOrder());
            List<String> images = sign.getImages();
            // 签到图片
            if (images != null && !images.isEmpty())
            {
                for (String image : images)
                {
                    FileInfoResponse upload = fileService.downAndReadFile(image);
                    if (upload == null)
                    {
                        ErrorCode.fail(res, ErrorCode.CODE_100005);
                        return res;
                    }
                    SnSignImage si = new SnSignImage();
                    si.setSignImageId(IDGen.uuid());
                    si.setSignId(sign.getSignId());
                    si.setImage(upload.getPath());
                    // 保存信息入库
                    snSignImageMapper.insertSelective(si);
                }
            }
            snSignMapper.insertSelective(convertToSnSign(sign));
            res.setSignId(sign.getSignId());
        }
        catch (Exception e)
        {
            LOGGER.error("新增签到异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 查询我的签到
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.sign.query", group = "sign", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QuerySignResponse queryMySign(QuerySignRequest req)
    {
        LOGGER.info("获取我的签到列表接口(mapps.sign.query)入口,请求参数==" + LogUtil.getObjectInfo(req));
        QuerySignResponse res = new QuerySignResponse();
        try
        {
            // 组织查询条件
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (StringUtil.isNotEmpty(req.getCreator()))
            {// 如果提交了指定id
                map.put("creator", req.getCreator());
            }
            else
            {// 没有指定就按当前登录人查
                map.put("creator", SessionContext.getUserId());
            }
            map.put("orgId", SessionContext.getOrgId());
            map.put("ecid", SessionContext.getEcId());
            Date signTimeBegin = DateUtil.sdf().parse(req.getSignDate());
            map.put("signTimeBegin", signTimeBegin);
            map.put("signTimeEnd", DateUtil.getNextDay(signTimeBegin));
            List<Sign> signs = snSignMapper.querySign(map);
            for (Sign sign : signs)
            {
                SnSignImage ssiInfo = new SnSignImage();
                ssiInfo.setSignId(sign.getSignId());
                List<SnSignImage> sis = snSignImageMapper.select(ssiInfo);
                sign.setImages(this.buildImages(sis));
            }
            res.setSignList(signs);
        }
        catch (Exception e)
        {
            LOGGER.error("获取我的签到列表异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 处理图片信息
     * 
     * @param sis
     * @return
     */
    private List<String> buildImages(List<SnSignImage> sis)
    {
        List<String> images = null;
        if (sis != null)
        {
            images = new ArrayList<String>(sis.size());
            for (SnSignImage si : sis)
            {
                images.add(fileService.getWebRoot() + si.getImage());
            }
        }
        return images;
    }

    /**
     * 查询签到详情
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.sign.detail", group = "sign", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QuerySignDetailResponse querySignDetail(QuerySignDetailRequest req)
    {
        LOGGER.info("查询签到详情接口(mapps.sign.detail)入口,请求参数==" + LogUtil.getObjectInfo(req));
        QuerySignDetailResponse res = new QuerySignDetailResponse();
        res.setSign(convertToSign(snSignMapper.selectByPrimaryKey(req.getId())));
        return res;
    }

    /**
     * 获取服务器时间
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.sign.servertime", group = "sign", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryServerTimeResponse queryMySign(QueryServerTimeRequest req)
    {
        LOGGER.info("获取服务器时间接口(mapps.sign.servertime)入口");
        QueryServerTimeResponse res = new QueryServerTimeResponse();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        res.setFormatTime(sdf.format(new Date()));
        LOGGER.info("当前服务器时间=" + res.getFormatTime());
        return res;
    }

    /**
     * 查询时间段签到排名
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.sign.queryadmin", group = "sign", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryRankSignResponse queryRankSign(QueryRankSignRequest req)
    {
        LOGGER.info("查询时间段签到排名接口(mapps.sign.queryadmin)入口,请求参数==" + LogUtil.getObjectInfo(req));
        QueryRankSignResponse res = new QueryRankSignResponse();
        try
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            Date signTimeBegin = DateUtil.sdf().parse(req.getSignDateBegin());
            map.put("signTimeBegin", signTimeBegin);
            Date signTimeEnd = DateUtil.sdf().parse(req.getSignDateEnd());
            map.put("signTimeEnd", DateUtil.getNextDay(signTimeEnd));
            map.put("creator", SessionContext.getUserId());
            map.put("databaseType", databaseType);
            // 执行查询
            List<Sign> list = snSignMapper.queryRankSign(map);
            // 准备条件
            if (list == null || list.size() == 0)
            {
                ErrorCode.fail(res, ErrorCode.CODE_100003);
                return res;
            }
            res.setSignInfo(list.get(0));
        }
        catch (Exception e)
        {
            ErrorCode.fail(res, ErrorCode.CODE_100001);
            LOGGER.error("获取我的签到列表异常：{}", e);
        }
        return res;
    }

    /**
     * 根据条件获取用户信息
     * 
     * @param depIds
     * @param userIds
     * @param virtualGroupIds
     * @param loginIdList
     * @return HashMap<loginId,用户信息对象>
     * @throws Exception
     */
    public HashMap<String, MyUser> getUsers(String depIds, String userIds, String virtualGroupIds,
            List<String> loginIdList)
        throws Exception
    {
        HashMap<String, MyUser> map = new HashMap<String, MyUser>();
        if (StringUtil.isNotEmpty(depIds))
        {
            List<MyUser> list = thirdPartAccessService.getUsersByDept(depIds);
            for (MyUser mu : list)
            {
                if (!loginIdList.contains(mu.getLoginId()))
                {
                    loginIdList.add(mu.getLoginId());
                    map.put(mu.getLoginId(), mu);
                }
            }
        }
        if (!StringUtil.isEmpty(userIds))
        {
            List<MyUser> list = thirdPartAccessService.getUserInfos(SessionContext.getOrgId(), userIds);
            for (MyUser mu : list)
            {
                if (!loginIdList.contains(mu.getLoginId()))
                {
                    loginIdList.add(mu.getLoginId());
                    map.put(mu.getLoginId(), mu);
                }
            }
        }
        // 已废弃
        if (!StringUtil.isEmpty(virtualGroupIds))
        {
            // cond.put("userIds", getAllVirtualUser(virtualGroupIds));
        }
        return map;
    }

    /**
     * 统计签到次数
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.sign.queryStat", group = "sign", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryStatResponse queryStat(QueryStatRequest req)
    {
        LOGGER.info("统计签到次数接口(mapps.sign.queryStat)入口,请求参数==" + LogUtil.getObjectInfo(req));
        QueryStatResponse res = new QueryStatResponse();
        try
        {
            String depIds = req.getDepIds();
            String userIds = req.getUserIds();
            String virtualGroupIds = req.getVirtualGroupIds();
            String signDate = req.getSignDate();
            // 参数校验
            if (StringUtil.isEmpty(depIds) && StringUtil.isEmpty(userIds) && StringUtil.isEmpty(virtualGroupIds)
                    || signDate == null)
            {
                ErrorCode.fail(res, ErrorCode.CODE_100003);
                return res;
            }
            // 准备参数
            List<String> loginIdList = new ArrayList<String>();
            HashMap<String, Object> cond = new HashMap<String, Object>();
            HashMap<String, MyUser> muMap = getUsers(depIds, userIds, virtualGroupIds, loginIdList);
            cond.put("userIds", "('" + StringUtils.join(loginIdList.toArray(), "','") + "')");
            cond.put("ecid", SessionContext.getEcId());
            if (signDate != null)
            {
                cond.put("signTimeBegin", DateUtil.sdf().parse(signDate));
                cond.put("signTimeEnd", DateUtil.getNextDay(DateUtil.sdf().parse(signDate)));
            }
            // 执行查询
            res.setStatSign(querSignSum(cond, loginIdList, muMap));
        }
        catch (Exception e)
        {
            ErrorCode.fail(res, ErrorCode.CODE_100001);
            LOGGER.error("统计签到次数接口(mapps.sign.queryStat)异常：{}", e);
        }
        // 返回结果
        return res;
    }

    /**
     * 签到,未签到,请假数据拼装
     * 
     * @param cond
     * @param loginIdList
     * @param muMap
     * @return
     * @throws Exception
     */
    public StatSign querSignSum(Map<String, Object> cond, List<String> loginIdList, HashMap<String, MyUser> muMap)
        throws Exception
    {
        List<Sign> list = snSignMapper.querySignSum(cond);
        List<String> lu = new ArrayList<String>();
        // 请假人员去请示应用中获取数据
        // List<String> lu = applyInfoDao.queryLeaveUser((Date) cond.get("signTimeBegin"), (Date)
        // cond.get("signTimeEnd"));
        HashSet<String> leaveUserIds = new HashSet<String>();
        leaveUserIds.addAll(lu);
        List<Sign> signed = new ArrayList<Sign>();
        List<Sign> unsigned = new ArrayList<Sign>();
        List<Sign> leaves = new ArrayList<Sign>();
        int signedCount = 0;
        int noSignedCount = 0;
        int leavesCount = 0;
        for (Sign sign : list)
        {
            signedCount++;
            signed.add(sign);
            sign.setState("1");// 有签到的
            loginIdList.remove(sign.getCreator());
        }
        for (String loginId : loginIdList)
        {
            Sign sign = new Sign();
            sign.setCreator(loginId);
            sign.setCreatorName(muMap.get(loginId).getUserName());
            if (leaveUserIds.contains(sign.getCreator()))
            {
                leavesCount++;
                leaves.add(sign);
                sign.setState("2");// 请假的
            }
            else
            {
                noSignedCount++;
                unsigned.add(sign);
                sign.setState("0");// 未签到的
            }
        }
        StatSign ssInfo = new StatSign();
        ssInfo.setSignedCount(signedCount);
        ssInfo.setNoSignedCount(noSignedCount);
        ssInfo.setLeavesCount(leavesCount);
        ssInfo.setList(list);
        ssInfo.setSigned(signed);
        ssInfo.setUnsigned(unsigned);
        ssInfo.setLeaves(leaves);
        return ssInfo;
    }

    /**
     * 分页查询签到信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.sign.lastSign.query", group = "sign", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryStatResponse querySignPage(QueryStatRequest req)
    {
        LOGGER.info("分页查询签到信息接口(mapps.sign.lastSign.query)入口,请求参数==" + LogUtil.getObjectInfo(req));
        QueryStatResponse res = new QueryStatResponse();
        try
        {
            String depIds = req.getDepIds();
            String userIds = req.getUserIds();
            String virtualGroupIds = req.getVirtualGroupIds();
            String signDate = req.getSignDate();
            // 参数校验
            if (StringUtil.isEmpty(depIds) && StringUtil.isEmpty(userIds) && StringUtil.isEmpty(virtualGroupIds)
                    || signDate == null)
            {
                ErrorCode.fail(res, ErrorCode.CODE_100003);
                return res;
            }
            // 准备参数
            List<String> loginIdList = new ArrayList<String>();

            HashMap<String, Object> cond = new HashMap<String, Object>();
            getUsers(depIds, userIds, virtualGroupIds, loginIdList);
            cond.put("userIds", "('" + StringUtils.join(loginIdList.toArray(), "','") + "')");
            cond.put("ecid", SessionContext.getEcId());
            cond.put("databaseType", databaseType);
            if (signDate != null)
            {
                cond.put("signTimeBegin", DateUtil.sdf().parse(signDate));
                cond.put("signTimeEnd", DateUtil.getNextDay(DateUtil.sdf().parse(signDate)));
            }
            // 执行查询
            PageHelper.startPage(req.getOffset(), req.getLimit(), true, false);
            List<Sign> list = snSignMapper.querySignStat(cond);
            PageInfo<Sign> page = new PageInfo<Sign>(list);
            res.setList(list);
            res.setTotal(page.getTotal());
        }
        catch (Exception e)
        {
            ErrorCode.fail(res, ErrorCode.CODE_100001);
            LOGGER.error("获取我的签到列表异常：{}", e);
        }
        return res;
    }

    /**
     * 获取用户头像
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.sign.getUserIcon", group = "sign", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetUserIconResponse getUserIcon(GetUserIconRequest req)
    {
        LOGGER.info("获取用户头像接口(mapps.sign.getUserIcon)入口,请求参数==" + LogUtil.getObjectInfo(req));
        GetUserIconResponse res = new GetUserIconResponse();
        try
        {
            List<MyUser> muList = thirdPartAccessService.getUserInfos(SessionContext.getOrgId(), req.getUserIds());
            HashMap<String, String> uim = new HashMap<String, String>();
            for (MyUser user : muList)
            {
                uim.put(user.getLoginId(), user.getAvatarUrl());
            }
            res.setUim(uim);
        }
        catch (Exception e)
        {
            ErrorCode.fail(res, ErrorCode.CODE_100001);
            LOGGER.error("获取我的签到列表异常：{}", e);
        }
        return res;
    }

    /**
     * 对象转换
     * 
     * @param info
     * @return
     */
    public Sign convertToSign(SnSign info)
    {
        Sign sInfo = new Sign();
        sInfo.setSignId(info.getSignId());
        sInfo.setEcid(info.getEcid());
        sInfo.setSignTime(info.getSignTime());
        sInfo.setLongitude(info.getLongitude());
        sInfo.setLatitude(info.getLatitude());
        sInfo.setAddress(info.getAddress());
        sInfo.setCity(info.getCity());
        sInfo.setContent(info.getContent());
        sInfo.setCreator(info.getCreator());
        sInfo.setCreatorName(info.getCreatorName());
        sInfo.setCreateTime(info.getCreateTime());
        sInfo.setDepId(info.getDepId());
        sInfo.setState(info.getState());
        sInfo.setTaskNo(info.getTaskNo());
        sInfo.setDeptOrder(info.getDeptOrder());
        return sInfo;
    }

    /**
     * 对象转换
     * 
     * @param info
     * @return
     */
    public SnSign convertToSnSign(Sign info)
    {
        SnSign sInfo = new SnSign();
        sInfo.setSignId(info.getSignId());
        sInfo.setEcid(info.getEcid());
        sInfo.setSignTime(info.getSignTime());
        sInfo.setLongitude(info.getLongitude());
        sInfo.setLatitude(info.getLatitude());
        sInfo.setAddress(info.getAddress());
        sInfo.setCity(info.getCity());
        sInfo.setContent(info.getContent());
        sInfo.setCreator(info.getCreator());
        sInfo.setCreatorName(info.getCreatorName());
        sInfo.setCreateTime(info.getCreateTime());
        sInfo.setDepId(info.getDepId());
        sInfo.setState(info.getState());
        sInfo.setTaskNo(info.getTaskNo());
        sInfo.setDeptOrder(info.getDeptOrder());
        return sInfo;
    }
}
