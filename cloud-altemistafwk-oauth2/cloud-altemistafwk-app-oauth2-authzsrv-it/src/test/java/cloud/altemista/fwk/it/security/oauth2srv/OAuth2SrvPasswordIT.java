/**
 * 
 */
package cloud.altemista.fwk.it.security.oauth2srv;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import cloud.altemista.fwk.core.rest.security.BasicAuthRestTemplate;
import cloud.altemista.fwk.it.AbstractIT;

/**
 * Resource owner password grant integration tests
 * @author NTT DATA
 */
public class OAuth2SrvPasswordIT extends AbstractIT {
	
	/** The right client id */
	private static final String CLIENT_ID = "pwdclient";

	/** The right secret of the client */
	private static final String CLIENT_SECRET = "pwdsecret";

	/** The unauthenticated RestTemplate */
	private static final RestTemplate unauthenticated = new RestTemplate();
	
	/** The authenticated RestTemplate */
	private static final RestTemplate authenticated = new BasicAuthRestTemplate(CLIENT_ID, CLIENT_SECRET);
	
	/** The authenticated RestTemplate with wrong secret (password) */
	private static final RestTemplate wrongPassword = new BasicAuthRestTemplate(CLIENT_ID, "foo");
	
	/** The authenticated RestTemplate with wrong client id (username) and secret (password) */
	private static final RestTemplate wrongClient = new BasicAuthRestTemplate("foo", "bar");
	
	/** An existing usename */
	private static final String USERNAME = "user";
	
	/** An existing password */
	private static final String PASSWORD = "password";
	
	//
	
	/** JUnit Rule to verify both type and message of exceptions */
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	//
	
	@Test
	public void unathenticatedWithoutGrantShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));
		
		final String url = this.resolver.getUrl(this.resolver.getUri("/oauth/token"));
		unauthenticated.postForEntity(url, null, String.class);
	}
	
	@Test
	public void unathenticatedWithoutCredentialsShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));
		
		final String url = this.resolver.getUrl(this.resolver.getUri("/oauth/token?grant_type=password"));
		unauthenticated.postForEntity(url, null, String.class);
	}
	
	@Test
	public void unathenticatedShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));
		
		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=password&username={client}&password={secret}"));
		unauthenticated.postForEntity(url, null, String.class, CLIENT_ID, CLIENT_SECRET);
	}

	@Test
	public void authenticatedWithoutGrantShould400() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("400"));
		
		final String url = this.resolver.getUrl(this.resolver.getUri("/oauth/token"));
		authenticated.postForEntity(url, null, String.class);
	}

	@Test
	public void authenticatedWithoutCredentialsShould400() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("400"));
		
		final String url = this.resolver.getUrl(this.resolver.getUri("/oauth/token?grant_type=password"));
		authenticated.postForEntity(url, null, String.class);
	}
	
	@Test
	public void authenticatedShouldGetAccessToken() {

		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=password&username={username}&password={password}"));
		ResponseEntity<? extends OAuth2AccessToken> response = authenticated.postForEntity(
				url, null, DefaultOAuth2AccessToken.class, USERNAME, PASSWORD);
		
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertNotNull(response.getBody());
	}
	
	@Test
	public void whenExchangeRefreshTokenTokenShouldGetNewAccessToken() {

		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=password&username={username}&password={password}"));
		ResponseEntity<? extends OAuth2AccessToken> response = authenticated.postForEntity(
				url, null, DefaultOAuth2AccessToken.class, USERNAME, PASSWORD);
		
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		
		OAuth2AccessToken accessToken = response.getBody();
		Assert.assertNotNull(accessToken);
		Assert.assertNotNull(accessToken.getRefreshToken());
		
		// Exchanges the refresh token for a new access token
		final String url2 = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=refresh_token&refresh_token={token}"));
		
		ResponseEntity<? extends OAuth2AccessToken> response2 = authenticated.postForEntity(
				url2, null, DefaultOAuth2AccessToken.class,
				accessToken.getRefreshToken().getValue(), CLIENT_ID, CLIENT_SECRET);
		
		Assert.assertNotNull(response2);
		Assert.assertEquals(org.springframework.http.HttpStatus.OK, response2.getStatusCode());
		
		OAuth2AccessToken accessToken2 = response2.getBody();
		Assert.assertNotNull(accessToken2);
		
		Assert.assertNotEquals(accessToken.getValue(), accessToken2.getValue());
	}
	
	@Test
	public void authenticatedButWrongPasswordShould400() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("400"));

		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=password&username={username}&password={password}"));
		authenticated.postForEntity(url, null, DefaultOAuth2AccessToken.class, USERNAME, "foo");
	}
	
	@Test
	public void authenticatedButWrongUsernameShould400() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("400"));

		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=password&username={username}&password={password}"));
		authenticated.postForEntity(url, null, DefaultOAuth2AccessToken.class, "foo", "bar");
	}
	
	@Test
	public void wrongPasswordWithoutGrantShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));
		
		final String url = this.resolver.getUrl(this.resolver.getUri("/oauth/token"));
		wrongPassword.postForEntity(url, null, String.class);
	}
	
	@Test
	public void wrongPasswordWithoutCredentialsShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));
		
		final String url = this.resolver.getUrl(this.resolver.getUri("/oauth/token?grant_type=password"));
		wrongPassword.postForEntity(url, null, String.class);
	}
	
	@Test
	public void wrongPasswordShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));

		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=password&username={client}&password={secret}"));
		wrongPassword.postForEntity(url, null, DefaultOAuth2AccessToken.class, CLIENT_ID, "foo");
	}
	
	@Test
	public void wrongPasswordRightCredentialsShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));

		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=password&username={client}&password={secret}"));
		wrongPassword.postForEntity(url, null, DefaultOAuth2AccessToken.class, CLIENT_ID, CLIENT_SECRET);
	}
	
	@Test
	public void wrongClientWithoutGrantShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));
		
		final String url = this.resolver.getUrl(this.resolver.getUri("/oauth/token"));
		wrongClient.postForEntity(url, null, String.class);
	}
	
	@Test
	public void wrongClientWithoutCredentialsShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));
		
		final String url = this.resolver.getUrl(this.resolver.getUri("/oauth/token?grant_type=password"));
		wrongClient.postForEntity(url, null, String.class);
	}
	
	@Test
	public void wrongClientShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));

		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=password&username={client}&password={secret}"));
		wrongClient.postForEntity(url, null, DefaultOAuth2AccessToken.class, "foo", "bar");
	}
	
	@Test
	public void wrongClientRightCredentialsShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));

		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=password&username={client}&password={secret}"));
		wrongClient.postForEntity(url, null, DefaultOAuth2AccessToken.class, CLIENT_ID, CLIENT_SECRET);
	}
}
