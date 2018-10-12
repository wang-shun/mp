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

import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.meeting.dao.StatisticalAnalysisMapper;
import com.fiberhome.mapps.meeting.entity.StatisticalAnalysis;
import com.fiberhome.mapps.meetingroom.request.StatisticalAnalysisRequest;
import com.fiberhome.mapps.meetingroom.response.StatisticalAnalysisResponse;
import com.fiberhome.mapps.meetingroom.utils.DateUtil;
import com.fiberhome.mapps.meetingroom.utils.ErrorCode;
import com.fiberhome.mapps.meetingroom.utils.ExcelUtils;
import com.fiberhome.mapps.meetingroom.utils.LogUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;
import com.rop.response.FileResponse;

@ServiceMethodBean(version = "1.0")
public class StatisticalAnalysisService
{
    @Autowired
    StatisticalAnalysisMapper  statisticalAnalysisMapper;
    protected final Logger     LOGGER   = LoggerFactory.getLogger(getClass());
    /**
     * 分页
     */
    public static final String PAGE_YES = "1";
    /**
     * 不分页
     */
    public static final String PAGE_NO  = "2";

    /**
     * 获取统计分析数据
     * 
     * @param req
     * @return
     */
    @ServiceMethod(method = "mapps.meetingroom.stat.query", group = "stat", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public StatisticalAnalysisResponse getStatisticalAnalysis(StatisticalAnalysisRequest req)
    {
        StatisticalAnalysisResponse res = new StatisticalAnalysisResponse();
        try
        {
            LOGGER.info("统计分析数据信息接口(mapps.meetingroom.stat.query)入口,请求参数==" + LogUtil.getObjectInfo(req));
            if (PAGE_YES.equals(req.getPageFlag()))
            {
                PageHelper.startPage(req.getOffset(), req.getLimit());
            }
            Map<String, Object> map = initQuery(req);
            List<StatisticalAnalysis> list = statisticalAnalysisMapper.getStatisticalAnalysis(map);
            if (PAGE_YES.equals(req.getPageFlag()))
            {
                PageInfo<StatisticalAnalysis> page = new PageInfo<StatisticalAnalysis>(list);
                res.setTotal(page.getTotal());
            }
            res.setStatList(list);
            res.success();
            LOGGER.info("统计分析数据信息成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("统计分析数据信息异常：{}", e);
            ErrorCode.fail(res, ErrorCode.CODE_100001);
        }
        return res;
    }

    /**
     * 导出会议室统计数据
     */
    @ServiceMethod(method = "mapps.meetingroom.stat.export", group = "stat", groupTitle = "API", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public FileResponse export(StatisticalAnalysisRequest req)
    {
        FileResponse fr = null;
        InputStream fileInput = null;
        OutputStream fileOuput = null;
        try
        {
            Map<String, Object> map = initQuery(req);
            List<StatisticalAnalysis> statisticList = statisticalAnalysisMapper.getStatisticalAnalysis(map);
            List<StatisticalAnalysisRequest> queryData = new ArrayList<StatisticalAnalysisRequest>();
            StatisticalAnalysisRequest sRequest = new StatisticalAnalysisRequest();
            sRequest.setStatBeginTime(new SimpleDateFormat("yyyy-MM-dd").format(map.get("statBeginTime")));
            sRequest.setStatEndTime(new SimpleDateFormat("yyyy-MM-dd").format(map.get("statEndTime")));
            queryData.add(sRequest);
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            File targetFile = File.createTempFile("会议室统计数据-" + simpleDate.format(new Date()), ".xls");
            String destPath = targetFile.getPath();
            fileInput = this.getClass().getClassLoader().getResourceAsStream("template/statisticAnalysis.xls");
            fileOuput = new FileOutputStream(targetFile);
            ExcelUtils.generateExcelByTemplate(fileOuput, fileInput, queryData, "queryData", statisticList,
                    "statisticList", 65000, null);
            fr = getResponseFile(new File(destPath), true);
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
     * 获得返回文件
     * 
     * @param photoId
     * @return
     * @throws Exception
     */
    public FileResponse getResponseFile(File file, boolean flag) throws Exception
    {
        FileResponse respFile = new FileResponse(file, flag);
        if (null != file)
        {
            respFile.setFile(file);
        }
        else
        {
            respFile = FileResponse.NOT_EXISTS;
        }
        return respFile;
    }

    /**
     * 根据统计类型获取对应值
     * 
     * @param entity
     * @param statType 1 预定次数 2 预定总时长 3 每天预定时长 4 每次预定时长
     * @return
     */

    public Map<String, Object> initQuery(StatisticalAnalysisRequest req) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Date sDate = StringUtil.isEmpty(req.getStatBeginTime()) ? new Date()
                : DateUtil.convertToDate(req.getStatBeginTime());
        Date eDate = StringUtil.isEmpty(req.getStatEndTime()) ? new Date()
                : DateUtil.convertToDate(req.getStatEndTime());
        map.put("statBeginTime", sDate);
        map.put("statEndTime", DateUtil.getLastTime(eDate));
        map.put("ecid", SessionContext.getEcId());
        map.put("dayNum", DateUtil.daysBetween(sDate, eDate) + 1);
        // map.put("userId", SessionContext.getUserId());
        map.put("roomStatus", '1');
        map.put("reservedStatus", '3');
        if (StringUtil.isNotEmpty(req.getSort()))
        {
            map.put("sort", req.getSort());
        }
        return map;
    }
}
