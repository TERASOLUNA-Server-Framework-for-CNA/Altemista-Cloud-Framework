/**
 * 
 */
package cloud.altemista.fwk.webapp.jsf.resource;

/*-
 * #%L
 * altemista-cloud presentation layer: JSF support (PrimeFaces)
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;

import org.primefaces.application.resource.PrimeResourceHandler;

/**
 * Custom {@link PrimeResourceHandler} aware of default servlet mapping (i.e. "/"),
 * that wraps {@link Resource} as {@link DefaultServletAwareResourceImpl} to
 * patch wrongly prefix mapped resource paths to the right extension mapped resource paths.
 * @author NTT DATA
 * @see cloud.altemista.fwk.webapp.jsf.resource.DefaultServletAwareResourceImpl
 */
public class DefaultServletAwarePrimeResourceHandler extends PrimeResourceHandler {

	/**
	 * Constructor
	 * @param wrapped ResourceHandler
	 */
	public DefaultServletAwarePrimeResourceHandler(ResourceHandler wrapped) {
		super(wrapped);
	}

	/* (non-Javadoc)
	 * @see org.primefaces.application.resource.PrimeResourceHandler#createResource(java.lang.String, java.lang.String)
	 */
	@Override
	public Resource createResource(String resourceName, String libraryName) {
		
		// super.super.createRersource(resourceName, libraryName):
		Resource resource = getWrapped().createResource(resourceName, libraryName);
		
		return this.makeResourceDefaultServletAware(resource);
	}

	/* (non-Javadoc)
	 * @see org.primefaces.application.resource.PrimeResourceHandler#createResource(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Resource createResource(String resourceName, String libraryName, String contentType) {
		
		// super.super.createRersource(resourceName, libraryName, contentType):
		Resource resource = getWrapped().createResource(resourceName, libraryName, contentType);
		
		return this.makeResourceDefaultServletAware(resource);
	}
	
	/**
	 * Ensures the returned Resource is a DefaultServletAwareResourceImpl
	 * @param resource the Resource to be wrapped
	 * @return a DefaultServletAwareResourceImpl
	 */
	protected Resource makeResourceDefaultServletAware(Resource resource) {
		
		if ((resource == null) || (resource instanceof DefaultServletAwareResourceImpl)) {
			return resource;
		}
		
		return new DefaultServletAwareResourceImpl(resource);
	}
}
