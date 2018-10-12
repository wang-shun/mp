package com.fiberhome.mapps.meeting.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.meeting.request.CreateImGroupRequest;
import com.fiberhome.mapps.meeting.request.JoinImGroupRequest;
import com.fiberhome.mapps.meeting.response.CreateImGroupResponse;
import com.fiberhome.mapps.meeting.response.JoinImGroupResponse;
import com.fiberhome.mapps.meeting.utils.CreateImGroupException;
import com.fiberhome.mapps.meeting.utils.ErrorCode;
import com.fiberhome.mapps.meeting.utils.JoinImGroupException;
import com.fiberhome.mos.core.openapi.rop.client.RopClient;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

import net.sf.json.JSONObject;

@ServiceMethodBean
public class ThirdPartAccessMosService
{
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Value("${mplus.mos.serviceUrl}")
    String                 webRoot;
    @Value("${mplus.sso.appKey}")
    String                 appKey;
    @Value("${mplus.sso.secret}")
    String                 appSecret;

    @ServiceMethod(method = "mapps.thirdpart.mobileark.createImGroup", group = "thirdpart", groupTitle = "文件服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public CreateImGroupResponse createImGroup(CreateImGroupRequest request) throws CreateImGroupException
    {
        CreateImGroupResponse response = new CreateImGroupResponse();
        try
        {
            LOGGER.debug("创建群组接口调用,serviceUrl:{},appKey:{},appSecret:{},ecid:{},username:{},groupname:{}", webRoot,
                    appKey, appSecret, SessionContext.getEcId(), SessionContext.getUserId(), request.getGroupName());
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("ecid", SessionContext.getEcId());
            reqMap.put("username", SessionContext.getUserId());
            reqMap.put("groupname", request.getGroupName());
            RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
            String result = client.requestForString("mos.thirdpart.im.group.add", "1.0", reqMap);
            LOGGER.debug("======创建群组响应结果======" + result);
            JSONObject data = JSONObject.fromObject(result);
            if (data.containsKey("code") && "1".equals(data.get("code")))
            {
                response.setGroupId(data.get("groupId").toString());
            }
            else
            {
                LOGGER.info("创建群组失败:{}", result);
                ErrorCode.fail(response, ErrorCode.CODE_100001);
                return response;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("创建群组失败：{}", e);
            ErrorCode.fail(response, ErrorCode.CODE_100001);
            throw new CreateImGroupException();
        }
        return response;
    }

    @ServiceMethod(method = "mapps.thirdpart.mobileark.joingroup", group = "thirdpart", groupTitle = "文件服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public JoinImGroupResponse joinImGroup(JoinImGroupRequest request) throws JoinImGroupException
    {
        JoinImGroupResponse response = new JoinImGroupResponse();
        try
        {
            LOGGER.debug("创建群组接口调用,serviceUrl:{},appKey:{},appSecret:{},ecid:{},username:{},GroupId:{},Members:{}",
                    webRoot, appKey, appSecret, SessionContext.getEcId(), SessionContext.getUserId(),
                    request.getGroupId(), request.getMembers());
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("ecid", SessionContext.getEcId());
            reqMap.put("username", SessionContext.getUserId());
            reqMap.put("groupid", request.getGroupId());
            reqMap.put("members", request.getMembers());
            RopClient client = new RopClient(webRoot, appKey, appSecret, "json");
            String result = client.requestForString("mos.thirdpart.im.group.joingroup", "1.0", reqMap);
            LOGGER.debug("======群组加入成员响应结果======" + result);
            JSONObject data = JSONObject.fromObject(result);
            if (data.containsKey("code") && "1".equals(data.get("code")))
            {
                response.success();
            }
            else
            {
                LOGGER.info("群组加入成员失败:{}", result);
                ErrorCode.fail(response, ErrorCode.CODE_100001);
                return response;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("群组加入成员失败：{}", e);
            ErrorCode.fail(response, ErrorCode.CODE_100001);
            throw new JoinImGroupException();
        }
        return response;
    }

}
