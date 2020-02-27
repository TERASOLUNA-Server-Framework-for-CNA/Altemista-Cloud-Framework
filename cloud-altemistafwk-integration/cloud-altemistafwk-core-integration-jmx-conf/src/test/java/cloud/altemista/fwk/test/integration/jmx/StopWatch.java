package cloud.altemista.fwk.test.integration.jmx;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
@ManagedResource
public class StopWatch implements InitializingBean {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

	private volatile Future<?> future;

	private final AtomicInteger seconds = new AtomicInteger();

	@ManagedAttribute
	public int getSeconds() {
		return this.seconds.get();
	}

	public void afterPropertiesSet() {
		this.start();
	}

	@ManagedOperation
	public void start() {
		this.scheduler.initialize();
		this.future = this.scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				seconds.incrementAndGet();
			}
		}, 1000);
	}

	@ManagedOperation
	public void stop() {
		this.future.cancel(true);
	}

	@ManagedOperation
	public void reset() {
		logger.info(" ===> Reset Seconds!!!");
		this.seconds.set(0);
	}

}
