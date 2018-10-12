package com.fiberhome.mapps.meeting.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.meeting.dao.MdParticipantsMapper;
import com.fiberhome.mapps.meeting.dao.MrRoomMapper;
import com.fiberhome.mapps.meeting.dao.MtAgendaMapper;
import com.fiberhome.mapps.meeting.dao.MtAttachmentMapper;
import com.fiberhome.mapps.meeting.dao.MtMeetingMapper;
import com.fiberhome.mapps.meeting.dao.MtParticipantsMapper;
import com.fiberhome.mapps.meeting.dao.MtRemarksMapper;
import com.fiberhome.mapps.meeting.dao.MtSigninRecordMapper;
import com.fiberhome.mapps.meeting.dao.MtSigninSequMapper;
import com.fiberhome.mapps.meeting.dao.MtSigninServMapper;
import com.fiberhome.mapps.meeting.dao.TmpReservedMapper;
import com.fiberhome.mapps.meeting.entity.ClientAgendaInfo;
import com.fiberhome.mapps.meeting.entity.ClientMeetingInfo;
import com.fiberhome.mapps.meeting.entity.MdParticipants;
import com.fiberhome.mapps.meeting.entity.MrRoom;
import com.fiberhome.mapps.meeting.entity.MtAgenda;
import com.fiberhome.mapps.meeting.entity.MtAttachment;
import com.fiberhome.mapps.meeting.entity.MtMeeting;
import com.fiberhome.mapps.meeting.entity.MtParticipants;
import com.fiberhome.mapps.meeting.entity.MtRemarks;
import com.fiberhome.mapps.meeting.entity.MtSignStatus;
import com.fiberhome.mapps.meeting.entity.MtSigninRecord;
import com.fiberhome.mapps.meeting.entity.MtSigninSequ;
import com.fiberhome.mapps.meeting.entity.MtSigninServ;
import com.fiberhome.mapps.meeting.entity.SignDetail;
import com.fiberhome.mapps.meeting.request.AddMeetingRequest;
import com.fiberhome.mapps.meeting.request.CancelMeetRequset;
import com.fiberhome.mapps.meeting.request.CreateImGroupRequest;
import com.fiberhome.mapps.meeting.request.DeleteMeetRequest;
import com.fiberhome.mapps.meeting.request.GetDocUrlRequest;
import com.fiberhome.mapps.meeting.request.GetUsersRequest;
import com.fiberhome.mapps.meeting.request.JoinImGroupRequest;
import com.fiberhome.mapps.meeting.request.QueryMeetingDetailRequest;
import com.fiberhome.mapps.meeting.request.QueryMeetingRequest;
import com.fiberhome.mapps.meeting.request.QueryMeetingServerRequest;
import com.fiberhome.mapps.meeting.request.QueryOuterDetailRequest;
import com.fiberhome.mapps.meeting.request.QuerySignStatusRequest;
import com.fiberhome.mapps.meeting.request.SignInRequest;
import com.fiberhome.mapps.meeting.response.AddMeetingResponse;
import com.fiberhome.mapps.meeting.response.AttachListResponse;
import com.fiberhome.mapps.meeting.response.CancelMeetResponse;
import com.fiberhome.mapps.meeting.response.CreateImGroupResponse;
import com.fiberhome.mapps.meeting.response.DeleteMeetResponse;
import com.fiberhome.mapps.meeting.response.GetDocUrlResponse;
import com.fiberhome.mapps.meeting.response.GetUsersResponse;
import com.fiberhome.mapps.meeting.response.JoinImGroupResponse;
import com.fiberhome.mapps.meeting.response.QueryMeetDetailWebResponse;
import com.fiberhome.mapps.meeting.response.QueryMeetingDetailResponse;
import com.fiberhome.mapps.meeting.response.QueryMeetingDetailResponseV11;
import com.fiberhome.mapps.meeting.response.QueryMeetingResponse;
import com.fiberhome.mapps.meeting.response.QuerySignStatusResponse;
import com.fiberhome.mapps.meeting.response.SignInResponse;
import com.fiberhome.mapps.meeting.response.SignRecordResponse;
import com.fiberhome.mapps.meeting.utils.ConvertUtil;
import com.fiberhome.mapps.meeting.utils.CreateImGroupException;
import com.fiberhome.mapps.meeting.utils.DateUtil;
import com.fiberhome.mapps.meeting.utils.ErrorCode;
import com.fiberhome.mapps.meeting.utils.ExcelUtils;
import com.fiberhome.mapps.meeting.utils.JoinImGroupException;
import com.fiberhome.mapps.meeting.utils.JsonUtil;
import com.fiberhome.mapps.meeting.utils.LogUtil;
import com.fiberhome.mapps.meetingroom.request.AddReservedRequest;
import com.fiberhome.mapps.meetingroom.request.DeleteReservedRequest;
import com.fiberhome.mapps.meetingroom.response.AddReservedResponse;
import com.fiberhome.mapps.meetingroom.service.MrReservedServiceV20;
import com.fiberhome.mapps.utils.IDGen;
import com.fiberhome.mos.core.openapi.rop.client.RopClient;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;
import com.rop.response.FileResponse;

import net.sf.json.JSONArray;

@ServiceMethodBean(version = "1.0")
public class MtMeetingService
{
    protected final Logger     LOGGER                     = LoggerFactory.getLogger(getClass());
    @Autowired
    MtMeetingMapper            mtMeetingMapper;
    @Autowired
    MdParticipantsMapper       mdParticipantsMapper;
    @Autowired
    MtAgendaMapper             mtAgendaMapper;
    @Autowired
    MtAttachmentMapper         mtAttachmentMapper;
    @Autowired
    MtSigninSequMapper         mtSigninSequMapper;
    @Autowired
    MtSigninServMapper         mtSigninServMapper;
    @Autowired
    MtSigninRecordMapper       mtSigninRecordMapper;
    @Autowired
    MtRemarksMapper            mtRemarksMapper;
    @Autowired
    MtParticipantsMapper       mtParticipantsMapper;
    @Autowired
    ThirdPartAccessService     thirdPartAccessService;
    @Autowired
    ThirdPartAccessMosService  thirdPartAccessMosService;
    @Autowired
    MrReservedServiceV20       mrReservedServiceV20;
    @Autowired
    MrRoomMapper               mrRoomMapper;
    @Autowired
    TmpReservedMapper          tmpReservedMapper;
    /** 草稿：10 */
    public static final String STAUTS_10                  = "10";
    /** 未进行：20 */
    public static final String STAUTS_20                  = "1";                                // "20";
    /** 进行中：30 */
    public static final String STAUTS_30                  = "2";                                // "30";
    /** 已取消：40 */
    public static final String STAUTS_40                  = "4";                                // "40";
    /** 已结束：50 */
    public static final String STAUTS_50                  = "3";                                // "50";
    /** 待审批 */
    public static final String RESERVED_STATUS_APPROVEING = "a";
    /** 审批拒绝 */
    public static final String RESERVED_STATUS_REJECT     = "r";
    /** 开启 1 */
    public static final String FLAG_OPEN                  = "1";
    /** 关闭 0 */
    public static final String FLAG_CLOSE                 = "0";
    /** 0：未通知 */
    public static final String NOTICE_STATUS_0            = "0";
    /** 1：已发送 */
    public static final String NOTICE_STATUS_1            = "1";
    /** 2：已送达 */
    public static final String NOTICE_STATUS_2            = "2";

    public static final String PARTICIPANT_TYPE_INNER     = "inner";
    public static final String PARTICIPANT_TYPE_OUTER     = "outer";
    public static final String PARTICIPANT_TYPE_SERVICE   = "service";
    public static final String SEPARATOR                  = ",";
    @Value("${services.fileservice.serviceUrl}")
    String                     serviceUrl;
    @Value("${services.fileservice.appKey}")
    String                     appKey;
    @Value("${services.fileservice.appSecret}")
    String                     appSecret;
    @Value("${services.fileservice.format}")
    String                     format;
    @Value("${services.fileservice.webRoot}")
    String                     webRoot;
    @Value("${web.url}")
    String                     webUrl;
    @Value("${meetingroom.scheduled.on-off}")
    Boolean                    scheduledFlag;
    public static final int    MAX_JOIN_NUMBER            = 40;

    @ServiceMethod(method = "mapps.meeting.meeting.virtual.request", group = "meeting", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
    public BaseResponse getVirtual(AddMeetingRequest req)
    {
        return new BaseResponse();
    }

    public void insertMeetingInfo(String meetingId, AddMeetingRequest addInfo) throws Exception
    {
        MtMeeting mtMeetingInfo = addInfo.getMeeting();
        // 参会人员 内部、外部、服务 MdParticipants
        List<MtParticipants> mtParticipantsList = new ArrayList<MtParticipants>();
        List<MdParticipants> inParticipantsList = addInfo.getInParticipantsList();
        Map<String, MtParticipants> inMap = new HashMap<String, MtParticipants>();
        for (MdParticipants info : inParticipantsList)
        {
            info.setId(IDGen.uuid());
            info.setObjectId(meetingId);
            info.setObjectName(mtMeetingInfo.getName());
            mdParticipantsMapper.insertSelective(info);
            if ("user".equals(info.getEntityType()))
            {
                MtParticipants entity = new MtParticipants();
                entity.setId(IDGen.uuid());
                entity.setMeetingId(meetingId);
                entity.setPersonType(PARTICIPANT_TYPE_INNER);
                entity.setPersonId(info.getEntityId());
                entity.setPersonName(info.getEntityName());
                entity.setNoticeStatus(NOTICE_STATUS_0);
                inMap.put(entity.getPersonId(), entity);
            }
            else if ("dept".equals(info.getEntityType()))
            {
                GetUsersRequest guReq = new GetUsersRequest();
                guReq.setDepUuid(info.getEntityId());
                guReq.setDepScope("1");
                GetUsersResponse guRes = thirdPartAccessService.getUsersByDept(guReq);
                for (MyUser user : guRes.getUserInfos())
                {
                    MtParticipants entity = new MtParticipants();
                    entity.setId(IDGen.uuid());
                    entity.setMeetingId(meetingId);
                    entity.setPersonType(PARTICIPANT_TYPE_INNER);
                    entity.setPersonId(user.getUserUuid());
                    entity.setPersonName(user.getUserName());
                    // 0：未通知 1：已发送 2：已送达
                    entity.setNoticeStatus(NOTICE_STATUS_0);
                    inMap.put(entity.getPersonId(), entity);
                }
            }
        }
        // 添加创建人信息
        MtParticipants mp = new MtParticipants();
        mp.setId(IDGen.uuid());
        mp.setMeetingId(meetingId);
        mp.setPersonType(PARTICIPANT_TYPE_INNER);
        mp.setPersonId(SessionContext.getUserId());
        mp.setPersonName(SessionContext.getUserName());
        mp.setNoticeStatus(NOTICE_STATUS_0);
        inMap.put(SessionContext.getUserId(), mp);
        // 生成MtParticipants数据
        for (MtParticipants entity : inMap.values())
        {
            entity.setId(IDGen.shortId());
            entity.setQrcode("-1");
            mtParticipantsMapper.insertSelective(entity);
            mtParticipantsList.add(entity);
        }

        List<MdParticipants> outParticipantsList = addInfo.getOutParticipantsList();
        for (MdParticipants info : outParticipantsList)
        {
            info.setId(IDGen.uuid());
            info.setObjectId(meetingId);
            info.setObjectName(mtMeetingInfo.getName());
            mdParticipantsMapper.insertSelective(info);
            MtParticipants entity = new MtParticipants();
            entity.setId(IDGen.shortId());
            entity.setMeetingId(meetingId);
            entity.setPersonType(PARTICIPANT_TYPE_OUTER);
            entity.setPersonId(info.getEntityId());
            entity.setPersonName(info.getEntityName());
            entity.setQrcode("-1");
            // 0：未通知 1：已发送 2：已送达
            entity.setNoticeStatus(NOTICE_STATUS_0);
            mtParticipantsMapper.insertSelective(entity);
            mtParticipantsList.add(entity);
        }
        // mtParticipantsMapper.insertList(mtParticipantsList);
        // 会议议程 MtAgenda
        if (FLAG_OPEN.equals(addInfo.getAgendaFlag()))
        {
            List<MtAgenda> agendaList = addInfo.getAgendaList();
            for (MtAgenda info : agendaList)
            {
                info.setId(IDGen.uuid());
                info.setMeetingId(meetingId);
                mtAgendaMapper.insertSelective(info);
            }
        }
        // 附件资料 MtAttachment
        if (FLAG_OPEN.equals(addInfo.getAttachmentFlag()))
        {
            List<MtAttachment> attachmentList = addInfo.getAttachmentList();
            for (MtAttachment info : attachmentList)
            {
                info.setId(IDGen.uuid());
                info.setMeetingId(meetingId);
                mtAttachmentMapper.insertSelective(info);
            }
        }
        // 会议签到 MtSigninSequ
        Map<String, MtSigninServ> serviceMap = new HashMap<String, MtSigninServ>();
        if (FLAG_OPEN.equals(addInfo.getSigninFlag()))
        {
            // 服务人员信息预处理
            List<MdParticipants> serviceParticipantsList = addInfo.getServiceParticipantsList();
            for (MdParticipants info : serviceParticipantsList)
            {
                info.setId(IDGen.uuid());
                info.setObjectId(meetingId);
                info.setObjectName(mtMeetingInfo.getName());
                mdParticipantsMapper.insertSelective(info);
                if ("user".equals(info.getEntityType()))
                {
                    MtSigninServ entity = new MtSigninServ();
                    entity.setId(IDGen.uuid());
                    entity.setMeetingId(meetingId);
                    entity.setUserId(info.getEntityId());
                    entity.setUserName(info.getEntityName());
                    serviceMap.put(info.getEntityId(), entity);
                }
                else if ("dept".equals(info.getEntityType()))
                {
                    GetUsersRequest guReq = new GetUsersRequest();
                    guReq.setDepUuid(info.getEntityId());
                    guReq.setDepScope("1");
                    GetUsersResponse guRes = thirdPartAccessService.getUsersByDept(guReq);
                    for (MyUser user : guRes.getUserInfos())
                    {
                        MtSigninServ entity = new MtSigninServ();
                        entity.setId(IDGen.uuid());
                        entity.setMeetingId(meetingId);
                        entity.setUserId(user.getUserUuid());
                        entity.setUserName(user.getUserName());
                        serviceMap.put(user.getUserUuid(), entity);
                    }
                }
            }
            // 保存服务人员信息
            for (MtSigninServ entity : serviceMap.values())
            {
                mtSigninServMapper.insertSelective(entity);
                // 创建服务人员二维码信息
                MtParticipants mpInfo = new MtParticipants();
                mpInfo.setId(IDGen.uuid());
                mpInfo.setMeetingId(meetingId);
                mpInfo.setPersonType(PARTICIPANT_TYPE_SERVICE);
                mpInfo.setPersonId(entity.getUserId());
                mpInfo.setPersonName(entity.getUserName());
                mpInfo.setQrcode("-1");
                mtParticipantsMapper.insertSelective(mpInfo);
            }
            // 签到信息
            long sequ = 1;
            List<MtSigninSequ> signinSequList = addInfo.getSigninSequList();
            // List<MtSigninRecord> recordList = new ArrayList<MtSigninRecord>();
            for (MtSigninSequ info : signinSequList)
            {
                String sequId = IDGen.uuid();
                info.setId(sequId);
                info.setMeetingId(meetingId);
                info.setSequ(sequ);
                mtSigninSequMapper.insertSelective(info);
                for (MtParticipants entity : mtParticipantsList)
                {
                    MtSigninRecord record = new MtSigninRecord();
                    record.setId(IDGen.uuid());
                    record.setMeetingId(meetingId);
                    record.setSequId(sequId);
                    record.setPersonId(entity.getPersonId());
                    record.setPersonName(entity.getPersonName());
                    record.setSigned("N");
                    record.setPersonType(entity.getPersonType());
                    mtSigninRecordMapper.insertSelective(record);
                    // recordList.add(record);
                }
                sequ++;
            }
            // mtSigninRecordMapper.insertList(recordList);
        }
        // 会议备注 MtRemarks
        if (FLAG_OPEN.equals(addInfo.getRemarksFlag()))
        {
            long sequ = 0;
            List<MtRemarks> remarksList = addInfo.getRemarksList();
            for (MtRemarks info : remarksList)
            {
                info.setId(IDGen.uuid());
                info.setMeetingId(meetingId);
                info.setSequ(sequ);
                mtRemarksMapper.insertSelective(info);
                sequ++;
            }
        }
        // 创建群组 调用im创建群组接口
        if (FLAG_OPEN.equals(addInfo.getCreateGroupFlag()))
        {
            // 创建群组
            CreateImGroupRequest cigReq = new CreateImGroupRequest();
            cigReq.setGroupName(mtMeetingInfo.getName());
            CreateImGroupResponse cigRes = thirdPartAccessMosService.createImGroup(cigReq);
            if (cigRes != null && BaseResponse.SUCCESS.equals(cigRes.getCode()))
            {
                String groupId = cigRes.getGroupId();
                mtMeetingInfo.setHasGroup(FLAG_OPEN);
                mtMeetingInfo.setGroupId(groupId);
                Map<String, String> map = new HashMap<String, String>();
                for (MtParticipants entity : inMap.values())
                {
                    map.put(entity.getPersonId(), entity.getPersonId());
                }
                for (MtSigninServ entity : serviceMap.values())
                {
                    map.put(entity.getUserId(), entity.getUserId());
                }
                map.remove(SessionContext.getUserId());

                // 加入成员
                if (map.size() > 0)
                {
                    if (!joinImGroup(groupId, map))
                    {
                        throw new JoinImGroupException();
                    }
                }
            }
            else
            {
                throw new CreateImGroupException();
            }
        }
    }

    public boolean checkData(AddMeetingRequest req, AddMeetingResponse res)
    {
        if (StringUtil.isEmpty(req.getRoomId()))
        {
            res.setMessage("会议室信息缺失");
            res.setCode(ErrorCode.CODE_300001);
            LOGGER.info("会议室id为空");
            return false;
        }
        MtMeeting mtMeetingInfo = req.getMeeting();
        if (mtMeetingInfo == null)
        {
            res.setMessage("会议信息缺失");
            res.setCode(ErrorCode.CODE_300001);
            LOGGER.info("会议主体信息为空");
            return false;
        }
        if (StringUtil.isEmpty(mtMeetingInfo.getName()) || StringUtil.isEmpty(mtMeetingInfo.getAddress())
                || mtMeetingInfo.getBeginTime() == null || mtMeetingInfo.getEndTime() == null)
        {
            res.setMessage("会议信息缺失");
            res.setCode(ErrorCode.CODE_300001);
            LOGGER.info("会议主体信息不全");
            return false;
        }
        List<MdParticipants> inParticipantsList = req.getInParticipantsList();
        List<MdParticipants> outParticipantsList = req.getOutParticipantsList();
        if (((inParticipantsList == null ? 0 : inParticipantsList.size())
                + (outParticipantsList == null ? 0 : outParticipantsList.size())) == 0)
        {
            res.setMessage("参会人员不可为空");
            res.setCode(ErrorCode.CODE_300001);
            LOGGER.info("参会人员为空");
            return false;
        }
        for (MdParticipants info : inParticipantsList)
        {
            info.setType(PARTICIPANT_TYPE_INNER);
            if (StringUtil.isEmpty(info.getEntityId()) || StringUtil.isEmpty(info.getEntityName())
                    || StringUtil.isEmpty(info.getEntityType()))
            {
                res.setMessage("内部参会人员信息不全");
                res.setCode(ErrorCode.CODE_300001);
                LOGGER.info("内部参会人员信息不全");
                return false;
            }
            if (!"user".equals(info.getEntityType()) && !"dept".equals(info.getEntityType()))
            {
                res.setMessage("参会人员类型错误");
                res.setCode(ErrorCode.CODE_300001);
                LOGGER.info("参会人员类型错误");
                return false;
            }
        }
        List<String> tempList = new ArrayList<String>();
        for (MdParticipants info : outParticipantsList)
        {
            info.setType(PARTICIPANT_TYPE_OUTER);
            if (StringUtil.isEmpty(info.getEntityId()) || StringUtil.isEmpty(info.getEntityName())
                    || StringUtil.isEmpty(info.getEntityType()))
            {
                res.setMessage("外部参会人员信息不全");
                res.setCode(ErrorCode.CODE_300001);
                LOGGER.info("外部参会人员信息不全");
                return false;
            }
            // 电话号码唯一校验
            if (!tempList.contains(info.getEntityId()))
            {
                tempList.add(info.getEntityId());
            }
        }
        if (outParticipantsList.size() != tempList.size())
        {
            res.setMessage("外部参会人员手机号码重复");
            res.setCode(ErrorCode.CODE_300001);
            LOGGER.info("外部参会人员手机号码重复");
            return false;
        }
        if (FLAG_OPEN.equals(req.getAgendaFlag()))
        {
            List<MtAgenda> agendaList = req.getAgendaList();
            for (MtAgenda info : agendaList)
            {
                if (StringUtil.isEmpty(info.getRemarks()) || info.getBeginTime() == null || info.getEndTime() == null)
                {
                    res.setMessage("议程信息不全");
                    res.setCode(ErrorCode.CODE_300001);
                    LOGGER.info("议程信息不全");
                    return false;
                }

                if (info.getBeginTime().before(mtMeetingInfo.getBeginTime())
                        || info.getEndTime().after(mtMeetingInfo.getEndTime()))
                {
                    res.setMessage("议程超出会议时间");
                    res.setCode(ErrorCode.CODE_300001);
                    LOGGER.info("议程超出会议时间");
                    return false;
                }
            }
        }
        if (FLAG_OPEN.equals(req.getAttachmentFlag()))
        {
            List<MtAttachment> attachmentList = req.getAttachmentList();
            for (MtAttachment info : attachmentList)
            {
                if (StringUtil.isEmpty(info.getFilePath()) || StringUtil.isEmpty(info.getFileName())
                        || StringUtil.isEmpty(info.getSize().toString()))
                {
                    res.setMessage("附件信息不全");
                    res.setCode(ErrorCode.CODE_300001);
                    LOGGER.info("附件信息不全");
                    return false;
                }
            }
        }
        if (FLAG_OPEN.equals(req.getSigninFlag()))
        {
            // 服务人员
            List<MdParticipants> serviceParticipantsList = req.getServiceParticipantsList();
            for (MdParticipants info : serviceParticipantsList)
            {
                info.setType(PARTICIPANT_TYPE_SERVICE);
                if (StringUtil.isEmpty(info.getEntityId()) || StringUtil.isEmpty(info.getEntityName())
                        || StringUtil.isEmpty(info.getEntityType()))
                {
                    res.setMessage("服务人员信息不全");
                    res.setCode(ErrorCode.CODE_300001);
                    LOGGER.info("服务人员信息不全");
                    return false;
                }
                if (!"user".equals(info.getEntityType()) && !"dept".equals(info.getEntityType()))
                {
                    res.setMessage("服务人员类型错误");
                    res.setCode(ErrorCode.CODE_300001);
                    LOGGER.info("服务人员类型错误");
                    return false;
                }
            }
            List<MtSigninSequ> signinSequList = req.getSigninSequList();
            for (MtSigninSequ info : signinSequList)
            {
                if (StringUtil.isEmpty(info.getRemarks()) || StringUtil.isEmpty(info.getSequ().toString()))
                {
                    res.setMessage("签到信息不全");
                    res.setCode(ErrorCode.CODE_300001);
                    LOGGER.info("签到信息不全");
                    return false;
                }
            }
        }
        if (FLAG_OPEN.equals(req.getRemarksFlag()))
        {
            List<MtRemarks> remarksList = req.getRemarksList();
            for (MtRemarks info : remarksList)
            {
                if (StringUtil.isEmpty(info.getRemarks()))
                {
                    res.setMessage("备注信息不全");
                    res.setCode(ErrorCode.CODE_300001);
                    LOGGER.info("备注信息不全");
                    return false;
                }
            }
        }
        return true;
    }

    @ServiceMethod(method = "mapps.meeting.check.capacity", group = "meeting", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public AddMeetingResponse checkCapacity(AddMeetingRequest req) throws Exception
    {
        LOGGER.info("校验容量接口(mapps.meeting.check.capacity)入口,请求参数==" + req.getMeetingJson());
        AddMeetingResponse res = new AddMeetingResponse();
        try
        {
            AddMeetingRequest addInfo = (AddMeetingRequest) JsonUtil.jsonToObject(req.getMeetingJson(),
                    AddMeetingRequest.class);
            if (!checkData(addInfo, res))
            {
                return res;
            }
            List<MdParticipants> inParticipantsList = addInfo.getInParticipantsList();
            Map<String, String> inMap = new HashMap<String, String>();
            for (MdParticipants info : inParticipantsList)
            {
                if ("user".equals(info.getEntityType()))
                {
                    inMap.put(info.getEntityId(), info.getEntityName());
                }
                else if ("dept".equals(info.getEntityType()))
                {
                    GetUsersRequest guReq = new GetUsersRequest();
                    guReq.setDepUuid(info.getEntityId());
                    guReq.setDepScope("1");
                    GetUsersResponse guRes = thirdPartAccessService.getUsersByDept(guReq);
                    for (MyUser user : guRes.getUserInfos())
                    {
                        inMap.put(user.getUserUuid(), user.getUserName());
                    }
                }
            }
            System.out.println(inMap.size());
            MrRoom mr = mrRoomMapper.selectByPrimaryKey(addInfo.getRoomId());
            System.out.println(mr.getCapacity());
            if (mr.getCapacity() < inMap.size())
            {
                res.setMessage("已选参会人员" + inMap.size() + "人大于会议室容量" + mr.getCapacity() + "人，继续发布会议？");
                res.setCode("900001");
                return res;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("校验容量异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return res;
    }

    @ServiceMethod(method = "mapps.meeting.meeting.client.add", group = "meeting", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional(rollbackFor = Exception.class)
    public AddMeetingResponse addMeetingFromClient(AddMeetingRequest req) throws Exception
    {
        LOGGER.info("新增会议接口(mapps.meeting.meeting.client.add)入口,请求参数==" + req.getMeetingJson());
        AddMeetingResponse res = new AddMeetingResponse();
        try
        {
            AddMeetingRequest addInfo = (AddMeetingRequest) JsonUtil.jsonToObject(req.getMeetingJson(),
                    AddMeetingRequest.class);
            if (!checkData(addInfo, res))
            {
                return res;
            }
            String meetingId = IDGen.uuid();
            MtMeeting mtMeetingInfo = addInfo.getMeeting();
            // 会议室预约信息处理
            AddReservedRequest arrReq = new AddReservedRequest();
            arrReq.setRoomId(addInfo.getRoomId());
            arrReq.setMeetingName(mtMeetingInfo.getName());
            arrReq.setReservedDate(new SimpleDateFormat("yyyy-MM-dd").format(mtMeetingInfo.getBeginTime()));
            arrReq.setReservedStartTime(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mtMeetingInfo.getBeginTime()));
            arrReq.setReservedEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mtMeetingInfo.getEndTime()));
            arrReq.setMeetingId(meetingId);
            AddReservedResponse arrRes = mrReservedServiceV20.addMrReserved(arrReq);
            if (arrRes == null)
            {
                res.setMessage("会议室预约信息保存失败");
                ErrorCode.fail(res, ErrorCode.CODE_100001);
                LOGGER.error("会议室预约信息保存失败");
                return res;
            }
            else if (!arrRes.getCode().equals("1"))
            {
                ErrorCode.fail(res, arrRes.getCode());
                return res;
            }
            mtMeetingInfo.setReservedId(arrRes.getReservedId());
            // 会议信息处理
            mtMeetingInfo.setSponsor(SessionContext.getUserId());
            mtMeetingInfo.setCreateTime(new Date());
            mtMeetingInfo.setStatus(arrRes.getStatus());
            mtMeetingInfo.setReleaseTime(new Date());

            Calendar nowTime = Calendar.getInstance();
            nowTime.setTime(mtMeetingInfo.getBeginTime());
            String type = mtMeetingInfo.getNoticeType();
            int val = mtMeetingInfo.getNoticeSet();
            if ("1".equals(type))
            {
                nowTime.add(Calendar.MINUTE, -val);
                mtMeetingInfo.setNoticeTime(nowTime.getTime());
            }
            else if ("2".equals(type))
            {
                nowTime.add(Calendar.HOUR, -val);
                mtMeetingInfo.setNoticeTime(nowTime.getTime());
            }
            // 会议信息 MtMeeting
            mtMeetingInfo.setId(meetingId);
            insertMeetingInfo(meetingId, addInfo);
            mtMeetingMapper.insertSelective(mtMeetingInfo);
        }
        catch (CreateImGroupException ce)
        {
            LOGGER.error("新增会议室信息异常：{}", ce);
            ErrorCode.fail(res, ErrorCode.CODE_300018);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        catch (JoinImGroupException je)
        {
            LOGGER.error("新增会议室信息异常：{}", je);
            ErrorCode.fail(res, ErrorCode.CODE_300018);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        catch (Exception e)
        {
            LOGGER.error("新增会议室信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        finally
        {
            tmpReservedMapper.deleteByPrimaryKey(req.getLockId());
        }
        return res;
    }

    public boolean joinImGroup(String groupId, Map<String, String> map) throws JoinImGroupException
    {

        String members = "";
        for (String loginId : map.values())
        {
            members += loginId + SEPARATOR;
        }
        JoinImGroupRequest jigReq = new JoinImGroupRequest();
        jigReq.setGroupId(groupId);
        if (StringUtil.isNotEmpty(members))
        {
            jigReq.setMembers(members.substring(0, members.length() - 1));
        }
        JoinImGroupResponse jimRes = thirdPartAccessMosService.joinImGroup(jigReq);
        if (jimRes == null || BaseResponse.FAIL.equals(jimRes.getCode()))
        {
            return false;
        }
        return true;
    }

    @ServiceMethod(method = "mapps.meeting.meeting.webquery", group = "meeting", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryMeetingResponse getMeetingForWeb(QueryMeetingServerRequest req)
    {

        QueryMeetingResponse res = new QueryMeetingResponse();
        try
        {
            LOGGER.info("分页查询会议接口(mapps.meeting.meeting.webquery)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ecid", SessionContext.getEcId());
            map.put("userId", SessionContext.getUserId());
            if (StringUtil.isNotEmpty(req.getMeetingName()))
            {
                map.put("meetingName", "%" + req.getMeetingName() + "%");
            }
            if (StringUtil.isNotEmpty(req.getMeetingHolder()))
            {
                map.put("sponsorName", req.getMeetingHolder());
                // String holderId = "";
                // String meetingHolder = req.getMeetingHolder();
                // GetUsersRequest request = new GetUsersRequest();
                // request.setUserName(meetingHolder);
                // GetUsersResponse response = thirdPartAccessService.getUsers(request);
                // List<MyUser> users = response.getUserInfos();
                // if (users.size() > 0)
                // {
                // holderId = users.get(0).getLoginId();
                // }
                // map.put("meetingHolder", holderId);
            }
            if (StringUtil.isNotEmpty(req.getMeetingStatus()))
            {
                map.put("meetingStatus", req.getMeetingStatus());
            }
            if (StringUtil.isNotEmpty(req.getBeginTime()))
            {
                map.put("beginTime", req.getBeginTime());
            }
            if (StringUtil.isNotEmpty(req.getEndTime()))
            {
                map.put("endTime", DateUtil.dayAdd(DateUtil.convertToDate(req.getEndTime()), 1));
            }
            if (StringUtil.isNotEmpty(req.getCreate_beginTime()))
            {
                map.put("create_beginTime", req.getCreate_beginTime());
            }
            if (StringUtil.isNotEmpty(req.getCreate_endTime()))
            {
                map.put("create_endTime", DateUtil.dayAdd(DateUtil.convertToDate(req.getCreate_endTime()), 1));
            }
            if (StringUtil.isNotEmpty(req.getAddress()))
            {
                map.put("address", "%" + req.getAddress() + "%");
            }
            if (StringUtil.isNotEmpty(req.getOrder()))
            {
                map.put("order", req.getOrder());
            }
            if (StringUtil.isNotEmpty(req.getSelfStatus()))
            {
                // 1 我召开的 2 我参与的 3我服务的
                if ("2".equals(req.getSelfStatus()))
                {
                    map.put("selfAttend", "-1");
                }
                else if ("1".equals(req.getSelfStatus()))
                {
                    map.put("selfCreate", "-1");
                }
                else if ("3".equals(req.getSelfStatus()))
                {
                    map.put("selfService", "-1");
                }
            }
            if (StringUtil.isNotEmpty(req.getSort()))
            {
                String sort = "";
                if (req.getSort().contains("beginTimeStr"))
                {
                    sort = req.getSort().replaceAll("beginTimeStr", "begin_time");
                }
                else if (req.getSort().contains("endTimeStr"))
                {
                    sort = req.getSort().replaceAll("endTimeStr", "end_time");
                }
                else if (req.getSort().contains("createTimeStr"))
                {
                    sort = req.getSort().replaceAll("createTimeStr", "create_time");
                }
                else
                {
                    sort = req.getSort();
                }
                map.put("sort", sort);
            }
            PageHelper.startPage(req.getOffset(), req.getLimit());
            List<ClientMeetingInfo> list = mtMeetingMapper.getMeetingForWeb(map);
            PageInfo<ClientMeetingInfo> page = new PageInfo<ClientMeetingInfo>(list);
            res.setTotal(page.getTotal());
            if (list.size() == 0)
            {
                list = new ArrayList<ClientMeetingInfo>();
            }
            res.setMeetingList(list);
            res.success();
            LOGGER.info("分页查询会议信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("分页查询会议信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    // @ServiceMethod(method = "mapps.meeting.meeting.clientquery", group = "meeting", groupTitle = "API", version =
    // "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryMeetingResponse getMeetingFromClient(QueryMeetingRequest req)
    {
        QueryMeetingResponse res = new QueryMeetingResponse();
        try
        {
            LOGGER.info("分页查询会议接口(mapps.meeting.meeting.clientquery)入口,请求参数==" + LogUtil.getObjectInfo(req));
            if (req.getTimestamp() == 0l)
            {
                req.setTimestamp(new Date().getTime());
            }
            Map<String, Object> map = initQuery(req);
            PageHelper.startPage(req.getOffset(), req.getLimit());
            List<ClientMeetingInfo> list = mtMeetingMapper.getMeeting(map);
            PageInfo<ClientMeetingInfo> page = new PageInfo<ClientMeetingInfo>(list);
            res.setTotal(page.getTotal());
            if (list == null)
            {
                list = new ArrayList<ClientMeetingInfo>();
            }
            res.setMeetingList(list);
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
            LOGGER.info("分页查询会议成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("分页查询会议信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public Map<String, Object> initQuery(QueryMeetingRequest req) throws Exception
    {
        String meetingName = req.getMeetingName();
        String status = req.getMeetingStatus();
        String selfStatus = req.getSelfStatus();
        Date time = new Date(req.getTimestamp());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("time", time);
        map.put("ecid", SessionContext.getEcId());
        map.put("userId", SessionContext.getUserId());

        if (StringUtil.isNotEmpty(meetingName))
        {
            map.put("likeParam", "%" + meetingName + "%");
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
        if (StringUtil.isNotEmpty(selfStatus))
        {
            // 2 我参与的 1 我召开的 3我服务的
            if ("2".equals(selfStatus))
            {
                map.put("selfAttend", "-1");
            }
            else if ("1".equals(selfStatus))
            {
                map.put("selfCreate", "-1");
            }
            else if ("3".equals(selfStatus))
            {
                map.put("selfService", "-1");
            }
        }
        return map;
    }

    public String queryQrcode(String meetingId)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("meetingId", meetingId);
        map.put("userId", SessionContext.getUserId());
        map.put("ecid", SessionContext.getEcId());
        // 根据meetingId和personId查询参会者id
        MtParticipants mt = mtParticipantsMapper.getParticipant(map);
        if (mt == null)
        {
            mt = mtParticipantsMapper.getParticipantService(map);
            if (mt == null)
            {
                return ErrorCode.CODE_100001;
            }
        }
        String reStr = "-1";
        if ("-1".equals(mt.getQrcode()))
        {
            reStr = getQrcode(mt.getId());
            if (!"-1".equals(reStr))
            {
                reStr = webRoot + reStr;
            }
        }
        else
        {
            reStr = webRoot + mt.getQrcode();
        }
        return reStr;
    }

    @ServiceMethod(method = "mapps.meeting.meeting.detail", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryMeetingDetailResponse getMeetingDetail(QueryMeetingDetailRequest req)
    {
        QueryMeetingDetailResponse res = new QueryMeetingDetailResponse();
        try
        {
            LOGGER.info("查询会议室详细信息接口(mapps.meeting.meeting.detail)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("meetingId", req.getMeetingId());
            map.put("userId", SessionContext.getUserId());
            map.put("ecid", SessionContext.getEcId());
            map.put("personId", SessionContext.getUserId());
            List<MtParticipants> mpList = mtParticipantsMapper.getByMeetingId(map);
            if (mpList == null || mpList.size() == 0)
            {
                res.fail("hasDelete");
                LOGGER.info("未查询到相关参会信息,meetingid:{},userId:{}", req.getMeetingId(), SessionContext.getUserId());
                return res;
            }
            if (req.isRefreshFlag())
            {
                // 基本信息+备注
                ClientMeetingInfo cmInfo = mtMeetingMapper.getMeetingInfo(map);
                // 获取召开人信息
                GetUsersRequest guReq = new GetUsersRequest();
                guReq.setLoginId(cmInfo.getSponsor());
                GetUsersResponse guRes = thirdPartAccessService.getUsers(guReq);
                if (guRes != null && guRes.getUserInfos().size() > 0)
                {
                    MyUser mu = guRes.getUserInfos().get(0);
                    cmInfo.setPerson(mu.getUserName());
                    cmInfo.setTel(mu.getPhoneNum());
                }
                res.setMeeting(cmInfo);
                res.setRemarksList(mtRemarksMapper.getByMeetingId(map));
                // 根据meetingId和personId查询参会者id
                String reCode = queryQrcode(req.getMeetingId());
                if (ErrorCode.CODE_100001.equals(reCode))
                {
                    ErrorCode.fail(res, ErrorCode.CODE_100001);
                    LOGGER.info("未查询到相关参会信息,meetingid:{},userId:{}", req.getMeetingId(), SessionContext.getUserId());
                    return res;
                }
                res.setQrcode(reCode);
                // 议程信息
                List<String> days = mtAgendaMapper.getDayByMeetingId(map);
                List<MtAgenda> agendaList = mtAgendaMapper.getByMeetingId(map);
                List<ClientAgendaInfo> caInfoList = new ArrayList<ClientAgendaInfo>();
                for (String day : days)
                {
                    ClientAgendaInfo entity = new ClientAgendaInfo();
                    entity.setDay(day);
                    List<MtAgenda> agendas = new ArrayList<MtAgenda>();
                    for (MtAgenda info : agendaList)
                    {
                        if (day.equals(info.getDayStr()))
                        {
                            agendas.add(info);
                        }
                    }
                    entity.setAgendaList(agendas);
                    caInfoList.add(entity);
                }
                res.setAgendaList(caInfoList);
                // 会议资料
                res.setAttachmentList(mtAttachmentMapper.getByMeetingId(map));
            }
            // 签到信息
            List<MtSigninSequ> list = mtSigninSequMapper.getByMeetingId(map);
            for (MtSigninSequ sequ : list)
            {
                int num = sequ.getSequ() == null ? 0 : sequ.getSequ().intValue();
                sequ.setSequStr(ConvertUtil.numberToChinese(num));
            }
            res.setSigninSequList(list);
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

    @ServiceMethod(method = "mapps.meeting.meeting.outerdetail", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
    public QueryMeetingDetailResponse getOuterDetail(QueryOuterDetailRequest req)
    {
        QueryMeetingDetailResponse res = new QueryMeetingDetailResponse();
        try
        {
            LOGGER.info("外部人员查询会议室详细信息接口(mapps.meeting.meeting.outerdetail)入口,请求参数==" + LogUtil.getObjectInfo(req));

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", req.getParticipantsId());
            MtParticipants mt = mtParticipantsMapper.getParById(map);
            if (mt == null)
            {
                ErrorCode.fail(res, ErrorCode.CODE_100001);
                return res;
            }
            map.clear();
            map.put("meetingId", mt.getMeetingId());
            // 基本信息+备注
            ClientMeetingInfo cmInfo = mtMeetingMapper.getMeetingInfo(map);
            // 获取召开人信息
            GetUsersRequest guReq = new GetUsersRequest();
            guReq.setLoginId(cmInfo.getSponsor());
            GetUsersResponse guRes = thirdPartAccessService.getUsers(guReq);
            if (guRes != null && guRes.getUserInfos().size() > 0)
            {
                MyUser mu = guRes.getUserInfos().get(0);
                cmInfo.setPerson(mu.getUserName());
                cmInfo.setTel(mu.getPhoneNum());
            }
            res.setMeeting(cmInfo);
            res.setRemarksList(mtRemarksMapper.getByMeetingId(map));
            String code = "-1".equals(mt.getQrcode()) ? getQrcode(mt.getId()) : mt.getQrcode();
            res.setQrcode("-1".equals(code) ? code : webRoot + code);
            // 议程信息
            List<String> days = mtAgendaMapper.getDayByMeetingId(map);
            List<MtAgenda> agendaList = mtAgendaMapper.getByMeetingId(map);
            List<ClientAgendaInfo> caInfoList = new ArrayList<ClientAgendaInfo>();
            for (String day : days)
            {
                ClientAgendaInfo entity = new ClientAgendaInfo();
                entity.setDay(day);
                List<MtAgenda> agendas = new ArrayList<MtAgenda>();
                for (MtAgenda info : agendaList)
                {
                    if (day.equals(info.getDayStr()))
                    {
                        agendas.add(info);
                    }
                }
                entity.setAgendaList(agendas);
                caInfoList.add(entity);
            }
            res.setAgendaList(caInfoList);
            // 会议资料
            res.setAttachmentList(mtAttachmentMapper.getByMeetingId(map));
            // 签到信息
            List<MtSigninSequ> list = mtSigninSequMapper.getByMeetingId(map);
            res.setSigninSequList(list);
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

    public List<MdParticipants> getDeptInfo(List<MdParticipants> mdList) throws Exception
    {
        List<String> mdUserIdList = new ArrayList<String>();
        for (MdParticipants mdInfo : mdList)
        {
            if ("user".equals(mdInfo.getEntityType()) && (PARTICIPANT_TYPE_INNER.equals(mdInfo.getType())
                    || PARTICIPANT_TYPE_SERVICE.equals(mdInfo.getType())))
            {
                mdUserIdList.add(mdInfo.getEntityId());
            }
        }
        if (mdUserIdList.size() > 0)
        {
            List<MyUser> muList = thirdPartAccessService.getUserInfos(SessionContext.getOrgId(),
                    StringUtils.join(mdUserIdList.toArray(), ","));
            for (MdParticipants mdInfo : mdList)
            {
                for (MyUser user : muList)
                {
                    if (mdInfo.getEntityId().equals(user.getLoginId()))
                    {
                        mdInfo.setDeptName(user.getDeptName());
                        mdInfo.setPhone(user.getPhoneNum());
                    }
                }
            }
        }
        return mdList;
    }

    @ServiceMethod(method = "mapps.meeting.meeting.detailForWeb", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryMeetDetailWebResponse getMeetingDetailWeb(QueryMeetingDetailRequest req)
    {
        QueryMeetDetailWebResponse res = new QueryMeetDetailWebResponse();
        try
        {
            LOGGER.info("查询会议室详细信息接口(mapps.meeting.meeting.detail)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("meetingId", req.getMeetingId());
            map.put("userId", SessionContext.getUserId());
            map.put("ecid", SessionContext.getEcId());
            // 基本信息+备注
            res.setMeeting(mtMeetingMapper.getMeetingInfo(map));
            res.setRemarksList(mtRemarksMapper.getByMeetingId(map));
            // 根据meetingId和personId查询参会者id
            String reCode = queryQrcode(req.getMeetingId());
            if (ErrorCode.CODE_100001.equals(reCode))
            {
                ErrorCode.fail(res, ErrorCode.CODE_100001);
                LOGGER.info("未查询到相关参会信息,meetingid:{},userId:{}", req.getMeetingId(), SessionContext.getUserId());
                return res;
            }
            res.setQrcode(reCode);
            // 议程信息
            List<MtAgenda> agendaList = mtAgendaMapper.getByMeetingId(map);
            for (MtAgenda agenda : agendaList)
            {
                agenda.setBeginTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(agenda.getBeginTime()));
                agenda.setEndTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(agenda.getEndTime()));
            }
            res.setAgendaList(agendaList);
            // 会议资料
            res.setAttachmentList(mtAttachmentMapper.getByMeetingId(map));
            // web参与与服务人员明细
            res.setMdList(getDeptInfo(mdParticipantsMapper.getByMeetingId(map)));
            // 签到信息
            List<MtSigninSequ> list = mtSigninSequMapper.getByMeetingId(map);
            for (MtSigninSequ sequ : list)
            {
                int num = sequ.getSequ() == null ? 0 : sequ.getSequ().intValue();
                sequ.setSequStr(ConvertUtil.numberToChinese(num));
            }

            res.setSigninSequList(list);
            List<MtSigninRecord> signinRecordList = mtSigninRecordMapper.getByMeetingId(map);
            String loginIds = "";
            for (int i = 0, j = 0; i < signinRecordList.size(); i++)
            {
                MtSigninRecord record = signinRecordList.get(i);
                if (PARTICIPANT_TYPE_INNER.equals(record.getPersonType()))
                {
                    if (j == 0)
                    {
                        loginIds += signinRecordList.get(i).getPersonId();
                    }
                    else
                    {
                        loginIds += "," + signinRecordList.get(i).getPersonId();
                    }
                    j++;
                }
            }
            if (loginIds.length() > 0)
            {
                List<MyUser> muList = thirdPartAccessService.getUserInfos(SessionContext.getOrgId(), loginIds);
                for (MtSigninRecord record : signinRecordList)
                {
                    if (PARTICIPANT_TYPE_INNER.equals(record.getPersonType()))
                    {
                        for (MyUser user : muList)
                        {
                            if (record.getPersonId().equals(user.getLoginId()))
                            {
                                record.setDeptName(user.getDeptName());
                                record.setPhone(user.getPhoneNum());
                            }
                        }
                    }
                    else if (PARTICIPANT_TYPE_OUTER.equals(record.getPersonType()))
                    {
                        record.setPhone(record.getPersonId());
                    }
                }
            }
            res.setSigninRecordList(signinRecordList);
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

    @ServiceMethod(method = "mapps.meeting.meeting.getattachmentfromWeb", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryMeetDetailWebResponse getAttachmentFromWeb(QueryMeetingDetailRequest req)
    {
        QueryMeetDetailWebResponse res = new QueryMeetDetailWebResponse();
        try
        {
            LOGGER.info("查询会议附件信息接口(mapps.meeting.meeting.detail)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("meetingId", req.getMeetingId());
            map.put("userId", SessionContext.getUserId());
            map.put("ecid", SessionContext.getEcId());
            // 会议资料
            res.setAttachmentList(mtAttachmentMapper.getByMeetingId(map));
            LOGGER.info("查询会议附件信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询会议附件信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public void clearMeetingData(String meetingId)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("meetingId", meetingId);
        mdParticipantsMapper.deleteByMeetingId(map);
        mtParticipantsMapper.deleteByMeetingId(map);
        mtAgendaMapper.deleteByMeetingId(map);
        mtAttachmentMapper.deleteByMeetingId(map);
        mtSigninSequMapper.deleteByMeetingId(map);
        mtSigninServMapper.deleteByMeetingId(map);
        mtSigninRecordMapper.deleteByMeetingId(map);
        mtRemarksMapper.deleteByMeetingId(map);
    }

    @ServiceMethod(method = "mapps.meeting.meeting.cancel", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional
    public CancelMeetResponse cancelMeeting(CancelMeetRequset req)
    {
        CancelMeetResponse cancelRes = new CancelMeetResponse();
        try
        {
            LOGGER.info("取消会议接口(mapps.meeting.meeting.cancel)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("meetingId", req.getMeetingId());
            reqMap.put("ecid", SessionContext.getEcId());
            reqMap.put("userId", SessionContext.getUserId());
            ClientMeetingInfo meeting = mtMeetingMapper.getMeetingInfo(reqMap);

            if (meeting.getStatus().equals(MtMeetingService.STAUTS_20)
                    || meeting.getStatus().equals(MtMeetingService.STAUTS_30)
                    || meeting.getStatus().equals(MtMeetingService.RESERVED_STATUS_APPROVEING))
            {
                // 取消会议
                mtMeetingMapper.cancelMeetById(reqMap);
                // bjyh start 同步会议室状态
                DeleteReservedRequest drReq = new DeleteReservedRequest();
                drReq.setOperationType(MrReservedServiceV20.OPERATION_TYPE1);
                drReq.setReservedId(meeting.getReservedId());
                mrReservedServiceV20.delMrReserved(drReq);
                // bjyh end
                // 获取参会人员登录名
                // 推送给参会人员
                String title = meeting.getMeetingName() + "会议取消";
                String content = "原定于" + meeting.getBeginTimeStr() + "-" + meeting.getEndTimeStr() + "的"
                        + meeting.getMeetingName() + "会议已被" + SessionContext.getUserName() + "取消";
                // sendMsg(req.getMeetingId(), meeting.getReservedId(), "会议取消", "");
                if (meeting.getStatus().equals(MtMeetingService.RESERVED_STATUS_APPROVEING))
                {
                    thirdPartAccessService.pushReceiveEvent(SessionContext.getEcId(), meeting.getSponsor(), title,
                            content, req.getMeetingId(), null);
                }
                else
                {
                    sendMsg(req.getMeetingId(), meeting.getReservedId(), title, content);
                }
                LOGGER.info("取消会议接口成功");
            }
            else
            {
                ErrorCode.fail(cancelRes, ErrorCode.CODE_100001);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("取消会议接口异常：{}", e);
            ErrorCode.fail(cancelRes, ErrorCode.CODE_100001);
            throw new IllegalArgumentException();
        }
        return cancelRes;
    }

    @ServiceMethod(method = "mapps.meeting.meeting.over", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional
    public CancelMeetResponse overMeeting(CancelMeetRequset req)
    {
        CancelMeetResponse cancelRes = new CancelMeetResponse();
        try
        {
            LOGGER.info("结束会议接口(mapps.meeting.meeting.over)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("meetingId", req.getMeetingId());
            reqMap.put("ecid", SessionContext.getEcId());
            reqMap.put("userId", SessionContext.getUserId());
            ClientMeetingInfo meeting = mtMeetingMapper.getMeetingInfo(reqMap);

            if (meeting.getStatus().equals(MtMeetingService.STAUTS_30))
            {
                // 取消会议
                mtMeetingMapper.overMeetById(reqMap);
                // bjyh start 同步会议室状态
                DeleteReservedRequest drReq = new DeleteReservedRequest();
                drReq.setOperationType(MrReservedServiceV20.OPERATION_TYPE2);
                drReq.setReservedId(meeting.getReservedId());
                mrReservedServiceV20.delMrReserved(drReq);
                // bjyh end
                LOGGER.info("结束会议接口成功");
            }
            else
            {
                ErrorCode.fail(cancelRes, ErrorCode.CODE_100001);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("结束会议接口异常：{}", e);
            ErrorCode.fail(cancelRes, ErrorCode.CODE_100001);
            throw new IllegalArgumentException();
        }
        return cancelRes;
    }

    public void sendMsg(String meetingId, String reservedId, String title, String content)
    {
        Map<String, Object> parMap = new HashMap<String, Object>();
        parMap.put("meetingId", meetingId);
        parMap.put("personType", PARTICIPANT_TYPE_INNER);
        List<MtParticipants> innerList = mtParticipantsMapper.getNoSendInnerByMeetingId(parMap);
        // String[] loginIds = new String[innerList.size()];
        // if (innerList.size() > 0)
        // {
        // for (int i = 0; i < innerList.size(); i++)
        // {
        // loginIds[i] = innerList.get(i).getPersonId();
        // }
        // }
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
        thirdPartAccessService.pushReceiveEvent(SessionContext.getEcId(), StringUtils.join(iList, ","), title, content,
                meetingId, null);
        if (sList != null && sList.size() > 0)
            thirdPartAccessService.pushReceiveEventForService(SessionContext.getEcId(), StringUtils.join(sList, ","),
                    title, content, meetingId, null);
        // parMap.put("personType", PARTICIPANT_TYPE_OUTER);
        // List<MtParticipants> outerList = mtParticipantsMapper.getNoSendOuterByMeetingId(parMap);
        // String[] phones = new String[outerList.size()];
        // if (outerList.size() > 0)
        // {
        // for (int i = 0; i < outerList.size(); i++)
        // {
        // phones[i] = outerList.get(i).getPersonId();
        // }
        // }
        // thirdPartAccessService.sendSms(StringUtils.join(phones, ","), content);
    }

    @ServiceMethod(method = "mapps.meeting.meeting.apply", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional
    public CancelMeetResponse applyMeeting(CancelMeetRequset req)
    {

        CancelMeetResponse cancelRes = new CancelMeetResponse();
        try
        {
            LOGGER.info("发布会议接口(mapps.meeting.meeting.cancel)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("meetingId", req.getMeetingId());
            reqMap.put("ecid", SessionContext.getEcId());
            reqMap.put("userId", SessionContext.getUserId());
            ClientMeetingInfo meeting = mtMeetingMapper.getMeetingInfo(reqMap);
            if (meeting.getStatus().equals(MtMeetingService.STAUTS_10))
            {
                // 发布会议
                mtMeetingMapper.applyMeetById(reqMap);
                // 获取参会人员登录名
                Map<String, Object> parMap = new HashMap<String, Object>();
                parMap.put("meetingId", req.getMeetingId());
                List<MtParticipants> mtpar = mtParticipantsMapper.getByMeetingId(parMap);
                String[] loginIds = new String[mtpar.size()];
                if (mtpar.size() > 0)
                {
                    for (int i = 0; i < mtpar.size(); i++)
                    {
                        loginIds[i] = mtpar.get(i).getPersonId();
                    }
                }
                LOGGER.info("发布会议接口成功");
            }
            else
            {
                ErrorCode.fail(cancelRes, ErrorCode.CODE_100001);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("取消会议接口异常：{}", e);
            ErrorCode.fail(cancelRes, ErrorCode.CODE_100001);
            throw new IllegalArgumentException();
        }
        return cancelRes;
    }

    @ServiceMethod(method = "mapps.meeting.meeting.delete", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional
    public DeleteMeetResponse deleteMeeting(DeleteMeetRequest req)
    {
        DeleteMeetResponse res = new DeleteMeetResponse();
        try
        {
            LOGGER.info("删除会议接口入口,请求参数==" + LogUtil.getObjectInfo(req));

            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("meetingId", req.getMeetingId());
            reqMap.put("userId", SessionContext.getUserId());
            reqMap.put("ecid", SessionContext.getEcId());
            // 删除会议是自动退出概念，只改变标志位
            mtParticipantsMapper.setMeetVisable(reqMap);
            ClientMeetingInfo meeting = mtMeetingMapper.getMeetingInfo(reqMap);
            // bjyh start 同步会议室状态
            DeleteReservedRequest drReq = new DeleteReservedRequest();
            drReq.setOperationType(MrReservedServiceV20.OPERATION_TYPE3);
            drReq.setReservedId(meeting.getReservedId());
            mrReservedServiceV20.delMrReserved(drReq);
            // bjyh end
            LOGGER.info("删除会议成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("删除会议异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
            throw new NullPointerException();
        }
        return res;
    }

    // 生成二维码
    private String getQrcode(String content)
    {
        Map<String, Object> resMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("content", content);
            RopClient client = new RopClient(serviceUrl, appKey, appSecret, format);
            resMap = client.requestForMap("mapps.fileservice.qrcode.gen", "1.0", map);
            if (resMap.containsKey("code") && "1".equals(resMap.get("code")) && resMap.containsKey("path"))
            {
                return (String) resMap.get("path");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "-1";
    }

    /**
     * 人员签到
     * 
     * @param args
     */
    @ServiceMethod(method = "mapps.meeting.meeting.signin", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public SignInResponse signIn(SignInRequest req)
    {

        SignInResponse res = new SignInResponse();
        try
        {
            LOGGER.info("签到入口" + LogUtil.getObjectInfo(req));
            // 根据Id查出参会人员信息
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", req.getParticipantId());
            MtParticipants mt = mtParticipantsMapper.getParById(map);
            // 签到
            if (mt != null)
            {
                map.put("meetingId", mt.getMeetingId());
                map.put("sequId", req.getSequId());// 签到序列
                map.put("personId", mt.getPersonId());// 参会人员
                map.put("serId", SessionContext.getUserId());// 服务人员
                MtSigninRecord mr = mtSigninRecordMapper.getUpdateData(map);
                if (mr != null)
                {
                    if (mr.getSigned().equals("Y") || mr.getSignTime() != null)
                    {
                        ErrorCode.fail(res, ErrorCode.CODE_100002);
                    }
                    else if (mr.getSigned().equals("N"))
                    {
                        map.put("time", new Date());
                        mtSigninRecordMapper.updateSignRecord(map);
                        res.setPersonName(mt.getPersonName());
                        LOGGER.info("签到成功");
                    }
                }
                else
                {
                    ErrorCode.fail(res, ErrorCode.CODE_100001);
                }
            }
            else
            {
                ErrorCode.fail(res, ErrorCode.CODE_100001);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("签到异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    @ServiceMethod(method = "mapps.meeting.meeting.queryAttach", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public AttachListResponse queryAttach(QueryMeetingDetailRequest req)
    {
        AttachListResponse res = new AttachListResponse();
        try
        {
            LOGGER.info("查询附件列表" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("meetingId", req.getMeetingId());
            PageHelper.startPage(req.getOffset(), req.getLimit());
            List<MtAttachment> mt = mtAttachmentMapper.getByMeetingId(map);
            PageInfo<MtAttachment> page = new PageInfo<MtAttachment>(mt);
            for (MtAttachment ma : mt)
            {
                String docId = ma.getFilePath();
                GetDocUrlRequest docReq = new GetDocUrlRequest();
                docReq.setDocumentId(docId);
                docReq.setPrivilege("3");
                GetDocUrlResponse docRes = thirdPartAccessService.GetDocUrl(docReq);
                ma.setDownloadUrl(docRes.getDocUrls()[0]);
                docReq.setPrivilege("1");
                GetDocUrlResponse docRes1 = thirdPartAccessService.GetDocUrl(docReq);
                ma.setViewUrl(docRes1.getDocUrls()[0]);
            }
            res.setTotal(page.getTotal());
            res.setAttachList(mt);
            LOGGER.info("查询附件列表成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询附件列表异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    @ServiceMethod(method = "mapps.meeting.meeting.querySequ", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public SignRecordResponse querySequ(QueryMeetingDetailRequest req)
    {
        SignRecordResponse res = new SignRecordResponse();
        try
        {
            LOGGER.info("查询签到次数" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("meetingId", req.getMeetingId());
            List<MtSigninSequ> msq = mtSigninSequMapper.getByMeetingId(map);
            res.setSize(msq.size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询签到次数异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 查询签到列表
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meeting.meeting.querySign", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public SignRecordResponse querySign(QueryMeetingDetailRequest req)
    {
        SignRecordResponse res = new SignRecordResponse();
        StringBuffer colSql = new StringBuffer();
        StringBuffer caseSql = new StringBuffer();
        try
        {
            LOGGER.info("查询签到列表" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("meetingId", req.getMeetingId());
            List<MtSigninSequ> msq = mtSigninSequMapper.getByMeetingId(map);
            for (MtSigninSequ mssInfo : msq)
            {
                colSql.append(",max(stime" + mssInfo.getSequ() + ") as \"signInfo.sequ" + mssInfo.getSequ() + "\" ");
                caseSql.append(",case when ss.sequ = '" + mssInfo.getSequ()
                        + "' then to_char(sign_time,'yyyy-mm-dd hh24:mi:ss') else null end stime" + mssInfo.getSequ()
                        + " ");
            }
            map.put("colSql", colSql.toString());
            map.put("caseSql", caseSql.toString());
            if (StringUtil.isNotEmpty(req.getSort()))
            {
                String[] strings = req.getSort().split(" ");
                String num = strings[0].replace("sequ", "");
                map.put("sort", "max(stime" + num + ") " + strings[1]);
            }
            if (req.getOffset() > 0 && req.getLimit() > 0)
            {
                PageHelper.startPage(req.getOffset(), req.getLimit());
            }
            List<LinkedHashMap<String, Object>> sdList = mtSigninRecordMapper.getSignDetail(map);
            if (req.getOffset() > 0 && req.getLimit() > 0)
            {
                PageInfo<LinkedHashMap<String, Object>> page = new PageInfo<LinkedHashMap<String, Object>>(sdList);
                res.setTotal(page.getTotal());
            }
            else
            {
                res.setTotal(sdList.size());
            }
            JSONArray json = JSONArray.fromObject(sdList);
            @SuppressWarnings("unchecked")
            List<SignDetail> srrList = (List<SignDetail>) JsonUtil.jsonToObject(json.toString(), SignDetail.class);

            // 调用第三方接口查询用户信息
            List<String> loginIds = new ArrayList<String>();
            for (SignDetail sd : srrList)
            {
                if (!PARTICIPANT_TYPE_OUTER.equals(sd.getPersonType()))
                {
                    loginIds.add(sd.getPersonId());
                }
                else
                {
                    sd.setPhone(sd.getPersonId());
                }
            }
            List<MyUser> muList = thirdPartAccessService.getUserInfos(SessionContext.getOrgId(),
                    StringUtils.join(loginIds, ","));
            // 设置电话号码和部门信息
            for (SignDetail sd : srrList)
            {
                if (sd.getSignInfo() == null)
                {
                    sd.setSignInfo(new LinkedHashMap<String, String>());
                }
                for (MyUser mu : muList)
                {
                    if (sd.getPersonId().equals(mu.getLoginId()))
                    {
                        sd.setPhone(mu.getPhoneNum());
                        sd.setDeptName(mu.getDeptName());
                    }
                }
            }

            res.setSignRecordList(srrList);
            res.setSize(msq.size());
            LOGGER.info("查询签到列表成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询签到列表异常：{}", e);
        }
        return res;
    }

    /**
     * 导出签到详情
     */
    @SuppressWarnings("rawtypes")
    @ServiceMethod(method = "mapps.meeting.meeting.exportSignInfo", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public FileResponse exportSign(QueryMeetingDetailRequest req)
    {
        FileResponse fr = null;
        InputStream fileInput = null;
        OutputStream fileOuput = null;
        try
        {
            SignRecordResponse sres = querySign(req);
            List<SignDetail> preData = sres.getSignRecordList();
            // 定义签到列
            List<String> cols = new ArrayList<String>();
            for (int i = 1; i <= sres.getSize(); i++)
            {
                cols.add("第" + i + "次签到时间");
            }
            // 定义数据主体
            for (SignDetail sd : preData)
            {
                Map<String, String> map = sd.getSignInfo();
                List<String> time = new ArrayList<String>();
                for (int i = 1; i <= sres.getSize(); i++)
                {
                    for (Map.Entry entry : map.entrySet())
                    {
                        if (entry.getKey().equals("sequ" + i))
                        {
                            time.add((String) entry.getValue());
                        }
                    }
                }
                sd.setSignTimes(time);
            }
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            File targetFile = File.createTempFile("签到记录-" + simpleDate.format(new Date()), ".xls");
            String destPath = targetFile.getPath();
            fileInput = this.getClass().getClassLoader().getResourceAsStream("template/signRecord.xls");
            fileOuput = new FileOutputStream(targetFile);
            ExcelUtils.generateExcelByTemplate(fileOuput, fileInput, cols, "cols", preData, "contents", 65000, "签到");
            fr = getResponseFile(new File(destPath), true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fileOuput != null)
                    fileOuput.close();
                if (fileInput != null)
                    fileInput.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return fr;
    }

    /**
     * 获得返回文件
     * 
     * @param photoId
     * @return
     * @throws Exception
     */
    public FileResponse getResponseFile(File file, boolean flag) throws Exception
    {
        FileResponse respFile = new FileResponse(file, flag);
        if (null != file)
        {
            respFile.setFile(file);
        }
        else
        {
            respFile = FileResponse.NOT_EXISTS;
        }
        return respFile;
    }

    @Scheduled(cron = "50 1/2 * * * ?")
    public void scheduleMeetingCall() throws Exception
    {
        if (!scheduledFlag)
        {
            return;
        }
        LOGGER.info("会议提醒推送定时任务开始执行");
        Map<String, Object> map = new HashMap<String, Object>();
        Calendar nowTime = Calendar.getInstance();
        map.put("stime", nowTime.getTime());
        nowTime.add(Calendar.MINUTE, 2);
        map.put("etime", nowTime.getTime());

        List<MtMeeting> meetingList = mtMeetingMapper.getMeetingCall(map);
        for (MtMeeting info : meetingList)
        {
            map.clear();
            map.put("meetingId", info.getId());
            List<MtParticipants> innerList = mtParticipantsMapper.getNoSendInnerByMeetingId(map);
            // bjyh 无外部 删除
            // List<MtParticipants> outerList = mtParticipantsMapper.getNoSendOuterByMeetingId(map);
            List<String> iList = new ArrayList<String>();
            List<String> sList = new ArrayList<String>();
            for (MtParticipants mpInfo : innerList)
            {
                if (PARTICIPANT_TYPE_INNER.equals(mpInfo.getPersonType()))
                    iList.add(mpInfo.getPersonId());
                if (PARTICIPANT_TYPE_SERVICE.equals(mpInfo.getPersonType()))
                    sList.add(mpInfo.getPersonId());
            }
            pushMeetingCall(info, iList, sList);
            // for (MtParticipants mpInfo : outerList)
            // {
            // sendMeetingCallSms(info, mpInfo.getPersonId());
            // }
        }
    }

    @Async
    public void pushMeetingCall(MtMeeting mtMeetingInfo, List<String> iList, List<String> sList)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("您的");
        sb.append(mtMeetingInfo.getName());
        sb.append("会议");
        sb.append("\n时间：");
        sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(mtMeetingInfo.getBeginTime()));
        sb.append("-");
        sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(mtMeetingInfo.getEndTime()));
        sb.append("\n地点：");
        sb.append(mtMeetingInfo.getAddress());
        sb.append("\n马上就要开始,请准时参会。");
        thirdPartAccessService.pushReceiveEvent(mtMeetingInfo.getEcid(), StringUtils.join(iList, ","), "您有会议马上开始",
                sb.toString(), mtMeetingInfo.getId(), null);
        if (sList != null && sList.size() > 0)
            thirdPartAccessService.pushReceiveEventForService(mtMeetingInfo.getEcid(), StringUtils.join(sList, ","),
                    "您有会议马上开始", sb.toString(), mtMeetingInfo.getId(), null);

        // EventParam param = mrReservedServiceV20.getEventParamByReservedId(mtMeetingInfo.getReservedId());
        // thirdPartAccessService.pushReceiveEvent(mtMeetingInfo.getEcid(), StringUtils.join(iList, ","), "会议提醒",
        // "您有会议马上开始", mtMeetingInfo.getId(), param);
        // if (sList != null && sList.size() > 0)
        // thirdPartAccessService.pushReceiveEventForService(mtMeetingInfo.getEcid(), StringUtils.join(sList, ","),
        // "会议提醒", "您有会议马上开始", mtMeetingInfo.getId(), param);
    }

    @Async
    public void sendMeetingCallSms(MtMeeting mtMeetingInfo, String phone)
    {
        // 短信发送
        if (StringUtil.isNotEmpty(phone))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("您的");
            sb.append(mtMeetingInfo.getName());
            sb.append("会议");
            sb.append(",时间：");
            sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(mtMeetingInfo.getBeginTime()));
            sb.append("-");
            sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(mtMeetingInfo.getEndTime()));
            sb.append(",地点：");
            sb.append(mtMeetingInfo.getAddress());
            sb.append(",马上就要开始,请准时参会。");
            thirdPartAccessService.sendSms(phone, sb.toString());
        }
    }

    /**
     * 会议消息推送
     * 
     * @throws Exception
     */
    @Scheduled(cron = "10 0/2 * * * ?")
    public void schedulePushStatus() throws Exception
    {
        if (!scheduledFlag)
        {
            return;
        }
        LOGGER.info("会议发布推送定时任务开始执行");
        List<MtMeeting> meetingList = mtMeetingMapper.selectAll();
        for (MtMeeting info : meetingList)
        {
            if (STAUTS_20.equals(info.getStatus()))
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("meetingId", info.getId());
                map.put("userId", info.getSponsor());
                MtParticipants mp = mtParticipantsMapper.getParticipant(map);
                if (mp == null)
                {
                    return;
                }
                String username = mp.getPersonName();
                if (StringUtil.isNotEmpty(username))
                {
                    map.put("noticeStatus", "0");
                    List<MtParticipants> innerList = mtParticipantsMapper.getNoSendInnerByMeetingId(map);
                    // List<MtParticipants> outerList = mtParticipantsMapper.getNoSendOuterByMeetingId(map);
                    List<String> iList = new ArrayList<String>();
                    List<String> sList = new ArrayList<String>();
                    for (MtParticipants mpInfo : innerList)
                    {
                        if (PARTICIPANT_TYPE_INNER.equals(mpInfo.getPersonType()))
                            iList.add(mpInfo.getPersonId());
                        if (PARTICIPANT_TYPE_SERVICE.equals(mpInfo.getPersonType()))
                            sList.add(mpInfo.getPersonId());
                    }
                    if (iList != null && iList.size() > 0)
                        pushMsg(info, iList, sList, username);
                    // for (MtParticipants mpInfo : outerList)
                    // {
                    // sendSms(info, mpInfo.getPersonId(), username, mpInfo.getId());
                    // }
                }
            }
        }

    }

    @Scheduled(cron = "30 0/1 * * * ?")
    public void scheduleQrcode()
    {
        if (!scheduledFlag)
        {
            return;
        }
        LOGGER.info("初始化二维码定时任务开始执行");
        List<MtParticipants> list = mtParticipantsMapper.getNoQrcode();
        for (MtParticipants entity : list)
        {
            initQrcode(entity);
        }
    }

    @Transactional
    public void initQrcode(MtParticipants entity)
    {
        try
        {
            entity.setQrcode(getQrcode(entity.getId()));
            mtParticipantsMapper.updateByPrimaryKeySelective(entity);
        }
        catch (Exception e)
        {
            LOGGER.error("二维码定时任务处理失败：{}", e);
        }
    }

    @Async
    @Transactional
    public void pushMsg(MtMeeting mtMeetingInfo, List<String> idList, List<String> sIdList, String username)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(username);
        sb.append("召开");
        sb.append(mtMeetingInfo.getName());
        sb.append("。");
        sb.append("\n时间：");
        sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(mtMeetingInfo.getBeginTime()));
        sb.append("-");
        sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(mtMeetingInfo.getEndTime()));
        sb.append("\n地点：");
        sb.append(mtMeetingInfo.getAddress());
        if (thirdPartAccessService.pushReceiveEvent(mtMeetingInfo.getEcid(), StringUtils.join(idList, ","), "您有新的会议",
                sb.toString(), mtMeetingInfo.getId(), null))
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("meetingId", mtMeetingInfo.getId());
            map.put("personType", PARTICIPANT_TYPE_INNER);
            // map.put("userId", loginId);
            mtParticipantsMapper.updateStatusToSend(map);
            map.put("personType", PARTICIPANT_TYPE_SERVICE);
            // map.put("userId", loginId);
            mtParticipantsMapper.updateStatusToSend(map);
        }
        // EventParam param = mrReservedServiceV20.getEventParamByReservedId(mtMeetingInfo.getReservedId());
        // if (thirdPartAccessService.pushReceiveEvent(mtMeetingInfo.getEcid(), StringUtils.join(idList, ","), "新的会议",
        // "",
        // mtMeetingInfo.getId(), param))
        // {
        // Map<String, Object> map = new HashMap<String, Object>();
        // map.put("meetingId", mtMeetingInfo.getId());
        // map.put("personType", PARTICIPANT_TYPE_INNER);
        // mtParticipantsMapper.updateStatusToSend(map);
        // }
        // if (thirdPartAccessService.pushReceiveEvent(mtMeetingInfo.getEcid(), StringUtils.join(sIdList, ","), "新的会议",
        // "",
        // mtMeetingInfo.getId(), param))
        // {
        // Map<String, Object> map = new HashMap<String, Object>();
        // map.put("meetingId", mtMeetingInfo.getId());
        // map.put("personType", PARTICIPANT_TYPE_SERVICE);
        // mtParticipantsMapper.updateStatusToSend(map);
        // }
    }

    @Async
    @Transactional
    public void sendSms(MtMeeting mtMeetingInfo, String phone, String username, String id)
    {
        // 短信发送
        if (StringUtil.isNotEmpty(phone))
        {
            // 访问网址
            String url = webUrl + "/EX/" + id;

            String content = "【会议通知】" + username + "召开了" + mtMeetingInfo.getName() + "会议，时间地点详见电子票：" + url;
            if (thirdPartAccessService.sendSms(phone, content))
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("meetingId", mtMeetingInfo.getId());
                map.put("userId", phone);
                mtParticipantsMapper.updateStatusToSend(map);
            }
        }
    }

    @ServiceMethod(method = "mapps.meeting.meeting.detail", group = "room", groupTitle = "API", version = "1.1", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryMeetingDetailResponseV11 getMeetingDetailV11(QueryMeetingDetailRequest req)
    {
        QueryMeetingDetailResponseV11 res = new QueryMeetingDetailResponseV11();
        try
        {
            LOGGER.info("查询会议室详细信息接口(mapps.meeting.meeting.detail)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("meetingId", req.getMeetingId());
            map.put("userId", SessionContext.getUserId());
            map.put("ecid", SessionContext.getEcId());
            map.put("personId", SessionContext.getUserId());
            List<MtParticipants> mpList = mtParticipantsMapper.getByMeetingId(map);
            if (mpList == null || mpList.size() == 0)
            {
                res.fail("hasDelete");
                LOGGER.info("未查询到相关参会信息,meetingid:{},userId:{}", req.getMeetingId(), SessionContext.getUserId());
                return res;
            }
            if (req.isRefreshFlag())
            {
                // 基本信息+备注
                ClientMeetingInfo cmInfo = mtMeetingMapper.getMeetingInfo(map);
                // 获取召开人信息
                GetUsersRequest guReq = new GetUsersRequest();
                guReq.setLoginId(cmInfo.getSponsor());
                MyUser muInfo = thirdPartAccessService.getUserInfo(SessionContext.getOrgId(), cmInfo.getSponsor());
                if (muInfo != null)
                {
                    cmInfo.setPerson(muInfo.getUserName());
                    cmInfo.setTel(muInfo.getPhoneNum());
                }
                res.setMeeting(cmInfo);
                res.setRemarksList(mtRemarksMapper.getByMeetingId(map));
                // 根据meetingId和personId查询参会者id
                String reCode = queryQrcode(req.getMeetingId());
                if (ErrorCode.CODE_100001.equals(reCode))
                {
                    ErrorCode.fail(res, ErrorCode.CODE_100001);
                    LOGGER.info("未查询到相关参会信息,meetingid:{},userId:{}", req.getMeetingId(), SessionContext.getUserId());
                    return res;
                }
                res.setQrcode(reCode);
                // 议程信息
                List<MtAgenda> agendaList = mtAgendaMapper.getByMeetingId(map);
                res.setAgendaList(agendaList);
                // 会议资料
                res.setAttachmentList(mtAttachmentMapper.getByMeetingId(map));
            }
            // 签到信息
            map.put("createUserId", res.getMeeting().getSponsor());
            List<MtSigninSequ> list = mtSigninSequMapper.getByMeetingId(map);
            for (MtSigninSequ sequ : list)
            {
                int num = sequ.getSequ() == null ? 0 : sequ.getSequ().intValue();
                sequ.setSequStr(ConvertUtil.numberToChinese(num));
            }
            if (req.getTimestamp() == 0l)
            {
                req.setTimestamp(new Date().getTime());
            }
            res.setSigninSequList(list);

            // bjyh start
            // 查询参会人信息
            Map<String, Object> pMap = new HashMap<String, Object>();
            pMap.put("meetingId", req.getMeetingId());
            pMap.put("createUserId", res.getMeeting().getSponsor());
            res.setSigninPersonList(mtParticipantsMapper.getByMeetingId(pMap));
            if (list != null && list.size() > 0)
            {
                map.put("time", new Date());

                // map.put("sequId", list.get(0).getId());
                // map.put("sort", "personId asc");
                // map.put("allFlag", "-1");
                // // 签到信息
                // List<MtSigninRecord> signinPersonList = mtSigninRecordMapper.getBySequId(map);
                // res.setSigninPersonList(signinPersonList);
                // map.remove("sequId");
                // map.remove("sort");
                // map.remove("allFlag");
                List<MtSigninRecord> signinRecordList = mtSigninRecordMapper.getBySequId(map);
                List<String> loginIdList = new ArrayList<String>();
                for (MtSigninRecord msrInfo : signinRecordList)
                {
                    if (PARTICIPANT_TYPE_INNER.equals(msrInfo.getPersonType()))
                    {
                        loginIdList.add(msrInfo.getPersonId());
                    }
                }
                if (loginIdList.size() > 0)
                {
                    List<MyUser> muList = thirdPartAccessService.getUserInfos(SessionContext.getOrgId(),
                            StringUtils.join(loginIdList, ","));
                    for (MtSigninRecord record : signinRecordList)
                    {
                        if (PARTICIPANT_TYPE_INNER.equals(record.getPersonType()))
                        {
                            for (MyUser user : muList)
                            {
                                if (record.getPersonId().equals(user.getLoginId()))
                                {
                                    record.setDeptName(user.getDeptName());
                                    record.setPhone(user.getPhoneNum());
                                }
                            }
                        }
                        else if (PARTICIPANT_TYPE_OUTER.equals(record.getPersonType()))
                        {
                            record.setPhone(record.getPersonId());
                        }
                    }
                }
                res.setSigninRecordList(signinRecordList);
            }
            else
            {
                // res.setSigninPersonList(new ArrayList<MtSigninRecord>());
                res.setSigninRecordList(new ArrayList<MtSigninRecord>());
            }
            // bjyh end
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

    @ServiceMethod(method = "mapps.meeting.meeting.detailforservice", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryMeetingDetailResponseV11 getMeetingDetailForService(QueryMeetingDetailRequest req)
    {
        QueryMeetingDetailResponseV11 res = new QueryMeetingDetailResponseV11();
        try
        {
            LOGGER.info(
                    "服务人员查询会议室详细信息接口(mapps.meeting.meeting.detailforservice)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("meetingId", req.getMeetingId());
            map.put("userId", SessionContext.getUserId());
            map.put("ecid", SessionContext.getEcId());
            map.put("personId", SessionContext.getUserId());
            List<MtParticipants> mpList = mtParticipantsMapper.getByMeetingId(map);
            /*
             * if (mpList == null || mpList.size() == 0) { res.fail("hasDelete");
             * LOGGER.info("未查询到相关参会信息,meetingid:{},userId:{}", req.getMeetingId(), SessionContext.getUserId()); return
             * res; }
             */
            if (req.isRefreshFlag())
            {
                // 基本信息+备注
                ClientMeetingInfo cmInfo = mtMeetingMapper.getMeetingInfo(map);
                // 获取召开人信息
                GetUsersRequest guReq = new GetUsersRequest();
                guReq.setLoginId(cmInfo.getSponsor());
                MyUser muInfo = thirdPartAccessService.getUserInfo(SessionContext.getOrgId(), cmInfo.getSponsor());
                if (muInfo != null)
                {
                    cmInfo.setPerson(muInfo.getUserName());
                    cmInfo.setTel(muInfo.getPhoneNum());
                }
                res.setMeeting(cmInfo);
                res.setRemarksList(mtRemarksMapper.getByMeetingId(map));
                // 根据meetingId和personId查询参会者id
                String reCode = queryQrcode(req.getMeetingId());
                /*
                 * if (ErrorCode.CODE_100001.equals(reCode)) { ErrorCode.fail(res, ErrorCode.CODE_100001);
                 * LOGGER.info("未查询到相关参会信息,meetingid:{},userId:{}", req.getMeetingId(), SessionContext.getUserId());
                 * return res; }
                 */
                res.setQrcode(reCode);
                // 议程信息
                List<MtAgenda> agendaList = mtAgendaMapper.getByMeetingId(map);
                res.setAgendaList(agendaList);
                // 会议资料
                res.setAttachmentList(mtAttachmentMapper.getByMeetingId(map));
            }
            // 签到信息
            List<MtSigninSequ> list = mtSigninSequMapper.getByMeetingId(map);
            for (MtSigninSequ sequ : list)
            {
                int num = sequ.getSequ() == null ? 0 : sequ.getSequ().intValue();
                sequ.setSequStr(ConvertUtil.numberToChinese(num));
            }
            if (req.getTimestamp() == 0l)
            {
                req.setTimestamp(new Date().getTime());
            }
            res.setSigninSequList(list);

            // bjyh start
            // 查询参会人信息
            Map<String, Object> pMap = new HashMap<String, Object>();
            pMap.put("meetingId", req.getMeetingId());
            res.setSigninPersonList(mtParticipantsMapper.getByMeetingId(pMap));
            if (list != null && list.size() > 0)
            {
                map.put("time", new Date());

                // map.put("sequId", list.get(0).getId());
                // map.put("sort", "personId asc");
                // map.put("allFlag", "-1");
                // // 签到信息
                // List<MtSigninRecord> signinPersonList = mtSigninRecordMapper.getBySequId(map);
                // res.setSigninPersonList(signinPersonList);
                // map.remove("sequId");
                // map.remove("sort");
                // map.remove("allFlag");
                List<MtSigninRecord> signinRecordList = mtSigninRecordMapper.getBySequId(map);
                List<String> loginIdList = new ArrayList<String>();
                for (MtSigninRecord msrInfo : signinRecordList)
                {
                    if (PARTICIPANT_TYPE_INNER.equals(msrInfo.getPersonType()))
                    {
                        loginIdList.add(msrInfo.getPersonId());
                    }
                }
                if (loginIdList.size() > 0)
                {
                    List<MyUser> muList = thirdPartAccessService.getUserInfos(SessionContext.getOrgId(),
                            StringUtils.join(loginIdList, ","));
                    for (MtSigninRecord record : signinRecordList)
                    {
                        if (PARTICIPANT_TYPE_INNER.equals(record.getPersonType()))
                        {
                            for (MyUser user : muList)
                            {
                                if (record.getPersonId().equals(user.getLoginId()))
                                {
                                    record.setDeptName(user.getDeptName());
                                    record.setPhone(user.getPhoneNum());
                                }
                            }
                        }
                        else if (PARTICIPANT_TYPE_OUTER.equals(record.getPersonType()))
                        {
                            record.setPhone(record.getPersonId());
                        }
                    }
                }
                res.setSigninRecordList(signinRecordList);
            }
            else
            {
                // res.setSigninPersonList(new ArrayList<MtSigninRecord>());
                res.setSigninRecordList(new ArrayList<MtSigninRecord>());
            }
            // bjyh end
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

    @ServiceMethod(method = "mapps.meeting.meeting.outerdetail", group = "room", groupTitle = "API", version = "1.1", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
    public QueryMeetingDetailResponseV11 getOuterDetailV11(QueryOuterDetailRequest req)
    {
        QueryMeetingDetailResponseV11 res = new QueryMeetingDetailResponseV11();
        try
        {
            LOGGER.info("外部人员查询会议室详细信息接口(mapps.meeting.meeting.outerdetail)入口,请求参数==" + LogUtil.getObjectInfo(req));

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", req.getParticipantsId());
            MtParticipants mt = mtParticipantsMapper.getParById(map);
            if (mt == null)
            {
                ErrorCode.fail(res, ErrorCode.CODE_100001);
                return res;
            }
            map.clear();
            map.put("meetingId", mt.getMeetingId());
            // 基本信息+备注
            ClientMeetingInfo cmInfo = mtMeetingMapper.getMeetingInfo(map);
            // 获取召开人信息
            // GetUsersRequest guReq = new GetUsersRequest();
            // guReq.setLoginId(cmInfo.getSponsor());
            // GetUsersResponse guRes = thirdPartAccessService.getUsers(guReq);
            // if (guRes != null && guRes.getUserInfos().size() > 0)
            // {
            // MyUser mu = guRes.getUserInfos().get(0);
            // }
            cmInfo.setPerson("");
            cmInfo.setTel("");
            res.setMeeting(cmInfo);
            res.setRemarksList(mtRemarksMapper.getByMeetingId(map));
            String code = "-1".equals(mt.getQrcode()) ? getQrcode(mt.getId()) : mt.getQrcode();
            res.setQrcode("-1".equals(code) ? code : webRoot + code);
            // 议程信息
            List<MtAgenda> agendaList = mtAgendaMapper.getByMeetingId(map);
            res.setAgendaList(agendaList);
            // 会议资料
            res.setAttachmentList(mtAttachmentMapper.getByMeetingId(map));
            // 签到信息
            List<MtSigninSequ> list = mtSigninSequMapper.getByMeetingId(map);
            res.setSigninSequList(list);
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

    @ServiceMethod(method = "mapps.meeting.meeting.querysignstatus", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QuerySignStatusResponse querySignStatus(QuerySignStatusRequest req)
    {
        QuerySignStatusResponse res = new QuerySignStatusResponse();
        try
        {
            LOGGER.info("轮询会议室签到情况接口(mapps.meeting.meeting.querysignstatus)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("meetingId", req.getMeetingId());
            map.put("userId", SessionContext.getUserId());
            // 获取签到信息
            MtSignStatus mtSignStatus = mtMeetingMapper.getMeetingSignStatus(map);

            res.setSignnum(Integer.parseInt(mtSignStatus.getSignnum()));
            res.setTotalnum(Integer.parseInt(mtSignStatus.getTotalnum()));
            LOGGER.info("轮询会议室签到情况成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("轮询会议室签到情况异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }
}
