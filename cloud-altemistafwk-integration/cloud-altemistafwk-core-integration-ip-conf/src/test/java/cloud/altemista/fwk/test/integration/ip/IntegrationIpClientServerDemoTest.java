package cloud.altemista.fwk.test.integration.ip;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cloud.altemista.fwk.test.AbstractSpringContextTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"classpath:/spring/cloud-altemistafwk-test-ip-common.xml"
})
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class IntegrationIpClientServerDemoTest extends AbstractSpringContextTest {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IntegrationIPSimpleGateway gateway;

	@Test
	public void testHappyDay() {
		String result = gateway.send("Hello world!");
		logger.info(" ===> testHappyDay result: " + result);
		assertEquals("echo:Hello world!", result);
	}

	@Test
	public void testZeroLength() {
		String result = gateway.send("");
		logger.info(" ===> testZeroLength result: " + result);
		assertEquals("echo:", result);
	}

	@Test
	public void testFail() {
		String result = gateway.send("FAIL");
		logger.info(" ===> testFail result: " + result);
		assertEquals("FAIL:Failure Demonstration", result);
	}

}
