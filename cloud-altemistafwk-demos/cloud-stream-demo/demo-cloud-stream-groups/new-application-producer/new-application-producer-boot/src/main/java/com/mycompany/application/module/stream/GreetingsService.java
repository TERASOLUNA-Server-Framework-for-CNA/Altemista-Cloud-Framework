package com.mycompany.application.module.stream;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.mycompany.application.module.model.Greetings;
 
@Service
@EnableBinding(Source.class)
public class GreetingsService {
	@Autowired
    Source  greetingsStreams;
 
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public void sendGreeting(final Greetings greetings) {
        logger.info("send-------------{}", greetings);
        
        greetingsStreams.output().send(MessageBuilder.withPayload(greetings).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}