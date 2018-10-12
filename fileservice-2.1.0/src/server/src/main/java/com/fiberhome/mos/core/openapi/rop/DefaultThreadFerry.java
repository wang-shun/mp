/**
 * 版权声明：中图一购网络科技有限公司 版权所有 违者必究 2012 
 * 日    期：12-7-20
 */
package com.fiberhome.mos.core.openapi.rop;

//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.subject.Subject;
//import org.apache.shiro.subject.support.SubjectThreadState;
//import org.apache.shiro.util.ThreadState;

import com.rop.ThreadFerry;

/**
 * <pre>
 * 功能说明：
 * </pre>
 *
 * @author 陈雄华
 * @version 1.0
 */
public class DefaultThreadFerry implements ThreadFerry{
//    private Subject subject = null;
//    private ThreadState threadState = null;
    
    @Override
    public void doInSrcThread() {
//        subject = SecurityUtils.getSubject();
    }

    @Override
    public void doInDestThread() {
//        if (subject != null) {
//            ThreadState threadState = new SubjectThreadState(subject);
//            threadState.bind();
//        }
    }
    
    @Override
    public void doOutDestThread()
    {
//        if(threadState != null){
//           threadState.clear(); 
//        }
//        
    }
}

