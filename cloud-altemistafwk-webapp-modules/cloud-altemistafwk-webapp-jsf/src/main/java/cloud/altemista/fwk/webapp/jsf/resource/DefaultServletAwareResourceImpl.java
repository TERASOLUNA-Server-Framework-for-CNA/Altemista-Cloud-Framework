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
import javax.faces.application.ResourceWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import com.sun.faces.util.Util;

/**
 * Custom {@link ResourceWrapper} aware of default servlet mapping (i.e. "/"),
 * that consequently can patch wrongly prefix mapped resource paths to the right extension mapped resource paths.
 * <br>
 * This is likely to happen when JSF is not the servlet processing the request but another MVC technology
 * (such as Spring Web Flow).
 * <br>
 * This is documented as the Spring Web Flow issue SWF-1607
 * "Wrong path to /javax.faces.resource/jsf.js" (https://jira.spring.io/browse/SWF-1607),
 * which seems to be originated due the JSF issue JAVASERVERFACES-2887
 * "Wrong path to /javax.faces.resource/jsf.js, suspicious "/*" return from com.sun.faces.util.Util.getMappingForRequest"
 * (https://java.net/jira/browse/JAVASERVERFACES-2887, dead link).
 * <br>
 * According the "Specification of Mappgings" section of the "Java Servlet Specification version 3.0"
 * (http://download.oracle.com/otn-pub/jcp/servlet-3.0-fr-eval-oth-JSpec/servlet-3_0-final-spec.pdf):
 * <blockquote>A string containing only the "/" character indicates the "default" servlet of the application.
 * In this case the servlet path is the request URI minus the context path and the path info is null.</blockquote>
 * As this is the case (because ACF sets the Spring Dispatcher Servlet as this "default" servlet),
 * either <code>com.sun.faces.util.Util.getFacesMapping(FacesContext)</code> or
 * <code>com.sun.faces.util.Util.getMappingForRequest(String, String)</code>
 * fail to properly detecting the Faces Servlet mapping.
 * @author NTT DATA
 * @see cloud.altemista.fwk.webapp.jsf.resource.DefaultServletAwarePrimeResourceHandler
 */
public class DefaultServletAwareResourceImpl extends ResourceWrapper {
	
	/** The right extension mapped Faces Servlet mapping */
	private static final String EXTENSION_MAPPED_FACES_MAPPING = ".jsf";
	
	/** The instance that we are wrapping */
	private Resource wrapped;

	/**
	 * Constructor
	 * @param resource the instance that we are wrapping
	 */
	public DefaultServletAwareResourceImpl(final Resource resource) {
		super();
		
		this.wrapped = resource;
	}

	/* (non-Javadoc)
	 * @see javax.faces.application.ResourceWrapper#getWrapped()
	 */
	@Override
	public Resource getWrapped() {
		
		return this.wrapped;
	}
	
	/* (non-Javadoc)
	 * @see javax.faces.application.ResourceWrapper#getRequestPath()
	 */
	@Override
	public String getRequestPath() {
		
		final String path = super.getRequestPath();
		if (StringUtils.isEmpty(path)) {
			// (nothing to patch)
			return path;
		}
		
		// Is the servlet the default servlet of the application (i.e. mapping "/")?
		// In this case the path info is null
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final ExternalContext externalContext = facesContext.getExternalContext();
		final String pathInfo = externalContext.getRequestPathInfo();
		if (pathInfo != null) {
			// (do not apply any patch)
			return path; 
		}
		
		// Does the URL pattern of the FacesServlet executing the current request starts with "/"?
		final String prefixMappedFacesMapping = Util.getFacesMapping(facesContext);
		if (!Util.isPrefixMapped(prefixMappedFacesMapping)) {
			// (do not apply any patch)
			return path;
		}
		
		// Patches the path to this resource
		final String contextPath = externalContext.getRequestContextPath();
		final String patchedUri = this.convertPrefixMappedToExtensionMapped(
				path, contextPath, prefixMappedFacesMapping, EXTENSION_MAPPED_FACES_MAPPING);
		return patchedUri;
	}

	/**
	 * Patches a wrongly prefix mapped resource path to the right extension mapped resource path.
	 * @param path in the form <code>contextRoot + '/' + facesServletMapping + ResourceHandler.RESOURCE_IDENTIFIER
	 * + '/' + getResourceName + '?' + resourceMetaData</code>
	 * @param contextRoot
	 * @param prefixMapping wrongly detected prefix mapped Faces Servlet mapping
	 * @param extensionMapping right extension mapped Faces Servlet mapping
	 * @return path in the form <code>contextRoot + '/' + ResourceHandler.RESOURCE_IDENTIFIER
	 * + '/' + getResourceName + facesServletMapping + '?' + resourceMetaData</code>
	 * @se javax.faces.application.Resource#getRequestPath()
	 */
	private String convertPrefixMappedToExtensionMapped(
			final String path, final String contextRoot, final String prefixMapping, final String extensionMapping) {
		
		StringBuilder sb = new StringBuilder();
		int index = 0;
		
		// Copies (keeps) the contextRoot (if present)
		if (StringUtils.startsWith(path, contextRoot)) {
			sb.append(contextRoot);
			index += contextRoot.length();
		}
		
		// Skips the wrong detected prefix mapped Faces Servlet mapping (if present)
		if (StringUtils.indexOf(path, prefixMapping, index) == index) {
			index += prefixMapping.length();
		}
		
		// Copies (keeps) the RESOURCE_IDENTIFIER and the resourceName
		String[] uriComponents = StringUtils.split(StringUtils.substring(path, index), "?", 2);
		sb.append(uriComponents[0]);
		
		// Appends the right extension mapped Faces Servlet mapping
		sb.append(extensionMapping);
		
		// Copies the resourceMetaData (if present)
		if (uriComponents.length > 1) {
			sb.append("?");
			sb.append(uriComponents[1]);
		}
		
		return sb.toString();
	}
}
