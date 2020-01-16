package com.mycompany.application.module.amazon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQSAsync;

@Service
public class SqsQueueReciever {

	private final QueueMessagingTemplate queueMessagingTemplate;

	@Autowired
	public SqsQueueReciever(AmazonSQSAsync amazonSqs) {
		this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSqs);
	}

	public String recieve() {
		return this.queueMessagingTemplate.receiveAndConvert("queueMessagingTemplate", String.class);
	}
}