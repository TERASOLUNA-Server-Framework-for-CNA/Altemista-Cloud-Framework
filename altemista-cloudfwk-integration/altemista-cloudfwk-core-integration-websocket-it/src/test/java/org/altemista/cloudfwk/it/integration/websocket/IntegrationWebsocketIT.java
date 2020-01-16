package org.altemista.cloudfwk.it.integration.websocket;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.altemista.cloudfwk.it.AbstractWebDriverIT;
import org.altemista.cloudfwk.it.integration.websocket.controller.WebsocketAdapterController;

public class IntegrationWebsocketIT extends AbstractWebDriverIT {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void testWebsocket() throws InterruptedException{
		final String body = this.testMapping(WebsocketAdapterController.MAPPING).getResponseBody();
		Assert.assertTrue(body, StringUtils.containsIgnoreCase(body, ""));
		Assert.assertEquals("{\"result\":\"returned message\"}", body);
	}

}
