package org.altemista.cloudfwk.test.integration.resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"classpath:/spring/altemista-cloudfwk-test-resource-common.xml"
})
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
@EnableIntegration
public class ResourceTest  extends AbstractSpringContextTest {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void testBinary() throws InterruptedException {
		Thread.sleep(2000);
	}

}
