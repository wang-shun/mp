package com.fiberhome.mapps;

import java.io.IOException;

import org.junit.Test;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

public class VideoConverterTest {

//	@Test
	public void test1() throws IOException {
		FFmpeg ffmpeg = new FFmpeg("d:/greensoft/ffmpeg/bin/ffmpeg.exe");
		FFprobe ffprobe = new FFprobe("d:/greensoft/ffmpeg/bin/ffprobe.exe");
		
		final FFmpegProbeResult result = ffprobe.probe("d:/VID_20170601_150434.mp4");
		
		FFmpegBuilder builder = new FFmpegBuilder()

				.addInput(result) // Filename, or a FFmpegProbeResult
				
				.overrideOutputFiles(true) // Override the output if it exists

				.addOutput("output.mp4") // Filename for the destination
				.setFormat("mp4") // Format is inferred from filename, or can be
//				.setTargetSize((long)(result.format.duration * 50000))
				.disableSubtitle() // No subtiles

				.setAudioChannels(2) // Mono audio
				.setAudioCodec("aac") // using the aac codec
				.setAudioSampleRate(48_000) // at 48KHz
				.setAudioBitRate(32768) // at 32 kbit/s

				.setVideoCodec("libx264") // Video using x264
				.setVideoFrameRate(24, 1) // at 24 frames per second
				.setVideoResolution(640, 480) // at 640x480 resolution
				
				.setVideoBitRate(372_000)

				.setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to
																// use
																// experimental
																// specs
				.done();

		FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

		// Run a one-pass encode
//		executor.createJob(builder).run();

		// Or run a two-pass encode (which is slower at the cost of better
		// quality)
		executor.createTwoPassJob(builder).run();
	}
	
	@Test
	public void test2() throws IOException {
		FFmpeg ffmpeg = new FFmpeg();//new FFmpeg("d:/greensoft/ffmpeg/bin/ffmpeg.exe");
		FFprobe ffprobe = new FFprobe();//new FFprobe("d:/greensoft/ffmpeg/bin/ffprobe.exe");
		
		final FFmpegProbeResult result = ffprobe.probe("d:/VID_20170601_150434.mp4");
		
		FFmpegBuilder builder = new FFmpegBuilder()
				.addInput(result)
				.addOutput("snapshot.jpg")
				.addExtraArgs("-vframes", "1")
				.addExtraArgs("-ss", "00:00:00")
				.addExtraArgs("-f", "mjpeg")
				.addExtraArgs("-crf", "18")
//				.addExtraArgs("-s", "640x480")
				.addExtraArgs("-an").done();
		
		FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
		executor.createJob(builder).run();
	}
}
