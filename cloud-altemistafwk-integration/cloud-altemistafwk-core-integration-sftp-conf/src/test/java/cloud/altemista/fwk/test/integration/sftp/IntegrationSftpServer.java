package cloud.altemista.fwk.test.integration.sftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;

public class IntegrationSftpServer implements InitializingBean, SmartLifecycle {

	public static final int PORT = 0;

	private final SshServer server = SshServer.setUpDefaultServer();

	private volatile int port;

	private volatile boolean running;

	private DefaultSftpSessionFactory defaultSftpSessionFactory;

	public void setPort(int port) {
		this.port = port;
	}

	public void setDefaultSftpSessionFactory(DefaultSftpSessionFactory defaultSftpSessionFactory) {
		this.defaultSftpSessionFactory = defaultSftpSessionFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		final PublicKey allowedKey = decodePublicKey();
		this.server.setPublickeyAuthenticator(new PublickeyAuthenticator() {
			@Override
			public boolean authenticate(String username, PublicKey key, ServerSession session) {
				return key.equals(allowedKey);
			}
		});
		this.server.setPort(this.port);
		this.server.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File("hostkey.ser")));
		List<NamedFactory<Command>> factories = new ArrayList<NamedFactory<Command>>();
		factories.add(new SftpSubsystemFactory());
		this.server.setSubsystemFactories(factories);
		final String pathname = System.getProperty("java.io.tmpdir") + File.separator + "sftptest" + File.separator;
		new File(pathname).mkdirs();
		server.setFileSystemFactory(new VirtualFileSystemFactory(Paths.get(pathname)));
	}

	private PublicKey decodePublicKey() throws Exception {
		InputStream stream = new ClassPathResource("META-INF/keys/sftp_rsa.pub").getInputStream();
		byte[] keyBytes = StreamUtils.copyToByteArray(stream);
		// strip any newline chars
		while (keyBytes[keyBytes.length - 1] == 0x0a || keyBytes[keyBytes.length - 1] == 0x0d) {
			keyBytes = Arrays.copyOf(keyBytes, keyBytes.length - 1);
		}
		byte[] decodeBuffer = Base64Utils.decode(keyBytes);
		ByteBuffer bb = ByteBuffer.wrap(decodeBuffer);
		int len = bb.getInt();
		byte[] type = new byte[len];
		bb.get(type);
		if ("ssh-rsa".equals(new String(type))) {
			BigInteger e = decodeBigInt(bb);
			BigInteger m = decodeBigInt(bb);
			RSAPublicKeySpec spec = new RSAPublicKeySpec(m, e);
			return KeyFactory.getInstance("RSA").generatePublic(spec);

		}
		else {
			throw new IllegalArgumentException("Only supports RSA");
		}
	}

	private BigInteger decodeBigInt(ByteBuffer bb) {
		int len = bb.getInt();
		byte[] bytes = new byte[len];
		bb.get(bytes);
		return new BigInteger(bytes);
	}

	@Override
	public boolean isAutoStartup() {
		return PORT == this.port;
	}

	@Override
	public int getPhase() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void start() {
		try {
			this.server.start();
			this.defaultSftpSessionFactory.setPort(this.server.getPort());
			this.running = true;
		}
		catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void stop(Runnable callback) {
		stop();
		callback.run();
	}

	@Override
	public void stop() {
		if (this.running) {
			try {
				server.stop(true);
			}
			catch (Exception e) {
				throw new IllegalStateException(e);
			}
			finally {
				this.running = false;
			}
		}
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}
}
