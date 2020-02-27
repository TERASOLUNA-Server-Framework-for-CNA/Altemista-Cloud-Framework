
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * An example class to test performance aspect
 * @author NTT DATA
 */
@Service
public class SecondTestService {

	@Autowired
	private ThirdTestService thirdService;

	/**
	 * Example method
	 * @param argument String
	 * @return String
	 */
	public String secondExampleMethod(String argument) {
		
		String ret = argument + " bar";
		for (int i = 0, n = RandomUtils.nextInt(3, 5); i < n; i++) {
			
			try {
				Thread.sleep(RandomUtils.nextLong(25L, 50L)); // NOSONAR
			} catch (InterruptedException e) {
				// (this exception is silently ignored)
			}
			
			ret = this.thirdService.thirdExampleMethod(ret);
		}
		
		return ret;
	}
}
