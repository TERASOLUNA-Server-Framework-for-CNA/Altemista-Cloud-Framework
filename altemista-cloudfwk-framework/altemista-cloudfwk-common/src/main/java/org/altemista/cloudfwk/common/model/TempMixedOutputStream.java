package org.altemista.cloudfwk.common.model;

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


import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamSource;

/**
 * OutputStream backed up by both an in-memory byte array and a temporary file.
 * The temporary will be created only if the content exceeds the buffer size.
 * @author NTT DATA
 */
public class TempMixedOutputStream extends FilterOutputStream implements InputStreamSource {
	
	/** The default prefix string to be used in generating the file name */
	private static final String DEFAULT_PREFIX = "altemista_cloud_";

	/** The default size for the temporary buffer (8K) */
	private static final int DEFAULT_BUFFER_SIZE = 8192;
	
	/** The internal buffer where data is stored */
	private byte[] buf;

	/** The number of valid bytes in the buffer */
	private int count;
	
	/** The prefix string to be used in generating the file name; must be at least three characters long */
	private String prefix;
	
	/** The suffix string to be used in generating the file name */
	private String suffix;
	
	/** The temporary file */
	private File file;
	
	/**
	 * Default constructor
	 */
	public TempMixedOutputStream() {
		this(DEFAULT_PREFIX, null, DEFAULT_BUFFER_SIZE);
	}
	
	/**
	 * Constructor
	 * @param prefix The prefix string to be used in generating the file name; must be at least three characters long
	 */
	public TempMixedOutputStream(String prefix) {
		this(prefix, null, DEFAULT_BUFFER_SIZE);
	}
	
	/**
	 * Constructor
	 * @param prefix The prefix string to be used in generating the file name; must be at least three characters long
	 * @param suffix The suffix string to be used in generating the file name
	 */
	public TempMixedOutputStream(String prefix, String suffix) {
		this(prefix, suffix, DEFAULT_BUFFER_SIZE);
	}
	
	/**
	 * Constructor
	 * @param size The size for the temporary buffer
	 */
	public TempMixedOutputStream(int size) {
		this(DEFAULT_PREFIX, null, size);
	}
	
	/**
	 * Constructor 
	 * @param prefix The prefix string to be used in generating the file name; must be at least three characters long
	 * @param size The size for the temporary buffer
	 */
	public TempMixedOutputStream(String prefix, int size) {
		this(prefix, null, size);
	}
	
	/**
	 * Constructor 
	 * @param prefix The prefix string to be used in generating the file name; must be at least three characters long
	 * @param suffix The suffix string to be used in generating the file name
	 * @param size The size for the temporary buffer
	 */
	public TempMixedOutputStream(String prefix, String suffix, int size) {
		super(null);
		
		if (StringUtils.length(prefix) < 3) { // NOSONAR
			 // (@see java.io.File#createTempFile0(String, String, File, boolean))
			throw new IllegalArgumentException("Prefix string too short");
		}
		if (size <= 0) {
			throw new IllegalArgumentException("Buffer size <= 0");
		}
		
		this.buf = new byte[size];
		this.count = 0;
		this.prefix = prefix;
		this.suffix = suffix;
		this.file = null;
	}
	
	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		
		// If the temporary file has been created, delegates to the underlying output stream
		if (super.out != null) {
			super.write(b);
			
		} else {
			// Fills the temporary buffer 
			if (count < buf.length) {
				buf[count++] = (byte) b;
			}
			
			// If the buffer is full, switches to a temporary file
			if (count == buf.length) {
				// Sets the underlying output stream to a temporary file
				this.file = File.createTempFile(this.prefix, this.suffix);
				this.file.deleteOnExit();
				super.out = new BufferedOutputStream(FileUtils.openOutputStream(this.file));
				
				// Moves the content of the temporary buffer to the underlying output stream
				super.write(this.buf, 0, this.count);
				this.buf = null;
			}
			
		}
	}
	
	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#flush()
	 */
	@Override
	public void flush() throws IOException {
		
		// (only if the underlying output stream exists)
		if (super.out != null) {
			super.flush();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		
		// (only if the underlying output stream exists)
		if (super.out != null) {
			super.close();
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.core.io.InputStreamSource#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		
		// If the temporary file has been created, returns an InputStream over the file
		if (this.file != null) {
			this.flush();
			return FileUtils.openInputStream(this.file);
		}
		
		// Otherwise, returns a ByteArrayInputStream over the temporary buffer
		return new ByteArrayInputStream(this.buf, 0, this.count);
	}
}
