package org.altemista.cloudfwk.example.cache;

/*
 * #%L
 * altemista-cloud spring cache CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.altemista.cloudfwk.core.cache.annotation.CacheableServiceMethod;

@Service
public class CachedServiceImpl implements CachedService {
	
	private static final AtomicInteger counter = new AtomicInteger();
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.example.cache.CachedService#nonCachedMethod()
	 */
	@Override
	public String nonCachedMethod() {

		return Integer.toString(CachedServiceImpl.counter.getAndIncrement());
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.example.cache.CachedService#cachedMethod()
	 */
	@Override
	@CacheableServiceMethod
	public String cachedMethod() {

		return Integer.toString(CachedServiceImpl.counter.getAndIncrement());
	}
}
