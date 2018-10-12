package com.fiberhome.mapps.meetingroom.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.meeting.dao.MrPrivilegeMapper;
import com.fiberhome.mapps.meeting.entity.MrPrivilege;
import com.fiberhome.mapps.meetingroom.request.AddPrivilegeRequest;
import com.fiberhome.mapps.meetingroom.request.QueryPrivilegeRequest;
import com.fiberhome.mapps.meetingroom.response.AddPrivilegeResponse;
import com.fiberhome.mapps.meetingroom.response.QueryPrivilegeResponse;
import com.fiberhome.mapps.meetingroom.utils.ErrorCode;
import com.fiberhome.mapps.meetingroom.utils.JsonUtil;
import com.fiberhome.mapps.meetingroom.utils.LogUtil;
import com.fiberhome.mapps.utils.IDGen;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean(version = "1.0")
public class MrPrivilegeService
{
    @Autowired
    MrPrivilegeMapper          mrPrivilegeMapper;
    protected final Logger     LOGGER           = LoggerFactory.getLogger(getClass());
    /** 管理员 */
    public static final String PRIV_ADMIN       = "admin";
    /** 服务人员 */
    public static final String PRIV_SERVICE     = "service";
    /** 普通用户 */
    public static final String PRIV_USER        = "user";

    public static final int    PRIV_ADMIN_MAX   = 3;
    public static final int    PRIV_SERVICE_MAX = 5;

    /**
     * 新增可见性权限信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.privilege.add", group = "privilege", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional
    public AddPrivilegeResponse addMrPrivilege(AddPrivilegeRequest req)
    {
        AddPrivilegeResponse res = new AddPrivilegeResponse();
        try
        {
            LOGGER.info("新增权限接口(mapps.meetingroom.privilege.add)入口,请求参数==" + LogUtil.getObjectInfo(req));
            String roomId = req.getRoomId();
            QueryPrivilegeResponse privData = (QueryPrivilegeResponse) JsonUtil.jsonToObject(req.getJsonData(),
                    QueryPrivilegeResponse.class);
            MrPrivilege deletePriv = new MrPrivilege();
            deletePriv.setRoomId(roomId);
            deletePriv.setPriv(PRIV_USER);
            mrPrivilegeMapper.delete(deletePriv);
            for (MrPrivilege entity : privData.getList())
            {
                String id = IDGen.uuid();
                entity.setId(id);
                entity.setEcid(SessionContext.getEcId());
                entity.setRoomId(roomId);
                entity.setAuthrTime(new Date());
                entity.setPriv(PRIV_USER);
                mrPrivilegeMapper.insertSelective(entity);

            }
            res.success();
            LOGGER.info("新增权限成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("新增权限信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 获取可见性权限信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.privilege.query", group = "privilege", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryPrivilegeResponse getMrPrivilege(QueryPrivilegeRequest req)
    {
        QueryPrivilegeResponse res = new QueryPrivilegeResponse();
        try
        {
            LOGGER.debug("获取权限信息接口(mapps.meetingroom.privilege.query)入口,请求参数==" + LogUtil.getObjectInfo(req));
            MrPrivilege mf = new MrPrivilege();
            mf.setRoomId(req.getRoomId());
            mf.setPriv(PRIV_USER);
            List<MrPrivilege> list = mrPrivilegeMapper.select(mf);
            res.setList(list);
            res.success();
            LOGGER.info("获取权限信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("获取权限信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 获取审批服务权限信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.privilege.queryadmin", group = "privilege", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryPrivilegeResponse getAdminPriv(QueryPrivilegeRequest req)
    {
        QueryPrivilegeResponse res = new QueryPrivilegeResponse();
        try
        {
            LOGGER.debug("获取权限信息接口(mapps.meetingroom.privilege.query)入口,请求参数==" + LogUtil.getObjectInfo(req));
            MrPrivilege mf = new MrPrivilege();
            mf.setRoomId(req.getRoomId());
            mf.setPriv(PRIV_ADMIN);
            List<MrPrivilege> adminList = mrPrivilegeMapper.select(mf);
            mf.setPriv(PRIV_SERVICE);
            List<MrPrivilege> serviceList = mrPrivilegeMapper.select(mf);
            res.setAdminList(adminList);
            res.setServiceList(serviceList);
            res.success();
            LOGGER.info("获取审批服务权限信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("获取审批服务权限信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 新增审批服务权限信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.privilege.addadmin", group = "privilege", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional
    public AddPrivilegeResponse addAdminPriv(AddPrivilegeRequest req)
    {
        AddPrivilegeResponse res = new AddPrivilegeResponse();
        try
        {
            LOGGER.info("新增审批服务权限接口(mapps.meetingroom.privilege.add)入口,请求参数==" + LogUtil.getObjectInfo(req));
            String roomId = req.getRoomId();
            QueryPrivilegeResponse privData = (QueryPrivilegeResponse) JsonUtil.jsonToObject(req.getJsonData(),
                    QueryPrivilegeResponse.class);
            if (privData.getAdminList() != null && privData.getAdminList().size() > PRIV_ADMIN_MAX)
            {
                res.fail("管理员设置超上限");
                return res;
            }
            else if (privData.getServiceList() != null && privData.getServiceList().size() > PRIV_SERVICE_MAX)
            {
                res.fail("服务人员设置超上限");
                return res;
            }
            batchInsertPriv(privData.getAdminList(), roomId, PRIV_ADMIN);
            batchInsertPriv(privData.getServiceList(), roomId, PRIV_SERVICE);
            res.success();
            LOGGER.info("新增审批服务权限成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("新增审批服务权限信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    private void batchInsertPriv(List<MrPrivilege> list, String roomId, String priv) throws Exception
    {
        MrPrivilege deletePriv = new MrPrivilege();
        deletePriv.setRoomId(roomId);
        deletePriv.setPriv(priv);
        mrPrivilegeMapper.delete(deletePriv);
        for (MrPrivilege entity : list)
        {
            String id = IDGen.uuid();
            entity.setId(id);
            entity.setEcid(SessionContext.getEcId());
            entity.setRoomId(roomId);
            entity.setAuthrTime(new Date());
            entity.setPriv(priv);
            mrPrivilegeMapper.insertSelective(entity);
        }
    }
}
