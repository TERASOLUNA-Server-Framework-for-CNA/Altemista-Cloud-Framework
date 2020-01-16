
package org.altemista.cloudfwk.common.annotation;

/*
 * #%L
 * altemista-cloud common
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to exclude sensitive parameters and return values (such as passwords)
 * for being printed by the methods of <code>ObjectUtil</code> (e.g: by the performance module)
 * @author NTT DATA
 * @see org.altemista.cloudfwk.common.util.ToStringUtil
 * @see org.altemista.cloudfwk.common.util.ReflectionToStringUtil
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
	java.lang.annotation.ElementType.TYPE,
	java.lang.annotation.ElementType.FIELD,
	java.lang.annotation.ElementType.METHOD,
	java.lang.annotation.ElementType.PARAMETER
})
public @interface HiddenValue {

	/**
	 * The placeholder literal that will be used instead of the hidden value 
	 * @return the placeholder literal
	 */
	String value() default "<hidden>";
}
