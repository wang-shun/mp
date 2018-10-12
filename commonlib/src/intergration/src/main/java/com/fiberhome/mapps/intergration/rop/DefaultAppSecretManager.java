package com.fiberhome.mapps.intergration.rop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rop.security.AppSecretManager;

public class DefaultAppSecretManager implements AppSecretManager
{
    private final Logger logger = LoggerFactory.getLogger(DefaultAppSecretManager.class);

    @Override
    public String getSecret(String secret)
    {
        logger.debug("getSecret方法调用：" + secret);
        // TODO Auto-generated method stub
        return "001";
    }

    @Override
    public boolean isValidAppKey(String appKey)
    {
//        System.out.println("获取用户ID="+SessionUtil.getLoginUserName());
//        //排除登录appKey
//        if("mapps.userservice.login".equals(appKey)){
//            return true;
//        }
//        try
//        {
//            //判断权限
//            Subject currentUser = SecurityUtils.getSubject();
//            logger.debug("isValidAppKey方法调用：" + appKey);
//            currentUser.checkPermission(appKey);
//            return true;
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//            return false;
//        }
        return true;
    }
}
