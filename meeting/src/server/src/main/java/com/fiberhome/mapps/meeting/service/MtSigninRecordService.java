package com.fiberhome.mapps.meeting.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.meeting.dao.MtSigninRecordMapper;
import com.fiberhome.mapps.meeting.entity.MtSigninRecord;
import com.fiberhome.mapps.meeting.request.QueryMeetingDetailRequest;
import com.fiberhome.mapps.meeting.response.QueryMeetingDetailResponse;
import com.fiberhome.mapps.meeting.utils.ErrorCode;
import com.fiberhome.mapps.meeting.utils.LogUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean(version = "1.0")
public class MtSigninRecordService
{
    protected final Logger     LOGGER                   = LoggerFactory.getLogger(getClass());
    @Autowired
    MtSigninRecordMapper       mtSigninRecordMapper;
    @Autowired
    ThirdPartAccessService     thirdPartAccessService;

    public static final String PARTICIPANT_TYPE_INNER   = "inner";
    public static final String PARTICIPANT_TYPE_OUTER   = "outer";
    public static final String PARTICIPANT_TYPE_SERVICE = "service";
    public static final String SEPARATOR                = ",";

    @Value("${flywaydb.locations}")
	String databaseType;
    
    @ServiceMethod(method = "mapps.meeting.meeting.detailsign", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryMeetingDetailResponse getMeetingDetailSign(QueryMeetingDetailRequest req)
    {
        QueryMeetingDetailResponse res = new QueryMeetingDetailResponse();
        try
        {
            LOGGER.info("查询会议室签到详细信息接口(mapps.meeting.meeting.detailsign)入口,请求参数==" + LogUtil.getObjectInfo(req));
            if (req.getTimestamp() == 0l)
            {
                req.setTimestamp(new Date().getTime());
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("time", new Date(req.getTimestamp()));
            map.put("meetingId", req.getMeetingId());
            map.put("sequId", req.getSequId());
            map.put("databaseType", databaseType);
            // 签到信息
            PageHelper.startPage(req.getOffset(), req.getLimit());
            List<MtSigninRecord> signinRecordList = mtSigninRecordMapper.getBySequId(map);
            PageInfo<MtSigninRecord> page = new PageInfo<MtSigninRecord>(signinRecordList);
            res.setTimestamp(req.getTimestamp());
            res.setTotal(page.getTotal());
            if (signinRecordList == null)
            {
                signinRecordList = new ArrayList<MtSigninRecord>();
            }
            if (page.isIsLastPage())
            {
                res.setEndflag(1);
            }
            else
            {
                res.setEndflag(0);
            }
            String loginIds = "";
            for (int i = 0, j = 0; i < signinRecordList.size(); i++)
            {
                MtSigninRecord record = signinRecordList.get(i);
                if (PARTICIPANT_TYPE_INNER.equals(record.getPersonType()))
                {
                    if (j == 0)
                    {
                        loginIds += signinRecordList.get(i).getPersonId();
                    }
                    else
                    {
                        loginIds += "," + signinRecordList.get(i).getPersonId();
                    }
                    j++;
                }
            }
            if (loginIds.length() > 0)
            {
                List<MyUser> muList = thirdPartAccessService.getUserInfos(SessionContext.getOrgId(), loginIds);
                for (MtSigninRecord record : signinRecordList)
                {
                    if (PARTICIPANT_TYPE_INNER.equals(record.getPersonType()))
                    {
                        for (MyUser user : muList)
                        {
                            if (record.getPersonId().equals(user.getLoginId()))
                            {
                                record.setDeptName(user.getDeptName());
                                record.setPhone(user.getPhoneNum());
                            }
                        }
                    }
                    else if (PARTICIPANT_TYPE_OUTER.equals(record.getPersonType()))
                    {
                        record.setPhone(record.getPersonId());
                    }
                }
            }
            res.setSigninRecordList(signinRecordList);
            LOGGER.info("查询会议室签到详细信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询会议室签到详细信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }
}
