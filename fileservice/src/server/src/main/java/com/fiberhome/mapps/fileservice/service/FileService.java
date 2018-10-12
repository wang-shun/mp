package com.fiberhome.mapps.fileservice.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.fiberhome.mapps.fileservice.ServiceAccessor;
import com.fiberhome.mapps.fileservice.exception.FilePathErrorException;
import com.fiberhome.mapps.fileservice.request.FileRetriveUrlRequest;
import com.fiberhome.mapps.fileservice.request.FileUploadRequest;
import com.fiberhome.mapps.fileservice.request.QRCodeGenRequest;
import com.fiberhome.mapps.fileservice.response.FileInfoResponse;
import com.fiberhome.mapps.fileservice.response.RetriveFileInfo;
import com.fiberhome.mapps.fileservice.response.webRootResponse;
import com.fiberhome.mapps.fileservice.utils.MD5Utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.rop.AbstractRopRequest;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

@ServiceMethodBean
public class FileService {
	private final static Logger LOG = LoggerFactory.getLogger(FileService.class);

	@Autowired
	ServiceAccessor serviceAccessor;

	@Value("${fileservice.webRoot}")
	String webRoot;

	@ServiceMethod(method = "mapps.fileservice.file.upload", group = "member", groupTitle = "图片服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public FileInfoResponse upload(FileUploadRequest request) {
		FileInfoResponse response = new FileInfoResponse();

		MultipartFile file = request.getFile();
		String fileName = file.getOriginalFilename();
		String contentType = file.getContentType();
		String ext = FilenameUtils.getExtension(fileName);
		if (StringUtils.isEmpty(ext)) {
			ext = "nil";
		}

		try {
			// StorePath path = upload(file.getInputStream(), fileName,
			// contentType, ext.toLowerCase());
			String md5 = MD5Utils.getMD5String(IOUtils.toByteArray(file.getInputStream()));
			String filePath = serviceAccessor.upload(file.getInputStream(), fileName, contentType, ext.toLowerCase(), md5);
			response.setPath(filePath);
			response.setUrl(this.getUrl(filePath, fileName));
			response.setFileName(fileName);
			response.setSize(file.getSize());
			response.setContentType(contentType);
			response.setWebRoot(webRoot);
		} catch (IOException e) {
			LOG.error("Upload  failed: {}", e.getMessage(), e);
			response.setCode("400001");
			response.setError_message("文件上传失败");
		}
		return response;
	}

	@ServiceMethod(method = "mapps.fileservice.file.retriveUrl", group = "member", groupTitle = "图片服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public FileInfoResponse retriveUrl(FileRetriveUrlRequest request) {
		FileInfoResponse response = new FileInfoResponse();
		String filePath = request.getFileId();
		RetriveFileInfo rfi = new RetriveFileInfo();
		try {
			rfi = serviceAccessor.retrive(filePath);
		} catch (FilePathErrorException e) {
			response.setCode("300002");
			response.setError_message("文件路径非法");
		} catch (FileNotFoundException e) {
			response.setCode("300001");
			response.setError_message("文件不存在");
		} catch (Exception e) {
			response.setCode("100001");
			response.setError_message("数据处理失败");
		}

		response.setPath(filePath);
		response.setUrl(getUrl(filePath, response.getFileName()));
		response.setSize(rfi.getSize());
		response.setFileName(rfi.getFileName());
		response.setContentType(rfi.getContentType());
		response.setWebRoot(webRoot);
		return response;
	}

	@ServiceMethod(method = "mapps.fileservice.qrcode.gen", group = "member", groupTitle = "图片服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public FileInfoResponse qrcodeGen(QRCodeGenRequest request) {
		String content = request.getContent();
		FileInfoResponse response = new FileInfoResponse();
		int width = 1000;
		int height = 1000;

		String format = "png";

		Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF8");
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			File tmp = File.createTempFile("qrcode", "." + format);
			MatrixToImageWriter.writeToFile(bitMatrix, format, tmp);
			String fileName = tmp.getName();
			String contentType = "image/png";
			FileInputStream fis = new FileInputStream(tmp);
			FileInputStream fisForMD5 = new FileInputStream(tmp);

			// StorePath path = upload(fis, fileName, contentType, format);
			String md5 = MD5Utils.getMD5String(IOUtils.toByteArray(fisForMD5));
			String filePath = serviceAccessor.upload(fis, fileName, contentType, format, md5);
			response.setPath(filePath);
			response.setUrl(this.getUrl(filePath, fileName));
			response.setFileName(fileName);
			response.setSize(tmp.length());
			response.setContentType(contentType);
			response.setWebRoot(webRoot);
		} catch (Exception e) {
			LOG.error("Generate qrcode failed: {}", e.getMessage(), e);
			response.setCode("400002");
			response.setError_message("二维码生成失败");
		}
		return response;
	}

	@ServiceMethod(method = "mapps.fileservice.file.upload", group = "member", groupTitle = "图片服务", version = "2.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public FileInfoResponse uploadToLocal(FileUploadRequest request) {
		return upload(request);
	}

	private String getUrl(String filePath, String fileName) {
		return webRoot + filePath;
	}

	@ServiceMethod(method = "mapps.fileservice.file.getwebRoot", group = "member", groupTitle = "图片服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public webRootResponse getWebRoot(AbstractRopRequest request) {
		webRootResponse response = new webRootResponse();
		response.setWebRoot(webRoot);
		LOG.debug("获取webRoot接口===" + webRoot);
		return response;
	}
}
