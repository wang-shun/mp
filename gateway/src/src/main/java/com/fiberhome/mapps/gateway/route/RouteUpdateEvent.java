package com.fiberhome.mapps.gateway.route;

import org.springframework.context.ApplicationEvent;

public class RouteUpdateEvent extends ApplicationEvent {

	private static final long serialVersionUID = -3171173033400499328L;

	public RouteUpdateEvent(Object source) {
		super(source);
	}

}
