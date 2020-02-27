package cloud.altemista.fwk.test.async.service;

/*
 * #%L
 * altemista-cloud asynchronous and scheduled executions CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledService {

	// tag::usage[]
	@Scheduled(fixedDelay = 5000) // <2>
	public void fixedRateExecution() { // <1>
		// (...)
		// end::usage[]
		try {
			Thread.sleep(2000); // NOSONAR
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// tag::usage[]
	}
		
	@Scheduled(cron = "*/5 * * * * MON-FRI") // <3>
	public void cronExpressionBasedExecution() {
		// (...)
		// end::usage[]
		try {
			Thread.sleep(2000); // NOSONAR
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// tag::usage[]
	}
	// end::usage[]
}
