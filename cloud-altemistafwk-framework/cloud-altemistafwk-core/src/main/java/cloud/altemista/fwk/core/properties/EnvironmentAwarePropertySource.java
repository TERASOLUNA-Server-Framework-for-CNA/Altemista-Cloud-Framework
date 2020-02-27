package cloud.altemista.fwk.core.properties;

/*-
 * #%L
 * altemista-cloud framework core
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.env.PropertySource;
import cloud.altemista.fwk.core.util.EnvironmentUtil;

/**
 * PropertySource that resolves properties that can be configured in multi-environment properties files.<br>
 * Keys of the execution environment-aware properties are split in two parts: {environment}.{key}={value},
 * as follows:<ul>
 * <li>{environment} specifies the execution environment where the value is valid.
 * The special value {@code "*" } acts as a wilcard to make the value valid for all the execution environments.</li>
 * <li>{key} is the actual key of the property the application will use.
 * I.e.: this key is used as the Spring placeholder, without environment prefix</li>
 * </ul>
 * @author NTT DATA
 * @see EnvironmentUtil
 */
public class EnvironmentAwarePropertySource extends PropertySource<FactoryBean<Properties>> {

	/**
	 * Constructor
	 * @param name given name
	 * @param source source object
	 */
	public EnvironmentAwarePropertySource(String name, FactoryBean<Properties> source) {
		super(name, source);
	}

	/* (non-Javadoc)
	 * @see org.springframework.core.env.PropertySource#getProperty(java.lang.String)
	 */
	@Override
	public Object getProperty(String name) {
		
		// (reads the Properties from the source object)
		final Properties properties;
		try {
			properties = this.source.getObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if ((properties == null) || properties.isEmpty()) {
			// (no properties)
			return null;
		}
		
		// Is {environment}.{key} defined?
		String environment = EnvironmentUtil.getCurrentEnvironment();
		if (StringUtils.isNotEmpty(environment)) {
			Object value = properties.get(environment + "." + name);
			if (value != null) {
				return value;
			}
		}
		
		// No; is {key} defined?
		Object value = properties.get(name);
		if (value != null) {
			return value;
		}
		
		// No; is *.{key} defined?
		value = properties.get("*." + name);
		return value;
	}
}
