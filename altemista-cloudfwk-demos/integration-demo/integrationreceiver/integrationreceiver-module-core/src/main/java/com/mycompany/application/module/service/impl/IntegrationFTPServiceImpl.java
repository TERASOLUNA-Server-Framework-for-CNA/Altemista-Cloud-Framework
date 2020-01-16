package com.mycompany.application.module.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.application.module.service.IntegrationFTPService;

//@Service
public class IntegrationFTPServiceImpl implements IntegrationFTPService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

//	@Autowired(required = false)
//	private MessageChannel ftpOutChannel;
	
	@Override
	public void sendFile(String filePath) {
//		if (ftpOutChannel!=null) {
//			final File fileToSendA = new File(filePath);
//			Message<File> message = MessageBuilder.withPayload(fileToSendA).build();
//			ftpOutChannel.send(message);
//		}
	}

}
