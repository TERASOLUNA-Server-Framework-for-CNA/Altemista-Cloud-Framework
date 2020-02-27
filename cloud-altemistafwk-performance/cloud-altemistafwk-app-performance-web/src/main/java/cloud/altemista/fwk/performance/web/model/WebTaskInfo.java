package cloud.altemista.fwk.performance.web.model;

/*
 * #%L
 * altemista-cloud performance: web part
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.http.HttpStatus;
import cloud.altemista.fwk.core.performance.model.TaskInfo;

/**
 * Information about the execution of one request as a measured task
 * @author NTT DATA
 */
public class WebTaskInfo implements TaskInfo {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 5902335409228694695L;

	/** The name of the HTTP method */
	private String method;
	
	/** The part of the request URL from the protocol name up to the query string */
	private String requestUri;
	
	/** The query string that is contained in the request URL after the path */
	private String queryString;
	
	/** The HTTP status code for the response */
	private Integer status;

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.performance.model.TaskInfo#getDescription()
	 */
	@Override
	public String getDescription() {

		List<String> fragments = new ArrayList<String>();
		
		if (StringUtils.isNotEmpty(this.method)) {
			fragments.add(this.method);
		}
		
		if (StringUtils.isNotEmpty(this.requestUri)) {
			fragments.add(this.requestUri);
		}
		
		if (StringUtils.isNotEmpty(this.queryString)) {
			fragments.add(String.format("?%s", this.queryString));
		}
		
		if (this.status != null) {
			fragments.add(this.status.toString());
			try {
				fragments.add(HttpStatus.valueOf(this.status).getReasonPhrase());
			} catch (IllegalArgumentException ignored) { // NOSONAR
				// (silently ignores "No matching constant")
			}
		}
		
		return StringUtils.join(fragments, " ");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		// Uses the description also for toString()
		return new ToStringBuilder(this).append(this.getDescription()).toString();
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return this.method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the requestUri
	 */
	public String getRequestUri() {
		return this.requestUri;
	}

	/**
	 * @param requestUri the requestUri to set
	 */
	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return this.queryString;
	}

	/**
	 * @param queryString the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return this.status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

}
