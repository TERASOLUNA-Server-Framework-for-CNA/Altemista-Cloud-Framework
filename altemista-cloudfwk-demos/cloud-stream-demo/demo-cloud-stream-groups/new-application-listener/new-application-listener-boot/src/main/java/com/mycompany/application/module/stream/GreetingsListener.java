package com.mycompany.application.module.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.mycompany.application.module.model.Greetings;

@Component
@EnableBinding(Sink.class)
public class GreetingsListener {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@StreamListener(value=Sink.INPUT)
	public void handleGreetings(@Payload Greetings greetings) {
        logger.info("receive------------------------{}", greetings);
   }


}

