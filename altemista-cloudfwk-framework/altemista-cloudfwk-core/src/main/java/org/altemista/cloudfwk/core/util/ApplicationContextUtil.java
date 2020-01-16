package org.altemista.cloudfwk.core.util;

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


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Utility class to give access to the ApplicationContext;
 * also offers convenience delegated methods to various {@code ApplicationContext.getBean(...)}.<br>
 * Not strictly an utility class as it should be a {@code @Component} that implements {@code ApplicationContextAware} 
 * @author NTT DATA
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

	/** The application context. */
	private static ApplicationContext applicationContext;
	
	/**
	 * Default constructor.
	 */
	public ApplicationContextUtil() {
		super();
	}

	/**
	 * Gets the application context
	 * @return the applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * Return an instance, which may be shared or independent, of the specified bean
	 * (convenience delegated method to applicationContext.getBean).
	 * @param beanName the name of the bean to retrieve
	 * @return an instance of the bean
	 */
	public static Object getBean(String beanName) {
		
		return applicationContext.getBean(beanName);
	}
	
	/**
	 * Return an instance, which may be shared or independent, of the specified bean
	 * (convenience delegated method to applicationContext.getBean).
	 * @param <T> the type of the bean to retrieve
	 * @param beanName the name of the bean to retrieve
	 * @param requiredType type the bean must match. Can be an interface or superclass
	 * of the actual class, or {@code null} for any match. For example, if the value
	 * is {@code Object.class}, this method will succeed whatever the class of the
	 * returned instance.
	 * @return an instance of the bean
	 */
	public static <T> T getBean(String beanName, Class<T> requiredType)  {
		
		return applicationContext.getBean(beanName, requiredType);
	}
	
	/**
	 * Returns the current Spring Environment
	 * @return the Spring Environment associated with the application context.
	 */
	public static Environment getEnvironment() {
		
		return applicationContext.getEnvironment();
	}
	
	/**
	 * Return the bean instance that uniquely matches the given object type, if any
	 * (convenience delegated method to applicationContext.getBean).
	 * @param <T> the type of the bean to retrieve
	 * @param requiredType type the bean must match; can be an interface or superclass.
	 * {@code null} is disallowed.
	 * @return an instance of the single bean matching the required type
	 */
	public static <T> T getBean(Class<T> requiredType) {
		
		return applicationContext.getBean(requiredType);
	}

	/**
	 * Close spring application context if ctx is of ConfigurableApplicationContext type   
	 */
	public static void unloadApplicationContext() {
		
		if (applicationContext instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext) applicationContext).close();
		}
		
		applicationContext = null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext ctx) {
		
		// Assign the ApplicationContext into a static method
		if (ctx instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext) ctx).registerShutdownHook();
		}
		
		// Sets the static application context
		applicationContext = ctx; // NOSONAR (Dodgy - Write to static field from instance method)
	}
}
