/**
 * 
 */
package cloud.altemista.fwk.test.util;

/*
 * #%L
 * Test dependency for altemista-cloud framework and application unit tests
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.springframework.core.io.InputStreamSource;

/**
 * InputStreamSource that returns a fresh SlowByteArrayInputStream on each call.
 * To be used where a latency reading from an array is to be simulated
 * and a fresh InputStream should be created on each call
 * @author NTT DATA
 * @see cloud.altemista.fwk.test.util.SlowByteArrayInputStream
 */
public class SlowInputStreamSource implements InputStreamSource {
	
	/** The array of bytes */
	private byte[] buf;
	
	/**
	 * Constructor
	 * @param buf the array of bytes
	 */
	public SlowInputStreamSource(byte[] buf) {
		super();
		
		this.buf = (buf == null) ? null : Arrays.copyOf(buf, buf.length);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.core.io.InputStreamSource#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		
		return new SlowByteArrayInputStream(this.buf);
	}
}
