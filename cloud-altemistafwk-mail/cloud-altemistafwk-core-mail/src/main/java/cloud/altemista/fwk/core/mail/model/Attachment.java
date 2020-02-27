
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


import cloud.altemista.fwk.common.model.ContentReadable;

/**
 * Attachment to a mail.
 * @author NTT DATA
 */
public interface Attachment extends ContentReadable {
	
	/**
	 * @return Is the attachment in-line?
	 */
	boolean isInline();
}
