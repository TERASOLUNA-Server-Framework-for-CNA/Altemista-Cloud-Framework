
package cloud.altemista.fwk.demo.performance;

/*
 * #%L
 * altemista-cloud performance: execution performance statistics CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

/**
 * An example class to test performance aspect
 * @author NTT DATA
 */
@Service
public class ThirdTestService {

	/**
	 * Example method
	 * @param argument String
	 * @return String
	 */
	public String thirdExampleMethod(String argument) {
		
		try {
			Thread.sleep(RandomUtils.nextLong(50L, 150L)); // NOSONAR
		} catch (InterruptedException e) {
			// (this exception is silently ignored)
		}
		
		return argument + " baz";
	}
}
