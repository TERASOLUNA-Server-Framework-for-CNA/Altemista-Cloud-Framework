/**
 * 
 */
package cloud.altemista.fwk.common.model;

/*
 * #%L
 * altemista-cloud common
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.springframework.core.io.InputStreamSource;
import cloud.altemista.fwk.common.TestConstants;

/**
 * Convenience base class for the tests of TempFileOutputStream and MixedOutputStream
 * @param <T> OutputStream & InputStreamSource
 * @author NTT DATA
 */
public abstract class AbstractOutputStreamTest<T extends OutputStream & InputStreamSource> {

	/**
	 * Convenience method
	 * @param contentCount the number of times the binary example will be written in the buffer
	 * @param readCount the number of times the content is to be read
	 * @param isParallel whether the reads will be done in parallel or sequentially
	 * @throws IOException in case of error
	 */
	protected void doTest(int contentCount, int readCount, boolean isParallel) throws IOException {
		
		InputStreamSource instance = this.createAndWrite(contentCount);
		if (isParallel) {
			this.parallelReads(instance, contentCount, readCount);
		} else {
			this.sequentialReads(instance, contentCount, readCount);
		}
	}

	/**
	 * Convenience method
	 * @param contentCount the binary example file will be written in the file
	 * @return InputStreamSource
	 * @throws IOException in case of error
	 */
	protected InputStreamSource createAndWrite(int contentCount) throws IOException {
		
		T instance = null;
		try {
			// Creates the object and writes some binary content
			instance = this.createOutputStream();
			for (int i = 0; i < contentCount; i++) {
				instance.write(TestConstants.BINARY);
			}
			return instance;
			
		} finally {
			IOUtils.closeQuietly(instance);
		}
	}
	
	/**
	 * Creates a new instance of the object to be tested
	 * @return the OutputStream + InputStreamSource to be tested
	 * @throws IOException in case of error
	 */
	protected abstract T createOutputStream() throws IOException;

	/**
	 * Convenience method
	 * @param instance the InputStreamSource to be tested
	 * @param contentCount the number of times the binary example will be written in the buffer
	 * @param readCount the number of times the content is to be read
	 * @throws IOException in case of error
	 */
	protected void sequentialReads(InputStreamSource instance, int contentCount, int readCount) throws IOException {
		
		// Repeats the reading
		InputStream previousInputStream = null;
		for(int i = 0; i < readCount; i++) {
			InputStream inputStream = null;
			try {
				// Gets the InputStream
				inputStream = instance.getInputStream();
				Assert.assertNotNull(inputStream);
				if (previousInputStream != null) {
					// (per contract of InputStreamSource must be a different InputStream on each call)
					Assert.assertNotSame(previousInputStream, inputStream);
				}
				
				if (contentCount == 0) {
					// Nothing to be read
					Assert.assertEquals(-1, inputStream.read());
					
				} else {
					// Reads the content
					final int n = TestConstants.BINARY.length;
					final int m = n * contentCount;
					final byte[] buffer = new byte[m];
					Assert.assertEquals(m, inputStream.read(buffer));
					
					// Asserts the InputStream has no more content
					Assert.assertEquals(-1, inputStream.read());
					
					// Asserts the read content
					for (int j = 0; j < m; j++) {
						Assert.assertEquals(TestConstants.BINARY[j % n], buffer[j]);
					}
				}
				
				// (for next iteration)
				previousInputStream = inputStream;
				
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
		}
	}
	
	/**
	 * Convenience method
	 * @param instance the InputStreamSource to be tested
	 * @param contentCount the number of times the binary example will be written in the buffer
	 * @param readCount the number of times the content is to be read
	 * @throws IOException in case of error
	 */
	protected void parallelReads(InputStreamSource instance, int contentCount, int readCount) throws IOException {
		
		InputStream[] inputStreams = new InputStream[readCount];
		try {
			// Gets the InputStreams
			for (int i = 0; i < readCount; i++) {
				inputStreams[i] = instance.getInputStream();
				if (i > 0) {
					// (per contract of InputStreamSource must be a different InputStream on each call)
					Assert.assertNotSame(inputStreams[i - 1], inputStreams[i]);
				}
			}
			
			// Reads the content
			for (int j = 0, n = TestConstants.BINARY.length, m = n * contentCount; j < m; j++) {
				for (InputStream inputStream : inputStreams) {
					// Asserts the read content
					Assert.assertEquals(TestConstants.BINARY[j % n], (byte) (inputStream.read() & 0xff));
				}
			}

			// Asserts the InputStreams have no more content
			for (InputStream inputStream : inputStreams) {
				Assert.assertEquals(-1, inputStream.read());
			}
			
		} finally {
			for (InputStream inputStream : inputStreams) {
				IOUtils.closeQuietly(inputStream);
			}
		}
	}

}
