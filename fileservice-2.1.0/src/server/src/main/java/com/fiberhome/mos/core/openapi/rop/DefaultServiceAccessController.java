/**
 * 日 期：12-2-13
 */
package com.fiberhome.mos.core.openapi.rop;

//import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.fiberhome.mos.core.utils.LogUtil;
import com.rop.security.ServiceAccessController;
import com.rop.session.Session;

/**
 * <pre>
 * 功能说明：对调用的方法进行安全性检查
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class DefaultServiceAccessController implements ServiceAccessController
{
    private final Logger logger = LoggerFactory.getLogger(DefaultServiceAccessController.class);

    @Override
    public boolean isAppGranted(String appKey, String method, String version)
    {
        // System.out.println("appKey=" + appKey);
        // System.out.println("method=" + method);
        // System.out.println("version=" + version);
        return true;
    }

    @Override
    public boolean isUserGranted(Session session, String method, String version)
    {
        logger.debug("ROP进行权限判断Session={},method={},version={}", session, method, version);
        if (null == session)
        {
            logger.debug("无session校验接口不需要验证权限。");
            return true;
        }
        // 排除登录appKey
        if ("mapps.userservice.login".equals(method))
        {
            return true;
        }
        try
        {
            // 判断权限
//            org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
//            currentUser.checkPermission(method);
//            LogUtil.addActionLog("api", "api接口调用", "", "接口操作 method=" + method + " version=" + version, method, true);
            return true;
        } catch (Exception e)
        {
            logger.debug("用户没有权限", e);
            return false;
        }
    }
}
