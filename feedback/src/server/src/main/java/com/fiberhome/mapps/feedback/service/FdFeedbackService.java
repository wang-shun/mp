package com.fiberhome.mapps.feedback.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.feedback.dao.FdFeedbackMapper;
import com.fiberhome.mapps.feedback.entity.FdFeedback;
import com.fiberhome.mapps.feedback.request.AddFeedbackRequest;
import com.fiberhome.mapps.feedback.request.FeedbackIdRequest;
import com.fiberhome.mapps.feedback.request.QueryFeedbackRequest;
import com.fiberhome.mapps.feedback.request.RoleRequest;
import com.fiberhome.mapps.feedback.response.FeedbackResponse;
import com.fiberhome.mapps.feedback.response.FileInfoResponse;
import com.fiberhome.mapps.feedback.response.QueryFeedbackResponse;
import com.fiberhome.mapps.feedback.response.RoleResponse;
import com.fiberhome.mapps.feedback.utils.DateUtil;
import com.fiberhome.mapps.feedback.utils.ErrorCode;
import com.fiberhome.mapps.feedback.utils.LogUtil;
import com.fiberhome.mapps.feedback.utils.VersionUtil;
import com.fiberhome.mapps.intergration.rop.BaseResponse;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.utils.IDGen;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

import tk.mybatis.mapper.util.StringUtil;

@ServiceMethodBean(version = "1.0")
public class FdFeedbackService
{
    @Autowired
    FdFeedbackMapper           fdFeedbackMapper;
    @Autowired
    FileService                fileService;
    @Autowired
    ThirdPartAccessService     thirdPartAccessService;
    protected final Logger     LOGGER        = LoggerFactory.getLogger(getClass());
    /** 反馈确认标识已处理 = 1 */
    public static final String CONFIRM_DONE  = "2";
    /** 反馈确认标识 处理中 = 1 */
    public static final String CONFIRM_DOING = "1";
    /** 反馈确认标识 待处理 = 0 */
    public static final String CONFIRM_DO    = "0";
    /** 反馈删除标识 已删除 = 1 */
    public static final String DELETE_YES    = "1";
    /** 反馈删除标识 未删除 = 0 */
    public static final String DELETE_NO     = "0";
    
    @Value("${flywaydb.locations}")
	String databaseType;

    /**
     * 管理员权限类型获取
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.feedback.isadmin", group = "feedback", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
    public RoleResponse getRole(RoleRequest req)
    {
        RoleResponse res = new RoleResponse();
        res.setAdminFlag(SessionContext.isAdmin());
        return res;
    }

    @ServiceMethod(method = "mapps.feedback.user", group = "room", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public FeedbackResponse getUserInfo(FeedbackIdRequest req)
    {
        FeedbackResponse res = new FeedbackResponse();
        try
        {
            LOGGER.info("get user info(mapps.feedback.user),request==" + LogUtil.getObjectInfo(req));
            String id = req.getFeedbackId();
            if (StringUtils.isEmpty(id))
            {
                LOGGER.error("not exists id：{}", id);
                ErrorCode.fail(res, ErrorCode.CODE_100001);
                return res;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("feedbackId", id);
            map.put("ecid", SessionContext.getEcId());
            map.put("databaseType", databaseType);
            List<FdFeedback> list = fdFeedbackMapper.getFdFeedback(map);
            if (list != null && list.size() > 0)
            {
                FdFeedback feedback = list.get(0);
                MyUser userInfo = thirdPartAccessService.getUserInfo(SessionContext.getOrgId(), feedback.getUserId());
                res.setFeedback(feedback);
                res.setDeptName(userInfo.getDeptName());
                res.setEmail(userInfo.getEmailAddress());
            }
            else
            {
                LOGGER.error("not exists id：{}", id);
                ErrorCode.fail(res, ErrorCode.CODE_100001);
                return res;
            }
            LOGGER.info("get user info success");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get user info error：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 新增意见反馈
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.feedback.submit", group = "feedback", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public BaseResponse addFeedback(AddFeedbackRequest req)
    {
        BaseResponse res = new BaseResponse();
        try
        {
            LOGGER.info("新增意见反馈接口(mapps.feedback.submit)入口,请求参数==" + LogUtil.getObjectInfo(req));
            String id = IDGen.uuid();
            String ecid = SessionContext.getEcId();
            String userId = SessionContext.getUserId();
            String userName = SessionContext.getUserName();
            FdFeedback record = new FdFeedback();
            record.setId(id);
            record.setEcid(ecid);
            record.setUserId(userId);
            record.setUserName(userName);
            record.setAppId(req.getAppId());
            record.setAppName(req.getAppName());
            record.setAppVer(req.getAppVer());
            record.setAppVerFmt(VersionUtil.getFmtVersion(req.getAppVer()));
            record.setDeviceName(req.getDeviceName());
            record.setOsVer(req.getOsVer());
            record.setFeedback(req.getFeedback());
            record.setContack(req.getContack());
            record.setSubmitTime(new Date());
            record.setConfirm(CONFIRM_DO);
            record.setDelFlag(DELETE_NO);
            List<String> pathList = new ArrayList<String>();
            // 保存图片到本服务器
            if (req.getImageItems() != null && req.getImageItems().size() > 0)
            {
                for (String image : req.getImageItems())
                {
                    FileInfoResponse upload = fileService.downAndReadFile(image);
                    if (upload == null || BaseResponse.FAIL.equals(upload.getCode()))
                    {
                        ErrorCode.fail(res, ErrorCode.CODE_100002);
                    }
                    pathList.add(upload.getPath());
                }
                record.setImages(StringUtils.join(pathList, ","));
            }
            fdFeedbackMapper.insertSelective(record);
            res.success();
            LOGGER.info("新增意见反馈成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("新增意见反馈异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    @ServiceMethod(method = "mapps.feedback.update", group = "feedback", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public BaseResponse updateFeedback(FeedbackIdRequest req)
    {
        BaseResponse res = new BaseResponse();
        try
        {
            LOGGER.info("Feedback Confirm interface(mapps.feedback.submit),request==" + LogUtil.getObjectInfo(req));
            String id = req.getFeedbackId();
            String confirm = req.getConfirm();
            if (StringUtils.isEmpty(id))
            {
                LOGGER.error("not exists id：{}", id);
                ErrorCode.fail(res, ErrorCode.CODE_100001);
                return res;
            }
            if (StringUtils.isEmpty(confirm))
            {
                LOGGER.error("not exists confirm：{}", confirm);
                ErrorCode.fail(res, ErrorCode.CODE_100001);
                return res;
            }
            FdFeedback record = new FdFeedback();
            record.setId(id);
            record.setConfirm(confirm);
            record.setProblem(req.getProblem());
            record.setSolution(req.getSolution());
            record.setConfirmUserId(SessionContext.getUserId());
            record.setConfirmUserName(SessionContext.getUserName());
            record.setConfirmTime(new Date());
            fdFeedbackMapper.updateByPrimaryKeySelective(record);
            res.success();
            LOGGER.info("Feedback Confirm Success");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("Feedback Confirm Error：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 删除意见反馈
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.feedback.delete", group = "feedback", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public BaseResponse deleteFeedback(FeedbackIdRequest req)
    {
        BaseResponse res = new BaseResponse();
        try
        {
            LOGGER.info("删除意见反馈接口(mapps.feedback.delete)入口,请求参数==" + LogUtil.getObjectInfo(req));
            if (StringUtil.isNotEmpty(req.getFeedbackId()))
            {
                FdFeedback record = new FdFeedback();
                record.setId(req.getFeedbackId());
                record.setDelFlag(DELETE_YES);
                fdFeedbackMapper.updateByPrimaryKeySelective(record);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("删除意见反馈异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 分页获取意见反馈信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.feedback.query", group = "feedback", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryFeedbackResponse getFeedbackForPage(QueryFeedbackRequest req)
    {
        QueryFeedbackResponse res = new QueryFeedbackResponse();
        try
        {
            LOGGER.info("分页查询意见反馈信息接口(mapps.feedback.query)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = initQuery(req);
            map.put("databaseType", databaseType);
            PageHelper.startPage(req.getOffset(), req.getLimit());
            List<FdFeedback> list = fdFeedbackMapper.getFdFeedback(map);
            PageInfo<FdFeedback> page = new PageInfo<FdFeedback>(list);
            res.setTotal(page.getTotal());
            if (list == null)
            {
                list = new ArrayList<FdFeedback>();
            }
            res.setList(list);
            res.success();
            LOGGER.info("分页查询意见反馈信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("分页查询意见反馈信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    public String getInSql(List<String> roomIds)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        for (String str : roomIds)
        {
            sb.append("'").append(str).append("'").append(",");
        }
        sb.deleteCharAt(sb.toString().length() - 1);
        sb.append(")");
        return sb.toString();
    }

    public Map<String, Object> initQuery(QueryFeedbackRequest req) throws Exception
    {
        String userName = req.getUserName();
        String sTime = req.getBeginDate();
        String eTime = req.getEndDate();
        String sort = req.getSort();

        Map<String, Object> map = new HashMap<String, Object>();

        if (StringUtil.isNotEmpty(userName))
        {
            map.put("name", "%" + userName.trim() + "%");
        }
        if (StringUtil.isNotEmpty(sTime))
        {
            map.put("submitStartTime", DateUtil.convertToDate(sTime));
        }
        if (StringUtil.isNotEmpty(eTime))
        {
            map.put("submitEndTime", DateUtil.getLastTime(DateUtil.convertToDate(eTime)));
        }
        if (StringUtil.isNotEmpty(sort))
        {
            map.put("order", sort);
        }
        map.put("ecid", SessionContext.getEcId());
        return map;
    }

    /**
     * 获取意见反馈详细信息
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.feedback.detail", group = "feedback", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public FeedbackResponse getFeedbackDetail(FeedbackIdRequest req)
    {
        FeedbackResponse res = new FeedbackResponse();
        try
        {
            LOGGER.info("查询意见反馈详细信息接口(mapps.feedback.detail)入口,请求参数==" + LogUtil.getObjectInfo(req));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("feedbackId", req.getFeedbackId());
            map.put("ecid", SessionContext.getEcId());
            map.put("databaseType", databaseType);
            List<FdFeedback> list = fdFeedbackMapper.getFdFeedback(map);
            if (list != null && list.size() > 0)
            {
                FdFeedback fdInfo = list.get(0);
                fdInfo.setImages(StringUtil.isEmpty(fdInfo.getImages()) ? "" : fdInfo.getImages());
                res.setFeedback(fdInfo);
                res.setWebRoot(fileService.getWebRoot());
            }
            else
            {
                LOGGER.error("未查询到记录：{}", req.getFeedbackId());
                ErrorCode.fail(res, ErrorCode.CODE_100001);
                return res;
            }
            LOGGER.info("查询意见反馈详细信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("查询意见反馈详细信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }
}
