package org.altemista.cloudfwk.test.integration.resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class ResourceMessageHandler implements MessageHandler {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		Resource[] resources = (Resource[])message.getPayload();
		for (Resource resource : resources) {
			try {
				logger.info(" ====> Payload: " + resource);
				FileSystemResource file = (FileSystemResource) resource;
				Properties prop = new Properties();
				InputStream targetStream = new FileInputStream(file.getFile());
				prop.load(targetStream);
				logger.info("-- listing properties --");
				for (String key : prop.stringPropertyNames()) {
					String value = prop.getProperty(key);
		            logger.info(key + "=" + value);
		        }
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

}
