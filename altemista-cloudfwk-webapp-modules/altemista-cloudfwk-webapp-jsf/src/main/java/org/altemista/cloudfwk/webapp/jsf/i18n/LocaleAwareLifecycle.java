package org.altemista.cloudfwk.webapp.jsf.i18n;

/*
 * #%L
 * altemista-cloud presentation layer: JSF support (PrimeFaces)
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.altemista.cloudfwk.core.util.ApplicationContextUtil;

/**
 * This class is used to publish the locale selected in each request, like
 * Spring MVC Dispatcher Servlet does.<br>
 * This is needed because Architecture classes needs this locale to get messages,
 * and if MVC not intercept the request, this locale resolver is not present.
 * @author NTT DATA
 */
public class LocaleAwareLifecycle extends Lifecycle {

	/** The ID of the intercepting lifecycle: will match the name of this class ("LocaleAwareLifecycle") */
	public static final String LIFECYCLE_ID = LocaleAwareLifecycleFactory.class.getSimpleName();

	/** The wrapped Lifecycle */
	private Lifecycle wrapped;
	
	/** Spring web-based locale resolution strategy */
	private LocaleResolver localeResolver;

	/**
	 * Constructor
	 * @param wrapped the wrapped Lifecycle
	 */
	public LocaleAwareLifecycle(Lifecycle wrapped) {
		super();
		
		this.wrapped = wrapped;
	}

	/* (non-Javadoc)
	 * @see javax.faces.lifecycle.Lifecycle#addPhaseListener(javax.faces.event.PhaseListener)
	 */
	@Override
	public void addPhaseListener(PhaseListener listener) {
		
		this.getWrapped().addPhaseListener(listener);
	}

	/* (non-Javadoc)
	 * @see javax.faces.lifecycle.Lifecycle#execute(javax.faces.context.FacesContext)
	 */
	@Override
	public void execute(FacesContext context) {
		
		this.publishLocaleResolver((HttpServletRequest) context.getExternalContext().getRequest());
		this.getWrapped().execute(context);
	}

	/* (non-Javadoc)
	 * @see javax.faces.lifecycle.Lifecycle#getPhaseListeners()
	 */
	@Override
	public PhaseListener[] getPhaseListeners() {
		
		return this.getWrapped().getPhaseListeners();
	}

	/* (non-Javadoc)
	 * @see javax.faces.lifecycle.Lifecycle#removePhaseListener(javax.faces.event.PhaseListener)
	 */
	@Override
	public void removePhaseListener(PhaseListener listener) {
		
		this.getWrapped().removePhaseListener(listener);
	}

	/* (non-Javadoc)
	 * @see javax.faces.lifecycle.Lifecycle#render(javax.faces.context.FacesContext)
	 */
	@Override
	public void render(FacesContext context) {
		
		this.getWrapped().render(context);
	}

	/**
	 * Publishes LocaleResolver like Spring DispatcherServlet does when it manages the request.
	 * @param request HttpServletRequest
	 */
	private void publishLocaleResolver(HttpServletRequest request) {
		
		// If LocaleResolver is already published does not publish again
		// (Spring MVC Servlet is already managing the request)
		if (request.getAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE) == null) {
			request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, this.getLocaleResolver());
		}
	}

	/**
	 * @return the localeResolver
	 */
	private LocaleResolver getLocaleResolver() {
		
		if (this.localeResolver == null) {
			try {
				this.localeResolver = ApplicationContextUtil.getBean("localeResolver", LocaleResolver.class);
				
			} catch (NoSuchBeanDefinitionException ignored) { // NOSONAR
				// Can't communicate MVC and JSF application
			}
		}
		
		return this.localeResolver;
	}

	/**
	 * @return the wrapped
	 */
	public Lifecycle getWrapped() {
		return wrapped;
	}
}
