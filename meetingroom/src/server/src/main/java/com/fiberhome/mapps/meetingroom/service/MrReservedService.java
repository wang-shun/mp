package com.fiberhome.mapps.meetingroom.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Component;

import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.meetingroom.dao.MrReservedMapper;
import com.fiberhome.mapps.meetingroom.dao.MrRoomMapper;
import com.fiberhome.mapps.meetingroom.entity.GetMrReserved;
import com.fiberhome.mapps.meetingroom.entity.MrReserved;
import com.fiberhome.mapps.meetingroom.entity.MrRoom;
import com.fiberhome.mapps.meetingroom.entity.ReservedDetail;
import com.fiberhome.mapps.meetingroom.entity.ReservedLogContent;
import com.fiberhome.mapps.meetingroom.request.AddReservedRequest;
import com.fiberhome.mapps.meetingroom.request.DeleteReservedRequest;
import com.fiberhome.mapps.meetingroom.request.QueryRequest;
import com.fiberhome.mapps.meetingroom.request.QueryReservedRequest;
import com.fiberhome.mapps.meetingroom.response.AddReservedResponse;
import com.fiberhome.mapps.meetingroom.response.DeleteReservedResponse;
import com.fiberhome.mapps.meetingroom.response.QueryReservedResponse;
import com.fiberhome.mapps.meetingroom.response.QueryResponse;
import com.fiberhome.mapps.meetingroom.utils.DateUtil;
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
public class MrReservedService
{
    @Autowired
    protected MrRoomMapper           mrRoomMapper;
    @Autowired
    protected MrReservedMapper       mrReservedMapper;
    @Autowired
    protected MrRoomService          mrRoomService;
    @Autowired
    protected OpLogService           opLogService;
    @Autowired
    protected ThirdPartAccessService thirdPartAccessService;
    @Value("${flywaydb.locations}")
	String databaseType;
    protected final Logger           LOGGER                 = LoggerFactory.getLogger(getClass());
    /** 准备中 */
    public static final String       RESERVED_STATUS_DO     = "1";
    /** 使用中 */
    public static final String       RESERVED_STATUS_DOING  = "2";
    /** 已结束 */
    public static final String       RESERVED_STATUS_DONE   = "3";
    /** 已取消 */
    public static final String       RESERVED_STATUS_CANCEL = "4";
    /** 已删除 */
    public static final String       RESERVED_STATUS_DELETE = "0";
    public static final String       FLAG_DELETE            = "1";
    /** 1：准备中取消会议 */
    public static final String       OPERATION_TYPE1        = "1";
    /** 2：使用中结束会议 */
    public static final String       OPERATION_TYPE2        = "2";
    /** 3：已结束/已取消删除会议 */
    public static final String       OPERATION_TYPE3        = "3";
    /** 时间段开始标示 */
    public static final String       STARTSTR               = "07:00";
    /** 时间段结束标示 */
    public static final String       ENDSTR                 = "22:00";

    /**
     * 2.0新增 预约详情接口
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.reserved.detail", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryResponse getMrRoomDetail(QueryRequest req)
    {
        QueryResponse res = new QueryResponse();
        try
        {
            LOGGER.info("查询会议室预约详细信息接口(mapps.meetingroom.reserved.detail)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("roomId", req.getRoomId());
            map.put("reservedDate", req.getReservedDate());
            map.put("status", "'1','2','a'");
            map.put("databaseType", databaseType);
            List<ReservedDetail> rdList = mrReservedMapper.getReservedDetail(map);
            if (rdList != null && rdList.size() > 0)
            {
                // 电话号码处理
                List<String> userIdList = new ArrayList<String>();
                for (ReservedDetail info : rdList)
                {
                    info.setPhoneNum("");
                    info.setReservedRemark("");
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
     * 会议室已预约校验
     * 
     * @param roomId
     * @param res
     * @return
     */
    public boolean checkRoomReserved(String roomId, BaseResponse res)
    {
        MrReserved entity = new MrReserved();
        entity.setRoomId(roomId);
        int i = mrReservedMapper.checkRoomReserved(entity);
        if (i > 0)
        {
            ErrorCode.fail(res, ErrorCode.CODE_300005);
            LOGGER.info("错误码:{}={}", ErrorCode.CODE_300005, ErrorCode.errorMap.get(ErrorCode.CODE_300005));
            return true;
        }
        return false;
    }

    /**
     * 会议室已预约时间段校验
     * 
     * @param roomId
     * @param res
     * @return
     */
    public boolean checkRoomReserved(String roomId, Date orderTimeBegin, Date orderTimeEnd, BaseResponse res)
    {
        MrReserved entity = new MrReserved();
        entity.setRoomId(roomId);
        entity.setOrderTimeBegin(orderTimeBegin);
        entity.setOrderTimeEnd(orderTimeEnd);
        int i = mrReservedMapper.checkRoomReserved(entity);
        if (i > 0)
        {
            ErrorCode.fail(res, ErrorCode.CODE_300007);
            LOGGER.info("错误码:{}={}", ErrorCode.CODE_300007, ErrorCode.errorMap.get(ErrorCode.CODE_300007));
            return true;
        }
        return false;
    }

    /**
     * 预定会议室
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.reserved.add", group = "reserved", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public AddReservedResponse addMrReserved(AddReservedRequest req)
    {
        LOGGER.debug("预定会议室接口(mapps.meetingroom.reserved.add)入口,请求参数==" + LogUtil.getObjectInfo(req));
        AddReservedResponse res = new AddReservedResponse();
        String roomId = req.getRoomId();
        MrRoom roomEntity = mrRoomMapper.selectByPrimaryKey(roomId);
        String id = IDGen.uuid();
        ReservedLogContent olc = new ReservedLogContent(roomEntity.getName(),
                req.getReservedDate() + " " + req.getReservedStartTime() + "-" + req.getReservedEndTime(),
                req.getMeetingName(), "预定会议室成功");
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
        info.setId(id);
        try
        {
            mrReservedMapper.insertSelective(info);
            res.setReservedId(id);
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

    /**
     * 初始化预约时间
     * 
     * @param info
     * @param req
     * @param res
     * @return true 成功 false 失败
     */
    public boolean initReservedDate(MrReserved info, AddReservedRequest req, AddReservedResponse res)
    {
        String date = req.getReservedDate();
        String sTime = req.getReservedStartTime();
        String eTime = req.getReservedEndTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try
        {
            Date sDate = df.parse(date + " " + sTime);
            Date eDate = df.parse(date + " " + eTime);
            info.setOrderTimeBegin(sDate);
            info.setOrderTimeEnd(eDate);
            long diff = eDate.getTime() - sDate.getTime();
            long min = diff / (1000 * 60);
            info.setOrderDuration(min);
            long nowTime = new Date().getTime();
            if (nowTime >= sDate.getTime())
            {
                ErrorCode.fail(res, ErrorCode.CODE_300006);
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("预定会议室异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_300012);
            return false;
        }
        return true;
    }

    /**
     * 已预约会议室操作(取消预约，结束预约，删除预约)
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.reserved.delete", group = "reserved", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
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
        if (!SessionContext.isAdmin())
        {
            depId = SessionContext.getDeptId();
            depName = SessionContext.getDeptName();
        }
        String op = "";
        String content = "";
        String srcStatus = entity.getStatus();
        String operationType = req.getOperationType();
        String targetStatus = null;
        if (RESERVED_STATUS_DO.equals(srcStatus) && OPERATION_TYPE1.equals(operationType))
        {
            targetStatus = RESERVED_STATUS_CANCEL;
            op = OpLogService.OP_CANCEL;
            content = "取消预约";
            if (SessionContext.isAdmin())
            {
                thirdPartAccessService.pushSysMsg(SessionContext.getEcId(), entity.getReservedUserId(),
                        roomEntity.getName() + "的预定已被取消",
                        "您预定的" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(entity.getOrderTimeBegin()) + "-"
                                + new SimpleDateFormat("HH:mm").format(entity.getOrderTimeEnd()) + " "
                                + roomEntity.getName() + "已被管理员取消");
            }
        }
        else if (RESERVED_STATUS_DOING.equals(srcStatus) && OPERATION_TYPE2.equals(operationType))
        {
            targetStatus = RESERVED_STATUS_DONE;
            op = OpLogService.OP_OVER;
            content = "结束预约";
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

    /**
     * 分页获取已预约会议室信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.reserved.query", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
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
            map.put("databaseType", databaseType);
            List<GetMrReserved> list = mrReservedMapper.getMrReserved(map);
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

    public Map<String, Object> initQuery(QueryReservedRequest req) throws Exception
    {
        String status = req.getStatus();
        String order = req.getOrder();
        String queryTerm = req.getQueryTerm();
        String roomId = req.getRoomId();
        String displayName = req.getDisplayName();
        String depName = req.getDepName();
        String startDate = req.getReservedStartDate();
        String endDate = req.getReservedEndDate();
        long time = req.getTimestamp();
        String sort = req.getSort();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loginUserId", SessionContext.getUserId());
        if (StringUtil.isNotEmpty(displayName))
        {
            map.put("displayName", "%" + displayName.trim() + "%");
        }
        if (StringUtil.isNotEmpty(depName))
        {
            map.put("depName", "%" + depName.trim() + "%");
        }
        if (StringUtil.isNotEmpty(startDate))
        {
            map.put("startDate", DateUtil.convertToDate(startDate));
        }
        if (StringUtil.isNotEmpty(endDate))
        {
            map.put("endDate", DateUtil.getLastTime(DateUtil.convertToDate(endDate)));
        }
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
            map.put("status", param);
        }
        else
        {
            map.put("status", "'-1'");
        }
        if (StringUtil.isNotEmpty(queryTerm))
        {
            map.put("queryTerm", "%" + queryTerm.trim() + "%");
        }
        if (StringUtil.isNotEmpty(order))
        {
            // 排序 预定时间 1.进行降序 2.升序排序
            if ("1".equals(order))
                map.put("order", "order_time_begin desc");
            else if ("2".equals(order))
                map.put("order", "order_time_begin asc");
            else if ("3".equals(order))
            {
                if (StringUtil.isEmpty(sort))
                    map.put("order", "order_time_begin desc");
                else if ("reservedDate asc".equals(sort))
                    map.put("order", "order_time_begin asc");
                else if ("reservedDate desc".equals(sort))
                    map.put("order", "order_time_begin desc");
                else
                    map.put("order", sort);
            }
        }
        if (StringUtil.isNotEmpty(roomId))
        {
            map.put("roomId", roomId);
        }
        else
        {
            map.put("userId", SessionContext.getUserId());
            map.put("time", time);
        }
        map.put("ecid", SessionContext.getEcId());
        return map;
    }

    /**
     * 查询无可预约时段的roomid集合
     * 
     * @param reservedDate
     * @return null 全部不可用 list.size>=0 不可用的集合
     */
    public List<String> getReservedByDay(String reservedDate)
    {
        List<String> roomIds = new ArrayList<String>();
        Date now = new Date();
        String nowDay = new SimpleDateFormat("yyyy-MM-dd").format(now);
        String startStr = STARTSTR;
        if (reservedDate.equals(nowDay))
        {
            startStr = getStartStr(reservedDate, now);
        }
        // 预约开始时间设置
        // Date reservedTime = null;
        // try
        // {
        // LOGGER.debug("格式化的时间参数=" + nowDay + " " + startStr + ":00");
        // reservedTime = DateUtil.sdfHMS.parse(nowDay + " " + startStr + ":00");
        // }
        // catch (Exception e)
        // {
        // LOGGER.error("格式化错误,{}", e.getMessage());
        // }
        // 今天当前时间超过20:00 无可用时间段
        if (StringUtil.isEmpty(startStr))
        {
            // 所有会议室都不可用
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("reservedDate", reservedDate);
        map.put("reservedTime", nowDay + " " + startStr);
        System.err.println(reservedDate);
        System.err.println(nowDay + " " + startStr);
        map.put("databaseType", databaseType);
        // 获取某天的预约数据
        List<MrReserved> reservedAllList = mrReservedMapper.getReservedByDay(map);
        map.put("roomId", "-1");
        // 获取某天的预约的会议室id
        List<MrReserved> roomList = mrReservedMapper.getReservedByDay(map);
        // 筛选会议室在一天中已使用的时间段
        for (MrReserved rInfo : roomList)
        {
            String roomId = rInfo.getRoomId();
            List<MrReserved> reservedList = new ArrayList<MrReserved>();
            for (MrReserved reInfo : reservedAllList)
            {
                if (reInfo.getRoomId().equals(roomId))
                {
                    reservedList.add(reInfo);
                }
            }
            String endStr = disposalData(startStr, reservedList);
            if (ENDSTR.equals(endStr))
            {
                // 无可用时段 此会议室不可用
                roomIds.add(roomId);
            }
        }
        return roomIds;
    }

    /**
     * @param Day yyyy-mm-dd 格式的日期字符串
     * @return "" 为无可用
     */
    public String getStartStr(String srcDay, Date now)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (7 >= hour)
        {
            return STARTSTR;
        }
        else if (hour >= 20)
        {
            return null;
        }
        else
        {
            if (0 <= minute && minute < 30)
            {
                return hour + ":30";
            }
            else
            {
                hour += 1;
                return hour + ":00";
            }
        }

    }

    public String disposalData(String startStr, List<MrReserved> reservedList)
    {
        for (MrReserved info : reservedList)
        {
            String beginStr = new SimpleDateFormat("HH:mm").format(info.getOrderTimeBegin());
            String endStr = new SimpleDateFormat("HH:mm").format(info.getOrderTimeEnd());
            if (startStr.equals(beginStr))
            {
                if (ENDSTR.equals(endStr))
                {
                    return ENDSTR;
                }
                return disposalData(endStr, reservedList);
            }
        }
        return "";
    }

}
