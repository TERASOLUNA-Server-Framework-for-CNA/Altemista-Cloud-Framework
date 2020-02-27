
package cloud.altemista.fwk.webapp.jsf.managedbean;

/*
 * #%L
 * altemista-cloud presentation layer: JSF support (PrimeFaces)
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;

/**
 * Exposes the default request attributes with the exception information (those starting with "javax.servlet.error.")
 * as a ManagedBean that can be used in error pages.
 * 
 * @author NTT DATA
 */
@ManagedBean(name = "errorHandler")
@RequestScoped
public class ErrorHandlerBean {

	/**
	 * Returns the request attribute under which the exception object is
	 * propagated during an error dispatch
	 * @return the exception object
	 */
	public Throwable getException() {
		
		return this.<Throwable> get(RequestDispatcher.ERROR_EXCEPTION);
	}

	/**
	 * Returns the request attribute under which the type of the exception
	 * object is propagated during an error dispatch
	 * @return the type of the exception object
	 */
	public Class<Throwable> getExceptionType() {
		
		return this.<Class<Throwable>> get(RequestDispatcher.ERROR_EXCEPTION_TYPE);
	}

	/**
	 * Returns the request attribute under which the exception message is
	 * propagated during an error dispatch
	 * @return the exception message
	 */
	public String getMessage() {
		
		return this.<String> get(RequestDispatcher.ERROR_MESSAGE);
	}

	/**
	 * Returns the request attribute under which the request URI whose
	 * processing caused the error is propagated during an error dispatch
	 * @return the request URI whose processing caused the error
	 */
	public String getRequestUri() {
		
		return this.<String> get(RequestDispatcher.ERROR_REQUEST_URI);
	}

	/**
	 * Returns the request attribute under which the name of the servlet in
	 * which the error occurred is propagated during an error dispatch
	 * @return the name of the servlet in which the error ocurred
	 */
	public String getServletName() {
		
		return this.<String> get(RequestDispatcher.ERROR_SERVLET_NAME);
	}

	/**
	 * Returns the request attribute under which the response status is
	 * propagated during an error dispatch
	 * @return the response status
	 */
	public Integer getStatusCode() {
		
		return this.<Integer> get(RequestDispatcher.ERROR_STATUS_CODE);
	}

	/**
	 * Convenience method
	 * @param name the name of the request attribute
	 * @return the request attribute
	 */
	@SuppressWarnings("unchecked")
	private <O> O get(String name) {

		return (O) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(name);
	}
}
