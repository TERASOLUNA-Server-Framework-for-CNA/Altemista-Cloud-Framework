package cloud.altemista.fwk.web.soap;

/*
 * #%L
 * altemista-cloud SOAP server
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import javax.xml.ws.WebServiceProvider;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Exporter for JAX-WS services using Apache CXF that guesses the service name if not provided,
 * allowing simpler {@link WebService} or {@link WebServiceProvider} annotations.
 * @author NTT DATA
 */
public class SimpleCxfJaxWsServiceExporter extends CxfJaxWsServiceExporter {
	
	/**
	 * Convenience method to retrieve the {@code serviceName},
	 * even if it is not present in the {@link WebService} annotation found.
	 * <p>This algorithm will return the first valid (computable and not null or empty) value of this list:<ol>
	 * <li>{@code serviceName} of the {@link WebService} annotation of the bean class,</li>
	 * <li>{@code serviceName} of the {@link WebService} annotation of the service interface,</li>
	 * <li>{@code name} of the {@link WebService} annotation of the bean class,</li>
	 * <li>{@code name} of the {@link WebService} annotation of the service interface,</li>
	 * <li>Short class name of the {@code endpointInterface} of the {@link WebService} annotation of the bean class,</li>
	 * <li>Short class name of the {@code endpointInterface} of the {@link WebService} annotation of the service interface,</li>
	 * <li>Short class name of the service interface,</li>
	 * <li>Short class name of the bean class.</li>
	 * </ol></p>
	 * @param endpoint the JAX-WS Endpoint object
	 * @param annotation the service bean's WebService annotation
	 * @return String the service name found
	 */
	@Override
	protected String getServiceName(Endpoint endpoint, WebService annotation) {
		
		// Uses the serviceName of the provided annotation if present (1)
		if (StringUtils.isNotEmpty(annotation.serviceName())) {
			return annotation.serviceName();
		}
		
		// Otherwise, searchs for other WebService annotations in the interfaces
		final Class<?> type = endpoint.getImplementor().getClass();
		final Class<?> endpointInterface = this.findEndpointInterface(type, annotation);
		WebService interfaceAnnotation =
				(endpointInterface == null) ? null : endpointInterface.getAnnotation(WebService.class);
		
		// Uses the serviceName of the interface annotation if present (2)
		if ((interfaceAnnotation != null) && StringUtils.isNotEmpty(interfaceAnnotation.serviceName())) {
			return interfaceAnnotation.serviceName();
		}
		
		// Otherwise, uses the name value if present (3, 4)
		if (StringUtils.isNotEmpty(annotation.name())) {
			return annotation.name();
		}
		if ((interfaceAnnotation != null) && StringUtils.isNotEmpty(interfaceAnnotation.name())) {
			return interfaceAnnotation.name();
		}
		
		// Otherwise, uses the endpointInterface value if present (5, 6)
		if (StringUtils.isNotEmpty(annotation.endpointInterface())) {
			return ClassUtils.getShortClassName(annotation.endpointInterface());
		}
		if ((interfaceAnnotation != null) && StringUtils.isNotEmpty(interfaceAnnotation.endpointInterface())) {
			return ClassUtils.getShortClassName(interfaceAnnotation.endpointInterface());
		}
		
		// Otherwise, uses the short name of the endpoint interface class (7)
		if (endpointInterface != null) {
			return ClassUtils.getShortCanonicalName(endpointInterface);
		}

		// Otherwise, uses the short name of the bean class (8)
		return ClassUtils.getShortCanonicalName(type);
	}

	/**
	 * Convenience method to find the proper endpoint interface class of a service,
	 * given the implementor class and the {@link WebService} annotation that will be used to publish the service.
	 * <p>This algorithm will return:<ul>
	 * <li>If there are no {@code endpointInterface} values:<ul>
	 * <li>The {@link WebService} annotated interface if it is the only one, or</li>
	 * <li>the implemented interface if it is the only one, or</li>
	 * <li>{@code null} otherwise.</li>
	 * </ul></li>
	 * <li>If there is only one unique {@code endpointInterface} value in the {@link WebService} annotations:<ul>
	 * <li>The interface that matches the {@code endpointInterface} value
	 * (the interface can either have the {@link WebService} annotation or not), or</li>
	 * <li>{@code null} otherwise.</li>
	 * </ul></li>
	 * <li>{@code null} otherwise (multiple {@code endpointInterface} values.</li>
	 * </ul></p>
	 * @param type the JAX-WS Endpoint implementor class
	 * @param typeAnnotation the service bean's WebService annotation
	 * @return endpoint interface class
	 */
	private Class<?> findEndpointInterface(Class<?> type, WebService typeAnnotation) {
		
		Set<Class<?>> interfaces = new HashSet<Class<?>>();
		Set<Class<?>> annotatedInterfaces = new HashSet<Class<?>>();
		Set<String> endpointInterfaces = new HashSet<String>();
		
		// Discriminator by the endpointInteface value of the WebService annotation(s)
		CollectionUtils.addIgnoreNull(endpointInterfaces,
				StringUtils.trimToNull(typeAnnotation.endpointInterface()));
		
		// For each interface implemented, recursively
		for (Queue<Class<?>> queue = new LinkedList<Class<?>>(Arrays.asList(type.getInterfaces())); !queue.isEmpty(); ) {
			Class<?> i = queue.poll();
			
			// (only once per interface, even if it appears more than once in the hierarchy)
			if (!interfaces.add(i)) {
				continue;
			}
			
			WebService interfaceAnnotation = i.getAnnotation(WebService.class);
			if (interfaceAnnotation != null) {
				annotatedInterfaces.add(i);
				CollectionUtils.addIgnoreNull(endpointInterfaces,
						StringUtils.trimToNull(interfaceAnnotation.endpointInterface()));
			}
			
			// Recursively
			queue.addAll(Arrays.asList(i.getInterfaces()));
		}
		
		// No endpointInterface values
		if (endpointInterfaces.isEmpty()) {
			
			// No endpointInterface values nor annotated interfaces: return the interface if unique
			if (annotatedInterfaces.isEmpty()) {
				return (interfaces.size() == 1) ? CollectionUtils.extractSingleton(interfaces) : null;
			}
			
			// No endpointInterface: return the annotated interface if unique
			return (annotatedInterfaces.size() == 1) ? CollectionUtils.extractSingleton(annotatedInterfaces) : null;
		}
		
		// Unique endpointInterface value
		if (endpointInterfaces.size() == 1) {
			final String endpointInterface = CollectionUtils.extractSingleton(endpointInterfaces);
			
			// Looks for the endpointInterface amongst the interfaces
			for (Class<?> i : interfaces) {
				if (StringUtils.equals(endpointInterface, i.getName())) {
					return i;
				}
			}
			
			// The endpointInterface is not implemented by the bean
			return null;
		}
		
		// Multiple endpointInterface values
		return null;
	}
	
	/**
	 * Convenience method to retrieve the {@code serviceName},
	 * even if it is not present in the {@link WebServiceProvider} annotation found.
	 * <p>This algorithm will return the first valid (computable and not null or empty) value of this list:<ol>
	 * <li>{@code serviceName} of the {@link WebServiceProvider} annotation of the bean class,</li>
	 * <li>{@code serviceName} of the {@link WebServiceProvider} annotation of the service interface,</li>
	 * <li>Short class name of the service interface,</li>
	 * <li>Short class name of the bean class.</li>
	 * </ol></p>
	 * @param endpoint the JAX-WS Endpoint object
	 * @param annotation the service bean's WebServiceProvider annotation
	 * @return String the service name found
	 */
	@Override
	protected String getServiceName(Endpoint endpoint, WebServiceProvider annotation) {
		
		// Uses the serviceName of the provided annotation if present (1)
		if (StringUtils.isNotEmpty(annotation.serviceName())) {
			return annotation.serviceName();
		}
		
		// Otherwise, searchs for other WebService annotations in the interfaces
		final Class<?> type = endpoint.getImplementor().getClass();
		final Class<?> endpointInterface = this.findEndpointInterface(type);
		WebServiceProvider interfaceAnnotation =
				(endpointInterface == null) ? null : endpointInterface.getAnnotation(WebServiceProvider.class);
		
		// Uses the serviceName of the interface annotation if present (2)
		if ((interfaceAnnotation != null) && StringUtils.isNotEmpty(interfaceAnnotation.serviceName())) {
			return interfaceAnnotation.serviceName();
		}
		
		// Otherwise, uses the short name of the endpoint interface class (3)
		if (endpointInterface != null) {
			return ClassUtils.getShortCanonicalName(endpointInterface);
		}

		// Otherwise, uses the short name of the bean class (4)
		return ClassUtils.getShortCanonicalName(type);
	}
	
	/**
	 * Convenience method to find the proper endpoint interface class of a service,
	 * given the implementor class and the {@link WebServiceProvider} annotation that will be used to publish the service.
	 * <p>This algorithm will return:<ul>
	 * <li>The {@link WebServiceProvider} annotated interface if it is the only one, or</li>
	 * <li>the implemented interface if it is the only one, or</li>
	 * <li>{@code null} otherwise.</li>
	 * </ul></p>
	 * @param type the JAX-WS Endpoint implementor class
	 * @return endpoint interface class
	 */
	private Class<?> findEndpointInterface(Class<?> type) {
		
		Set<Class<?>> interfaces = new HashSet<Class<?>>();
		Set<Class<?>> annotatedInterfaces = new HashSet<Class<?>>();
		
		// For each interface implemented, recursively
		Queue<Class<?>> queue = new LinkedList<Class<?>>(Arrays.asList(type.getInterfaces()));
		for (Class<?> i : queue) {
			
			// (only once per interface, even if it appears more than once in the hierarchy)
			if (!interfaces.add(i)) {
				continue;
			}
			
			if (i.isAnnotationPresent(WebServiceProvider.class)) {
				annotatedInterfaces.add(i);
			}
			
			// Recursively
			queue.addAll(Arrays.asList(i.getInterfaces()));
		}
		
		return (annotatedInterfaces.size() == 1) ? CollectionUtils.extractSingleton(annotatedInterfaces)
				: (interfaces.size() == 1) ? CollectionUtils.extractSingleton(interfaces)
				: null;
	}
}
