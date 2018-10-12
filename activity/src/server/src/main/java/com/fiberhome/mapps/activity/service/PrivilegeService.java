package com.fiberhome.mapps.activity.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fiberhome.mapps.activity.dao.AtPrivilegeMapper;
import com.fiberhome.mapps.activity.entity.AtPrivilege;
import com.fiberhome.mapps.activity.request.AddPrivilegeRequest;
import com.fiberhome.mapps.activity.request.QueryPrivilegeRequest;
import com.fiberhome.mapps.activity.response.AddPrivilegeResponse;
import com.fiberhome.mapps.activity.response.QueryPrivilegeResponse;
import com.fiberhome.mapps.contact.utils.JsonUtil;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.utils.ErrorCode;
import com.fiberhome.mapps.utils.IDGen;
import com.fiberhome.mapps.utils.LogUtil;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean
public class PrivilegeService {
    @Autowired
    AtPrivilegeMapper          atPrivilegeMapper;
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
    @ServiceMethod(method = "mapps.activity.privilege.add", group = "privilege", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional
    public AddPrivilegeResponse addMrPrivilege(AddPrivilegeRequest req)
    {
        AddPrivilegeResponse res = new AddPrivilegeResponse();
        try
        {
            LOGGER.info("新增权限接口(mapps.activity.privilege.add)入口,请求参数==" + LogUtil.getObjectInfo(req));
            String actId = req.getActId();
            QueryPrivilegeResponse privData = (QueryPrivilegeResponse) JsonUtil.jsonToObject(req.getJsonData(),
                    QueryPrivilegeResponse.class);
            AtPrivilege deletePriv = new AtPrivilege();
            deletePriv.setActId(actId);
            deletePriv.setPriv(PRIV_USER);
            atPrivilegeMapper.delete(deletePriv);
            for (AtPrivilege entity : privData.getList())
            {
                String id = IDGen.uuid();
                entity.setId(id);
                entity.setEcid(SessionContext.getEcId());
                entity.setActId(actId);
                entity.setDeptOrder(SessionContext.getDeptOrder());
                entity.setAuthrTime(new Date());
                entity.setPriv(PRIV_USER);
                atPrivilegeMapper.insertSelective(entity);

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
    @ServiceMethod(method = "mapps.activity.privilege.query", group = "privilege", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryPrivilegeResponse getMrPrivilege(QueryPrivilegeRequest req)
    {
        QueryPrivilegeResponse res = new QueryPrivilegeResponse();
        try
        {
            LOGGER.debug("获取权限信息接口(mapps.activity.privilege.query)入口,请求参数==" + LogUtil.getObjectInfo(req));
            AtPrivilege atPrivilege = new AtPrivilege();
            atPrivilege.setActId(req.getActId());
            atPrivilege.setPriv(PRIV_USER);
            List<AtPrivilege> list = atPrivilegeMapper.select(atPrivilege);
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
}
