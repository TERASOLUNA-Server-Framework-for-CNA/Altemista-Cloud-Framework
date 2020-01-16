package org.altemista.cloudfwk.test.integration.sftp;

import java.io.File;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.file.remote.RemoteFileTemplate;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

import com.jcraft.jsch.ChannelSftp.LsEntry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ 
	"classpath:/spring/altemista-cloudfwk-test-rmi-common.xml",
	"classpath:/spring/altemista-cloudfwk-test-rmi-outbound-transfer.xml"
})
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class IntegrationSftpOutboundTransferTest extends AbstractSpringContextTest {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MessageChannel inputChannel;
	
	@Autowired
	CachingSessionFactory<LsEntry> sessionFactory;

	@Test
	public void testOutbound() throws Exception{

		final String sourceFileName = "test.txt";
		final String destinationFileName = sourceFileName +"_foo";

		RemoteFileTemplate<LsEntry> template = new RemoteFileTemplate<LsEntry>(sessionFactory);
		IntegrationSftpTestUtils.createTestFiles(template);

		try {
			URL url = this.getClass().getResource("/files/" + sourceFileName);
			final File file = new File(url.toURI());

			Assert.isTrue(file.exists(), String.format("File '%s' does not exist.", sourceFileName));

			final Message<File> message = MessageBuilder.withPayload(file).build();

			inputChannel.send(message);
			Thread.sleep(2000);

			Assert.isTrue(IntegrationSftpTestUtils.fileExists(template, destinationFileName),
					String.format("File '%s' does not exist.", destinationFileName));

			logger.info(String.format("Successfully transferred '%s' file to a " +
					"remote location under the name '%s'", sourceFileName, destinationFileName));
		}
		finally {
			IntegrationSftpTestUtils.cleanUp(template, destinationFileName);
		}
	}

}
