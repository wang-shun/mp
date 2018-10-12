package com.fiberhome.mapps.mssdk.metrics;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.rop.RopRequestContext;
import com.rop.event.AfterDoServiceEvent;
import com.rop.event.RopEventListener;

/**
 * Rop 指标采集
 * @author fh
 *
 */
public class RopMetricsListener implements RopEventListener<AfterDoServiceEvent>, ApplicationContextAware {
	CustomMetricsWriter metricsWriter;
	ApplicationContext applicationContext;
	
	public void setMetricsWriter() {
	}

	@Override
	public void onRopEvent(AfterDoServiceEvent ropEvent) {
		
		if (metricsWriter == null) {
			return;
		}
		RopRequestContext ropRequestContext = ropEvent.getRopRequestContext();
		String method = ropRequestContext.getMethod();
		String version = ropRequestContext.getVersion();


		MetricName meter = new MetricName("rop.api.meter");
		meter.tag("method", method);
		meter.tag("v", version);
		metricsWriter.increment(meter);

		MetricName timer = new MetricName("rop.api.timer");
		timer.tag("method", method);
		timer.tag("v", version);
			
		double e = ropEvent.getServiceEndTime() - ropEvent.getServiceBeginTime();
		metricsWriter.submit(timer, e);
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
		try {
			metricsWriter = applicationContext.getBean(CustomMetricsWriter.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}

}
