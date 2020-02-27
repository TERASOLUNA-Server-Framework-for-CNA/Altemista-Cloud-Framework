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
import org.springframework.jdbc.core.RowCountCallbackHandler;
import cloud.altemista.fwk.core.performance.model.MeasuredTask;
import cloud.altemista.fwk.performance.jdbc.model.JdbcTaskInfo;

/**
 * Tests the PreparedStatementHandler and ResultSetHandler are actually registering executions
 * @author NTT DATA
 */
public class PreparedStatementHandlerTest extends DataSourceHandlerTest {
	
	/** The example query */
	private static final String EXAMPLE_QUERY = "SELECT * FROM T_RECIPE WHERE ID <> ?";

	/**
	 * Tests the PreparedStatementHandler and ResultSetHandler are actually registering executions
	 */
	@Test
	public void testPreparedStatementRegistered() {
		
		// (synchronize to avoid problems if tests are run in parallel)
		synchronized (this.getStorage()) {
			this.getStorage().clear();
			Assert.assertTrue(this.getStorage().get().isEmpty());
			
			RowCountCallbackHandler rch = new RowCountCallbackHandler();
			this.getJdbcTemplate().query(EXAMPLE_QUERY, new Object[]{ 5 }, rch);
			
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
			
			// Asserts the parameters has ben registered
			List<Object> sqlParameters = taskInfo.getSqlParameters();
			Assert.assertNotNull(sqlParameters);
			Assert.assertFalse(sqlParameters.isEmpty());
			Object sqlParameter = sqlParameters.iterator().next();
			Assert.assertNotNull(sqlParameter);
			Assert.assertEquals(Integer.valueOf(5), sqlParameter);
			
			// Asserts related to the ResultSetHandler
			Assert.assertEquals(rch.getRowCount(), taskInfo.getResultCount());
		}
	}

}
