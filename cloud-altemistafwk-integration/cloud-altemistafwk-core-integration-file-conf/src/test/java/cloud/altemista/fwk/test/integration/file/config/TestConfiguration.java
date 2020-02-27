package cloud.altemista.fwk.test.integration.file.config;

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
	
	@Value("${test.integration.file.absolutepath}")
	private String fileAbsolutepath;
	
	@Value("${test.integration.file.name}")
	private String fileName;
	
	@Value("${test.integration.file.inbound.path}")
	private String inboundPath;
	
	@Value("${test.integration.file.outbound.path}")
	private String outboundPath;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostConstruct
	public void init() throws Exception {
		
		File outboundDirectory = new File(outboundPath);
		if (!outboundDirectory.exists()) {
			outboundDirectory.mkdirs();
		}
		
		File inboundDirectory = new File(inboundPath);
		if (!inboundDirectory.exists()) {
			inboundDirectory.mkdirs();
		}
		
		FileUtils.cleanDirectory(outboundDirectory); 
		
		logger.info(" ====> Copying "+fileAbsolutepath + fileName +" to Java IO Temp: " + inboundPath + fileName);
		
		File dest = new File(inboundPath + fileName);
		URL inputUrl = getClass().getResource(fileAbsolutepath + fileName);
		FileUtils.copyURLToFile(inputUrl, dest);
		
	}

}
