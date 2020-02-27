package cloud.altemista.fwk.core.i18n;

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


import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;

/**
 * Custom interface for resolving messages, with support for the parameterization
 * and internationalization of such messages.
 * Extends {@link org.springframework.context.MessageSource} with methods that does not require an specific Locale,
 * using the current locale given by the method getCurrentLocale().
 * @author NTT DATA
 */
public interface DefaultLocaleMessageSource extends MessageSource {

	/** Bean name in Spring context */
	String BEAN_NAME = "messageSource";

	/** Locale property name */
	String LOCALE_PROPERTY_NAME = DefaultLocaleMessageSource.class.getPackage() + ".LOCALE";

	/**
	 * Gets the current value of the locale.
	 * Try get locale from LocaleContext, if has not been setted, get the been default locale.
	 * Gets the default value of the Java Virtual Machine in other case. 
	 * @return the current locale
	 */
	Locale getCurrentLocale();

	/**
	 * Sets the default locale for this bean.
	 * @param defaultLocale the new default locale
	 */
	void setDefaultLocale(String defaultLocale);

	/**
	 * Try to resolve the message in the current Locale. Return default message if no message was found.
	 * @param code the code to lookup up
	 * @param args array of arguments that will be filled in for params within the message
	 * @param defaultMessage String to return if the lookup fails
	 * @return the resolved message if the lookup was successful;
	 * otherwise the default message passed as a parameter
	 */
	String getMessage(String code, Object[] args, String defaultMessage);

	/**
	 * Try to resolve the message in the current Locale.
	 * @param code the code to lookup up
	 * @param args array of arguments that will be filled in for params within the message
	 * @return the resolved message
	 */
	String getMessage(String code, Object[] args);

	/**
	 * Try to resolve the message in the current Locale
	 * using all the attributes contained within the {@code MessageSourceResolvable} argument that was passed in.
	 * @param resolvable value object storing attributes required to properly resolve a message
	 * @return the resolved message
	 */
	String getMessage(MessageSourceResolvable resolvable);

}
