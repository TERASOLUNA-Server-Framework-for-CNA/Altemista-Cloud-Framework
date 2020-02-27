package cloud.altemista.fwk.core.properties.impl;

/*
 * #%L
 * altemista-cloud framework core
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.util.Assert;
import cloud.altemista.fwk.core.properties.PropertiesProvider;

/**
 * Simple database-backed properties provider. Usage:<br>
 * <tt>&lt;bean id="databasePropertiesProvider" class="cloud.altemista.fwk.core.properties.bean.DatabasePropertiesProvider"&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="dataSource" ref="yourDataSource" /&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="propertiesQuery" value="select KEY, VALUE from T_PROPERTIES" /&gt;<br>
 * &lt;/bean&gt;<br></tt>
 * @author NTT DATA
 */
public class DatabasePropertiesProvider extends JdbcDaoSupport implements PropertiesProvider {
	
	/** The logger */
	private static final Logger logger = LoggerFactory.getLogger(DatabasePropertiesProvider.class); // NOSONAR
	
	/**
	 * The query used to extract the properties from the database.
	 * It must SELECT at least two columns with the key and values of the properties
	 */
	private String propertiesQuery;
	
	/** The properties object */
	private Properties properties = new Properties();
	
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.support.JdbcDaoSupport#checkDaoConfig()
	 */
	@Override
	protected void checkDaoConfig() {
		super.checkDaoConfig();
		
		Assert.notNull(this.propertiesQuery);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.dao.support.DaoSupport#initDao()
	 */
	@Override
	protected void initDao() throws Exception {
		super.initDao();
		
		logger.debug("Reading database properties using query \"{}\"", this.propertiesQuery);
		
		this.getJdbcTemplate().query(this.propertiesQuery, new PropertiesRowCallbackHandler(properties));
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.properties.PropertiesProvider#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String key) {
		return this.properties.getProperty(key);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.properties.PropertiesProvider#getProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public String getProperty(String key, String defaultValue) {
		return this.properties.getProperty(key, defaultValue);
	}

	/**
	 * @param propertiesQuery the propertiesQuery to set
	 */
	public void setPropertiesQuery(String propertiesQuery) {
		this.propertiesQuery = propertiesQuery;
	}

	/**
	 * Implementation of RowCallbackHandler that populates a Properties object
	 * @author NTT DATA
	 */
	private static final class PropertiesRowCallbackHandler implements RowCallbackHandler {
		
		/** The properties object to be populated */
		private Properties properties;
		
		/**
		 * Constructor
		 * @param properties  object to be populated
		 */
		private PropertiesRowCallbackHandler(Properties properties) {
			super();
			
			this.properties = properties;
		}

		/* (non-Javadoc)
		 * @see org.springframework.jdbc.core.RowCallbackHandler#processRow(java.sql.ResultSet)
		 */
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			
			final String key = rs.getString(1);
			final String value = rs.getString(2);
			this.properties.put(key, value);

			logger.trace("Database property read: {}={}", key, value);
		}
	}

}
