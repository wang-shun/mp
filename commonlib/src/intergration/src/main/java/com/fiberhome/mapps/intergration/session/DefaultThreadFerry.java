/**
 * 版权声明：中图一购网络科技有限公司 版权所有 违者必究 2012 
 * 日    期：12-7-20
 */
package com.fiberhome.mapps.intergration.session;

import com.rop.ThreadFerry;

/**
 * <pre>
 * 功能说明：
 * </pre>
 *
 * @author 陈雄华
 * @version 1.0
 */
public class DefaultThreadFerry implements ThreadFerry {
	RopSession session;

	@Override
	public void doInSrcThread() {
		session = SessionContext.get();
	}

	@Override
	public void doInDestThread() {
		SessionContext.set(session);
	}

	@Override
	public void doOutDestThread() {
		SessionContext.clear();
	}
}
