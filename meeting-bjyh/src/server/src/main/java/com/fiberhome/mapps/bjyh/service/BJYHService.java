package com.fiberhome.mapps.bjyh.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fiberhome.mapps.bjyh.response.LoginUserInfoResponse;
import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.meeting.dao.MrPrivilegeMapper;
import com.fiberhome.mapps.meeting.dao.MtMeetingMapper;
import com.fiberhome.mapps.meeting.entity.MrPrivilege;
import com.fiberhome.mapps.meeting.utils.ErrorCode;
import com.fiberhome.mapps.meetingroom.service.MRThirdPartAccessService;
import com.fiberhome.mapps.meetingroom.service.MrPrivilegeService;
import com.rop.AbstractRopRequest;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean(version = "1.0")
public class BJYHService
{
    protected final Logger             LOGGER = LoggerFactory.getLogger(getClass());
    @Autowired
    MtMeetingMapper                    mtMeetingMapper;
    @Autowired
    protected MRThirdPartAccessService thirdPartAccessService;
    @Autowired
    protected MrPrivilegeMapper        mrPrivilegeMapper;

    /**
     * 客户端首页加载时，初始化登录用户个人信息与管理员身份标识
     * 
     * @return
     */
    @ServiceMethod(method = "mapps.bjyh.client.userinfo", group = "bjyhclient", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public LoginUserInfoResponse initLoginUserInfoFromClient(AbstractRopRequest req)
    {
        LoginUserInfoResponse res = new LoginUserInfoResponse();
        try
        {
            LOGGER.info("获取个人信息接口(mapps.bjyh.client.userinfo)入口");
            MyUser myUser = thirdPartAccessService.getUserInfo(SessionContext.getOrgId(), SessionContext.getUserId());
            res.setMyUser(myUser);
            res.setAdminFlag(0);
            MrPrivilege mp = new MrPrivilege();
            mp.setEcid(SessionContext.getEcId());
            mp.setEntityId(SessionContext.getUserId());
            mp.setPriv(MrPrivilegeService.PRIV_ADMIN);
            if (mrPrivilegeMapper.selectCount(mp) > 0)
            {
                res.setAdminFlag(1);
            }
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
