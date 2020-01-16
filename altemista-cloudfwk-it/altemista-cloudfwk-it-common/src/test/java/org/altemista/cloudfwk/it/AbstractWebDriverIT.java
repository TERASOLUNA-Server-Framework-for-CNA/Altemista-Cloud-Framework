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


import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Base for the integration tests of the presentation layer that may require functionality provided by SeleniumHQ.
 * Besides the new convenience methods,
 * the existing methods will return {@link WebDriverItRequest} instead of {@code ItRequest}
 * @author NTT DATA
 */
public abstract class AbstractWebDriverIT extends AbstractIT<WebDriverItRequest> {

	/**
	 * Convenience method to try to navigate to a mapping and getting logged in at the login page
	 * @param mapping the internal mapping to navigate to
	 * @param username the usename
	 * @param password the password
	 * @return WebDriverItRequest for the test to check the response values
	 */
	protected WebDriverItRequest navigateAndLogin(String mapping, String username, String password) {
		
		// Navigates to the internal mapping
		WebDriverItRequest webDriver = this.invokeMapping(mapping);
		
		// Verifies if the login page has been hit
		if (StringUtils.equalsIgnoreCase(webDriver.getTitle(), "Login page")) {
			
			Assert.assertEquals(HttpStatus.SC_OK, webDriver.getStatusCode());
			
			WebElement usernameField = webDriver.findElement(By.name("username"));
			WebElement passwordField = webDriver.findElement(By.name("password"));
			WebElement submitButton = webDriver.findElement(By.name("submit"));
			Assert.assertNotNull(usernameField);
			Assert.assertNotNull(passwordField);
			Assert.assertNotNull(submitButton);
			
			// Does the login
			usernameField.clear();
			usernameField.sendKeys(username);
			passwordField.clear();
			passwordField.sendKeys(password);
			submitButton.click();
		}
		
		return webDriver;
	}
}
