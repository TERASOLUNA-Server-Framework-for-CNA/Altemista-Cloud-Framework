package com.mycompany.application.module.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mycompany.application.module.service.IntegrationAMQPService;

@Service
public class IntegrationAMQPServiceImpl implements IntegrationAMQPService {

	@Value("${spring.integration.amqp.rabbitmq.queue}") 
	private String queue;

	@Value("${spring.integration.amqp.rabbitmq.topic-exchange}") 
	private String exchange;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Override
	public void sendMessage(String message) {
		rabbitTemplate.convertAndSend(exchange, queue, message);
	}

}
