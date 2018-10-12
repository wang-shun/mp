package com.fiberhome.mapps.fileservice;

import java.io.OutputStream;
import java.io.RandomAccessFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiberhome.mapps.fileservice.entity.FsFile;
import com.fiberhome.mapps.fileservice.service.FileServiceDownloadService;

@RestController
public class FileServiceDownloadController {
	Logger LOG = LoggerFactory.getLogger(FileServiceDownloadController.class);
	
	@Autowired
	FileServiceDownloadService fsds;
	
	@RequestMapping("/d/{imageuuid}")
    // 在线打开方式 下载
	public void downLoad(@PathVariable String imageuuid, HttpServletRequest req, HttpServletResponse response) throws Exception {
		FsFile fsfile = fsds.getFsFile(imageuuid);
		String filePath = fsfile.getFilePath();
		LOG.debug("获取fileservice本地图片接口,请求文件路径==" + filePath);
		
		RandomAccessFile raFile = new RandomAccessFile(filePath, "r");  
		response.reset();
        String range = req.getHeader("RANGE");  
        int start=0, end=0;  
        if(null!=range && range.startsWith("bytes=")){  
        	response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);//206  
            String[] values =range.split("=")[1].split("-");  
            start = Integer.parseInt(values[0]);  
            end = Integer.parseInt(values[1]);  
        }  
        int requestSize=0;  
        if(end!=0 && end > start){  
            requestSize = end - start + 1;  
            response.setHeader("Content-Length", ""+(requestSize));  
        } else {  
            requestSize = Integer.MAX_VALUE;  
            response.setHeader("Content-Length", ""+(fsfile.getContentSize()));
        }  
          
        byte[] buffer = new byte[4096];  
          
         // 非常重要
        response.setContentType(fsfile.getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=" + fsfile.getFileName());
        response.setHeader("Accept-Ranges", "bytes");
        OutputStream os = response.getOutputStream();  
        int needSize = requestSize;  
        
        
	    try{  
	        raFile.seek(start);  
	        while(needSize > 0){  
	            int len = raFile.read(buffer);  
	            if(needSize < buffer.length){  
	                os.write(buffer,0,needSize);  
	            } else {  
	                os.write(buffer,0,len);  
	                if(len < buffer.length){  
	                    break;  
	                }  
	            }  
	            needSize -= buffer.length;  
	        }  
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			raFile.close();  
	        os.close();
		}
        LOG.debug("获取成功,文件名==" + imageuuid);
    }
	
	@Value("${test.key:}")
	String testV;
	
	@Autowired
    Environment env;
	
	@RequestMapping("/test")
	public String test() {
		System.out.println(env.containsProperty("test.key"));
		return testV + "---HCCCC";
	}
	
	
}
