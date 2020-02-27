/**
 * Implementation of both the exposed and the internal services of module.
 * I.e.: {@code @Service} annotated classes
 */
package com.mycompany.application.module.service.impl;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import com.mycompany.application.module.service.ActiveMQService;
import org.springframework.beans.factory.annotation.Value;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.annotation.Resource;
import javax.jms.JMSException;

@Service
public class ActiveMQServiceImpl implements ActiveMQService {

	@Resource(name = "jmsTemplate")
	JmsTemplate jmsTemplate;

	@Value("${jms.queue.name}")
	String queueName;

	@Resource(name = "jmsTemplateProducer")
	JmsTemplate jmsTemplateProducer;


	@Override
	public void sendMessage(String message) {
		jmsTemplate.convertAndSend(message);
	}

	@Override
	public void receiveMessage() {
		Message message = jmsTemplate.receive(queueName);
		TextMessage textMessage = (TextMessage) message;
		try {
			System.out.println("Message received : " + textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void publishMessage(String message) {
		jmsTemplateProducer.convertAndSend(message);
	}
}