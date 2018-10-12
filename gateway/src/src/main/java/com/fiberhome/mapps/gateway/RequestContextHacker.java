package com.fiberhome.mapps.gateway;

import java.io.NotSerializableException;
import java.util.Enumeration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.util.DeepCopy;

@Aspect 
public class RequestContextHacker {
	
	@Around("execution(* com.netflix.zuul.context.RequestContext.copy )")
	public RequestContext hacker(ProceedingJoinPoint pjp) {
		RequestContext orginal = (RequestContext)pjp.getTarget();
		
		RequestContext copy = new RequestContext();
        Enumeration<String> keys = orginal.keys();
        
        while (keys.hasMoreElements()) {
        	String key = keys.nextElement();
            Object orig = orginal.get(key);
            try {
                Object copyValue = DeepCopy.copy(orig);
                if (copyValue != null) {
                    copy.set(key, copyValue);
                } else {
                    copy.set(key, orig);
                }
            } catch (NotSerializableException e) {
                copy.set(key, orig);
            }
            
        }
        return copy;
	}
}
