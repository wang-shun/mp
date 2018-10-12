package com.fiberhome.mapps.contact.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.fiberhome.mapps.contact.MplusException;
import com.fiberhome.mapps.contact.pojo.EventParam;
import com.fiberhome.mapps.contact.pojo.MobilearkBean;
import com.fiberhome.mapps.contact.pojo.MobilearkDepartment;
import com.fiberhome.mapps.contact.pojo.MobilearkOrgInfo;
import com.fiberhome.mapps.contact.pojo.MobilearkUser;
import com.fiberhome.mapps.contact.pojo.MyDepartment;
import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.contact.pojo.PushEvent;
import com.fiberhome.mapps.contact.pojo.PushEventV13;
import com.fiberhome.mapps.contact.utils.JsonUtil;
import com.fiberhome.mos.core.openapi.rop.client.RopClient;
import com.fiberhome.mos.core.openapi.rop.client.RopClientException;
import com.rop.annotation.ServiceMethodBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@ServiceMethodBean
public class MplusAccessService
{
    protected final Logger     LOGGER              = LoggerFactory.getLogger(getClass());

    @Value("${mplus.sso.serviceUrl}")
    String                     webRoot;
    @Value("${mplus.sso.appKey}")
    String                     appKey;
    @Value("${mplus.sso.secret}")
    String                     appSecret;
    @Value("${client.appId}")
    String                     appId;
    @Value("${client.appName}")
    String                     appName;
    @Value("${mplus.sso.appId}")
    String                     appIdMng;
    public static final int    DEFAULT_LIMIT       = 500;
    /** mobileark.getusers */
    public static final int    SPLIT_TYPE_GETUSERS = 0;
    /** mobileark.getuser */
    public static final int    SPLIT_TYPE_GETUSER  = 1;

    public static final String APPTYPE_EXMOBI      = "1";
    public static final String APPTYPE_NATIVEAPP   = "2";
    public static final String APPTYPE_HTML5       = "3";
    public static final String APPTYPE_PC          = "4";

    /**
     * 获取机构列表
     * 
     * @return
     * @throws Exception
     */
    public Map<String, String> getOrg() throws Exception
    {
        try
        {
            LOGGER.info("获得机构列表1.1(mobileark.getorglist)入口");
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("startPage", "-1");
            RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
            String result = client.requestForString("mobileark.getorglist", "1.1", reqMap);
            LOGGER.debug("======响应结果======" + result);
            JSONObject data = JSONObject.fromObject(result);
            if (data.containsKey("code"))
            {
                LOGGER.info("获取机构列表失败:{}", result);
                return null;
            }
            MobilearkBean bean = (MobilearkBean) JsonUtil.jsonToObject(result, MobilearkBean.class);
            Map<String, String> map = new HashMap<String, String>();
            for (MobilearkOrgInfo info : bean.getOrgs())
            {
                map.put(info.getOrgCode(), info.getOrgUuid());
            }
            return map;
        }
        catch (Exception e)
        {
            LOGGER.error("获取机构列表失败：{}", e);
        }
        return null;
    }

    /**
     * 根据机构唯一标示向mplus请求组织信息
     * 
     * @param orgUuid
     * @return
     * @throws Exception
     */
    public List<MyDepartment> getDepartments(String orgUuid) throws Exception
    {
        try
        {
            LOGGER.info("获得组织架构1.3(mobileark.getdepartments)入口,请求参数==" + orgUuid);
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("orgUuid", orgUuid);
            RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
            String result = client.requestForString("mobileark.getdepartments", "1.3", reqMap);
            LOGGER.debug("======响应结果======" + result);
            JSONObject data = JSONObject.fromObject(result);
            if (data.containsKey("code"))
            {
                LOGGER.info("获取部门失败:{}", result);
                return null;
            }
            MobilearkBean bean = (MobilearkBean) JsonUtil.jsonToObject(result, MobilearkBean.class);
            return convertDep(bean.getDepartmentInfos());
        }
        catch (Exception e)
        {
            LOGGER.error("获取部门失败：{}", e);
        }
        return null;
    }

    /**
     * 接口返回信息重新封装
     * 
     * @param list
     * @return
     */
    private List<MyDepartment> convertDep(List<MobilearkDepartment> list)
    {
        List<MyDepartment> reList = new ArrayList<MyDepartment>();
        // 权重排序
        Collections.sort(list, new Comparator<MobilearkDepartment>()
        {
            public int compare(MobilearkDepartment obj1, MobilearkDepartment obj2)
            {
                return obj1.getDepWeight().compareTo(obj2.getDepWeight());
            }
        });
        // 对象封装
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

    /**
     * 根据部门id查询成员列表
     * 
     * @param orgUuid
     * @param depUuid
     * @param depScope 是否查询子集 部门范围，0表示当前部门成员，1表示部门及子部门所有成员
     * @param maxNum 0为不限制
     * @return
     * @throws Exception
     */
    public List<MyUser> getUsersByDept(String orgUuid, String depUuid, String depScope, int maxNum) throws Exception
    {
        LOGGER.info("获得成员列表(getUsersByDept)入口,请求参数==orgUuid=" + orgUuid + ",depUuid=" + depUuid + ",depScope="
                + depScope + ",maxNum=" + maxNum);
        if (StringUtils.isEmpty(depUuid) || StringUtils.isEmpty(depScope))
            return null;
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("orgUuid", orgUuid);
        reqMap.put("depUuid", depUuid);
        reqMap.put("depScope", depScope);
        reqMap.put("startPage", "1");
        return getUsers(reqMap, maxNum);
    }

    /**
     * 根据用户名查询部门下的成员列表
     * 
     * @param orgUuid
     * @param depUuid
     * @param depScope 是否查询子集 部门范围，0表示当前部门成员，1表示部门及子部门所有成员
     * @param userName 模糊查询用户名
     * @param maxNum 0为不限制
     * @return
     * @throws Exception
     */
    public List<MyUser> getUsersInDeptByName(String orgUuid, String depUuid, String userName, String depScope,
            int maxNum)
        throws Exception
    {
        LOGGER.info("根据用户名查询部门下的成员列表(getUsersInDeptByName)入口,请求参数==orgUuid=" + orgUuid + ",depUuid=" + depUuid
                + ",depScope=" + depScope + ",userName=" + userName + ",maxNum=" + maxNum);
        if (StringUtils.isEmpty(depUuid) || StringUtils.isEmpty(depScope))
            return null;
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("orgUuid", orgUuid);
        reqMap.put("depUuid", depUuid);
        reqMap.put("depScope", depScope);
        reqMap.put("userName", userName);
        reqMap.put("startPage", "1");
        return getUsers(reqMap, maxNum);
    }

    /**
     * 根据用户名模糊查询成员列表
     * 
     * @param orgUuid
     * @param userName 模糊查询用户名
     * @param maxNum 获取上限 0为不限制
     * @return 用户信息列表
     * @throws Exception
     */
    public List<MyUser> getUsersByName(String orgUuid, String userName, int maxNum) throws Exception
    {
        LOGGER.info(
                "获得成员列表(getUsersByName)入口,请求参数==orgUuid=" + orgUuid + ",userName=" + userName + ",maxNum=" + maxNum);
        if (StringUtils.isEmpty(userName))
            return null;
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("orgUuid", orgUuid);
        reqMap.put("userName", userName);
        reqMap.put("startPage", "1");
        return getUsers(reqMap, maxNum);
    }

    /**
     * 获取用户列表
     * 
     * @param reqMap
     * @param maxNum
     * @return
     * @throws Exception
     */
    public List<MyUser> getUsers(Map<String, Object> reqMap, int maxNum) throws Exception
    {
        try
        {
            LOGGER.info("获得成员列表V1.3(mobileark.getusers)入口");
            // 设置每页数量
            int limit = maxNum > 0 && maxNum < DEFAULT_LIMIT ? maxNum : DEFAULT_LIMIT;
            reqMap.put("limit", limit);
            RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
            String result = client.requestForString("mobileark.getusers", "1.3", reqMap);
            LOGGER.debug("======响应结果======" + result);
            JSONObject data = JSONObject.fromObject(result);
            if (data.containsKey("code"))
            {
                LOGGER.info("获取成员失败:{}", result);
                return null;
            }
            MobilearkBean bean = (MobilearkBean) JsonUtil.jsonToObject(result, MobilearkBean.class);
            List<MyUser> myUserList = convertUser(bean.getUserInfos(), SPLIT_TYPE_GETUSERS);
            // 判断是否超查询上限
            if (maxNum > 0 && maxNum < DEFAULT_LIMIT)
            {
                return myUserList;
            }
            // 设置用户数量上限
            int size = maxNum > 0 && bean.getUserSize() > maxNum ? maxNum : bean.getUserSize();
            int pageNum = size / limit;
            int remainder = size % limit;
            if (remainder > 0)
            {
                pageNum++;
            }
            for (int i = 2; i <= pageNum; i++)
            {
                MobilearkBean mb = getUserForPage(reqMap, i, limit);
                myUserList.addAll(convertUser(mb.getUserInfos(), SPLIT_TYPE_GETUSERS));
            }
            return myUserList;
        }
        catch (Exception e)
        {
            LOGGER.error("获取成员失败：{}", e);
        }
        return null;
    }

    /**
     * 分页获取用户数据
     * 
     * @param reqMap
     * @param startPage
     * @param limit
     * @return
     * @throws Exception
     */
    public MobilearkBean getUserForPage(Map<String, Object> reqMap, int startPage, int limit) throws Exception
    {
        reqMap.put("startPage", startPage);
        reqMap.put("limit", limit);
        RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
        String result = client.requestForString("mobileark.getusers", "1.3", reqMap);
        LOGGER.debug("======获取成员第" + startPage + "页响应结果======" + result);
        JSONObject data = JSONObject.fromObject(result);
        if (data.containsKey("code"))
        {
            LOGGER.info("获取成员失败:{}", result);
            throw new Exception();
        }
        return (MobilearkBean) JsonUtil.jsonToObject(result, MobilearkBean.class);
    }

    /**
     * 用户信息转换
     * 
     * @param list
     * @return
     */
    public List<MyUser> convertUser(List<MobilearkUser> list, int type)
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
            myUser.setDeptName(subDeptName(user.getDepartment(), type));
            myUser.setDeptFullName(user.getDepartment());
            myUser.setPhoneNum(user.getPhoneNumber());
            myUser.setLoginId(user.getLoginId());
            myUser.setAvatarUrl(user.getAvatarUrl());
            myUser.setEmailAddress(user.getEmailAddress());
            myUser.setUserAttrs(user.getUserAttrs());
            reList.add(myUser);
        }
        return reList;
    }

    /**
     * 拆分部门名称
     * 
     * @param deptNames
     * @return
     */
    private String subDeptName(String deptNames, int type)
    {
        if (StringUtils.isNotEmpty(deptNames))
        {
            String[] strArr = deptNames.split(type == SPLIT_TYPE_GETUSERS ? "\\\\" : "/");
            if (strArr.length > 0)
            {
                return strArr[strArr.length - 1];
            }
        }
        return "";
    }

    /**
     * 通过登录账号精确查询用户信息
     * 
     * @param orgUuid
     * @param loginIds
     * @return
     * @throws Exception
     */
    public List<MyUser> getUserByLoginId(String orgUuid, String loginIds) throws Exception
    {
        MobilearkBean bean = new MobilearkBean();
        try
        {
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("orgUuid", orgUuid);
            reqMap.put("loginIds", loginIds);
            RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
            String result = client.requestForString("mobileark.getuser", "1.3", reqMap);
            LOGGER.debug("======获取成员响应结果======" + result);
            JSONObject data = JSONObject.fromObject(result);
            if (data.containsKey("code"))
            {
                LOGGER.info("获取成员失败:{}", result);
                return null;
            }
            bean = (MobilearkBean) JsonUtil.jsonToObject(result, MobilearkBean.class);
            List<MyUser> myUserList = convertUser(bean.getUserInfos(), SPLIT_TYPE_GETUSER);
            return myUserList;
        }
        catch (Exception e)
        {
            LOGGER.error("获取成员失败：{}", e);
        }
        return null;
    }

    /**
     * 系统消息推送(参数详情可看 mobileark 接口文档 mobileark.syspush V1.2)
     * 
     * @param ecid
     * @param userId
     * @param title
     * @param content
     * @throws RopClientException
     */
    public void pushSysMsg(String ecid, String userId, String title, String content)
        throws MplusException, RopClientException
    {
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("ecid", ecid);
        reqMap.put("loginIds", userId);
        reqMap.put("type", "1");
        reqMap.put("depUuids", "");
        reqMap.put("title", title);
        reqMap.put("content", content);
        RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
        String result = client.requestForString("mobileark.syspush", "1.2", reqMap);
        LOGGER.debug("======响应结果======" + result);
    }

    /**
     * 发送代办事件 (参数详情可看 mobileark 接口文档 mobileark.receiveevent V1.2)
     * 
     * @param ecid
     * @param userIds
     * @param title
     * @param content
     * @param scheme
     * @throws MplusException
     * @throws RopClientException
     */
    public void pushReceiveEvent(String ecid, String userIds, String title, String content, String scheme)
        throws MplusException, RopClientException
    {
        Map<String, Object> reqMap = new HashMap<String, Object>();
        PushEvent pe = new PushEvent();
        pe.setTitle(title);
        pe.setSummary(content);
        if (StringUtils.isNotEmpty(scheme))
        {
            pe.setScheme(scheme);
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

    /**
     * @param ecid
     * @param userIds
     * @param title
     * @param content
     * @param scheme
     * @param appType
     * @param param
     * @throws MplusException
     * @throws RopClientException
     * @throws UnsupportedEncodingException
     */
    public void pushReceiveEvent(String ecid, String userIds, String title, String content, String scheme,
            String appType, EventParam param)
        throws MplusException, RopClientException, UnsupportedEncodingException
    {
        Map<String, Object> reqMap = new HashMap<String, Object>();
        PushEventV13 pe = new PushEventV13();
        pe.setTitle(title);
        pe.setSummary(content);
        if (StringUtils.isNotEmpty(scheme))
        {
            pe.setScheme(scheme);
            pe.setPcscheme(scheme);
        }
        if (param != null)
        {
            pe.setParam(Base64.encodeBase64String(JsonUtil.toJson(param).getBytes("UTF-8")));
        }
        JSONArray jsonArray = JSONArray.fromObject(pe);
        reqMap.put("events", jsonArray);
        JSONObject jo = JSONObject.fromObject(reqMap);
        reqMap.put("events", jo);
        reqMap.put("ecid", ecid);
        reqMap.put("loginId", userIds);
        if (APPTYPE_NATIVEAPP.equals(appType))
        {
            reqMap.put("appid", "");
            reqMap.put("appid_ios", appId);
            reqMap.put("appid_pc", "");
        }
        else if (APPTYPE_PC.equals(appType))
        {
            reqMap.put("appid", "");
            reqMap.put("appid_ios", "");
            reqMap.put("appid_pc", appIdMng);
        }
        else
        {
            reqMap.put("appid", appId);
            reqMap.put("appid_ios", "");
            reqMap.put("appid_pc", "");
        }
        reqMap.put("appName", appName);
        reqMap.put("appType", appType);
        RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
        String result = client.requestForString("mobileark.receiveevent", "1.3", reqMap);
        LOGGER.debug("======发送代办事件响应结果======" + result);
    }

    public MobilearkBean getPersonDocs(String orgUuid, String userUuid, String folderId, String folderName)
    {
        try
        {
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("userUuid", userUuid);
            reqMap.put("orgUuid", orgUuid);
            reqMap.put("folderId", StringUtils.isEmpty(folderId) ? "-1" : folderId);
            if (StringUtils.isNotEmpty(folderName))
            {
                reqMap.put("folderNameSearch", folderName);
            }
            RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
            String result = client.requestForString("mobileark.getpersondocs", "1.0", reqMap);
            LOGGER.debug("======获取个人文档响应结果======" + result);
            JSONObject data = JSONObject.fromObject(result);
            if (data.containsKey("code"))
            {
                LOGGER.info("获取个人文档失败:{}", result);
                return null;
            }
            return (MobilearkBean) JsonUtil.jsonToObject(result, MobilearkBean.class);
        }
        catch (Exception e)
        {
            LOGGER.error("获取个人文档失败：{}", e);
        }
        return null;
    }

    public String[] getDocUrl(String documentId, String type)
    {
        try
        {
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("documentId", documentId);
            if (StringUtils.isNotEmpty(type))
                reqMap.put("type", type);
            RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
            String result = client.requestForString("mobileark.getdocurl", "1.0", reqMap);
            LOGGER.debug("======获取获取预览下载地址响应结果======" + result);
            JSONObject data = JSONObject.fromObject(result);
            if (data.containsKey("code"))
            {
                LOGGER.info("获取获取预览下载地址失败:{}", result);
                return null;
            }
            MobilearkBean bean = (MobilearkBean) JsonUtil.jsonToObject(result, MobilearkBean.class);
            return bean.getDocUrls();
        }
        catch (Exception e)
        {
            LOGGER.error("获取获取预览下载地址失败：{}", e);
        }
        return null;
    }
}
