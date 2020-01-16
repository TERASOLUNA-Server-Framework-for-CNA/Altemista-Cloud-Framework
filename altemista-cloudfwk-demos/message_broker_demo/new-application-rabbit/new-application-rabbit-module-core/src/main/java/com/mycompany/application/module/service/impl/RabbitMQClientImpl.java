/**
 * Implementation of both the exposed and the internal services of module.
 * I.e.: {@code @Service} annotated classes
 */
package com.mycompany.application.module.service.impl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.mycompany.application.module.service.RabbitMQClient;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Service
public class RabbitMQClientImpl implements RabbitMQClient {

	@Value("${rabbitmq.queue.name}")
	String queueName;
	@Value("${rabbitmq.host}")
	String host;
	@Value("${rabbitmq.port}")
	Integer port;
	@Value("${rabbitmq.user}")
	String user;
	@Value("${rabbitmq.pass}")
	String pass;
	@Value("${rabbitmq.exchange.name}")
	String exchangeName;

	public Connection createConnection() {

		ConnectionFactory factory = new ConnectionFactory();

		factory.setHost(host);
		factory.setPort(port);
		factory.setUsername(user);
		factory.setPassword(pass);
		Connection connection = null;
		try {
			connection = factory.newConnection();
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	@Override
	public void sendMessage(String msg) {
		Connection connection = this.createConnection();
		Channel channel = null;
		try {
			channel = connection.createChannel();
			channel.queueDeclare(queueName, false, false, false, null);
			channel.basicPublish("", queueName, null, msg.getBytes("UTF-8"));
			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void receiveMessage() {
		Connection connection = this.createConnection();
		Channel channel = null;
		try {
			channel = connection.createChannel();

			channel.queueDeclare(queueName, false, false, false, null);

			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					System.out.println("Message received : " + message);
				}
			};
			channel.basicConsume(queueName, true, consumer);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void publishMessage(String msg) {
		Connection connection = this.createConnection();
		Channel channel;
		try {
			channel = connection.createChannel();

			channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);

			channel.basicPublish(exchangeName, "", null, msg.getBytes("UTF-8"));

			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void subcribeMessages() {
		try {
			Connection connection = this.createConnection();
			Channel channel = connection.createChannel();

			channel.exchangeDeclare(exchangeName, "fanout");
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, exchangeName, "");

			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					System.out.println(" [x] Received '" + message + "'");
				}
			};
			channel.basicConsume(queueName, true, consumer);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}