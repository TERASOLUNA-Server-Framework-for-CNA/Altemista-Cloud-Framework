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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.StringUtils;
import org.altemista.cloudfwk.core.AltemistaCloudfwkApplicationContextInitializer;
import org.altemista.cloudfwk.core.mail.model.Mail;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;
import org.altemista.cloudfwk.test.util.TestConstants;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.ServerOptions;
import com.dumbster.smtp.SmtpServer;
import com.dumbster.smtp.SmtpServerFactory;

/**
 * Provides common methods to test the mail services
 * @author NTT DATA
 */
@ContextConfiguration(initializers = AltemistaCloudfwkApplicationContextInitializer.class)
public abstract class AbstractMailServiceTest extends AbstractSpringContextTest {
	
	@Value("${mail.port}")
	private int port;
	
	/**
	 * Convenience method to start the fake SMTP server in each test
	 * @return SmtpServer
	 */
	protected SmtpServer startServer() {
		
		// Starts the fake SMTP server in the specified port
		ServerOptions options = new ServerOptions();
		options.port = this.port;
		return SmtpServerFactory.startServer(options);
	}	
	
	/**
	 * Convenience method to create a new mail in each test
	 * @return Mail
	 */
	protected Mail newMail() {
		Mail mail = new Mail();
		mail.setFrom("sender@nttdata.com");
		mail.setTo("receiver@nttdata.com");
		mail.setCc("cc@nttdata.com");
		mail.setSubject("subject " + System.currentTimeMillis());
		mail.setText(TestConstants.TEXT);
		return mail;
	}
	
	/**
	 * Convenience method to compare the mail sent in each test
	 * @param mail Mail
	 * @param server SmtpSerevr
	 * @return MailMessage to perform additional asserts if needed
	 */
	protected MailMessage mailAsserts(Mail mail, SmtpServer server) {
		
		// Verifies one and only one message has been sent
		Assert.assertEquals(1, server.getEmailCount());
		MailMessage sentMail = server.getMessage(0);
		Assert.assertNotNull(sentMail);
		
		// Verifies the values
		// (notes: beware of the comparisons of arrays, as SmtpServer returns some fields concatenated.
		// Also, BCC is not compared due limitations of SmtpServer)
		Assert.assertArrayEquals(
				new String[]{ mail.getFrom() },
				sentMail.getHeaderValues("From"));
		Assert.assertEquals(
				StringUtils.arrayToDelimitedString(mail.getTo(), ", "),
				StringUtils.arrayToCommaDelimitedString(sentMail.getHeaderValues("To")));
		Assert.assertEquals(
				StringUtils.arrayToDelimitedString(mail.getCc(), ", "),
				StringUtils.arrayToCommaDelimitedString(sentMail.getHeaderValues("Cc")));
		Assert.assertArrayEquals(
				new String[]{ mail.getSubject() },
				sentMail.getHeaderValues("Subject"));
		
		return sentMail;
	}

}
