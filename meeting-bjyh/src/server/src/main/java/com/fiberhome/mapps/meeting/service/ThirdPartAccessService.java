package com.fiberhome.mapps.meeting.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fiberhome.mapps.contact.pojo.EventParam;
import com.fiberhome.mapps.contact.pojo.MobilearkBean;
import com.fiberhome.mapps.contact.pojo.MobilearkDocument;
import com.fiberhome.mapps.contact.pojo.MyDepartment;
import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.contact.service.MplusAccessService;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.meeting.entity.MtDocument;
import com.fiberhome.mapps.meeting.request.GetDepartmentsRequest;
import com.fiberhome.mapps.meeting.request.GetDocUrlRequest;
import com.fiberhome.mapps.meeting.request.GetPersonDocRequest;
import com.fiberhome.mapps.meeting.request.GetUsersRequest;
import com.fiberhome.mapps.meeting.response.GetDepartmentsResponse;
import com.fiberhome.mapps.meeting.response.GetDocUrlResponse;
import com.fiberhome.mapps.meeting.response.GetPersonDocResponse;
import com.fiberhome.mapps.meeting.response.GetUsersResponse;
import com.fiberhome.mapps.meeting.utils.ErrorCode;
import com.fiberhome.mapps.meeting.utils.LogUtil;
import com.fiberhome.mapps.push.PushTask;
import com.fiberhome.mos.core.openapi.rop.client.RopClient;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

import tk.mybatis.mapper.util.StringUtil;

@ServiceMethodBean
public class ThirdPartAccessService
{
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Value("${mplus.sso.serviceUrl}")
    String                 webRoot;
    @Value("${mplus.sso.appKey}")
    String                 appKey;
    @Value("${mplus.sso.secret}")
    String                 appSecret;
    @Value("${client.appId}")
    String                 appId;
    @Value("${client.appName}")
    String                 appName;
    @Autowired
    MplusAccessService     accessService;

    /*
     * 将M-plus文档信息转成web
     */
    private List<MtDocument> convertDocument(List<MobilearkDocument> list, GetPersonDocRequest request)
    {
        String searchParam = StringUtil.isEmpty(request.getSearchParam()) ? "" : request.getSearchParam();
        String flag = request.getSearchExistFlag();
        String ids = request.getFileIds();
        List<MtDocument> reList = new ArrayList<MtDocument>();
        // 无需要查询的内容
        if ("1".equals(flag) && StringUtil.isEmpty(ids))
        {
            return reList;
        }
        for (MobilearkDocument doc : list)
        {
            MtDocument mtDoc = new MtDocument();
            String name = doc.getDocumentName();
            String id = doc.getDocumentId();
            if (name.contains(searchParam))
            {
                if ("1".equals(flag) && !ids.contains(id))
                {
                    continue;
                }
                mtDoc.setFileName(name);
                mtDoc.setFileId(id);
                mtDoc.setFileSize(doc.getSize());
                mtDoc.setFileType(doc.getType());
                mtDoc.setUploadTime(doc.getCreateTime());
                reList.add(mtDoc);
            }
        }
        return reList;
    }

    /**
     * 获取文档列表
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @ServiceMethod(method = "mapps.thirdpart.mobileark.getpersondocs", group = "thirdpart", groupTitle = "个人文档获取", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetPersonDocResponse GetPersonDocs(GetPersonDocRequest request) throws Exception
    {
        GetPersonDocResponse response = new GetPersonDocResponse();
        try
        {
            MobilearkBean bean = accessService.getPersonDocs(SessionContext.getOrgId(), SessionContext.getUserUuid(),
                    request.getFolderId(), request.getSearchParam());
            if (bean != null)
            {
                response.setDocumentInfo(convertDocument(bean.getDocumentList(), request));
                if (StringUtil.isEmpty(request.getSearchParam()))
                    response.setFolderList(bean.getFolderList());
            }
        }
        catch (Exception e)
        {
            LOGGER.error("获取个人文档失败：{}", e);
            ErrorCode.fail(response, ErrorCode.CODE_100001);
        }
        return response;
    }

    /**
     * 获取预览/下载地址
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @ServiceMethod(method = "mapps.thirdpart.mobileark.getdocurl", group = "thirdpart", groupTitle = "获取预览下载地址", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetDocUrlResponse GetDocUrl(GetDocUrlRequest request) throws Exception
    {
        LOGGER.info("获取预览/下载地址(mapps.thirdpart.mobileark.getdocurl)入口,documentId={}", request.getDocumentId());
        GetDocUrlResponse response = new GetDocUrlResponse();
        response.setDocUrls(accessService.getDocUrl(request.getDocumentId(), null));
        return response;
    }

    @ServiceMethod(method = "mapps.thirdpart.mobileark.getdepartments", group = "thirdpart", groupTitle = "文件服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetDepartmentsResponse getDepartments(GetDepartmentsRequest request) throws Exception
    {
        GetDepartmentsResponse response = new GetDepartmentsResponse();
        try
        {
            LOGGER.info("获取部门接口(mapps.thirdpart.mobileark.getdepartments)入口");
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("orgUuid", SessionContext.getOrgId());
            List<MyDepartment> mdList = accessService.getDepartments(SessionContext.getOrgId());
            
            // 如果有orgdepid,则通过orgdeporder进行权限控制
            if (SessionContext.getOrgDeptId() != null)
            {
                List<MyDepartment> newmdList = new ArrayList<MyDepartment>();
                for (MyDepartment md : mdList)
                {
                    if (md.getDepOrder().startsWith(SessionContext.getOrgDeptOrder()))
                    {
                        LOGGER.info("deporder===>" + md.getDepOrder());
                        newmdList.add(md);
                    }
                }
                mdList = newmdList;
            }
            
            response.setDepartmentInfos(mdList);
            LOGGER.info("获取部门接口调用成功");
        }
        catch (Exception e)
        {
            LOGGER.error("获取部门失败：{}", e);
            ErrorCode.fail(response, ErrorCode.CODE_100001);
        }
        return response;
    }

    @ServiceMethod(method = "mapps.thirdpart.mobileark.getusers", group = "thirdpart", groupTitle = "文件服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetUsersResponse getUsers(GetUsersRequest request) throws Exception
    {
        GetUsersResponse response = new GetUsersResponse();
        try
        {
            LOGGER.info("获取成员接口(mapps.thirdpart.mobileark.getusers)入口,请求参数==" + LogUtil.getObjectInfo(request));
            String depUuid = request.getDepUuid();
            if (StringUtil.isNotEmpty(depUuid))
            {
                return getUsersByDept(request);
            }
            String userName = request.getUserName();
            if (StringUtil.isEmpty(userName))
                return response;
            LOGGER.info("====================获取到OrgDeptId:" + SessionContext.getOrgDeptId());
            // 如果有orgdepid,则通过orgdepid进行搜索,通过权限控制搜索的范围
            if (SessionContext.getOrgDeptId() == null)
            {
                response.setUserInfos(accessService.getUsersByName(SessionContext.getOrgId(), userName, 20));
            }
            else
            {
                response.setUserInfos(accessService.getUsersInDeptByName(SessionContext.getOrgId(),
                        SessionContext.getOrgDeptId(), userName, "1", 20));
            }
            LOGGER.info("获取成员接口调用成功");
        }
        catch (Exception e)
        {
            LOGGER.error("获取成员失败：{}", e);
            ErrorCode.fail(response, ErrorCode.CODE_100002);
        }
        return response;
    }

    public GetUsersResponse getUsersByDept(GetUsersRequest request) throws Exception
    {
        GetUsersResponse response = new GetUsersResponse();
        try
        {
            LOGGER.info("获取成员接口(getUsersByDept)入口,请求参数==" + LogUtil.getObjectInfo(request));
            if (StringUtil.isEmpty(request.getDepUuid()) || StringUtil.isEmpty(request.getDepScope()))
                return response;
            response.setUserInfos(accessService.getUsersByDept(SessionContext.getOrgId(), request.getDepUuid(),
                    request.getDepScope(), 0));
            LOGGER.info("获取成员接口调用成功");
        }
        catch (Exception e)
        {
            LOGGER.error("获取成员失败：{}", e);
            ErrorCode.fail(response, ErrorCode.CODE_100002);
        }
        return response;
    }

    public List<MyUser> getUserInfos(String orgUuid, String loginIds) throws Exception
    {
        LOGGER.info("获取用户信息入口");
        return accessService.getUserByLoginId(orgUuid, loginIds);
    }

    public MyUser getUserInfo(String orgUuid, String loginId) throws Exception
    {
        List<MyUser> list = getUserInfos(orgUuid, loginId);
        if (list != null && list.size() > 0)
        {
            return list.get(0);
        }
        return null;
    }

    @ServiceMethod(method = "mapps.thirdpart.mobileark.getlimtedusers", group = "thirdpart", groupTitle = "文件服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetUsersResponse getLimtedUsers(GetUsersRequest request) throws Exception
    {
        GetUsersResponse response = new GetUsersResponse();
        return response;
    }

    public boolean sendSms(String phone, String content)
    {
        try
        {
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("phoneNumbers", phone);
            reqMap.put("content", content);
            RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
            String result = client.requestForString("mobileark.smspush", "1.0", reqMap);
            LOGGER.debug("======短信发送响应结果======" + result);
        }
        catch (Exception e)
        {
            LOGGER.error("短信发送失败：{}", e);
            return false;
        }
        return true;
    }

    // public void pushSysMsg(String ecid, String userId, String title, String content)
    // {
    // LOGGER.debug("系统消息推送,ecid={},userId={},title={},content={}", ecid, userId, title, content);
    // try
    // {
    // accessService.pushSysMsg(ecid, userId, title, content);
    // }
    // catch (Exception e)
    // {
    // LOGGER.error("系统消息发送失败：{}", e);
    // }
    // }

    public boolean pushReceiveEvent(String ecid, String userIds, String title, String content, String meetingId,
            EventParam param)
    {
        LOGGER.debug("发送代办事件,ecid={},userId={},title={},content={},reservedId={}", ecid, userIds, title, content,
                meetingId);
        try
        {
            String scheme = "";
            if (StringUtil.isNotEmpty(meetingId))
            {
                scheme = "#ID-PageMeetDetail/" + meetingId;
            }
            PushTask.addJobForReceiveEvent(ecid, userIds, title, content, scheme, null);
            // accessService.pushReceiveEvent(ecid, userIds, title, content, scheme);
            // accessService.pushReceiveEvent(ecid, userIds, title, content, scheme, MplusAccessService.APPTYPE_HTML5,
            // param);
        }
        catch (Exception e)
        {
            LOGGER.error("发送代办事件失败：{}", e);
            return false;
        }
        return true;
    }

    public boolean pushReceiveEventForService(String ecid, String userIds, String title, String content,
            String meetingId, EventParam param)
    {
        LOGGER.debug("发送服务人员代办事件,ecid={},userId={},title={},content={},reservedId={}", ecid, userIds, title, content,
                meetingId);
        try
        {
            String scheme = "";
            if (StringUtil.isNotEmpty(meetingId))
            {
                scheme = "#ID-PageMeetDetailForService/" + meetingId;
            }
            PushTask.addJobForReceiveEvent(ecid, userIds, title, content, scheme, null);
            // accessService.pushReceiveEvent(ecid, userIds, title, content, scheme);
            // accessService.pushReceiveEvent(ecid, userIds, title, content, scheme, MplusAccessService.APPTYPE_HTML5,
            // param);
        }
        catch (Exception e)
        {
            LOGGER.error("发送代办事件失败：{}", e);
            return false;
        }
        return true;
    }

}
