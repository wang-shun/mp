package com.fiberhome.mapps.intergration.rop;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import net.sf.ehcache.Cache;
//import net.sf.ehcache.CacheManager;
//import net.sf.ehcache.Element;

import com.rop.security.InvokeTimesController;
import com.rop.session.Session;

/**
 * open api 访问控制
 * @author jianghaifeng
 *
 */
public class AppInvokeTimesController implements InvokeTimesController {

    public static int Limits;
    Logger logger = LoggerFactory.getLogger(getClass());
//    CacheManager singletonManager = CacheManager.create();
    
    
	@Override
	public void caculateInvokeTimes(String ip, Session session) {
		setCounter(ip);
	}

	@Override
	public boolean isUserInvokeLimitExceed(String appKey, Session session) {
		return false;
	}

	@Override
	public boolean isSessionInvokeLimitExceed(String appKey, String sessionId) {
		return false;
	}

	@Override
	public boolean isAppInvokeLimitExceed(String ip) {
//		Cache ropCache = singletonManager.getCache("ropController");
//		if(ropCache!=null){
//			int counter = (Integer) ropCache.get(ip).getValue();
//			logger.debug("每分钟最大访问次数{}"+Limits+","+ip+"已经访问次数{}"+counter);
//			return counter>Limits ; 
//		}else{
//			return false;
//		}
		
		return false;
		
	}

	@Override
	public boolean isAppInvokeFrequencyExceed(String appKey) {
		return false;
	}
	
	public void setCounter(String ip){
//		if (singletonManager.getCache("ropController") == null) {
//			Cache memoryOnlyCache = new Cache("ropController", 5000, false,
//					false, 60, 60);
//			singletonManager.addCache(memoryOnlyCache);
//		}
//		Cache ropCache =singletonManager.getCache("ropController");
//		Element e = ropCache.get(ip);
//		if(e!=null){
//			int counter = (Integer) e.getValue();
//			ropCache.put(new Element(ip,counter+1));
//		}else{
//			ropCache.put(new Element(ip,1));
//		}
	}

	public static int getLimits() {
		return Limits;
	}

	public static void setLimits(int limits) {
		Limits = limits;
	}
	
}
