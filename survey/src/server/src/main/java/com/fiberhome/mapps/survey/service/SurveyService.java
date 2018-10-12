package com.fiberhome.mapps.survey.service;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.fiberhome.mapps.contact.pojo.MyUser;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.survey.dao.SuAnswerMapper;
import com.fiberhome.mapps.survey.dao.SuAnswerPeopleMapper;
import com.fiberhome.mapps.survey.dao.SuParticipantsMapper;
import com.fiberhome.mapps.survey.dao.SuQuestionMapper;
import com.fiberhome.mapps.survey.dao.SuQuestionOptionsMapper;
import com.fiberhome.mapps.survey.dao.SuSurveyGroupMapper;
import com.fiberhome.mapps.survey.dao.SuSurveyMapper;
import com.fiberhome.mapps.survey.dao.SuSurveyQuestionMapper;
import com.fiberhome.mapps.survey.dao.SuSurveyTemplateMapper;
import com.fiberhome.mapps.survey.dao.SuTemplateQuestionMapper;
import com.fiberhome.mapps.survey.entity.AnalysisQuestionJson;
import com.fiberhome.mapps.survey.entity.AnalysisQuestionOptionsJson;
import com.fiberhome.mapps.survey.entity.SuAnalysisCondition;
import com.fiberhome.mapps.survey.entity.SuAnalysisOption;
import com.fiberhome.mapps.survey.entity.SuAnswer;
import com.fiberhome.mapps.survey.entity.SuAnswerPeople;
import com.fiberhome.mapps.survey.entity.SuAnswerPeoplePojo;
import com.fiberhome.mapps.survey.entity.SuParticipants;
import com.fiberhome.mapps.survey.entity.SuQuestion;
import com.fiberhome.mapps.survey.entity.SuQuestionJson;
import com.fiberhome.mapps.survey.entity.SuQuestionOptions;
import com.fiberhome.mapps.survey.entity.SuQuestionOptionsJson;
import com.fiberhome.mapps.survey.entity.SuSurvey;
import com.fiberhome.mapps.survey.entity.SuSurveyGroup;
import com.fiberhome.mapps.survey.entity.SuSurveyJson;
import com.fiberhome.mapps.survey.entity.SuSurveyPojo;
import com.fiberhome.mapps.survey.entity.SuSurveyQuestion;
import com.fiberhome.mapps.survey.entity.SuSurveyTemplate;
import com.fiberhome.mapps.survey.entity.SuSurveyTemplateJson;
import com.fiberhome.mapps.survey.entity.SuSurveyTemplatePojo;
import com.fiberhome.mapps.survey.entity.SuTemplateQuestion;
import com.fiberhome.mapps.survey.entity.SurveyAnswer;
import com.fiberhome.mapps.survey.entity.SurveyAnswerJson;
import com.fiberhome.mapps.survey.request.CopySurveyRequest;
import com.fiberhome.mapps.survey.request.DelSurveyGroupRequest;
import com.fiberhome.mapps.survey.request.DelSurveyRequest;
import com.fiberhome.mapps.survey.request.DelSurveyTemplateRequest;
import com.fiberhome.mapps.survey.request.GetSurveyAnalysisRequest;
import com.fiberhome.mapps.survey.request.GetSurveyAnswerPeopleListRequest;
import com.fiberhome.mapps.survey.request.GetSurveyContentRequest;
import com.fiberhome.mapps.survey.request.GetSurveyContentToWebRequest;
import com.fiberhome.mapps.survey.request.GetSurveyGroupRequest;
import com.fiberhome.mapps.survey.request.GetSurveyListForWebRequest;
import com.fiberhome.mapps.survey.request.GetSurveyListRequest;
import com.fiberhome.mapps.survey.request.GetSurveyQuestionOptionRequest;
import com.fiberhome.mapps.survey.request.GetSurveyQuestionRequest;
import com.fiberhome.mapps.survey.request.GetSurveyTemplateListRequest;
import com.fiberhome.mapps.survey.request.GetSurveyTemplateRequest;
import com.fiberhome.mapps.survey.request.GetUsersRequest;
import com.fiberhome.mapps.survey.request.SaveSurveyAnswerRequest;
import com.fiberhome.mapps.survey.request.SaveSurveyGroupRequest;
import com.fiberhome.mapps.survey.response.CopySurveyResponse;
import com.fiberhome.mapps.survey.response.DelSurveyGroupResponse;
import com.fiberhome.mapps.survey.response.DelSurveyResponse;
import com.fiberhome.mapps.survey.response.DelSurveyTemplateResponse;
import com.fiberhome.mapps.survey.response.GetSurveyAnalysisResponse;
import com.fiberhome.mapps.survey.response.GetSurveyAnswerPeopleListResponse;
import com.fiberhome.mapps.survey.response.GetSurveyContentResponse;
import com.fiberhome.mapps.survey.response.GetSurveyGroupResponse;
import com.fiberhome.mapps.survey.response.GetSurveyListResponse;
import com.fiberhome.mapps.survey.response.GetSurveyQuestionOptionResponse;
import com.fiberhome.mapps.survey.response.GetSurveyQuestionResponse;
import com.fiberhome.mapps.survey.response.GetSurveyTemplateListResponse;
import com.fiberhome.mapps.survey.response.GetUsersResponse;
import com.fiberhome.mapps.survey.response.PreviewSurveyResponse;
import com.fiberhome.mapps.survey.response.PreviewSurveyTemplateResponse;
import com.fiberhome.mapps.survey.response.SaveSurveyAnswerResponse;
import com.fiberhome.mapps.survey.response.SaveSurveyGroupResponse;
import com.fiberhome.mapps.survey.utils.DateUtil;
import com.fiberhome.mapps.survey.utils.ErrorCode;
import com.fiberhome.mapps.utils.IDGen;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@ServiceMethodBean(version = "1.0")
@Controller
public class SurveyService
{
    protected final Logger      logger               = LoggerFactory.getLogger(getClass());

    public static final String  EDIT                 = "0";

    public static final String  PUBLISH              = "1";

    public static final String  CLOSE                = "2";

    public static final String  RADIO                = "1";

    public static final String  CHECKBOX             = "2";

    public static final String  SELECT               = "3";

    public static final String  TEXT                 = "4";

    public static final String  TEXTAREA             = "5";

    /** CSV文件列分隔符 */
    private static final String CSV_COLUMN_SEPARATOR = ",";
    /** CSV文件列分隔符 */
    private static final String CSV_RN               = "\r\n";

    @Autowired
    SuSurveyMapper              surveyMapper;

    @Autowired
    SuSurveyQuestionMapper      surveyQuestionMapper;

    @Autowired
    SuAnswerPeopleMapper        answerPeopleMapper;

    @Autowired
    SuQuestionMapper            questionMapper;

    @Autowired
    SuAnswerMapper              answerMapper;

    @Autowired
    SuQuestionOptionsMapper     questionOptionsMapper;

    @Autowired
    SuSurveyTemplateMapper      surveyTemplateMapper;

    @Autowired
    SuTemplateQuestionMapper    templateQuestionMapper;

    @Autowired
    SuSurveyGroupMapper         surveyGroupMapper;

    @Autowired
    SuParticipantsMapper        suParticipantsMapper;

    @Autowired
    ThirdPartAccessService      thirdPartAccessService;
    @Autowired
    ScheduledService            scheduledService;

    @ServiceMethod(method = "mapps.survey.clientquery", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetSurveyListResponse getSurveyList(GetSurveyListRequest request)
    {
        GetSurveyListResponse response = new GetSurveyListResponse();
        Map<String, Object> map = new HashMap<String, Object>();
        if ("1".equals(request.getType()))
        {
            map.put("type", request.getType());
        }
        map.put("userId", SessionContext.getUserId());
        map.put("ecid", SessionContext.getEcId());
        if (request.getTimestamp() == 0l)
        {
            request.setTimestamp(new Date().getTime());
        }
        map.put("timestamp", new Date(request.getTimestamp()));
        map.put("curenttimestamp", new Date());
        PageHelper.startPage(request.getOffset(), request.getLimit());
        List<SuSurveyPojo> list = surveyMapper.getSurvey(map);
        PageInfo<SuSurveyPojo> page = new PageInfo<SuSurveyPojo>(list);
        response.setSurveyInfos(page.getList());
        response.setTimestamp(request.getTimestamp());
        if (page.isIsLastPage())
        {
            response.setEndflag(1);
        }
        else
        {
            response.setEndflag(0);
        }
        return response;
    }

    @ServiceMethod(method = "mapps.survey.clientdetail", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetSurveyContentResponse getSurveyContent(GetSurveyContentRequest request)
    {
        GetSurveyContentResponse response = new GetSurveyContentResponse();
        String id = request.getSurveyId();
        String personId = SessionContext.getUserId();
        SuSurvey survey = surveyMapper.selectByPrimaryKey(id);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("personId", personId);
        map.put("surveyId", id);
        SuAnswerPeople answerPeople = answerPeopleMapper.getAnswerPeopleByPerson(map);
        SuSurveyJson surveyContent = new SuSurveyJson();
        surveyContent.setStatus(survey.getStatus());
        surveyContent.setPager(survey.getPager());
        surveyContent.setTitle(survey.getTitle());
        surveyContent.setTitleHtml(survey.getTitleHtml());
        surveyContent.setCreateTime(new Date());
        // 已经完成问卷调查
        if (answerPeople != null && "1".equals(answerPeople.getAnswered()))
        {
            surveyContent.setSubmitTime(DateUtil.sdfHMS().format(answerPeople.getSubmitTime()));
        }
        else
        {
            surveyContent.setSubmitTime("");
        }
        List<SuQuestion> list = questionMapper.getQuestionBySurveyId(id);
        List<SuQuestionJson> qlist = new ArrayList<SuQuestionJson>();
        // 获取选项
        for (SuQuestion sq : list)
        {
            SuQuestionJson qc = new SuQuestionJson();
            qc.setId(sq.getId());
            qc.setCode(sq.getCode());
            qc.setSelMax(sq.getSelMax());
            qc.setSelMin(sq.getSelMin());
            qc.setType(sq.getType());
            qc.setRequired(sq.getRequired());
            qc.setQuestion(sq.getQuestion());
            qc.setQuestionHtml(sq.getQuestionHtml());
            String questionId = sq.getId();
            map.put("questionId", questionId);
            // 文本取答案
            if (TEXT.equals(sq.getType()) || TEXTAREA.equals(sq.getType()))
            {
                if (answerPeople != null && "1".equals(answerPeople.getAnswered()))
                {
                    SuAnswer answer = answerMapper.getTextAnswerByQuestion(map);
                    if (answer != null)
                    {
                        qc.setTextValue(answer.getAnswer());
                    }
                }
            }
            else
            {
                List<SuQuestionOptionsJson> oContents = new ArrayList<SuQuestionOptionsJson>();
                if (answerPeople != null && "1".equals(answerPeople.getAnswered()))
                {
                    oContents = questionOptionsMapper.getOptionsAnswersByQuestionId(map);
                }
                else
                {
                    List<SuQuestionOptions> options = questionOptionsMapper.getQuestionOptionsByQuestionId(questionId);
                    for (SuQuestionOptions so : options)
                    {
                        SuQuestionOptionsJson oc = new SuQuestionOptionsJson();
                        oc.setId(so.getId());
                        oc.setOption(so.getOptions());
                        oc.setOptionHtml(so.getOptionsHtml());
                        oc.setOptions(so.getOptions());
                        oc.setOptionsHtml(so.getOptionsHtml());
                        oc.setSequ(so.getSequ());
                        oc.setCustomInput(so.getCustomInput());
                        oContents.add(oc);
                    }
                }
                qc.setSuQustionOptions(oContents);
            }
            qlist.add(qc);
        }
        surveyContent.setSuQuestion(qlist);
        response.setSurvey(surveyContent);
        return response;
    }

    @ServiceMethod(method = "mapps.survey.clientsubmit", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional(rollbackFor = Exception.class)
    public SaveSurveyAnswerResponse saveAnswer(SaveSurveyAnswerRequest request)
    {
        SaveSurveyAnswerResponse response = new SaveSurveyAnswerResponse();
        try
        {
            String jsonStr = request.getJsonStr();
            SurveyAnswerJson jsonBean = JSON.parseObject(jsonStr, SurveyAnswerJson.class);
            List<SurveyAnswer> answers = jsonBean.getSurveyAnswer();
            String surveyId = jsonBean.getSurveyId();
            // String questionId = answers.get(0).getQuestionId();
            String personId = SessionContext.getUserId();
            // 校验问卷是否过期
            SuSurvey s = surveyMapper.selectByPrimaryKey(surveyId);
            if (CLOSE.equals(s.getStatus()))
            {
                ErrorCode.fail(response, ErrorCode.CODE_300001);
                return response;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            // 校验是否已经答过题目
            map.put("surveyId", surveyId);
            map.put("personId", personId);
            SuAnswerPeople suAp = answerPeopleMapper.getAnswerPeopleByPersonQues(map);
            if (suAp == null)
            {
                ErrorCode.fail(response, ErrorCode.CODE_300002);
                return response;
            }
            else if ("1".equals(suAp.getAnswered()))
            {
                ErrorCode.fail(response, ErrorCode.CODE_300003);
                return response;
            }
            // 修改问卷答题人数
            surveyMapper.updateSurveyPersons(surveyId);
            // 修改答题者的状态
            map.put("surveyId", surveyId);
            map.put("personId", personId);
            map.put("beginTime", jsonBean.getBeginTime());
            Date submitTime = new Date();
            map.put("submitTime", submitTime);
            map.put("duration", submitTime.getTime() - jsonBean.getBeginTime().getTime());
            answerPeopleMapper.updateAnswered(map);
            // 保存答案
            for (SurveyAnswer sa : answers)
            {
                sa.setId(IDGen.uuid());
                sa.setAnswerId(personId);
                answerMapper.addAnswer(sa);
            }
            // 全部答题后修改问卷状态
            SuSurvey survey = surveyMapper.getSurveyByQuestion(surveyId);
            if (survey != null && (survey.getAnswerPersons() == survey.getTargetPersons()))
            {
                survey.setStatus(CLOSE);
                survey.setEndTime(new Date());
                surveyMapper.updateByPrimaryKeySelective(survey);
            }
        }
        catch (Exception e)
        {
            logger.error("答题保存异常！", e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return response;

    }

    @ServiceMethod(method = "mapps.survey.webget", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetSurveyListResponse getListForWeb(GetSurveyListForWebRequest request)
    {
        GetSurveyListResponse response = new GetSurveyListResponse();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(request.getTitle()))
        {
            map.put("title", request.getTitle());
        }
        if (StringUtils.isNotEmpty(request.getStatus()))
        {
            map.put("status", request.getStatus());
        }
        if (StringUtils.isNotEmpty(request.getSort()))
        {
            map.put("sort", request.getSort().replace("title1", "title"));
        }
        map.put("ecid", SessionContext.getEcId());
        map.put("userId", SessionContext.getUserId());

        PageHelper.startPage(request.getOffset(), request.getLimit());
        List<SuSurveyPojo> list = surveyMapper.getSurveyForWeb(map);
        PageInfo<SuSurveyPojo> page = new PageInfo<SuSurveyPojo>(list);
        response.setTotal(page.getTotal());
        response.setSurveyInfos(page.getList());

        return response;
    }

    @ServiceMethod(method = "mapps.survey.preview", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public PreviewSurveyResponse previewSurvey(GetSurveyContentRequest request)
    {
        PreviewSurveyResponse response = new PreviewSurveyResponse();
        String id = request.getSurveyId();
        // String personId = SessionContext.getUserId();
        SuSurvey survey = surveyMapper.selectByPrimaryKey(id);
        // SuAnswerPeople answerPeople = answerPeopleMapper.getAnswerPeopleByPerson(personId);
        SuSurveyJson surveyContent = new SuSurveyJson();
        surveyContent.setStatus(survey.getStatus());
        surveyContent.setPager(survey.getPager());
        surveyContent.setTitle(survey.getTitle());
        surveyContent.setTitleHtml(survey.getTitleHtml());
        surveyContent.setEffectiveTime(survey.getEffectiveTime());
        surveyContent.setEndTime(survey.getEndTime());
        surveyContent.setPushTo(survey.getPushTo());
        surveyContent.setId(survey.getId());
        surveyContent.setAnswerPersons(survey.getAnswerPersons());
        surveyContent.setTargetPersons(survey.getTargetPersons());
        if (!EDIT.equals(survey.getStatus()) && survey.getEffectiveTime() != null)
        {
            Date d = new Date();
            Date e = survey.getEndTime();
            if (e != null && e.getTime() < d.getTime())
            {
                d = e;
            }
            surveyContent
                    .setDurationStr(DateUtil.secToDate((d.getTime() - survey.getEffectiveTime().getTime()) / 1000));
        }
        else
        {
            surveyContent.setDurationStr("00分00秒");
        }
        SuParticipants sp = new SuParticipants();
        sp.setObjectId(id);
        surveyContent.setSuParticipants(suParticipantsMapper.select(sp));
        // 已经完成问卷调查
        // if (answerPeople != null && "1".equals(answerPeople.getAnswered()))
        // {
        // surveyContent.setSubmitTime(DateUtil.sdfHMS().format(answerPeople.getSubmitTime()));
        // }
        List<SuQuestion> list = questionMapper.getQuestionBySurveyId(id);
        List<SuQuestionJson> qlist = new ArrayList<SuQuestionJson>();
        // 获取选项
        for (SuQuestion sq : list)
        {
            SuQuestionJson qc = new SuQuestionJson();
            qc.setQuestion(sq.getQuestion());
            qc.setQuestionHtml(sq.getQuestionHtml());
            qc.setCode(sq.getCode());
            qc.setSelMax(sq.getSelMax());
            qc.setSelMin(sq.getSelMin());
            qc.setType(sq.getType());
            qc.setRequired(sq.getRequired());
            String questionId = sq.getId();
            // 文本取答案
            if (TEXT.equals(sq.getType()) || TEXTAREA.equals(sq.getType()))
            {
            }
            else
            {
                List<SuQuestionOptionsJson> oContents = new ArrayList<SuQuestionOptionsJson>();
                List<SuQuestionOptions> options = questionOptionsMapper.getQuestionOptionsByQuestionId(questionId);
                for (SuQuestionOptions so : options)
                {
                    SuQuestionOptionsJson oc = new SuQuestionOptionsJson();
                    oc.setOption(so.getOptions());
                    oc.setOptionHtml(so.getOptionsHtml());
                    oc.setOptions(so.getOptions());
                    oc.setOptionsHtml(so.getOptionsHtml());
                    oc.setSequ(so.getSequ());
                    oc.setCustomInput(so.getCustomInput());
                    oContents.add(oc);
                }
                qc.setSuQustionOptions(oContents);
            }
            qlist.add(qc);
        }
        surveyContent.setSuQuestion(qlist);
        response.setSurvey(surveyContent);
        return response;
    }

    /**
     * 问卷拷贝功能
     * 
     * @param request
     */
    @ServiceMethod(method = "mapps.survey.copy", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional(rollbackFor = Exception.class)
    public CopySurveyResponse surveyCopy(CopySurveyRequest request)
    {
        CopySurveyResponse response = new CopySurveyResponse();
        try
        {
            String copySurveyId = request.getSurveyId();
            SuSurvey survey = surveyMapper.selectByPrimaryKey(copySurveyId);
            if (survey == null)
            {
                response.fail("问卷不存在！");
            }
            // 拷贝问卷表
            String newSurveyId = IDGen.uuid();
            survey.setId(newSurveyId);
            String newTitle = survey.getTitle();
            String newTitleHtml = survey.getTitleHtml();
            if (newTitle.length() <= 48)
            {
                newTitle = newTitle + "副本";
                newTitleHtml += "副本";
            }
            survey.setTitle(newTitle);
            survey.setTitleHtml(newTitleHtml);
            survey.setCreateTime(new Date());
            survey.setEffectiveTime(null);
            survey.setEndTime(null);
            survey.setStatus(EDIT);
            survey.setTargetPersons(0l);
            survey.setAnswerPersons(0l);
            surveyMapper.insertSelective(survey);
            // 拷贝问题表
            List<SuQuestion> questionList = questionMapper.getQuestionBySurveyId(copySurveyId);
            SuSurveyQuestion sq = new SuSurveyQuestion();
            sq.setSurveyId(copySurveyId);
            List<SuSurveyQuestion> surveyQuestionList = surveyQuestionMapper.select(sq);
            // List<SuSurveyQuestion> newSurveyQuestionList = new ArrayList<SuSurveyQuestion>();
            Map<String, Long> sqMap = new HashMap<String, Long>();
            for (SuSurveyQuestion su : surveyQuestionList)
            {
                sqMap.put(su.getQuestionId(), su.getSequ());
            }
            for (SuQuestion question : questionList)
            {
                String copyQuestionId = question.getId();
                String newQuestionId = IDGen.uuid();
                List<SuQuestionOptions> optionList = questionOptionsMapper
                        .getQuestionOptionsByQuestionId(copyQuestionId);
                for (SuQuestionOptions so : optionList)
                {
                    so.setId(IDGen.uuid());
                    so.setQuestionId(newQuestionId);
                    so.setCreateTime(new Date());
                    questionOptionsMapper.insertSelective(so);
                }
                // questionOptionsMapper.insertList(optionList);
                question.setId(newQuestionId);
                question.setCreator(SessionContext.getUserId());
                question.setCreateTime(new Date());
                SuSurveyQuestion newsq = new SuSurveyQuestion();
                newsq.setQuestionId(newQuestionId);
                newsq.setSurveyId(newSurveyId);
                newsq.setSequ(sqMap.get(copyQuestionId));
                // newSurveyQuestionList.add(newsq);
                questionMapper.insertSelective(question);
                surveyQuestionMapper.insertSelective(newsq);
            }
            // questionMapper.insertList(questionList);
            // surveyQuestionMapper.insertList(newSurveyQuestionList);

        }
        catch (Exception e)
        {
            logger.error("复制问卷失败！", e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return response;
    }

    /**
     * 问卷模板拷贝功能
     * 
     * @param request
     */
    @ServiceMethod(method = "mapps.surveyTemplate.copy", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional(rollbackFor = Exception.class)
    public CopySurveyResponse surveyTemplateCopy(CopySurveyRequest request)
    {
        CopySurveyResponse response = new CopySurveyResponse();
        String copySurveyId = request.getSurveyId();
        SuSurveyTemplate surveyTemplate = surveyTemplateMapper.selectByPrimaryKey(copySurveyId);
        if (surveyTemplate == null)
        {
            response.fail("问卷模板不存在！");
        }
        // 拷贝问卷表
        String newTempalteId = IDGen.uuid();
        surveyTemplate.setId(newTempalteId);
        String newTitle = surveyTemplate.getTitle();
        String newTitleHtml = surveyTemplate.getTitleHtml();
        if (newTitle.length() <= 48)
        {
            newTitle = newTitle + "副本";
            newTitleHtml += "副本";
        }
        surveyTemplate.setTitle(newTitle);
        surveyTemplate.setTitleHtml(newTitleHtml);
        surveyTemplate.setCreateTime(new Date());
        surveyTemplate.setModifiedTime(new Date());
        surveyTemplate.setUseTimes(0L);
        surveyTemplateMapper.insertSelective(surveyTemplate);
        // 拷贝问题表
        List<SuQuestion> questionList = questionMapper.getQuestionByTemplateId(copySurveyId);
        SuTemplateQuestion sq = new SuTemplateQuestion();
        sq.setTemplateId(copySurveyId);
        List<SuTemplateQuestion> templateQuestionList = templateQuestionMapper.select(sq);
        List<SuTemplateQuestion> newTemplateQuestionList = new ArrayList<SuTemplateQuestion>();
        Map<String, Long> sqMap = new HashMap<String, Long>();
        for (SuTemplateQuestion su : templateQuestionList)
        {
            sqMap.put(su.getQuestionId(), su.getSequ());
        }
        for (SuQuestion question : questionList)
        {
            String copyQuestionId = question.getId();
            String newQuestionId = IDGen.uuid();
            List<SuQuestionOptions> optionList = questionOptionsMapper.getQuestionOptionsByQuestionId(copyQuestionId);
            for (SuQuestionOptions so : optionList)
            {
                so.setId(IDGen.uuid());
                so.setQuestionId(newQuestionId);
                questionOptionsMapper.insertSelective(so);
            }
            // questionOptionsMapper.insertList(optionList);
            question.setId(newQuestionId);
            question.setCreator(SessionContext.getUserId());
            question.setCreateTime(new Date());
            questionMapper.insertSelective(question);
            SuTemplateQuestion newsq = new SuTemplateQuestion();
            newsq.setQuestionId(newQuestionId);
            newsq.setTemplateId(newTempalteId);
            newsq.setSequ(sqMap.get(copyQuestionId));
            newTemplateQuestionList.add(newsq);
            templateQuestionMapper.insertSelective(newsq);
        }
        // questionMapper.insertList(questionList);
        // templateQuestionMapper.insertList(newTemplateQuestionList);
        return response;
    }

    @ServiceMethod(method = "mapps.survey.add", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional(rollbackFor = Exception.class)
    public SaveSurveyAnswerResponse addSurvey(SaveSurveyAnswerRequest request)
    {
        SaveSurveyAnswerResponse response = new SaveSurveyAnswerResponse();
        try
        {
            String jsonStr = request.getJsonStr();
            SuSurveyJson surveyJson = JSON.parseObject(jsonStr, SuSurveyJson.class);
            String id = IDGen.uuid();
            surveyJson.setId(id);
            SuSurvey survey = new SuSurvey();
            BeanUtils.copyProperties(surveyJson, survey);
            survey.setCreator(SessionContext.getUserId());
            survey.setEcid(SessionContext.getEcId());
            Date d = new Date();
            survey.setCreateTime(d);
            survey.setModifiedTime(d);
            int i = addSuParticipants(surveyJson);
            survey.setTargetPersons((long) i);
            survey.setAnswerPersons(0l);
            survey.setEndTime(DateUtil.getLastTime(survey.getEndTime()));
            if (PUBLISH.equals(surveyJson.getStatus()) && survey.getEffectiveTime() == null)
            {
                survey.setEffectiveTime(new Date());
            }
            else if (PUBLISH.equals(surveyJson.getStatus())
                    && (DateUtil.sdf().format(survey.getEffectiveTime()).equals((DateUtil.sdf().format(new Date())))))
            {
                survey.setEffectiveTime(new Date());
            }
            else if (PUBLISH.equals(surveyJson.getStatus())
                    && !(DateUtil.sdf().format(survey.getEffectiveTime()).equals((DateUtil.sdf().format(new Date())))))
            {
                survey.setStatus("0");
            }
            surveyMapper.insertSelective(survey);
            saveQuestion(surveyJson);
            response.setId(id);
            if (isImmediatePush(survey))
            {
                SuSurveyPojo entity = new SuSurveyPojo();
                entity.setEcid(SessionContext.getEcId());
                entity.setId(id);
                entity.setTitle(survey.getTitle());
                entity.setEndTime(survey.getEndTime());
                scheduledService.pushBySurvey(entity);
            }
        }
        catch (Exception e)
        {
            logger.error("保存问卷失败！", e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return response;
    }

    @ServiceMethod(method = "mapps.survey.update", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public SaveSurveyAnswerResponse updateSurvey(SaveSurveyAnswerRequest request)
    {
        SaveSurveyAnswerResponse response = new SaveSurveyAnswerResponse();
        try
        {
            String jsonStr = request.getJsonStr();
            SuSurveyJson surveyJson = JSON.parseObject(jsonStr, SuSurveyJson.class);
            String surveyId = surveyJson.getId();
            response.setId(surveyId);
            SuSurvey survey = new SuSurvey();
            BeanUtils.copyProperties(surveyJson, survey);
            survey.setModifier(SessionContext.getUserId());
            survey.setModifiedTime(new Date());
            int i = addSuParticipants(surveyJson);
            survey.setTargetPersons((long) i);
            survey.setEndTime(DateUtil.getLastTime(survey.getEndTime()));
            if (PUBLISH.equals(surveyJson.getStatus()) && survey.getEffectiveTime() == null)
            {
                survey.setEffectiveTime(new Date());
            }
            else if (PUBLISH.equals(surveyJson.getStatus())
                    && (DateUtil.sdf().format(survey.getEffectiveTime()).equals((DateUtil.sdf().format(new Date())))))
            {
                survey.setEffectiveTime(new Date());
            }
            else if (PUBLISH.equals(surveyJson.getStatus())
                    && !(DateUtil.sdf().format(survey.getEffectiveTime()).equals((DateUtil.sdf().format(new Date())))))
            {
                survey.setStatus("0");
            }
            surveyMapper.updateByPrimaryKeySelective(survey);
            // 删除问题
            questionOptionsMapper.deleteQuestionOptionBySurveyId(surveyId);
            questionMapper.deleteSurveyQuestionBySurveyId(surveyId);
            surveyQuestionMapper.deleteSurveyQuestionBySurveyId(surveyId);
            // 保存问题
            saveQuestion(surveyJson);
            if (isImmediatePush(survey))
            {
                SuSurveyPojo entity = new SuSurveyPojo();
                entity.setEcid(SessionContext.getEcId());
                entity.setId(surveyId);
                entity.setTitle(survey.getTitle());
                entity.setEndTime(survey.getEndTime());
                scheduledService.pushBySurvey(entity);
            }
        }
        catch (Exception e)
        {
            logger.error("保存问卷失败！", e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return response;
    }

    public boolean isImmediatePush(SuSurvey survey)
    {
        boolean immediatePushFlag = false;
        if (PUBLISH.equals(survey.getStatus()) && "1".equals(survey.getPushTo()))
        {
            Calendar nowTime = Calendar.getInstance();
            Date now = nowTime.getTime();
            if (survey.getEffectiveTime() == null)
            {
                immediatePushFlag = true;
            }
            else if (now.getTime() >= survey.getEffectiveTime().getTime())
            {
                immediatePushFlag = true;
            }
        }
        return immediatePushFlag;
    }

    public void saveQuestion(SuSurveyJson json)
    {
        String surveyId = json.getId();
        List<SuQuestionJson> list = json.getSuQuestion();
        // List<SuQuestion> questionlist = new ArrayList<SuQuestion>();
        // List<SuSurveyQuestion> suQuList = new ArrayList<SuSurveyQuestion>();
        for (SuQuestionJson questionJson : list)
        {
            SuQuestion question = new SuQuestion();
            SuSurveyQuestion suQu = new SuSurveyQuestion();
            String id = IDGen.uuid();
            BeanUtils.copyProperties(questionJson, question);
            question.setCreator(SessionContext.getUserId());
            question.setCreateTime(new Date());
            List<SuQuestionOptionsJson> optionJsonList = questionJson.getSuQustionOptions();
            // List<SuQuestionOptions> optionList = new ArrayList<SuQuestionOptions>();
            for (SuQuestionOptionsJson optionJson : optionJsonList)
            {
                SuQuestionOptions option = new SuQuestionOptions();
                BeanUtils.copyProperties(optionJson, option);
                option.setId(IDGen.uuid());
                option.setQuestionId(id);
                option.setCreator(SessionContext.getUserId());
                option.setCreateTime(new Date());
                option.setOptions(optionJson.getOption());
                option.setOptionsHtml(optionJson.getOptionHtml());
                // optionList.add(option);
                questionOptionsMapper.insertSelective(option);
            }
            // questionOptionsMapper.insertList(optionList);
            question.setId(id);
            question.setCreator(SessionContext.getUserId());
            question.setCreateTime(new Date());
            question.setCode(questionJson.getSequ().toString());
            question.setUsable("1");
            // questionlist.add(question);
            suQu.setQuestionId(id);
            suQu.setSurveyId(surveyId);
            suQu.setSequ(questionJson.getSequ());
            // suQuList.add(suQu);
            questionMapper.insertSelective(question);
            surveyQuestionMapper.insertSelective(suQu);
        }
        // questionMapper.insertList(questionlist);
        // surveyQuestionMapper.insertList(suQuList);
    }

    public int addSuParticipants(SuSurveyJson surveyJson) throws Exception
    {
        Map<String, SuAnswerPeople> inMap = new HashMap<String, SuAnswerPeople>();
        // su_participants
        SuParticipants deletePriv = new SuParticipants();
        deletePriv.setType("inner");
        deletePriv.setObjectId(surveyJson.getId());
        suParticipantsMapper.delete(deletePriv);
        for (SuParticipants entity : surveyJson.getSuParticipants())
        {
            String id = IDGen.uuid();
            entity.setId(id);
            entity.setType("inner");
            entity.setObjectId(surveyJson.getId());
            entity.setObjectName(surveyJson.getTitle());
            suParticipantsMapper.insertSelective(entity);
            if ("user".equals(entity.getEntityType()))
            {
                SuAnswerPeople info = new SuAnswerPeople();
                info.setId(IDGen.uuid());
                info.setSurveyId(surveyJson.getId());
                info.setPersonId(entity.getEntityId());
                info.setPersonName(entity.getEntityName());
                info.setPersonType("inner");
                inMap.put(info.getPersonId(), info);
            }
            else if ("dept".equals(entity.getEntityType()))
            {
                GetUsersRequest guReq = new GetUsersRequest();
                guReq.setDepUuid(entity.getEntityId());
                guReq.setDepScope("1");
                GetUsersResponse guRes = thirdPartAccessService.getUsersByDept(guReq);
                for (MyUser user : guRes.getUserInfos())
                {
                    SuAnswerPeople info = new SuAnswerPeople();
                    info.setId(IDGen.uuid());
                    info.setSurveyId(surveyJson.getId());
                    info.setPersonId(user.getUserUuid());
                    info.setPersonName(user.getUserName());
                    info.setPersonType("inner");
                    inMap.put(info.getPersonId(), info);
                }
            }

        }
        SuAnswerPeople people = new SuAnswerPeople();
        people.setPersonType("inner");
        people.setSurveyId(surveyJson.getId());
        answerPeopleMapper.delete(people);
        for (SuAnswerPeople entity : inMap.values())
        {
            answerPeopleMapper.insertSelective(entity);
        }
        return inMap.size();
    }

    @ServiceMethod(method = "mapps.survey.answerPeople", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetSurveyAnswerPeopleListResponse getSurveyAnswerPeopleList(GetSurveyAnswerPeopleListRequest request)
    {
        GetSurveyAnswerPeopleListResponse response = new GetSurveyAnswerPeopleListResponse();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(request.getSort()))
        {
            map.put("sort", request.getSort());
        }
        if (StringUtils.isNotEmpty(request.getSurveyId()))
        {
            map.put("surveyId", request.getSurveyId());
        }
        PageHelper.startPage(request.getOffset(), request.getLimit());
        List<SuAnswerPeoplePojo> list = answerPeopleMapper.getAnswerPeopleList(map);
        PageInfo<SuAnswerPeoplePojo> page = new PageInfo<SuAnswerPeoplePojo>(list);
        response.setAnswerPeople(list);
        response.setTotal(page.getTotal());
        response.setOrgId(SessionContext.getOrgId());
        return response;
    }

    @ServiceMethod(method = "mapps.surveyTemplate.webget", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetSurveyTemplateListResponse getSurveyTemplate(GetSurveyTemplateListRequest request)
    {
        GetSurveyTemplateListResponse response = new GetSurveyTemplateListResponse();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(request.getTitle()))
        {
            map.put("title", request.getTitle());
        }
        map.put("type", request.getType());
        if ("2".equals(request.getType()))
        {
            map.put("userId", SessionContext.getUserId());
        }
        if (StringUtils.isNotEmpty(request.getSort()))
        {
            map.put("sort", request.getSort());
        }
        if (StringUtils.isNotEmpty(request.getDefaultid()))
        {
            map.put("defaultid", request.getDefaultid());
        }
        PageHelper.startPage(request.getOffset(), request.getLimit());
        List<SuSurveyTemplatePojo> list = surveyTemplateMapper.getSurveyTemplate(map);
        PageInfo<SuSurveyTemplatePojo> page = new PageInfo<SuSurveyTemplatePojo>(list);
        response.setSurveyInfos(page.getList());
        response.setTotal(page.getTotal());
        return response;
    }

    @ServiceMethod(method = "mapps.surveyTemplate.add", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional(rollbackFor = Exception.class)
    public SaveSurveyAnswerResponse addSurveyTemplate(SaveSurveyAnswerRequest request)
    {
        SaveSurveyAnswerResponse response = new SaveSurveyAnswerResponse();
        try
        {
            String jsonStr = request.getJsonStr();
            SuSurveyTemplateJson surveyTemplateJson = JSON.parseObject(jsonStr, SuSurveyTemplateJson.class);
            String id = IDGen.uuid();
            surveyTemplateJson.setId(id);
            SuSurveyTemplate surveyTemplate = new SuSurveyTemplate();
            BeanUtils.copyProperties(surveyTemplateJson, surveyTemplate);
            // surveyTemplate.setType("2");
            Date d = new Date();
            surveyTemplate.setCreateTime(d);
            surveyTemplate.setModifiedTime(d);
            // if (StringUtils.isNotEmpty(surveyTemplate.getGroupId()))
            // {
            // surveyTemplate.setType("1");
            // }
            surveyTemplate.setOwner(SessionContext.getUserId());
            surveyTemplate.setQuestions((long) surveyTemplateJson.getSuQuestion().size());
            surveyTemplateMapper.insertSelective(surveyTemplate);
            saveTemplateQuestion(surveyTemplateJson);
        }
        catch (Exception e)
        {
            logger.error("新增模板失败！", e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return response;
    }

    @ServiceMethod(method = "mapps.surveyTemplate.update", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    @Transactional(rollbackFor = Exception.class)
    public SaveSurveyAnswerResponse updateSurveyTemplate(SaveSurveyAnswerRequest request)
    {
        SaveSurveyAnswerResponse response = new SaveSurveyAnswerResponse();
        try
        {
            String jsonStr = request.getJsonStr();
            SuSurveyTemplateJson surveyTemplateJson = JSON.parseObject(jsonStr, SuSurveyTemplateJson.class);
            String surveyId = surveyTemplateJson.getId();
            response.setId(surveyId);
            SuSurveyTemplate surveyTemplate = new SuSurveyTemplate();
            BeanUtils.copyProperties(surveyTemplateJson, surveyTemplate);
            surveyTemplate.setQuestions((long) surveyTemplateJson.getSuQuestion().size());
            surveyTemplate.setModifiedTime(new Date());
            surveyTemplateMapper.updateByPrimaryKeySelective(surveyTemplate);
            // 删除问题
            questionOptionsMapper.deleteQuestionOptionByTemplateId(surveyId);
            questionMapper.deleteTemplateQuestionBySurveyId(surveyId);
            templateQuestionMapper.deleteTemplateQuestionBySurveyId(surveyId);
            // 保存问题
            saveTemplateQuestion(surveyTemplateJson);
        }
        catch (Exception e)
        {
            logger.error("修改模板失败！", e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return response;
    }

    public void saveTemplateQuestion(SuSurveyTemplateJson json)
    {
        String templateId = json.getId();
        List<SuQuestionJson> list = json.getSuQuestion();
        // List<SuQuestion> questionlist = new ArrayList<SuQuestion>();
        List<SuTemplateQuestion> suQuList = new ArrayList<SuTemplateQuestion>();
        for (SuQuestionJson questionJson : list)
        {
            SuQuestion question = new SuQuestion();
            SuTemplateQuestion suQu = new SuTemplateQuestion();
            String id = IDGen.uuid();
            BeanUtils.copyProperties(questionJson, question);
            question.setCreator(SessionContext.getUserId());
            question.setCreateTime(new Date());
            List<SuQuestionOptionsJson> optionJsonList = questionJson.getSuQustionOptions();
            List<SuQuestionOptions> optionList = new ArrayList<SuQuestionOptions>();
            for (SuQuestionOptionsJson optionJson : optionJsonList)
            {
                SuQuestionOptions option = new SuQuestionOptions();
                BeanUtils.copyProperties(optionJson, option);
                option.setId(IDGen.uuid());
                option.setQuestionId(id);
                option.setCreator(SessionContext.getUserId());
                option.setCreateTime(new Date());
                option.setOptions(optionJson.getOption());
                option.setOptionsHtml(optionJson.getOptionHtml());
                optionList.add(option);
                questionOptionsMapper.insertSelective(option);
            }
            // questionOptionsMapper.insertList(optionList);
            question.setId(id);
            question.setUsable("1");
            question.setCode(questionJson.getSequ().toString());
            question.setCreator(SessionContext.getUserId());
            question.setCreateTime(new Date());
            // questionlist.add(question);
            suQu.setQuestionId(id);
            suQu.setTemplateId(templateId);
            suQu.setSequ(questionJson.getSequ());
            suQuList.add(suQu);
            questionMapper.insertSelective(question);
            templateQuestionMapper.insertSelective(suQu);
        }
        // questionMapper.insertList(questionlist);
        // templateQuestionMapper.insertList(suQuList);
    }

    @ServiceMethod(method = "mapps.surveyTemplate.preview", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public PreviewSurveyTemplateResponse previewSurveyTemplate(GetSurveyTemplateRequest request)
    {
        PreviewSurveyTemplateResponse response = new PreviewSurveyTemplateResponse();
        String id = request.getSurveyTemplateId();
        // String personId = SessionContext.getUserId();
        SuSurveyTemplate survey = surveyTemplateMapper.selectByPrimaryKey(id);
        // SuAnswerPeople answerPeople = answerPeopleMapper.getAnswerPeopleByPerson(personId);
        SuSurveyTemplateJson surveyContent = new SuSurveyTemplateJson();
        surveyContent.setStatus(survey.getStatus());
        surveyContent.setPager(survey.getPager());
        surveyContent.setTitle(survey.getTitle());
        surveyContent.setTitleHtml(survey.getTitleHtml());
        surveyContent.setId(survey.getId());
        surveyContent.setGroupId(survey.getGroupId());
        List<SuQuestion> list = questionMapper.getQuestionByTemplateId(id);
        List<SuQuestionJson> qlist = new ArrayList<SuQuestionJson>();
        // 获取选项
        for (SuQuestion sq : list)
        {
            SuQuestionJson qc = new SuQuestionJson();
            qc.setQuestion(sq.getQuestion());
            qc.setCode(sq.getCode());
            qc.setSelMax(sq.getSelMax());
            qc.setSelMax(sq.getSelMin());
            qc.setType(sq.getType());
            qc.setRequired(sq.getRequired());
            String questionId = sq.getId();
            // 文本取答案
            if (TEXT.equals(sq.getType()) || TEXTAREA.equals(sq.getType()))
            {
            }
            else
            {
                List<SuQuestionOptionsJson> oContents = new ArrayList<SuQuestionOptionsJson>();
                List<SuQuestionOptions> options = questionOptionsMapper.getQuestionOptionsByQuestionId(questionId);
                for (SuQuestionOptions so : options)
                {
                    SuQuestionOptionsJson oc = new SuQuestionOptionsJson();
                    oc.setOption(so.getOptions());
                    oc.setOptionHtml(so.getOptionsHtml());
                    oc.setOptions(so.getOptions());
                    oc.setOptionsHtml(so.getOptionsHtml());
                    oc.setSequ(so.getSequ());
                    oc.setCustomInput(so.getCustomInput());
                    oContents.add(oc);
                }
                qc.setSuQustionOptions(oContents);
            }
            qlist.add(qc);
        }
        surveyContent.setSuQuestion(qlist);
        response.setSurvey(surveyContent);
        return response;
    }

    @ServiceMethod(method = "mapps.surveyTemplate.groupSave", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public SaveSurveyGroupResponse saveSurveyGroup(SaveSurveyGroupRequest request)
    {
        SaveSurveyGroupResponse response = new SaveSurveyGroupResponse();
        String groupId = request.getGroupId();
        if (StringUtils.isEmpty(groupId))
        {
            // 先查出最大的sequ
            Long i = surveyGroupMapper.getMaxSequ(SessionContext.getEcId());
            if (i == null)
            {
                i = 0l;
            }
            String id = IDGen.uuid();
            SuSurveyGroup sg = new SuSurveyGroup();
            sg.setId(id);
            sg.setEcid(SessionContext.getEcId());
            sg.setName(request.getGroupName());
            sg.setSequ(i + 1);
            surveyGroupMapper.insert(sg);
            response.setId(id);
            return response;
        }
        else
        {
            SuSurveyGroup sg = new SuSurveyGroup();
            sg.setId(groupId);
            sg.setEcid(SessionContext.getEcId());
            sg.setName(request.getGroupName());
            surveyGroupMapper.updateGroupName(sg);
            response.setId(groupId);
            return response;
        }
    }

    @ServiceMethod(method = "mapps.surveyTemplate.groupDel", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public DelSurveyGroupResponse delSurveyGroup(DelSurveyGroupRequest request)
    {
        DelSurveyGroupResponse response = new DelSurveyGroupResponse();
        surveyGroupMapper.deleteByPrimaryKey(request.getGroupId());
        return response;
    }

    @ServiceMethod(method = "mapps.surveyTemplate.groupGet", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetSurveyGroupResponse getSurveyGroupByEcid(GetSurveyGroupRequest request)
    {
        GetSurveyGroupResponse res = new GetSurveyGroupResponse();
        SuSurveyGroup group = new SuSurveyGroup();
        group.setEcid(SessionContext.getEcId());
        List<SuSurveyGroup> list = surveyGroupMapper.select(group);
        Collections.sort(list, new Comparator<SuSurveyGroup>()
        {
            @Override
            public int compare(SuSurveyGroup o1, SuSurveyGroup o2)
            {
                long i = o1.getSequ() - o2.getSequ();
                return (int) i;
            }
        });
        res.setSurveyGroups(list);
        if (SessionContext.isAdmin())
        {
            res.setIsAdmin("1");
        }
        else
        {
            res.setIsAdmin("0");
        }
        return res;
    }

    @ServiceMethod(method = "mapps.surveyTemplate.delete", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public DelSurveyTemplateResponse deleteSurveyTemplate(DelSurveyTemplateRequest req)
    {
        DelSurveyTemplateResponse res = new DelSurveyTemplateResponse();
        String templateId = req.getSurveyTemplateId();
        questionOptionsMapper.deleteQuestionOptionBySurveyId(templateId);
        questionMapper.deleteTemplateQuestionBySurveyId(templateId);
        SuTemplateQuestion sq = new SuTemplateQuestion();
        sq.setTemplateId(templateId);
        templateQuestionMapper.delete(sq);
        surveyTemplateMapper.deleteByPrimaryKey(templateId);
        return res;
    }

    @ServiceMethod(method = "mapps.survey.delete", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public DelSurveyResponse deleteSurvey(DelSurveyRequest req)
    {
        DelSurveyResponse res = new DelSurveyResponse();
        String surveyId = req.getSurveyId();
        questionOptionsMapper.deleteQuestionOptionBySurveyId(surveyId);
        questionMapper.deleteSurveyQuestionBySurveyId(surveyId);
        SuQuestion sq = new SuQuestion();
        sq.setId(surveyId);
        questionMapper.delete(sq);
        surveyMapper.deleteByPrimaryKey(surveyId);
        return res;
    }

    @ServiceMethod(method = "mapps.survey.dataAnalysis", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetSurveyAnalysisResponse getSurveyDataAnalysis(GetSurveyAnalysisRequest req)
    {
        GetSurveyAnalysisResponse res = new GetSurveyAnalysisResponse();
        SuAnalysisCondition condition = JSON.parseObject(req.getJsonStr(), SuAnalysisCondition.class);
        String surveyId = condition.getSurveyId();
        SuSurvey survey = surveyMapper.selectByPrimaryKey(surveyId);
        res.setStatus(survey.getStatus());
        res.setAnswerPersons(survey.getAnswerPersons());
        if (!EDIT.equals(survey.getStatus()) && survey.getEffectiveTime() != null)
        {
            Date d = new Date();
            Date e = survey.getEndTime();
            if (e != null && e.getTime() < d.getTime())
            {
                d = e;
            }
            res.setDurationStr(DateUtil.secToDate((d.getTime() - survey.getEffectiveTime().getTime()) / 1000));
        }
        else
        {
            res.setDurationStr("00分00秒");
        }
        List<SuAnalysisOption> aopList = condition.getOption();
        String sql = "";
        for (SuAnalysisOption so : aopList)
        {
            // 0表示没选择
            if ("0".equals(so.getIsSelect()))
            {
                if (StringUtils.isNotEmpty(sql))
                {
                    sql += " intersect ";
                }
                sql += "select distinct answer_id from su_answer where question_id='" + so.getQuestionId()
                        + "' and answer_id not in (select distinct answer_id from su_answer where question_id='"
                        + so.getQuestionId() + "' and sequ=" + so.getOptionSequ() + ")";
            }
            else
            {
                if (StringUtils.isNotEmpty(sql))
                {
                    sql += " intersect ";
                }
                sql += "select distinct answer_id from su_answer where question_id='" + so.getQuestionId()
                        + "' and sequ=" + so.getOptionSequ();
            }
        }
        List<SuQuestionJson> list = questionMapper.getSelectQuestion(surveyId);
        Map<String, Object> conditionMap = new HashMap<String, Object>();
        List<AnalysisQuestionJson> resJson = new ArrayList<AnalysisQuestionJson>();
        if (aopList != null && aopList.size() > 0)
        {
            List<String> answerList = answerPeopleMapper.getAnalysisAnswer(sql);
            if (answerList.size() > 0)
            {
                conditionMap.put("answerId", answerList);
            }
            else
            {
                List<String> l = new ArrayList<String>();
                l.add("xxx");
                conditionMap.put("answerId", l);
            }
        }
        for (SuQuestionJson sq : list)
        {
            conditionMap.put("questionId", sq.getId());
            List<AnalysisQuestionOptionsJson> resQueJson = questionOptionsMapper.getQuestionAnalysis(conditionMap);
            // 获取该题的答题人数
            long total = questionOptionsMapper.getQuestionAnswerCount(conditionMap);
            DecimalFormat df = new DecimalFormat("0.00");
            for (AnalysisQuestionOptionsJson aqj : resQueJson)
            {
                if (aqj.getSelected() == null)
                {
                    aqj.setSelected(0l);
                }
                String geff = "0%";
                if (total != 0)
                {
                    geff = df.format((aqj.getSelected() * 100 / total)) + '%';
                }
                aqj.setSelectedPercent(geff);
            }
            AnalysisQuestionJson aj = new AnalysisQuestionJson();
            aj.setSuQustionOptions(resQueJson);
            aj.setId(sq.getId());
            aj.setQuestion(sq.getQuestion());
            aj.setSequ(sq.getSequ());
            aj.setTotal(String.valueOf(total));
            aj.setType(sq.getType());
            resJson.add(aj);
        }
        res.setAnalysisQuestions(resJson);
        return res;
    }

    @ServiceMethod(method = "mapps.survey.question", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetSurveyQuestionResponse getSurveyQuestion(GetSurveyQuestionRequest request)
    {
        GetSurveyQuestionResponse res = new GetSurveyQuestionResponse();
        List<SuQuestionJson> list = questionMapper.getSelectQuestion(request.getSurveyId());
        res.setQuestions(list);
        return res;
    }

    @ServiceMethod(method = "mapps.survey.option", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetSurveyQuestionOptionResponse getQuestionOption(GetSurveyQuestionOptionRequest request)
    {
        GetSurveyQuestionOptionResponse res = new GetSurveyQuestionOptionResponse();
        List<SuQuestionOptions> list = questionOptionsMapper.getQuestionOptionsByQuestionId(request.getQuestionId());
        res.setOptions(list);
        return res;
    }

    @RequestMapping("/exportCVS")
    public void exportListData(String orgId, String surveyId, HttpServletResponse response)
    {
        ServletOutputStream os = null;
        try
        {
            SuSurvey ssInfo = surveyMapper.selectByPrimaryKey(surveyId);
            String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(ssInfo.getCreateTime()) + "_"
                    + ssInfo.getTitle() + "_原始数据.csv";
            Map<String, String> map = getTitleMap(surveyId);
            List<LinkedHashMap<String, String>> list = getExportLsitData(surveyId, orgId);
            response.setContentType("application/csv;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
            os = response.getOutputStream();
            StringBuffer buf = new StringBuffer();

            if (null != list && list.size() > 0)
            {
                // 输出列头
                for (String key : map.keySet())
                {
                    buf.append(map.get(key)).append(CSV_COLUMN_SEPARATOR);
                }
                buf.append(CSV_RN);
                // 输出数据
                for (int i = 0; i < list.size(); i++)
                {
                    LinkedHashMap<String, String> dmap = list.get(i);
                    for (String dk : map.keySet())
                    {
                        buf.append(dmap.get(dk) == null ? "" : dmap.get(dk)).append(CSV_COLUMN_SEPARATOR);
                    }
                    buf.append(CSV_RN);
                }
            }
            // 写出响应
            os.write(new byte[]
            {
                (byte) 0xEF, (byte) 0xBB, (byte) 0xBF
            });
            os.write(buf.toString().getBytes("UTF-8"));
            os.flush();
        }
        catch (IOException e)
        {
            logger.error("输出文件失败！", e.getMessage(), e);
        }
        finally
        {
            try
            {
                if (os != null)
                {
                    os.close();
                }
            }
            catch (Exception e)
            {
                logger.error("关闭流失败！", e.getMessage(), e);
            }
        }

    }

    public List<LinkedHashMap<String, String>> getExportLsitData(String surveyId, String orgId)
    {
        List<LinkedHashMap<String, String>> exportData = new ArrayList<LinkedHashMap<String, String>>();
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("surveyId", surveyId);
            List<SuAnswerPeoplePojo> list = answerPeopleMapper.getAnswerPeopleList(map);
            if (list == null || list.size() == 0)
            {
                return exportData;
            }
            // 获取人员信息
            String loginIds = "";
            for (SuAnswerPeoplePojo sp : list)
            {
                loginIds += sp.getPersonId() + ",";
            }
            if (loginIds.length() > 0)
            {
                loginIds = loginIds.substring(0, loginIds.length() - 1);
            }
            List<MyUser> userList = thirdPartAccessService.getUserInfos(orgId, loginIds);
            Map<String, MyUser> userMap = new HashMap<String, MyUser>();
            for (MyUser user : userList)
            {
                userMap.put(user.getLoginId(), user);
            }
            // 查询每个人的答题情况
            for (int i = 0; i < list.size(); i++)
            {
                SuAnswerPeoplePojo sap = list.get(i);
                String userId = sap.getPersonId();
                map.put("userId", userId);
                List<SuAnswer> alist = answerMapper.getExportData(map);
                Set<String> key = new HashSet<String>();
                LinkedHashMap<String, String> dmap = new LinkedHashMap<String, String>();
                MyUser userInfo = userMap.get(userId);
                dmap.put("1", userId);
                dmap.put("2", userInfo != null && StringUtils.isNotEmpty(userInfo.getDeptName())
                        ? userInfo.getDeptName() : "");
                dmap.put("3", userInfo != null && StringUtils.isNotEmpty(userInfo.getUserName())
                        ? userInfo.getUserName() : "");
                dmap.put("4", userInfo != null && StringUtils.isNotEmpty(userInfo.getEmailAddress())
                        ? userInfo.getEmailAddress() : "");
                dmap.put("5", sap.getBeginTimeStr());
                dmap.put("6", sap.getSubmitTimeStr());
                dmap.put("7", sap.getDurationStr());
                for (SuAnswer sa : alist)
                {
                    if (key.add(sa.getQuestionId()))
                    {
                        dmap.put(sa.getQuestionId(), StringUtils.isEmpty(sa.getAnswer()) ? "" : sa.getAnswer());
                    }
                    else
                    {
                        String value = dmap.get(sa.getQuestionId()) + ";"
                                + (StringUtils.isEmpty(sa.getAnswer()) ? "" : sa.getAnswer());
                        dmap.put(sa.getQuestionId(), value);
                    }
                }
                exportData.add(dmap);
            }
        }
        catch (Exception e)
        {
            logger.error("查询导出数据失败！", e.getMessage(), e);
        }
        return exportData;
    }

    public Map<String, String> getTitleMap(String surveyId)
    {
        List<SuQuestionJson> questionList = questionMapper.getExportQuestion(surveyId);
        Map<String, String> titleMap = new LinkedHashMap<String, String>();
        titleMap.put("1", "用户名");
        titleMap.put("2", "部门");
        titleMap.put("3", "姓名");
        titleMap.put("4", "邮箱");
        titleMap.put("5", "答题时间");
        titleMap.put("6", "提交时间");
        titleMap.put("7", "答题时长");

        for (int i = 0; i < questionList.size(); i++)
        {
            titleMap.put(questionList.get(i).getId(), "Q" + (i + 1) + "：" + questionList.get(i).getQuestion());
        }
        return titleMap;
    }

    @RequestMapping(value = "/exportChartData")
    public void exportChartData(@RequestParam("jsonData") String jsonData, HttpServletResponse response)
    {
        OutputStream os = null;
        WritableWorkbook wbook = null;
        try
        {
            SuAnalysisCondition condition = JSON.parseObject(jsonData, SuAnalysisCondition.class);
            SuSurvey ssInfo = surveyMapper.selectByPrimaryKey(condition.getSurveyId());
            String fileName = ssInfo.getTitle() + "_统计报告" + new SimpleDateFormat("yyyy-MM-ddHHmmss").format(new Date())
                    + ".xls";
            response.reset();
            response.setContentType("application/ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));// +
            os = response.getOutputStream();
            wbook = Workbook.createWorkbook(os);
            WritableSheet wsheet = wbook.createSheet("问卷调查", 0);

            GetSurveyAnalysisRequest req = new GetSurveyAnalysisRequest();
            req.setJsonStr(jsonData);

            GetSurveyAnalysisResponse res = getSurveyDataAnalysis(req);
            int line = 0;

            WritableCellFormat cellFormat_left = new WritableCellFormat();
            cellFormat_left.setAlignment(jxl.format.Alignment.LEFT);
            cellFormat_left.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            WritableCellFormat cellFormat_right = new WritableCellFormat();
            cellFormat_right.setAlignment(jxl.format.Alignment.RIGHT);
            cellFormat_right.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

            List<SuAnalysisOption> conption = condition.getOption();
            for (SuAnalysisOption so : conption)
            {
                Label label0 = new Label(0, line, "筛选条件" + (line + 1), cellFormat_left);
                wsheet.addCell(label0);
                String questionId = so.getQuestionId();
                SuQuestion s = questionMapper.selectByPrimaryKey(questionId);
                Label label1 = new Label(1, line, s.getQuestion(), cellFormat_left);
                wsheet.addCell(label1);
                String type = so.getIsSelect();
                if ("0".equals(type))
                {
                    type = "没有选择";
                }
                else
                {
                    type = "选择了";
                }
                Label label2 = new Label(2, line, type, cellFormat_left);
                wsheet.addCell(label2);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("questionId", so.getQuestionId());
                map.put("sequ", Long.valueOf(so.getOptionSequ()));
                String option = questionOptionsMapper.getOptionByQuestionAndSequ(map);
                Label label3 = new Label(3, line, option, cellFormat_left);
                wsheet.addCell(label3);
                line++;
            }
            line++;

            List<AnalysisQuestionJson> list = res.getAnalysisQuestions();
            int i = 1;
            for (AnalysisQuestionJson aj : list)
            {
                wsheet.mergeCells(0, line, 2, line);
                Label label0 = new Label(0, line, "Q" + i + "." + aj.getQuestion(), cellFormat_left);
                i++;
                wsheet.addCell(label0);
                line++;
                Label label1 = new Label(0, line, "选项", cellFormat_left);
                wsheet.addCell(label1);
                Label label2 = new Label(1, line, "计数", cellFormat_left);
                wsheet.addCell(label2);
                Label label3 = new Label(2, line, "占比", cellFormat_left);
                wsheet.addCell(label3);
                line++;
                List<AnalysisQuestionOptionsJson> spj = aj.getSuQustionOptions();
                for (AnalysisQuestionOptionsJson aqo : spj)
                {
                    Label label4 = new Label(0, line, aqo.getOption(), cellFormat_left);
                    wsheet.addCell(label4);
                    Label label5 = new Label(1, line, aqo.getSelected().toString(), cellFormat_right);
                    wsheet.addCell(label5);
                    Label label6 = new Label(2, line, aqo.getSelectedPercent(), cellFormat_left);
                    wsheet.addCell(label6);
                    line++;
                }
                wsheet.mergeCells(0, line, 2, line);
                Label label7 = new Label(0, line, "答题人数：" + aj.getTotal(), cellFormat_left);
                wsheet.addCell(label7);
                line++;
            }
            // 写出响应
            wbook.write();
            response.flushBuffer();
            os.flush();
        }
        catch (Exception e)
        {
            logger.error("输出文件失败！", e.getMessage(), e);
        }
        finally
        {
            try
            {
                if (wbook != null)
                {
                    wbook.close();
                }
                if (os != null)
                {
                    os.close();
                }
            }
            catch (Exception e)
            {
                logger.error("关闭流失败！", e.getMessage(), e);
            }
        }

    }

    @ServiceMethod(method = "mapps.survey.sessionforever", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetSurveyGroupResponse setSessionForever(GetSurveyGroupRequest request)
    {
        GetSurveyGroupResponse response = new GetSurveyGroupResponse();
        return response;
    }

    @ServiceMethod(method = "mapps.survey.close", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public DelSurveyResponse closeSurvey(DelSurveyRequest request)
    {
        DelSurveyResponse response = new DelSurveyResponse();
        String surveyId = request.getSurveyId();
        SuSurvey ss = new SuSurvey();
        ss.setId(surveyId);
        ss.setStatus(CLOSE);
        ss.setEndTime(new Date());
        surveyMapper.updateByPrimaryKeySelective(ss);
        return response;
    }

    @ServiceMethod(method = "mapps.survey.webdetail", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetSurveyContentResponse getSurveyContentToWeb(GetSurveyContentToWebRequest request)
    {
        GetSurveyContentResponse response = new GetSurveyContentResponse();
        String id = request.getSurveyId();
        String personId = request.getPersonId();
        SuSurvey survey = surveyMapper.selectByPrimaryKey(id);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("personId", personId);
        map.put("surveyId", id);
        SuAnswerPeople answerPeople = answerPeopleMapper.getAnswerPeopleByPerson(map);
        SuSurveyJson surveyContent = new SuSurveyJson();
        surveyContent.setStatus(survey.getStatus());
        surveyContent.setPager(survey.getPager());
        surveyContent.setTitle(survey.getTitle());
        surveyContent.setCreateTime(new Date());
        // 已经完成问卷调查
        if (answerPeople != null && "1".equals(answerPeople.getAnswered()))
        {
            surveyContent.setSubmitTime(DateUtil.sdfHMS().format(answerPeople.getSubmitTime()));
        }
        else
        {
            surveyContent.setSubmitTime("");
        }
        List<SuQuestion> list = questionMapper.getQuestionBySurveyId(id);
        List<SuQuestionJson> qlist = new ArrayList<SuQuestionJson>();
        // 获取选项
        for (SuQuestion sq : list)
        {
            SuQuestionJson qc = new SuQuestionJson();
            qc.setId(sq.getId());
            qc.setCode(sq.getCode());
            qc.setSelMax(sq.getSelMax());
            qc.setSelMin(sq.getSelMin());
            qc.setType(sq.getType());
            qc.setRequired(sq.getRequired());
            qc.setQuestion(sq.getQuestion());
            qc.setQuestionHtml(sq.getQuestionHtml());
            String questionId = sq.getId();
            map.put("questionId", questionId);
            // 文本取答案
            if (TEXT.equals(sq.getType()) || TEXTAREA.equals(sq.getType()))
            {
                if (answerPeople != null && "1".equals(answerPeople.getAnswered()))
                {
                    SuAnswer answer = answerMapper.getTextAnswerByQuestion(map);
                    if (answer != null)
                    {
                        qc.setTextValue(answer.getAnswer());
                    }
                }
            }
            else
            {
                List<SuQuestionOptionsJson> oContents = new ArrayList<SuQuestionOptionsJson>();
                if (answerPeople != null && "1".equals(answerPeople.getAnswered()))
                {
                    oContents = questionOptionsMapper.getOptionsAnswersByQuestionId(map);
                }
                else
                {
                    List<SuQuestionOptions> options = questionOptionsMapper.getQuestionOptionsByQuestionId(questionId);
                    for (SuQuestionOptions so : options)
                    {
                        SuQuestionOptionsJson oc = new SuQuestionOptionsJson();
                        oc.setId(so.getId());
                        oc.setOption(so.getOptions());
                        oc.setOptionHtml(so.getOptionsHtml());
                        oc.setOptions(so.getOptions());
                        oc.setOptionsHtml(so.getOptionsHtml());
                        oc.setSequ(so.getSequ());
                        oc.setCustomInput(so.getCustomInput());
                        oContents.add(oc);
                    }
                }
                qc.setSuQustionOptions(oContents);
            }
            qlist.add(qc);
        }
        surveyContent.setSuQuestion(qlist);
        response.setSurvey(surveyContent);
        return response;
    }

    @ServiceMethod(method = "mapps.surveyTemplate.useTimes", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public CopySurveyResponse addSurveyTemplateUseTimes(CopySurveyRequest request)
    {
        CopySurveyResponse response = new CopySurveyResponse();
        try
        {
            String surveyId = request.getSurveyId();
            surveyTemplateMapper.updateUseTimes(surveyId);
        }
        catch (Exception e)
        {
            logger.error("修改模板引用次数失败！", e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return response;
    }

}
