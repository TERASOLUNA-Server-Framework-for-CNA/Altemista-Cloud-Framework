package org.altemista.cloudfwk.example.batch;

/*
 * #%L
 * altemista-cloud batch: Spring Batch implementation CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * Example ItemWriter for testing the batch module
 * @author NTT DATA
 */
@Component
public class ExampleWriter implements ItemWriter<ExampleBean> {
	
	/** The SLF4J Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleWriter.class);
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(List<? extends ExampleBean> beans) throws Exception {
		
		LOGGER.debug("Begin");
		for (ExampleBean bean : beans) {
			LOGGER.debug("\t{}, {}", bean.getSurname(), bean.getName());
		}
		LOGGER.debug("End");
	}

}
