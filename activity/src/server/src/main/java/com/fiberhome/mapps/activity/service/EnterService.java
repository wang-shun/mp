package com.fiberhome.mapps.activity.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import com.fiberhome.mapps.activity.dao.AtActivityMapper;
import com.fiberhome.mapps.activity.dao.AtEnterMapper;
import com.fiberhome.mapps.activity.entity.AtActivity;
import com.fiberhome.mapps.activity.entity.AtEnter;
import com.fiberhome.mapps.activity.request.EnterRequest;
import com.fiberhome.mapps.activity.request.ExportEnterRequest;
import com.fiberhome.mapps.activity.request.QueryEnterRequest;
import com.fiberhome.mapps.activity.response.EnterResponse;
import com.fiberhome.mapps.activity.response.ExportEnterResponse;
import com.fiberhome.mapps.activity.response.QueryEnterResponse;
import com.fiberhome.mapps.intergration.session.SessionContext;
import com.fiberhome.mapps.utils.ErrorCode;
import com.fiberhome.mapps.utils.ExcelUtils;
import com.fiberhome.mapps.utils.IDGen;
import com.fiberhome.mapps.utils.LogUtil;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean
public class EnterService
{
    @Autowired
    AtEnterMapper          atEnterMapper;
    @Autowired
    AtActivityMapper       atActivityMapper;
    @Autowired
    OpLogService           opLogService;

    @Value("${mail.host}")
    private String         host;
    @Value("${mail.account}")
    private String         account;
    @Value("${mail.password}")
    private String         password;

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @ServiceMethod(method = "mapps.activity.one.enter", group = "activity", groupTitle = "活动报名", version = "2.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public EnterResponse enter(EnterRequest req) throws Exception
    {
        EnterResponse res = new EnterResponse();
        LOGGER.info("报名接口(mapps.activity.one.add)入口,请求参数==" + LogUtil.getObjectInfo(req));
        AtEnter atEnter = new AtEnter();
        String id = IDGen.uuid();
        try
        {
            atEnter.setEnterId(id);
            atEnter.setActId(req.getActId());
            atEnter.setIdCard(req.getIdCard());
            atEnter.setName(req.getName());
            atEnter.setDeptName(SessionContext.getDeptName());
            atEnter.setPhone(req.getPhone());
            atEnter.setRemark(req.getRemark());
            atEnter.setSex(req.getSex());
            atEnter.setEcid(SessionContext.getEcId());
            atEnter.setEnterPersonId(SessionContext.getUserId());
            atEnter.setEnterTime(new Date());
            atEnterMapper.insertSelective(atEnter);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("actId", req.getActId());
            atActivityMapper.updateActivityByAddEnter(map);
            res.setMessage("true");
            res.setEnterNum("");
            res.success();
            LOGGER.info("活动报名成功");
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
            ErrorCode.fail(res, ErrorCode.CODE_100001);
            LOGGER.error("报名活动异常：{}", e);
        }
        return res;
    }

    @ServiceMethod(method = "mapps.activity.list.queryEnter", group = "activity", groupTitle = "活动报名", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public QueryEnterResponse enter(QueryEnterRequest req) throws Exception
    {
        QueryEnterResponse res = new QueryEnterResponse();
        List<AtEnter> enterList = null;
        LOGGER.info("获取报名列表接口(mapps.activity.one.add)入口,请求参数==" + LogUtil.getObjectInfo(req));
        AtEnter atEnter = new AtEnter();
        try
        {
            atEnter.setActId(req.getActId());
            enterList = atEnterMapper.select(atEnter);
            res.setEnterList(enterList);
            res.success();
            LOGGER.info("获取报名列表成功");
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
            ErrorCode.fail(res, ErrorCode.CODE_100001);
            LOGGER.error("获取报名列表成功异常：{}", e);
        }
        return res;
    }

    @ServiceMethod(method = "mapps.activity.exportEnter", group = "activity", groupTitle = "导出报名表", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.YES)
    public ExportEnterResponse exportEnter(ExportEnterRequest req)
    {
        ExportEnterResponse res = new ExportEnterResponse();
        if (StringUtils.isEmpty(host) || StringUtils.isEmpty(account) || StringUtils.isEmpty(password))
        {
            ErrorCode.fail(res, ErrorCode.CODE_300021);
            LOGGER.info("Mail parameters not configured");
            return res;
        }
        List<AtEnter> atEnterList = null;
        InputStream fileInput = null;
        OutputStream fileOuput = null;
        LOGGER.info("获取报名列表接口(mapps.activity.exportEnter)入口,请求参数==" + LogUtil.getObjectInfo(req));
        AtEnter atEnter = new AtEnter();
        try
        {
            AtActivity info = atActivityMapper.selectByPrimaryKey(req.getActId());
            atEnter.setActId(req.getActId());
            atEnterList = atEnterMapper.select(atEnter);
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd：HH mm");
            fileInput = this.getClass().getClassLoader().getResourceAsStream("template/enterRecord.xls");
            File targetFile = File.createTempFile("活动报名数据-" + simpleDate.format(new Date()), ".xls");
            String destPath = targetFile.getPath();
            LOGGER.info("tempFile path:{}", destPath);
            fileOuput = new FileOutputStream(targetFile);
            ExcelUtils.generateExcelByTemplate(fileOuput, fileInput, null, "title", atEnterList, "atEnterList", 65000,
                    null);
            sengMail(destPath, req.getEmail(), info);
            res.success();
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
            ErrorCode.fail(res, ErrorCode.CODE_100001);
            LOGGER.error("导出报名列表异常：{}", e);
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
        return res;
    }

    public void sengMail(String filePath, String addressee, AtActivity info)
        throws MessagingException, UnsupportedEncodingException
    {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        senderImpl.setHost(host);
        // 建立邮件消息,发送简单邮件和html邮件的区别
        MimeMessage mailMessage = senderImpl.createMimeMessage();
        // 注意这里的boolean,等于真的时候才能嵌套图片，在构建MimeMessageHelper时候，所给定的值是true表示启用，
        // multipart模式 为true时发送附件 可以设置html格式
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "GBK");
        LOGGER.info("messageHelper code:GBK");
        // 设置收件人，寄件人
        messageHelper.setTo(addressee);// 收件人
        messageHelper.setFrom(account);// 发件人
        messageHelper.setSubject("活动报名：" + info.getActTitle() + "报名表");// 邮件主题
        // true 表示启动HTML格式的邮件
        messageHelper.setText(getMailBody(info), true);// 邮件内容

        FileSystemResource file = new FileSystemResource(new File(filePath));
        String attName = info.getActTitle() + "报名表.xls";
        LOGGER.info("attName:{},encodeName:{}", attName, MimeUtility.encodeWord(attName));
        messageHelper.addAttachment(attName, file);

        senderImpl.setUsername(account);
        senderImpl.setPassword(password);
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.timeout", "25000");
        senderImpl.setJavaMailProperties(prop);
        // 发送邮件
        senderImpl.send(mailMessage);
        LOGGER.info("email sand success");
    }

    public String getMailBody(AtActivity info)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<head><meta http-equiv='content-type' content='text/html; charset=GBK'></head>");
        sb.append("<body>");
        sb.append("<h3>");
        sb.append(info.getActTitle());
        sb.append("报名表");
        sb.append("</h3>");
        sb.append("</br>");
        sb.append("活动主题：");
        sb.append(info.getActContent());
        sb.append("</br>");
        sb.append("活动时间：");
        sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(info.getActStartTime()));
        sb.append("~");
        sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(info.getActEndTime()));
        sb.append("</br>");
        sb.append("活动地址：");
        sb.append(info.getActAddress());
        sb.append("</br>");
        sb.append("发起人：");
        sb.append(info.getCreateName());
        sb.append("</br>");
        sb.append("</body>");
        sb.append("</html>");
        LOGGER.info("mail body:{}", sb.toString());
        return sb.toString();
    }
}
