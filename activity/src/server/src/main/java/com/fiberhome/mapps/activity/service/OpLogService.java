package com.fiberhome.mapps.activity.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.activity.dao.OpLogMapper;
import com.fiberhome.mapps.activity.entity.BaseOpLogContent;
import com.fiberhome.mapps.activity.entity.GetOpLog;
import com.fiberhome.mapps.activity.entity.OpLog;
import com.fiberhome.mapps.activity.request.UserRecoedRequest;
import com.fiberhome.mapps.activity.response.UserRecoedResponse;
import com.fiberhome.mapps.utils.DateUtil;
import com.fiberhome.mapps.utils.ErrorCode;
import com.fiberhome.mapps.utils.IDGen;
import com.fiberhome.mapps.utils.LogUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;
import com.rop.response.FileResponse;

import net.sf.json.JSONObject;

@ServiceMethodBean
public class OpLogService
{
    protected final Logger     LOGGER           = LoggerFactory.getLogger(getClass());

    @Autowired
    OpLogMapper                opLogMapper;

    public static final String MODULE_ACTIVITY      = "activity";
    public static final String MODULE_ENTER  = "enter";
    public static final String MODULE_PRIVILEGE = "privilege";
    public static final String OP_ADD           = "新增";
    public static final String OP_EDIT          = "修改";
    public static final String OP_DELETE        = "删除";
    public static final String OP_RESERVED      = "预定";
    public static final String OP_CANCEL        = "取消";
    public static final String OP_OVER          = "结束";
    /** 成功 */
    public static final String RESULT_YES       = "1";
    /** 失败 */
    public static final String RESULT_NO        = "0";

    /**
     * 分页
     */
    public static final String PAGE_YES         = "1";
    /**
     * 不分页
     */
    public static final String PAGE_NO          = "2";

    /**
     * 操作记录日志添加方法
     * 
     * @param module 日志所属模块
     * @param object 模块主表唯一标示
     * @param op 新增/修改/删除操作
     * @param contentInfo 日志主体信息
     * @param result 成功/失败
     */
    @Async
    public void addModuleOpLog(String module, String object, String op, BaseOpLogContent contentInfo, String result,
            String ecid, String userId, String userName)
    {
        LOGGER.debug("操作记录保存日志入口：module=" + module + ",object=" + object + ",op=" + op + ",content=["
                + contentInfo.toString() + "],result=" + result);
        try
        {
            OpLog entity = new OpLog();
            entity.setId(IDGen.uuid());
            entity.setModule(module);
            entity.setObject(object);
            entity.setOp(op);
            entity.setContent(JSONObject.fromObject(contentInfo).toString());
            entity.setResult(result);
            entity.setEcid(ecid);
            entity.setUserId(userId);
            entity.setUserName(userName);
            entity.setDeptId("-1");
            entity.setDeptName("");
            entity.setOpTime(new Date());
            opLogMapper.insert(entity);
        }
        catch (Exception e)
        {
            LOGGER.error("操作记录日志保存失败：{}", e);
        }
    }

    /**
     * 操作记录日志添加方法
     * 
     * @param module 日志所属模块
     * @param object 模块主表唯一标示
     * @param op 新增/修改/删除操作
     * @param contentInfo 日志主体信息
     * @param result 成功/失败
     */
    @Async
    public void addModuleOpLog(String module, String object, String op, BaseOpLogContent contentInfo, String result,
            String ecid, String userId, String userName, String deptId, String deptName)
    {
        LOGGER.debug("操作记录保存日志入口：module=" + module + ",object=" + object + ",op=" + op + ",content=["
                + contentInfo.toString() + "],result=" + result);
        try
        {
            OpLog entity = new OpLog();
            entity.setId(IDGen.uuid());
            entity.setModule(module);
            entity.setObject(object);
            entity.setOp(op);
            entity.setContent(JSONObject.fromObject(contentInfo).toString());
            entity.setResult(result);
            entity.setEcid(ecid);
            entity.setUserId(userId);
            entity.setUserName(userName);
            entity.setDeptId(deptId);
            entity.setDeptName(deptName);
            entity.setOpTime(new Date());
            opLogMapper.insert(entity);
        }
        catch (Exception e)
        {
            LOGGER.error("操作记录日志保存失败：{}", e);
        }
    }

    /**
     * @param UserRecoedRequest
     * @return
     */

    public Map<String, Object> initQuery(UserRecoedRequest req) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Date sDate = StringUtil.isEmpty(req.getStatBeginTime()) ? new Date()
                : DateUtil.convertToDate(req.getStatBeginTime());
        Date eDate = StringUtil.isEmpty(req.getStatEndTime()) ? new Date()
                : DateUtil.convertToDate(req.getStatEndTime());
        map.put("statBeginTime", sDate);
        map.put("statEndTime", DateUtil.getLastTime(eDate));
        map.put("ecid", SessionContext.getEcId());
        String userName = req.getUserName();
        String depName = req.getDepName();
        String op = req.getOp();
        if (StringUtil.isNotEmpty(userName))
        {
            map.put("userName", "%" + userName.trim() + "%");
        }
        if (StringUtil.isNotEmpty(depName))
        {
            map.put("depName", "%" + depName.trim() + "%");
        }
        if (StringUtil.isNotEmpty(op))
        {
            map.put("op", op);
        }
        return map;
    }
}
