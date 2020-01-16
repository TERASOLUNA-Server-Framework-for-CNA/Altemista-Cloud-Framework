package org.altemista.cloudfwk.test.performance.jdbc.proxy;

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


import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the ConnectionHandler is actually wrapping the Connections returned by the DataSource.
 * @author NTT DATA
 */
public class ConnectionHandlerTest extends DataSourceHandlerTest {
	
	/**
	 * Tests the ConnectionHandler is actually wrapping the Connections returned by the DataSource
	 * @throws SQLException
	 */
	@Test
	public void testAspect() throws SQLException {
		
		Connection connection = null;
		try {
			connection = this.getDataSource().getConnection();
			
			Assert.assertNotNull(connection);
			Assert.assertTrue(Proxy.isProxyClass(connection.getClass()));
			
		} finally {
			DbUtils.close(connection);
		}
	}
}
