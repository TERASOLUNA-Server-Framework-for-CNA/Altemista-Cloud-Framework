
package cloud.altemista.fwk.webapp.jsf.i18n;

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


import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;


/**
 * Fake map that allows accessing the internationalized messages, mainly for usage in expression languages.
 * @author NTT DATA
 */
public class MessageSourceMap implements Map<String, MessageSourceMap> {

	/** The message source that backs up the map. */
	private MessageSource messageSource;
	
	/** The current key; it may not be a copmlete key */
	private String currentKey;

	/**
	 * Initial constructor
	 * @param messageSource the message source that backs up the map
	 * @param key the initial part of the key (e.g.: "my")
	 */
	public MessageSourceMap(MessageSource messageSource, String key) {
		super();
		
		Assert.notNull(messageSource);
		Assert.hasText(key);
		
		this.messageSource = messageSource;
		this.currentKey = StringUtils.trimToEmpty(key);
	}

	/**
	 * Incremental constructor
	 * @param source The current MessageSourceMap with the previous parts of the key (e.g.: "my")
	 * @param key the next part of the key to be added (e.g.: "message")
	 */
	private MessageSourceMap(MessageSourceMap source, String key) {
		super();
		
		Assert.notNull(source);
		Assert.hasText(key);
		
		this.messageSource = source.messageSource;
		this.currentKey = source.currentKey + "." + StringUtils.trimToEmpty(key);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public MessageSourceMap get(Object key) {
		
		// When get() is invoked, the current key grows with the next part of the key.
		// e.g.: being "my.message" the current resolved map, ".key" will return a new map for the key "my.message.key"

		// (sanity check)
		if ((key == null)
				|| (!(key instanceof String))
				|| StringUtils.isBlank((String) key)) {
			// (nothing to add to the key)
			return this;
		}
		
		// Returns a new map with the augmented key
		return new MessageSourceMap(this, (String) key);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		// When toString() is invoked, the current key is expected to be complete (e.g.: "my.message.key")
		// look for the the internationalized message
		return this.messageSource.getMessage(this.currentKey, null, this.currentKey, LocaleContextHolder.getLocale());
	}
	
	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return 1;
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
		return this.currentKey.equals(key);
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
	public MessageSourceMap put(String key, MessageSourceMap value) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public MessageSourceMap remove(Object key) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends String, ? extends MessageSourceMap> m) {
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
		return Collections.singleton(this.currentKey);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<MessageSourceMap> values() {
		return Collections.singleton(this);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<Map.Entry<String, MessageSourceMap>> entrySet() {
		throw new UnsupportedOperationException();
	}
}
