/**
 * 
 */
package org.altemista.cloudfwk.it;

/*
 * #%L
 * altemista-cloud web application: Spring Web Flow + Apache Tiles integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Tests Spring Web Flow and Apache Tiles integration
 * @author NTT DATA
 */
public class WebFlowAndTilesIT extends AbstractWebDriverIT {
	
	private static final String FLOW_ENTRANCE_URI = "/example-flow";
	
	private static final String NAME_FIELD_NAME = "name";
	private static final String NAME_FIELD_FIRST_VALUE = "foo";
	private static final String NAME_FIELD_SECOND_VALUE = "bar";
	
	private static final String SUBMIT_BUTTON_NAME = "_eventId_submit";
	
	private static final String FIRST_VALIDATION_MESSAGE = "Your name is: " + NAME_FIELD_FIRST_VALUE;
	private static final String SECOND_VALIDATION_MESSAGE = "Your name is: " + NAME_FIELD_SECOND_VALUE;
	
	private static final String REVISE_BUTTON_NAME = "_eventId_revise";
	private static final String CANCEL_BUTTON_NAME = "_eventId_cancel";
	private static final String CONFIRM_BUTTON_NAME = "_eventId_confirm";
	
	private static final String DATA_CANCELLED_MESSAGE = "Data cancelled";
	private static final String DATA_CONFIRMED_MESSAGE = "Data confirmed";

	private static final String START_AGAIN_LINK_TEXT = "Start again";
	
	/**
	 * Tests the example flow
	 */
	@Test
	public void testExampleFlow() {
		
		// Enters the flow
		WebDriverItRequest webDriver = this.invokeMapping(FLOW_ENTRANCE_URI);
		Assert.assertEquals(HttpStatus.SC_OK, webDriver.getStatusCode());
		
		// Inputs the name "foo" and submits the form
		{
			WebElement nameField = webDriver.findElement(By.name(NAME_FIELD_NAME));
			Assert.assertNotNull(nameField);
			nameField.clear();
			nameField.sendKeys(NAME_FIELD_FIRST_VALUE);
			WebElement submitButton = webDriver.findElement(By.name(SUBMIT_BUTTON_NAME));
			Assert.assertNotNull(submitButton);
			submitButton.click();
			Assert.assertEquals(HttpStatus.SC_OK, webDriver.getStatusCode());
		}
		
		// Click "revise"
		{
			Assert.assertTrue(webDriver.getResponseBody().contains(FIRST_VALIDATION_MESSAGE));
			WebElement reviseButton = webDriver.findElement(By.name(REVISE_BUTTON_NAME));
			Assert.assertNotNull(reviseButton);
			reviseButton.click();
		}
		
		// Click "cancel"
		{
			WebElement cancelButton = webDriver.findElement(By.name(CANCEL_BUTTON_NAME));
			Assert.assertNotNull(cancelButton);
			cancelButton.click();
			Assert.assertTrue(webDriver.getResponseBody().contains(DATA_CANCELLED_MESSAGE));
		}
		
		// Click "start again" (re-enters the flow)
		{
			WebElement startAgainLink = webDriver.findElement(By.linkText(START_AGAIN_LINK_TEXT));
			Assert.assertNotNull(startAgainLink);
			startAgainLink.click();
		}
		
		// Inputs the name "bar" and submits the form
		{
			WebElement nameField = webDriver.findElement(By.name(NAME_FIELD_NAME));
			Assert.assertNotNull(nameField);
			nameField.clear();
			nameField.sendKeys(NAME_FIELD_SECOND_VALUE);
			WebElement submitButton = webDriver.findElement(By.name(SUBMIT_BUTTON_NAME));
			Assert.assertNotNull(submitButton);
			submitButton.click();
			Assert.assertEquals(HttpStatus.SC_OK, webDriver.getStatusCode());
		}
		
		// Click "confirm"
		{
			Assert.assertTrue(webDriver.getResponseBody().contains(SECOND_VALIDATION_MESSAGE));
			WebElement confirmButton = webDriver.findElement(By.name(CONFIRM_BUTTON_NAME));
			Assert.assertNotNull(confirmButton);
			confirmButton.click();
			Assert.assertTrue(webDriver.getResponseBody().contains(DATA_CONFIRMED_MESSAGE));
		}
	}
}
