
package org.altemista.cloudfwk.performance.web;

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


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;
import org.altemista.cloudfwk.core.performance.MeasuredTaskHolder;
import org.altemista.cloudfwk.core.performance.MeasuresStorage;
import org.altemista.cloudfwk.core.performance.model.MeasuredTask;
import org.altemista.cloudfwk.performance.web.model.WebTaskInfo;

/**
 * Web filter to gather performance information about request executions
 * @author NTT DATA
 */
public class WebPerformanceFilterBean extends GenericFilterBean {

	/** Part of the path of JSF resources. */
	private static final String JAVAX_FACES_RESOURCE = "javax.faces.resource";

	/** The holder class for the aspect */
	private MeasuredTaskHolder holder;

	/** The storage policy for registering measured tasks */
	private MeasuresStorage storage;
	
	/** Enables or disables the performance aspect (enabled by default) */
	private boolean enabled = true;
	
	/**
	 * Optional regular expression to use as a filter for the request path.
	 * Gives more flexibility than standard servlet url-filter.
	 * All requests will be logged if no urlFilter is set.
	 */
	private String urlFilter;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.holder, "The holder class for the web filter is required");
		Assert.notNull(this.storage, "The storage policy for registering measured tasks is required");
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// If performance is disabled or the holder is not accepting new tasks, just executes the request
		if ((!this.enabled) || (!this.holder.isAcceptingTasks())) {
			chain.doFilter(request, response);
			return;
		}
		
		// Extracts the information from the request
		WebTaskInfo taskInfo = this.generateTaskInfo(request);
		if (taskInfo == null) {
			chain.doFilter(request, response);
			return;
		}
		
		// Starts a task in the holder class (that will take care of nesting)
		this.holder.start(taskInfo);
		
		try {
			// Executes the request
			chain.doFilter(request, response);
			
		} finally {
			this.saveResponseValues(response, taskInfo);
			
			// Stops the task and puts it in the storage
			MeasuredTask measuredTask = this.holder.stop();
			if (!measuredTask.isNestedTask()) {
				this.storage.put(measuredTask);
			}
		}
	}
	
	/**
	 * Creates a TaskInfo with the information of the request
	 * @param request HttpServletRequest
	 * @return WebTaskInfo with the request information, or null if could not extract information from the request
	 */
	private WebTaskInfo generateTaskInfo(ServletRequest request) {

		// Ignore non-HTTP servlet requests
		if (!(request instanceof HttpServletRequest)) {
			return null;
		}
		
		// (convenience constants)
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final String requestUri = httpRequest.getRequestURI();

		// Ignore Faces resources
		if (requestUri.contains(JAVAX_FACES_RESOURCE)) {
			return null;
		}
		
		// Ignore requests that do not match the urlFilter
		if ((urlFilter != null) && (!requestUri.matches(urlFilter))) {
			return null;
		}
		
		WebTaskInfo taskInfo = new WebTaskInfo();
		taskInfo.setMethod(httpRequest.getMethod());
		taskInfo.setRequestUri(requestUri);
		taskInfo.setQueryString(httpRequest.getQueryString());
		return taskInfo;
	}
	
	/**
	 * Stores the response values in the task information
	 * @param response ServletResponse
	 * @param taskInfo WebTaskInfo
	 */
	private void saveResponseValues(ServletResponse response, WebTaskInfo taskInfo) {

		// Ignore non-HTTP servlet responses
		if (!(response instanceof HttpServletResponse)) {
			return;
		}
		
		// (convenience constants)
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		taskInfo.setStatus(httpResponse.getStatus());
	}
	
	/**
	 * @param holder the holder to set
	 */
	public void setHolder(MeasuredTaskHolder holder) {
		this.holder = holder;
	}

	/**
	 * @param storage the storage to set
	 */
	public void setStorage(MeasuresStorage storage) {
		this.storage = storage;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param urlFilter the urlFilter to set
	 */
	public void setUrlFilter(String urlFilter) {
		this.urlFilter = urlFilter;
	}

}
