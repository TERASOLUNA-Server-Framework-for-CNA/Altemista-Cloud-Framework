package cloud.altemista.fwk.test.integration.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceActivator {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String serviceMethod (String input) {
		logger.info(" ====> ");
		return input;
	}

}
