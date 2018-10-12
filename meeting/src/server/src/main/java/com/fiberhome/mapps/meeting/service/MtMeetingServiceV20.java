package com.fiberhome.mapps.meeting.service;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.meeting.dao.MdParticipantsMapper;
import com.fiberhome.mapps.meeting.dao.MtAgendaMapper;
import com.fiberhome.mapps.meeting.dao.MtAttachmentMapper;
import com.fiberhome.mapps.meeting.dao.MtMeetingMapper;
import com.fiberhome.mapps.meeting.dao.MtParticipantsMapper;
import com.fiberhome.mapps.meeting.dao.MtRemarksMapper;
import com.fiberhome.mapps.meeting.dao.MtSigninRecordMapper;
import com.fiberhome.mapps.meeting.dao.MtSigninSequMapper;
import com.fiberhome.mapps.meeting.dao.MtSigninServMapper;
import com.fiberhome.mapps.meeting.entity.ClientMeetingInfo;
import com.fiberhome.mapps.meeting.entity.MdParticipants;
import com.fiberhome.mapps.meeting.entity.MtAgenda;
import com.fiberhome.mapps.meeting.entity.MtAttachment;
import com.fiberhome.mapps.meeting.entity.MtMeeting;
import com.fiberhome.mapps.meeting.entity.MtParticipants;
import com.fiberhome.mapps.meeting.entity.MtRemarks;
import com.fiberhome.mapps.meeting.entity.MtSigninRecord;
import com.fiberhome.mapps.meeting.entity.ClientMtSigninSequInfo;
import com.fiberhome.mapps.meeting.entity.MtSigninServ;
import com.fiberhome.mapps.meeting.request.AddMeetingRequest;
import com.fiberhome.mapps.meeting.request.CreateImGroupRequest;
import com.fiberhome.mapps.meeting.request.EditMeetingRequest;
import com.fiberhome.mapps.meeting.request.GetUsersRequest;
import com.fiberhome.mapps.meeting.request.JoinImGroupRequest;
import com.fiberhome.mapps.meeting.request.QueryMeetingRequest;
import com.fiberhome.mapps.meeting.response.AddMeetingResponse;
import com.fiberhome.mapps.meeting.response.CreateImGroupResponse;
import com.fiberhome.mapps.meeting.response.GetUsersResponse;
import com.fiberhome.mapps.meeting.response.QueryMeetingResponse;
import com.fiberhome.mapps.meeting.utils.CreateImGroupException;
import com.fiberhome.mapps.meeting.utils.DeleteMembersFromImGroupException;
import com.fiberhome.mapps.meeting.utils.ErrorCode;
import com.fiberhome.mapps.meeting.utils.JoinImGroupException;
import com.fiberhome.mapps.meeting.utils.JsonUtil;
import com.fiberhome.mapps.meeting.utils.LogUtil;
import com.fiberhome.mapps.utils.IDGen;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean(version = "1.0")
public class MtMeetingServiceV20 {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	MtMeetingService mtMeetingService;
	@Autowired
	MtMeetingMapper mtMeetingMapper;
	@Autowired
	MdParticipantsMapper mdParticipantsMapper;
	@Autowired
	MtAgendaMapper mtAgendaMapper;
	@Autowired
	MtAttachmentMapper mtAttachmentMapper;
	@Autowired
	MtSigninSequMapper mtSigninSequMapper;
	@Autowired
	MtSigninServMapper mtSigninServMapper;
	@Autowired
	MtSigninRecordMapper mtSigninRecordMapper;
	@Autowired
	MtRemarksMapper mtRemarksMapper;
	@Autowired
	MtParticipantsMapper mtParticipantsMapper;
	@Autowired
	ThirdPartAccessService thirdPartAccessService;
	@Autowired
	ThirdPartAccessMosService thirdPartAccessMosService;

	/** 开启 1 */
	public static final String FLAG_OPEN = "1";
	/** 关闭 0 */
	public static final String FLAG_CLOSE = "0";
	/** 草稿：10 */
	public static final String STAUTS_10 = "10";
	/** 未进行：20 */
	public static final String STAUTS_20 = "20";
	/** 进行中：30 */
	public static final String STAUTS_30 = "30";
	/** 已取消：40 */
	public static final String STAUTS_40 = "40";
	/** 已结束：50 */
	public static final String STAUTS_50 = "50";

	public static final String PARTICIPANT_TYPE_INNER = "inner";
	public static final String PARTICIPANT_TYPE_OUTER = "outer";
	public static final String PARTICIPANT_TYPE_SERVICE = "service";
	public static final String PARTICIPANT_TYPE_CREATE = "create";
	
	@Value("${flywaydb.locations}")
	String databaseType;

	@ServiceMethod(method = "mapps.meeting.meeting.clientquery", group = "meeting", groupTitle = "API", version = "2.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	public QueryMeetingResponse getMeetingFromClient(QueryMeetingRequest req) {
		QueryMeetingResponse res = new QueryMeetingResponse();
		try {
			LOGGER.info("分页查询会议接口(mapps.meeting.meeting.clientquery)入口,请求参数==" + LogUtil.getObjectInfo(req));
			if (req.getTimestamp() == 0l) {
				req.setTimestamp(new Date().getTime());
			}
			Map<String, Object> map = initQuery(req);
			map.put("databaseType", databaseType);
			PageHelper.startPage(req.getOffset(), req.getLimit());
			List<ClientMeetingInfo> list = mtMeetingMapper.getMeetingV20(map);
			PageInfo<ClientMeetingInfo> page = new PageInfo<ClientMeetingInfo>(list);
			res.setTotal(page.getTotal());
			if (list == null) {
				list = new ArrayList<ClientMeetingInfo>();
			}
			res.setMeetingList(list);
			res.setTimestamp(req.getTimestamp());
			if (page.isIsLastPage()) {
				res.setEndflag(1);
			} else {
				res.setEndflag(0);
			}
			if ("1".equals(req.getLoginUserFlag())) {
				MyUser myUser = thirdPartAccessService.getUserInfo(SessionContext.getOrgId(),
						SessionContext.getUserId());
				res.setMyUser(myUser);
			}
			res.success();
			LOGGER.info("分页查询会议成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("分页查询会议信息异常：{}", e);
			ErrorCode.fail(res, ErrorCode.CODE_100001);
		}
		return res;
	}

	public Map<String, Object> initQuery(QueryMeetingRequest req) throws Exception {
		String meetingName = req.getMeetingName();
		String selectDate = req.getSelectDate();
		Date time = new Date(req.getTimestamp());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("time", time);
		map.put("ecid", SessionContext.getEcId());
		map.put("userId", SessionContext.getUserId());

		if (StringUtil.isNotEmpty(meetingName)) {
			map.put("likeParam", "%" + meetingName + "%");
		}
		if (StringUtil.isNotEmpty(selectDate)) {
			map.put("selectDate", selectDate);
		}
		return map;
	}

	@ServiceMethod(method = "mapps.meeting.meeting.client.add", group = "meeting", groupTitle = "API", version = "2.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	@Transactional
	public AddMeetingResponse addMeetingFromClient(AddMeetingRequest req) {
		LOGGER.info("新增会议接口(mapps.meeting.meeting.client.add)入口,请求参数==" + req.getMeetingJson());
		AddMeetingResponse res = new AddMeetingResponse();
		try {
			AddMeetingRequest addInfo = (AddMeetingRequest) JsonUtil.jsonToObject(req.getMeetingJson(),
					AddMeetingRequest.class);
			if (!checkData(addInfo, res)) {
				return res;
			}
			String meetingId = "";
			MtMeeting mtMeetingInfo = addInfo.getMeeting();
			mtMeetingInfo.setSponsor(SessionContext.getUserId());
			mtMeetingInfo.setCreateTime(new Date());
			mtMeetingInfo.setStatus(STAUTS_20);
			mtMeetingInfo.setReleaseTime(new Date());

			Calendar cal = Calendar.getInstance();
			cal.setTime(mtMeetingInfo.getBeginTime());
			if ("1".equals(mtMeetingInfo.getNoticeType()))// 分钟
			{
				cal.add(Calendar.MINUTE, -mtMeetingInfo.getNoticeSet());
				mtMeetingInfo.setNoticeTime(cal.getTime());
			} else if ("2".equals(mtMeetingInfo.getNoticeType()))// 小时
			{
				cal.add(Calendar.HOUR, -mtMeetingInfo.getNoticeSet());
				mtMeetingInfo.setNoticeTime(cal.getTime());
			}
			// 会议信息 MtMeeting
			meetingId = IDGen.uuid();
			mtMeetingInfo.setId(meetingId);
			insertMeetingInfo(meetingId, addInfo, "add");
			mtMeetingMapper.insertSelective(mtMeetingInfo);
		} catch (CreateImGroupException ce) {
			LOGGER.error("新增会议室信息异常：{}", ce);
			ErrorCode.fail(res, ErrorCode.CODE_300018);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		} catch (JoinImGroupException je) {
			LOGGER.error("新增会议室信息异常：{}", je);
			ErrorCode.fail(res, ErrorCode.CODE_300018);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		} catch (Exception e) {
			LOGGER.error("新增会议室信息异常：{}", e);
			ErrorCode.fail(res, ErrorCode.CODE_100001);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return res;
	}

	public boolean checkData(AddMeetingRequest req, AddMeetingResponse res) {
		MtMeeting mtMeetingInfo = req.getMeeting();
		if (mtMeetingInfo == null) {
			res.setMessage("保存失败,会议主体信息为空");
			ErrorCode.fail(res, ErrorCode.CODE_100001);
			LOGGER.error("会议主体信息为空");
			return false;
		}
		if (StringUtil.isEmpty(mtMeetingInfo.getName()) || StringUtil.isEmpty(mtMeetingInfo.getAddress())
				|| mtMeetingInfo.getBeginTime() == null || mtMeetingInfo.getEndTime() == null) {
			res.setMessage("保存失败,会议主体信息不全");
			ErrorCode.fail(res, ErrorCode.CODE_100001);
			LOGGER.error("会议主体信息不全");
			return false;
		}
		List<MdParticipants> inParticipantsList = req.getInParticipantsList();
		// List<MdParticipants> outParticipantsList =
		// req.getOutParticipantsList();
		if (inParticipantsList == null || inParticipantsList.size() == 0)
		// + (outParticipantsList == null ? 0 : outParticipantsList.size())) ==
		// 0)
		{
			res.setMessage("保存失败,参会人员为空");
			ErrorCode.fail(res, ErrorCode.CODE_100001);
			LOGGER.error("参会人员为空");
			return false;
		}
		for (MdParticipants info : inParticipantsList) {
			info.setType(PARTICIPANT_TYPE_INNER);
			if (StringUtil.isEmpty(info.getEntityId()) || StringUtil.isEmpty(info.getEntityName())
					|| StringUtil.isEmpty(info.getEntityType())) {
				res.setMessage("保存失败,内部参会人员信息不全");
				ErrorCode.fail(res, ErrorCode.CODE_100001);
				LOGGER.error("内部参会人员信息不全");
				return false;
			}
			if (!"user".equals(info.getEntityType()) && !"dept".equals(info.getEntityType())) {
				res.setMessage("保存失败,参会人员类型错误");
				ErrorCode.fail(res, ErrorCode.CODE_100001);
				LOGGER.error("参会人员类型错误");
				return false;
			}
		}
		// 服务人员
		List<MdParticipants> serviceParticipantsList = req.getServiceParticipantsList();
		if (serviceParticipantsList != null && inParticipantsList.size() > 0) {
			for (MdParticipants info : serviceParticipantsList) {
				info.setType(PARTICIPANT_TYPE_SERVICE);
				if (StringUtil.isEmpty(info.getEntityId()) || StringUtil.isEmpty(info.getEntityName())
						|| StringUtil.isEmpty(info.getEntityType())) {
					res.setMessage("保存失败,服务人员信息不全");
					ErrorCode.fail(res, ErrorCode.CODE_100001);
					LOGGER.error("服务人员信息不全");
					return false;
				}
				if (!"user".equals(info.getEntityType()) && !"dept".equals(info.getEntityType())) {
					res.setMessage("保存失败,服务人员类型错误");
					ErrorCode.fail(res, ErrorCode.CODE_100001);
					LOGGER.error("服务人员类型错误");
					return false;
				}
			}
		}
		// List<String> tempList = new ArrayList<String>();
		// for (MdParticipants info : outParticipantsList)
		// {
		// info.setType(PARTICIPANT_TYPE_OUTER);
		// if (StringUtil.isEmpty(info.getEntityId()) ||
		// StringUtil.isEmpty(info.getEntityName())
		// || StringUtil.isEmpty(info.getEntityType()))
		// {
		// res.setMessage("保存失败,外部参会人员信息不全");
		// ErrorCode.fail(res, ErrorCode.CODE_100001);
		// LOGGER.error("外部参会人员信息不全");
		// return false;
		// }
		// // 电话号码唯一校验
		// if (!tempList.contains(info.getEntityId()))
		// {
		// tempList.add(info.getEntityId());
		// }
		// }
		// if (outParticipantsList.size() != tempList.size())
		// {
		// res.setMessage("保存失败,外部参会人员手机号码重复");
		// ErrorCode.fail(res, ErrorCode.CODE_100001);
		// LOGGER.error("外部参会人员手机号码重复");
		// return false;
		// }
		if (FLAG_OPEN.equals(req.getAgendaFlag())) {
			List<MtAgenda> agendaList = req.getAgendaList();
			for (MtAgenda info : agendaList) {
				if (StringUtil.isEmpty(info.getRemarks()) || info.getBeginTime() == null || info.getEndTime() == null) {
					res.setMessage("保存失败,议程信息不全");
					ErrorCode.fail(res, ErrorCode.CODE_100001);
					LOGGER.error("议程信息不全");
					return false;
				}

				if (info.getBeginTime().before(mtMeetingInfo.getBeginTime())
						|| info.getEndTime().after(mtMeetingInfo.getEndTime())) {
					res.setMessage("保存失败，议程超出会议时间");
					ErrorCode.fail(res, ErrorCode.CODE_100001);
					LOGGER.error("议程超出会议时间");
					return false;
				}
			}
		}
		if (FLAG_OPEN.equals(req.getAttachmentFlag())) {
			List<MtAttachment> attachmentList = req.getAttachmentList();
			for (MtAttachment info : attachmentList) {
				if (StringUtil.isEmpty(info.getFilePath()) || StringUtil.isEmpty(info.getFileName())
						|| StringUtil.isEmpty(info.getSize().toString())) {
					res.setMessage("保存失败,附件信息不全");
					ErrorCode.fail(res, ErrorCode.CODE_100001);
					LOGGER.error("附件信息不全");
					return false;
				}
			}
		}
		if (FLAG_OPEN.equals(req.getSigninFlag())) {

			List<ClientMtSigninSequInfo> signinSequList = req.getSigninSequList();
			for (ClientMtSigninSequInfo info : signinSequList) {
				if (StringUtil.isEmpty(info.getRemarks()) || StringUtil.isEmpty(info.getSequ().toString())) {
					res.setMessage("保存失败,签到信息不全");
					ErrorCode.fail(res, ErrorCode.CODE_100001);
					LOGGER.error("签到信息不全");
					return false;
				}
			}
		}
		if (FLAG_OPEN.equals(req.getRemarksFlag())) {
			List<MtRemarks> remarksList = req.getRemarksList();
			for (MtRemarks info : remarksList) {
				if (StringUtil.isEmpty(info.getRemarks())) {
					res.setMessage("保存失败,备注信息不全");
					ErrorCode.fail(res, ErrorCode.CODE_100001);
					LOGGER.error("备注信息不全");
					return false;
				}
			}
		}
		return true;
	}

	public void insertMeetingInfo(String meetingId, AddMeetingRequest addInfo, String optFlag) throws Exception {
		MtMeeting mtMeetingInfo = addInfo.getMeeting();
		// 参会人员 内部、外部、服务 MdParticipants
		List<MtParticipants> mtParticipantsList = new ArrayList<MtParticipants>();
		List<MdParticipants> inParticipantsList = addInfo.getInParticipantsList();
		Map<String, MtParticipants> inMap = new HashMap<String, MtParticipants>();
		for (MdParticipants info : inParticipantsList) {
			info.setId(IDGen.uuid());
			info.setObjectId(meetingId);
			info.setObjectName(mtMeetingInfo.getName());
			mdParticipantsMapper.insertSelective(info);
			if ("user".equals(info.getEntityType())) {
				MtParticipants entity = new MtParticipants();
				entity.setId(IDGen.uuid());
				entity.setMeetingId(meetingId);
				entity.setPersonType(PARTICIPANT_TYPE_INNER);
				entity.setPersonId(info.getEntityId());
				entity.setPersonName(info.getEntityName());
				entity.setNoticeStatus(MtMeetingService.NOTICE_STATUS_0);
				inMap.put(entity.getPersonId(), entity);
			} else if ("dept".equals(info.getEntityType())) {
				GetUsersRequest guReq = new GetUsersRequest();
				guReq.setDepUuid(info.getEntityId());
				guReq.setDepScope("1");
				GetUsersResponse guRes = thirdPartAccessService.getUsersByDept(guReq);
				for (MyUser user : guRes.getUserInfos()) {
					MtParticipants entity = new MtParticipants();
					entity.setId(IDGen.uuid());
					entity.setMeetingId(meetingId);
					entity.setPersonType(PARTICIPANT_TYPE_INNER);
					entity.setPersonId(user.getUserUuid());
					entity.setPersonName(user.getUserName());
					// 0：未通知 1：已发送 2：已送达
					entity.setNoticeStatus(MtMeetingService.NOTICE_STATUS_0);
					inMap.put(entity.getPersonId(), entity);
				}
			}
		}
		// 生成MtParticipants数据
		for (MtParticipants entity : inMap.values()) {
			entity.setId(IDGen.shortId());
			entity.setQrcode("-1");
			mtParticipantsMapper.insertSelective(entity);
			mtParticipantsList.add(entity);
		}
		// 添加创建人信息
		MtParticipants mp = new MtParticipants();
		mp.setId(IDGen.uuid());
		mp.setMeetingId(meetingId);
		mp.setPersonType(PARTICIPANT_TYPE_CREATE);
		mp.setPersonId(SessionContext.getUserId());
		mp.setPersonName(SessionContext.getUserName());
		mp.setNoticeStatus(MtMeetingService.NOTICE_STATUS_0);
		inMap.put(SessionContext.getUserId(), mp);
		// 服务人员信息预处理
		Map<String, MtSigninServ> serviceMap = new HashMap<String, MtSigninServ>();
		List<MdParticipants> serviceParticipantsList = addInfo.getServiceParticipantsList();
		for (MdParticipants info : serviceParticipantsList) {
			info.setId(IDGen.uuid());
			info.setObjectId(meetingId);
			info.setObjectName(mtMeetingInfo.getName());
			mdParticipantsMapper.insertSelective(info);
			if ("user".equals(info.getEntityType())) {
				MtSigninServ entity = new MtSigninServ();
				entity.setId(IDGen.uuid());
				entity.setMeetingId(meetingId);
				entity.setUserId(info.getEntityId());
				entity.setUserName(info.getEntityName());
				serviceMap.put(info.getEntityId(), entity);
			} else if ("dept".equals(info.getEntityType())) {
				GetUsersRequest guReq = new GetUsersRequest();
				guReq.setDepUuid(info.getEntityId());
				guReq.setDepScope("1");
				GetUsersResponse guRes = thirdPartAccessService.getUsersByDept(guReq);
				for (MyUser user : guRes.getUserInfos()) {
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
		for (MtSigninServ entity : serviceMap.values()) {
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
		// List<MdParticipants> outParticipantsList =
		// addInfo.getOutParticipantsList();
		// for (MdParticipants info : outParticipantsList)
		// {
		// info.setId(IDGen.uuid());
		// info.setObjectId(meetingId);
		// info.setObjectName(mtMeetingInfo.getName());
		// mdParticipantsMapper.insertSelective(info);
		// MtParticipants entity = new MtParticipants();
		// entity.setId(IDGen.shortId());
		// entity.setMeetingId(meetingId);
		// entity.setPersonType(PARTICIPANT_TYPE_OUTER);
		// entity.setPersonId(info.getEntityId());
		// entity.setPersonName(info.getEntityName());
		// entity.setQrcode("-1");
		// // 0：未通知 1：已发送 2：已送达
		// entity.setNoticeStatus(MtMeetingService.NOTICE_STATUS_0);
		// mtParticipantsMapper.insertSelective(entity);
		// mtParticipantsList.add(entity);
		// }
		// mtParticipantsMapper.insertList(mtParticipantsList);
		// 会议议程 MtAgenda
		if (FLAG_OPEN.equals(addInfo.getAgendaFlag())) {
			List<MtAgenda> agendaList = addInfo.getAgendaList();
			for (MtAgenda info : agendaList) {
				info.setId(IDGen.uuid());
				info.setMeetingId(meetingId);
				mtAgendaMapper.insertSelective(info);
			}
		}
		// 附件资料 MtAttachment
		if (FLAG_OPEN.equals(addInfo.getAttachmentFlag())) {
			List<MtAttachment> attachmentList = addInfo.getAttachmentList();
			for (MtAttachment info : attachmentList) {
				info.setId(IDGen.uuid());
				info.setMeetingId(meetingId);
				mtAttachmentMapper.insertSelective(info);
			}
		}
		// 会议签到 MtSigninSequ
		if (FLAG_OPEN.equals(addInfo.getSigninFlag())) {

			// 签到信息
			long sequ = 1;
			List<ClientMtSigninSequInfo> signinSequList = addInfo.getSigninSequList();
			// List<MtSigninRecord> recordList = new
			// ArrayList<MtSigninRecord>();
			for (ClientMtSigninSequInfo info : signinSequList) {
				String sequId = IDGen.uuid();
				info.setId(sequId);
				info.setMeetingId(meetingId);
				info.setSequ(sequ);
				info.setQrcode(mtMeetingService.getQrcode("!sign20!" + meetingId + "::" + sequ));
				mtSigninSequMapper.insertSelective(info);
				for (MtParticipants entity : mtParticipantsList) {
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
		if (FLAG_OPEN.equals(addInfo.getRemarksFlag())) {
			long sequ = 0;
			List<MtRemarks> remarksList = addInfo.getRemarksList();
			for (MtRemarks info : remarksList) {
				info.setId(IDGen.uuid());
				info.setMeetingId(meetingId);
				info.setSequ(sequ);
				mtRemarksMapper.insertSelective(info);
				sequ++;
			}
		}
		// 根据flag操作im群组
		if (optFlag == "add") {
			// 创建群组 调用im创建群组接口
			createIMGroup(addInfo, mtMeetingInfo, inMap, serviceMap);
		} else if (optFlag == "edit") {
			// 修改群组 调用im创建群组接口
			editIMGroup((EditMeetingRequest) addInfo, inMap, serviceMap);
		}
	}

	@ServiceMethod(method = "mapps.meeting.meeting.client.edit", group = "meeting", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
	@Transactional
	public AddMeetingResponse editMeetingFromClient(EditMeetingRequest req) {
		LOGGER.info("编辑会议接口(mapps.meeting.meeting.edit)入口,请求参数==" + req.getMeetingJson());
		AddMeetingResponse res = new AddMeetingResponse();
		try {
			EditMeetingRequest addInfo = (EditMeetingRequest) JsonUtil.jsonToObject(req.getMeetingJson(),
					EditMeetingRequest.class);
			if (!checkData(addInfo, res)) {
				return res;
			}
			MtMeeting mtMeetingInfo = addInfo.getMeeting();
			MtMeeting realMeetingInfo = mtMeetingMapper.selectByPrimaryKey(mtMeetingInfo.getId());
			if (!STAUTS_20.equals(realMeetingInfo.getStatus())) {
				ErrorCode.fail(res, ErrorCode.CODE_400002);
				return res;
			}

			mtMeetingInfo.setSponsor(SessionContext.getUserId());
			mtMeetingInfo.setCreateTime(new Date());
			mtMeetingInfo.setStatus(STAUTS_20);
			mtMeetingInfo.setReleaseTime(new Date());
			mtMeetingInfo.setId(mtMeetingInfo.getId());

			Calendar cal = Calendar.getInstance();
			cal.setTime(mtMeetingInfo.getBeginTime());
			if ("1".equals(mtMeetingInfo.getNoticeType()))// 分钟
			{
				cal.add(Calendar.MINUTE, -mtMeetingInfo.getNoticeSet());
				mtMeetingInfo.setNoticeTime(cal.getTime());
			} else if ("2".equals(mtMeetingInfo.getNoticeType()))// 小时
			{
				cal.add(Calendar.HOUR, -mtMeetingInfo.getNoticeSet());
				mtMeetingInfo.setNoticeTime(cal.getTime());
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("meetingId", addInfo.getMeeting().getId());
			List<MtParticipants> oldList = mtParticipantsMapper.getByMeetingId(map);
			// 会议信息 MtMeeting
			clearMeetingInfo(mtMeetingInfo.getId());
			insertMeetingInfo(mtMeetingInfo.getId(), addInfo, "edit");
			mtMeetingMapper.updateByPrimaryKeySelective(mtMeetingInfo);
			// 编辑推送处理
			pushForEdit(addInfo,oldList);
		} catch (DeleteMembersFromImGroupException dmfige) {
			LOGGER.error("编辑会议室信息异常：{}", dmfige);
			ErrorCode.fail(res, ErrorCode.CODE_300018);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		// catch (JoinImGroupException je)
		// {
		// LOGGER.error("编辑会议室信息异常：{}", je);
		// ErrorCode.fail(res, ErrorCode.CODE_300018);
		// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		// }
		catch (Exception e) {
			LOGGER.error("编辑会议室信息异常：{}", e);
			ErrorCode.fail(res, ErrorCode.CODE_100001);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return res;
	}

	public void clearMeetingInfo(String meetingId) {
		Map<String, Object> meetingIdMap = new HashMap<String, Object>();
		meetingIdMap.put("meetingId", meetingId);
		mdParticipantsMapper.deleteByMeetingId(meetingIdMap);
		mtParticipantsMapper.deleteByMeetingId(meetingIdMap);
		mtSigninServMapper.deleteByMeetingId(meetingIdMap);
		mtAgendaMapper.deleteByMeetingId(meetingIdMap);
		mtAttachmentMapper.deleteByMeetingId(meetingIdMap);
		mtSigninSequMapper.deleteByMeetingId(meetingIdMap);
		mtSigninRecordMapper.deleteByMeetingId(meetingIdMap);
		mtRemarksMapper.deleteByMeetingId(meetingIdMap);
	}

	public void createIMGroup(AddMeetingRequest addInfo, MtMeeting mtMeetingInfo, Map<String, MtParticipants> inMap,
			Map<String, MtSigninServ> serviceMap) throws JoinImGroupException, Exception {
		// 创建群组 调用im创建群组接口
		if (FLAG_OPEN.equals(addInfo.getCreateGroupFlag())) {
			// 创建群组
			CreateImGroupRequest cigReq = new CreateImGroupRequest();
			cigReq.setGroupName(mtMeetingInfo.getName());
			CreateImGroupResponse cigRes = thirdPartAccessMosService.createImGroup(cigReq);
			if (cigRes != null && BaseResponse.SUCCESS.equals(cigRes.getCode())) {
				String groupId = cigRes.getGroupId();
				mtMeetingInfo.setHasGroup(FLAG_OPEN);
				mtMeetingInfo.setGroupId(groupId);
				Map<String, String> map = new HashMap<String, String>();
				for (MtParticipants entity : inMap.values()) {
					map.put(entity.getPersonId(), entity.getPersonId());
				}
				for (MtSigninServ entity : serviceMap.values()) {
					map.put(entity.getUserId(), entity.getUserId());
				}
				map.remove(SessionContext.getUserId());

				// 加入成员
				if (map.size() > 0) {
					if (!mtMeetingService.joinImGroup(groupId, map)) {
						throw new JoinImGroupException();
					}
				}
			} else {
				throw new CreateImGroupException();
			}
		}
	}

	public void editIMGroup(EditMeetingRequest addInfo, Map<String, MtParticipants> inMap,
			Map<String, MtSigninServ> serviceMap) throws JoinImGroupException, Exception {
		// 修改群组 调用im创建群组接口
		if (FLAG_OPEN.equals(addInfo.getCreateGroupFlag())) {
			// 清空群组
			JoinImGroupRequest jigReq = new JoinImGroupRequest();
			jigReq.setGroupId(addInfo.getMeeting().getGroupId());
			String members = "";
			for (MtParticipants mc : addInfo.getSigninPersonList()) {
				if (!mc.getPersonId().equals(SessionContext.getUserId())) {
					members += mc.getPersonId() + mtMeetingService.SEPARATOR;
				}
			}
			if (StringUtil.isNotEmpty(members)) {
				jigReq.setMembers(members.substring(0, members.length() - 1));
			}
			jigReq.setMembers(members);
			thirdPartAccessMosService.deleteMembersFromImGroup(jigReq);
			// 加入群组
			String groupId = addInfo.getMeeting().getGroupId();
			Map<String, String> map = new HashMap<String, String>();
			for (MtParticipants entity : inMap.values()) {
				map.put(entity.getPersonId(), entity.getPersonId());
			}
			for (MtSigninServ entity : serviceMap.values()) {
				map.put(entity.getUserId(), entity.getUserId());
			}
			map.remove(SessionContext.getUserId());

			// 加入成员
			if (map.size() > 0) {
				if (!mtMeetingService.joinImGroup(groupId, map)) {
					throw new JoinImGroupException();
				}
			}
		}
	}

	public void pushForEdit(EditMeetingRequest addInfo,List<MtParticipants> oldList) throws Exception {
		MtMeeting mtMeetingInfo = addInfo.getMeeting();
		String meetingId = mtMeetingInfo.getId();
		// 参会人员 内部、外部、服务 MdParticipants
		List<MtParticipants> newList = new ArrayList<MtParticipants>();
		List<MdParticipants> inParticipantsList = addInfo.getInParticipantsList();
		Map<String, MtParticipants> inMap = new HashMap<String, MtParticipants>();
		for (MdParticipants info : inParticipantsList) {
			info.setId(IDGen.uuid());
			info.setObjectId(meetingId);
			info.setObjectName(mtMeetingInfo.getName());
			if ("user".equals(info.getEntityType())) {
				MtParticipants entity = new MtParticipants();
				entity.setId(IDGen.uuid());
				entity.setMeetingId(meetingId);
				entity.setPersonType(PARTICIPANT_TYPE_INNER);
				entity.setPersonId(info.getEntityId());
				entity.setPersonName(info.getEntityName());
				entity.setNoticeStatus(MtMeetingService.NOTICE_STATUS_0);
				inMap.put(entity.getPersonId(), entity);
			} else if ("dept".equals(info.getEntityType())) {
				GetUsersRequest guReq = new GetUsersRequest();
				guReq.setDepUuid(info.getEntityId());
				guReq.setDepScope("1");
				GetUsersResponse guRes = thirdPartAccessService.getUsersByDept(guReq);
				for (MyUser user : guRes.getUserInfos()) {
					MtParticipants entity = new MtParticipants();
					entity.setId(IDGen.uuid());
					entity.setMeetingId(meetingId);
					entity.setPersonType(PARTICIPANT_TYPE_INNER);
					entity.setPersonId(user.getUserUuid());
					entity.setPersonName(user.getUserName());
					// 0：未通知 1：已发送 2：已送达
					entity.setNoticeStatus(MtMeetingService.NOTICE_STATUS_0);
					inMap.put(entity.getPersonId(), entity);
				}
			}
		}
		// 生成MtParticipants数据
		for (MtParticipants entity : inMap.values()) {
			newList.add(entity);
		}
		// 服务人员信息预处理
		Map<String, MtSigninServ> serviceMap = new HashMap<String, MtSigninServ>();
		List<MdParticipants> serviceParticipantsList = addInfo.getServiceParticipantsList();
		for (MdParticipants info : serviceParticipantsList) {
			info.setId(IDGen.uuid());
			info.setObjectId(meetingId);
			info.setObjectName(mtMeetingInfo.getName());
			if ("user".equals(info.getEntityType())) {
				MtSigninServ entity = new MtSigninServ();
				entity.setId(IDGen.uuid());
				entity.setMeetingId(meetingId);
				entity.setUserId(info.getEntityId());
				entity.setUserName(info.getEntityName());
				serviceMap.put(info.getEntityId(), entity);
			} else if ("dept".equals(info.getEntityType())) {
				GetUsersRequest guReq = new GetUsersRequest();
				guReq.setDepUuid(info.getEntityId());
				guReq.setDepScope("1");
				GetUsersResponse guRes = thirdPartAccessService.getUsersByDept(guReq);
				for (MyUser user : guRes.getUserInfos()) {
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
		for (MtSigninServ entity : serviceMap.values()) {
			// 创建服务人员二维码信息
			MtParticipants mpInfo = new MtParticipants();
			mpInfo.setId(IDGen.uuid());
			mpInfo.setMeetingId(meetingId);
			mpInfo.setPersonType(PARTICIPANT_TYPE_SERVICE);
			mpInfo.setPersonId(entity.getUserId());
			mpInfo.setPersonName(entity.getUserName());
			mpInfo.setQrcode("-1");
			newList.add(mpInfo);
		}

		// 对比
		List<String> addList = new ArrayList<String>();
		List<String> deleteList = new ArrayList<String>();
		List<String> stayList = new ArrayList<String>();

		for (MtParticipants old : oldList) {
			String hasFlag = "0";
			String personId = old.getPersonId();
			for (MtParticipants entity : newList) {
				if (personId.equals(entity.getPersonId())) {
					hasFlag = "1";
				}
			}
			if ("1".equals(hasFlag)) {
				stayList.add(personId);
			} else {
				deleteList.add(personId);
			}
		}

		for (MtParticipants entity : newList) {
			String isAddFlag = "0";
			String personId = entity.getPersonId();
			for (String stay : stayList) {
				if (personId.equals(stay)) {
					isAddFlag = "1";
				}
			}
			for (String delete : deleteList) {
				if (personId.equals(delete)) {
					isAddFlag = "1";
				}
			}
			if ("0".equals(isAddFlag)) {
				addList.add(personId);
			}
		}

//		 System.out.println("========================>>>>>>>>>>>>>>>>oldList>>");
//		 for(MtParticipants entity : oldList){
//		 System.out.println(entity.getPersonId());
//		 }
//		 System.out.println("========================>>>>>>>>>>>>>>>>newList>>");
//		 for(MtParticipants entity : newList){
//		 System.out.println(entity.getPersonId());
//		 }
//		 System.out.println("========================>>>>>>>>>>>>>>>>addList>>");
//		 for(String entity : addList){
//		 System.out.println((entity));
//		 }
//		 System.out.println("========================>>>>>>>>>>>>>>>>deleteList>>");
//		 for(String entity : deleteList){
//		 System.out.println((entity));
//		 }
//		 System.out.println("========================>>>>>>>>>>>>>>>>stayList>>");
//		 for(String entity : stayList){
//		 System.out.println((entity));
//		 }

		// 推送处理
		Map<String, Object> getParticipantMap = new HashMap<String, Object>();
		getParticipantMap.put("meetingId", mtMeetingInfo.getId());
		getParticipantMap.put("userId", mtMeetingInfo.getSponsor());
		MtParticipants mp = mtParticipantsMapper.getParticipant(getParticipantMap);
		if (mp == null) {
			return;
		}
		String username = mp.getPersonName();

		// 新增的人员将按照普通推送方式走
		// 处理编辑前后都存在的人员
		for (String entity : stayList) {
			Map<String, Object> getStayMpMap = new HashMap<String, Object>();
			getStayMpMap.put("meetingId", mtMeetingInfo.getId());
			getStayMpMap.put("personId", entity);
			List<MtParticipants> stayMpRs = mtParticipantsMapper.getByMeetingId(getStayMpMap);
			if(stayMpRs.size()>0){
				MtParticipants stayMp = stayMpRs.get(0);
				stayMp.setNoticeStatus("1");
				mtParticipantsMapper.updateByPrimaryKeySelective(stayMp);
			}
		}
		pushStayMsg(mtMeetingInfo, stayList, username);
		// 推送被移除人员
		pushDelMsg(mtMeetingInfo, deleteList, username);
	}

	public void pushDelMsg(MtMeeting mtMeetingInfo, List<String> idList, String username) {
		String title = "您被" + username + "移出了会议。";
		StringBuilder sb = new StringBuilder();
		sb.append("会议名称：");
		sb.append(mtMeetingInfo.getName());
		sb.append("\n会议时间：");
		sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(mtMeetingInfo.getBeginTime()));
		sb.append("-");
		sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(mtMeetingInfo.getEndTime()));
		sb.append("\n会议地址：");
		sb.append(mtMeetingInfo.getAddress());
		sb.append("\n召开人：");
		sb.append(username);
		thirdPartAccessService.pushReceiveEvent(mtMeetingInfo.getEcid(), StringUtils.join(idList, ","), title,
				sb.toString(), null);
	}

	public void pushStayMsg(MtMeeting mtMeetingInfo, List<String> idList, String username) {
		String title = "您参加的会议被" + username + "更新。";
		StringBuilder sb = new StringBuilder();
		sb.append("会议名称：");
		sb.append(mtMeetingInfo.getName());
		sb.append("\n会议时间：");
		sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(mtMeetingInfo.getBeginTime()));
		sb.append("-");
		sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(mtMeetingInfo.getEndTime()));
		sb.append("\n会议地址：");
		sb.append(mtMeetingInfo.getAddress());
		sb.append("\n召开人：");
		sb.append(username);
		thirdPartAccessService.pushReceiveEvent(mtMeetingInfo.getEcid(), StringUtils.join(idList, ","), title,
				sb.toString(), mtMeetingInfo.getId());
	}
}
