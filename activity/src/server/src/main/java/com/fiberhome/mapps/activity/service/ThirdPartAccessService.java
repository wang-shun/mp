package com.fiberhome.mapps.activity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fiberhome.mapps.activity.request.GetDepartmentsRequest;
import com.fiberhome.mapps.activity.request.GetUsersRequest;
import com.fiberhome.mapps.activity.response.GetDepartmentsResponse;
import com.fiberhome.mapps.activity.response.GetUsersResponse;
import com.fiberhome.mapps.contact.pojo.MyDepartment;
import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.contact.service.MplusAccessService;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.utils.ErrorCode;
import com.fiberhome.mapps.utils.LogUtil;
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
            response.setUserInfos(accessService.getUsersByName(SessionContext.getOrgId(), userName, 20));
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

    public boolean pushReceiveEvent(String ecid, String userIds, String title, String content, String reservedId)
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
            accessService.pushReceiveEvent(ecid, userIds, title, content, scheme);
        }
        catch (Exception e)
        {
            LOGGER.error("发送代办事件失败：{}", e);
            return false;
        }
        return true;
    }

}
