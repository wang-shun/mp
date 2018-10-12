package com.fiberhome.mapps.activity.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.fiberhome.mapps.activity.request.CreateImGroupRequest;
import com.fiberhome.mapps.activity.request.JoinImGroupRequest;
import com.fiberhome.mapps.activity.response.CreateImGroupResponse;
import com.fiberhome.mapps.activity.response.JoinImGroupResponse;
import com.fiberhome.mapps.contact.pojo.MosUserInfoViewBean;
import com.fiberhome.mapps.contact.utils.JsonUtil;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.utils.CreateImGroupException;
import com.fiberhome.mapps.utils.ErrorCode;
import com.fiberhome.mapps.utils.JoinImGroupException;
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

    @Value("${mplus.imsg.serviceUrl}")
    String                 imsgWebRoot;
    @Value("${mplus.contacts.serviceUrl}")
    String                 contactsWebRoot;
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
            LOGGER.debug("创建群组接口调用,serviceUrl:{},appKey:{},appSecret:{},ecid:{},username:{},groupname:{}", imsgWebRoot,
                    appKey, appSecret, SessionContext.getEcId(), SessionContext.getUserId(), request.getGroupName());
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("ecid", SessionContext.getEcId());
            reqMap.put("username", SessionContext.getUserId());
            reqMap.put("groupname", request.getGroupName());
            RopClient client = new RopClient(imsgWebRoot, appKey, appSecret, "json");
            String result = client.requestForString("mos.thirdpart.im.group.add", "1.1", reqMap);
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
                    imsgWebRoot, appKey, appSecret, SessionContext.getEcId(), SessionContext.getUserId(),
                    request.getGroupId(), request.getMembers());
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("ecid", SessionContext.getEcId());
            reqMap.put("username", request.getUsername());
            reqMap.put("groupid", request.getGroupId());
            reqMap.put("members", request.getMembers());
            RopClient client = new RopClient(imsgWebRoot, appKey, appSecret, "json");
            String result = client.requestForString("mos.thirdpart.im.group.joingroup", "1.0", reqMap);
            LOGGER.debug("======群组加入成员响应结果======" + result);
            JSONObject data = JSONObject.fromObject(result);
            if (data.containsKey("code"))
            {
                String code = data.get("code").toString();
                if ("1".equals(code))
                {
                    response.success();
                }
                else
                {
                    response.setCode(code);
                    return response;
                }
            }
            else
            {
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

    public String queryImAccount(String ecid, String loginId)
    {
        try
        {
            LOGGER.debug("用户在线查看接口调用,serviceUrl:{},appKey:{},appSecret:{},ecid:{},userid:{}", imsgWebRoot, appKey,
                    appSecret, SessionContext.getEcId(), SessionContext.getUserId());
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("ecid", ecid);
            reqMap.put("loginid", loginId);
            RopClient client = new RopClient(contactsWebRoot, appKey, appSecret, "json");
            String result = client.requestForString("mos.thirdpart.user.online.view", "1.0", reqMap);
            LOGGER.debug("======用户在线查看接口响应结果======" + result);
            JSONObject data = JSONObject.fromObject(result);
            if (data.containsKey("code") && "1".equals(data.get("code")))
            {
                MosUserInfoViewBean bean = (MosUserInfoViewBean) JsonUtil.jsonToObject(result,
                        MosUserInfoViewBean.class);
                if (bean != null && bean.getList() != null && bean.getList().size() == 1)
                    return bean.getList().get(0).getImaccount();
            }
        }
        catch (Exception e)
        {
            LOGGER.error("用户在线查看接口失败：{}", e);
        }
        return null;
    }
}
