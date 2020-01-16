package org.altemista.cloudfwk.test.performance;

/*
 * #%L
 * altemista-cloud performance: execution performance statistics
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

/**
 * 
 */


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.altemista.cloudfwk.core.performance.MeasuresStorage;
import org.altemista.cloudfwk.core.performance.impl.LongestMeasuresStorageImpl;
import org.altemista.cloudfwk.core.performance.model.MeasuredTask;
import org.altemista.cloudfwk.core.performance.model.SimpleTaskInfo;
import org.altemista.cloudfwk.core.performance.model.TaskInfo;

/**
 * Tests the simple storage policy for keeping the longest measured tasks
 * @author NTT DATA
 */
public class MeasuresStorageTest {
	
	/** Reduces the storage size to speed up the tests */
	private static final int TEST_STORAGE_MAX_SIZE = 10;
	
	/** The simple storage policy that will be tested */
	private MeasuresStorage storage = new LongestMeasuresStorageImpl();
	
	/**
	 * Default constructor
	 */
	public MeasuresStorageTest() {
		super();
		
		LongestMeasuresStorageImpl storage = new LongestMeasuresStorageImpl();
		storage.setMaxSize(TEST_STORAGE_MAX_SIZE);
		
		this.storage = storage;
	}

	/**
	 * Test the clear method
	 */
	@Test
	public void testClear() {
		
		// (as MeasuresStorage is static, synchronize to avoid problems if tests are run in parallel)
		synchronized (this.storage) {
			this.storage.clear();
			Assert.assertTrue(this.storage.get().isEmpty());
		}
	}
	
	/**
	 * Test putting one element when the registry is empty
	 */
	@Test
	public void testPutOneWhenRegistryIsEmpty() {
		
		// (as MeasuresStorage is static, synchronize to avoid problems if tests are run in parallel)
		synchronized (this.storage) {
			this.storage.clear();
			
			int before = this.storage.get().size();
			
			this.registerNewMeasuredTask(0L, 10L);
			
			int after = this.storage.get().size();
			
			Assert.assertNotEquals(before, after);
			Assert.assertEquals(1, after - before);
		}
	}
	
	/**
	 * Test putting one element when the registry is at half capacity
	 */
	@Test
	public void testPutOneWhenRegistryIsHalf() {
		
		// (as MeasuresStorage is static, synchronize to avoid problems if tests are run in parallel)
		synchronized (this.storage) {
			this.storage.clear();
			
			this.registerNewMeasuredTasks(TEST_STORAGE_MAX_SIZE / 2, 0L, 10L);
			
			int before = this.storage.get().size();
			
			this.registerNewMeasuredTask(0L, 10L);
			
			int after = this.storage.get().size();
			
			Assert.assertNotEquals(before, after);
			Assert.assertEquals(1, after - before);
		}
	}
	
	/**
	 * Test putting one element when the registry is full
	 */
	@Test
	public void testPutOneWhenRegistryIsFull() {
		
		// (as MeasuresStorage is static, synchronize to avoid problems if tests are run in parallel)
		synchronized (this.storage) {
			this.storage.clear();
			
			this.registerNewMeasuredTasks(TEST_STORAGE_MAX_SIZE, 0L, 10L);
			
			int before = this.storage.get().size();
			
			this.registerNewMeasuredTask(0L, 10L);
			
			int after = this.storage.get().size();
			
			Assert.assertEquals(before, after);
		}
	}
	
	/**
	 * Test the order of the elements
	 */
	@Test
	public void testOrder() {
		
		// (as JdbcStatisticsRegistry is static, synchronize to avoid problems if tests are run in parallel)
		synchronized (this.storage) {
			this.storage.clear();
			
			// (larger lapse range to maximize the distribution)
			this.registerNewMeasuredTasks(TEST_STORAGE_MAX_SIZE, 0L, 150L);
			
			long totalTimeMillis = Long.MAX_VALUE;
			for (MeasuredTask task : this.storage.get()) {
				Assert.assertTrue(task.getTotalTimeMillis() <= totalTimeMillis);
				totalTimeMillis = task.getTotalTimeMillis();
			}
		}
	}
	
	/**
	 * Test that the slowest executions are kept
	 */
	@Test
	public void testSlowestAreKept() {
		
		// (as JdbcStatisticsRegistry is static, synchronize to avoid problems if tests are run in parallel)
		synchronized (this.storage) {
			this.storage.clear();
			
			// Initially, simulate fast executions
			this.registerNewMeasuredTasks(TEST_STORAGE_MAX_SIZE, 0L, 10L);
			long totalFastLapses = 0L;
			for (MeasuredTask jdbcStatistics : this.storage.get()) {
				totalFastLapses += jdbcStatistics.getTotalTimeMillis();
			}
			
			// (control variable)
			final long slowExecutionsBegin = System.currentTimeMillis();
			
			// Now, simulate slower executions
			this.registerNewMeasuredTasks(TEST_STORAGE_MAX_SIZE, 50L, 150L);
			long totalSlowLapses = 0L;
			for (MeasuredTask jdbcStatistics : this.storage.get()) {
				// (all the fast executions should no longer be in the registry)
				Assert.assertFalse(jdbcStatistics.getStartTimeMillis() <= slowExecutionsBegin);
				totalSlowLapses += jdbcStatistics.getTotalTimeMillis();
			}
			Assert.assertTrue(totalSlowLapses > totalFastLapses);
			
			// Now, simulate mixed executions
			this.registerNewMeasuredTasks(TEST_STORAGE_MAX_SIZE, 0L, 100L);
			long totalSlowLapses2 = 0L;
			for (MeasuredTask jdbcStatistics : this.storage.get()) {
				totalSlowLapses2 += jdbcStatistics.getTotalTimeMillis();
			}
			// (some of the executions should have replaced the previously slow executions)
			Assert.assertTrue(totalSlowLapses2 >= totalSlowLapses);
			
			// (control variable)
			final long fastExecutionsBegin = System.currentTimeMillis();
			
			// Finally, simulate another fast executions
			this.registerNewMeasuredTasks(TEST_STORAGE_MAX_SIZE, 0L, 10L);
			for (MeasuredTask jdbcStatistics : this.storage.get()) {
				// (none of these fast executions is to be in the registry)
				Assert.assertFalse(jdbcStatistics.getStartTimeMillis() >= fastExecutionsBegin);
			}
			
		}
	}
	
	/**
	 * Convenience method to create, close and register n fake MeasuredTask
	 * @param n the number of MeasuredTask to create, close and register
	 * @param minimumLapse the minimum number of milliseconds the MeasuredTask will have as time elapsed
	 * @param maximumLapse the maximum number of milliseconds the MeasuredTask will have as time elapsed
	 */
	private void registerNewMeasuredTasks(int n, long minimumLapse, long maximumLapse) {
		
		for (int i = 0; i < n; i++) {
			this.registerNewMeasuredTask(minimumLapse, maximumLapse);
		}
	}
	
	/**
	 * Convenience method to create, close and register a fake MeasuredTask
	 * @param minimumLapse the minimum number of milliseconds the MeasuredTask will have as time elapsed
	 * @param minimumLapse the minimum number of milliseconds the MeasuredTask will have as time elapsed
	 */
	private void registerNewMeasuredTask(long minimumLapse, long maximumLapse) {
		
		// (small pause to ensure no two JdbcStatistics are created in the same millisecond)
		try {
			Thread.sleep(1L); // NOSONAR
		} catch (InterruptedException e) {
			// (this exception is silently ignored)
		}
		
		TaskInfo taskInfo = new SimpleTaskInfo(RandomStringUtils.randomAscii(RandomUtils.nextInt(10, 200)));
		MeasuredTask task = new MeasuredTask(taskInfo, 1);
		
		// (random lapse to populate the JdbcStatistics with different time lapses)
		try {
			Thread.sleep(RandomUtils.nextLong(minimumLapse, maximumLapse)); // NOSONAR
		} catch (InterruptedException e) {
			// (this exception is silently ignored)
		}
		
		task.stop();
		this.storage.put(task);
	}

}
