package cloud.altemista.fwk.core.jmx.bean;

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

import java.util.IllegalFormatException;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.jmx.export.metadata.JmxAttributeSource;
import org.springframework.jmx.export.metadata.ManagedResource;
import org.springframework.jmx.export.naming.MetadataNamingStrategy;
import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Object naming strategy that allows a custom pattern to format the ManagedResourceName.
 * This implementation expects one "%s" in the pattern that will be replaced by the object name.
 * @author NTT DATA
 */
public class CustomMetadataNamingStrategy extends MetadataNamingStrategy {

	/** The SLF4J Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomMetadataNamingStrategy.class);
	
	/**
	 * The {@code JmxAttributeSource} implementation to use for reading metadata.
	 * Duplicated here as it is private in MetadataNamingStrategy and it has no getter.
	 */
	private JmxAttributeSource attributeSource;
	
	/** The custom format to create the ObjectName, applied using String.format() */
	private String objectNameFormat;

	/**
	 * Default constructor.
	 */
	public CustomMetadataNamingStrategy() {
		super();
	}

	/**
	 * Constructor.
	 * @param attributeSource the JmxAttributeSource to use
	 */
	public CustomMetadataNamingStrategy(JmxAttributeSource attributeSource) {
		super(attributeSource);
		this.attributeSource = attributeSource;
	}

	/* (non-Javadoc)
	 * @see org.springframework.jmx.export.naming.MetadataNamingStrategy#setAttributeSource(org.springframework.jmx.export.metadata.JmxAttributeSource)
	 */
	@Override
	public void setAttributeSource(JmxAttributeSource attributeSource) {
		super.setAttributeSource(attributeSource);
		this.attributeSource = attributeSource;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.jmx.export.naming.MetadataNamingStrategy#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		
		Assert.notNull(objectNameFormat, "namePattern must not be null");
		try {
			this.validateObjectNameFormat();
		} catch (IllegalFormatException e) {
			throw new IllegalArgumentException(
					"objectNameFormat has an illegal format: \"" + this.objectNameFormat + "\"", e);
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.jmx.export.naming.MetadataNamingStrategy#getObjectName(java.lang.Object, java.lang.String)
	 */
	@Override
	public ObjectName getObjectName(Object managedBean, String beanKey) throws MalformedObjectNameException {

		Class<?> managedClass = AopUtils.getTargetClass(managedBean);
		ManagedResource mr = this.attributeSource.getManagedResource(managedClass);

		ObjectName objectName = null;
		
		// Check that an object name has been specified.
		if (mr != null && StringUtils.hasText(mr.getObjectName())) {
			try {
				String name = this.applyFormat(managedBean, mr);
				LOGGER.info("Registering MBean with name {}", name);
				objectName = ObjectNameManager.getInstance(name);
				
			} catch (MalformedObjectNameException e) {
				LOGGER.warn("MalformedObjectNameException while registering MBean", e);
			}
		}
		
		if (objectName == null) {
			LOGGER.info("MBean not registered; retrying with the default implementation");
			objectName = super.getObjectName(managedBean, beanKey);
		}

		LOGGER.info("MBean {} registered", objectName);
		return objectName;
	}
	
	/**
	 * Validates the custom format to create the ObjectName.
	 * This method can be overwritten by subclasses that uses a different formatting strategy.
	 * @throws IllegalFormatException if the format is not valid 
	 */
	protected void validateObjectNameFormat() throws IllegalFormatException { // NOSONAR
		
		String.format(this.getObjectNameFormat(), "objectName"); // NOSONAR
	}
	
	/**
	 * Applies the custom format to create the ObjectName.
	 * This method can be overwritten by subclasses that uses a different formatting strategy.
	 * @param bean the managed bean that will be exposed
	 * @param resource the metadata of the managed bean
	 * @return String with the ObjectName
	 */
	protected String applyFormat(Object bean, ManagedResource resource) { // NOSONAR
		
		return String.format(this.getObjectNameFormat(), resource.getObjectName());
	}

	/**
	 * @param objectNameFormat the objectNameFormat to set
	 */
	public void setObjectNameFormat(String objectNameFormat) {
		this.objectNameFormat = objectNameFormat;
	}

	/**
	 * @return the attributeSource
	 */
	protected JmxAttributeSource getAttributeSource() {
		return attributeSource;
	}

	/**
	 * @return the objectNameFormat
	 */
	protected String getObjectNameFormat() {
		return objectNameFormat;
	}
}
