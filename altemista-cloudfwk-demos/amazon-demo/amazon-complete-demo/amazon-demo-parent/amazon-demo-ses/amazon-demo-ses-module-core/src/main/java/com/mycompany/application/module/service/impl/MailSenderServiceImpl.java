package com.mycompany.application.module.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.mycompany.application.module.service.MailSenderService;

@Service
public class MailSenderServiceImpl implements MailSenderService {

	private final MailSender testSender;

	@Autowired
	public MailSenderServiceImpl(MailSender testSender) {
		this.testSender = testSender;
	}

	public void sendMessage(String message) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("alberto.cortes.everis@gmail.com");
		simpleMailMessage.setTo("alberto.cortes.everis@gmail.com");
		simpleMailMessage.setSubject("subject");
		simpleMailMessage.setText(message);

		this.testSender.send(simpleMailMessage);

	}

}
