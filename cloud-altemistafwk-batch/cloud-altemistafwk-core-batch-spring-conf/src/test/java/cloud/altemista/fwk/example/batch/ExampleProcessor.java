package cloud.altemista.fwk.example.batch;

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


import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Example ItemProcessor for testing the batch module
 * @author NTT DATA
 */
@Component
public class ExampleProcessor implements ItemProcessor<ExampleBean, ExampleBean> {

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public ExampleBean process(final ExampleBean bean) throws Exception {

		final String newName = bean.getName().toUpperCase();
		final String newSurname = bean.getSurname().toUpperCase();

		final ExampleBean newBean = new ExampleBean();
		newBean.setName(newName);
		newBean.setSurname(newSurname);
		return newBean;
	}
}
