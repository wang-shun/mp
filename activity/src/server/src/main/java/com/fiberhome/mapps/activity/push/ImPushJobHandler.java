package com.fiberhome.mapps.activity.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fiberhome.mapps.activity.entity.AtPrivilege;
import com.fiberhome.mapps.activity.request.AddActivityRequest;
import com.fiberhome.mapps.contact.pojo.EventParam;
import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.contact.service.MplusAccessService;
import com.fiberhome.mapps.redismq.Job;
import com.fiberhome.mapps.redismq.JobHandler;
import com.fiberhome.mapps.utils.JsonUtil;

import tk.mybatis.mapper.util.StringUtil;

@Component
public class ImPushJobHandler implements JobHandler
{
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    MplusAccessService     accessService;

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public boolean handle(String jobId, Job job)
    {
        LOGGER.debug("开始执行Job推送代办");
        String jsonStr = job.getDetail();
        LOGGER.info(jsonStr);
        Map<String, Object> jobMap = JsonUtil.getMapFromJsonStr(jsonStr);
        String ecid = (String) jobMap.get("ecid");
        String title = (String) jobMap.get("title");
        String conTel = (String) jobMap.get("conTel");
        String address = (String) jobMap.get("address");
        String startTime = (String) jobMap.get("startTime");
        String endTime = (String) jobMap.get("endTime");
        String createName = (String) jobMap.get("createName");
        String actId = (String) jobMap.get("actId");
        String enterEndTime = (String) jobMap.get("enterEndTime");
        String content = (String) jobMap.get("content");
        String orgId = (String) jobMap.get("orgId");
        String priJson = (String) jobMap.get("priJson");
        EventParam param = new EventParam();
        List<String> list = new ArrayList<String>();
        list.add("发起者：" + createName);
        list.add("开始时间：" + startTime);
        list.add("结束时间：" + endTime);
        list.add("报名截止：" + enterEndTime);
        list.add("地址：" + address);
        list.add("联系电话：" + conTel);
        param.setSummaryextend(list);

        List<String> userIdList = this.getUserIds(priJson, orgId);

        int total = userIdList.size();
        int pageSize = 100;
        int times = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        LOGGER.info("分批推送待办共" + times + "批--------");
        for (int i = 0; i < times; i++)
        {
            int sidx = i * pageSize;
            int eidx = (i + 1) * pageSize;
            if (eidx > total)
            {
                eidx = total;
            }
            List<String> subUserIdList = userIdList.subList(sidx, eidx);
            LOGGER.info("第" + i + "批推送----" + sidx + "----" + eidx);
            StringBuffer subUserIds = new StringBuffer();
            for (String userId : subUserIdList)
            {
                subUserIds = subUserIds.append(userId).append(",");
            }
            String userIds = subUserIds.substring(0, subUserIds.length() - 1);
            pushReceiveEvent(ecid, userIds, title, content, actId, param);
        }

        return true;

    }

    /**
     * 获取所有userId集合
     */
    public List<String> getUserIds(String priStr, String orgId)
    {
        List<String> userIds = new ArrayList<>();
        AddActivityRequest reqPriv = (AddActivityRequest) JsonUtil.jsonToObject(priStr, AddActivityRequest.class);
        List<AtPrivilege> Privileges = reqPriv.getPrivileges();

        if (Privileges.size() < 1)
        {
            List<MyUser> userInfo;
            try
            {
                Map<String, Object> reqMap = new HashMap<String, Object>();
                reqMap.put("orgUuid", orgId);
                reqMap.put("startPage", "1");
                userInfo = accessService.getUsers(reqMap, 0);
                if (userInfo.size() > 0)
                {
                    for (MyUser myUser : userInfo)
                    {
                        userIds.add(myUser.getLoginId());
                    }
                }

            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LOGGER.debug("获取部门人员失败 ");
            }
        }
        else
        {
            for (AtPrivilege atPrivilege : Privileges)
            {
                if ("user".equals(atPrivilege.getType()))
                {
                    userIds.add(atPrivilege.getEntityId());
                }
                else
                {
                    try
                    {
                        List<MyUser> userInfo = accessService.getUsersByDept(orgId, atPrivilege.getEntityId(), "1", 0);
                        if (userInfo.size() > 0)
                        {
                            for (MyUser user : userInfo)
                            {
                                userIds.add(user.getLoginId());
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        LOGGER.debug("获取部门人员失败 ");
                    }
                }

            }
        }
        return userIds;
    }

    public boolean pushReceiveEvent(String ecid, String userIds, String title, String content, String actId,
            EventParam param)
    {
        LOGGER.debug("发送代办事件,ecid={},userId={},title={},content={}", ecid, userIds, title, content);
        try
        {
            String scheme = "";
            if (StringUtil.isNotEmpty(actId))
            {
                scheme = "#ID-PageDetail/" + actId;
            }
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

}
