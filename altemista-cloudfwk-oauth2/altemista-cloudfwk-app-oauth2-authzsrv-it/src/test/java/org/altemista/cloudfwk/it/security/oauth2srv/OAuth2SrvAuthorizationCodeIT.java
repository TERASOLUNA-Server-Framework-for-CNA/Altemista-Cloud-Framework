/**
 * 
 */
package org.altemista.cloudfwk.it.security.oauth2srv;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;
import org.altemista.cloudfwk.core.rest.security.BasicAuthRestTemplate;
import org.altemista.cloudfwk.it.AbstractWebDriverIT;
import org.altemista.cloudfwk.it.WebDriverItRequest;

/**
 * Authorization code grant integration tests
 * @author NTT DATA
 */
public class OAuth2SrvAuthorizationCodeIT extends AbstractWebDriverIT {
	
	/** The right client id */
	private static final String CLIENT_ID = "authclient";

	/** The right secret of the client */
	private static final String CLIENT_SECRET = "authsecret";
	
	/** The right scope of the client */
	private static final String CLIENT_SCOPE = "AUTHSCOPE";
	
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
				"/oauth/authorize?response_type=code&client_id=" + CLIENT_ID,
				"foo", "bar");

		// Asserts has navigated to a page that is the login page again
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertEquals("Login Page", request.getTitle());
		Assert.assertTrue(StringUtils.containsIgnoreCase(request.getResponseBody(), "Bad credentials"));
	}
	
	@Test
	public void withClientCredentialsShouldNotLogIn() {
		
		WebDriverItRequest request = this.navigateAndLogin(
				"/oauth/authorize?response_type=code&client_id=" + CLIENT_ID,
				CLIENT_ID, CLIENT_SECRET);

		// Asserts has navigated to a page that is the login page again
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertEquals("Login Page", request.getTitle());
		Assert.assertTrue(StringUtils.containsIgnoreCase(request.getResponseBody(), "Bad credentials"));
	}
	
	@Test
	public void withUserCredentialsWithoutRedirectUriShould404() {
		
		WebDriverItRequest request = this.navigateAndLogin(
				"/oauth/authorize?response_type=code&client_id=" + CLIENT_ID,
				USERNAME, PASSWORD);
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
		Map<String, String> map = QueryStringUtil.parseAuthorizationCodeRedirect(request.getCurrentUrl());
		Assert.assertEquals("access_denied", map.get("error"));
	}

	@Test
	public void whenUserAuthorizesWithoutScopeShouldGetAuthorizationCode() {

		// Logs in properly and authorize
		this.loginAndAuthorizeAssertAuthorizationCode(null);
	}
	
	@Test
	public void whenUserAuthorizesShouldGetAuthorizationCode() {

		// Logs in properly and authorize
		this.loginAndAuthorizeAssertAuthorizationCode(CLIENT_SCOPE);
	}
	
	@Test
	public void whenExchangeCodeForAccessTokenWithoutScopeShouldGetAccessToken() {

		// Logs in properly, authorize, and exchange code for access token
		String authorizationCode = this.loginAndAuthorizeAssertAuthorizationCode(null);
		ResponseEntity<? extends OAuth2AccessToken> response = this.exchangeCodeForAccessToken(authorizationCode);
		
		Assert.assertNotNull(response);
		Assert.assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
		Assert.assertNotNull(response.getBody());
	}
	
	@Test
	public void whenExchangeCodeForAccessTokenShouldGetAccessToken() {

		// Logs in properly, authorize, and exchange code for access token
		String authorizationCode = this.loginAndAuthorizeAssertAuthorizationCode(CLIENT_SCOPE);
		ResponseEntity<? extends OAuth2AccessToken> response = this.exchangeCodeForAccessToken(authorizationCode);
		
		Assert.assertNotNull(response);
		Assert.assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
		Assert.assertNotNull(response.getBody());
	}
	
	@Test
	public void whenExchangeRefreshTokenTokenShouldGetNewAccessToken() {

		// Logs in properly, authorize, and exchange code for access token
		String authorizationCode = this.loginAndAuthorizeAssertAuthorizationCode(CLIENT_SCOPE);
		ResponseEntity<? extends OAuth2AccessToken> response = this.exchangeCodeForAccessToken(authorizationCode);
		
		Assert.assertNotNull(response);
		Assert.assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
		
		OAuth2AccessToken accessToken = response.getBody();
		Assert.assertNotNull(accessToken);
		Assert.assertNotNull(accessToken.getRefreshToken());
		
		ResponseEntity<? extends OAuth2AccessToken> response2 =
				this.exchangeRefreshTokenForAccessToken(accessToken.getRefreshToken().getValue());
		
		Assert.assertNotNull(response2);
		Assert.assertEquals(org.springframework.http.HttpStatus.OK, response2.getStatusCode());
		
		OAuth2AccessToken accessToken2 = response2.getBody();
		Assert.assertNotNull(accessToken2);
		
		Assert.assertNotEquals(accessToken.getValue(), accessToken2.getValue());
	}
	
	/**
	 * Convenience method to properly login and assert the response is the OAuth Approval page
	 * @param scope optional scope to use
	 * @return WebDriverItRequest at the OAuth Approval page
	 */
	private WebDriverItRequest loginAssertOAuthApprovalPage(String scope) {
		
		StringBuilder url = new StringBuilder();
		url.append("/oauth/authorize?response_type=code");
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
	 * Convenience method to properly login, authorize, and assert there is an authorization code in the response
	 * @param scope optional scope to use
	 * @return String with the authorization code
	 */
	private String loginAndAuthorizeAssertAuthorizationCode(String scope) {
		
		WebDriverItRequest request = this.loginAssertOAuthApprovalPage(scope);
		
		// Clicks the "Authorize" button
		WebElement authorizeSubmitButton = request.findElement(By.name("authorize"));
		Assert.assertNotNull("'Authorize' button in OAuth Approval page expected", authorizeSubmitButton);
		authorizeSubmitButton.click();
		
		// Asserts the redirection with ?code=...
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Map<String, String> map = QueryStringUtil.parseAuthorizationCodeRedirect(request.getCurrentUrl());
		String authorizationCode = map.get("code");
		Assert.assertNotNull("'code' expected", authorizationCode);
		
		return authorizationCode;
	}
	
	/**
	 * Convenience method to exchange an authorization code for an access token
	 * @param authorizationCode String
	 * @return ResponseEntity with the access token parsed
	 */
	private ResponseEntity<? extends OAuth2AccessToken> exchangeCodeForAccessToken(String authorizationCode) {

		// Exchanges the authorization code for the access token
		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=authorization_code"
				+ "&code={code}&client_id={client}&client_secret={secret}"
				+ "&redirect_uri={redirectUri}"));
		
		// (same redirectUri as the authorization_code request)
		final String redirectUri = this.resolver.getUrl(this.resolver.getUri("/index.html"));
		
		RestTemplate authenticated = new BasicAuthRestTemplate(CLIENT_ID, CLIENT_SECRET);
		ResponseEntity<? extends OAuth2AccessToken> response = authenticated.postForEntity(
				url, null, DefaultOAuth2AccessToken.class, authorizationCode, CLIENT_ID, CLIENT_SECRET, redirectUri);
		
		return response;
	}
	
	/**
	 * Convenience method to exchange a refresh token for a new access token
	 * @param refreshToken String
	 * @return ResponseEntity with the new access token parsed
	 */
	private ResponseEntity<? extends OAuth2AccessToken> exchangeRefreshTokenForAccessToken(String refreshToken) {
		
		// Exchanges the refresh token for a new access token
		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=refresh_token"
				+ "&refresh_token={token}&client_id={client}&client_secret={secret}"));
		
		RestTemplate authenticated = new BasicAuthRestTemplate(CLIENT_ID, CLIENT_SECRET);
		ResponseEntity<? extends OAuth2AccessToken> response = authenticated.postForEntity(
				url, null, DefaultOAuth2AccessToken.class, refreshToken, CLIENT_ID, CLIENT_SECRET);
		
		return response;
	}
}
