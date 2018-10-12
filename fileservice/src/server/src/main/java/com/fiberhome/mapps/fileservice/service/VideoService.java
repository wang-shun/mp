package com.fiberhome.mapps.fileservice.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.fiberhome.mapps.fileservice.ServiceAccessor;
import com.fiberhome.mapps.fileservice.entity.FsFile;
import com.fiberhome.mapps.fileservice.request.FileRetriveUrlRequest;
import com.fiberhome.mapps.fileservice.request.VideoUploadRequest;
import com.fiberhome.mapps.fileservice.response.VideoUploadResponse;
import com.fiberhome.mapps.fileservice.response.videoDurationResponse;
import com.fiberhome.mapps.fileservice.utils.MD5Utils;
import com.rop.annotation.IgnoreSignType;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

@ServiceMethodBean
public class VideoService {
	private final static Logger LOG = LoggerFactory.getLogger(VideoService.class);

	@Autowired
	ServiceAccessor serviceAccessor;
	
	@Autowired
	FileStorageService fss;

	FFmpeg ffmpeg;
	FFprobe ffprobe;

	@ServiceMethod(method = "mapps.fileservice.video.upload", group = "member", groupTitle = "视频服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public VideoUploadResponse uploadVideo(VideoUploadRequest request) {
		VideoUploadResponse response = new VideoUploadResponse();
		String resultCode = "1";
		MultipartFile file = request.getFile();
		String fileMD5 = "";
		try {
			InputStream inputStream = file.getInputStream();
			fileMD5 = MD5Utils.getMD5String(IOUtils.toByteArray(inputStream));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			init();
		} catch (IOException e) {
			LOG.error("初始化ffmpeg错误，请检查ffmpeg是否正确安装，以及环境变量FFMPEG是否指向ffmpeg的执行文件路径", e);
			resultCode = "1001"; // 服务不可用
		}
		File input = null;
		try {
			input = File.createTempFile("input", ".mp4");
		} catch (IOException e) {
			LOG.error("创建临时文件失败", e);
			resultCode = "1001"; // 服务不可用
		}
		try {

			file.transferTo(input);
		} catch (Exception e) {
			LOG.error("上传文件转存临时文件失败", e);
			resultCode = "1001"; // 服务不可用
		}

		//根据md5判断重复性
		String existFileId = fss.getFileByMD5(fileMD5);
		if(!"".equals(existFileId)){
			LOG.debug("检测到相同md5的视频,返回此视频id：" + existFileId);
			response.setVideoId(existFileId);
		}else{
			// 视频处理
			try {
				String videoId = transform(file.getOriginalFilename(), input.getPath(), request.getWidth(),
						request.getHeight(), request.getBitrate(), fileMD5);
				response.setVideoId(videoId);
			} catch (IOException e) {
				LOG.error("视频文件处理失败", e);
				resultCode = "1001001";
			}
		}

		// 获取第一帧
		try {
			String screenshotId = fetchScreenshot(input.getPath());
			response.setScreenshotId(screenshotId);
		} catch (IOException e) {
			LOG.error("视频文件处理失败", e);
			resultCode = "1001001";
		}
		response.setCode(resultCode);
		
		
		// 获取视屏时长
//		try {
//			String videoTime = getVideoTime(input.getPath(),ffmpeg.getPath());
//			response.setVideoDuration(videoTime);
//		} catch (Exception e) {
//			LOG.error("获取视频时长失败", e);
//		}
		
		if (input != null) {
			input.delete();
		}

		return response;
	}
	
	@ServiceMethod(method = "mapps.fileservice.video.getDuration", group = "member", groupTitle = "视频服务", version = "1.0", ignoreSign = IgnoreSignType.YES, needInSession = NeedInSessionType.NO)
	public videoDurationResponse getVideoDuration(FileRetriveUrlRequest request) {
		videoDurationResponse response = new videoDurationResponse();
		// 获取视屏时长
		try {
			init();
			FsFile file = fss.getFsFile(request.getFileId());
			File input = fss.getFile(file.getFilePath());
			String videoTime = getVideoTime(input.getAbsolutePath(),ffmpeg.getPath());
			response.setVideoDuration(videoTime);
		} catch (Exception e) {
			LOG.error("获取视频时长失败", e);
			response.setCode("1001002");
			response.setError_message("获取视频时长失败");
		}
		return response;
	}
	
	private String transform(String fileName, String videoFile, int width, int height, int bitrate, String fileMD5) throws IOException {
		FFmpegProbeResult result = ffprobe.probe(videoFile);

		
		int ow = 0; // result.streams.get(0).width;
		int oh = 0; // result.streams.get(0).height;
		int rf = 0; // result.streams.get(0).r_frame_rate.intValue();
		
		for (FFmpegStream stream : result.streams) {
			ow = Math.max(ow, stream.width);
			oh = Math.max(ow, stream.height);
			rf = Math.max(rf, stream.r_frame_rate.intValue());
		}
		File output = File.createTempFile("output", ".mp4");
		
		try {
			FFmpegBuilder builder = new FFmpegBuilder().addInput(result) // Filename,
																			// or a
																			// FFmpegProbeResult
	
					.overrideOutputFiles(true) // Override the output if it exists
	
					.addOutput(output.getPath()) // Filename for the destination
					.setFormat("mp4") // Format is inferred from filename, or can be
					// .setTargetSize((long)(result.format.duration * 50000))
					.disableSubtitle() // No subtiles
	
					.setAudioChannels(2) // Mono audio
					.setAudioCodec("aac") // using the aac codec
					.setAudioSampleRate(48_000) // at 48KHz
					.setAudioBitRate(32768) // at 32 kbit/s
	
					.setVideoCodec("libx264") // Video using x264
					.setVideoFrameRate(Math.min(24, rf), 1) // at 24 frames per second
					.setVideoResolution(Math.min(width, ow), Math.min(height, oh)) // at 640x480 resolution
	
					.setVideoBitRate(bitrate)
					.addExtraArgs("-preset", "veryfast")
	
					.setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to
																	// use
																	// experimental
																	// specs
					.done();
	
			FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
	
			// Or run a two-pass encode (which is slower at the cost of better
			// quality)
			executor.createTwoPassJob(builder).run();
	
			// 存储
			return serviceAccessor.upload(new FileInputStream(output), fileName, "video/mp4", "mp4", fileMD5);
		}	finally {
			// delete tmp file
			output.delete();
		}
	}

	private String fetchScreenshot(String videoFile) throws IOException {
		FFmpegProbeResult result = ffprobe.probe(videoFile);
		
        File output = File.createTempFile("snapshot", ".jpg"); 

        try{
            FFmpegBuilder builder = new FFmpegBuilder()
					.addInput(result)
					.addOutput(output.getPath())
					.addExtraArgs("-vframes", "1")
					.addExtraArgs("-ss", "00:00:00")
					.addExtraArgs("-f", "mjpeg")
					.addExtraArgs("-crf", "18")
					.addExtraArgs("-an").done();
			
			FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
			executor.createJob(builder).run();
			String fileMD5 = MD5Utils.getMD5String(IOUtils.toByteArray(new FileInputStream(output)));
			// 存储
			return serviceAccessor.upload(new FileInputStream(output), output.getPath(), "image/jpeg", "jpg", fileMD5);
        } finally {
        	output.delete();
        }
	}
	
	public String getVideoTime(String video_path, String ffmpeg_path) {  
        List<String> commands = new java.util.ArrayList<String>();  
        commands.add(ffmpeg_path);  
        commands.add("-i");  
        commands.add(video_path);  
        try {  
            ProcessBuilder builder = new ProcessBuilder();  
            builder.command(commands);  
            final Process p = builder.start();  
              
            //从输入流中读取视频信息  
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));  
            StringBuffer sb = new StringBuffer();  
            String line = "";  
            while ((line = br.readLine()) != null) {  
                sb.append(line);  
            }  
            br.close();  
              
            //从视频信息中解析时长  
            String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";  
            Pattern pattern = Pattern.compile(regexDuration);  
            Matcher m = pattern.matcher(sb.toString());  
            if (m.find()) {  
                String time = (m.group(1));  
                LOG.debug(video_path+",视频时长："+time+", 开始时间："+m.group(2)+",比特率："+m.group(3)+"kb/s");  
                return time;  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
          
        return "00:00";  
    }

	private void init() throws IOException {
		if (ffmpeg != null && ffprobe != null)
			return;

		String ffmpegPath = FFmpeg.DEFAULT_PATH;

		String suffix = System.getProperty("os.name").contains("Windows") ? ".exe" : "";
		ffmpeg = new FFmpeg(ffmpegPath + File.separator + "ffmpeg" + suffix);
		ffprobe = new FFprobe(ffmpegPath + File.separator + "ffprobe" + suffix);
	}
}
