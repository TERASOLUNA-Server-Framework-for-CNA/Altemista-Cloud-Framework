
package org.altemista.cloudfwk.mail.javamail;

/*
 * #%L
 * altemista-cloud mail server conf: JavaMail CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.altemista.cloudfwk.core.mail.MailService;
import org.altemista.cloudfwk.core.mail.model.Mail;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.SmtpServer;

/**
 * Tests the mail server uses the debug addresses if set.
 * @author NTT DATA
 */
public class MailServiceDebugAddressesTest extends AbstractMailServiceTest {
	
	/** Mail server */
	@Autowired
	private MailService mailService;
	
	/**
	 * Tests the injection as the mailService is overwritten in XML
	 */
	@Test
	public void testInjection() {
		
		Assert.assertNotNull(this.mailService);
	}
	
	@Test
	public void testSendToDebugAddresses() {
		
		SmtpServer server = this.startServer();
		try {
			Mail mail = this.newMail();
			this.mailService.send(mail);
			
			// Verifies one and only one message has been sent
			Assert.assertEquals(1, server.getEmailCount());
			MailMessage sentMail = server.getMessage(0);
			Assert.assertNotNull(sentMail);
			
			// Verifies the values
			Assert.assertArrayEquals(new String[]{ mail.getFrom() }, sentMail.getHeaderValues("From"));
			
			// The addresses of the mail object should not have been used
			Assert.assertNotEquals(
					StringUtils.arrayToDelimitedString(mail.getTo(), ", "),
					StringUtils.arrayToCommaDelimitedString(sentMail.getHeaderValues("To")));
			Assert.assertNotEquals(
					StringUtils.arrayToDelimitedString(mail.getCc(), ", "),
					StringUtils.arrayToCommaDelimitedString(sentMail.getHeaderValues("Cc")));

			// The debug addresses should be in the "from" field
			Assert.assertEquals(
					"debug@nttdata.com",
					StringUtils.arrayToCommaDelimitedString(sentMail.getHeaderValues("To")));
			
		} finally {
			server.stop();
		}
	}
}
