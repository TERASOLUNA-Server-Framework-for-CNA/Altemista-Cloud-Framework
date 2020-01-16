package org.altemista.cloudfwk.test.integration.file;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"classpath:/spring/altemista-cloudfwk-test-file-common.xml",
	"classpath:/spring/altemista-cloudfwk-test-file-binary.xml"
})
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class BinaryCopyTest extends AbstractSpringContextTest {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${java.io.tmpdir}")
	private String javaIoTmpdir;
	
	@Value("${test.integration.file.outbound.path}")
	private String outboundPath;
	
	@Value("${test.integration.file.name}")
	private String fileName;

	@Test
	public void testBinary() throws InterruptedException {
		Thread.sleep(2000);
		boolean exists = Files.exists(Paths.get(outboundPath + fileName));
		Assert.assertEquals(true, exists);
	}

}
