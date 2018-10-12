package com.fiberhome.mapps.meeting.service.bjyh;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.meeting.dao.MrPrivilegeMapper;
import com.fiberhome.mapps.meeting.dao.MtMeetingMapper;
import com.fiberhome.mapps.meeting.entity.ClientMeetingInfo;
import com.fiberhome.mapps.meeting.entity.MrPrivilege;
import com.fiberhome.mapps.meeting.request.RoleRequest;
import com.fiberhome.mapps.meeting.request.bjyh.QueryMeetingRequest;
import com.fiberhome.mapps.meeting.response.QueryMeetingResponse;
import com.fiberhome.mapps.meeting.response.RoleResponse;
import com.fiberhome.mapps.meeting.utils.ErrorCode;
import com.fiberhome.mapps.meeting.utils.LogUtil;
import com.fiberhome.mapps.meetingroom.service.MRThirdPartAccessService;
import com.fiberhome.mapps.meetingroom.service.MrPrivilegeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean(version = "1.0")
public class MtMeetingServiceV10
{
    protected final Logger             LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    MtMeetingMapper                    mtMeetingMapper;
    @Autowired
    protected MRThirdPartAccessService thirdPartAccessService;
    @Autowired
    protected MrPrivilegeMapper        mrPrivilegeMapper;

    /**
     * 管理员权限类型获取
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meeting.meeting.role", group = "meeting", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public RoleResponse getRole(RoleRequest req)
    {
        RoleResponse res = new RoleResponse();
        res.setAdminFlag(SessionContext.isAdmin());
        return res;
    }

    /**
     * 客户端会议列表查询
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meeting.meeting.clientquery", group = "meeting", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryMeetingResponse getMeetingFromClient(QueryMeetingRequest req)
    {
        QueryMeetingResponse res = new QueryMeetingResponse();
        try
        {
            LOGGER.info("分页查询会议接口(mapps.meeting.meeting.clientquery)入口,请求参数==" + LogUtil.getObjectInfo(req));
            if (req.getTimestamp() == 0l)
            {
                req.setTimestamp(new Date().getTime());
            }
            Map<String, Object> map = initQuery(req);
            PageHelper.startPage(req.getOffset(), req.getLimit());
            List<ClientMeetingInfo> list = mtMeetingMapper.getBJYHMeeting(map);
            PageInfo<ClientMeetingInfo> page = new PageInfo<ClientMeetingInfo>(list);
            res.setTotal(page.getTotal());
            if (list == null)
            {
                list = new ArrayList<ClientMeetingInfo>();
            }
            res.setMeetingList(list);
            res.setTimestamp(req.getTimestamp());
            if (page.isIsLastPage())
            {
                res.setEndflag(1);
            }
            else
            {
                res.setEndflag(0);
            }
            if ("1".equals(req.getLoginUserFlag()))
            {
                MyUser myUser = thirdPartAccessService.getUserInfo(SessionContext.getOrgId(),
                        SessionContext.getUserId());
                res.setMyUser(myUser);
            }
            res.setAdminFlag(0);
            MrPrivilege mp = new MrPrivilege();
            mp.setEcid(SessionContext.getEcId());
            mp.setEntityId(SessionContext.getUserId());
            mp.setPriv(MrPrivilegeService.PRIV_ADMIN);
            if (mrPrivilegeMapper.selectCount(mp) > 0)
            {
                res.setAdminFlag(1);
            }
            res.success();
            LOGGER.info("分页查询会议成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("分页查询会议信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public Map<String, Object> initQuery(QueryMeetingRequest req) throws Exception
    {
        String meetingName = req.getMeetingName();
        String selectDate = req.getSelectDate();
        Date time = new Date(req.getTimestamp());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("time", time);
        map.put("ecid", SessionContext.getEcId());
        map.put("userId", SessionContext.getUserId());
        map.put("now", new Date());
        if (StringUtil.isNotEmpty(meetingName))
        {
            map.put("likeName", "%" + meetingName + "%");
        }
        if (StringUtil.isNotEmpty(selectDate))
        {
            map.put("selectDate", selectDate);
        }
        return map;
    }

}
