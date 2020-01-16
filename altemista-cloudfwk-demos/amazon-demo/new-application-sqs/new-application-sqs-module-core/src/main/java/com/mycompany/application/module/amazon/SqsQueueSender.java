package com.mycompany.application.module.amazon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQSAsync;

@Service
public class SqsQueueSender {

	private final QueueMessagingTemplate queueMessagingTemplate;

	@Autowired
	public SqsQueueSender(AmazonSQSAsync amazonSqs) {
		this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSqs);
	}

	public void send(String message) {
		this.queueMessagingTemplate.send("queueMessagingTemplate", MessageBuilder.withPayload(message).build());
	}
}