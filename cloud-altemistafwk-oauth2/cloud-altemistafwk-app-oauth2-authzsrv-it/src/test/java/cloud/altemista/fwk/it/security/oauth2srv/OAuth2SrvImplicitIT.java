/**
 * 
 */
package cloud.altemista.fwk.it.security.oauth2srv;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import cloud.altemista.fwk.it.AbstractWebDriverIT;
import cloud.altemista.fwk.it.WebDriverItRequest;

/**
 * Implicit grant integration tests
 * @author NTT DATA
 */
public class OAuth2SrvImplicitIT extends AbstractWebDriverIT {
	
	/** The right client id */
	private static final String CLIENT_ID = "impclient";

	/** The right secret of the client */
	private static final String CLIENT_SECRET = "impsecret";
	
	/** The right scope of the client */
	private static final String CLIENT_SCOPE = "IMPSCOPE";
	
	/** An existing usename */
	private static final String USERNAME = "user";
	
	/** An existing password */
	private static final String PASSWORD = "password";
	
	//
	
	@Test
	public void withoutClientShould404() {
		
		// (ClientRegistrationException error: No client with requested id: null)
		this.testMapping("/oauth/authorize", HttpStatus.SC_NOT_FOUND);
	}
	
	@Test
	public void withWrongClientShould404() {
		
		// (ClientRegistrationException error: No client with requested id: foo)
		this.testMapping("/oauth/authorize?client_id=foo", HttpStatus.SC_NOT_FOUND);
	}
	
	@Test
	public void withoutResponseTypeShould404() {
		
		// (OAuth2 error: error="unsupported_response_type", error_description="Unsupported response types: []")
		this.testMapping("/oauth/authorize?client_id=" + CLIENT_ID, HttpStatus.SC_NOT_FOUND);
	}
	
	@Test
	public void withWrongResponseTypeShould404() {
		
		// (OAuth2 error: error="unsupported_response_type", error_description="Unsupported response types: [password]")
		this.testMapping("/oauth/authorize?response_type=password&client_id=" + CLIENT_ID, HttpStatus.SC_NOT_FOUND);
	}
	
	@Test
	public void withWrongCredentialsShouldNotLogIn() {
		
		WebDriverItRequest request = this.navigateAndLogin(
				"/oauth/authorize?response_type=token&client_id=" + CLIENT_ID, "foo", "bar");

		// Asserts has navigated to a page that is the login page again
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertEquals("Login Page", request.getTitle());
		Assert.assertTrue(StringUtils.containsIgnoreCase(request.getResponseBody(), "Bad credentials"));
	}
	
	@Test
	public void withClientCredentialsShouldNotLogIn() {
		
		WebDriverItRequest request = this.navigateAndLogin(
				"/oauth/authorize?response_type=token&client_id=" + CLIENT_ID, CLIENT_ID, CLIENT_SECRET);

		// Asserts has navigated to a page that is the login page again
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertEquals("Login Page", request.getTitle());
		Assert.assertTrue(StringUtils.containsIgnoreCase(request.getResponseBody(), "Bad credentials"));
	}
	
	@Test
	public void withUserCredentialsWithWrongScopeShouldShowError() {

		// (dummy redirect to verify request parameters only)
		final String redirectUrl = this.resolver.getUrl(this.resolver.getUri("/index.html"));
		
		WebDriverItRequest request = this.navigateAndLogin(
				"/oauth/authorize?response_type=token"
				+ "&client_id=" + CLIENT_ID + "&scope=foo&redirect_uri=" + redirectUrl,
				USERNAME, PASSWORD);

		// Asserts the redirection with ?error=invalid_scope
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Map<String, String> map = QueryStringUtil.parseImplicitRedirect(request.getCurrentUrl());
		Assert.assertEquals("invalid_scope", map.get("error"));
	}
	
	@Test
	public void withUserCredentialsWithoutRedirectUriShould404() {
		
		WebDriverItRequest request = this.navigateAndLogin(
				"/oauth/authorize?response_type=token&client_id=" + CLIENT_ID, USERNAME, PASSWORD);
		Assert.assertEquals(HttpStatus.SC_NOT_FOUND, request.getStatusCode());
	}
	
	@Test
	public void withUserCredentialsShouldAskForApproval() {

		// Logs in properly
		this.loginAssertOAuthApprovalPage(null);
	}
	
	@Test
	public void whenUserDeniesShouldShowError() {

		// Logs in properly
		WebDriverItRequest request = this.loginAssertOAuthApprovalPage(null);
		
		// Clicks the "Deny" button
		WebElement denySubmitButton = request.findElement(By.name("deny"));
		Assert.assertNotNull("'Deny' button in OAuth Approval page expected", denySubmitButton);
		denySubmitButton.click();

		// Asserts the redirection with ?error=access_denied
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Map<String, String> map = QueryStringUtil.parseImplicitRedirect(request.getCurrentUrl());
		Assert.assertEquals("access_denied", map.get("error"));
	}
	
	@Test
	public void whenUserAuthorizesWithoutScopeShouldGetAccessToken() {

		// Logs in properly and authorize
		WebDriverItRequest request = this.loginAndAuthorize(null);

		// Asserts the redirection with ?access_token=...
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Map<String, String> map = QueryStringUtil.parseImplicitRedirect(request.getCurrentUrl());
		Assert.assertNotNull("'access_token' expected", map.get("access_token"));
		Assert.assertEquals("bearer", map.get("token_type"));
		Assert.assertNotNull("'expires_in' expected", map.get("expires_in"));
	}
	
	@Test
	public void whenUserAuthorizesShouldGetAccessToken() {

		// Logs in properly and authorize
		WebDriverItRequest request = this.loginAndAuthorize(CLIENT_SCOPE);

		// Asserts the redirection with ?access_token=...
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Map<String, String> map = QueryStringUtil.parseImplicitRedirect(request.getCurrentUrl());
		Assert.assertNotNull("'access_token' expected", map.get("access_token"));
		Assert.assertEquals("bearer", map.get("token_type"));
		Assert.assertNotNull("'expires_in' expected", map.get("expires_in"));
	}
	
	/**
	 * Convenience method to properly login and assert the response is the OAuth Approval page
	 * @param scope optional scope to use
	 * @return WebDriverItRequest at the OAuth Approval page
	 */
	private WebDriverItRequest loginAssertOAuthApprovalPage(String scope) {
		
		StringBuilder url = new StringBuilder();
		url.append("/oauth/authorize?response_type=token");
		url.append("&client_id=" + CLIENT_ID);
		if (StringUtils.isNotEmpty(scope)) {
			url.append("&scope=" + scope);
		}
		// (dummy redirect to verify request parameters only)
		url.append("&redirect_uri=" + this.resolver.getUrl(this.resolver.getUri("/index.html")));
		
		WebDriverItRequest request = this.navigateAndLogin(url.toString(), USERNAME, PASSWORD);

		// Asserts has navigated to a page that is the OAuth Approval
		Assert.assertEquals("200 OK (OAuth Approval page) expected",
				HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertTrue("OAuth Approval page expected",
				StringUtils.containsIgnoreCase(request.getResponseBody(), "OAuth Approval"));
		
		return request;
	}
	
	/**
	 * Convenience method to properly login and authorize
	 * @param scope optional scope to use
	 * @return WebDriverItRequest after autorhizing at the OAuth Approval page
	 */
	private WebDriverItRequest loginAndAuthorize(String scope) {
		
		WebDriverItRequest request = this.loginAssertOAuthApprovalPage(scope);
		
		// Clicks the "Authorize" button
		WebElement authorizeSubmitButton = request.findElement(By.name("authorize"));
		Assert.assertNotNull("'Authorize' button in OAuth Approval page expected", authorizeSubmitButton);
		authorizeSubmitButton.click();
		
		return request;
	}
}
