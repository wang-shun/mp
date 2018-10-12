package com.fiberhome.mapps.intergration.rop;

import com.rop.event.AfterStartedRopEvent;
import com.rop.event.RopEventListener;

/**
 * <pre>
 * 功能说明：
 * </pre>
 *
 * @author 陈雄华
 * @version 1.0
 */
public class DefaultPostInitializeEventListener implements RopEventListener<AfterStartedRopEvent> {

    @Override
    public void onRopEvent(AfterStartedRopEvent ropRopEvent) {
        System.out.println(ropRopEvent.toString());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

