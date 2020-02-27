/**
 * 
 */
package cloud.altemista.fwk.it.security.jwt;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import cloud.altemista.fwk.core.rest.security.BasicAuthRestTemplate;
import cloud.altemista.fwk.it.AbstractWebDriverIT;
import cloud.altemista.fwk.it.WebDriverItRequest;
import cloud.altemista.fwk.it.security.jwt.controller.OAuth2ApiController;

/**
 * Access to the resource server integration tests
 * @author NTT DATA
 */
public class OAuth2ApiIT extends AbstractWebDriverIT {
	
	/** JUnit Rule to verify both type and message of exceptions */
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	//
	
	@Test
	public void unauthenticatedShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));
		
		final RestTemplate unauthenticated = new RestTemplate();
		final String url = this.resolver.getUrl(this.resolver.getUri("/api/hello"));
		
		unauthenticated.getForEntity(url, String.class);
	}
	
	@Test
	public void authenticatedThroughAuthorizationCodeShouldGetMessage() {
		
		OAuth2AccessToken accessToken = this.getAccessTokenUsingAuthorizationCode();
		String message = this.getMessageUsingAccessToken(accessToken);
		Assert.assertEquals(String.format(OAuth2ApiController.HELLO_MESSAGE, "user"), message);
	}
	
	@Test
	public void authenticatedThroughImplicitShouldGetMessage() {
		
		OAuth2AccessToken accessToken = this.getAccessTokenUsingImplicit();
		String message = this.getMessageUsingAccessToken(accessToken);
		Assert.assertEquals(String.format(OAuth2ApiController.HELLO_MESSAGE, "user"), message);
	}
	
	@Test
	public void authenticatedThroughClientCredentialsShouldGetMessage() {
		
		OAuth2AccessToken accessToken = this.getAccessTokenUsingClientCredentials();
		String message = this.getMessageUsingAccessToken(accessToken);
		Assert.assertEquals(String.format(OAuth2ApiController.HELLO_MESSAGE, "cliclient"), message);
	}
	
	@Test
	public void authenticatedThroughPasswordShouldGetMessage() {
		
		OAuth2AccessToken accessToken = this.getAccessTokenUsingPassword();
		String message = this.getMessageUsingAccessToken(accessToken);
		Assert.assertEquals(String.format(OAuth2ApiController.HELLO_MESSAGE, "user"), message);
	}

	//
	
	@Test
	public void authenticatedThroughAuthorizationCodeShouldGetAuthorizationCodeScopeOnly() {
		
		final OAuth2AccessToken accessToken = this.getAccessTokenUsingAuthorizationCode();

		ResponseEntity<String> response = this.invokeApiUsingAccessToken("/api/helloAuthorizationCode", accessToken);
		Assert.assertTrue("Expected 2xx but was: " + response.getStatusCode(), response.getStatusCode().is2xxSuccessful());
		Assert.assertEquals(OAuth2ApiController.AUTHORIZATION_CODE_ONLY_MESSAGE, response.getBody());

		response = this.invokeApiUsingAccessToken("/api/helloImplicit", accessToken);
		Assert.assertTrue("Expected 4xx but was: " + response.getStatusCode(), response.getStatusCode().is4xxClientError());

		response = this.invokeApiUsingAccessToken("/api/helloClientCredentials", accessToken);
		Assert.assertTrue("Expected 4xx but was: " + response.getStatusCode(), response.getStatusCode().is4xxClientError());

		response = this.invokeApiUsingAccessToken("/api/helloPassword", accessToken);
		Assert.assertTrue("Expected 4xx but was: " + response.getStatusCode(), response.getStatusCode().is4xxClientError());
	}
	
	@Test
	public void authenticatedThroughImplicitShouldGetImplicitScopeOnly() {
		
		final OAuth2AccessToken accessToken = this.getAccessTokenUsingImplicit();

		ResponseEntity<String> response = this.invokeApiUsingAccessToken("/api/helloAuthorizationCode", accessToken);
		Assert.assertTrue("Expected 4xx but was: " + response.getStatusCode(), response.getStatusCode().is4xxClientError());

		response = this.invokeApiUsingAccessToken("/api/helloImplicit", accessToken);
		Assert.assertTrue("Expected 2xx but was: " + response.getStatusCode(), response.getStatusCode().is2xxSuccessful());
		Assert.assertEquals(OAuth2ApiController.IMPLICIT_ONLY_MESSAGE, response.getBody());

		response = this.invokeApiUsingAccessToken("/api/helloClientCredentials", accessToken);
		Assert.assertTrue("Expected 4xx but was: " + response.getStatusCode(), response.getStatusCode().is4xxClientError());

		response = this.invokeApiUsingAccessToken("/api/helloPassword", accessToken);
		Assert.assertTrue("Expected 4xx but was: " + response.getStatusCode(), response.getStatusCode().is4xxClientError());
	}
	
	@Test
	public void authenticatedThroughClientCredentialsShouldGetClientCredentialsScopeOnly() {
		
		final OAuth2AccessToken accessToken = this.getAccessTokenUsingClientCredentials();

		ResponseEntity<String> response = this.invokeApiUsingAccessToken("/api/helloAuthorizationCode", accessToken);
		Assert.assertTrue("Expected 4xx but was: " + response.getStatusCode(), response.getStatusCode().is4xxClientError());

		response = this.invokeApiUsingAccessToken("/api/helloImplicit", accessToken);
		Assert.assertTrue("Expected 4xx but was: " + response.getStatusCode(), response.getStatusCode().is4xxClientError());

		response = this.invokeApiUsingAccessToken("/api/helloClientCredentials", accessToken);
		Assert.assertTrue("Expected 2xx but was: " + response.getStatusCode(), response.getStatusCode().is2xxSuccessful());
		Assert.assertEquals(OAuth2ApiController.CLIENT_CREDENETIALS_ONLY_MESSAGE, response.getBody());

		response = this.invokeApiUsingAccessToken("/api/helloPassword", accessToken);
		Assert.assertTrue("Expected 4xx but was: " + response.getStatusCode(), response.getStatusCode().is4xxClientError());
	}
	
	@Test
	public void authenticatedThroughPasswordShouldGetPasswordScopeOnly() {
		
		final OAuth2AccessToken accessToken = this.getAccessTokenUsingPassword();

		ResponseEntity<String> response = this.invokeApiUsingAccessToken("/api/helloAuthorizationCode", accessToken);
		Assert.assertTrue("Expected 4xx but was: " + response.getStatusCode(), response.getStatusCode().is4xxClientError());

		response = this.invokeApiUsingAccessToken("/api/helloImplicit", accessToken);
		Assert.assertTrue("Expected 4xx but was: " + response.getStatusCode(), response.getStatusCode().is4xxClientError());

		response = this.invokeApiUsingAccessToken("/api/helloClientCredentials", accessToken);
		Assert.assertTrue("Expected 4xx but was: " + response.getStatusCode(), response.getStatusCode().is4xxClientError());

		response = this.invokeApiUsingAccessToken("/api/helloPassword", accessToken);
		Assert.assertTrue("Expected 2xx but was: " + response.getStatusCode(), response.getStatusCode().is2xxSuccessful());
		Assert.assertEquals(OAuth2ApiController.PASSWORD_ONLY_MESSAGE, response.getBody());
	}

	//
	
	/**
	 * Convenience method to retrieve the access token using authorization code grant flow
	 * @return OAuth2AccessToken
	 */
	private OAuth2AccessToken getAccessTokenUsingAuthorizationCode() {
		
		// (dummy redirect to verify request parameters only)
		final String redirectUri = this.resolver.getUrl(this.resolver.getUri("/index.html"));
		
		WebDriverItRequest request = this.navigateAndLogin(String.format(
				"/oauth/authorize?response_type=code"
				+ "&client_id=%s&scope=%s&redirect_uri=%s", "authclient", "AUTHSCOPE", redirectUri),
				"user", "password");

		// Asserts has navigated to a page that is the OAuth Approval
		Assert.assertEquals("200 OK (OAuth Approval page) expected",
				HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertTrue("OAuth Approval page expected",
				StringUtils.containsIgnoreCase(request.getResponseBody(), "OAuth Approval"));
		
		// Clicks the "Authorize" button
		WebElement authorizeSubmitButton = request.findElement(By.name("authorize"));
		Assert.assertNotNull("'Authorize' button in OAuth Approval page expected", authorizeSubmitButton);
		authorizeSubmitButton.click();
		
		// Asserts the redirection with ?code=...
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Map<String, String> map = QueryStringUtil.parseAuthorizationCodeRedirect(request.getCurrentUrl());
		String authorizationCode = map.get("code");
		Assert.assertNotNull("'code' expected", authorizationCode);
		
		// Exchanges the authorization code for the access token
		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=authorization_code"
				+ "&code={code}&client_id={client}&client_secret={secret}"
				+ "&redirect_uri={redirectUri}"));
		
		RestTemplate authenticated = new BasicAuthRestTemplate("authclient", "authsecret");
		ResponseEntity<?> response = null;
		try {
			response = authenticated.postForEntity(
					url, null, DefaultOAuth2AccessToken.class,
					authorizationCode, "authclient", "authsecret", redirectUri);
			
		} catch (HttpClientErrorException e) {
			// Transforms the HttpClientErrorException into a ResponseEntity
			response = new ResponseEntity<String>(
					e.getResponseBodyAsString(), e.getResponseHeaders(), e.getStatusCode());
		}
		
		Assert.assertNotNull(response);
		Assert.assertTrue("Expected 2xx but was: " + response.getStatusCode(),
				response.getStatusCode().is2xxSuccessful());
		final Object responseBody = response.getBody();
		Assert.assertNotNull(responseBody);
		Assert.assertTrue("Expected OAuth2AccessToken but was: " + responseBody.getClass(),
				responseBody instanceof OAuth2AccessToken);
		
		return (OAuth2AccessToken) responseBody;
	}
	
	/**
	 * Convenience method to retrieve the access token using implicit grant flow
	 * @return OAuth2AccessToken
	 */
	private OAuth2AccessToken getAccessTokenUsingImplicit() {
		
		// (dummy redirect to verify request parameters only)
		final String redirectUri = this.resolver.getUrl(this.resolver.getUri("/index.html"));
		
		WebDriverItRequest request = this.navigateAndLogin(String.format(
				"/oauth/authorize?response_type=token&client_id=%s&redirect_uri=%s", "impclient", redirectUri),
				"user", "password");

		// Asserts has navigated to a page that is the OAuth Approval
		Assert.assertEquals("200 OK (OAuth Approval page) expected",
				HttpStatus.SC_OK, request.getStatusCode());
		Assert.assertTrue("OAuth Approval page expected",
				StringUtils.containsIgnoreCase(request.getResponseBody(), "OAuth Approval"));
		
		// Clicks the "Authorize" button
		WebElement authorizeSubmitButton = request.findElement(By.name("authorize"));
		Assert.assertNotNull("'Authorize' button in OAuth Approval page expected", authorizeSubmitButton);
		authorizeSubmitButton.click();
		
		// Asserts the redirection with ?access_token=...
		Assert.assertEquals(HttpStatus.SC_OK, request.getStatusCode());
		Map<String, String> map = QueryStringUtil.parseImplicitRedirect(request.getCurrentUrl());
		Assert.assertNotNull("'access_token' expected", map.get("access_token"));
		Assert.assertEquals("bearer", map.get("token_type"));
		Assert.assertNotNull("'expires_in' expected", map.get("expires_in"));
		
		return DefaultOAuth2AccessToken.valueOf(map);
	}

	/**
	 * Convenience method to retrieve the access token using client credentials grant flow
	 * @return OAuth2AccessToken
	 */
	private OAuth2AccessToken getAccessTokenUsingClientCredentials() {
		
		final RestTemplate clientCredentialsRestTemplate = new BasicAuthRestTemplate("cliclient", "clisecret");
		final String url = this.resolver.getUrl(this.resolver.getUri("/oauth/token?grant_type=client_credentials"));
		
		ResponseEntity<?> response = null;
		try {
			response = clientCredentialsRestTemplate.postForEntity(url, null, DefaultOAuth2AccessToken.class);
			
		} catch (HttpClientErrorException e) {
			// Transforms the HttpClientErrorException into a ResponseEntity
			response = new ResponseEntity<String>(
					e.getResponseBodyAsString(), e.getResponseHeaders(), e.getStatusCode());
		}
		
		Assert.assertNotNull(response);
		Assert.assertTrue("Expected 2xx but was: " + response.getStatusCode(),
				response.getStatusCode().is2xxSuccessful());
		final Object responseBody = response.getBody();
		Assert.assertNotNull(responseBody);
		Assert.assertTrue("Expected OAuth2AccessToken but was: " + responseBody.getClass(),
				responseBody instanceof OAuth2AccessToken);
		
		OAuth2AccessToken ret = (OAuth2AccessToken) responseBody;
		
		//check is JWT
		String token = ret.getValue();
		Jwt jToken = JwtHelper.decode(token);
		Assert.assertNotNull(jToken.getClaims());
		
		return (OAuth2AccessToken) ret;
	}

	/**
	 * Convenience method to retrieve the access token using resource owner password grant flow
	 * @return OAuth2AccessToken
	 */
	private OAuth2AccessToken getAccessTokenUsingPassword() {
		
		final RestTemplate passwordRestTemplate = new BasicAuthRestTemplate("pwdclient", "pwdsecret");
		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=password&username=user&password=password"));
		
		ResponseEntity<?> response = null;
		try {
			response = passwordRestTemplate.postForEntity(url, null, DefaultOAuth2AccessToken.class);
			
		} catch (HttpClientErrorException e) {
			// Transforms the HttpClientErrorException into a ResponseEntity
			response = new ResponseEntity<String>(
					e.getResponseBodyAsString(), e.getResponseHeaders(), e.getStatusCode());
		}
		
		Assert.assertNotNull(response);
		Assert.assertTrue("Expected 2xx but was: " + response.getStatusCode(),
				response.getStatusCode().is2xxSuccessful());
		final Object responseBody = response.getBody();
		Assert.assertNotNull(responseBody);
		Assert.assertTrue("Expected OAuth2AccessToken but was: " + responseBody.getClass(),
				responseBody instanceof OAuth2AccessToken);
		
		return (OAuth2AccessToken) responseBody;
	}

	/**
	 * Convenience method to invoke the secured API using an access token
	 * @param mapping the internal mapping to navigate to
	 * @param accessToken OAuth2AccessToken to use
	 * @return response ResponseEntity, even if an HTTP 4xx is received
	 */
	private ResponseEntity<String> invokeApiUsingAccessToken(String mapping, OAuth2AccessToken accessToken) {
		
		Assert.assertNotNull(accessToken);
		
		final RestTemplate unauthenticated = new RestTemplate();
		final String url = this.resolver.getUrl(this.resolver.getUri(mapping));
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.AUTHORIZATION,
				String.format("%s %s", accessToken.getTokenType(), accessToken.getValue()));
		final HttpEntity<?> httpEntity = new HttpEntity<Object>(httpHeaders);
		
		try {
			ResponseEntity<String> response = unauthenticated.exchange(url, HttpMethod.GET, httpEntity, String.class);
			Assert.assertNotNull(response);
			return response;
			
		} catch (HttpClientErrorException e) {
			// Transforms the HttpClientErrorException into a ResponseEntity
			return new ResponseEntity<String>(e.getResponseBodyAsString(), e.getResponseHeaders(), e.getStatusCode());
		}
	}

	/**
	 * Convenience method to invoke the default mapping of the secured API using an access token
	 * and returning the message
	 * @param mapping the internal mapping to navigate to
	 * @param accessToken OAuth2AccessToken to use
	 * @return response message from the body of the response
	 */
	private String getMessageUsingAccessToken(OAuth2AccessToken accessToken) {
		
		ResponseEntity<String> response = this.invokeApiUsingAccessToken("/api/hello", accessToken);
		Assert.assertTrue("Expected 2xx but was: " + response.getStatusCode(),
				response.getStatusCode().is2xxSuccessful());
		return response.getBody();
	}
}
