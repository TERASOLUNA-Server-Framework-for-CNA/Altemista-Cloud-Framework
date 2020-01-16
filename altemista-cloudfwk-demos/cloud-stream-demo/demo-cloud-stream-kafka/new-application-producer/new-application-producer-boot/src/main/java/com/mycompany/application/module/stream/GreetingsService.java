package com.mycompany.application.module.stream;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.mycompany.application.module.model.Greetings;
 
@Service
public class GreetingsService {
    private final GreetingsStreams greetingsStreams;
 
    public GreetingsService(GreetingsStreams greetingsStreams) {
        this.greetingsStreams = greetingsStreams;
    }
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public void sendGreeting(final Greetings greetings) {
        logger.info("send-------------{}", greetings);
        
        MessageChannel messageChannel = greetingsStreams.outboundGreetings();
        messageChannel.send(MessageBuilder.withPayload(greetings).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}