package org.altemista.cloudfwk.it.integration.mqtt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class IntegrationMQTTConditional implements Condition {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Environment environment = context.getEnvironment();
		String testActive = environment.getProperty("integration.mqtt.test.active");
		logger.info(" ====> testActive: " + testActive);
		return testActive!=null && "true".equals(testActive) ? true : false;
	}

}
