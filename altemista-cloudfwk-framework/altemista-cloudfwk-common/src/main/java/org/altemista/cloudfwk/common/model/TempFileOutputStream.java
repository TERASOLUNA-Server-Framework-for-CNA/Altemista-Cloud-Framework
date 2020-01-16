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
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamSource;

/**
 * OutputStream backed up by a temporary file.
 * @author NTT DATA
 */
public class TempFileOutputStream extends FilterOutputStream implements InputStreamSource {
	
	/** The default prefix string to be used in generating the file name */
	private static final String DEFAULT_PREFIX = "altemista_cloud_";
	
	/** The temporary file */
	private File file;
	
	/**
	 * Default constructor
	 * @throws IOException if the file cannot be created or written
	 * or if a parent directory needs creating but that fails
	 */
	public TempFileOutputStream() throws IOException {
		this(DEFAULT_PREFIX, null);
	}

	/**
	 * Constructor 
	 * @param prefix The prefix string to be used in generating the file name; must be at least three characters long
	 * @throws IOException if the file cannot be created or written
	 * or if a parent directory needs creating but that fails
	 */
	public TempFileOutputStream(String prefix) throws IOException {
		this(prefix, null);
	}

	/**
	 * Constructor 
	 * @param prefix The prefix string to be used in generating the file name; must be at least three characters long
	 * @param suffix The suffix string to be used in generating the file name
	 * @throws IOException if the file cannot be created or written
	 * or if a parent directory needs creating but that fails
	 */
	public TempFileOutputStream(String prefix, String suffix) throws IOException {
		super(null);
		
		// Sets the underlying output stream to a temporary file
		this.file = File.createTempFile(StringUtils.defaultString(prefix, DEFAULT_PREFIX), suffix);
		this.file.deleteOnExit();
		super.out = new BufferedOutputStream(FileUtils.openOutputStream(this.file));
	}

	/* (non-Javadoc)
	 * @see org.springframework.core.io.InputStreamSource#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		
		this.flush();
		return FileUtils.openInputStream(this.file);
	}
}
