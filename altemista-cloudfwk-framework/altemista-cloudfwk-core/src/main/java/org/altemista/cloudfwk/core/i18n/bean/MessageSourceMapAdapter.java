
package org.altemista.cloudfwk.core.i18n.bean;

/*
 * #%L
 * altemista-cloud framework core
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;


/**
 * Convenience adapter from MessageSource to Map; mainly for usage in expression languages.<br>
 * @author NTT DATA
 * @see org.altemista.cloudfwk.webapp.i18n.bean.HtmlMessageSourceMapAdapter
 */
public class MessageSourceMapAdapter implements Map<String, String>, InitializingBean {

	/** The message source that backs up the map. */
	private MessageSource messageSource;

	/**
	 * Default constructor
	 */
	public MessageSourceMapAdapter() {
		super();
	}
	
	/**
	 * Constructor
	 * @param messageSource the message source that backs up the map
	 */
	public MessageSourceMapAdapter(MessageSource messageSource) {
		super();
		this.messageSource = messageSource;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.messageSource);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return this.get(key) != null;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public String get(Object key) {
		
		// (sanity checks)
		if (key == null) {
			return "";
		}
		final String code = String.valueOf(key);
		if (StringUtils.isEmpty(code)) {
			return "";
		}
		
		final Locale locale = LocaleContextHolder.getLocale();
		
		return this.messageSource.getMessage(code, null, code, locale);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public String put(String key, String value) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public String remove(Object key) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<String> keySet() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<String> values() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	protected void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
