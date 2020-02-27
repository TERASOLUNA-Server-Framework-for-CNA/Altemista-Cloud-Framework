/**
 * 
 */
package cloud.altemista.fwk.it.security.jwt;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import cloud.altemista.fwk.core.rest.security.BasicAuthRestTemplate;
import cloud.altemista.fwk.it.AbstractIT;

/**
 * Client credentials grant integration tests
 * @author NTT DATA
 */
public class OAuth2SrvClientCredentialsIT extends AbstractIT {
	
	/** The right client id */
	private static final String CLIENT_ID = "cliclient";

	/** The right secret of the client */
	private static final String CLIENT_SECRET = "clisecret";
	
//	/** The right scope of the client */
//	private static final String CLIENT_SCOPE = "CLISCOPE";

	/** The unauthenticated RestTemplate */
	private static final RestTemplate unauthenticated = new RestTemplate();
	
	/** The authenticated RestTemplate */
	private static final RestTemplate authenticated = new BasicAuthRestTemplate(CLIENT_ID, CLIENT_SECRET);
	
	/** The authenticated RestTemplate with wrong secret (password) */
	private static final RestTemplate wrongPassword = new BasicAuthRestTemplate(CLIENT_ID, "foo");
	
	/** The authenticated RestTemplate with wrong client id (username) and secret (password) */
	private static final RestTemplate wrongClient = new BasicAuthRestTemplate("foo", "bar");
	
	//
	
	/** JUnit Rule to verify both type and message of exceptions */
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	//
	
	@Test
	public void unathenticatedShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));
		
		final String url = this.resolver.getUrl(this.resolver.getUri("/oauth/token?grant_type=client_credentials"));
		unauthenticated.postForEntity(url, null, String.class);
	}
	
	@Test
	public void wrongPasswordShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));

		final String url = this.resolver.getUrl(this.resolver.getUri("/oauth/token?grant_type=client_credentials"));
		wrongPassword.postForEntity(url, null, String.class);
	}
	
	@Test
	public void wrongClientShould401() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));

		final String url = this.resolver.getUrl(this.resolver.getUri("/oauth/token?grant_type=client_credentials"));
		wrongClient.postForEntity(url, null, String.class);
	}

	@Test
	public void authenticatedWithoutGrantShould400() {
		
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("400"));
		
		final String url = this.resolver.getUrl(this.resolver.getUri("/oauth/token"));
		authenticated.postForEntity(url, null, String.class);
	}

	@Test
	public void authenticatedWithoutCredentialsShouldGetAccessToken() {
		
		final String url = this.resolver.getUrl(this.resolver.getUri("/oauth/token?grant_type=client_credentials"));
		ResponseEntity<? extends OAuth2AccessToken> response = authenticated.postForEntity(
				url, null, DefaultOAuth2AccessToken.class);
		
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertNotNull(response.getBody());
	}

	@Test
	public void authenticatedWithCredentialsShouldGetAccessToken() {
		
		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=client_credentials&client_id={client}&client_secret={secret}"));
		ResponseEntity<? extends OAuth2AccessToken> response = authenticated.postForEntity(
				url, null, DefaultOAuth2AccessToken.class, CLIENT_ID, CLIENT_SECRET);
		
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertNotNull(response.getBody());
	}
	
	@Test
	public void authenticatedWithWrongPasswordShouldGetAccessToken() {
		
		// (client ID matches; client_secret is ignored)
		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=client_credentials&client_id={client}&client_secret={secret}"));
		ResponseEntity<? extends OAuth2AccessToken> response = authenticated.postForEntity(
				url, null, DefaultOAuth2AccessToken.class, CLIENT_ID, "bar");
		
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertNotNull(response.getBody());
	}
	
	@Test
	public void authenticatedWithWrongCrendentialsShould401() {
		
		// ("Given client ID does not match authenticated client")
		this.thrown.expect(RestClientException.class);
		this.thrown.expectMessage(CoreMatchers.startsWith("401"));

		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=client_credentials&client_id={client}&client_secret={secret}"));
		authenticated.postForEntity(url, null, String.class, "foo", "bar");
	}
	
	
	@Test
	public void whenTokenShouldBeAnJWT() {

		// Logs in properly, authorize, and exchange code for access token
		final String url = this.resolver.getUrl(this.resolver.getUri(
				"/oauth/token?grant_type=client_credentials&client_id={client}&client_secret={secret}"));
		ResponseEntity<? extends OAuth2AccessToken> response = authenticated.postForEntity(
				url, null, DefaultOAuth2AccessToken.class, CLIENT_ID, CLIENT_SECRET);
		Assert.assertNotNull(response);
		Assert.assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
		Assert.assertNotNull(response.getBody());
		String token = response.getBody().getValue();
		Jwt jToken = JwtHelper.decode(token);
		Assert.assertNotNull(jToken.getClaims());
		
	}
}
