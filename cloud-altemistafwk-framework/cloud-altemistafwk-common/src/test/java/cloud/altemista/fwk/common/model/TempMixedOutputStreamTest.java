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

import org.junit.Test;

/**
 * Tests the OutputStream backed up by both an in-memory byte array and a temporary file.
 * @author NTT DATA
 */
public class TempMixedOutputStreamTest extends AbstractOutputStreamTest<TempMixedOutputStream> {

	@Test
	public void testNoContent() throws IOException {
		
		this.doTest(0, 1, false);
	}

	@Test
	public void testReadOnce() throws IOException {
		
		this.doTest(1, 1, false);
	}

	@Test
	public void testReadManySequential() throws IOException {
		
		this.doTest(1, 10, false);
	}

	@Test
	public void testReadManyParallel() throws IOException {
		
		this.doTest(1, 10, true);
	}

	@Test
	public void testLargeReadOnce() throws IOException {
		
		this.doTest(1000, 1, false);
	}

	@Test
	public void testLargeReadManySequential() throws IOException {
		
		this.doTest(1000, 10, false);
	}

	@Test
	public void testLargeReadManyParallel() throws IOException {
		
		this.doTest(100, 10, true);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.model.AbstractOutputStreamTest#createOutputStream()
	 */
	@Override
	protected TempMixedOutputStream createOutputStream() throws IOException {
		return new TempMixedOutputStream();
	}

}
