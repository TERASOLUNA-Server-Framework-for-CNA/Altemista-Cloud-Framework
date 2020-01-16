package org.altemista.cloudfwk.it.integration.ws;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.altemista.cloudfwk.it.AbstractWebDriverIT;
import org.altemista.cloudfwk.it.integration.ws.controller.CustomRestController;

public class IntegrationWebserviceIT extends AbstractWebDriverIT {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String REQUEST_PARAM = "OK!!!";
	
	@Test
	public void testWebservice(){
		final String body = this.testMapping(CustomRestController.MAPPING + "/test/" + REQUEST_PARAM).getResponseBody();
		logger.info(" =======> Body: "+body);
		Assert.assertTrue(body, StringUtils.containsIgnoreCase(body, ""));
		Assert.assertEquals(body, "{\"result\":\"Web Service Response: "+REQUEST_PARAM+"\"}");
	}

}
