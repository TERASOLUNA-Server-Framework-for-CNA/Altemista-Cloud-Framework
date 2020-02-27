package com.mycompany.application.module.amqp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntegrationAMQPListenerConfiguration {

	Logger logger = LoggerFactory.getLogger(this.getClass());

//	@Bean
//	public IntegrationAMQPListener getIntegrationAMQPListener() {
//		return new IntegrationAMQPListener();
//	}
//
//	@Bean
//	public SimpleMessageListenerContainer getSimpleMessageListenerContainer(
//			ConnectionFactory connectionFactory,
//			Queue queue,
//			IntegrationAMQPListener integrationAMQPListener) {
//		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//		container.setConnectionFactory(connectionFactory);
//		container.setQueues(queue);
//		container.setMessageListener(integrationAMQPListener);
//		container.setReceiveTimeout(5000);
//		return container;
//	}
//
//	private class IntegrationAMQPListener implements MessageListener {
//		@Override
//		public void onMessage(Message message) {
//			logger.info("Message received: " + new String(message.getBody()));
//		}
//	}
}
