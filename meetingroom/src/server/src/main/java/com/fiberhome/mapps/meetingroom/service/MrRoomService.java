package com.fiberhome.mapps.meetingroom.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.meetingroom.dao.MrApproveInfoMapper;
import com.fiberhome.mapps.meetingroom.dao.MrFavoriteMapper;
import com.fiberhome.mapps.meetingroom.dao.MrPrivilegeMapper;
import com.fiberhome.mapps.meetingroom.dao.MrReservedMapper;
import com.fiberhome.mapps.meetingroom.dao.MrRoomMapper;
import com.fiberhome.mapps.meetingroom.entity.GetMrRoom;
import com.fiberhome.mapps.meetingroom.entity.MrFavorite;
import com.fiberhome.mapps.meetingroom.entity.MrPrivilege;
import com.fiberhome.mapps.meetingroom.entity.MrReserved;
import com.fiberhome.mapps.meetingroom.entity.MrRoom;
import com.fiberhome.mapps.meetingroom.entity.ReservedDate;
import com.fiberhome.mapps.meetingroom.entity.ReservedTime;
import com.fiberhome.mapps.meetingroom.entity.RoomLogContent;
import com.fiberhome.mapps.meetingroom.request.AddRoomRequest;
import com.fiberhome.mapps.meetingroom.request.DeleteRoomRequest;
import com.fiberhome.mapps.meetingroom.request.EditRoomRequest;
import com.fiberhome.mapps.meetingroom.request.FavoriteRequest;
import com.fiberhome.mapps.meetingroom.request.QueryRoomDetailRequest;
import com.fiberhome.mapps.meetingroom.request.QueryRoomRequest;
import com.fiberhome.mapps.meetingroom.response.AddRoomResponse;
import com.fiberhome.mapps.meetingroom.response.DeleteRoomResponse;
import com.fiberhome.mapps.meetingroom.response.EditRoomResponse;
import com.fiberhome.mapps.meetingroom.response.FavoriteResponse;
import com.fiberhome.mapps.meetingroom.response.QueryRoomDetailResponse;
import com.fiberhome.mapps.meetingroom.response.QueryRoomResponse;
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
public class MrRoomService
{
    @Autowired
    MrRoomMapper                  mrRoomMapper;
    @Autowired
    MrReservedService             mrReservedService;
    @Autowired
    MrReservedMapper              mrReservedMapper;
    @Autowired
    MrFavoriteMapper              mrFavoriteMapper;
    
    @Autowired
    FileService            fileService;
    
    @Value("${flywaydb.locations}")
	String databaseType;

    @Autowired
    protected MrPrivilegeMapper   mrPrivilegeMapper;
    @Autowired
    protected MrApproveInfoMapper mrApproveInfoMapper;
    @Autowired
    OpLogService                  opLogService;
    protected final Logger        LOGGER                        = LoggerFactory.getLogger(getClass());
    /**
     * 会议室收藏操作
     */
    public static final String    OPERATIONTYPE_FAVORITE        = "1";
    /**
     * 会议室取消收藏操作
     */
    public static final String    OPERATIONTYPE_CANCEL_FAVORITE = "0";
    @Value("${meetingroom.privilege.whiteList}")
    boolean                       whiteList;

    /**
     * 会议室名称重复校验
     * 
     * @param id
     * @param name
     * @param res
     * @return
     */
    public boolean checkRoomName(String id, String name, BaseResponse res)
    {
        MrRoom mrRoom = new MrRoom();
        mrRoom.setId(id);
        mrRoom.setEcid(SessionContext.getEcId());
        mrRoom.setName(name);
        int i = mrRoomMapper.checkName(mrRoom);
        if (i > 0)
        {
            ErrorCode.fail(res, ErrorCode.CODE_300004);
            LOGGER.error("错误码:{}", ErrorCode.CODE_300004);
            return true;
        }
        return false;
    }

    /**
     * 新增会议室信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.room.add", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public AddRoomResponse addMrRoom(AddRoomRequest req)
    {
        AddRoomResponse res = new AddRoomResponse();
        LOGGER.info("新增会议室接口(mapps.meetingroom.room.add)入口,请求参数==" + LogUtil.getObjectInfo(req));
        String id = IDGen.uuid();
        RoomLogContent olc = new RoomLogContent(id, req.getRoomName(), "新增会议室成功");
        String name = req.getRoomName();
        if (checkRoomName(id, name, res))
        {
            return res;
        }
        MrRoom mrRoom = new MrRoom();
        mrRoom.setId(id);
        mrRoom.setName(name);
        mrRoom.setAddress(req.getAddress());
        mrRoom.setArea(req.getArea());
        mrRoom.setCapacity(req.getCapacity());
        mrRoom.setProjector(StringUtil.isEmpty(req.getProjector()) ? "0" : req.getProjector());
        mrRoom.setDisplay(StringUtil.isEmpty(req.getDisplay()) ? "0" : req.getDisplay());
        mrRoom.setMicrophone(StringUtil.isEmpty(req.getMicrophone()) ? "0" : req.getMicrophone());
        mrRoom.setStereo(StringUtil.isEmpty(req.getStereo()) ? "0" : req.getStereo());
        mrRoom.setWifi(StringUtil.isEmpty(req.getWifi()) ? "0" : req.getWifi());
        mrRoom.setLayout(req.getImagePath());
        mrRoom.setRemarks(req.getRemarks());
        mrRoom.setCreateTime(new Date());
        try
        {
            mrRoomMapper.insertSelective(mrRoom);
            res.setRoomId(id);
            opLogService.addModuleOpLog(OpLogService.MODULE_ROOM, id, OpLogService.OP_ADD, olc, OpLogService.RESULT_YES,
                    SessionContext.getEcId(), SessionContext.getUserId(), SessionContext.getUserName());
            res.success();
            LOGGER.info("新增会议室成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("新增会议室信息异常：{}", e);
            olc.setMessage("新增会议室失败," + e.getMessage());
            opLogService.addModuleOpLog(OpLogService.MODULE_ROOM, id, OpLogService.OP_ADD, olc, OpLogService.RESULT_NO,
                    SessionContext.getEcId(), SessionContext.getUserId(), SessionContext.getUserName());
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 修改会议室信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.room.edit", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public EditRoomResponse editMrRoom(EditRoomRequest req)
    {
        EditRoomResponse res = new EditRoomResponse();
        RoomLogContent oLogContent = new RoomLogContent(req.getRoomId(), req.getRoomName(), "修改会议室成功");
        try
        {
            LOGGER.info("修改会议室接口(mapps.meetingroom.room.edit)入口,请求参数==" + LogUtil.getObjectInfo(req));
            String id = req.getRoomId();
            String name = req.getRoomName();
            if (mrReservedService.checkRoomReserved(id, res))
            {
                return res;
            }
            if (checkRoomName(id, name, res))
            {
                return res;
            }
            MrRoom mrRoom = new MrRoom();
            mrRoom.setId(id);
            mrRoom.setName(name);
            mrRoom.setAddress(req.getAddress());
            mrRoom.setArea(req.getArea());
            mrRoom.setCapacity(req.getCapacity());
            mrRoom.setProjector(StringUtil.isEmpty(req.getProjector()) ? "0" : req.getProjector());
            mrRoom.setDisplay(StringUtil.isEmpty(req.getDisplay()) ? "0" : req.getDisplay());
            mrRoom.setMicrophone(StringUtil.isEmpty(req.getMicrophone()) ? "0" : req.getMicrophone());
            mrRoom.setStereo(StringUtil.isEmpty(req.getStereo()) ? "0" : req.getStereo());
            mrRoom.setWifi(StringUtil.isEmpty(req.getWifi()) ? "0" : req.getWifi());
            String imagePath = req.getImagePath();

            mrRoom.setLayout(imagePath == null ? "" : imagePath);
            mrRoom.setRemarks(req.getRemarks());
            mrRoom.setModifiedTime(new Date());
            mrRoomMapper.updateByPrimaryKeySelective(mrRoom);
            opLogService.addModuleOpLog(OpLogService.MODULE_ROOM, id, OpLogService.OP_EDIT, oLogContent,
                    OpLogService.RESULT_YES, SessionContext.getEcId(), SessionContext.getUserId(),
                    SessionContext.getUserName());
            res.success();
            LOGGER.info("修改会议室成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            oLogContent.setMessage("修改会议室失败," + e.getMessage());
            opLogService.addModuleOpLog(OpLogService.MODULE_ROOM, req.getRoomId(), OpLogService.OP_EDIT, oLogContent,
                    OpLogService.RESULT_NO, SessionContext.getEcId(), SessionContext.getUserId(),
                    SessionContext.getUserName());
            LOGGER.error("修改会议室信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 删除会议室信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.room.delete", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public DeleteRoomResponse deleteMrRoom(DeleteRoomRequest req)
    {
        DeleteRoomResponse res = new DeleteRoomResponse();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roomId", req.getRoomId());
        map.put("userId", SessionContext.getUserId());
        MrRoom room = mrRoomMapper.selectByPrimaryKey(req.getRoomId());
        if (room == null)
        {
            ErrorCode.fail(res, ErrorCode.CODE_300011);
            LOGGER.info("错误码:{}={}", ErrorCode.CODE_300011, ErrorCode.errorMap.get(ErrorCode.CODE_300011));
            return res;
        }
        RoomLogContent oLogContent = new RoomLogContent(req.getRoomId(), room.getName(), "删除会议室成功");
        try
        {
            LOGGER.info("删除会议室接口(mapps.meetingroom.room.delete)入口,请求参数==" + LogUtil.getObjectInfo(req));
            String id = req.getRoomId();
            if (mrReservedService.checkRoomReserved(id, res))
            {
                return res;
            }
            mrRoomMapper.deleteByPrimaryKey(id);
            res.success();
            opLogService.addModuleOpLog(OpLogService.MODULE_ROOM, req.getRoomId(), OpLogService.OP_DELETE, oLogContent,
                    OpLogService.RESULT_YES, SessionContext.getEcId(), SessionContext.getUserId(),
                    SessionContext.getUserName());
            LOGGER.info("删除会议室成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            oLogContent.setMessage("删除会议室失败," + e.getMessage());
            opLogService.addModuleOpLog(OpLogService.MODULE_ROOM, req.getRoomId(), OpLogService.OP_DELETE, oLogContent,
                    OpLogService.RESULT_NO, SessionContext.getEcId(), SessionContext.getUserId(),
                    SessionContext.getUserName());
            LOGGER.error("删除会议室信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 分页获取会议室信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.room.query", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryRoomResponse getMrRoomForPage(QueryRoomRequest req)
    {
        QueryRoomResponse res = new QueryRoomResponse();
        try
        {
            LOGGER.info("分页查询会议室接口(mapps.meetingroom.room.query)入口,请求参数==" + LogUtil.getObjectInfo(req));
            if (req.getTimestamp() == 0l)
            {
                req.setTimestamp(new Date().getTime());
            }
            String sTime = req.getReservedStartTime();
            String eTime = req.getReservedEndTime();
            if (StringUtil.isNotEmpty(sTime))
            {
                if (!DateUtil.convertToTime(sTime).after(new Date()))
                {
                    ErrorCode.fail(res, ErrorCode.CODE_300017);
                    return res;
                }
            }
            if (StringUtil.isNotEmpty(eTime))
            {
                if (!DateUtil.convertToTime(eTime).after(new Date()))
                {
                    ErrorCode.fail(res, ErrorCode.CODE_300017);
                    return res;
                }
            }
            Map<String, Object> map = initQuery(req);
            PageHelper.startPage(req.getOffset(), req.getLimit());
            List<GetMrRoom> list = mrRoomMapper.getMrRoom(map);
            PageInfo<GetMrRoom> page = new PageInfo<GetMrRoom>(list);
            res.setTotal(page.getTotal());
            if (list == null)
            {
                list = new ArrayList<GetMrRoom>();
            }
            else
            {
                for (GetMrRoom info : list)
                {
                    if (StringUtil.isNotEmpty(info.getLayout()))
                        info.setLayout(fileService.getWebRoot() + info.getLayout());
                }
            }
            res.setGetMrRoomList(list);
            res.setTimestamp(req.getTimestamp());
            if (page.isIsLastPage())
            {
                res.setEndflag(1);
            }
            else
            {
                res.setEndflag(0);
            }
            MrReserved rEntity = new MrReserved();
            rEntity.setEcid(SessionContext.getEcId());
            rEntity.setReservedUserId(SessionContext.getUserId());
            rEntity.setOrderTimeBegin(new Date());
            rEntity.setStatus(MrReservedService.RESERVED_STATUS_DO);
            int rCount = mrReservedMapper.getReservedCountByUser(rEntity);
            if (rCount > 0)
            {
                res.setReservedFlag(1);
            }
            else
            {
                // 判断是否有待审批的记录
                Map<String, Object> aiMap = new HashMap<String, Object>();
                aiMap.put("ecid", SessionContext.getEcId());
                aiMap.put("userId", SessionContext.getUserId());
                aiMap.put("approved", "0");
                aiMap.put("status", "a");
                if (mrApproveInfoMapper.getCountByUser(aiMap) > 0)
                    res.setReservedFlag(1);
                else
                    res.setReservedFlag(0);
            }
            // 是否被设置为会议室管理员
            res.setAdminFlag(0);
            MrPrivilege mp = new MrPrivilege();
            mp.setEcid(SessionContext.getEcId());
            mp.setEntityId(SessionContext.getUserId());
            mp.setPriv(MrPrivilegeService.PRIV_ADMIN);
            if (mrPrivilegeMapper.selectCount(mp) > 0)
            {
                res.setAdminFlag(1);
            }
            res.success();
            LOGGER.info("分页查询会议室成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("分页查询会议室信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public String resetTime(String stime, String etime, QueryRoomRequest res)
    {
        String sStr1 = " 07:00:00";
        String eStr2 = " 22:00:00";
        int sHourInt = 700;
        int eHourInt = 2200;
        String[] sStr = stime.split(" ");
        String[] eStr = etime.split(" ");
        int sHourMin = Integer.parseInt(stime.substring(11, 13) + stime.substring(14, 16));
        int eHourMin = Integer.parseInt(etime.substring(11, 13) + etime.substring(14, 16));
        if ((sHourMin < sHourInt || sHourMin >= eHourInt) && (eHourMin <= sHourInt || eHourMin > eHourInt))
        {
            return "noData";
        }
        if (sHourMin < sHourInt)
        {
            res.setReservedStartTime(sStr[0] + sStr1);
        }
        else if (sHourMin >= eHourInt)
        {
            return "noData";
        }
        if (eHourMin < sHourInt)
        {
            return "noData";
        }
        else if (eHourMin > eHourInt)
        {
            res.setReservedEndTime(eStr[0] + eStr2);
        }
        return "";
    }

    public String getInSql(List<String> roomIds)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        for (String str : roomIds)
        {
            sb.append("'").append(str).append("'").append(",");
        }
        sb.deleteCharAt(sb.toString().length() - 1);
        sb.append(")");
        return sb.toString();
    }

    public Map<String, Object> initQuery(QueryRoomRequest req) throws Exception
    {
        String roomName = req.getRoomName();
        String address = req.getAddress();
        String sTime = req.getReservedStartTime();
        String eTime = req.getReservedEndTime();
        Long capacity = req.getCapacity();
        String projector = req.getProjector();
        String display = req.getDisplay();
        String microphone = req.getMicrophone();
        String stereo = req.getStereo();
        String wifi = req.getWifi();
        String order = req.getOrder();
        String sort = req.getSort();
        long time = req.getTimestamp();
        String reservedDate = req.getReservedDate();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("databaseType", databaseType);
        if (StringUtil.isNotEmpty(sTime) && StringUtil.isNotEmpty(eTime))
        {
            if ("noData".equals(resetTime(sTime, eTime, req)))
            {
                map.put("noData", "-1");
            }
            sTime = req.getReservedStartTime();
            eTime = req.getReservedEndTime();
        }
        map.put("whiteList", whiteList);
        // 权限判断
        if (!SessionContext.isAdmin())
        {
            LOGGER.debug("普通用户获取会议室列表查询条件,userid={},deptorder={}", SessionContext.getUserId(),
                    SessionContext.getDeptOrder());
            map.put("isUser", "-1");
            map.put("deptOrder", SessionContext.getDeptOrder());
        }
        map.put("userId", SessionContext.getUserId());
        if (StringUtil.isNotEmpty(reservedDate) && StringUtil.isEmpty(sTime))
        {
        	System.err.println("yyyyyyyyyyyyyy");
            List<String> roomIds = mrReservedService.getReservedByDay(reservedDate);
            if (roomIds != null)
            {
                if (roomIds.size() > 0)
                {
                    map.put("roomIds", getInSql(roomIds));
                }
            }
            else
            {
                map.put("noData", "-1");
            }
        }
        if (StringUtil.isNotEmpty(roomName))
        {
            map.put("name", "%" + roomName.trim() + "%");
        }
        if (StringUtil.isNotEmpty(address))
        {
            map.put("address", "%" + address.trim() + "%");
        }
        if (StringUtil.isNotEmpty(sTime))
        {
            map.put("reservedStartTime", DateUtil.convertToTime(sTime));
        }
        if (StringUtil.isNotEmpty(eTime))
        {
            map.put("reservedEndTime", DateUtil.convertToTime(eTime));
        }
        if (capacity != null)
        {
            map.put("capacity", capacity);
        }
        if (StringUtil.isNotEmpty(projector))
        {
            map.put("projector", projector);
        }
        if (StringUtil.isNotEmpty(display))
        {
            map.put("display", display);
        }
        if (StringUtil.isNotEmpty(microphone))
        {
            map.put("microphone", microphone);
        }
        if (StringUtil.isNotEmpty(stereo))
        {
            map.put("stereo", stereo);
        }
        if (StringUtil.isNotEmpty(wifi))
        {
            map.put("wifi", wifi);
        }
        if (StringUtil.isNotEmpty(order))
        {
            // 1.会场容量由高到低 2.会场容量由低到高 3.面积由高到低 4.面积由低到高
            if ("1".equals(order))
                map.put("order", "collection desc,capacity desc");
            else if ("2".equals(order))
                map.put("order", "collection desc,capacity asc");
            else if ("3".equals(order))
                map.put("order", "collection desc,area desc");
            else if ("4".equals(order))
                map.put("order", "collection desc,area asc");
            else if ("5".equals(order))
            {
                if (StringUtil.isNotEmpty(sort))
                    map.put("order", sort);
                else
                    map.put("order", "create_time desc");
            }
        }
        else
        {
            map.put("order", "collection desc,create_time desc");
        }
        map.put("time", time);
        map.put("ecid", SessionContext.getEcId());
        return map;
    }

    /**
     * 获取会议室详细信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.room.detail", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryRoomDetailResponse getMrRoomDetail(QueryRoomDetailRequest req)
    {
        QueryRoomDetailResponse res = new QueryRoomDetailResponse();
        try
        {
            LOGGER.info("查询会议室详细信息接口(mapps.meetingroom.room.detail)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("roomId", req.getRoomId());
            map.put("ecid", SessionContext.getEcId());
            map.put("userId", SessionContext.getUserId());
            map.put("databaseType", databaseType);
            List<GetMrRoom> list = mrRoomMapper.getMrRoom(map);
            List<ReservedDate> dList = mrReservedMapper.getReservedDates(map);
            for (ReservedDate date : dList)
            {
                map.put("reservedDate", date.getReservedDate());
                List<ReservedTime> tList = mrReservedMapper.getReservedTimes(map);
                date.setReservedTimeList(tList);
            }
            setDetailResponse(res, list, dList);
            LOGGER.info("查询会议室详细信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询会议室详细信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public void setDetailResponse(QueryRoomDetailResponse res, List<GetMrRoom> list, List<ReservedDate> dList)
    {
        GetMrRoom info = null;
        if (list != null && !list.isEmpty())
        {
            info = list.get(0);
        }
        else
        {
            ErrorCode.fail(res, ErrorCode.CODE_300011);
            return;
        }
        res.setRoomId(info.getRoomId());
        res.setRoomName(info.getRoomName());
        res.setAddress(info.getAddress());
        res.setCapacity(info.getCapacity());
        res.setArea(info.getArea());
        if (!StringUtils.isEmpty(info.getLayout()))
        {
            res.setImagePath(fileService.getWebRoot() + info.getLayout());
        }
        res.setLayout(StringUtils.isEmpty(info.getLayout()) ? "" : info.getLayout());
        res.setRemarks(info.getRemarks());
        res.setProjector(info.getProjector());
        res.setDisplay(info.getDisplay());
        res.setMicrophone(info.getMicrophone());
        res.setStereo(info.getStereo());
        res.setWifi(info.getWifi());
        res.setCollection(info.getCollection());
        res.setReservedDateList(dList);
        res.success();
    }

    /**
     * 会议室收藏与取消收藏
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.room.favorite", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public FavoriteResponse addFavorite(FavoriteRequest req)
    {
        FavoriteResponse res = new FavoriteResponse();
        try
        {
            LOGGER.info("会议室收藏与取消收藏接口(mapps.meetingroom.room.favorite)入口,请求参数==" + LogUtil.getObjectInfo(req));
            String id = IDGen.uuid();
            String ecid = SessionContext.getEcId();
            String userId = SessionContext.getUserId();
            String roomId = req.getRoomId();
            String operationType = req.getOperationType();
            MrFavorite info = new MrFavorite();
            info.setEcid(ecid);
            info.setUserId(userId);
            info.setRoomId(roomId);
            if (OPERATIONTYPE_FAVORITE.equals(operationType))
            {
                if (checkFavorite(ecid, userId, roomId))
                {
                    ErrorCode.fail(res, ErrorCode.CODE_300008);
                    return res;
                }
                info.setId(id);
                info.setFavTime(new Date());
                mrFavoriteMapper.insertSelective(info);
            }
            else
            {
                if (!checkFavorite(ecid, userId, roomId))
                {
                    ErrorCode.fail(res, ErrorCode.CODE_300009);
                    return res;
                }
                mrFavoriteMapper.delete(info);
            }
            res.success();
            LOGGER.info("会议室收藏与取消收藏成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("会议室收藏与取消收藏异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 校验是否已收藏
     * 
     * @param ecid
     * @param userId
     * @param roomId
     * @return true 已收藏 false 未收藏
     */
    public boolean checkFavorite(String ecid, String userId, String roomId)
    {
        MrFavorite info = new MrFavorite();
        info.setEcid(ecid);
        info.setUserId(userId);
        info.setRoomId(roomId);
        int j = mrFavoriteMapper.selectCount(info);
        if (j > 0)
        {
            return true;
        }
        return false;
    }
}
