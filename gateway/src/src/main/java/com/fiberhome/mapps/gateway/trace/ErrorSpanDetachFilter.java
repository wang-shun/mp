package com.fiberhome.mapps.gateway.trace;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import com.netflix.zuul.context.RequestContext;

public class ErrorSpanDetachFilter implements Filter {
	protected static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";
	private Tracer tracer;
	
	public ErrorSpanDetachFilter(Tracer tracer) {
		this.tracer = tracer;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} finally {
			RequestContext ctx = RequestContext.getCurrentContext();
			// 如果是通过SendError重定向的请求,且请求路径为/error，则从上下文中de
			
			if (ctx.getBoolean(SEND_ERROR_FILTER_RAN, false)) {
				Span span = this.tracer.getCurrentSpan();
				if (this.tracer.isTracing() && span != null && span.getParents().isEmpty()) {
					System.err.println("Detach Error Span: " + span);
					this.tracer.detach(span);
				}
			}
		}
	}

	@Override
	public void destroy() {
		
	}

	
}
