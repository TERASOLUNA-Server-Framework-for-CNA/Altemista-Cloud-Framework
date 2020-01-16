
package org.altemista.cloudfwk.mail.javamail;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
import org.springframework.test.context.ContextConfiguration;
import org.altemista.cloudfwk.core.mail.MailService;
import org.altemista.cloudfwk.core.mail.model.ContentReadableAttachment;
import org.altemista.cloudfwk.core.mail.model.Mail;
import org.altemista.cloudfwk.core.mail.model.MailStatus;
import org.altemista.cloudfwk.test.util.TestConstants;

import com.dumbster.smtp.MailMessage;
import com.dumbster.smtp.SmtpServer;

/**
 * Tests the mail server using JavaMail.
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/altemista-cloudfwk-test-mail-javamail.xml")
public class MailServiceTest extends AbstractMailServiceTest {
	
	/** Mail server */
	// tag::mailService[]
	@Autowired
	private MailService mailService;
	// end::mailService[]
	
	/**
	 * Tests the synchronous mail server can be used
	 * without the beans necessary to be able to use the asynchronous service
	 */
	@Test
	public void testInjection() {
		
		Assert.assertNotNull(this.mailService);
	}
	
	@Test
	public void sanityChecks() {
		
		SmtpServer server = this.startServer();
		try {
			this.mailService.send(null);

			// The calls has not failed and no mail has been sent
			Assert.assertEquals(0, server.getEmailCount());
			
		} finally {
			server.stop();
		}
	}
	
	@Test
	public void testSend() {
		
		SmtpServer server = this.startServer();
		try {
			Mail mail = this.newMail();
			this.mailService.send(mail);
			
			MailMessage sentMail = this.mailAsserts(mail, server);
			Assert.assertEquals(mail.getText(), sentMail.getBody());
			
		} finally {
			server.stop();
		}
	}
	
	@Test
	public void testSendToMultipleAddresses() {
		
		
		SmtpServer server = this.startServer();
		try {
			Mail mail = this.newMail();
			mail.setTo(new String[]{ "receiver@nttdata.co.jp", "receiver2@nttdata.co.jp" });
			mail.setCc(new String[]{ "cc@nttdata.co.jp", "cc2@nttdata.co.jp" });
			
			this.mailService.send(mail);
			
			MailMessage sentMail = this.mailAsserts(mail, server);
			Assert.assertEquals(mail.getText(), sentMail.getBody());
			
		} finally {
			server.stop();
		}
	}
	
	@Test
	public void testSendWithAttachment() {
		
		SmtpServer server = this.startServer();
		try {
			Mail mail = this.newMail();
			mail.setAttachments(new ContentReadableAttachment(TestConstants.BINARY_BYTE_ARRAY_CONTENT_READABLE));
			
			this.mailService.send(mail);
			
			// The call has not failed 
			this.mailAsserts(mail, server);
			
		} finally {
			server.stop();
		}
	}
	
	@Test
	public void testSendWithInlineAttachment() {
		
		SmtpServer server = this.startServer();
		try {
			Mail mail = this.newMail();
			ContentReadableAttachment attachment = new ContentReadableAttachment(TestConstants.BINARY_BYTE_ARRAY_CONTENT_READABLE);
			attachment.setInline(true);
			mail.setAttachments(attachment);
			
			this.mailService.send(mail);
			
			// The call has not failed 
			this.mailAsserts(mail, server);
			
		} finally {
			server.stop();
		}
	}
	/**
	 * Tests the send is actually asynchronous
	 */
	@Test
	public void testAsynchronousSendIsAsynchronous() {
		
		SmtpServer server = this.startServer();
		try {
			// (uses a "slow" file as an attachment to ensure the sending of the file takes longer than the timeout)
			Mail mail = this.newMail();
			mail.setAttachments(new ContentReadableAttachment(TestConstants.binarySlowStreamSourceContentReadable()));
			
			Future<MailStatus> future = this.mailService.sendAsync(mail);
			
			// The status is still pending
			Assert.assertFalse(future.isCancelled());
			Assert.assertFalse(future.isDone());

			// No mail has been sent yet
			Assert.assertEquals(0, server.getEmailCount());
			
		} finally {
			server.stop();
		}
	}
	
	/**
	 * Tests the asynchronous send does actually finishes
	 * @throws InterruptedException
	 * @throws ExecutionException 
	 */
	@Test
	public void testAsynchronousSendIsSent() throws InterruptedException, ExecutionException {
		
		final long wait = 250L; // NOSONAR
		final long maxTotalWait = 20000L; // NOSONAR
		
		SmtpServer server = this.startServer();
		try {
			// (uses a "slow" file as an attachment to ensure the sending of the file takes longer than the timeout)
			Mail mail = this.newMail();
			mail.setAttachments(new ContentReadableAttachment(TestConstants.binarySlowStreamSourceContentReadable()));
			
			Future<MailStatus> future = this.mailService.sendAsync(mail);
			
			// The status is still pending
			Assert.assertFalse(future.isCancelled());
			Assert.assertFalse(future.isDone());

			// No mail has been sent yet
			Assert.assertEquals(0, server.getEmailCount());
			
			// Wait enough for the mail with attachment to be sent
			long totalWait = 0L;
			while (!(future.isCancelled()) && (!future.isDone())) {
				Thread.sleep(wait); // NOSONAR
				
				// (10 seconds)
				totalWait += wait;
				if (totalWait > maxTotalWait) { 
					Assert.fail("the mail with attachment was not sent after 10 seconds");
				}
			}
			
			// The mail has been sent
			Assert.assertFalse(future.isCancelled());
			Assert.assertTrue(future.isDone());
			
			MailStatus mailStatus = future.get();
			if (mailStatus.getException() != null) {
				// (propagates the asynchronous exception to provide more information in the test result)
				throw mailStatus.getException();
			}
			Assert.assertTrue(mailStatus.isSent());
			
			// (ensures the fake server has actually registered the mail)
			Thread.sleep(wait); // NOSONAR

			// No has been sent
			Assert.assertNotEquals(0, server.getEmailCount());
			
		} finally {
			server.stop();
		}
	}
	
}
