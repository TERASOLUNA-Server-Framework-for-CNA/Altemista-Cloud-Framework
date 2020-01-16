package com.mycompany.application.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.MessageHandlerOptions;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.TopicClient;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

@RestController
public class Controller2 {

	@Autowired
	private QueueClient queueClient;
	@Autowired
	private TopicClient topicClient;
	@Autowired
	private SubscriptionClient subscriptionClient;

	@GetMapping(value = "/sendQueue")
	public String sendQueueMessag() throws IOException, ServiceBusException, InterruptedException {
		final String messageBody = "queue message";
		System.out.println("Sending message: " + messageBody);
		final Message message = new Message(messageBody.getBytes(StandardCharsets.UTF_8));
		queueClient.send(message);
		return "correct send";
	}

	@GetMapping(value = "/receiveQueue")
	public String receiveQueueMessag() throws IOException, ServiceBusException, InterruptedException {
		queueClient.registerMessageHandler(new MessageHandler(), new MessageHandlerOptions());
		TimeUnit.SECONDS.sleep(5);
		queueClient.close();
		return "correct receive";
	}

	@GetMapping(value = "/sendTopic")
	public String sendTopicMessage() throws IOException, ServiceBusException, InterruptedException {
		final String messageBody = "topic message";
		System.out.println("Sending message: " + messageBody);
		final Message message = new Message(messageBody.getBytes(StandardCharsets.UTF_8));
		topicClient.send(message);
		topicClient.close();
		return "correct send";
	}

	@GetMapping(value = "/receiveSubscription")
	public String receiveSubscriptionMessage() throws IOException, ServiceBusException, InterruptedException {
		subscriptionClient.registerMessageHandler(new MessageHandler(), new MessageHandlerOptions());
		TimeUnit.SECONDS.sleep(5);
		subscriptionClient.close();
		return "correct receive";
	}

	static class MessageHandler implements IMessageHandler {
		public CompletableFuture<Void> onMessageAsync(IMessage message) {
			final String messageString = new String(message.getBody(), StandardCharsets.UTF_8);
			System.out.println("Received message: " + messageString);
			return CompletableFuture.completedFuture(null);
		}

		public void notifyException(Throwable exception, ExceptionPhase phase) {
			System.out.println(phase + " encountered exception:" + exception.getMessage());
		}
	}
}
