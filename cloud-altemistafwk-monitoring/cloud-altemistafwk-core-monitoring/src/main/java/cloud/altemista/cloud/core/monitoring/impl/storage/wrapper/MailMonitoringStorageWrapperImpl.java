
package cloud.altemista.fwk.core.monitoring.impl.storage.wrapper;

import java.text.DateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * #%L
 * altemista-cloud monitoring
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import cloud.altemista.fwk.common.monitoring.exception.MonitoringException;
import cloud.altemista.fwk.common.monitoring.model.MonitoringInfo;
import cloud.altemista.fwk.common.monitoring.model.Status;

/**
 * Wrapper for monitorable resources that sends an e-mail with the result if it is an error or a warning.
 * @author NTT DATA
 */
public class MailMonitoringStorageWrapperImpl extends AbstractMonitoringStorageWrapperImpl implements InitializingBean {

	/** The SLF4J logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(MailMonitoringStorageWrapperImpl.class);

	/** The mail sender to send the mails. */
	private MailSender mailSender;

	/** The template to create the mails to be sent. */
	private SimpleMailMessage template;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		
		if (this.mailSender == null) {
			throw new MonitoringException("invalid_storage", new Object[] { "mailSender" });
		}
		if (this.template == null) {
			throw new MonitoringException("invalid_storage", new Object[] { "template" });
		}
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.monitoring.impl.storage.wrapper.AbstractMonitoringStorageWrapperImpl#add(cloud.altemista.fwk.common.monitoring.model.MonitoringInfo)
	 */
	@Override
	public void add(MonitoringInfo info) {
		super.add(info);
		
		if (info.getStatus().equals(Status.FAILED)) {
			this.sendMail(info.getIndicatorId(), String.format("ERROR: %s", info.getMessage()));
		}
	}
	
	/**
	 * Sends an e-mail with the result
	 * @param indicatorName id of the identifier
	 * @param msg message to be sent
	 */
	protected void sendMail(String indicatorName, String msg) {
		
		// Prepares the mail to be sent
		SimpleMailMessage mail = new SimpleMailMessage(this.template);
		this.prepareMail(mail, indicatorName, msg);
		
		// Sends the mail
		try {
			this.mailSender.send(mail);
			
		} catch (MailException e) {
			LOGGER.warn(String.format("Could not send mail notification on error in %s: %s", indicatorName, e));
		}
	}
	

	/**
	 * Prepares (usually setting the subject and the text) the mail to be sent.
	 * The default implementation uses the subject <code>[ACF monitoring module] - [&lt;component name&gt;]</code>
	 * and the body of the mail is <code>Exectued at &lt;time of execution&gt;</code>
	 * followed by the text passed as msg attribute
	 * @param mail to be sent
	 * @param indicatorName identifier of the indicator
	 * @param msg message to be added to the body 
	 * @return
	 */
	protected SimpleMailMessage prepareMail(SimpleMailMessage mail, String indicatorName, String result) {
		
		mail.setSubject(String.format("[ACF Monitoring Module] - [%s]", indicatorName));
		mail.setText(String.format("Executed at %s%n%s", DateFormat.getInstance().format(new Date()), result));
		return mail;
	}

	/**
	 * @param mailSender the mailSender to set
	 */
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(SimpleMailMessage template) {
		this.template = template;
	}
}
