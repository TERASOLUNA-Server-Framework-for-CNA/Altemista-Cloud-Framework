package org.altemista.cloudfwk.core.reporting;

/*
 * #%L
 * altemista-cloud reporting
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.InputStream;

/**
 * Defines how the logical name of the templates are resolved to the actual templates.
 * @author NTT DATA
 */
public interface TemplateResolver {

	/**
	 * Gets the actual template from the logical template name
	 * @param templateName the logical template name
	 * @return InputStream to the actual template
	 */
	InputStream getTemplate(String templateName);
}
