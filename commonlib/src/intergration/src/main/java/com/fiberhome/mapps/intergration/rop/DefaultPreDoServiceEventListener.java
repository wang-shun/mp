package com.fiberhome.mapps.intergration.rop;

import javax.servlet.http.HttpServletRequest;

import com.rop.RopRequest;
import com.rop.RopRequestContext;
import com.rop.event.PreDoServiceEvent;
import com.rop.event.RopEventListener;

/**
 * <pre>
 * 功能说明：
 * </pre>
 *
 * @author 陈雄华
 * @version 1.0
 */
public class DefaultPreDoServiceEventListener implements RopEventListener<PreDoServiceEvent> {

    @Override
    public void onRopEvent(PreDoServiceEvent ropEvent) {
        RopRequestContext context = ropEvent.getRopRequestContext();
        
        HttpServletRequest  request = (HttpServletRequest)context.getRawRequestObject();
        
        if(context != null && context.getRopRequest() != null){
            RopRequest ropRequest = context.getRopRequest();
//            String message = MessageMarshallerUtils.getMessage(ropRequest, ropRequestContext.getMessageFormat());
//            System.out.println("message("+ropEvent.getServiceBeginTime()+")"+message);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

