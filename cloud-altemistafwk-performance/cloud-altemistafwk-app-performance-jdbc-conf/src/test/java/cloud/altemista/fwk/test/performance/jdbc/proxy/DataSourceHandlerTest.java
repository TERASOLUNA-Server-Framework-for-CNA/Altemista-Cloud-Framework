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


import java.lang.reflect.Proxy;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import cloud.altemista.fwk.core.performance.MeasuresStorage;
import cloud.altemista.fwk.test.AbstractSpringContextTest;

/**
 * Tests the DataSourceHandler is actually wrapping the DataSource.
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/cloud-altemistafwk-test-performance-jdbc.xml")
public class DataSourceHandlerTest extends AbstractSpringContextTest {
	
	/** The dataSource DataSource */
	private DataSource dataSource;
	
	/** The jdbcTemplate JdbcTemplate */
	private JdbcTemplate jdbcTemplate;
	
	/** The storage policy */
	@Autowired
	@Qualifier("jdbcMeasuresStorage")
	private MeasuresStorage storage;
	
	/**
	 * Tests the Spring context is correct
	 */
	@Test
	public void testSpringContext() {
		
		Assert.assertNotNull(this.getDataSource());
		Assert.assertNotNull(this.getStorage());
	}
	
	/**
	 * Tests the DataSourceHandler is actually wrapping the DataSource
	 */
	@Test
	public void testDataSourceIsWrapper() {
		
		Assert.assertTrue(Proxy.isProxyClass(this.getDataSource().getClass()));
	}
	
	/**
	 * Sets the DataSource, instantiating the JdbcTemplate instance
	 * @param dataSource DataSource
	 */
	@Autowired
	@Qualifier("wrappedDataSource")
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * @return the dataSource
	 */
	protected DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @return the jdbcTemplate
	 */
	protected JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * @return the storage
	 */
	protected MeasuresStorage getStorage() {
		return storage;
	}
}
