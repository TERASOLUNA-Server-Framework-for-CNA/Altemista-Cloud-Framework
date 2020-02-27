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


public interface CachedService {
	
	String nonCachedMethod();

	String cachedMethod();
}
