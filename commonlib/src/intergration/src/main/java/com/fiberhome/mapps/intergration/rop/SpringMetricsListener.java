package com.fiberhome.mapps.intergration.rop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;

import com.rop.RopRequestContext;
import com.rop.event.AfterDoServiceEvent;
import com.rop.event.RopEventListener;

public class SpringMetricsListener implements RopEventListener<AfterDoServiceEvent>{

	@Override
	public void onRopEvent(AfterDoServiceEvent ropEvent) {
		
		
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

}
