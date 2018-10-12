package com.fiberhome.mapps.meetingroom.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

import com.fiberhome.mapps.contact.pojo.EventParam;
import com.fiberhome.mapps.contact.pojo.MyDepartment;
import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.contact.service.MplusAccessService;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.meetingroom.request.GetDepartmentsRequest;
import com.fiberhome.mapps.meetingroom.request.GetUsersRequest;
import com.fiberhome.mapps.meetingroom.response.GetDepartmentsResponse;
import com.fiberhome.mapps.meetingroom.response.GetUsersResponse;
import com.fiberhome.mapps.meetingroom.utils.ErrorCode;
import com.fiberhome.mapps.meetingroom.utils.LogUtil;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

import tk.mybatis.mapper.util.StringUtil;

@ServiceMethodBean
public class ThirdPartAccessService
{
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Autowired
    MplusAccessService     accessService;

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
                    if (md.getDepOrder().substring(0, 4).equals(SessionContext.getOrgDeptOrder()))
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

    @Async
    public void pushSysMsg(String ecid, String userId, String title, String content)
    {
        LOGGER.debug("系统消息推送,ecid={},userId={},title={},content={}", ecid, userId, title, content);
        try
        {
            accessService.pushSysMsg(ecid, userId, title, content);
        }
        catch (Exception e)
        {
            LOGGER.error("系统消息发送失败：{}", e);
        }
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

    public boolean pushReceiveEvent(String ecid, String userIds, String title, String content, String reservedId,
            EventParam param)
    {
        LOGGER.debug("发送代办事件,ecid={},userId={},title={},content={},reservedId={}", ecid, userIds, title, content,
                reservedId);
        try
        {
            String scheme = "";
            if (StringUtil.isNotEmpty(reservedId))
            {
                scheme = "result.html?id=" + reservedId;
            }
            // accessService.pushReceiveEvent(ecid, userIds, title, content, scheme);
            accessService.pushReceiveEvent(ecid, userIds, title, content, scheme, MplusAccessService.APPTYPE_HTML5,
                    param);
        }
        catch (Exception e)
        {
            LOGGER.error("发送代办事件失败：{}", e);
            return false;
        }
        return true;
    }

    @Value("${meetingroom.routePath}")
    String  routePath;
    @Value("${meetingroom.pushToPc}")
    boolean pcFlag;

    public boolean pushReceiveEventToPc(String ecid, String userIds, String title, String content, String reservedId,
            EventParam param)
    {
        if (!pcFlag)
        {
            LOGGER.info("未开启对pc的推送功能");
        }
        LOGGER.debug("发送代办事件给pc,ecid={},userId={},title={},content={},reservedId={}", ecid, userIds, title, content,
                reservedId);
        try
        {
            String path = StringUtils.isEmpty(routePath) ? "" : routePath;
            String scheme = path + "/websso?sessionId=${token}&clienttype=p#menu/2";
            if (StringUtil.isNotEmpty(reservedId))
            {
                scheme = path + "/websso?sessionId=${token}&clienttype=p#menu/2";
            }
            LOGGER.debug("scheme：{}", scheme);
            accessService.pushReceiveEvent(ecid, userIds, title, content, scheme, MplusAccessService.APPTYPE_PC, param);
        }
        catch (Exception e)
        {
            LOGGER.error("发送代办事件失败：{}", e);
            return false;
        }
        return true;
    }
}
