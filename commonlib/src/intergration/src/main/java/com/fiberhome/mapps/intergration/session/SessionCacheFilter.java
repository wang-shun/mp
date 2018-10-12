package com.fiberhome.mapps.intergration.session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fiberhome.mapps.intergration.security.sso.SsoFilter;
import com.fiberhome.mapps.intergration.security.sso.UserInfo;
import com.rop.config.SystemParameterNames;

/**
 * 级别最高，用于对session的缓存处理
 * 
 * @author Administrator
 *
 */
@Component
public class SessionCacheFilter extends AbstractFilter {
    private static Logger LOG = LoggerFactory.getLogger(SessionCacheFilter.class);

    private static int expiredTime = 5 * 60 * 1000; // session的cache的存活时间
    private static ConcurrentHashMap<String, UserInfo> sessionCache = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Long> sessionLived = new ConcurrentHashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Scheduled(fixedRate = 10000)
    public void livedSessionValidat() {
        // LOG.info("Session 清理任务启动......");
        Long ex = System.currentTimeMillis() - expiredTime;

        List<String> expired = new ArrayList<String>();

        Enumeration<String> keys = sessionLived.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Long ts = sessionLived.get(key);
            if (ts < ex) {
                expired.add(key);
            }
        }

        for (String key : expired) {
            LOG.debug("SessionCacheFilter 清理, sessionId:{}", key);
            sessionCache.remove(key);
            sessionLived.remove(key);
        }
    }

    void filterIt(HttpServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        String sessionId = request.getParameter(SystemParameterNames.getSessionId());

        if (sessionId == null) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = request.getSession();

        UserInfo user = sessionCache.get(sessionId);

        LOG.debug("SessionCacheFilter working, sessionId:{}, User In Cache: {}", sessionId, user);

        try {
            if (user != null) {
                session.setAttribute(SsoFilter.SESSION_KEY, user);
                session.setAttribute(SsoFilter.SESSION_ID, sessionId);
            }
            chain.doFilter(request, response);

            if (user == null && sessionId != null) {
                user = (UserInfo)session.getAttribute(SsoFilter.SESSION_KEY);
                String sid = (String)session.getAttribute(SsoFilter.SESSION_ID);
                if (user != null && sessionId.equals(sid)) {
                    LOG.debug("SessionCacheFilter cached session, sessionId:{}, User: {}", sessionId, user);
                    sessionCache.put(sessionId, user);
                    sessionLived.put(sessionId, System.currentTimeMillis());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
