package cloud.altemista.fwk.test.integration.sftp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.file.remote.RemoteFileTemplate;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cloud.altemista.fwk.test.AbstractSpringContextTest;

import com.jcraft.jsch.ChannelSftp.LsEntry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ 
	"classpath:/spring/cloud-altemistafwk-test-rmi-common.xml",
	"classpath:/spring/cloud-altemistafwk-test-rmi-outbound-gateway.xml"
})
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class IntegrationSftpOutboundGatewayTest extends AbstractSpringContextTest {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IntegrationSftpFlowGateway integrationSftpFlowGateway;
	
	@Autowired
	CachingSessionFactory<LsEntry> sessionFactory;

	@Test
	public void testMethod(){
		RemoteFileTemplate<LsEntry> template = null;
		String file1 = "1.ftptest";
		String file2 = "2.ftptest";
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));

		try {
			new File(tmpDir, file1).delete();
			new File(tmpDir, file2).delete();

			template = new RemoteFileTemplate<LsEntry>(sessionFactory);
			IntegrationSftpTestUtils.createTestFiles(template, file1, file2);

			List<Boolean> rmResults = integrationSftpFlowGateway.lsGetAndRmFiles("si.sftp.sample");

			assertEquals(2, rmResults.size());
			for (Boolean result : rmResults) {
				assertTrue(result);
			}

		}
		finally {
			IntegrationSftpTestUtils.cleanUp(template, file1, file2);
			assertTrue("Could note delete retrieved file", new File(tmpDir, file1).delete());
			assertTrue("Could note delete retrieved file", new File(tmpDir, file2).delete());
		}
	}

}
