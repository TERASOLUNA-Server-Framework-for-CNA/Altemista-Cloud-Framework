package cloud.altemista.fwk.test.integration.jmx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cloud.altemista.fwk.test.AbstractSpringContextTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ 
	"classpath:/spring/cloud-altemistafwk-test-jmx-common.xml"
})
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class JmxAdapterTest extends AbstractSpringContextTest{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	public void testJmxAdapterDemo() throws Exception{
		Thread.sleep(3000);
	}

}
