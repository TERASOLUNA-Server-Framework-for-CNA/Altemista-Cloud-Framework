package cloud.altemista.fwk.it.integration.mqtt;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cloud.altemista.fwk.it.AbstractWebDriverIT;
import cloud.altemista.fwk.it.integration.mqtt.controller.CustomRestController;

public class IntegrationMQTTIT extends AbstractWebDriverIT {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void testHttp(){
		final String body = this.testMapping(CustomRestController.MAPPING + "/test").getResponseBody();
		logger.info(" =======> Body: "+body);
		Assert.assertTrue(body, StringUtils.containsIgnoreCase(body, ""));
		Assert.assertEquals(body, "{\"result\":\"OK!\"}");
	}

}
