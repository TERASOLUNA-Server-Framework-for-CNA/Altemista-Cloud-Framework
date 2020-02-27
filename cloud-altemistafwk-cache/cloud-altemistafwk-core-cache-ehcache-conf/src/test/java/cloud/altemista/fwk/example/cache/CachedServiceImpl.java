package cloud.altemista.fwk.example.cache;

/*
 * #%L
 * altemista-cloud cache: Ehcache implementation CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import cloud.altemista.fwk.core.cache.annotation.CacheableServiceMethod;

@Service
public class CachedServiceImpl implements CachedService {
	
	private static final AtomicInteger counter = new AtomicInteger();
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.example.cache.CachedService#nonCachedMethod()
	 */
	@Override
	public String nonCachedMethod() {

		return Integer.toString(CachedServiceImpl.counter.getAndIncrement());
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.example.cache.CachedService#cachedMethod()
	 */
	@Override
	@CacheableServiceMethod
	public String cachedMethod() {

		return Integer.toString(CachedServiceImpl.counter.getAndIncrement());
	}
}
