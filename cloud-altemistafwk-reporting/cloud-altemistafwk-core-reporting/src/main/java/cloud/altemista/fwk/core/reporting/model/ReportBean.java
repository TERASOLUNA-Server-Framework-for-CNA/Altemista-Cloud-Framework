package cloud.altemista.fwk.core.reporting.model;

import java.util.HashMap;
import java.util.Map;

import cloud.altemista.fwk.common.model.ContentType;

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

/**
 * POJO with a report information.
 * This class can be overwritten in the different implementations to include specific attributes and logic.
 * For the parameters, this base implementation trims to empty the map keys.
 * @author NTT DATA
 */
public class ReportBean {
	
	/** The logical name of the report template */
	private String template;
	
	/** Specifies the content type (also known as media type or MIME type) of the report that is to be generated */ 
	private ContentType contentType;
	
	/** Specifies the output file name */
	private String filename;

	/** Report parameters. Will eventually be converted to the data types required by the reporting tool. */
	private Map<String, Object> parameters = new HashMap<String, Object>();
	
	/**
	 * Removes all of the mappings from the parameter map.
	 */
	public void clearParameters() {
		
		this.parameters.clear();
	}
	
	/**
	 * Associates the specified parameter value with the specified parameter key.
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with key, or null
	 */
	public Object putParameter(String key, Object value) {
		
		return this.parameters.put(key, value);
	}
	
	/**
	 * @return the report parameters (actually, a copy)
	 */
	public Map<String, Object> getParameters() {
		
		return new HashMap<String, Object>(this.parameters);
	}
	
	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return the contentType
	 */
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
