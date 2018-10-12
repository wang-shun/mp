package com.fiberhome.mapps.mssdk.spring;

import java.util.Collections;

import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * MVC endpoint to allow an application to be restarted on a POST (to /restart by
 * default).
 * 
 * @author Dave Syer
 *
 */
public class RestartMvcEndpoint extends EndpointMvcAdapter {

	public RestartMvcEndpoint(RestartEndpoint delegate) {
		super(delegate);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@Override
	public Object invoke() {
		if (!getDelegate().isEnabled()) {
			return new ResponseEntity<>(Collections.singletonMap(
					"message", "This endpoint is disabled"), HttpStatus.NOT_FOUND);
		}
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				RestartMvcEndpoint.super.invoke();
			}
		});
		thread.setDaemon(false);
		thread.start();
		return Collections.singletonMap("message", "Restarting");
	}

}
