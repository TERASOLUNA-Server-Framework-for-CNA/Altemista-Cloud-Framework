/**
 * 
 */
package org.altemista.cloudfwk.test.util;

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


import java.io.InputStream;
import java.util.Arrays;

/**
 * ByteArrayInputStream that takes at least a millisecond to read every byte.
 * To be used where a latency reading from an array is to be simulated.
 * @author NTT DATA
 */
public class SlowByteArrayInputStream extends InputStream {

	/** The array of bytes */
	private byte[] buf;
	
	/** The index of the next character to read from the input stream buffer */
	private int pos;
	
	/** The index one greater than the last valid character in the input stream buffer */
	private int count;
	
	/**
	 * Constructor
	 * @param buf the array of bytes
	 */
	public SlowByteArrayInputStream(byte[] buf) {
		super();
		
		if (buf == null) {
			this.buf = null;
			this.pos = 0;
			this.count = 0;
			
		} else {
			this.buf = Arrays.copyOf(buf, buf.length);
			this.pos = 0;
			this.count = buf.length;
		}
	}

	/* (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	@Override
	public synchronized int read() {
		
		try {
			// Note: SonarQube recommends replacing Thread.sleep() with wait(),
			// but the performance is much worse and causes some tests to fail due timeouts
			Thread.sleep(1L); // NOSONAR
			
		} catch (InterruptedException ignored) { // NOSONAR
			// (this exception is silently ignored)
		}
		
		return (this.pos < this.count) ? (this.buf[this.pos++] & 0xff) : -1;
	}
}
