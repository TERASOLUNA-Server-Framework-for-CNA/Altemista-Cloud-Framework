/**
 * 
 */
package cloud.altemista.fwk.core.mail.model;

/*
 * #%L
 * altemista-cloud mail server
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.InputStream;

import org.springframework.util.Assert;
import cloud.altemista.fwk.common.model.ContentReadable;

/**
 * Attachment to a mail based on a ContentReadable.
 * Note that certain mail server implementations  may have specific requirements over the attachments
 * (e.g.: JavaMail requires the inputStream to be a fresh one on each call).
 * @author NTT DATA
 */
public class ContentReadableAttachment implements Attachment {
	
	/** The original ContentReadable */
	private ContentReadable source;
	
	/** Is the attachment in-line? */
	private boolean inline;
	
	/**
	 * Constructor
	 * @param source the original ContentReadable
	 */
	public ContentReadableAttachment(ContentReadable source) {
		super();
		Assert.notNull(source);
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.model.ContentReadable#getInputStream()
	 */
	@Override
	public InputStream getInputStream() {
		return this.source.getInputStream();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.model.ContentReadable#getContentType()
	 */
	@Override
	public String getContentType() {
		return this.source.getContentType();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.model.ContentReadable#getPath()
	 */
	@Override
	public String getPath() {
		return this.source.getPath();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.mail.model.Attachment#isInline()
	 */
	@Override
	public boolean isInline() {
		return this.inline;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName()).append(": ");
		sb.append("source=").append(this.source).append("; ");
		sb.append("inline=").append(this.inline);
		return sb.toString();
	}

	/**
	 * @param inline the inline to set
	 */
	public void setInline(boolean inline) {
		this.inline = inline;
	}
}
