package org.altemista.cloudfwk.common.model;

import java.io.Serializable;

/*
 * #%L
 * altemista-cloudfwk common
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;
import org.altemista.cloudfwk.common.util.DefensiveUtil;

/**
 * A specific content type (also known as media type or MIME type)
 * and a set of extensions usually associated to files of that specific type.
 * @author NTT DATA
 */
public final class ContentType implements Serializable {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = -5500564710409634612L;

	/** Comma-Separated Values (CSV) */
	public static final ContentType CSV = register("text/csv", "csv");
	
	/** HyperText Markup Language (HTML) */
	public static final ContentType HTML = register("text/html", "html", "htm");
	
	/** Microsoft Excel */
	public static final ContentType MS_EXCEL = register("application/vnd.ms-excel", "xls");
	
	/** Microsoft PowerPoint */
	public static final ContentType MS_POWERPOINT = register("application/vnd.ms-powerpoint", "ppt");
	
	/** Microsoft Word */
	public static final ContentType MS_WORD = register("application/msword", "doc");
	
	/** Microsoft Office - OOXML - Spreadsheet */
	public static final ContentType OOXML_EXCEL = register(
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
	
	/** Microsoft Office - OOXML - Word Document */
	public static final ContentType OOXML_WORD = register(
			"application/vnd.openxmlformats-officedocument.wordprocessingml.sheet", "docx");
	
	/** Microsoft Office - OOXML - Presentation */
	public static final ContentType OOXML_POWERPOINT = register(
			"application/vnd.openxmlformats-officedocument.presentationml.sheet", "pptx");

	/** Adobe Portable Document Format (PDF) */
	public static final ContentType PDF = register("application/pdf", "pdf");

	/** Rich Text Format */
	public static final ContentType RTF = register("application/rtf", "rtf");
	
	/** Text file */
	public static final ContentType TEXT = register("text/plain", "txt");

	/** Extensible Markup Language (XML) */
	public static final ContentType XML = register("application/xml", "xml");

	/** The content type; also known as media type or MIME type */
	private String contentType;
	
	/**
	 * The known extensions. The first element will be the default extension.
	 * (Serializable by construction using {@link DefensiveUtil#unmodifiableList(Object[])})
	 */
	private List<String> extensions; // NOSONAR
	
	/**
	 * Private constructor. Use <code>ContentType.register()<code> instead.
	 * @param contentType The content type
	 * @param extensions The known extensions. The first element will be the default extension
	 * @see org.altemista.cloudfwk.common.model.ContentType#register(String, String...)
	 */
	private ContentType(String contentType, String ... extensions) {
		super();
		
		this.contentType = contentType;
		this.extensions = DefensiveUtil.unmodifiableList(extensions);
	}

	
	/**
	 * Creates and registers a new content type
	 * @param pContentType The content type
	 * @param pExtensions The known extensions. The first element will be the default extension
	 * @return ContentType
	 */
	public static ContentType register(String pContentType, String ... pExtensions) {
		
		// (sanity checks)
		final String lContentType = StringUtils.trimToNull(pContentType);
		Assert.notNull(lContentType, "The content type is mandatory");
		
		final List<String> extensions = new ArrayList<String>();
		if (pExtensions != null) {
			for (String extension : pExtensions) {
				if (StringUtils.isNotBlank(extension)) {
					extensions.add(StringUtils.trim(extension));
				}
			}
		}
		Assert.notEmpty(extensions, "At least one extension is mandatory");
		
		// Creates and registers the content type
		final ContentType contentType = new ContentType(
				lContentType, extensions.toArray(new String[extensions.size()]));
		ContentTypeRegister.SET.add(contentType);
		return contentType;
	}
	
	/**
	 * Looks for the registered content type object with the specified content type
	 * @param pContentType the content type
	 * @return ContentType or null if no matching content type was not found
	 */
	public static ContentType fromContentType(String pContentType) {
		
		// (sanity checks)
		final String contentType = StringUtils.trimToNull(pContentType);
		if (contentType == null) {
			return null;
		}
		
		// Looks for the specified content type
		for (ContentType it : ContentTypeRegister.SET) {
			if (StringUtils.equalsIgnoreCase(contentType, it.contentType)) {
				return it;
			}
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Guesses the registered content type object based on the filename
	 * @param pFilename the filename
	 * @return ContentType or null if no matching content type was not found
	 */
	public static ContentType fromFilename(String pFilename) {
		
		// (sanity checks)
		final String filename = StringUtils.trimToNull(pFilename);
		if (filename == null) {
			return null;
		}
		
		return fromExtension(FilenameUtils.getExtension(filename));
	}
	
	/**
	 * Guesses the registered content type object based on the filename extension
	 * @param pExtension the filename extension
	 * @return ContentType or null if no matching content type was not found
	 */
	public static ContentType fromExtension(String pExtension) {
		
		// (sanity checks)
		final String extension = StringUtils.trimToNull(pExtension);
		if (extension == null) {
			return null;
		}
		
		// First, looks for matches with the default extension
		for (ContentType it : ContentTypeRegister.SET) {
			if (StringUtils.equalsIgnoreCase(extension, it.getDefaultExtension())) {
				return it;
			}
		}
		
		// Then, looks for matches in all the known extensions
		for (ContentType it : ContentTypeRegister.SET) {
			for (String itExtension : it.extensions) {
				if (StringUtils.equalsIgnoreCase(extension, itExtension)) {
					return it;
				}
			}
		}
		
		// Not found
		return null;
	}
	
	/**
	 * @return the content type
	 */
	public String getContentType() {
		return this.contentType;
	}

	/**
	 * @return the default extension for this content type
	 */
	public String getDefaultExtension() {
		return this.extensions.iterator().next();
	}

	/**
	 * @return the known extensions for this content type
	 */
	public String[] getKnownExtensions() {
		
		return (this.extensions == null)
				? null
				: this.extensions.toArray(new String[this.extensions.size()]);
	}
	
	/**
	 * Checks if a filename (its extension, in fact) is appropriate for this content type 
	 * @param pFilename the filename
	 * @return flag indicating if the filename is appropriate for this content type
	 */
	public boolean isProperFilename(String pFilename) {
		
		// (sanity checks)
		final String filename = StringUtils.trimToNull(pFilename);
		if (filename == null) {
			return false;
		}
		
		return this.isProperExtension(FilenameUtils.getExtension(filename));
	}
	
	/**
	 * Checks if a filename extension is appropriate for this content type 
	 * @param pExtension the filename extension
	 * @return flag indicating if the filename extension is appropriate for this content type
	 */
	public boolean isProperExtension(String pExtension) {
		
		// (sanity checks)
		final String extension = StringUtils.trimToNull(pExtension);
		if (extension == null) {
			return false;
		}
		
		// Looks for matches in all the known extensions
		for (String it : this.extensions) {
			if (StringUtils.equalsIgnoreCase(extension, it)) {
				return true;
			}
		}
		
		// Not found
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return new ToStringBuilder(this)
				.append("contentType", this.contentType)
				.append("extensions", this.extensions)
				.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		
		return new HashCodeBuilder()
				.append(this.contentType)
				.append(this.extensions)
				.toHashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		ContentType that = (ContentType) obj;
		return new EqualsBuilder()
				.append(this.contentType, that.contentType)
				.append(this.extensions, that.extensions)
				.isEquals();
	}
	
	/**
	 * Set of registered content types
	 * (Inner class to ensure the Set gets initialized before the constant ContentTypes)
	 */
	private static final class ContentTypeRegister {
		
		/** The registered content types */
		private static final Set<ContentType> SET = new HashSet<ContentType>();
		
		/**
		 * Private default constructor (non-instanceable class)
		 */
		private ContentTypeRegister() {
			super();
		}
	}
}
