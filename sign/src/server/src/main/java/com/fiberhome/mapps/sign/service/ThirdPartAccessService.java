package com.fiberhome.mapps.sign.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fiberhome.mapps.contact.pojo.MyDepartment;
import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.contact.service.MplusAccessService;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.sign.utils.LogUtil;
import com.rop.annotation.ServiceMethodBean;

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

    public List<String> getDeptOrder(String depIds) throws Exception
    {
        List<String> list = new ArrayList<String>();
        try
        {
            LOGGER.info("获取deptorder入口,depIds = {}", depIds);
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("orgUuid", SessionContext.getOrgId());
            List<MyDepartment> mdList = accessService.getDepartments(SessionContext.getOrgId());
            for (MyDepartment mdInfo : mdList)
            {
                if (depIds.contains(mdInfo.getDepUuid()))
                {
                    list.add(mdInfo.getDepOrder());
                }
            }
            LOGGER.info("获取deptorder成功");
        }
        catch (Exception e)
        {
            LOGGER.error("获取deptorder失败：{}", e);
        }
        return list;
    }

    // @ServiceMethod(method = "mapps.thirdpart.mobileark.getusers", group = "thirdpart", groupTitle = "文件服务", version =
    // "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    // public GetUsersResponse getUsers(GetUsersRequest request) throws Exception
    // {
    // GetUsersResponse response = new GetUsersResponse();
    // try
    // {
    // LOGGER.info("获取成员接口(mapps.thirdpart.mobileark.getusers)入口,请求参数==" + LogUtil.getObjectInfo(request));
    // String depUuid = request.getDepUuid();
    // if (StringUtil.isNotEmpty(depUuid))
    // {
    // return getUsersByDept(request);
    // }
    // String userName = request.getUserName();
    // if (StringUtil.isEmpty(userName))
    // return response;
    // response.setUserInfos(accessService.getUsersByName(SessionContext.getOrgId(), userName, 20));
    // LOGGER.info("获取成员接口调用成功");
    // }
    // catch (Exception e)
    // {
    // LOGGER.error("获取成员失败：{}", e);
    // ErrorCode.fail(response, ErrorCode.CODE_100002);
    // }
    // return response;
    // }

    public List<MyUser> getUsersByDept(String deptIds) throws Exception
    {
        List<MyUser> muList = new ArrayList<MyUser>();
        try
        {
            LOGGER.info("获取成员接口(getUsersByDept)入口,请求参数==" + LogUtil.getObjectInfo(deptIds));
            String[] dids = deptIds.split(",");
            for (String did : dids)
            {
                muList.addAll(accessService.getUsersByDept(SessionContext.getOrgId(), did, "1", 0));
            }
            LOGGER.info("获取成员接口调用成功");
        }
        catch (Exception e)
        {
            LOGGER.error("获取成员失败：{}", e);
        }
        return muList;
    }

    public List<MyUser> getUserInfos(String orgUuid, String loginIds) throws Exception
    {
        LOGGER.info("获取用户信息入口");
        return accessService.getUserByLoginId(orgUuid, loginIds);
    }
    //
    // public MyUser getUserInfo(String orgUuid, String loginId) throws Exception
    // {
    // List<MyUser> list = getUserInfos(orgUuid, loginId);
    // if (list != null && list.size() > 0)
    // {
    // return list.get(0);
    // }
    // return null;
    // }
    //
    // @ServiceMethod(method = "mapps.thirdpart.mobileark.getlimtedusers", group = "thirdpart", groupTitle = "文件服务",
    // version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    // public GetUsersResponse getLimtedUsers(GetUsersRequest request) throws Exception
    // {
    // GetUsersResponse response = new GetUsersResponse();
    // return response;
    // }
    //
    // public boolean sendSms(String phone, String content)
    // {
    // try
    // {
    // Map<String, Object> reqMap = new HashMap<String, Object>();
    // reqMap.put("phoneNumbers", phone);
    // reqMap.put("content", content);
    // RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
    // String result = client.requestForString("mobileark.smspush", "1.0", reqMap);
    // LOGGER.debug("======短信发送响应结果======" + result);
    // }
    // catch (Exception e)
    // {
    // LOGGER.error("短信发送失败：{}", e);
    // return false;
    // }
    // return true;
    // }
    //
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
    //
    // public boolean pushReceiveEvent(String ecid, String userIds, String title, String content, String meetingId)
    // {
    // LOGGER.debug("发送代办事件,ecid={},userId={},title={},content={},reservedId={}", ecid, userIds, title, content,
    // meetingId);
    // try
    // {
    // String scheme = "";
    // if (StringUtil.isNotEmpty(meetingId))
    // {
    // scheme = "index.html#eventToDetail/" + meetingId;
    // }
    // accessService.pushReceiveEvent(ecid, userIds, title, content, scheme);
    // }
    // catch (Exception e)
    // {
    // LOGGER.error("发送代办事件失败：{}", e);
    // return false;
    // }
    // return true;
    // }

}
