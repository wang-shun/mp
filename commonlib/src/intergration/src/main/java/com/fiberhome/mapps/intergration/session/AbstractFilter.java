package com.fiberhome.mapps.intergration.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

public abstract class AbstractFilter implements Filter {
    @Autowired
    private MultipartResolver multipartResolver;

    /**
     * Convert the request into a multipart request, and make multipart resolver available.
     * <p>
     * If no multipart resolver is set, simply use the existing request.
     * 
     * @param request current HTTP request
     * @return the processed request (multipart wrapper if necessary)
     * @see MultipartResolver#resolveMultipart
     */
    protected HttpServletRequest checkMultipart(HttpServletRequest request) throws MultipartException {
        if (this.multipartResolver != null && this.multipartResolver.isMultipart(request)) {
            if (!(request instanceof MultipartHttpServletRequest)) {
                return this.multipartResolver.resolveMultipart(request);
            }
        }
        // If not returned before: return original request.
        return request;
    }

    public MultipartResolver getMultipartResolver() {
        return multipartResolver;
    }

    public void setMultipartResolver(MultipartResolver multipartResolver) {
        this.multipartResolver = multipartResolver;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;

        HttpServletRequest checkRequest = checkMultipart(req);

        filterIt(checkRequest, response, chain);
    }

    abstract void filterIt(HttpServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException;
}
