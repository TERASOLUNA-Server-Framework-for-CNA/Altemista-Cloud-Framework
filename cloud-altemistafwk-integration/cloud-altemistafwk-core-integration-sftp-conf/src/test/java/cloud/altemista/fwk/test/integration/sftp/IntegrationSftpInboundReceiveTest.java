package cloud.altemista.fwk.test.integration.sftp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.file.remote.RemoteFileTemplate;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cloud.altemista.fwk.test.AbstractSpringContextTest;

import com.jcraft.jsch.ChannelSftp.LsEntry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ 
	"classpath:/spring/cloud-altemistafwk-test-rmi-common.xml",
	"classpath:/spring/cloud-altemistafwk-test-rmi-inbound-receive.xml"
})
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class IntegrationSftpInboundReceiveTest  extends AbstractSpringContextTest {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("receiveChannel")
	PollableChannel localFileChannel;
	
	@Autowired
	CachingSessionFactory<LsEntry> sessionFactory;
	
	@Autowired
	SourcePollingChannelAdapter adapter;

	@Test
	public void testMethod() throws InterruptedException{
		RemoteFileTemplate<LsEntry> template = null;
		String file1 = "a.txt";
		String file2 = "b.txt";
		String file3 = "c.bar";
		new File("local-dir", file1).delete();
		new File("local-dir", file2).delete();
		try {
			template = new RemoteFileTemplate<LsEntry>(sessionFactory);
			IntegrationSftpTestUtils.createTestFiles(template, file1, file2, file3);

			adapter.start();
			
			Thread.sleep(2000);

			Message<?> received = localFileChannel.receive();
			assertNotNull("Expected file", received);
			logger.info("Received first file message: " + received);
			
			received = localFileChannel.receive();
			assertNotNull("Expected file", received);
			logger.info("Received second file message: " + received);
			
			received = localFileChannel.receive(1000);
			assertNull("Expected null", received);
			logger.info("No third file was received as expected");
		}
		finally {
			IntegrationSftpTestUtils.cleanUp(template, file1, file2, file3);
			assertTrue("Could note delete retrieved file", new File("local-dir", file1).delete());
			assertTrue("Could note delete retrieved file", new File("local-dir", file2).delete());
		}
	}

}
