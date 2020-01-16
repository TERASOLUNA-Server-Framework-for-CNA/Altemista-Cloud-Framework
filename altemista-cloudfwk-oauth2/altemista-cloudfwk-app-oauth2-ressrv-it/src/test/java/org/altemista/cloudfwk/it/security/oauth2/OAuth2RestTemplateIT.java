package org.altemista.cloudfwk.it.security.oauth2;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestOperations;
import org.altemista.cloudfwk.it.AbstractIT;
import org.altemista.cloudfwk.it.security.oauth2.controller.OAuth2ApiController;

/**
 * Access to the resource server using Spring {@link OAuth2RestTemplate}
 * @author NTT DATA
 */
@ContextConfiguration(locations = "classpath:spring/it-junit-oauth2resttemplates.xml")
public class OAuth2RestTemplateIT extends AbstractIT {

	@Autowired
	@Qualifier("clientCredentialsRestTemplate")
	private RestOperations restTemplate;

	@Test
	public void authenticatedThroughClientCredentialsShouldGetMessage() {
		
		final String url = this.resolver.getUrl(this.resolver.getUri("/api/hello"));
		
		final ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);
		Assert.assertTrue("Expected 2xx but was: " + response.getStatusCode(),
				response.getStatusCode().is2xxSuccessful());
		final String message = response.getBody();
		Assert.assertNotNull(message);
		Assert.assertEquals(String.format(OAuth2ApiController.HELLO_MESSAGE, "cliclient"), message);
	}
}
