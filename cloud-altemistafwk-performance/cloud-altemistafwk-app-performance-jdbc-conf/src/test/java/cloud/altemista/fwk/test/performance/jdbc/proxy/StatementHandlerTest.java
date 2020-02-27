/**
 * 
 */
package cloud.altemista.fwk.test.performance.jdbc.proxy;

/*
 * #%L
 * altemista-cloud performance: JDBC monitoring and performance CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import cloud.altemista.fwk.core.performance.model.MeasuredTask;
import cloud.altemista.fwk.performance.jdbc.model.JdbcTaskInfo;

/**
 * Tests the StatementHandler is actually registering executions
 * @author NTT DATA
 */
public class StatementHandlerTest extends DataSourceHandlerTest {
	
	/** The example query */
	private static final String EXAMPLE_QUERY = "SELECT * FROM T_RECIPE";

	/**
	 * Tests the StatementHandler is actually registering executions
	 */
	@Test
	public void testStatementRegistered() {
		
		// (synchronize to avoid problems if tests are run in parallel)
		synchronized (this.getStorage()) {
			this.getStorage().clear();
			Assert.assertTrue(this.getStorage().get().isEmpty());
			
			this.getJdbcTemplate().execute(EXAMPLE_QUERY);
			
			// Asserts one execution has been registered
			List<MeasuredTask> list = this.getStorage().get();
			Assert.assertFalse(list.isEmpty());
			Assert.assertEquals(1, list.size());
			
			// Asserts the registered execution is of the correct type
			MeasuredTask task = list.iterator().next();
			Assert.assertNotNull(task);
			Assert.assertFalse(task.isRunning());
			Assert.assertTrue(task.getTaskInfo() instanceof JdbcTaskInfo);
			
			// Asserts the query has been registered
			JdbcTaskInfo taskInfo = (JdbcTaskInfo) task.getTaskInfo();
			Assert.assertEquals(EXAMPLE_QUERY, taskInfo.getSqlQuery());
		}
	}

}
