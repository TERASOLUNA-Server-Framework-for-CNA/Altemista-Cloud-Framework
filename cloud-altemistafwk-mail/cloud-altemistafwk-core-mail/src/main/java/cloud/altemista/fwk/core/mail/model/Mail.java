
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


import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.ObjectUtils;
import cloud.altemista.fwk.common.util.ArrayUtil;

/**
 * Models a simple mail message whose text can be HTML and can have attachments.
 * @author NTT DATA
 */
public class Mail extends SimpleMailMessage {

	/** serialVersionUID long */
	private static final long serialVersionUID = 7602405987043232303L;

	/** The body as hmtl. */
	private boolean html;
	
	/** The attachments (serializable by construction using {@link ArrayUtil#asList(T[])}) */
	private List<Attachment> attachments; // NOSONAR
	
	/**
	 * Constructor.
	 */
	public Mail() {
		super();
	}
	
	/**
	 * Copy constructor.
	 * @param original
	 */
	public Mail(Mail original) {
		super(original);
		
		this.html = original.html;
		this.attachments = original.attachments;
	}

	/* (non-Javadoc)
	 * @see org.springframework.mail.SimpleMailMessage#toString()
	 */
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder("Mail: ");
		sb.append(super.toString()).append("; ");
		sb.append("html=").append(this.html).append("; ");
		if (!CollectionUtils.isEmpty(this.attachments)) {
			sb.append("attachments=").append(StringUtils.join(this.attachments, ","));
		}
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.mail.SimpleMailMessage#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		
		if (!super.equals(other)) {
			return false;
		}
		if (!(other instanceof Mail)) {
			return false;
		}
		Mail otherMail = (Mail) other;
		return ObjectUtils.nullSafeEquals(this.html, otherMail.html)
				&& CollectionUtils.isEqualCollection(this.attachments, otherMail.attachments);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.mail.SimpleMailMessage#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashCode = super.hashCode();
		hashCode = 29 * hashCode + Boolean.valueOf(this.html).hashCode(); // NOSONAR
		if (this.attachments != null) {
			hashCode = 29 * hashCode + this.attachments.hashCode(); // NOSONAR
		}
		return hashCode;
	}

	/**
	 * @return the html
	 */
	public boolean isHtml() {
		return html;
	}

	/**
	 * @param html the html to set
	 */
	public void setHtml(boolean html) {
		this.html = html;
	}

	/**
	 * @return the attachments
	 */
	public Attachment[] getAttachments() {
		return this.attachments == null
				? null
				: this.attachments.toArray(new Attachment[this.attachments.size()]);
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(Attachment ... attachments) {
		
		if (attachments == null) {
			this.attachments = null;
		}
		this.attachments = ArrayUtil.asList(attachments);
	}
}
