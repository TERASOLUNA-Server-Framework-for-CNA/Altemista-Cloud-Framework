package cloud.altemista.fwk.core.soap;

import javax.jws.WebService;

/*
 * #%L
 * altemista-cloud SOAP client
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;
import org.springframework.remoting.jaxws.LocalJaxWsServiceFactory;

/**
 * FactoryBean for a specific port of a JAX-WS service that guesses the service name if not provided,
 * allowing simpler client bean definitions.
 * @author NTT DATA
 */
public class SimpleJaxWsPortProxyFactoryBean extends JaxWsPortProxyFactoryBean {
	
	/* (non-Javadoc)
	 * @see org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		
		// Conveniently sets the serviceName even if it is not provided
		if (StringUtils.isEmpty(this.getServiceName())) {
			this.setServiceName(this.guessServiceName());
		}
		
		super.afterPropertiesSet();
	}
	
	/**
	 * Convenience method to guess the {@code serviceName},
	 * even if it is not provied via the {@code setServiceName()} method.
	 * <p>This algorithm will return the first valid (computable and not null or empty) value of this list:<ol>
	 * <li>Provided {@code serviceName} of the {@link LocalJaxWsServiceFactory},</li>
	 * <li>{@code serviceName} of the {@link WebService} annotation of the service interface,</li>
	 * <li>{@code name} of the {@link WebService} annotation of the service interface,</li>
	 * <li>Short class name of the {@code endpointInterface} of the {@link WebService} annotation of the service interface,</li>
	 * <li>Short class name of the service interface class.</li>
	 * </ol></p>
	 */
	private String guessServiceName() {
		
		// Uses the provided serviceName if present (1)
		if (StringUtils.isNotEmpty(this.getServiceName())) {
			return this.getServiceName();
		}
		
		final Class<?> serviceInterface = this.getServiceInterface();
		if (serviceInterface == null) {
			throw new IllegalArgumentException("Property 'serviceInterface' is required");
		}
		
		final WebService interfaceAnnotation = serviceInterface.getAnnotation(WebService.class);
		
		// Uses the serviceName of the interface annotation if present (2)
		if ((interfaceAnnotation != null) && StringUtils.isNotEmpty(interfaceAnnotation.serviceName())) {
			return interfaceAnnotation.serviceName();
		}
		
		// Otherwise, uses the name value if present (3)
		if ((interfaceAnnotation != null) && StringUtils.isNotEmpty(interfaceAnnotation.name())) {
			return interfaceAnnotation.name();
		}
		
		// Otherwise, uses the endpointInterface value if present in the annotation (4)
		if ((interfaceAnnotation != null) && StringUtils.isNotEmpty(interfaceAnnotation.endpointInterface())) {
			return ClassUtils.getShortClassName(interfaceAnnotation.endpointInterface());
		}
		
		// Otherwise, uses the short name of the endpoint interface class (5)
		return ClassUtils.getShortCanonicalName(serviceInterface);
	}

}
