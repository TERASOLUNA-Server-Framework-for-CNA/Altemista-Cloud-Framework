package com.mycompany.application.module.stream;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class HandlingService {
	private static final String ORIGINAL_QUEUE = "handling.briefProcessingGroup";

	private static final String DLQ = ORIGINAL_QUEUE + ".dlq";

	@RabbitListener(queues = DLQ)
	private void getDLQmessage(Message failedMessage) {
		System.out.println("Repairing message: "+failedMessage.toString());
	}
}