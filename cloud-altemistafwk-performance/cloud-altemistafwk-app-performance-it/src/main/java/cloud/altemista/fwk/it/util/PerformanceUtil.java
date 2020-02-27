/**
 * 
 */
package cloud.altemista.fwk.it.util;

/*
 * #%L
 * altemista-cloud performance module integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.commons.lang3.RandomUtils;

/**
 * Utility class for the peformance module integration tests
 * @author NTT DATA
 */
public final class PerformanceUtil {
	
	/**
	 * Convenience method to invoke Thread.sleep() silently ignoring InterruptedException
	 * @param millis the length of time to sleep in milliseconds
	 */
	public static final void sleep(long millis) {
		
		try {
			Thread.sleep(millis);
			
		} catch (InterruptedException e) {
			// (this exception is silently ignored)
		}
	}
	
	/**
	 * Convenience method to sleep a random number of milliseconds
	 * @param millisStartInclusive the shortest length of time to sleep in milliseconds
	 * @param millisEndInclusive the longest length of time to sleep in milliseconds
	 */
	public static final void randomSleep(long millisStartInclusive, long millisEndInclusive) {
		
		try {
			Thread.sleep(RandomUtils.nextLong(millisStartInclusive, millisEndInclusive));
			
		} catch (InterruptedException e) {
			// (this exception is silently ignored)
		}
	}
	
	/**
	 * Private default constructor (utility class)
	 */
	private PerformanceUtil() {
		super();
	}

}
