package cloud.altemista.fwk.it.integration.websocket.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WebsocketAdapterController.MAPPING)
public class WebsocketAdapterController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String MAPPING = "test";

	@Autowired
	private DirectChannel webSocketInputChannel;

	@RequestMapping
	public ResponseEntity<Map<String,String>> testMethod() throws InterruptedException {
		
		final Map<String,String> result = new HashMap<String,String>();
		
		webSocketInputChannel.addInterceptor(new ChannelInterceptorAdapter() {
			@Override
			public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
				Object payload = message.getPayload();
				logger.info(" ===> Payload: "+payload);
				result.put("result", payload.toString());
			}
		});
		
		Message<?> message = new Message<String>() {

			@Override
			public String getPayload() {
				return "returned message";
			}

			@Override
			public MessageHeaders getHeaders() {
				return null;
			}
		};
		webSocketInputChannel.send(message);

		Thread.sleep(1000);
		
		return ResponseEntity.ok(result);
	}
}
