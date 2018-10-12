package com.fiberhome.mapps.meeting.service;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.fiberhome.mapps.meeting.dao.TmpReservedMapper;
import com.fiberhome.mapps.meeting.entity.TmpReserved;
import com.fiberhome.mapps.meeting.request.GetLockReservedTimeRequest;
import com.fiberhome.mapps.meeting.response.GetLockReservedResponse;
import com.fiberhome.mapps.meeting.utils.DateUtil;
import com.fiberhome.mapps.meetingroom.utils.ErrorCode;
import com.fiberhome.mapps.utils.IDGen;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean(version = "1.0")
public class TmpReservedService
{
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Autowired
    TmpReservedMapper      tmpReservedMapper;

    @Value("${meetingroom.scheduled.on-off}")
    Boolean                scheduledFlag;

    @ServiceMethod(method = "mapps.meeting.reserved.lock", group = "meeting", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetLockReservedResponse getLockReserved(GetLockReservedTimeRequest req)
    {
        GetLockReservedResponse res = new GetLockReservedResponse();
        try
        {
            String roomId = req.getRoomId();
            Date timeStart = DateUtil.convertToTime(req.getTimeStart());
            Date timeEnd = DateUtil.convertToTime(req.getTimeEnd());
            TmpReserved entity = new TmpReserved();
            entity.setRoomId(roomId);
            entity.setTimeBegin(timeStart);
            entity.setTimeEnd(timeEnd);
            int i = tmpReservedMapper.checkRoomReserved(entity);
            if (i > 0)
            {
                ErrorCode.fail(res, ErrorCode.CODE_300005);
                LOGGER.info("错误码:{}={}", ErrorCode.CODE_300005, ErrorCode.errorMap.get(ErrorCode.CODE_300005));
                return res;
            }
            entity.setId(IDGen.uuid());
            entity.setCreateTime(new Date());
            tmpReservedMapper.insert(entity);
            res.setLockId(entity.getId());
        }
        catch (Exception e)
        {
            ErrorCode.fail(res, ErrorCode.CODE_100001);
            LOGGER.error("预约时间校验异常：{}", e);
        }
        return res;
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void clearLock()
    {
        if (!scheduledFlag)
        {
            return;
        }
        LOGGER.debug("预约临时表记录清理开始");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, -10);
        TmpReserved entity = new TmpReserved();
        entity.setCreateTime(calendar.getTime());
        tmpReservedMapper.clearLock(entity);
    }
}
