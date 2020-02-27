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


import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import javax.xml.ws.WebServiceProvider;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter;

/**
 * Exporter for JAX-WS services using Apache CXF.
 * Additionally, supports filtering of the exported JAX-WS services by package
 * @author NTT DATA
 */
@DependsOn(Bus.DEFAULT_BUS_ID)
public class CxfJaxWsServiceExporter extends AbstractJaxWsServiceExporter {
	
	/** The SLF4J logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(CxfJaxWsServiceExporter.class);
	
	/** Base address for publishing the JAX-WS services */
	private static final String BASE_ADDRESS = "/";
	
	/** Optional array of the packages to filter the auto-detected service beans */ 
	private String[] basePackages;

	/**
	 * Copied from AbstractJaxWsServiceExporter due private access modifier
	 * @see org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter#endpointProperties
	 */
	private Map<String, Object> endpointProperties;

	/**
	 * Copied from AbstractJaxWsServiceExporter due private access modifier
	 * @see org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter#executor
	 */
	private Executor executor;

	/**
	 * Copied from AbstractJaxWsServiceExporter due private access modifier
	 * @see org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter#beanFactory
	 */
	private ListableBeanFactory beanFactory;

	/**
	 * Copied from AbstractJaxWsServiceExporter due private access modifier
	 * @see org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter#publishedEndpoints
	 */
	private final Set<Endpoint> publishedEndpoints = new LinkedHashSet<Endpoint>();

	/**
	 * Initializes the basePackages array 
	 * @param basePackage the basePackage to set
	 */
	public void setBasePackage(String basePackage) {
		
		// Mimics the <context:component-scan/> element behavior
		// @see ComponentScanBeanDefinitionParser.parse()
		this.basePackages = org.springframework.util.StringUtils.tokenizeToStringArray(
				basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter#publishEndpoints()
	 */
	@Override
	public void publishEndpoints() {
		
		// Read the bean names
		Set<String> beanNames = new LinkedHashSet<String>();
		CollectionUtils.addAll(beanNames, this.beanFactory.getBeanNamesForAnnotation(WebService.class));
		CollectionUtils.addAll(beanNames, this.beanFactory.getBeanNamesForAnnotation(WebServiceProvider.class));
		
		LOGGER.debug("Annotated beans found: {}", beanNames);
		
		for (String beanName : beanNames) {
			Object bean = this.beanFactory.getBean(beanName);
			
			// Discard by package
			Class<?> type = AopProxyUtils.ultimateTargetClass(bean);
			if (!this.isCandidate(type)) {
				LOGGER.debug("Discarding bean {}. Class {} not in package[s] {}", beanName, type, this.basePackages);
				continue;
			}
			
			// Reads the annotations
			WebService wsAnnotation = AnnotationUtils.findAnnotation(type, WebService.class);
			WebServiceProvider wsProviderAnnotation = AnnotationUtils.findAnnotation(type, WebServiceProvider.class);
			
			// Creates the endpoint
			LOGGER.info("Creating endpoint for bean {}", beanName);
			Endpoint endpoint = this.createEndpoint(bean);
			
			// Configures the endpoint
			if (this.endpointProperties != null) {
				endpoint.setProperties(this.endpointProperties);
			}
			if (this.executor != null) {
				endpoint.setExecutor(this.executor);
			}
			
			// Publishes the endpoint
			if (wsAnnotation != null) {
				this.publishEndpoint(endpoint, wsAnnotation);
			} else {
				this.publishEndpoint(endpoint, wsProviderAnnotation);
			}
			
			// (@see #destroy())
			this.publishedEndpoints.add(endpoint);
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter#publishEndpoint(javax.xml.ws.Endpoint, javax.jws.WebService)
	 */
	@Override
	protected void publishEndpoint(Endpoint endpoint, WebService annotation) {
		
		EndpointImpl cxfEndpoint = (EndpointImpl) endpoint;
		cxfEndpoint.setAddress(this.findAddress(endpoint, annotation));
		cxfEndpoint.publish();
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter#publishEndpoint(javax.xml.ws.Endpoint, javax.xml.ws.WebServiceProvider)
	 */
	@Override
	protected void publishEndpoint(Endpoint endpoint, WebServiceProvider annotation) {
		
		EndpointImpl cxfEndpoint = (EndpointImpl) endpoint;
		cxfEndpoint.setAddress(this.findAddress(endpoint, annotation));
		cxfEndpoint.publish();
	}

	/**
	 * Convenience method to check if a type is in the base packages
	 * @param type Class of the type to be checked
	 * @return true if the type is at least in one of the base packages,
	 * or if there is no base packages filter
	 */
	protected boolean isCandidate(Class<?> type) {
		
		if (ArrayUtils.isEmpty(this.basePackages)) {
			return true;
		}
		
		// For every base package in the array...
		if (type.getPackage()!=null){
			final String beanPackageName = type.getPackage().getName();
			for (String basePacakge : this.basePackages) {
				if (StringUtils.startsWith(beanPackageName, basePacakge)) {
					return true;
				}
			}
		}
		
		// (not found)
		return false;
	}

	/**
	 * Convenience method to compute the address for an endpoint
	 * @param endpoint the JAX-WS Endpoint object
	 * @param annotation the service bean's WebService annotation
	 * @return String with the address for the Endpoint
	 */
	protected String findAddress(Endpoint endpoint, WebService annotation) {
		
		return this.addressFromServiceName(this.getServiceName(endpoint, annotation));
	}

	/**
	 * Convenience method to compute the address for an endpoint
	 * @param endpoint the JAX-WS Provider Endpoint object
	 * @param annotation the service bean's WebServiceProvider annotation
	 * @return String with the address for the Endpoint
	 */
	protected String findAddress(Endpoint endpoint, WebServiceProvider annotation) {
		
		return this.addressFromServiceName(this.getServiceName(endpoint, annotation));
	}

	/**
	 * Extracts the {@code serviceName} from the {@link WebService} annotation
	 * @param endpoint the JAX-WS Endpoint object
	 * @param annotation the service bean's WebService annotation
	 * @return String the service name found
	 */
	protected String getServiceName(Endpoint endpoint, WebService annotation) {
		
		return annotation.serviceName();
	}

	/**
	 * Extracts the {@code serviceName} from the {@link WebServiceProvider} annotation.
	 * @param endpoint the JAX-WS Endpoint object
	 * @param annotation the service bean's WebServiceProvider annotation
	 * @return String the service name found
	 */
	protected String getServiceName(Endpoint endpoint, WebServiceProvider annotation) {
		
		return annotation.serviceName();
	}
	
	/**
	 * Builds the address from the service name
	 * @param serviceName String with the service name
	 * @return String with the address
	 */
	private String addressFromServiceName(String serviceName) {
		
		return StringUtils.prependIfMissing(serviceName, BASE_ADDRESS);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter#destroy()
	 */
	@Override
	public void destroy() {
		super.destroy();
		
		for (Endpoint endpoint : this.publishedEndpoints) {
			endpoint.stop();
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter#setEndpointProperties(java.util.Map)
	 */
	@Override
	public void setEndpointProperties(Map<String, Object> endpointProperties) {
		super.setEndpointProperties(endpointProperties);
		
		this.endpointProperties = endpointProperties;
	}

	/* (non-Javadoc)
	 * @see org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter#setExecutor(java.util.concurrent.Executor)
	 */
	@Override
	public void setExecutor(Executor executor) {
		super.setExecutor(executor);
		
		this.executor = executor;
	}

	/* (non-Javadoc)
	 * @see org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		super.setBeanFactory(beanFactory);
		
		this.beanFactory = (ListableBeanFactory) beanFactory;
	}
}
