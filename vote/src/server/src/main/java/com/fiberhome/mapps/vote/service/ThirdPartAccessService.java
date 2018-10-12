package com.fiberhome.mapps.vote.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.vote.entity.MobilearkBean;
import com.fiberhome.mapps.vote.entity.MobilearkDepartment;
import com.fiberhome.mapps.vote.entity.MobilearkUser;
import com.fiberhome.mapps.vote.entity.MyDepartment;
import com.fiberhome.mapps.vote.entity.MyUser;
import com.fiberhome.mapps.vote.entity.PushEvent;
import com.fiberhome.mapps.vote.request.GetUsersRequest;
import com.fiberhome.mapps.vote.response.GetUsersResponse;
import com.fiberhome.mapps.vote.utils.ErrorCode;
import com.fiberhome.mapps.vote.utils.JsonUtil;
import com.fiberhome.mos.core.openapi.rop.client.RopClient;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tk.mybatis.mapper.util.StringUtil;

@ServiceMethodBean
public class ThirdPartAccessService
{
    protected final Logger  LOGGER = LoggerFactory.getLogger(getClass());

    @Value("${mplus.sso.serviceUrl}")
    String                  webRoot;
    @Value("${mplus.sso.appKey}")
    String                  appKey;
    @Value("${mplus.sso.secret}")
    String                  appSecret;
    @Value("${client.appId}")
    String                  appId;
    @Value("${client.appName}")
    String                  appName;
    public static final int limit  = 100;

    private List<MyDepartment> convertDep(List<MobilearkDepartment> list)
    {
        List<MyDepartment> reList = new ArrayList<MyDepartment>();
        Collections.sort(list, new Comparator<MobilearkDepartment>()
        {
            public int compare(MobilearkDepartment obj1, MobilearkDepartment obj2)
            {
                return obj1.getDepWeight().compareTo(obj2.getDepWeight());
            }
        });
        for (MobilearkDepartment dep : list)
        {
            MyDepartment myDep = new MyDepartment();
            myDep.setDepUuid(dep.getDepUuid());
            myDep.setDepName(dep.getDepName());
            myDep.setParentId(dep.getParentId());
            myDep.setDepOrder(dep.getDepOrder());
            reList.add(myDep);
        }
        return reList;
    }

    public List<MyUser> convertUser(List<MobilearkUser> list)
    {
        List<MyUser> reList = new ArrayList<MyUser>();
        Collections.sort(list, new Comparator<MobilearkUser>()
        {
            public int compare(MobilearkUser obj1, MobilearkUser obj2)
            {
                return obj1.getUserWeight().compareTo(obj2.getUserWeight());
            }
        });
        for (MobilearkUser user : list)
        {
            MyUser myUser = new MyUser();
            myUser.setUserUuid(user.getLoginId());
            myUser.setUserName(user.getUserName());
            myUser.setDeptUuid(user.getDepUuid());
            myUser.setDeptName(subDeptName(user.getDepartment()));
            myUser.setPhoneNum(user.getPhoneNumber());
            myUser.setLoginId(user.getLoginId());
            reList.add(myUser);
        }
        return reList;
    }

    private String subDeptName(String deptNames)
    {
        if (StringUtil.isNotEmpty(deptNames))
        {
            String[] strArr = deptNames.split("\\\\");
            if (strArr.length > 0)
            {
                return strArr[strArr.length - 1];
            }
        }
        return "";
    }

    @ServiceMethod(method = "mapps.thirdpart.mobileark.getusers", group = "thirdpart", groupTitle = "文件服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetUsersResponse getUsers(GetUsersRequest request) throws Exception
    {
        GetUsersResponse response = new GetUsersResponse();
        try
        {
            String userName = request.getUserName();
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("orgUuid", SessionContext.getOrgId());
            if (StringUtil.isNotEmpty(userName))
            {
                reqMap.put("userName", userName);
            }
            if (StringUtil.isNotEmpty(request.getLoginId()))
            {
                reqMap.put("loginId", request.getLoginId());
            }
            if (StringUtil.isNotEmpty(request.getDepUuid()))
            {
                reqMap.put("depUuid", request.getDepUuid());
            }
            if (StringUtil.isNotEmpty(request.getDepScope()))
            {
                reqMap.put("depScope", request.getDepScope());
            }
            reqMap.put("startPage", 1);
            reqMap.put("limit", limit);
            RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
            String result = client.requestForString("mobileark.getusers", "1.3", reqMap);
            // LOGGER.debug("======获取成员响应结果======" + result);
            JSONObject data = JSONObject.fromObject(result);
            if (data.containsKey("code"))
            {
                LOGGER.info("获取成员失败:{}", result);
                ErrorCode.fail(response, ErrorCode.CODE_100001);
                return response;
            }
            MobilearkBean bean = (MobilearkBean) JsonUtil.jsonToObject(result, MobilearkBean.class);
            response.setUserInfos(convertUser(bean.getUserInfos()));
            int pageNum = bean.getUserSize() / limit;
            int remainder = bean.getUserSize() % limit;
            if (remainder > 0)
            {
                pageNum++;
            }
            for (int i = 2; i <= pageNum; i++)
            {
                MobilearkBean mb = getUserForPage(reqMap, i);
                response.getUserInfos().addAll(convertUser(mb.getUserInfos()));
            }
        }
        catch (Exception e)
        {
            LOGGER.error("获取成员失败：{}", e);
            ErrorCode.fail(response, ErrorCode.CODE_100001);
        }
        return response;
    }

    public MobilearkBean getUserForPage(Map<String, Object> reqMap, int startPage) throws Exception
    {
        reqMap.put("startPage", startPage);
        reqMap.put("limit", limit);
        RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
        String result = client.requestForString("mobileark.getusers", "1.3", reqMap);
        // LOGGER.debug("======获取成员第" + startPage + "页响应结果======" + result);
        JSONObject data = JSONObject.fromObject(result);
        if (data.containsKey("code"))
        {
            LOGGER.info("获取成员失败:{}", result);
            throw new Exception();
        }
        return (MobilearkBean) JsonUtil.jsonToObject(result, MobilearkBean.class);
    }

    public MobilearkBean getuser(String loginIds) throws Exception
    {
        MobilearkBean bean = new MobilearkBean();
        try
        {
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("orgUuid", SessionContext.getOrgId());
            reqMap.put("loginIds", loginIds);
            RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
            String result = client.requestForString("mobileark.getuser", "1.1", reqMap);
            // LOGGER.debug("======获取成员响应结果======" + result);
            JSONObject data = JSONObject.fromObject(result);
            if (data.containsKey("code"))
            {
                LOGGER.info("获取成员失败:{}", result);
                return null;
            }
            bean = (MobilearkBean) JsonUtil.jsonToObject(result, MobilearkBean.class);
        }
        catch (Exception e)
        {
            LOGGER.error("获取成员失败：{}", e);
        }
        return bean;
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

    public boolean pushSysMsg(String ecid, String[] userIds, String title, String content)
    {
        LOGGER.debug("系统消息推送,ecid={},userId={},title={},content={}", ecid, userIds, title, content);
        try
        {
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("ecid", ecid);
            reqMap.put("loginIds", StringUtils.join(userIds, ",") + ",a");
            reqMap.put("type", "1");
            reqMap.put("depUuids", "");
            reqMap.put("title", title);
            reqMap.put("content", content);
            RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
            String result = client.requestForString("mobileark.syspush", "1.2", reqMap);
            LOGGER.debug("======消息推送响应结果======" + result);
        }
        catch (Exception e)
        {
            LOGGER.error("消息推送失败：{}", e);
            return false;
        }
        return true;
    }

    public boolean pushReceiveEvent(String ecid, String userIds, String title, String content, String meetingId)
    {
        LOGGER.debug("发送代办事件,ecid={},userId={},title={},content={}", ecid, userIds, title, content);
        try
        {
            Map<String, Object> reqMap = new HashMap<String, Object>();
            PushEvent pe = new PushEvent();
            pe.setTitle(title);
            pe.setSummary(content);
            if (StringUtil.isNotEmpty(meetingId))
            {
                pe.setScheme("poll2.html?id=" + meetingId + "&sessionId=${token}&password=${password}&token=${token}");
            }
            JSONArray jsonArray = JSONArray.fromObject(pe);
            reqMap.put("events", jsonArray);
            JSONObject jo = JSONObject.fromObject(reqMap);
            reqMap.put("events", jo);
            reqMap.put("ecid", ecid);
            reqMap.put("loginId", userIds);
            reqMap.put("appid", appId);
            reqMap.put("appid_ios", "");
            reqMap.put("appName", appName);
            reqMap.put("appType", 3);
            RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
            String result = client.requestForString("mobileark.receiveevent", "1.2", reqMap);
            LOGGER.debug("======发送代办事件响应结果======" + result);
        }
        catch (Exception e)
        {
            LOGGER.error("发送代办事件失败：{}", e);
            return false;
        }
        return true;
    }

    public int getUserSizeFormDept(String depIds) throws Exception
    {
        int count = 0;
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("orgUuid", SessionContext.getOrgId());
        reqMap.put("depScope", "1");
        String[] dida = depIds.split(",");
        for (String did : dida)
        {
            reqMap.put("depUuid", did);
            RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
            String result = client.requestForString("mobileark.getusers", "1.3", reqMap);
            // LOGGER.debug("======获取成员响应结果======" + result);
            JSONObject data = JSONObject.fromObject(result);
            if (data.containsKey("code"))
            {
                LOGGER.info("获取成员失败:{}", result);
            }
            MobilearkBean bean = (MobilearkBean) JsonUtil.jsonToObject(result, MobilearkBean.class);
            count += bean.getUserSize();
        }
        return count;
    }
}
