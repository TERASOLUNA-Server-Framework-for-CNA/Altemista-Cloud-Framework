package cloud.altemista.fwk.core.monitoring.impl.indicator;

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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import cloud.altemista.fwk.common.monitoring.exception.MonitoringException;
import cloud.altemista.fwk.common.monitoring.model.MonitoringInfo;
import cloud.altemista.fwk.common.monitoring.model.Status;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

/**
 * Indicator to check the status of a MongoDB database.
 * @author NTT DATA
 */
public class MongoIndicator extends AbstractIndicator implements InitializingBean {

	/** The SLF4J logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseIndicator.class);

	/** The client of MongoDB to check the connection. */
	private MongoClient client;


	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.monitoring.impl.indicator.AbstractIndicator#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();

		if (this.client == null) {
			throw new MonitoringException("invalid_indicator_configuration", new Object[] { "client", this.getId() });
		}
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.monitoring.Indicator#getDescription()
	 */
	@Override
	public String getDescription() {
		return String.format("Mongo database %s", this.getId());
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.monitoring.Indicator#execute()
	 */
	@Override
	public MonitoringInfo execute() {

		MonitoringInfo info = new MonitoringInfo(this.getId(), this.getDescription());
		// Executes a sample statement
		try {
			this.client.getDatabaseNames();
			return info.update(Status.OK);

		} catch (MongoException e) {
			LOGGER.error(this.getDescription() + ": MongoDB exception executing statement", e);
			return info.update(Status.FAILED, this.getDescription() + ": unable to execute statement");
		}
			
	}

	/**
	 * @param mongo the mongo to set
	 */
	public void setClient(MongoClient client) {
	
		this.client = client;
	}

}
