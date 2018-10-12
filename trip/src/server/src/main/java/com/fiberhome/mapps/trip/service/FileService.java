package com.fiberhome.mapps.trip.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

import com.fiberhome.mapps.trip.response.FileInfoResponse;
import com.fiberhome.mapps.trip.utils.PhotouploadConstants;
import com.fiberhome.mapps.trip.utils.HttpRequestSupport.TrustAnyHostnameVerifier;
import com.fiberhome.mapps.trip.utils.HttpRequestSupport.TrustAnyTrustManager;
import com.fiberhome.mapps.utils.IDGen;
import com.fiberhome.mos.core.openapi.rop.client.RopClient;
import com.fiberhome.mos.core.openapi.rop.client.RopClientException;
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
    public static final List<String> UPLOAD_IMAGE_TYPE = Arrays.asList("bmp", "jpg", "gif", "png");

    public static final long         FILE_MAX_SIZE     = 5 * 1024 * 1024;

    public String uploadLocalFile(File file)
    {
        try
        {
            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("file", new FileSystemResource(file));
            RopClient client = new RopClient(serviceUrl, appKey, appSecret, format);
            Map<String, Object> resMap = client.requestForMap(method, v, reqMap);
            LOGGER.debug("======响应结果======" + resMap.toString());
            return (String) resMap.get("path");
        }
        catch (RopClientException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public FileInfoResponse downAndReadFile(String filePath)
    {
        LOGGER.debug("同步ark图片到本地，ark文件路径=" + filePath);
        String prefix = IDGen.uuid();
        String suffix = "";
        if (filePath.indexOf("?") != -1)
        {
            suffix = filePath.substring(filePath.lastIndexOf(PhotouploadConstants.INDEX_POINT),
                    filePath.lastIndexOf("?"));
        }
        else
        {
            suffix = filePath.substring(filePath.lastIndexOf(PhotouploadConstants.INDEX_POINT));
        }
        try
        {
            File file = File.createTempFile(prefix, suffix);
            if (file != null && !file.exists())
            {
                file.createNewFile();
            }
            OutputStream oputstream = new FileOutputStream(file);
            URL url = new URL(filePath);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]
            {
                new TrustAnyTrustManager()
            }, new java.security.SecureRandom());
            HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
            uc.setSSLSocketFactory(sc.getSocketFactory());
            uc.setHostnameVerifier(new TrustAnyHostnameVerifier());
            uc.setDoInput(true);// 设置是否要从 URL 连接读取数据,默认为true
            uc.connect();
            InputStream iputstream = uc.getInputStream();
            LOGGER.debug("同步ark图片到本地，ark文件大小=" + uc.getContentLength());
            byte[] buffer = new byte[1024 * 1024];
            int byteRead = -1;
            while ((byteRead = (iputstream.read(buffer))) != -1)
            {
                oputstream.write(buffer, 0, byteRead);
            }
            oputstream.flush();
            iputstream.close();
            oputstream.close();

            Map<String, Object> reqMap = new HashMap<String, Object>();
            reqMap.put("file", new FileSystemResource(file));
            RopClient client = new RopClient(serviceUrl, appKey, appSecret, format);
            Map<String, Object> resMap = client.requestForMap(method, v, reqMap);
            LOGGER.debug("======响应结果======" + resMap.toString());
            FileInfoResponse response = new FileInfoResponse();
            response.setPath((String) resMap.get("path"));
            response.setUrl((String) resMap.get("url"));
            file.delete();
            return response;
        }
        catch (Exception e)
        {
            LOGGER.error("读取失败！{}", e);
            e.printStackTrace();
        }
        return null;
    }

    public String getAbsoluteWebappPath()
    {
        String path = PhotouploadConstants.EMPTY;
        path = this.getClass().getResource(PhotouploadConstants.PATH_SPLIT).getPath();
        // 切割掉固定的文件夹
        // path = path.substring(0, path.lastIndexOf(PhotouploadConstants.WEB_INF));
        // 拼接图片上传的路径
        // path = path.replace(PhotouploadConstants.FILE_EMPTY, PhotouploadConstants.FILE_EMPTY_STR);
        return path;
    }
    
	public String getWebRoot() {
		Map<String, Object> reqMap = new HashMap<String, Object>();
        RopClient client = new RopClient(serviceUrl, appKey, appSecret, format);
        String webRoot = "";
		try {
			Map<String, Object> resMap = client.requestForMap("mapps.fileservice.file.getwebRoot", v, reqMap);
			webRoot = (String) resMap.get("webRoot");
			LOGGER.debug("======响应结果======webRoot==" + webRoot);
		} catch (RopClientException e) {
			LOGGER.error("webRoot获取失败：{}", e);
		}
		return webRoot;
	}
}
