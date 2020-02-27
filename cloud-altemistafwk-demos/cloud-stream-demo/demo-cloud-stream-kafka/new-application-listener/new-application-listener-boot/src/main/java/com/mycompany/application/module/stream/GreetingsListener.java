package com.mycompany.application.module.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.mycompany.application.module.model.Greetings;

@Component
public class GreetingsListener {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @StreamListener(GreetingsStreams.INPUT)
    public void handleGreetings(@Payload Greetings greetings) {
        logger.info("receive------------------------{}", greetings);
   }


}

