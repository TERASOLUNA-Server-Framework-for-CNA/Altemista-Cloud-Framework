/**
 * 
 */
package org.altemista.cloudfwk.it;

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


import org.openqa.selenium.WebDriver;
import org.altemista.cloudfwk.it.ItRequest;

/**
 * Convenience interface to combine both main SeleniumHQ interface and ItRequest 
 * @author NTT DATA
 */
public interface WebDriverItRequest extends WebDriver, ItRequest {

	// (no new methods)
}
