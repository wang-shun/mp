package com.fiberhome.mapps.ydzf.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.fiberhome.mapps.ydzf.request.FileDownloadRequest;
import com.fiberhome.mapps.ydzf.request.FileUploadRequest;
import com.fiberhome.mapps.ydzf.response.FileInfoResponse;
import com.fiberhome.mapps.ydzf.utils.ErrorCode;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;
import com.rop.response.FileResponse;

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
    @Value("${services.fileservice.root}")
    String 							 root;
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
            String directory = request.getDirectory();
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
            name = Long.toString(System.currentTimeMillis())+"."+extension; 
            SaveFileFromInputStream(file.getInputStream(),root+directory,name); 
            response.setPath(directory+"/"+name);
        }
        catch (Exception e)
        {
            LOGGER.error("图片上传失败：{}", e);
            ErrorCode.fail(response, ErrorCode.CODE_100002);
        }
        return response;
    }
    
    public void SaveFileFromInputStream(InputStream stream,String path,String filename) throws IOException   
    {   
    	File file =new File(path);    
    	//如果文件夹不存在则创建    
    	if  (!file .exists()  && !file .isDirectory())      
    	{       
    	    System.out.println("//不存在");  
    	    System.out.println(path);
    	    file.mkdirs();    
    	}
    	FileOutputStream fs=new FileOutputStream( path +File.separator+ filename); 
    	try {
    		  
            byte[] buffer =new byte[1024*1024];   
            int bytesum = 0;   
            int byteread = 0;    
            while ((byteread=stream.read(buffer))!=-1)   
            {   
               bytesum+=byteread;   
               fs.write(buffer,0,byteread);   
               fs.flush();   
            }    
		}finally {
			fs.close();   
	        stream.close(); 
		}
        
                
    }  
    
    
    @ServiceMethod(method = "mapps.fileservice.file.download", group = "member", groupTitle = "文件服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public FileResponse download(FileDownloadRequest request) throws Exception{
    	LOGGER.debug("mapps.fileservice.file.download--文件下载开始");
    	FileResponse res =new FileResponse();
    	String directory = request.getDirectory();
    	String path = root + directory;
    	try {
    		File file = new File(path);
    		if(file.exists()){
    			res.setFile(file);
    			LOGGER.debug("mapps.fileservice.file.download--文件下载成功");
    		}else{
    			LOGGER.debug("mapps.fileservice.file.download--文件不存在");
    		}
			
		} catch (Exception e) {
			LOGGER.debug("mapps.fileservice.file.download--文件下载失败");
			e.printStackTrace();
			return res;
		}
    	return res;
    }
    
}
