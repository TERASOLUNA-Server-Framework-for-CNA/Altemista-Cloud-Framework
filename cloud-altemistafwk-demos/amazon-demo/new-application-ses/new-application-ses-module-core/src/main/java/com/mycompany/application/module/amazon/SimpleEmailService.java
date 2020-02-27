/**
 * Amazon Web Services SES Implementation
 */
package com.mycompany.application.module.amazon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class SimpleEmailService {

	private final MailSender testSender;

	@Autowired
	public SimpleEmailService(MailSender testSender) {
		this.testSender = testSender;
	}

	public void sendMessage(String message) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("");
		simpleMailMessage.setTo("");
		simpleMailMessage.setSubject("subject");
		simpleMailMessage.setText(message);

		this.testSender.send(simpleMailMessage);

	}

}
