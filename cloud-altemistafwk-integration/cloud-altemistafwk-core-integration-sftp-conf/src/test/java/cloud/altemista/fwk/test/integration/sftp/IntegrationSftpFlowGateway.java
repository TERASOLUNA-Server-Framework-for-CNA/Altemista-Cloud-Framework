package cloud.altemista.fwk.test.integration.sftp;

import java.util.List;

public interface IntegrationSftpFlowGateway {

	public List<Boolean> lsGetAndRmFiles(String dir);
}
