package com.mycompany.application.module.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.mycompany.application.module.service.SqsQueueSender;

@Service
public class SqsQueueSenderImpl implements SqsQueueSender {

	private final QueueMessagingTemplate queueMessagingTemplate;

	@Autowired
	public SqsQueueSenderImpl(AmazonSQSAsync amazonSqs) {
		this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSqs);
	}

	public void send(String message) {
		this.queueMessagingTemplate.send("queueMessagingTemplate", MessageBuilder.withPayload(message).build());
	}
}