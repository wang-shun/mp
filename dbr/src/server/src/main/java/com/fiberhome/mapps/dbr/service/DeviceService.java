package com.fiberhome.mapps.dbr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fiberhome.mapps.dbr.dao.DBRDeviceMapper;
import com.fiberhome.mapps.dbr.dao.DBRLogMapper;
import com.fiberhome.mapps.dbr.entity.DBRDevice;
import com.fiberhome.mapps.dbr.entity.DBRLog;
import com.fiberhome.mapps.dbr.entity.GetDeviceInfo;
import com.fiberhome.mapps.dbr.request.AddDeviceRequest;
import com.fiberhome.mapps.dbr.request.QueryDeviceRequest;
import com.fiberhome.mapps.dbr.request.RoleRequest;
import com.fiberhome.mapps.dbr.response.QueryDeviceResponse;
import com.fiberhome.mapps.dbr.response.RoleResponse;
import com.fiberhome.mapps.dbr.utils.ErrorCode;
import com.fiberhome.mapps.dbr.utils.LogUtil;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.utils.IDGen;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean(version = "1.0")
public class DeviceService
{
    @Autowired
    DBRDeviceMapper            deviceMapper;
    @Autowired
    DBRLogMapper               logMapper;
    protected final Logger     LOGGER                = LoggerFactory.getLogger(getClass());
    /** 未出借 */
    public static final String DEVICE_STATUS_NOTLENT = "0";
    /** 已出借 */
    public static final String DEVICE_STATUS_LENT    = "1";
    /** 借走了 */
    public static final String LOG_FLAG_BORROW       = "0";
    /** 归还了 */
    public static final String LOG_FLAG_RETURN       = "1";

    /**
     * 管理员权限类型获取
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.dbr.isadmin", group = "feedback", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public RoleResponse getRole(RoleRequest req)
    {
        RoleResponse res = new RoleResponse();
        res.setAdminFlag(SessionContext.isAdmin());
        return res;
    }

    @ServiceMethod(method = "mapps.dbr.check", group = "feedback", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public BaseResponse checkDevice(AddDeviceRequest req)
    {
        BaseResponse res = new BaseResponse();
        try
        {
            LOGGER.info("设备校验接口(mapps.dbr.check)入口,请求参数==" + LogUtil.getObjectInfo(req));
            String ecid = SessionContext.getEcId();
            String userId = SessionContext.getUserId();
            DBRDevice record = new DBRDevice();
            record.setEcid(ecid);
            record.setDeviceId(req.getDeviceId());
            // 设备存在情况
            if (deviceMapper.selectCount(record) > 0)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("deviceId", req.getDeviceId());
                map.put("ecid", ecid);
                List<GetDeviceInfo> list = deviceMapper.getDevice(map);
                if (list == null || list.size() == 0)
                {
                    ErrorCode.fail(res, ErrorCode.CODE_300002);
                    return res;
                }
                else if (list.size() > 1)
                {
                    ErrorCode.fail(res, ErrorCode.CODE_300003);
                    return res;
                }
                GetDeviceInfo deviceInfo = list.get(0);
                // 管理员
                if (userId.equals(deviceInfo.getUserId()))
                {
                    // 未出借的设备
                    if (DEVICE_STATUS_NOTLENT.equals(deviceInfo.getDeviceStatus())
                            && StringUtils.isEmpty(deviceInfo.getBorrowUserId()))
                    {
                        ErrorCode.fail(res, ErrorCode.CODE_300005);
                        return res;
                    }
                    else
                    {
                        ErrorCode.fail(res, ErrorCode.CODE_300009);
                        return res;
                    }
                }
                // 非管理员
                else if (userId.equals(deviceInfo.getBorrowUserId()))
                {
                    ErrorCode.fail(res, ErrorCode.CODE_300010);
                    return res;
                }
                else
                {
                    ErrorCode.fail(res, ErrorCode.CODE_300008);
                    return res;
                }
            }
            else
            {
                ErrorCode.fail(res, ErrorCode.CODE_300007);
            }
            LOGGER.info("设备校验成功,code={}", res.getCode());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("设备校验异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 新增设备
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.dbr.submit", group = "feedback", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public BaseResponse addDevice(AddDeviceRequest req)
    {
        BaseResponse res = new BaseResponse();
        try
        {
            LOGGER.info("新增设备接口(mapps.dbr.submit)入口,请求参数==" + LogUtil.getObjectInfo(req));
            String id = IDGen.uuid();
            String ecid = SessionContext.getEcId();
            String userId = SessionContext.getUserId();
            String userName = SessionContext.getUserName();
            DBRDevice record = new DBRDevice();
            record.setEcid(ecid);
            record.setDeviceId(req.getDeviceId());
            if (deviceMapper.selectCount(record) > 0)
            {
                res = borrowReturnDevice(req);
                return res;
            }
            record.setId(id);
            record.setUserId(userId);
            record.setUserName(userName);
            record.setDeviceName(req.getDeviceName());
            record.setCreateTime(new Date());
            record.setDeviceStatus(DEVICE_STATUS_NOTLENT);
            deviceMapper.insertSelective(record);
            res.setError_message("设备录入成功");
            res.success();
            LOGGER.info("新增设备成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("新增设备异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 设备出借归还
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.dbr.borrowreturn", group = "feedback", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public BaseResponse borrowReturnDevice(AddDeviceRequest req)
    {
        BaseResponse res = new BaseResponse();
        try
        {
            LOGGER.info("设备出借归还接口(mapps.dbr.borrowreturn)入口,请求参数==" + LogUtil.getObjectInfo(req));
            String userId = SessionContext.getUserId();
            String ecid = SessionContext.getEcId();
            String userName = SessionContext.getUserName();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("deviceId", req.getDeviceId());
            map.put("ecid", ecid);
            List<GetDeviceInfo> list = deviceMapper.getDevice(map);
            if (list == null || list.size() == 0)
            {
                ErrorCode.fail(res, ErrorCode.CODE_300002);
                return res;
            }
            else if (list.size() > 1)
            {
                ErrorCode.fail(res, ErrorCode.CODE_300003);
                return res;
            }
            GetDeviceInfo deviceInfo = list.get(0);
            DBRDevice record = new DBRDevice();
            record.setId(deviceInfo.getId());
            DBRLog rLog = new DBRLog();
            // 管理员
            if (userId.equals(deviceInfo.getUserId()))
            {
                // 未出借的设备
                if (DEVICE_STATUS_NOTLENT.equals(deviceInfo.getDeviceStatus())
                        && StringUtils.isEmpty(deviceInfo.getBorrowUserId()))
                {
                    ErrorCode.fail(res, ErrorCode.CODE_300005);
                    return res;
                }
                record.setDeviceStatus(DEVICE_STATUS_NOTLENT);
                deviceMapper.updateByPrimaryKeySelective(record);
                rLog.setId(deviceInfo.getLogId());
                rLog.setReturnTime(new Date());
                rLog.setLogFlag(LOG_FLAG_RETURN);
                logMapper.updateByPrimaryKeySelective(rLog);
                res.setError_message("设备回收成功");
                LOGGER.info("设备回收成功");
            }
            // 非管理员
            else if (userId.equals(deviceInfo.getBorrowUserId()))
            {
                ErrorCode.fail(res, ErrorCode.CODE_300010);
                return res;
            }
            else
            {
                record.setDeviceStatus(DEVICE_STATUS_LENT);
                deviceMapper.updateByPrimaryKeySelective(record);
                Date date = new Date();
                if (StringUtils.isNotEmpty(deviceInfo.getLogId()))
                {
                    rLog.setId(deviceInfo.getLogId());
                    rLog.setReturnTime(date);
                    rLog.setLogFlag(LOG_FLAG_RETURN);
                    logMapper.updateByPrimaryKeySelective(rLog);
                }
                DBRLog bLog = new DBRLog();
                bLog.setId(IDGen.uuid());
                bLog.setDeviceId(req.getDeviceId());
                bLog.setEcid(ecid);
                bLog.setDeptId(SessionContext.getDeptId());
                bLog.setDeptName(SessionContext.getDeptName());
                bLog.setUserId(userId);
                bLog.setUserName(userName);
                bLog.setBorrowTime(date);
                bLog.setLogFlag(LOG_FLAG_BORROW);
                logMapper.insert(bLog);
                res.setError_message("设备出借成功");
                LOGGER.info("设备出借成功");
            }
            res.success();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("设备出借归还接口异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 获取设备列表
     *
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.dbr.adminquery", group = "feedback", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryDeviceResponse getAdminDevice(QueryDeviceRequest req)
    {
        QueryDeviceResponse res = new QueryDeviceResponse();
        try
        {
            LOGGER.info("获取设备列表接口(mapps.dbr.query)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = initQuery(req);
            List<GetDeviceInfo> list = deviceMapper.getDevice(map);
            if (list == null)
            {
                list = new ArrayList<GetDeviceInfo>();
            }
            res.setList(list);
            res.success();
            LOGGER.info("获取设备列表成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("获取设备列表异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public Map<String, Object> initQuery(QueryDeviceRequest req) throws Exception
    {
        String deviceName = req.getDeviceName();

        Map<String, Object> map = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(deviceName))
        {
            map.put("deviceName", "%" + deviceName.trim() + "%");
        }
        map.put("ecid", SessionContext.getEcId());
        map.put("order", "deviceStatus asc,deviceName asc,createTime asc");
        return map;
    }

    /**
     * 获取设备借出信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.dbr.borrowquery", group = "feedback", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryDeviceResponse getBorrowDevice(QueryDeviceRequest req)
    {
        QueryDeviceResponse res = new QueryDeviceResponse();
        try
        {
            LOGGER.info("获取设备借出列表接口(mapps.dbr.borrowquery)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = initBorrowQuery(req);
            List<GetDeviceInfo> list = deviceMapper.getDevice(map);
            if (list == null)
            {
                list = new ArrayList<GetDeviceInfo>();
            }
            res.setList(list);
            res.success();
            LOGGER.info("获取设备列表借出成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("获取设备借出列表异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public Map<String, Object> initBorrowQuery(QueryDeviceRequest req) throws Exception
    {
        String deviceName = req.getDeviceName();

        Map<String, Object> map = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(deviceName))
        {
            map.put("deviceName", "%" + deviceName.trim() + "%");
        }
        map.put("ecid", SessionContext.getEcId());
        map.put("loginId", SessionContext.getUserId());
        map.put("myDevice", SessionContext.getUserId());
        return map;
    }
}
