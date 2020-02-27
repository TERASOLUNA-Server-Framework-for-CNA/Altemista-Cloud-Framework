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

@Service
@EnableBinding(Source.class)
public class HandlingService {
	@Autowired
	Source handlingStreams;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public void sendMessage(final long handling) {
		logger.info("send-------------{}", handling);

		handlingStreams.output().send(MessageBuilder.withPayload(handling)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
	}
}