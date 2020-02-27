
package cloud.altemista.fwk.mail.javamail.impl;

import java.util.concurrent.Future;

/*
 * #%L
 * altemista-cloud mail server: JavaMail implementation
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import cloud.altemista.fwk.core.mail.MailService;
import cloud.altemista.fwk.core.mail.exception.MailServerException;
import cloud.altemista.fwk.core.mail.model.Attachment;
import cloud.altemista.fwk.core.mail.model.Mail;
import cloud.altemista.fwk.core.mail.model.MailStatus;
import cloud.altemista.fwk.mail.javamail.model.JavaMailAttachment;

/**
 * Implementation of a mail server using JavaMail.
 * @author NTT DATA
 */
public class MailServiceImpl implements MailService {
	
	/** The SLF4J logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);
	
	/** Spring MailSender interface for JavaMail. */
	@Autowired
	private JavaMailSender javaMailSender;
	
	/** The debug addresses. If this field is set, this addresses will be used. */
	private String[] debugAddresses;
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.mail.MailService#send(cloud.altemista.fwk.core.mail.model.Mail)
	 */
	@Override
	public void send(Mail mail) {
		
		// (sanity checks)
		if (mail == null) {
			return;
		}
		
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			
			// If it has attachments, the MimeMessage must have multipart mode
			final int multipartMode = ArrayUtils.isEmpty(mail.getAttachments())
					? MimeMessageHelper.MULTIPART_MODE_NO
					: MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

			// Basic message creation
			MimeMessageHelper helper = new MimeMessageHelper(message, multipartMode);
			helper.setValidateAddresses(true);
			helper.setFrom(mail.getFrom());
			helper.setSubject(StringUtils.trimToEmpty(mail.getSubject()));
			helper.setText(StringUtils.trimToEmpty(mail.getText()), mail.isHtml());

			// Addresses
			this.setAddresses(helper, mail);
			
			// Attachments
			this.setAttachments(helper, mail);

			javaMailSender.send(message);

		} catch (AddressException e) {
			throw new MailServerException("wrong_address", e);
			
		} catch (MessagingException e) {
			throw new MailServerException("creation_error", e);
			
		} catch (MailException e) {
			throw new MailServerException("send_error", e);
			
		} catch (Exception e) {
			throw new MailServerException("unknown_error", e);
		} 
	}

	/**
	 * Sets the addresses in the MimeMessageHelper
	 * @param helper MimeMessageHelper
	 * @param mail Mail
	 * @throws MessagingException MessagingException
	 */
	private void setAddresses(MimeMessageHelper helper, Mail mail) throws MessagingException {
		
		if (!ArrayUtils.isEmpty(debugAddresses)) {
			// Debug configuration support: use only the debug addresses
			helper.setTo(this.debugAddresses);
			return;
		}
		
		if (mail.getTo() != null) {
			helper.setTo(mail.getTo());
		}
		if (mail.getCc() != null) {
			helper.setCc(mail.getCc());
		}
		if (mail.getBcc() != null) {
			helper.setBcc(mail.getBcc());
		}
	}

	/**
	 * Sets the attachments in the MimeMessageHelper
	 * @param helper MimeMessageHelper
	 * @param mail Mail
	 * @throws MessagingException MessagingException
	 */
	private void setAttachments(MimeMessageHelper helper, Mail mail) throws MessagingException {

		if (ArrayUtils.isEmpty(mail.getAttachments())) {
			// No attachments
			return;
		}
		
		for (Attachment attachment : mail.getAttachments()) {
			if (attachment.isInline()) {
				helper.addInline(attachment.getPath(), new JavaMailAttachment(attachment));
			} else {
				helper.addAttachment(attachment.getPath(), new JavaMailAttachment(attachment));
			}
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.mail.MailService#sendAsync(cloud.altemista.fwk.core.mail.model.Mail)
	 */
	@Async
	@Override
	public Future<MailStatus> sendAsync(Mail mail) {

		try {
			// Delegates in the actual mail server
			this.send(mail);
			return new AsyncResult<MailStatus>(new MailStatus());
			
		} catch (MailServerException e) {
			LOGGER.warn("Mail service asynchronous send error: ", e);
			return new AsyncResult<MailStatus>(new MailStatus(e));
		}
	}

	/**
	 * @param javaMailSender the javaMailSender to set
	 */
	public void setJavaMailSender(JavaMailSender javaMailSender) {
	
		this.javaMailSender = javaMailSender;
	}
	
	/**
	 * @param debugAddresses the debugAddresses to set
	 */
	public void setDebugAddresses(String ... debugAddresses) {
		
		this.debugAddresses = debugAddresses;
	}
}
