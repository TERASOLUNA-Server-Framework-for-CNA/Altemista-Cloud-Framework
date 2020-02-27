package cloud.altemista.fwk.it.integration.mqtt.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.StringUtils;

@Configuration
@Conditional(IntegrationMQTTConditional.class)
@IntegrationComponentScan
@EnableIntegration
public class IntegrationMQTTConfiguration {
	
	private static final String MQTT_URI = "tcp://localhost:1883";

	private static final String MQTT_USERNAME = "guest";
	
	private static final String MQTT_PASSWORD = "guest";
	
	private static final String MQTT_CLIENT_ID = "testClient";
	
	private static final String MQTT_TOPIC = "testTopic";
	
	private static final int MQTT_QOS = 1;

	@Bean
	public MqttPahoClientFactory mqttClientFactory(){
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setServerURIs(MQTT_URI);
		if (StringUtils.hasText(MQTT_USERNAME) && StringUtils.hasText(MQTT_PASSWORD)){
			factory.setUserName(MQTT_USERNAME);
			factory.setPassword(MQTT_PASSWORD);
		}
		return factory;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel", autoStartup="true")
	public MessageHandler mqttOutbound() {
		String clientId = MQTT_CLIENT_ID;
		if( !StringUtils.hasText(clientId) )
		{
			clientId = UUID.randomUUID().toString();
		}
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId, mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic(MQTT_TOPIC);
		if( MQTT_QOS >= 0 && MQTT_QOS <=2 )
		{

			messageHandler.setDefaultQos(MQTT_QOS);
		}
		return messageHandler;
	}

	@Bean
	public MessageChannel mqttOutboundChannel() {
		DirectChannel dc = new DirectChannel();
		return dc;
	}

	@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
	public interface MqttMsgproducer {
		void sendToMqtt(String data);
	}

}
