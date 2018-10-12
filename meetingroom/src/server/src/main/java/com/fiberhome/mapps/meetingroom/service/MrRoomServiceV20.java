package com.fiberhome.mapps.meetingroom.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.meetingroom.entity.GetMrRoom;
import com.fiberhome.mapps.meetingroom.entity.MrPrivilege;
import com.fiberhome.mapps.meetingroom.entity.ReservedDate;
import com.fiberhome.mapps.meetingroom.entity.ReservedTime;
import com.fiberhome.mapps.meetingroom.request.QueryRoomDetailRequest;
import com.fiberhome.mapps.meetingroom.request.QueryRoomFromWebRequest;
import com.fiberhome.mapps.meetingroom.response.QueryRoomDetailResponse;
import com.fiberhome.mapps.meetingroom.response.QueryRoomResponse;
import com.fiberhome.mapps.meetingroom.utils.ErrorCode;
import com.fiberhome.mapps.meetingroom.utils.LogUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

import tk.mybatis.mapper.util.StringUtil;

@ServiceMethodBean(version = "1.0")
public class MrRoomServiceV20 extends MrRoomService
{
    @Autowired
    ThirdPartAccessService thirdPartAccessService;

    @Value("${flywaydb.locations}")
	String databaseType;
    
    /**
     * 分页获取会议室信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.room.queryweb", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryRoomResponse getMrRoomForPage(QueryRoomFromWebRequest req)
    {
        QueryRoomResponse res = new QueryRoomResponse();
        try
        {
            LOGGER.info("分页查询会议室接口(mapps.meetingroom.room.queryweb)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = initQuery(req);
            PageHelper.startPage(req.getOffset(), req.getLimit());
            List<GetMrRoom> list = mrRoomMapper.getMrRoomFormWeb(map);
            PageInfo<GetMrRoom> page = new PageInfo<GetMrRoom>(list);
            res.setTotal(page.getTotal());
            if (list == null)
            {
                list = new ArrayList<GetMrRoom>();
            }
            else
            {
                for (GetMrRoom info : list)
                {
                    if (StringUtil.isNotEmpty(info.getLayout()))
                        info.setLayout(fileService.getWebRoot() + info.getLayout());
                }
            }
            res.setGetMrRoomList(list);
            res.success();
            LOGGER.info("分页查询会议室成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("分页查询会议室信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public Map<String, Object> initQuery(QueryRoomFromWebRequest req) throws Exception
    {
        String roomName = req.getRoomName();
        String address = req.getAddress();
        String projector = req.getProjector();
        String display = req.getDisplay();
        String microphone = req.getMicrophone();
        String stereo = req.getStereo();
        String wifi = req.getWifi();
        String sort = req.getSort();
        String roomType = req.getRoomType();

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("userId", SessionContext.getUserId());
        map.put("ecid", SessionContext.getEcId());
        if (StringUtil.isNotEmpty(roomName))
        {
            map.put("name", "%" + roomName.trim() + "%");
        }
        if (StringUtil.isNotEmpty(address))
        {
            map.put("address", "%" + address.trim() + "%");
        }
        if (StringUtil.isNotEmpty(projector))
        {
            map.put("projector", projector);
        }
        if (StringUtil.isNotEmpty(display))
        {
            map.put("display", display);
        }
        if (StringUtil.isNotEmpty(microphone))
        {
            map.put("microphone", microphone);
        }
        if (StringUtil.isNotEmpty(stereo))
        {
            map.put("stereo", stereo);
        }
        if (StringUtil.isNotEmpty(wifi))
        {
            map.put("wifi", wifi);
        }
        if (StringUtil.isNotEmpty(roomType))
        {
            map.put("roomType", roomType);
        }

        if (StringUtil.isNotEmpty(sort))
            map.put("order", sort);
        else
            map.put("order", "create_time desc");

        return map;
    }

    /**
     * 获取会议室详细信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.room.detail", group = "room", groupTitle = "API", version = "2.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryRoomDetailResponse getMrRoomDetail(QueryRoomDetailRequest req)
    {
        QueryRoomDetailResponse res = new QueryRoomDetailResponse();
        try
        {
            LOGGER.info("查询会议室详细信息接口(mapps.meetingroom.room.detail)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("roomId", req.getRoomId());
            map.put("ecid", SessionContext.getEcId());
            map.put("userId", SessionContext.getUserId());
            map.put("databaseType", databaseType);
            List<GetMrRoom> list = mrRoomMapper.getMrRoom(map);
            List<ReservedDate> dList = mrReservedMapper.getReservedDatesV20(map);
            for (ReservedDate date : dList)
            {
                map.put("reservedDate", date.getReservedDate());
                List<ReservedTime> tList = mrReservedMapper.getReservedTimesV20(map);
                date.setReservedTimeList(tList);
            }
            setDetailResponse(res, list, dList);
            res.setUserName(SessionContext.getUserName());
            MyUser muInfo = thirdPartAccessService.getUserInfo(SessionContext.getOrgId(), SessionContext.getUserId());
            if (muInfo != null)
            {
                res.setUserPhone(muInfo.getPhoneNum());
            }
            // 是否是需要审批的会议室
            res.setNeedApprove("0");
            MrPrivilege mp = new MrPrivilege();
            mp.setRoomId(req.getRoomId());
            mp.setPriv(MrPrivilegeService.PRIV_ADMIN);
            if (mrPrivilegeMapper.selectCount(mp) > 0)
            {
                res.setNeedApprove("1");
            }
            mp.setEcid(SessionContext.getEcId());
            mp.setPriv(MrPrivilegeService.PRIV_ADMIN);
            res.setAdminName(getUserName(mrPrivilegeMapper.select(mp)));
            mp.setPriv(MrPrivilegeService.PRIV_SERVICE);
            res.setServiceName(getUserName(mrPrivilegeMapper.select(mp)));
            LOGGER.info("查询会议室详细信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询会议室详细信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    private String getUserName(List<MrPrivilege> mpList)
    {
        List<String> list = new ArrayList<String>();
        for (MrPrivilege mp : mpList)
        {
            list.add(mp.getEntityName());
        }
        return StringUtils.join(list, ",");
    }
}
