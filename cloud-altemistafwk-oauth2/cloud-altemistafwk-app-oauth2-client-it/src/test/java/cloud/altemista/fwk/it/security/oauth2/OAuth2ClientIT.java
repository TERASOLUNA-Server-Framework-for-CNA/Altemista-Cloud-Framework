package cloud.altemista.fwk.it.security.oauth2;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import cloud.altemista.fwk.it.AbstractWebDriverIT;
import cloud.altemista.fwk.it.WebDriverItRequest;
import cloud.altemista.fwk.it.security.oauth2.controller.OAuth2ApiController;
import cloud.altemista.fwk.it.security.oauth2.controller.OAuth2ClientController;

/**
 * Access to the OAuth2 client
 * @author NTT DATA
 */
public class OAuth2ClientIT extends AbstractWebDriverIT {

	/** Mapping that returns a message directly */
	private static final String DIRECT_HELLO_MAPPING = OAuth2ClientController.MAPPING + "/hello";
	
	/** Mapping that returns a message invoking the resource server (the API) */
	private static final String API_HELLO_MAPPING = OAuth2ClientController.MAPPING + "/hello/api";

	/** An existing usename */
	private static final String USERNAME = "user";
	
	/** An existing password */
	private static final String PASSWORD = "password";
	
	@Test
	public void directHelloShouldReturnClientMessage() {
		
		WebDriverItRequest request = this.testMapping(DIRECT_HELLO_MAPPING);
		
		// (asserts the right message)
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertTrue(request.getResponseBody(), StringUtils.containsIgnoreCase(
				request.getResponseBody(), OAuth2ClientController.HELLO_MESSAGE));
	}
	
	@Test
	public void apiHelloShouldRedirectToLoginPage() {
		
		WebDriverItRequest request = this.testMapping(API_HELLO_MAPPING);
		
		// (login page)
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertEquals("Login Page", request.getTitle());
	}
	
	@Test
	public void apiHelloWithWrongCredentialsShouldNotLogInShouldNotLogin() {
		
		WebDriverItRequest request = this.navigateAndLogin(API_HELLO_MAPPING, "foo", "bar");
		
		// (login page with "Bad credentials" message)
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertEquals("Login Page", request.getTitle());
		Assert.assertTrue(StringUtils.containsIgnoreCase(request.getResponseBody(), "Bad credentials"));
	}
	
	@Test
	public void apiHelloWithUserCredentialsShouldAskForApproval() {
		
		this.loginAssertOAuthApprovalPage();
	}
	
	@Test
	public void apiHelloWithUserCredentialsAndAuthorizeShouldReturnApiMessage() {
		
		WebDriverItRequest request = this.loginAssertOAuthApprovalPage();
		
		// Clicks the "Authorize" button
		WebElement authorizeSubmitButton = request.findElement(By.name("authorize"));
		Assert.assertNotNull("'Authorize' button in OAuth Approval page expected", authorizeSubmitButton);
		authorizeSubmitButton.click();
		
		// (asserts the right message)
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertTrue(request.getResponseBody(), StringUtils.containsIgnoreCase(
				request.getResponseBody(), String.format(OAuth2ApiController.HELLO_MESSAGE, USERNAME)));
	}

	/**
	 * Convenience method to properly login and assert the response is the OAuth Approval page
	 * @return WebDriverItRequest at the OAuth Approval page
	 */
	private WebDriverItRequest loginAssertOAuthApprovalPage() {
		
		WebDriverItRequest request = this.navigateAndLogin(API_HELLO_MAPPING, USERNAME, PASSWORD);
		
		// (OAuth approval page)
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertTrue(StringUtils.containsIgnoreCase(request.getResponseBody(), "OAuth Approval"));
		
		return request;
	}
}
