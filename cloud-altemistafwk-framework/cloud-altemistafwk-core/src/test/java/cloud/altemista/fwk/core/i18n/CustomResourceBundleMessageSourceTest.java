package cloud.altemista.fwk.core.i18n;

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


import java.util.Locale;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cloud.altemista.fwk.core.CloudAltemistafwkApplicationContextInitializer;

/**
 * The Class CustomResourceBundleMessageSourceTest.
 * @author NTT DATA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		initializers = CloudAltemistafwkApplicationContextInitializer.class,
		locations = "classpath:cloud/altemista/fwk/config/core/cloud-altemistafwk-core.xml"
	)
public class CustomResourceBundleMessageSourceTest {
	
	static {
		
	}

	//tag::example[]
	/** The message resource. */
	@Autowired
	private DefaultLocaleMessageSource messageSource; // <1>
	//end::example[]
	
	public void example() {
		
		//tag::example[]
		// ...
		String message = this.messageSource.getMessage("example.message", null);
		// ...
		//end::example[]
		message.toString();
	}

	/**
	 * Inits the test.
	 */
	@Test
	public void autowiredTest() {
		
		Assert.assertNotNull(messageSource);
	}

	
	/**
	 * Check default.
	 */
	@Test
	public void checkDefault() {

		Assume.assumeNotNull(messageSource);
		Assert.assertEquals("Mensaje de prueba", messageSource.getMessage("test.message", null));
		Assert.assertEquals("Test", messageSource.getMessage("unspecified.field",null,"Test"));

	}
	
	/**
	 * Check spanish.
	 */
	@Test
	public void checkSpanish() {

		Assume.assumeNotNull(messageSource);
		
		final Locale locale = new Locale("es", "ES");
		
		Assert.assertEquals("Mensaje de prueba",
				messageSource.getMessage("test.message", null, locale));
		Assert.assertEquals("Test", messageSource.getMessage("unspecified.field",
				null, "Test", locale));

	}

	/**
	 * Check english.
	 */
	@Test
	public void checkEnglish() {

		Assume.assumeNotNull(messageSource);
		
		final Locale locale = new Locale("en", "GB");

		Assert.assertEquals("Test Message",
				messageSource.getMessage("test.message", null, locale));
		Assert.assertEquals("Test", messageSource.getMessage("unspecified.field",
				null, "Test", locale));

	}

	/**
	 * Check no such method exception.
	 */
	@Test(expected = NoSuchMessageException.class)
	public void checkNoSuchMessageException() {
		
		Assume.assumeNotNull(messageSource);
		
		messageSource.getMessage("label.notFound", null, Locale.UK);
	}

}
