
package org.altemista.cloudfwk.demo.performance;

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
import org.altemista.cloudfwk.common.annotation.HiddenValue;

/**
 * An example class to test performance aspect
 * @author NTT DATA
 */
@Service
public class FirstTestService {

	/** The second service. */
	@Autowired
	private SecondTestService secondService;

	/** The third service. */
	@Autowired
	private ThirdTestService thirdService;

	/**
	 * Example method
	 */
	public void exampleMethod() {
		
		String string = this.secondService.secondExampleMethod("foo");
		
		this.thirdService.thirdExampleMethod(string);
	}

	/**
	 * Example method with hidden values
	 * @param hiddenArgument String
	 * @param visibleArgument String
	 * @return String
	 */
	@HiddenValue
	public String exampleMethodWithHiddenValues(@HiddenValue String hiddenArgument, String visibleArgument) {
		
		try {
			Thread.sleep(RandomUtils.nextLong(75L, 125L)); // NOSONAR
		} catch (InterruptedException e) {
			// (this exception is silently ignored)
		}
		
		return hiddenArgument;
	}

}
