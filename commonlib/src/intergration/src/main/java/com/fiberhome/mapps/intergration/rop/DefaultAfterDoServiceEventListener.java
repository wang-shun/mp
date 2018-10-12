package com.fiberhome.mapps.intergration.rop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rop.event.AfterDoServiceEvent;
import com.rop.event.RopEventListener;

/**
 * <pre>
 * 功能说明：1
 * </pre>
 *
 * @author 陈雄华
 * @version 1.0
 */
public class DefaultAfterDoServiceEventListener implements RopEventListener<AfterDoServiceEvent> {
	
    private final Logger logger = LoggerFactory.getLogger(DefaultAfterDoServiceEventListener.class);
    
    @Override
    public void onRopEvent(AfterDoServiceEvent ropEvent) {
        
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

