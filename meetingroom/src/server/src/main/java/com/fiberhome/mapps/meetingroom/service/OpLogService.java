package com.fiberhome.mapps.meetingroom.service;

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
import com.fiberhome.mapps.meetingroom.dao.OpLogMapper;
import com.fiberhome.mapps.meetingroom.entity.BaseOpLogContent;
import com.fiberhome.mapps.meetingroom.entity.GetOpLog;
import com.fiberhome.mapps.meetingroom.entity.OpLog;
import com.fiberhome.mapps.meetingroom.entity.ReservedLogContent;
import com.fiberhome.mapps.meetingroom.request.UserRecoedRequest;
import com.fiberhome.mapps.meetingroom.response.UserRecoedResponse;
import com.fiberhome.mapps.meetingroom.utils.DateUtil;
import com.fiberhome.mapps.meetingroom.utils.ErrorCode;
import com.fiberhome.mapps.meetingroom.utils.ExcelUtils;
import com.fiberhome.mapps.meetingroom.utils.JsonUtil;
import com.fiberhome.mapps.meetingroom.utils.LogUtil;
import com.fiberhome.mapps.utils.IDGen;
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

    @Autowired
    StatisticalAnalysisService statisticalAnalysisService;

    public static final String MODULE_ROOM      = "room";
    public static final String MODULE_RESERVED  = "reserved";
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
     * 获取用户记录数据
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.oplog.query", group = "oplog", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public UserRecoedResponse getOpLog(UserRecoedRequest req)
    {
        UserRecoedResponse res = new UserRecoedResponse();
        try
        {
            LOGGER.info("用户记录信息接口(mapps.meetingroom.oplog.query)入口,请求参数==" + LogUtil.getObjectInfo(req));
            if (PAGE_YES.equals(req.getPageFlag()))
            {
                PageHelper.startPage(req.getOffset(), req.getLimit());
            }
            Map<String, Object> map = initQuery(req);
            List<OpLog> list = opLogMapper.getOpLog(map);
            if (PAGE_YES.equals(req.getPageFlag()))
            {
                PageInfo<OpLog> page = new PageInfo<OpLog>(list);
                res.setTotal(page.getTotal());
            }
            List<GetOpLog> opLogList = new ArrayList<GetOpLog>();
            for (OpLog entity : list)
            {
                ReservedLogContent content = (ReservedLogContent) JsonUtil.jsonToObject(entity.getContent(),
                        ReservedLogContent.class);
                GetOpLog info = new GetOpLog();
                info.setUserId(entity.getUserId());
                info.setUserName(entity.getUserName());
                info.setDepName(entity.getDeptName());
                info.setOp(entity.getOp());
                info.setOpTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getOpTime()));
                info.setRoomName(content.getRoomName());
                info.setReservedTime(content.getReservedTime());
                info.setMeetingName(content.getMeetingName());
                info.setResult(entity.getResult());
                opLogList.add(info);
            }
            res.setOpLogList(opLogList);
            res.success();
            LOGGER.info("用户记录信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("用户记录信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 下载用户操作记录
     */
    @ServiceMethod(method = "mapps.meetingroom.oplog.export", group = "oplog", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public FileResponse export(UserRecoedRequest req)
    {
        FileResponse fr = null;
        InputStream fileInput = null;
        OutputStream fileOuput = null;
        try
        {
            Map<String, Object> map = initQuery(req);
            List<OpLog> list = opLogMapper.getOpLog(map);
            List<GetOpLog> opLogList = new ArrayList<GetOpLog>();
            for (OpLog entity : list)
            {
                ReservedLogContent content = (ReservedLogContent) JsonUtil.jsonToObject(entity.getContent(),
                        ReservedLogContent.class);
                GetOpLog info = new GetOpLog();
                info.setUserId(entity.getUserId());
                info.setUserName(entity.getUserName());
                info.setDepName(entity.getDeptName());
                info.setOp(entity.getOp());
                info.setOpTime(new SimpleDateFormat("yyyy-MM-dd").format(entity.getOpTime()));
                info.setRoomName(content.getRoomName());
                info.setReservedTime(content.getReservedTime());
                info.setMeetingName(content.getMeetingName());
                info.setResult(entity.getResult().equals("1") ? "成功" : "失败");
                opLogList.add(info);
            }
            List<UserRecoedRequest> queryData = new ArrayList<>();
            UserRecoedRequest uRequest = new UserRecoedRequest();
            uRequest.setUserName(req.getUserName());
            uRequest.setDepName(req.getDepName());
            uRequest.setStatBeginTime(new SimpleDateFormat("yyyy-MM-dd").format(map.get("statBeginTime")));
            uRequest.setStatEndTime(new SimpleDateFormat("yyyy-MM-dd").format(map.get("statEndTime")));
            uRequest.setOp(req.getOp());
            queryData.add(uRequest);
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            File targetFile = File.createTempFile("用户记录-" + simpleDate.format(new Date()), ".xls");
            String destPath = targetFile.getPath();
            fileInput = this.getClass().getClassLoader().getResourceAsStream("template/userRecord.xls");
            fileOuput = new FileOutputStream(targetFile);
            ExcelUtils.generateExcelByTemplate(fileOuput, fileInput, queryData, "queryData", opLogList, "opLogList",
                    65000, null);
            fr = statisticalAnalysisService.getResponseFile(new File(destPath), true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fr = FileResponse.NOT_EXISTS;
        }
        finally
        {
            try
            {
                if (fileOuput != null)
                    fileOuput.close();
                if (fileInput != null)
                    fileInput.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return fr;
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
