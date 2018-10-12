package com.fiberhome.mapps.intergration.rop;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rop.session.Session;
import com.rop.session.SessionManager;
import com.rop.session.SimpleSession;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author hlj
 * @version 1.0
 */
public class DefaultSessionManager implements SessionManager
{
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void addSession(String sessionId, Session session)
    {
    }

    @Override
    public Session getSession(String sessionId)
    {
        logger.debug("ROP进行Session校验：{}", sessionId);
        try
        {
        	
//            Subject subject = SecurityUtils.getSubject();
//            
//            org.apache.shiro.session.Session session = subject.getSession();
//            if (!subject.isAuthenticated() || session == null)
//            {
//                return null;
//            }
            SimpleSession ss = new SimpleSession();
            //设置SessionID
            ss.setAttribute("sessionId", sessionId);
//            session.touch();
            return ss;
        } catch (Exception e)
        {
            logger.debug("用户Session校验失败", e);
            return null;
        }
    }

    @Override
    public void removeSession(String sessionId)
    {
    }
}
