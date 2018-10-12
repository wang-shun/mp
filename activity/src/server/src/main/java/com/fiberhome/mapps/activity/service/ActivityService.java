package com.fiberhome.mapps.activity.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.fiberhome.mapps.activity.dao.AtActivityMapper;
import com.fiberhome.mapps.activity.dao.AtEnterMapper;
import com.fiberhome.mapps.activity.dao.AtPhotoMapper;
import com.fiberhome.mapps.activity.dao.AtPrivilegeMapper;
import com.fiberhome.mapps.activity.entity.ActivityDetail;
import com.fiberhome.mapps.activity.entity.AtActivity;
import com.fiberhome.mapps.activity.entity.AtEnter;
import com.fiberhome.mapps.activity.entity.AtPhoto;
import com.fiberhome.mapps.activity.entity.AtPrivilege;
import com.fiberhome.mapps.activity.push.PushTask;
import com.fiberhome.mapps.activity.request.AddActivityRequest;
import com.fiberhome.mapps.activity.request.AddEnterRequest;
import com.fiberhome.mapps.activity.request.CreateImGroupRequest;
import com.fiberhome.mapps.activity.request.DeleteActivityRequest;
import com.fiberhome.mapps.activity.request.JoinImGroupRequest;
import com.fiberhome.mapps.activity.request.QueryActivityDetailRequest;
import com.fiberhome.mapps.activity.request.QueryActivityRequest;
import com.fiberhome.mapps.activity.response.AddActivityResponse;
import com.fiberhome.mapps.activity.response.CreateImGroupResponse;
import com.fiberhome.mapps.activity.response.DeleteActivityResponse;
import com.fiberhome.mapps.activity.response.FileInfoResponse;
import com.fiberhome.mapps.activity.response.JoinImGroupResponse;
import com.fiberhome.mapps.activity.response.QueryActivityDetailResponse;
import com.fiberhome.mapps.activity.response.QueryActivityResponse;
import com.fiberhome.mapps.activity.response.ShareWorkCircleResponse;
import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.contact.service.MplusAccessService;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.utils.CreateImGroupException;
import com.fiberhome.mapps.utils.DateUtil;
import com.fiberhome.mapps.utils.ErrorCode;
import com.fiberhome.mapps.utils.IDGen;
import com.fiberhome.mapps.utils.ImErrorCode;
import com.fiberhome.mapps.utils.JoinImGroupException;
import com.fiberhome.mapps.utils.JsonUtil;
import com.fiberhome.mapps.utils.LogUtil;
import com.fiberhome.mapps.utils.RandomUtil;
import com.fiberhome.mapps.utils.SaveFileException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

import tk.mybatis.mapper.util.StringUtil;

@ServiceMethodBean(version = "1.0")
public class ActivityService
{
    @Autowired
    AtActivityMapper          activityMapper;
    @Autowired
    AtPhotoMapper             photoMapper;
    @Autowired
    AtEnterMapper             enterMapper;
    @Autowired
    AtPrivilegeMapper         privilegeMapper;
    @Autowired
    FileService               fileService;
    @Autowired
    ThirdPartAccessService    thirdPartAccessService;
    @Autowired
    ThirdPartAccessMosService thirdPartAccessMosService;

    @Autowired
    MplusAccessService        accessService;

    protected final Logger    LOGGER            = LoggerFactory.getLogger(getClass());

    @Value("${workcircle.id}")
    String                    workCircleId;
    @Value("${client.appId}")
    String                    appId;
    @Value("${client.appName}")
    String                    appName;
    @Value("${activity.im-on-off}")
    boolean                   imOnOff;

    public static final int   UNLIMITED         = 999999999;
    public static final long  ENTER_DEFAULT_NUM = 1l;

    /**
     * 新增活动信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.activity.one.add", group = "activity", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional(rollbackFor = Exception.class)
    public AddActivityResponse addMrRoom(AddActivityRequest req)
    {
        AddActivityResponse res = new AddActivityResponse();
        LOGGER.info("新增活动接口(mapps.activity.one.add)入口,请求参数==" + LogUtil.getObjectInfo(req));
        String id = IDGen.uuid();
        String ecid = SessionContext.getEcId();

        AtActivity act = new AtActivity();
        act.setId(id);
        act.setEcid(ecid);
        act.setConTel(req.getConTel());
        act.setEnterNum(0L);
        act.setActAddress(req.getAddress());
        act.setActCoordinate(req.getActCoordinate());
        act.setActContent(req.getContent());
        act.setActTitle(req.getActTitle());
        if (req.getNumLimit() == null || 0 == req.getNumLimit())
        {
            act.setNumLimit(UNLIMITED);
        }
        else
        {
            act.setNumLimit(req.getNumLimit());
        }
        act.setEnterNum(ENTER_DEFAULT_NUM);
        act.setPhone(StringUtil.isEmpty(req.getPhone()) ? "0" : req.getPhone());
        act.setName(StringUtil.isEmpty(req.getName()) ? "0" : req.getName());
        act.setIdCard(StringUtil.isEmpty(req.getIdCard()) ? "0" : req.getIdCard());
        act.setRemark(StringUtil.isEmpty(req.getRemark()) ? "0" : req.getRemark());
        act.setSex(StringUtil.isEmpty(req.getSex()) ? "0" : req.getSex());
        act.setCreateTime(new Date());
        // act.setModifiedTime(new Date());
        act.setCreateId(SessionContext.getUserId());
        act.setCreateName(SessionContext.getUserName());
        act.setDefaultImage(RandomUtil.getDefaultImage());
        try
        {
            if (StringUtils.isNotEmpty(req.getActPosterUrl()))
            {
                FileInfoResponse upload = fileService.downAndReadFile(req.getActPosterUrl());
                if (upload == null || BaseResponse.FAIL.equals(upload.getCode()))
                {
                    ErrorCode.fail(res, ErrorCode.CODE_100002);
                    throw new SaveFileException();
                }
                act.setActPosterUrl(upload.getPath());
            }
            Date actStartTime = DateUtil.convertToTime(req.getActStartTime());
            Date actEndTime = DateUtil.convertToTime(req.getActEndTime());
            Date enterEndTime = DateUtil.convertToTime(req.getEnterEndTime());
            if (actStartTime.getTime() > actEndTime.getTime())
            {
                ErrorCode.fail(res, ErrorCode.CODE_300003);
                LOGGER.info("错误码:{}={}", ErrorCode.CODE_300003, ErrorCode.errorMap.get(ErrorCode.CODE_300003));
                return res;
            }

            if (enterEndTime.getTime() > actStartTime.getTime())
            {
                ErrorCode.fail(res, ErrorCode.CODE_300004);
                LOGGER.info("错误码:{}={}", ErrorCode.CODE_300004, ErrorCode.errorMap.get(ErrorCode.CODE_300004));
                return res;
            }
            act.setActStartTime(actStartTime);
            act.setActEndTime(actEndTime);
            act.setEnterEndTime(enterEndTime);

            // TODO: 新增活动图片
            List<AtPhoto> photoList = new ArrayList<AtPhoto>();
            if (req.getImageItems() != null && req.getImageItems().size() > 0)
            {
                Long i = 1L;
                for (String image : req.getImageItems())
                {
                    FileInfoResponse upload = fileService.downAndReadFile(image);
                    if (upload == null || BaseResponse.FAIL.equals(upload.getCode()))
                    {
                        ErrorCode.fail(res, ErrorCode.CODE_100002);
                        throw new SaveFileException();
                    }
                    AtPhoto atPhoto = new AtPhoto();
                    atPhoto.setActId(id);
                    atPhoto.setPhoneId(IDGen.uuid());
                    atPhoto.setCreateTime(new Date());
                    atPhoto.setSort(i);
                    atPhoto.setPhoneRoute(upload.getPath());
                    photoList.add(atPhoto);
                    i++;
                }
                for (AtPhoto info : photoList)
                {

                    photoMapper.insertSelective(info);
                }
            }
            // TODO: 权限设置
            AddActivityRequest reqPriv = (AddActivityRequest) JsonUtil.jsonToObject(req.getPrivilegesJson(),
                    AddActivityRequest.class);
            List<AtPrivilege> Privileges = reqPriv.getPrivileges();
            if (Privileges.size() < 1)
            {
                AtPrivilege privilege = new AtPrivilege();
                privilege.setId(IDGen.uuid());
                privilege.setEcid(ecid);
                privilege.setActId(id);
                privilege.setType("dept");
                privilege.setEntityId("-1");
                privilege.setEntityName("-1");
                privilege.setAuthrTime(new Date());
                privilege.setDeptOrder("-1");
                privilegeMapper.insertSelective(privilege);
            }
            else
            {
                for (AtPrivilege atPrivilege : Privileges)
                {
                    atPrivilege.setId(IDGen.uuid());
                    atPrivilege.setActId(id);
                    atPrivilege.setAuthrTime(new Date());
                    privilegeMapper.insertSelective(atPrivilege);
                }
            }

            // TODO: 新增讨论组

            // 讨论组ID
            // 创建群组
            // act.setDisGroupId("1078");
            if (imOnOff)
            {
                String groupName = act.getActTitle();
                if (act.getActTitle().length() > 10)
                {
                    groupName = act.getActTitle().substring(0, 10) + "...";
                }
                CreateImGroupRequest cigReq = new CreateImGroupRequest();
                cigReq.setGroupName(groupName);
                CreateImGroupResponse cigRes = thirdPartAccessMosService.createImGroup(cigReq);
                if (cigRes != null && BaseResponse.SUCCESS.equals(cigRes.getCode()))
                {
                    String groupId = cigRes.getGroupId();
                    act.setDisGroupId(groupId);
                }
                else
                {
                    throw new CreateImGroupException();
                }
            }
            else
            {
                act.setDisGroupId("-1");
            }
            // 创建人默认报名
            AtEnter createEnter = new AtEnter();
            createEnter.setEnterId(IDGen.uuid());
            createEnter.setActId(act.getId());
            createEnter.setPhone(SessionContext.getUser().getPhoneNumber());
            createEnter.setName(SessionContext.getUserName());
            createEnter.setDeptName(SessionContext.getDeptName());
            createEnter.setEnterTime(new Date());
            createEnter.setEcid(ecid);
            createEnter.setEnterPersonId(SessionContext.getUserId());
            enterMapper.insertSelective(createEnter);
            activityMapper.insertSelective(act);
            if ("1".equals(req.getPushFlag()))
                // 添加代办job
                PushTask.addJobForReceiveEvent(SessionContext.getEcId(), SessionContext.getOrgId(),
                        SessionContext.getUserName(), id, req);

            res.setActId(id);
            res.success();
            LOGGER.info("新增活动成功");
        }
        catch (SaveFileException se)
        {
            LOGGER.error("新增活动信息异常：{}", se);
            ErrorCode.fail(res, ErrorCode.CODE_100002);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        catch (CreateImGroupException ce)
        {
            LOGGER.error("新增活动信息异常：{}", ce);
            ErrorCode.fail(res, ErrorCode.CODE_300018);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        catch (JoinImGroupException je)
        {
            LOGGER.error("新增活动信息异常：{}", je);
            ErrorCode.fail(res, ErrorCode.CODE_300018);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("新增活动信息异常：{}", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 删除活动信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.activity.one.delete", group = "activity", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public DeleteActivityResponse deleteMrRoom(DeleteActivityRequest req)
    {
        DeleteActivityResponse res = new DeleteActivityResponse();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("actId", req.getActId());
        map.put("userId", SessionContext.getUserId());
        AtActivity act = activityMapper.selectByPrimaryKey(req.getActId());
        if (act == null)
        {
            ErrorCode.fail(res, ErrorCode.CODE_300011);
            LOGGER.info("错误码:{}={}", ErrorCode.CODE_300011, ErrorCode.errorMap.get(ErrorCode.CODE_300011));
            return res;
        }
        try
        {
            LOGGER.info("删除活动接口(mapps.activity.one.delete)入口,请求参数==" + LogUtil.getObjectInfo(req));
            String id = req.getActId();
            // TODO: 删除报名信息
            activityMapper.deleteByPrimaryKey(id);
            res.success();
            LOGGER.info("删除活动成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("删除活动信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 分页获取活动信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.activity.list.query", group = "activity", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryActivityResponse getActivityForPage(QueryActivityRequest req)
    {
        QueryActivityResponse res = new QueryActivityResponse();
        try
        {
            LOGGER.info("分页查询活动接口(mapps.activity.list.query)入口,请求参数==" + LogUtil.getObjectInfo(req));
            if (req.getTimestamp() == 0)
            {
                req.setTimestamp(new Date().getTime());
            }

            Map<String, Object> map = initQuery(req);
            map.put("userId", SessionContext.getUserId());
            PageHelper.startPage(req.getOffset(), req.getLimit());
            List<ActivityDetail> list = activityMapper.getActList(map);
            PageInfo<ActivityDetail> page = new PageInfo<ActivityDetail>(list);
            res.setTotal(page.getTotal());
            if (list == null)
            {
                list = new ArrayList<ActivityDetail>();
            }
            else
            {
                for (ActivityDetail info : list)
                {
                    if (StringUtil.isNotEmpty(info.getActPosterUrl()))
                        info.setActPosterUrl(fileService.getWebRoot() + info.getActPosterUrl());
                }
            }
            res.setGetActivityList(list);
            res.setTimestamp(req.getTimestamp());
            if (page.isIsLastPage())
            {
                res.setEndflag(1);
            }
            else
            {
                res.setEndflag(0);
            }
            res.success();
            LOGGER.info("分页查询活动成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("分页查询活动信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 分页获取我参与的活动信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.activity.list.queryMyEnter", group = "activity", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryActivityResponse getMyEnterActivityForPage(QueryActivityRequest req)
    {
        QueryActivityResponse res = new QueryActivityResponse();
        try
        {
            LOGGER.info("分页查询活动接口(mapps.activity.list.queryMyEnter)入口,请求参数==" + LogUtil.getObjectInfo(req));
            if (req.getTimestamp() == 0l)
            {
                req.setTimestamp(new Date().getTime());
            }

            // Map<String, Object> map = initQuery(req);
            Map<String, Object> map = new HashMap<String, Object>();
            // enterPersonId
            map.put("enterPersonId", SessionContext.getUserId());
            map.put("actExpire", req.getActExpire());
            map.put("userId", SessionContext.getUserId());
            map.put("now", new Date());
            if (StringUtils.isNotEmpty(req.getContent()))
            {
                map.put("title", "%" + req.getContent().trim() + "%");
            }
            PageHelper.startPage(req.getOffset(), req.getLimit());
            List<ActivityDetail> list = activityMapper.getMyEnterActList(map);
            PageInfo<ActivityDetail> page = new PageInfo<ActivityDetail>(list);
            res.setTotal(page.getTotal());
            if (list == null)
            {
                list = new ArrayList<ActivityDetail>();
            }
            else
            {
                for (ActivityDetail info : list)
                {
                    if (StringUtil.isNotEmpty(info.getActPosterUrl()))
                        info.setActPosterUrl(fileService.getWebRoot() + info.getActPosterUrl());
                }
            }

            res.setGetActivityList(list);
            res.setTimestamp(req.getTimestamp());
            if (page.isIsLastPage())
            {
                res.setEndflag(1);
            }
            else
            {
                res.setEndflag(0);
            }
            // 是否被设置为会议室管理员
            // res.setAdminFlag(0);
            res.success();
            LOGGER.info("分页查询活动成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("分页查询活动信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 分页获取我创建的活动信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.activity.list.queryMyCreate", group = "activity", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryActivityResponse getMyCreaterActivityForPage(QueryActivityRequest req)
    {
        QueryActivityResponse res = new QueryActivityResponse();
        try
        {
            LOGGER.info("分页查询活动接口(mapps.activity.list.queryMyEnter)入口,请求参数==" + LogUtil.getObjectInfo(req));
            if (req.getTimestamp() == 0l)
            {
                req.setTimestamp(new Date().getTime());
            }

            // Map<String, Object> map = initQuery(req);
            Map<String, Object> map = new HashMap<String, Object>();
            // enterPersonId
            map.put("createId", SessionContext.getUserId());
            map.put("actExpire", req.getActExpire());
            map.put("userId", SessionContext.getUserId());
            map.put("now", new Date());
            if (StringUtils.isNotEmpty(req.getContent()))
            {
                map.put("title", "%" + req.getContent().trim() + "%");
            }
            PageHelper.startPage(req.getOffset(), req.getLimit());
            List<ActivityDetail> list = activityMapper.getMyCreateActList(map);
            PageInfo<ActivityDetail> page = new PageInfo<ActivityDetail>(list);
            res.setTotal(page.getTotal());
            if (list == null)
            {
                list = new ArrayList<ActivityDetail>();
            }
            else
            {
                for (ActivityDetail info : list)
                {
                    if (StringUtil.isNotEmpty(info.getActPosterUrl()))
                        info.setActPosterUrl(fileService.getWebRoot() + info.getActPosterUrl());
                }
            }

            res.setGetActivityList(list);
            res.setTimestamp(req.getTimestamp());
            if (page.isIsLastPage())
            {
                res.setEndflag(1);
            }
            else
            {
                res.setEndflag(0);
            }
            // 是否被设置为会议室管理员
            // res.setAdminFlag(0);
            res.success();
            LOGGER.info("分页查询活动成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("分页查询活动信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public Map<String, Object> initQuery(QueryActivityRequest req) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ecid", SessionContext.getEcId());
        map.put("userId", SessionContext.getUserId());
        map.put("deptOrder", SessionContext.getDeptOrder());
        map.put("now", new Date());
        map.put("timeFlag", new Date(req.getTimestamp()));
        // 权限 创建人 权限表 dept=-1
        // 过期的 未过期的
        map.put("actExpire", req.getActExpire());
        String content = req.getContent();
        String order = req.getOrder();
        // map.put("whiteList", whiteList);
        // 权限判断
        // if (!SessionContext.isAdmin())
        // {
        // LOGGER.debug("普通用户获取活动列表查询条件,userid={},deptorder={}", SessionContext.getUserId(),
        // SessionContext.getDeptOrder());
        // map.put("isUser", "-1");
        // map.put("deptOrder", SessionContext.getDeptOrder());
        // }

        if (StringUtil.isNotEmpty(content))
        {
            map.put("title", "%" + content.trim() + "%");
        }
        if (StringUtil.isNotEmpty(order))
        {
            map.put("order", order);
        }
        return map;
    }

    /**
     * 获取活动详细信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.activity.one.detail", group = "activity", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
    public QueryActivityDetailResponse getMrRoomDetail(QueryActivityDetailRequest req)
    {
        QueryActivityDetailResponse res = new QueryActivityDetailResponse();
        try
        {
            LOGGER.info("查询活动详细信息接口(mapps.activity.one.detail)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("actId", req.getActId());
            map.put("ecid", SessionContext.getEcId());
            map.put("userId", SessionContext.getUserId());
            map.put("now", new Date());
            map.put("deptOrder", SessionContext.getDeptOrder());
            List<ActivityDetail> list = activityMapper.getActivity(map);
            // List<ReservedDate> dList = mrReservedMapper.getReservedDates(map);
            setDetailResponse(res, list);
            // 插入报名人员详情
            AtEnter enter = new AtEnter();
            enter.setActId(req.getActId());
            List<AtEnter> enterList = enterMapper.select(enter);
            // 插入图片详情
            res.setEnterList(enterList);
            AtPhoto photo = new AtPhoto();
            photo.setActId(req.getActId());
            List<AtPhoto> photoList = photoMapper.select(photo);
            for (AtPhoto pInfo : photoList)
            {
                pInfo.setPhoneRoute(StringUtils.isEmpty(pInfo.getPhoneRoute()) ? "" : fileService.getWebRoot() + pInfo.getPhoneRoute());
            }
            res.setPhotoList(photoList);
            LOGGER.info("查询活动详细信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询活动详细信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public void setDetailResponse(QueryActivityDetailResponse res, List<ActivityDetail> list)
    {
        ActivityDetail info = null;
        if (list != null && !list.isEmpty())
        {
            info = list.get(0);
        }
        else
        {
            ErrorCode.fail(res, ErrorCode.CODE_300011);
            return;
        }
        res.setActId(info.getId());
        res.setActTitle(info.getActTitle());
        res.setConTel(info.getConTel());
        res.setActContent(info.getActContent());
        res.setAddress(info.getActAddress());
        res.setNumLimit(info.getNumLimit());
        res.setPhone(info.getPhone());
        res.setName(info.getName());
        res.setActTitle(res.getActTitle());
        res.setIdCard(info.getIdCard());
        res.setRemark(info.getRemark());
        res.setSex(info.getSex());
        res.setCreateId(info.getCreateId());
        res.setCreateName(info.getCreateName());
        res.setEnterNum(info.getEnterNum());
        res.setDisGroupId(info.getDisGroupId());
        if (StringUtil.isNotEmpty(info.getActPosterUrl()))
            res.setActPosterUrl(fileService.getWebRoot() + info.getActPosterUrl());
        else
            res.setActPosterUrl("");
        res.setEnterFlag(info.getEnterFlag());
        res.setCreateFlag(info.getCreateFlag());
        res.setDefaultImage(info.getDefaultImage());
        if (info.getState() > 0)
            res.setJoinFlag("1");
        else
            res.setJoinFlag("0");
        try
        {
            res.setActStartTime(info.getActStartTime());
            res.setActEndTime(info.getActEndTime());
            res.setEnterEndTime(info.getEnterEndTime());
            res.setActStartTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(info.getActStartTime()));
            res.setActEndTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(info.getActEndTime()));
            res.setEnterEndTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(info.getEnterEndTime()));
            if (imOnOff)
            {
                res.setCreateImAccount(thirdPartAccessMosService.queryImAccount(info.getEcid(), info.getCreateId()));
            }
            else
            {
                res.setCreateImAccount("");
            }
        }
        catch (Exception e)
        {
            ErrorCode.fail(res, ErrorCode.CODE_300012);
            return;
        }
        res.success();
    }

    @ServiceMethod(method = "mapps.activity.one.enter", group = "activity", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse getEnter(AddEnterRequest req)
    {
        BaseResponse res = new BaseResponse();
        try
        {
            LOGGER.info("查询活动报名接口(mapps.activity.one.enter)入口,请求参数==" + LogUtil.getObjectInfo(req));
            AtActivity actInfo = activityMapper.selectByPrimaryKey(req.getActId());
            if (actInfo == null)
            {
                ErrorCode.fail(res, ErrorCode.CODE_300011);
                return res;
            }
            if (actInfo.getEnterEndTime().getTime() <= new Date().getTime())
            {
                ErrorCode.fail(res, ErrorCode.CODE_300022);
                return res;
            }
            // 报名是否超限
            if (actInfo.getNumLimit() != UNLIMITED && actInfo.getEnterNum() + 1 > actInfo.getNumLimit())
            {
                ErrorCode.fail(res, ErrorCode.CODE_300019);
                return res;
            }
            AtEnter enterInfo = new AtEnter();
            enterInfo.setActId(req.getActId());
            enterInfo.setEnterPersonId(SessionContext.getUserId());
            enterInfo.setEcid(SessionContext.getEcId());
            int count = enterMapper.selectCount(enterInfo);
            if (count > 0)
            {
                ErrorCode.fail(res, ErrorCode.CODE_300020);
                return res;
            }
            enterInfo.setEnterId(IDGen.uuid());
            enterInfo.setPhone(req.getPhone());
            enterInfo.setName(SessionContext.getUserName());
            enterInfo.setDeptName(SessionContext.getDeptName());
            enterInfo.setIdCard(req.getIdCard());
            enterInfo.setRemark(req.getRemark());
            enterInfo.setSex(req.getSex());
            enterInfo.setEnterTime(new Date());
            enterMapper.insertSelective(enterInfo);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("actId", req.getActId());
            activityMapper.updateActivityByAddEnter(map);
            LOGGER.info("查询活动报名成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询活动报名异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return res;
    }

    @ServiceMethod(method = "mapps.activity.group.join", group = "activity", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
    public BaseResponse joinImGroup(JoinImGroupRequest req)
    {
        BaseResponse res = new BaseResponse();
        if (!imOnOff)
        {
            ErrorCode.fail(res, ErrorCode.CODE_300018);
            return res;
        }
        try
        {
            AtActivity actInfo = activityMapper.selectByPrimaryKey(req.getActId());
            if (actInfo == null)
            {
                ErrorCode.fail(res, ErrorCode.CODE_300011);
                return res;
            }
            JoinImGroupRequest jigReq = new JoinImGroupRequest();
            jigReq.setGroupId(req.getGroupId());
            jigReq.setMembers(SessionContext.getUserId());
            jigReq.setUsername(actInfo.getCreateId());
            JoinImGroupResponse jimRes = thirdPartAccessMosService.joinImGroup(jigReq);
            if (jimRes == null || !BaseResponse.SUCCESS.equals(jimRes.getCode()))
            {
                ImErrorCode.fail(res, jimRes.getCode());
                if (StringUtils.isEmpty(res.getCode()) || StringUtils.isEmpty(res.getError_message()))
                {
                    ErrorCode.fail(res, ErrorCode.CODE_300018);
                }
                return res;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询活动报名异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    @ServiceMethod(method = "mapps.activity.circle.share", group = "activity", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public ShareWorkCircleResponse shareWorkCircle(QueryActivityDetailRequest req)
    {
        ShareWorkCircleResponse res = new ShareWorkCircleResponse();
        res.setImFlag(imOnOff);
        try
        {
            MyUser user = thirdPartAccessService.getUserInfo(SessionContext.getOrgId(), SessionContext.getUserId());
            res.setPhone(user.getPhoneNum());
            if (StringUtils.isEmpty(workCircleId))
            {
                // 圈子功能未配置
                ErrorCode.fail(res, ErrorCode.CODE_300023);
                return res;
            }
            if ("-1".equals(req.getActId()))
            {
                res.setId(workCircleId);
                return res;
            }
            AtActivity actInfo = activityMapper.selectByPrimaryKey(req.getActId());
            if (actInfo == null)
            {
                ErrorCode.fail(res, ErrorCode.CODE_300011);
                return res;
            }
            String appTypeHtml5 = "4";
            res.setComment("");// 用户评论
            res.setTitle(actInfo.getActTitle());// 分享标题
            res.setDesc(actInfo.getActContent());// 分享描述
            res.setAppname(appName);// 分享应用名
            res.setAppid(appId);// 分享应用id
            String imgUrl = "";
            if (StringUtil.isNotEmpty(actInfo.getActPosterUrl()))
                imgUrl = fileService.getWebRoot() + actInfo.getActPosterUrl() + "_200x200";
            else
                imgUrl = "app_local:" + appTypeHtml5 + ":" + appId + ":/logo.png";
            res.setImgUrl(imgUrl);// 分享图标，见imgUrl链接格式定义
            res.setLink("app_scheme://" + appTypeHtml5 + ":" + appId + "/param/index.html#ID-PageDetail/"
                    + actInfo.getId());// 分享链接，见link链接格式
            res.setId(workCircleId);// 圈子id
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询活动报名异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }
}
