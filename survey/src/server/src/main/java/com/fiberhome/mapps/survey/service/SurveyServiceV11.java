package com.fiberhome.mapps.survey.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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
import com.fiberhome.mapps.survey.entity.SuSurveyPojo;
import com.fiberhome.mapps.survey.request.GetMySurveyRequest;
import com.fiberhome.mapps.survey.response.GetSurveyListResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean(version = "1.0")
@Controller
public class SurveyServiceV11
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

    @ServiceMethod(method = "mapps.survey.mysurvey", group = "survey", groupTitle = "问卷调查服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public GetSurveyListResponse getListForWeb(GetMySurveyRequest request)
    {
        GetSurveyListResponse response = new GetSurveyListResponse();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(request.getTitle()))
        {
            map.put("title", request.getTitle());
        }
        if (StringUtils.isNotEmpty(request.getMystatus()))
        {
            map.put("mystatus", request.getMystatus());
        }
        if (StringUtils.isNotEmpty(request.getSort()))
        {
            map.put("sort", request.getSort().replace("title1", "title"));
        }
        map.put("ecid", SessionContext.getEcId());
        map.put("userId", SessionContext.getUserId());

        PageHelper.startPage(request.getOffset(), request.getLimit());
        List<SuSurveyPojo> list = surveyMapper.getMySurvey(map);
        PageInfo<SuSurveyPojo> page = new PageInfo<SuSurveyPojo>(list);
        response.setTotal(page.getTotal());
        response.setSurveyInfos(page.getList());

        return response;
    }

}
