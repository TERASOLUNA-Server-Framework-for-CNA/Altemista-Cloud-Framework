package org.altemista.cloudfwk.test.integration.mail;

import java.util.Date;

import javax.mail.Address;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ 
	"classpath:/spring/altemista-cloudfwk-test-mail-common.xml"
})
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
@TestPropertySource({"classpath:application-test.properties"})
public class MailAdapterTest extends AbstractSpringContextTest{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired(required = false)
	DirectChannel receiveChannel;
	
	@Value("${gmail.username:null}")
	private String gmailUsername;
	
	@Value("${gmail.password:null}")
	private String gmailPassword;

	@Test
	public void testMailAdapterDemo() throws Exception{
		logger.info(" ====> testMailAdapterDemo init");
		
		logger.info(" ====> GMail username: " +gmailUsername);
		logger.info(" ====> GMail password: " +gmailPassword);
		
		if (gmailUsername!=null && gmailPassword!=null && receiveChannel!=null) {
			receiveChannel.subscribe(new MessageHandler() {
				@Override
				public void handleMessage(Message<?> message) throws MessagingException {
					logger.info("Message received!!!");
					try {
						MimeMessage payload = (MimeMessage) message.getPayload();
						String subject = payload.getSubject();
						logger.info(" ==> Message subjet: " + subject);
						Date receivedDate = payload.getReceivedDate();
						logger.info(" ==> Message received date: " + receivedDate);
						Address[] from = payload.getFrom();
						for (Address fromAddress : from) {
							logger.info(" ==> Message from: " + fromAddress.toString());
						}
					} catch (javax.mail.MessagingException e) {
						e.printStackTrace();
					}
					
				}
			});
			Thread.sleep(20000);
		} else {
			logger.info(" ====> GMail username or password or receiveChannel are null!!!");
			logger.info("       Maybe you have to enable testMail in `spring.profiles.active` property? See application-test.properties");
		}
		
		logger.info(" ====> testMailAdapterDemo end");
	}

}
