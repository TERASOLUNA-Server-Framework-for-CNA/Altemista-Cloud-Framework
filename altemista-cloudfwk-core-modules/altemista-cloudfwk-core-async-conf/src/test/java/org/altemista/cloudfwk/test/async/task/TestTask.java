package org.altemista.cloudfwk.test.async.task;

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


public class TestTask implements Runnable {

	public void run() {

		try {
			Thread.sleep(2000); // NOSONAR
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
