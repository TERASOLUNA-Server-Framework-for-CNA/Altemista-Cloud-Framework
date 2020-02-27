package cloud.altemista.fwk.it.integration.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.websocket.ServerWebSocketContainer;

@Configuration
public class WebsocketAdapterTestConfiguration {

	@Bean
	public ServerWebSocketContainer serverWebSocketContainer() {
		return new ServerWebSocketContainer("/time").withSockJs();
	}
	
}
