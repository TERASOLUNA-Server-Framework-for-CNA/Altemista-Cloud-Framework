package org.altemista.cloudfwk.core.i18n;

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


import static org.junit.Assert.assertEquals;

import java.util.Locale;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.NoSuchMessageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.altemista.cloudfwk.core.AltemistaCloudfwkApplicationContextInitializer;

/**
 * The Class CustomResourceBundleMessageSourceTest.
 * 
 * @author NTT DATA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
	initializers = AltemistaCloudfwkApplicationContextInitializer.class,
	locations = {
		"classpath:org/altemista/cloudfwk/config/core/altemista-cloudfwk-core.xml",
		"classpath:org/altemista/cloudfwk/config/test/altemista-cloudfwk-test-i18n.xml"
	})
public class PropertiesI18nCodeListTest {

	/** The message resource. */
	@Inject
//	@Named("CL_I18N")
	PropertiesI18nCodeList messageResource;

	/**
	 * Inits the.
	 */
	@Before
	public void init() {
		Assert.assertNotNull(messageResource);
	}

	/**
	 * Check spanish.
	 */
	@Test
	public void checkSpanish() {

		Locale locale = new Locale("es", "ES");

		assertEquals("Mensaje de prueba",
				messageResource.getMessage("test.message", null, locale));
		assertEquals("Test", messageResource.getMessage("unspecified.field",
				null, "Test", locale));

	}

	/**
	 * Check english.
	 */
	@Test
	public void checkEnglish() {

		Locale locale = new Locale("en", "GB");

		assertEquals("Test Message",
				messageResource.getMessage("test.message", null, locale));
		assertEquals("Test", messageResource.getMessage("unspecified.field",
				null, "Test", locale));

	}

	/**
	 * Check no such method exception.
	 */
	@Test(expected = NoSuchMessageException.class)
	public void checkNoSuchMessageException() {
		messageResource.getMessage("label.notFound", null, Locale.UK);
	}

}
