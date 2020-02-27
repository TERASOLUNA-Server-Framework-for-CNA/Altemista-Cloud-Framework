package com.mycompany.application.module.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@EnableBinding(Sink.class)
public class HandlingListener {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@StreamListener(value=Sink.INPUT)
	public void handleMessage(@Payload long message) throws Exception {
		throw new Exception("BOOM!");
	}

}