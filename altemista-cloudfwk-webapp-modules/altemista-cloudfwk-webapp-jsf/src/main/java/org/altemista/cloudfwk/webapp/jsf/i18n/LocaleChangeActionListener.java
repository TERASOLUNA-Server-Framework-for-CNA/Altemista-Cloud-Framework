package org.altemista.cloudfwk.webapp.jsf.i18n;

import java.util.Locale;

import javax.faces.context.ExternalContext;

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
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.altemista.cloudfwk.core.util.ApplicationContextUtil;

import com.sun.faces.application.ActionListenerImpl;

/**
 * ActionListener that allows for changing the current locale on a request,
 * in a similar way to Spring LocaleChangeInterceptor.<br>
 * This ActionListener actually intercepts every action event, but only processes those with the "locale" parameter.<br>
 * This ActionListener works with the LocaleAwareLifecycle that makes the LocaleResolver available to Spring i18n beans.
 * 
 * @author NTT DATA
 * @see org.springframework.web.servlet.i18n.LocaleChangeInterceptor
 * @see org.altemista.cloudfwk.webapp.jsf.i18n.LocaleAwareLifecycle
 */
public class LocaleChangeActionListener extends ActionListenerImpl implements ActionListener {

	/** Default name of the locale specification parameter: "locale". */
	private String paramName = LocaleChangeInterceptor.DEFAULT_PARAM_NAME;

	/** The previous ActionListener to delegate if necessary */
	private ActionListener delegate;

	/** Spring web-based locale resolution strategy */
	private LocaleResolver localeResolver;

	/**
	 * Constructor
	 * @param delegate the previous ActionListener to delegate if necessary
	 */
	public LocaleChangeActionListener(ActionListener delegate) {
		super();
		
		this.delegate = delegate;
	}

	/* (non-Javadoc)
	 * @see com.sun.faces.application.ActionListenerImpl#processAction(javax.faces.event.ActionEvent)
	 */
	@Override
	public void processAction(ActionEvent actionEvent) {
		
		// If no localeResolver bean was available, do nothing (delegate)
		if (this.getLocaleResolver() == null) {
			this.getDelegate().processAction(actionEvent);
			return;
		}

		// If no "locale" parameter was received, do nothing (delegate)
		final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		final String newLocale = externalContext.getRequestParameterMap()
				.get(this.paramName);
		if (newLocale == null) {
			this.getDelegate().processAction(actionEvent);
			return;
		}
		
		// Sets the new locale to both the LocaleResolver and the LocaleContextHolder
		Locale locale = StringUtils.parseLocaleString(newLocale);
		this.getLocaleResolver().setLocale(
				(HttpServletRequest) externalContext.getRequest(),
				(HttpServletResponse) externalContext.getResponse(),
				locale);
		LocaleContextHolder.setLocale(new Locale(newLocale));
			
		// Return to keep in the same page. With this, no navigation should be defined for changeLocale action
		return;
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
	 * @return the delegate
	 */
	public ActionListener getDelegate() {
		return delegate;
	}
}
