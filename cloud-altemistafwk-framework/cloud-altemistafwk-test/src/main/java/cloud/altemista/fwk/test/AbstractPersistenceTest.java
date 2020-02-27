package cloud.altemista.fwk.test;

/*
 * #%L
 * Test dependency for altemista-cloud framework and application unit tests
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Convenience base class for tests depending on the in-memory DataSource
 * @author NTT DATA
 */
@ContextConfiguration("classpath:cloud/altemista/fwk/config/test/cloud-altemistafwk-test-exampleDatabase.xml")
public abstract class AbstractPersistenceTest extends AbstractSpringContextTest {
	
	/** The dataSource DataSource */
	private DataSource dataSource;
	
	/** The jdbcTemplate JdbcTemplate */
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Sets the DataSource, instantiating the JdbcTemplate instance
	 * @param dataSource DataSource
	 */
	@Autowired
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
}
