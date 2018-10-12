package com.fiberhome.mapps.meetingroom.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.fiberhome.mapps.meetingroom.request.FileUploadRequest;
import com.fiberhome.mapps.meetingroom.response.FileInfoResponse;
import com.fiberhome.mapps.meetingroom.utils.ErrorCode;
import com.fiberhome.mos.core.openapi.rop.client.MulitipartFileResourceWrapper;
import com.fiberhome.mos.core.openapi.rop.client.RopClient;
import com.fiberhome.mos.core.openapi.rop.client.RopClientException;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean
public class FileService
{
    protected final Logger           LOGGER            = LoggerFactory.getLogger(getClass());

    @Value("${services.fileservice.serviceUrl}")
    String                           serviceUrl;
    @Value("${services.fileservice.appKey}")
    String                           appKey;
    @Value("${services.fileservice.appSecret}")
    String                           appSecret;
    @Value("${services.fileservice.format}")
    String                           format;
    @Value("${services.fileservice.v}")
    String                           v;
    @Value("${services.fileservice.method}")
    String                           method;
    @Value("${services.fileservice.webRoot:}")
    String                           defaultWebRoot;
    public static final List<String> UPLOAD_IMAGE_TYPE = Arrays.asList("bmp", "jpg", "gif", "png");

    public static final long         FILE_MAX_SIZE     = 5 * 1024 * 1024;

    @ServiceMethod(method = "mapps.fileservice.file.upload", group = "member", groupTitle = "文件服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
    public FileInfoResponse upload(FileUploadRequest request) throws Exception
    {
        FileInfoResponse response = new FileInfoResponse();
        try
        {
            MultipartFile file = request.getFile();
            if (null == file)
            {
                ErrorCode.fail(response, ErrorCode.CODE_300014);
                return response;
            }
            String name = file.getOriginalFilename().toLowerCase();
            LOGGER.debug("======文件名======" + name);
            long size = file.getSize();
            LOGGER.debug("======文件大小======" + size);
            if (size > FILE_MAX_SIZE)
            {
                ErrorCode.fail(response, ErrorCode.CODE_300015);
                return response;
            }
            String extension = FilenameUtils.getExtension(name).toLowerCase();
            LOGGER.debug("======文件后缀======" + extension);
            // 判断类型是否合规则
            if (!UPLOAD_IMAGE_TYPE.contains(extension))
            {
                LOGGER.debug("======" + String.format("不支持的文件格式：%s, 只支持：%s", extension, UPLOAD_IMAGE_TYPE) + "======");
                ErrorCode.fail(response, ErrorCode.CODE_300016);
                return response;
            }
            MulitipartFileResourceWrapper fiwr = new MulitipartFileResourceWrapper(file);
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("file", fiwr);
            RopClient client = new RopClient(serviceUrl, appKey, appSecret, format);
            Map<String, Object> resMap = client.requestForMap(method, v, reqMap);
            LOGGER.debug("======响应结果======" + resMap.toString());
            response.setPath((String) resMap.get("path"));
            response.setUrl((String) resMap.get("url"));
            System.out.println(resMap);
        }
        catch (Exception e)
        {
            LOGGER.error("图片上传失败：{}", e);
            ErrorCode.fail(response, ErrorCode.CODE_100002);
        }
        return response;
    }
    
    public String getWebRoot() {
		Map<String, Object> reqMap = new HashMap<String, Object>();
        RopClient client = new RopClient(serviceUrl, appKey, appSecret, format);
        String webRoot = "";
		try {
			Map<String, Object> resMap = client.requestForMap("mapps.fileservice.file.getwebRoot", v, reqMap);
			webRoot = (String) resMap.get("webRoot");
			LOGGER.debug("======响应结果======webRoot==" + webRoot);
			return webRoot;
		} catch (RopClientException e) {
			LOGGER.error("webRoot获取失败====返回本地配置默认值==="+defaultWebRoot+"：{}", e);
			return defaultWebRoot;
		}
	}
}
