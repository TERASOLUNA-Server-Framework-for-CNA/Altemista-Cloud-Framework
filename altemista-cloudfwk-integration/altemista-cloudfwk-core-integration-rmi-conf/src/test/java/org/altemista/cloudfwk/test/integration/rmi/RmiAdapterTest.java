package org.altemista.cloudfwk.test.integration.rmi;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ 
	"classpath:/spring/altemista-cloudfwk-test-rmi-common.xml"
})
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class RmiAdapterTest extends AbstractSpringContextTest{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    MessageChannel requestChannel;
    
    @Autowired
    MessagingTemplate template;
	
	@Test
	public void testRmiAdapterDemo() throws Exception{
		String requestParam = "requestParam";
		String param = template.convertSendAndReceive(requestChannel, requestParam, String.class);
		logger.info(" =======> Param: "+param);
		Assert.assertNotNull(param);
		Assert.assertEquals("Param is " + requestParam, param);
	}

}
