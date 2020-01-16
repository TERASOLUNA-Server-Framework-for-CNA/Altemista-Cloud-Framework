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

import java.util.Iterator;

import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

/**
 * Custom LifecycleFactory that creates a LocaleAwareLifecycle.
 * @author NTT DATA
 */
public class LocaleAwareLifecycleFactory extends LifecycleFactory {

	/** The previous LifecycleFactory to delegate when necessary */
	private LifecycleFactory delegate;

	/**
	 * Constructor
	 * @param delegate the previous LifecycleFactory to delegate when necessary
	 */
	public LocaleAwareLifecycleFactory(LifecycleFactory delegate) {
		super();
		
		this.delegate = delegate;

		// Gets the default lifecycle to be wrapped
		Lifecycle defaultLifecycle = delegate.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

		// Registers the locale aware lifecycle implementation
		this.addLifecycle(LocaleAwareLifecycle.LIFECYCLE_ID, new LocaleAwareLifecycle(defaultLifecycle));
	}

	/* (non-Javadoc)
	 * @see javax.faces.lifecycle.LifecycleFactory#addLifecycle(java.lang.String, javax.faces.lifecycle.Lifecycle)
	 */
	@Override
	public void addLifecycle(String lifecycleId, Lifecycle lifecycle) {
		this.getDelegate().addLifecycle(lifecycleId, lifecycle);
	}

	/* (non-Javadoc)
	 * @see javax.faces.lifecycle.LifecycleFactory#getLifecycle(java.lang.String)
	 */
	@Override
	public Lifecycle getLifecycle(String lifecycleId) {
		
		return this.getDelegate().getLifecycle(lifecycleId);
	}

	/* (non-Javadoc)
	 * @see javax.faces.lifecycle.LifecycleFactory#getLifecycleIds()
	 */
	@Override
	public Iterator<String> getLifecycleIds() {
		
		return this.getDelegate().getLifecycleIds();
	}

	/**
	 * @return the delegate
	 */
	public LifecycleFactory getDelegate() {
		return delegate;
	}

}
