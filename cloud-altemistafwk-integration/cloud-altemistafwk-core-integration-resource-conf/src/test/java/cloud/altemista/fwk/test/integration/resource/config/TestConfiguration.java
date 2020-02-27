package cloud.altemista.fwk.test.integration.resource.config;

import java.io.File;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TestConfiguration {
	
	@Value("${test.integration.resource.absolutepath}")
	private String fileAbsolutepath;
	
	@Value("${test.integration.resource.name1}")
	private String fileName1;
	
	@Value("${test.integration.resource.name2}")
	private String fileName2;
	
	@Value("${test.integration.resource.inbound.path}")
	private String inboundPath;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostConstruct
	public void init() throws Exception {
		
		File inboundDirectory = new File(inboundPath);
		if (!inboundDirectory.exists()) {
			inboundDirectory.mkdirs();
		}
		
		logger.info(" ====> Copying "+fileAbsolutepath + fileName1 +" to Java IO Temp: " + inboundPath + fileName1);
		
		File dest = new File(inboundPath + fileName1);
		URL inputUrl = getClass().getResource(fileAbsolutepath + fileName1);
		FileUtils.copyURLToFile(inputUrl, dest);
		
	}

}
