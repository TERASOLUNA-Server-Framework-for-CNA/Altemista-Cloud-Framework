
package org.altemista.cloudfwk.core.monitoring.impl.indicator;

/*
 * #%L
 * altemista-cloud monitoring
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.altemista.cloudfwk.common.monitoring.exception.MonitoringException;
import org.altemista.cloudfwk.common.monitoring.model.MonitoringInfo;
import org.altemista.cloudfwk.common.monitoring.model.Status;

/**
 * Indicator to check the status of a database through a data source.
 * @author NTT DATA
 */
public class DatabaseIndicator extends AbstractIndicator implements InitializingBean {

	/** The SLF4J logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseIndicator.class);

	/** The DataSource this aspect will check. */
	private DataSource dataSource;

	/** Sample query to check if database is available. */
	private String checkQuery;

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.impl.indicator.AbstractIndicator#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();

		if (this.dataSource == null) {
			throw new MonitoringException("invalid_indicator_configuration", new Object[] { "datasource", this.getId() });
		}
		if (StringUtils.isBlank(this.checkQuery)) {
			throw new MonitoringException("invalid_indicator_configuration", new Object[] { "checkQuery", this.getId() });
		}
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.Indicator#getDescription()
	 */
	@Override
	public String getDescription() {
		return String.format("database %s", this.getId());
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.monitoring.Indicator#execute()
	 */
	@Override
	public MonitoringInfo execute() {

		MonitoringInfo info = new MonitoringInfo(this.getId(), this.getDescription());

		// Establishes the connection and executes the sample statement
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			// Establishes the connection
			try {
				connection = this.dataSource.getConnection();
				
			} catch (SQLException e) {
				LOGGER.error(this.getDescription() + ": SQL exception establishing connection", e);
				return info.update(Status.FAILED, this.getDescription() + ": unable to establish connection");
			}

			// Executes the sample statement
			try {
				statement = connection.prepareStatement(this.checkQuery); // NOSONAR
				statement.execute();
				return info.update(Status.OK);

			} catch (SQLException e) {
				LOGGER.error(this.getDescription() + ": SQL exception executing statement", e);
				return info.update(Status.FAILED, this.getDescription() + ": unable to execute statement");
			}
			
		} finally {
			DbUtils.closeQuietly(connection, statement, null);
		}
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
	
		this.dataSource = dataSource;
	}

	/**
	 * @param checkQuery the checkQuery to set
	 */
	public void setCheckQuery(String checkQuery) {
	
		this.checkQuery = checkQuery;
	}
}
