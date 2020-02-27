package cloud.altemista.fwk.it.persistence;

/*
 * #%L
 * altemista-cloud persistence: MyBatis integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.junit.Test;
import cloud.altemista.fwk.it.AbstractIT;
import cloud.altemista.fwk.it.persistence.controller.MyBatisController;

public class MyBatisIT extends AbstractIT {
	
	@Test
	public void readOnlyCrudExample() {
		
		this.testMappings(MyBatisController.MAPPING);
	}
	
	/*
	 * TODO test insert
	 * TODO test update
	 * TODO test delete
	 * TODO test paging
	 */
	
}
