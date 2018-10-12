package com.fiberhome.mapps.mssdk.trace.rop;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.web.TraceHandlerInterceptor;
import org.springframework.cloud.sleuth.util.ExceptionUtils;

import com.rop.config.SystemParameterNames;

/**
 * 用于Rop服务调用及请求的切面
 * @author fh
 *
 */
@Aspect
public class RopTraceAspect {
	private final static String SERVICE_RUNNABLE_CLASS = "com.rop.impl.AnnotationServletServiceRouter$ServiceRunnable";
	private final TraceHandlerInterceptor interceptor;
	
	private final Tracer tracer;
	
	public RopTraceAspect(TraceHandlerInterceptor interceptor, Tracer tracer) {
		this.interceptor = interceptor;
		this.tracer = tracer;
	}
	
	/**
	 * 切入rop调用，增加Span
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* com.rop.impl.AnnotationServletServiceRouter.service(..))")
	public Object traceBackgroundThread(final ProceedingJoinPoint pjp) throws Throwable {
		if (interceptor == null && tracer == null) {
			return pjp.proceed();
		}
		HttpServletRequest request = (HttpServletRequest)pjp.getArgs()[0];
		HttpServletResponse response = (HttpServletResponse)pjp.getArgs()[1];
		
		Exception processEx = null;
		Span newSpan = null;
		
		try {
			interceptor.preHandle(request, response, pjp.getThis());
						
			Object result = null;
			String method = request.getParameter(SystemParameterNames.getMethod());
			String version = request.getParameter(SystemParameterNames.getVersion());
			newSpan = this.tracer.createSpan("rop:/service/" + method + "/" + version);
			try {				
				newSpan.logEvent("Rop Api Execute");
				result = pjp.proceed();
				this.tracer.addTag("response", response.getStatus() + "\t" + response.getContentType());
			} catch (Exception proEx) {
				processEx = proEx;
				this.tracer.addTag("exception", proEx.getMessage());
			} finally {
				this.tracer.close(newSpan);
			}
			
			interceptor.postHandle(request, response, pjp.getThis(), null);
			if (processEx == null) {					
				return result;			
			}
		} catch (Exception ex) {
			
		} finally {
			interceptor.afterCompletion(request, response, pjp.getThis(), processEx);
		}
		if (processEx == null) {					
			return pjp.proceed();			
		} else {
			throw processEx;
		}
	}
	
	/**
	 * 切入rop的线程调用，增加调用跟踪
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* java.util.concurrent.ExecutorService.submit(..))")
	public Object traceServiceRunnable(final ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();
		
		if (args.length == 1 && args[0] != null && SERVICE_RUNNABLE_CLASS.equals(args[0].getClass().getName())) {
			MethodSignature signature = (MethodSignature) pjp.getSignature();
			Method method = signature.getMethod();
			
			return method.invoke(pjp.getTarget(), tracer.wrap((Runnable)pjp.getArgs()[0]));
		} else {
			return pjp.proceed();
		}
	}
	
	/**
	 * 切入RestTemplate，捕获RestClientException
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* org.springframework.web.client.RestTemplate.postForObject(..))")
	public Object traceRestTemplateException(final ProceedingJoinPoint pjp) throws Throwable {
		try {
			return pjp.proceed();
		} catch (Exception ex) {
			this.tracer.addTag(Span.SPAN_ERROR_TAG_NAME, ExceptionUtils.getExceptionMessage(ex));
			throw ex;
		}
	}
	
}
