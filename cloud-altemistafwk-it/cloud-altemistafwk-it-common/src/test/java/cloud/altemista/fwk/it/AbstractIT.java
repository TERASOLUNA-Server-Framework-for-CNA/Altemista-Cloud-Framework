/**
 * 
 */
package cloud.altemista.fwk.it;

/*
 * #%L
 * altemista-cloud common: integration tests common utilities
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cloud.altemista.fwk.core.CloudAltemistafwkApplicationContextInitializer;

/**
 * Provides a base for the integration tests
 * @param <R> ItRequest
 * @author NTT DATA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		initializers = CloudAltemistafwkApplicationContextInitializer.class,
		locations = "classpath:cloud/altemista/fwk/config/it/cloud-altemistafwk-it.xml"
	)
public abstract class AbstractIT<R extends ItRequest> {

	/** The SLF4J logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIT.class);

	/** Internal format to create the series of internal URIs */
	private static final String INTEGRATION_TEST_CONSECUTIVE_URI_FORMAT = "%s/%d";
	
	/** Allows different implementations for resolve controller mappings as URIs and complete URLs */
	@Autowired
	protected ItMappingResolver resolver;
	
	/** Allows different implementations for invoking complete URLs from integration tests */
	@Autowired
	protected ItRequestProvider<R> provider;
	
	/**
	 * Resets the ItRequest to its initial state before each test
	 */
	@Before
	public void resetItRequestProviderBeforeEachTest() {
		
		this.provider.reset();
	}
	
	/**
	 * Navigates to an internal mapping 
	 * @param mapping the internal mapping to navigate to
	 * @return R to allow the test to check the response values
	 */
	protected R invokeMapping(String mapping) {
		
		return this.invokeUri(this.resolver.getUri(mapping));
	}
	
	/**
	 * Navigates to an internal URI 
	 * @param uri the internal URI to navigate to
	 * @return R to allow the test to check the response values
	 */
	protected R invokeUri(String uri) {
		
		return this.invokeUrl(this.resolver.getUrl(uri));
	}
	
	/**
	 * Navigates to an URL 
	 * @param url the URL to navigate to
	 * @return R to allow the test to check the response values
	 */
	protected R invokeUrl(String url) {
		
		try {
			return this.provider.invokeUrl(url);
			
		} catch (IOException e) {
			Assert.fail(String.format("IOException at URL: %s: %s", url, e.getMessage()));
			return null;
		}
	}
	
	/**
	 * Navigates to an internal URI and verifies the response is OK (HTTP status code 200) 
	 * @param mapping the internal mapping to navigate to
	 * @return R to allow the test to check additional response values such as HTTP headers or response body
	 */
	protected R testMapping(String mapping) {
		
		return this.testMapping(mapping, HttpStatus.OK.value());
	}

	/**
	 * Navigates to an internal URI and verifies the response has the expected HTTP status code
	 * @param mapping the internal mapping to navigate to
	 * @param expectedHttpStatus the expected HTTP status code (one of HttpStatus.*.value())
	 * @return R to allow the test to check additional response values such as HTTP headers or response body
	 */
	protected R testMapping(String mapping, int expectedHttpStatus) {
		
		return this.testUri(this.resolver.getUri(mapping), expectedHttpStatus);
	}
	
	/**
	 * Navigates to an internal URI and verifies the response is OK (HTTP status code 200) 
	 * @param uri the internal URI to navigate to
	 * @return R to allow the test to check additional response values such as HTTP headers or response body
	 */
	protected R testUri(String uri) {
		
		return this.testUri(uri, HttpStatus.OK.value());
	}

	/**
	 * Navigates to an internal URI and verifies the response has the expected HTTP status code
	 * @param uri the internal URI to navigate to
	 * @param expectedHttpStatus the expected HTTP status code (one of HttpStatus.*.value())
	 * @return R to allow the test to check additional response values such as HTTP headers or response body
	 */
	protected R testUri(String uri, int expectedHttpStatus) {
		
		R request = this.invokeUri(uri);
		
		int httpStatus = request.getStatusCode();
		LOGGER.debug("Expected {}, got {} at URI: {}", expectedHttpStatus, httpStatus, uri);
		Assert.assertEquals("URI: " + uri, expectedHttpStatus, httpStatus);
		
		return request;
	}
	
	/**
	 * Navigates to a series of internal URIs and verifies the responses are OK (HTTP status code 200) 
	 * until one of the responses is NOT_FOUND (HTTP status code 404)
	 * @param mapping the base of the internal mappings to navigate to.
	 * The actual URIs will have "/1", "/2", and so forth concatenated.
	 */
	protected void testMappings(String mapping) {
		
		this.testMappings(mapping, HttpStatus.OK.value(), HttpStatus.NOT_FOUND.value());
	}

	/**
	 * Navigates to a series of internal URIs and verifies the responses have the expected HTTP status code
	 * until one of the responses is the marker HTTP status code for termination
	 * @param mapping the base of the internal mappings to navigate to.
	 * The actual URIs will have "/1", "/2", and so forth concatenated.
	 * @param expectedHttpStatus the expected HTTP status code (one of HttpStatus.SC_*)
	 * @param breakHttpStatus the marker HTTP status code for termination (usually, HttpStatus.SC_NOT_FOUND)
	 */
	protected void testMappings(String mapping, int expectedHttpStatus, int breakHttpStatus) {
		
		final String baseUri = this.resolver.getUri(mapping);
		LOGGER.info("Trying to access to " + baseUri);
		List<String> failMessages = new ArrayList<String>();
		
		// Starting with "/1"...
		int i = 1;
		for (;; i++) {
			
			// Creates the URI to be tested in this iteration
			String uri = String.format(INTEGRATION_TEST_CONSECUTIVE_URI_FORMAT, baseUri, i);
			
			int httpStatus = this.invokeUri(uri).getStatusCode();
			
			// If the response is the marker HTTP status code for termination, terminates
			if (httpStatus == breakHttpStatus) {
				// Expected at least one iteration before termination  
				if (i == 1) {
					Assert.fail(String.format(
							"Expected %d, got %d at URI: %s", expectedHttpStatus, httpStatus, uri));
				}
				LOGGER.debug("Stopping. Got {} at URI: {}", httpStatus, uri);
				break;
			}
				
			try {
				// Verifies the response have the expected HTTP status code
				LOGGER.debug("Expected {}, got {} at URI: {}", expectedHttpStatus, httpStatus, uri);
				Assert.assertEquals("URI: " + uri, expectedHttpStatus, httpStatus);
				
			} catch (AssertionError a) {
				// Stores all the AssertionError messages to show all the failures (not just the first one)
				failMessages.add(a.getMessage());
			}
		}

		// If there were AssertionErrors, fails with all the failure messages combined
		if (!failMessages.isEmpty()) {
			Assert.fail(StringUtils.join(failMessages, StringUtils.LF));
		}
	}
}
