/**
 * 
 */
package org.altemista.it;

/*
 * #%L
 * altemista-cloud web integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.http.HttpStatus;
import org.junit.Test;
import cloud.altemista.fwk.it.AbstractIT;
import cloud.altemista.fwk.it.controller.WebI18nController;

/**
 * Simple, base integration test to check the application has been deployed
 * @author NTT DATA
 */
public class WebI18nIT extends AbstractIT {
	
	@Test
	public void testI18n() {
		
		this.testMappings(WebI18nController.MAPPING);
	}
	
	@Test
	public void testCodeListLangparam() {
		
		this.testMapping(WebI18nController.MAPPING + "/codelist?locale=jp");
	}
	
	@Test
	public void testLangparamNoChange() {
		
		this.testMapping(WebI18nController.MAPPING + "/msgsrc/expectdefault");
		
		// (there are no messages in this locale)
		this.testMapping(WebI18nController.MAPPING + "/msgsrc/expectdefault?locale=it_IT");
	}
	
	@Test
	public void testLangparamRightLocale() {
		
		this.testMapping(WebI18nController.MAPPING + "/msgsrc/expectes?locale=es_ES");
		this.testMapping(WebI18nController.MAPPING + "/msgsrc/expecten?locale=en_GB");
		
		// (no session: the latest locale used is not kept)
		this.testMapping(WebI18nController.MAPPING + "/msgsrc/expectdefault");
	}
	
	@Test
	public void testLangparamWrongLocale() {
		
		this.testMapping(WebI18nController.MAPPING + "/msgsrc/expectes?locale=en_GB", HttpStatus.SC_INTERNAL_SERVER_ERROR);
		this.testMapping(WebI18nController.MAPPING + "/msgsrc/expecten?locale=es_ES", HttpStatus.SC_INTERNAL_SERVER_ERROR);
		
		// (no session: the latest locale used is not kept)
		this.testMapping(WebI18nController.MAPPING + "/msgsrc/expectdefault");
	}
	
	@Test
	public void testServiceException() {
		
		this.testMapping(WebI18nController.MAPPING + "/serviceException", HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}
}
