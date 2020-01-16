
package org.altemista.cloudfwk.core.cache.annotation;

/*
 * #%L
 * altemista-cloud cache
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cache.annotation.Cacheable;

/**
 * Annotation to store cache values into cache area called "repository". It uses
 * method arguments to calculate the key
 * 
 * @author NTT DATA
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Cacheable(cacheNames = "repository", key = "#root.methodName.concat(#root.args)")
public @interface CacheableRepositoryMethod {

	// (no elements)
}
