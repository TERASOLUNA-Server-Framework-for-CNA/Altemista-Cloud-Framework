package org.altemista.cloudfwk.test.async.service;

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


import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

	// tag::usage[]
	@Async
	public Future<Boolean> longProcessingTask() { //<1>
		// (long processing task here)
		// end::usage[]
		try {
			Thread.sleep(1000); // NOSONAR
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// tag::usage[]
		return new AsyncResult<Boolean>(true); //<2>
	}
	// end::usage[]
}
