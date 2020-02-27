package cloud.altemista.fwk.it.integration.websocket;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cloud.altemista.fwk.it.AbstractWebDriverIT;
import cloud.altemista.fwk.it.integration.websocket.controller.WebsocketAdapterController;

public class IntegrationWebsocketIT extends AbstractWebDriverIT {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void testWebsocket() throws InterruptedException{
		final String body = this.testMapping(WebsocketAdapterController.MAPPING).getResponseBody();
		Assert.assertTrue(body, StringUtils.containsIgnoreCase(body, ""));
		Assert.assertEquals("{\"result\":\"returned message\"}", body);
	}

}
