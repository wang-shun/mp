package com.fiberhome.mapps.meetingroom.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fiberhome.mapps.contact.pojo.EventParam;
import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.meeting.dao.MrApproveInfoMapper;
import com.fiberhome.mapps.meeting.dao.MrPrivilegeMapper;
import com.fiberhome.mapps.meeting.dao.MtMeetingMapper;
import com.fiberhome.mapps.meeting.dao.MtParticipantsMapper;
import com.fiberhome.mapps.meeting.entity.ClientMeetingInfo;
import com.fiberhome.mapps.meeting.entity.GetMrReserved;
import com.fiberhome.mapps.meeting.entity.MrApproveInfo;
import com.fiberhome.mapps.meeting.entity.MrPrivilege;
import com.fiberhome.mapps.meeting.entity.MrReserved;
import com.fiberhome.mapps.meeting.entity.MrRoom;
import com.fiberhome.mapps.meeting.entity.MtMeeting;
import com.fiberhome.mapps.meeting.entity.MtParticipants;
import com.fiberhome.mapps.meeting.entity.ReservedDetail;
import com.fiberhome.mapps.meeting.entity.ReservedLogContent;
import com.fiberhome.mapps.meeting.entity.ReservedStatus;
import com.fiberhome.mapps.meeting.entity.Room;
import com.fiberhome.mapps.meeting.service.MtMeetingService;
import com.fiberhome.mapps.meeting.service.ThirdPartAccessService;
import com.fiberhome.mapps.meetingroom.request.AddReservedRequest;
import com.fiberhome.mapps.meetingroom.request.DeleteReservedRequest;
import com.fiberhome.mapps.meetingroom.request.QueryApproveRequest;
import com.fiberhome.mapps.meetingroom.request.QueryRequest;
import com.fiberhome.mapps.meetingroom.request.QueryReservedRequest;
import com.fiberhome.mapps.meetingroom.request.UpdateApproveRequest;
import com.fiberhome.mapps.meetingroom.response.AddReservedResponse;
import com.fiberhome.mapps.meetingroom.response.DeleteReservedResponse;
import com.fiberhome.mapps.meetingroom.response.QueryReservedResponse;
import com.fiberhome.mapps.meetingroom.response.QueryResponse;
import com.fiberhome.mapps.meetingroom.utils.ClassUtil;
import com.fiberhome.mapps.meetingroom.utils.ErrorCode;
import com.fiberhome.mapps.meetingroom.utils.LogUtil;
import com.fiberhome.mapps.utils.IDGen;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

import tk.mybatis.mapper.util.StringUtil;

@ServiceMethodBean(version = "1.0")
@Component
public class MrReservedServiceV20 extends MrReservedService
{
    @Autowired
    protected MrPrivilegeMapper      mrPrivilegeMapper;
    @Autowired
    protected MrApproveInfoMapper    mrApproveInfoMapper;
    @Autowired
    protected MtMeetingMapper        mtMeetingMapper;
    @Autowired
    protected ThirdPartAccessService accessService;
    /** 审批同意 */
    public static final String       RESERVED_APPROVE_YES       = "1";
    /** 审批不同意 */
    public static final String       RESERVED_APPROVE_NO        = "0";
    /** 待审批 */
    public static final String       RESERVED_STATUS_APPROVEING = "a";
    /** 审批拒绝 */
    public static final String       RESERVED_STATUS_REJECT     = "r";
    /** 预约成功 */
    public static final int          PUSH_TYPE_OK               = 1;
    /** 预约取消 */
    public static final int          PUSH_TYPE_CANCEL           = 2;
    /** 预约审批通过 */
    public static final int          PUSH_TYPE_PASS             = 3;
    /** 预约审批打回 */
    public static final int          PUSH_TYPE_REJECT           = 4;
    /** 预约提示管理员审批 */
    public static final int          PUSH_TYPE_APPROVE          = 5;
    /** 需审批的预约过期 */
    public static final int          PUSH_TYPE_OVER             = 6;
    /** 被管理员预约取消 */
    public static final int          PUSH_TYPE_ADMIN_CANCEL     = 7;

    @Value("${meetingroom.scheduled.on-off}")
    Boolean                          scheduledFlag;

    /**
     * 根据日期查询各会议室准备中预约信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.reserved.servicedetail", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryResponse getDetailByService(QueryRequest req)
    {
        QueryResponse res = new QueryResponse();
        try
        {
            LOGGER.info(
                    "查询全天会议室预约情况接口(mapps.meetingroom.reserved.servicedetail)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ecid", SessionContext.getEcId());
            if (StringUtil.isNotEmpty(req.getReservedDate()))
                map.put("reservedDate", req.getReservedDate());
            List<Room> rList = mrReservedMapper.getRoomByDo(map);
            for (Room room : rList)
            {
                map.put("roomId", room.getRoomId());
                map.put("status", "'1','2','3'");
                List<ReservedDetail> rdList = mrReservedMapper.getReservedDetail(map);
                if (rdList != null && rdList.size() > 0)
                {
                    // 电话号码处理
                    List<String> userIdList = new ArrayList<String>();
                    for (ReservedDetail info : rdList)
                    {
                        info.setPhoneNum("");
                        userIdList.add(info.getReservedUserId());
                    }
                    List<MyUser> muList = thirdPartAccessService.getUserInfos(SessionContext.getOrgId(),
                            StringUtils.join(userIdList, ","));
                    if (muList != null && muList.size() > 0)
                    {
                        for (ReservedDetail info : rdList)
                        {
                            for (MyUser muInfo : muList)
                            {
                                if (info.getReservedUserId().equals(muInfo.getLoginId()))
                                {
                                    info.setPhoneNum(muInfo.getPhoneNum());
                                    info.setDeptName(muInfo.getDeptFullName());
                                    break;
                                }
                            }
                        }
                    }
                }
                room.setReservedDetailList(rdList);
            }
            res.setRoomList(rList);
            LOGGER.info("查询全天会议室预约情况成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询全天会议室预约情况异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 2.0新增 预约详情接口 手机端会议室详情下方的预约列表
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.reserved.detail", group = "room", groupTitle = "API", version = "2.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryResponse getMrRoomDetail(QueryRequest req)
    {
        QueryResponse res = new QueryResponse();
        try
        {
            LOGGER.info("查询会议室预约详细信息接口(mapps.meetingroom.reserved.detail)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            if (StringUtil.isNotEmpty(req.getRoomId()))
                map.put("roomId", req.getRoomId());
            if (StringUtil.isNotEmpty(req.getReservedDate()))
                map.put("reservedDate", req.getReservedDate());
            map.put("status", "'1','2','a'");
            List<ReservedDetail> rdList = mrReservedMapper.getReservedDetail(map);
            if (rdList != null && rdList.size() > 0)
            {
                // 电话号码处理
                List<String> userIdList = new ArrayList<String>();
                for (ReservedDetail info : rdList)
                {
                    info.setPhoneNum("");
                    userIdList.add(info.getReservedUserId());
                }
                List<MyUser> muList = thirdPartAccessService.getUserInfos(SessionContext.getOrgId(),
                        StringUtils.join(userIdList, ","));
                if (muList != null && muList.size() > 0)
                {
                    for (ReservedDetail info : rdList)
                    {
                        for (MyUser muInfo : muList)
                        {
                            if (info.getReservedUserId().equals(muInfo.getLoginId()))
                            {
                                info.setPhoneNum(muInfo.getPhoneNum());
                                break;
                            }
                        }
                    }
                }
            }
            res.setReservedDetailList(rdList);
            LOGGER.info("查询会议室预约详细信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询会议室预约详细信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 预约详情 手机端推送事件跳转到的详情页面
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.reserved.detailone", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryResponse getDetailOne(QueryRequest req)
    {
        QueryResponse res = new QueryResponse();
        try
        {
            LOGGER.info("查询会议室预约详细信息接口(mapps.meetingroom.reserved.detailone)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("reservedId", req.getReservedId());
            map.put("userId", SessionContext.getUserId());
            ReservedDetail rd = mrReservedMapper.getReservedDetailOne(map);
            if (rd != null)
            {
                // 电话号码处理
                rd.setPhoneNum("");
                List<MyUser> muList = thirdPartAccessService.getUserInfos(SessionContext.getOrgId(),
                        rd.getReservedUserId());
                if (muList != null && muList.size() > 0)
                {
                    for (MyUser muInfo : muList)
                    {
                        if (rd.getReservedUserId().equals(muInfo.getLoginId()))
                        {
                            rd.setPhoneNum(muInfo.getPhoneNum());
                            break;
                        }
                    }
                }
                rd.setApprove(getReservedStatus(req.getReservedId()));
            }
            List<ReservedDetail> reservedDetailList = new ArrayList<ReservedDetail>();
            reservedDetailList.add(rd);
            res.setReservedDetailList(reservedDetailList);
            LOGGER.info("查询会议室预约详细信息成功");
        }
        catch (

        Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询会议室预约详细信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 预约状态 分别获取预约的状态与当前登录人对此预约的审批状态进行判别
     * 
     * @param reservedId
     * @return
     */
    public boolean getReservedStatus(String reservedId)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("reservedId", reservedId);
        map.put("userId", SessionContext.getUserId());
        ReservedStatus rs = mrReservedMapper.getReservedStatus(map);
        if (rs != null)
        {
            if (RESERVED_STATUS_APPROVEING.equals(rs.getStatus()) && RESERVED_APPROVE_NO.equals(rs.getApproved()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 预定会议室 2.0 新增审批流程处理 与服务人员的推送功能
     * 
     * @param req
     * @return
     */
    // @ServiceMethod(method = "mapps.meetingroom.reserved.add", group = "reserved", groupTitle = "API", version =
    // "2.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    // @Transactional
    public AddReservedResponse addMrReserved(AddReservedRequest req)
    {
        LOGGER.debug("预定会议室接口(mapps.meetingroom.reserved.add)入口,请求参数==" + LogUtil.getObjectInfo(req));
        AddReservedResponse res = new AddReservedResponse();
        String roomId = req.getRoomId();
        MrRoom roomEntity = mrRoomMapper.selectByPrimaryKey(roomId);
        String id = IDGen.uuid();
        MrReserved info = new MrReserved();
        info.setRoomId(roomId);
        info.setMeetingName(req.getMeetingName());
        if (!initReservedDate(info, req, res))
        {
            return res;
        }
        if (checkRoomReserved(roomId, info.getOrderTimeBegin(), info.getOrderTimeEnd(), res))
        {
            return res;
        }
        info.setReservedTime(new Date());
        info.setStatus(RESERVED_STATUS_DO);
        info.setEcid(SessionContext.getEcId());
        info.setReservedUserId(SessionContext.getUserId());
        info.setReservedUserName(SessionContext.getUserName());
        info.setReservedUserDept(SessionContext.getDeptName());
        info.setReservedUserDeptId(SessionContext.getDeptId());
        info.setReservedRemark(req.getReservedRemark());
        info.setMeetingTimeEnd(info.getOrderTimeEnd());
        info.setId(id);
        // 判断是否是特殊会议室
        info.setNeedApprove(RESERVED_APPROVE_NO);
        MrPrivilege mp = new MrPrivilege();
        mp.setRoomId(roomId);
        mp.setPriv(MrPrivilegeService.PRIV_ADMIN);
        List<MrPrivilege> mpList = mrPrivilegeMapper.select(mp);
        List<MrApproveInfo> maiList = new ArrayList<MrApproveInfo>();
        if (mpList != null && mpList.size() > 0)
        {
            info.setNeedApprove(RESERVED_APPROVE_YES);
            info.setStatus(RESERVED_STATUS_APPROVEING);
            for (MrPrivilege mpInfo : mpList)
            {

                MrApproveInfo mai = new MrApproveInfo();
                mai.setReservedId(id);
                mai.setUserId(mpInfo.getEntityId());
                mai.setApproved("0");
                mai.setApproveTime(new Date());
                maiList.add(mai);
            }
        }

        ReservedLogContent olc = new ReservedLogContent(roomEntity.getName(),
                new SimpleDateFormat("yyyy-MM-dd").format(info.getOrderTimeBegin()) + " "
                        + new SimpleDateFormat("HH:mm").format(info.getOrderTimeBegin()) + "-"
                        + new SimpleDateFormat("HH:mm").format(info.getOrderTimeEnd()),
                req.getMeetingName(), "预定会议室成功");
        try
        {
            mrReservedMapper.insertSelective(info);
            // 初始化审批记录
            addApproves(maiList);
            // 推送服务人员
            if (RESERVED_STATUS_DO.equals(info.getStatus()))
            {
                pushEvent(info, PUSH_TYPE_OK, SessionContext.getEcId(), req.getMeetingId());
            }
            else if (RESERVED_STATUS_APPROVEING.equals(info.getStatus()))
            {
                // 推送管理员提示审批
                pushEvent(info, PUSH_TYPE_APPROVE, SessionContext.getEcId(), req.getMeetingId());
            }
            res.setReservedId(id);
            res.setStatus(info.getStatus());
            opLogService.addModuleOpLog(OpLogService.MODULE_RESERVED, id, OpLogService.OP_RESERVED, olc,
                    OpLogService.RESULT_YES, SessionContext.getEcId(), SessionContext.getUserId(),
                    SessionContext.getUserName(), SessionContext.getDeptId(), SessionContext.getDeptName());
            res.success();
            LOGGER.info("预定会议室操作成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("预定会议室异常：{}", e);
            olc.setMessage("预定会议室操作失败," + e.getMessage());
            opLogService.addModuleOpLog(OpLogService.MODULE_RESERVED, id, OpLogService.OP_RESERVED, olc,
                    OpLogService.RESULT_NO, SessionContext.getEcId(), SessionContext.getUserId(),
                    SessionContext.getUserName(), SessionContext.getDeptId(), SessionContext.getDeptName());
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public void addApproves(List<MrApproveInfo> maiList) throws Exception
    {
        for (MrApproveInfo maiInfo : maiList)
        {
            mrApproveInfoMapper.insertSelective(maiInfo);
        }
    }

    /**
     * 获取审批列表信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.reserved.queryapprove", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryReservedResponse getReviewForReserved(QueryApproveRequest req)
    {
        QueryReservedResponse res = new QueryReservedResponse();
        try
        {
            LOGGER.debug("分页获取审批信息接口(mapps.meetingroom.reserved.queryapprove)入口,请求参数==" + LogUtil.getObjectInfo(req));
            if (req.getTimestamp() == 0l)
            {
                req.setTimestamp(new Date().getTime());
            }
            PageHelper.startPage(req.getOffset(), req.getLimit());
            Map<String, Object> map = initQuery(req);
            List<GetMrReserved> list = mrReservedMapper.getReviewForReserved(map);
            PageInfo<GetMrReserved> page = new PageInfo<GetMrReserved>(list);
            if (list != null && list.size() > 0)
            {
                // 电话号码处理
                List<String> userIdList = new ArrayList<String>();
                for (GetMrReserved info : list)
                {
                    info.setPhone("");
                    userIdList.add(info.getReservedUserId());
                }
                List<MyUser> muList = thirdPartAccessService.getUserInfos(SessionContext.getOrgId(),
                        StringUtils.join(userIdList, ","));
                if (muList != null && muList.size() > 0)
                {
                    for (GetMrReserved info : list)
                    {
                        for (MyUser muInfo : muList)
                        {
                            if (info.getReservedUserId().equals(muInfo.getLoginId()))
                            {
                                info.setPhone(muInfo.getPhoneNum());
                                break;
                            }
                        }
                    }
                }
            }
            res.setTotal(page.getTotal());
            res.setMrReservedList(list);
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
            LOGGER.info("分页获取审批信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("分页获取审批信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public Map<String, Object> initQuery(QueryApproveRequest req) throws Exception
    {
        String status = req.getStatus();
        long time = req.getTimestamp();

        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(status))
        {
            String[] sArr = status.split(",");
            String param = "";
            for (int i = 0; i < sArr.length; i++)
            {
                if (i == 0)
                {
                    param += "'" + sArr[i] + "'";
                }
                else
                {
                    param += ",'" + sArr[i] + "'";
                }
            }
            map.put("approved", param);
        }
        else
        {
            map.put("approved", "'-1'");
        }
        if (StringUtil.isNotEmpty(req.getReservedId()))
        {
            map.put("reservedId", req.getReservedId());
        }
        map.put("userId", SessionContext.getUserId());
        if (req.getNoPage() != 1)
        {
            map.put("time", new Date(time));
        }
        map.put("now", new Date());
        map.put("ecid", SessionContext.getEcId());
        return map;
    }

    /**
     * 会议预约审批
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.reserved.updateapprove", group = "reserved", groupTitle = "API", version = "2.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional
    public AddReservedResponse updateApprove(UpdateApproveRequest req)
    {
        LOGGER.debug("会议预约审批接口(mapps.meetingroom.reserved.updateapprove)入口,请求参数==" + LogUtil.getObjectInfo(req));
        AddReservedResponse res = new AddReservedResponse();
        try
        {
            if (StringUtil.isEmpty(req.getReservedId()))
            {
                ErrorCode.fail(res, ErrorCode.CODE_300013);
                return res;
            }
            MrReserved r = new MrReserved();
            r.setId(req.getReservedId());
            MrReserved entity = mrReservedMapper.selectOne(r);
            MtMeeting mmEntity = new MtMeeting();
            mmEntity.setReservedId(req.getReservedId());
            mmEntity = mtMeetingMapper.selectOne(mmEntity);
            if (entity == null)
            {
                ErrorCode.fail(res, ErrorCode.CODE_300013);
                return res;
            }
            // 预约状态已变更
            if (!RESERVED_STATUS_APPROVEING.equals(entity.getStatus()))
            {
                ErrorCode.fail(res, ErrorCode.CODE_300010);
                return res;
            }
            // 预约超期过滤
            if (entity.getOrderTimeEnd().getTime() < new Date().getTime())
            {
                ErrorCode.fail(res, ErrorCode.CODE_300017);
                return res;
            }
            MrApproveInfo ai = new MrApproveInfo();
            ai.setReservedId(req.getReservedId());
            ai.setUserId(SessionContext.getUserId());
            ai.setApproveTime(new Date());
            ai.setApproved("1");
            ai.setApproveResult(req.getApproved());
            mrApproveInfoMapper.updateByPrimaryKeySelective(ai);

            MtMeeting mm = new MtMeeting();
            mm.setReservedId(req.getReservedId());
            mm = mtMeetingMapper.selectOne(mm);

            if ("0".equals(req.getApproved()))
            {
                // 不同意 修改预约记录审批状态

                r.setStatus(RESERVED_STATUS_REJECT);
                mrReservedMapper.updateByPrimaryKeySelective(r);
                mmEntity.setStatus(RESERVED_STATUS_REJECT);
                mtMeetingMapper.updateByPrimaryKeySelective(mmEntity);
                if (mm != null && StringUtil.isNotEmpty(mm.getId()))
                    pushEvent(entity, PUSH_TYPE_REJECT, SessionContext.getEcId(), mm.getId());
            }
            else
            {
                // 同意 判断是否都同意 全通过则修改预约记录状态
                MrApproveInfo mai = new MrApproveInfo();
                mai.setReservedId(req.getReservedId());
                mai.setApproved("0");
                if (mrApproveInfoMapper.selectCount(mai) == 0)
                {
                    r.setStatus(RESERVED_STATUS_DO);
                    mrReservedMapper.updateByPrimaryKeySelective(r);
                    mmEntity.setStatus(RESERVED_STATUS_DO);
                    mtMeetingMapper.updateByPrimaryKeySelective(mmEntity);
                    if (mm != null && StringUtil.isNotEmpty(mm.getId()))
                        pushEvent(entity, PUSH_TYPE_PASS, SessionContext.getEcId(), mm.getId());
                }
            }
            res.success();
            LOGGER.info("会议预约审批操作成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("会议预约审批异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 已预约会议室操作(取消预约，结束预约，删除预约)
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.reserved.delete", group = "reserved", groupTitle = "API", version = "2.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public DeleteReservedResponse delMrReserved1(DeleteReservedRequest req)
    {
        LOGGER.info("已预约会议室操作接口(mapps.meetingroom.reserved.delete)入口,请求参数==reservedId:{},operationType:{},isAdmin:{}",
                req.getReservedId(), req.getOperationType(), SessionContext.isAdmin());
        DeleteReservedResponse res = new DeleteReservedResponse();
        MrReserved entity = mrReservedMapper.selectByPrimaryKey(req.getReservedId());
        if (entity == null)
        {
            ErrorCode.fail(res, ErrorCode.CODE_300013);
            return res;
        }
        MrRoom roomEntity = mrRoomMapper.selectByPrimaryKey(entity.getRoomId());
        if (roomEntity == null)
        {
            ErrorCode.fail(res, ErrorCode.CODE_300011);
            return res;
        }
        String depId = "-1";
        String depName = "";
        // if (!SessionContext.isAdmin())
        // {
        depId = SessionContext.getDeptId();
        depName = SessionContext.getDeptName();
        // }
        String op = "";
        String content = "";
        String srcStatus = entity.getStatus();
        String operationType = req.getOperationType();
        String targetStatus = null;
        if ((RESERVED_STATUS_DO.equals(srcStatus) || RESERVED_STATUS_APPROVEING.equals(srcStatus))
                && OPERATION_TYPE1.equals(operationType))
        {
            targetStatus = RESERVED_STATUS_CANCEL;
            op = OpLogService.OP_CANCEL;
            content = "取消预约";
            MtMeeting mm = new MtMeeting();
            mm.setReservedId(req.getReservedId());
            mm = mtMeetingMapper.selectOne(mm);
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("meetingId", mm.getId());
            reqMap.put("ecid", mm.getEcid());
            reqMap.put("userId", mm.getSponsor());
            ClientMeetingInfo meeting = mtMeetingMapper.getMeetingInfo(reqMap);
            // 推送给参会人员
            String title = meeting.getMeetingName() + "会议取消";
            String content1 = "原定于" + meeting.getBeginTimeStr() + "-" + meeting.getEndTimeStr() + "的"
                    + meeting.getMeetingName() + "会议已被" + SessionContext.getUserName() + "取消";
            // sendMsg(req.getMeetingId(), meeting.getReservedId(), "会议取消", "");
            if (meeting.getStatus().equals(MtMeetingService.RESERVED_STATUS_APPROVEING))
            {
                thirdPartAccessService.pushReceiveEvent(SessionContext.getEcId(), meeting.getSponsor(), title, content1,
                        mm.getId(), null);
            }
            else
            {
                sendMsg(mm.getId(), meeting.getReservedId(), title, content1);
            }
            if (RESERVED_STATUS_APPROVEING.equals(srcStatus))
            {
                if (mm != null && StringUtil.isNotEmpty(mm.getId()))
                    pushEvent(entity, PUSH_TYPE_ADMIN_CANCEL, SessionContext.getEcId(), mm.getId());
            }
            // 取消“准备中”状态会议室，推送“取消会议室预定”代办提醒至会议室服务员。
            if (RESERVED_STATUS_DO.equals(srcStatus))
            {
                if (mm != null && StringUtil.isNotEmpty(mm.getId()))
                    pushEvent(entity, PUSH_TYPE_CANCEL, SessionContext.getEcId(), mm.getId());
            }
        }
        else if (RESERVED_STATUS_DOING.equals(srcStatus) && OPERATION_TYPE2.equals(operationType))
        {
            targetStatus = RESERVED_STATUS_DONE;
            op = OpLogService.OP_OVER;
            content = "结束预约";
            entity.setMeetingTimeEnd(new Date());
        }
        else if (RESERVED_STATUS_DONE.equals(srcStatus) && OPERATION_TYPE3.equals(operationType))
        {
            targetStatus = RESERVED_STATUS_DELETE;
            op = OpLogService.OP_DELETE;
            content = "删除预约";
        }
        else if (RESERVED_STATUS_CANCEL.equals(srcStatus) && OPERATION_TYPE3.equals(operationType))
        {
            targetStatus = RESERVED_STATUS_DELETE;
            op = OpLogService.OP_DELETE;
            content = "删除预约";
        }
        else if (RESERVED_STATUS_REJECT.equals(srcStatus) && OPERATION_TYPE3.equals(operationType))
        {
            targetStatus = RESERVED_STATUS_DELETE;
            op = OpLogService.OP_DELETE;
            content = "删除预约";
        }
        else
        {
            ErrorCode.fail(res, ErrorCode.CODE_300010);
            return res;
        }

        ReservedLogContent olc = new ReservedLogContent(roomEntity.getName(),
                new SimpleDateFormat("yyyy-MM-dd").format(entity.getOrderTimeBegin()) + " "
                        + new SimpleDateFormat("HH:mm").format(entity.getOrderTimeBegin()) + "-"
                        + new SimpleDateFormat("HH:mm").format(entity.getOrderTimeEnd()),
                entity.getMeetingName(), content + "操作成功");
        try
        {
            LOGGER.info("已预约会议室操作接口(mapps.meetingroom.reserved.delete)入口,请求参数==" + LogUtil.getObjectInfo(req));
            LOGGER.info("预约信息==" + LogUtil.getObjectInfo(entity));
            entity.setStatus(targetStatus);
            mrReservedMapper.updateByPrimaryKeySelective(entity);
            opLogService.addModuleOpLog(OpLogService.MODULE_RESERVED, entity.getId(), op, olc, OpLogService.RESULT_YES,
                    SessionContext.getEcId(), SessionContext.getUserId(), SessionContext.getUserName(), depId, depName);
            res.success();
            LOGGER.info("已预约会议室操作成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("已预约会议室操作异常：{}", e);
            olc.setMessage(content + "操作失败," + e.getMessage());
            opLogService.addModuleOpLog(OpLogService.MODULE_RESERVED, entity.getId(), op, olc, OpLogService.RESULT_NO,
                    SessionContext.getEcId(), SessionContext.getUserId(), SessionContext.getUserName(), depId, depName);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public static final String PARTICIPANT_TYPE_INNER   = "inner";
    public static final String PARTICIPANT_TYPE_OUTER   = "outer";
    public static final String PARTICIPANT_TYPE_SERVICE = "service";
    @Autowired
    MtParticipantsMapper       mtParticipantsMapper;

    public void sendMsg(String meetingId, String reservedId, String title, String content)
    {
        Map<String, Object> parMap = new HashMap<String, Object>();
        parMap.put("meetingId", meetingId);
        parMap.put("personType", PARTICIPANT_TYPE_INNER);
        List<MtParticipants> innerList = mtParticipantsMapper.getNoSendInnerByMeetingId(parMap);
        List<String> iList = new ArrayList<String>();
        List<String> sList = new ArrayList<String>();
        for (MtParticipants mpInfo : innerList)
        {
            if (PARTICIPANT_TYPE_INNER.equals(mpInfo.getPersonType()))
                iList.add(mpInfo.getPersonId());
            if (PARTICIPANT_TYPE_SERVICE.equals(mpInfo.getPersonType()))
                sList.add(mpInfo.getPersonId());
        }
        // EventParam param = mrReservedServiceV20.getEventParamByReservedId(reservedId);
        accessService.pushReceiveEvent(SessionContext.getEcId(), StringUtils.join(iList, ","), title, content,
                meetingId, null);
        if (sList != null && sList.size() > 0)
            accessService.pushReceiveEventForService(SessionContext.getEcId(), StringUtils.join(sList, ","), title,
                    content, meetingId, null);
    }

    public DeleteReservedResponse delMrReserved(DeleteReservedRequest req)
    {
        LOGGER.info("已预约会议室操作接口(mapps.meetingroom.reserved.delete)入口,请求参数==reservedId:{},operationType:{},isAdmin:{}",
                req.getReservedId(), req.getOperationType(), SessionContext.isAdmin());
        DeleteReservedResponse res = new DeleteReservedResponse();
        MrReserved entity = mrReservedMapper.selectByPrimaryKey(req.getReservedId());
        if (entity == null)
        {
            ErrorCode.fail(res, ErrorCode.CODE_300013);
            return res;
        }
        MrRoom roomEntity = mrRoomMapper.selectByPrimaryKey(entity.getRoomId());
        if (roomEntity == null)
        {
            ErrorCode.fail(res, ErrorCode.CODE_300011);
            return res;
        }
        String depId = "-1";
        String depName = "";
        // if (!SessionContext.isAdmin())
        // {
        depId = SessionContext.getDeptId();
        depName = SessionContext.getDeptName();
        // }
        String op = "";
        String content = "";
        String srcStatus = entity.getStatus();
        String operationType = req.getOperationType();
        String targetStatus = null;
        if ((RESERVED_STATUS_DO.equals(srcStatus) || RESERVED_STATUS_APPROVEING.equals(srcStatus))
                && OPERATION_TYPE1.equals(operationType))
        {
            targetStatus = RESERVED_STATUS_CANCEL;
            op = OpLogService.OP_CANCEL;
            content = "取消预约";
            MtMeeting mm = new MtMeeting();
            mm.setReservedId(req.getReservedId());
            mm = mtMeetingMapper.selectOne(mm);
            if (RESERVED_STATUS_APPROVEING.equals(srcStatus))
            {
                if (mm != null && StringUtil.isNotEmpty(mm.getId()))
                    pushEvent(entity, PUSH_TYPE_ADMIN_CANCEL, SessionContext.getEcId(), mm.getId());
            }
            // 取消“准备中”状态会议室，推送“取消会议室预定”代办提醒至会议室服务员。
            if (RESERVED_STATUS_DO.equals(srcStatus))
            {
                if (mm != null && StringUtil.isNotEmpty(mm.getId()))
                    pushEvent(entity, PUSH_TYPE_CANCEL, SessionContext.getEcId(), mm.getId());
            }
        }
        else if (RESERVED_STATUS_DOING.equals(srcStatus) && OPERATION_TYPE2.equals(operationType))
        {
            targetStatus = RESERVED_STATUS_DONE;
            op = OpLogService.OP_OVER;
            content = "结束预约";
            entity.setMeetingTimeEnd(new Date());
        }
        else if (RESERVED_STATUS_DONE.equals(srcStatus) && OPERATION_TYPE3.equals(operationType))
        {
            targetStatus = RESERVED_STATUS_DELETE;
            op = OpLogService.OP_DELETE;
            content = "删除预约";
        }
        else if (RESERVED_STATUS_CANCEL.equals(srcStatus) && OPERATION_TYPE3.equals(operationType))
        {
            targetStatus = RESERVED_STATUS_DELETE;
            op = OpLogService.OP_DELETE;
            content = "删除预约";
        }
        else if (RESERVED_STATUS_REJECT.equals(srcStatus) && OPERATION_TYPE3.equals(operationType))
        {
            targetStatus = RESERVED_STATUS_DELETE;
            op = OpLogService.OP_DELETE;
            content = "删除预约";
        }
        else
        {
            ErrorCode.fail(res, ErrorCode.CODE_300010);
            return res;
        }

        ReservedLogContent olc = new ReservedLogContent(roomEntity.getName(),
                new SimpleDateFormat("yyyy-MM-dd").format(entity.getOrderTimeBegin()) + " "
                        + new SimpleDateFormat("HH:mm").format(entity.getOrderTimeBegin()) + "-"
                        + new SimpleDateFormat("HH:mm").format(entity.getOrderTimeEnd()),
                entity.getMeetingName(), content + "操作成功");
        try
        {
            LOGGER.info("已预约会议室操作接口(mapps.meetingroom.reserved.delete)入口,请求参数==" + LogUtil.getObjectInfo(req));
            LOGGER.info("预约信息==" + LogUtil.getObjectInfo(entity));
            entity.setStatus(targetStatus);
            mrReservedMapper.updateByPrimaryKeySelective(entity);
            opLogService.addModuleOpLog(OpLogService.MODULE_RESERVED, entity.getId(), op, olc, OpLogService.RESULT_YES,
                    SessionContext.getEcId(), SessionContext.getUserId(), SessionContext.getUserName(), depId, depName);
            res.success();
            LOGGER.info("已预约会议室操作成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("已预约会议室操作异常：{}", e);
            olc.setMessage(content + "操作失败," + e.getMessage());
            opLogService.addModuleOpLog(OpLogService.MODULE_RESERVED, entity.getId(), op, olc, OpLogService.RESULT_NO,
                    SessionContext.getEcId(), SessionContext.getUserId(), SessionContext.getUserName(), depId, depName);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public void pushEvent(MrReserved entity, int pushType, String ecid, String meetingId)
    {
        MrRoom roomEntity = mrRoomMapper.selectByPrimaryKey(entity.getRoomId());
        if (roomEntity == null)
        {
            return;
        }
        MrPrivilege mp = new MrPrivilege();
        List<MrPrivilege> mpList = new ArrayList<MrPrivilege>();
        EventParam param = getEventParam(entity, roomEntity);
        switch (pushType)
        {
            case PUSH_TYPE_OK:
                mp.setRoomId(entity.getRoomId());
                mp.setPriv(MrPrivilegeService.PRIV_SERVICE);
                mpList = mrPrivilegeMapper.select(mp);
                if (mpList != null && mpList.size() > 0)
                {
                    // accessService.pushReceiveEventForService(ecid, ClassUtil.getUserIds(mpList), "预定成功", "",
                    // meetingId,
                    // param);
                    accessService.pushReceiveEventForService(ecid, ClassUtil.getUserIds(mpList), "会议预定",
                            entity.getReservedUserName() + "预定的"
                                    + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeBegin()) + "-"
                                    + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeEnd()) + " "
                                    + roomEntity.getName() + "已预定成功",
                            meetingId, null);
                }
                break;
            case PUSH_TYPE_APPROVE:
                mp.setRoomId(entity.getRoomId());
                mp.setPriv(MrPrivilegeService.PRIV_ADMIN);
                mpList = mrPrivilegeMapper.select(mp);
                if (mpList != null && mpList.size() > 0)
                {
                    // thirdPartAccessService.pushReceiveEvent(ecid, ClassUtil.getUserIds(mpList), "预定审批", "",
                    // entity.getId(), param);
                    thirdPartAccessService.pushReceiveEvent(ecid, ClassUtil.getUserIds(mpList), "会议预定",
                            SessionContext.getUserName() + "预定的"
                                    + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeBegin()) + "-"
                                    + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeEnd()) + " "
                                    + roomEntity.getName() + "等待您审批",
                            entity.getId(), null);
                }
                break;
            case PUSH_TYPE_CANCEL:
                mp.setRoomId(entity.getRoomId());
                mp.setPriv(MrPrivilegeService.PRIV_SERVICE);
                mpList = mrPrivilegeMapper.select(mp);
                if (mpList != null && mpList.size() > 0)
                {
                    // accessService.pushReceiveEventForService(ecid, ClassUtil.getUserIds(mpList), "预定取消", "",
                    // meetingId,
                    // param);
                    accessService.pushReceiveEventForService(ecid, ClassUtil.getUserIds(mpList), "取消会议预定",
                            entity.getReservedUserName() + "预定的"
                                    + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeBegin()) + "-"
                                    + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeEnd()) + " "
                                    + roomEntity.getName() + "已被取消",
                            meetingId, null);
                }
                break;
            case PUSH_TYPE_ADMIN_CANCEL:
                mp.setRoomId(entity.getRoomId());
                mp.setPriv(MrPrivilegeService.PRIV_ADMIN);
                mpList = mrPrivilegeMapper.select(mp);
                if (mpList != null && mpList.size() > 0)
                {
                    // accessService.pushReceiveEventForService(ecid, ClassUtil.getUserIds(mpList), "预定取消", "",
                    // meetingId,
                    // param);
                    thirdPartAccessService.pushReceiveEvent(ecid, ClassUtil.getUserIds(mpList), "取消会议预定",
                            entity.getReservedUserName() + "预定的"
                                    + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeBegin()) + "-"
                                    + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeEnd()) + " "
                                    + roomEntity.getName() + "已被取消",
                            meetingId, null);
                }
                break;
            case PUSH_TYPE_PASS:
                // accessService.pushReceiveEvent(ecid, entity.getReservedUserId(), "通过审核", "", meetingId, param);
                accessService.pushReceiveEvent(ecid, entity.getReservedUserId(), "会议预定通过审核",
                        "您预定的" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeBegin()) + "-"
                                + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeEnd()) + " "
                                + roomEntity.getName() + "已通过审核",
                        meetingId, null);
                mp.setRoomId(entity.getRoomId());
                mp.setPriv(MrPrivilegeService.PRIV_SERVICE);
                mpList = mrPrivilegeMapper.select(mp);
                if (mpList != null && mpList.size() > 0)
                {
                    // accessService.pushReceiveEventForService(ecid, ClassUtil.getUserIds(mpList), "通过审核", "",
                    // meetingId,
                    // param);
                    accessService.pushReceiveEventForService(ecid, ClassUtil.getUserIds(mpList), "会议预定通过审核",
                            entity.getReservedUserName() + "预定的"
                                    + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeBegin()) + "-"
                                    + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeEnd()) + " "
                                    + roomEntity.getName() + "已通过审核",
                            meetingId, null);
                }
                break;
            case PUSH_TYPE_REJECT:
                // accessService.pushReceiveEvent(ecid, entity.getReservedUserId(), "未通过审核", "", meetingId, param);
                accessService.pushReceiveEvent(ecid, entity.getReservedUserId(), "会议预定未通过审核",
                        "您预定的" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeBegin()) + "-"
                                + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeEnd()) + " "
                                + roomEntity.getName() + "未通过审核",
                        meetingId, null);
                break;
            case PUSH_TYPE_OVER:
                // accessService.pushReceiveEvent(ecid, entity.getReservedUserId(), "审核过期", "", meetingId, param);
                accessService.pushReceiveEvent(ecid, entity.getReservedUserId(), "会议预定审核过期",
                        "您预定的" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeBegin()) + "-"
                                + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeEnd()) + " "
                                + roomEntity.getName() + "审核过期已取消",
                        meetingId, null);
                break;

            default:
                break;
        }

    }

    public EventParam getEventParam(MrReserved entity, MrRoom roomEntity)
    {
        EventParam param = new EventParam();
        List<String> list = new ArrayList<String>();
        list.add("召开人：" + entity.getReservedUserName());
        list.add("开始时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeBegin()));
        list.add("结束时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeEnd()));
        list.add("会议室：" + roomEntity.getName());
        param.setSummaryextend(list);
        LOGGER.debug("*******************************" + LogUtil.getObjectInfo(param));
        return param;
    }

    public EventParam getEventParamByReservedId(String reservedId)
    {
        MrReserved r = new MrReserved();
        r.setId(reservedId);
        MrReserved entity = mrReservedMapper.selectOne(r);
        MrRoom roomEntity = mrRoomMapper.selectByPrimaryKey(entity.getRoomId());
        return getEventParam(entity, roomEntity);
    }

    @ServiceMethod(method = "mapps.meetingroom.reserved.query", group = "room", groupTitle = "API", version = "2.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryReservedResponse getMrReservedForPage(QueryReservedRequest req)
    {
        QueryReservedResponse res = new QueryReservedResponse();
        try
        {
            LOGGER.debug("分页获取已预约会议室信息接口(mapps.meetingroom.reserved.query)入口,请求参数==" + LogUtil.getObjectInfo(req));
            if (req.getTimestamp() == 0l)
            {
                req.setTimestamp(new Date().getTime());
            }
            PageHelper.startPage(req.getOffset(), req.getLimit());
            Map<String, Object> map = initQuery(req);
            List<GetMrReserved> list = mrReservedMapper.getMrReservedV20(map);
            PageInfo<GetMrReserved> page = new PageInfo<GetMrReserved>(list);
            if (list != null && list.size() > 0)
            {
                // 电话号码处理
                List<String> userIdList = new ArrayList<String>();
                for (GetMrReserved info : list)
                {
                    info.setPhone("");
                    userIdList.add(info.getReservedUserId());
                }
                List<MyUser> muList = thirdPartAccessService.getUserInfos(SessionContext.getOrgId(),
                        StringUtils.join(userIdList, ","));
                if (muList != null && muList.size() > 0)
                {
                    for (GetMrReserved info : list)
                    {
                        for (MyUser muInfo : muList)
                        {
                            if (info.getReservedUserId().equals(muInfo.getLoginId()))
                            {
                                info.setPhone(muInfo.getPhoneNum());
                                break;
                            }
                        }
                    }
                }
            }
            res.setTotal(page.getTotal());
            res.setMrReservedList(list);
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
            LOGGER.info("分页获取已预约会议室信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("分页获取已预约会议室信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    @Transactional
    public void scheduleChangeStatus()
    {
        if (!scheduledFlag)
        {
            return;
        }
        try
        {
            Date now = new Date();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("now", now);
            mrReservedMapper.updateStatusSchedule(map);
            LOGGER.debug("刷新会议预定状态定时任务执行完成,执行时间:{}", now);
            // 待审批状态 当前时间超过会议开始时间则删除此预约
            List<MrReserved> mrList = mrReservedMapper.selectApproveStatusSchedule(map);
            mrReservedMapper.updateApproveStatusSchedule(map);
            // bjyh 向mt_meeting表同步状态位
            mrReservedMapper.syncStatusToMeeting(map);

            for (MrReserved mr : mrList)
            {
                MtMeeting mm = new MtMeeting();
                mm.setReservedId(mr.getId());
                mm = mtMeetingMapper.selectOne(mm);
                if (mm != null && StringUtil.isNotEmpty(mm.getId()))
                    pushEvent(mr, PUSH_TYPE_OVER, mr.getEcid(), mm.getId());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
