package org.altemista.cloudfwk.common.exception;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.common.exception.SystemException;
import org.altemista.cloudfwk.common.util.DefensiveUtil;

/*
 * #%L
 * altemista-cloud common
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


/**
 * Base implementation for all the framework exceptions.
 * The framework exceptions will automatically prepend their package to the code to prevent code collisions.
 * Application exceptions should inherit from {@link ApplicationException} instead.
 * @author NTT DATA
 */
public abstract class FrameworkException extends SystemException implements MessageSourceResolvable {
	
	/**  serialVersionUID long. */
	private static final long serialVersionUID = -5133582921534121794L;

	/** The arguments (serializable by construction using {@link DefensiveUtil#unmodifiableList(Object[])}) */
	private final List<Object> arguments; // NOSONAR

	/** The default message. */
	private final String defaultMessage;
	
	
	/**
	 * Creates a new FrameworkException.
	 * @param code the code to be used to resolve the message of this exception
	 */
	protected FrameworkException(String code) {
		this(code, (Object[]) null, (String) null);
	}
	
	/**
	 * Creates a new FrameworkException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param defaultMessage the default message to be used to resolve the message of this exception
	 */
	protected FrameworkException(String code, String defaultMessage) {
		this(code, (Object[]) null, defaultMessage);
	}
	
	/**
	 * Creates a new FrameworkException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 */
	protected FrameworkException(String code, Object... arguments) {
		this(code, arguments, (String) null);
	}
	
	/**
	 * Creates a new FrameworkException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	protected FrameworkException(String code, Throwable cause) {
		this(code, (Object[]) null, cause);
	}

	
	/**
	 * Instantiates a new framework exception.
	 *
	 * @param code the code
	 * @param defaultMessage the default message
	 * @param cause the cause
	 */
	protected FrameworkException(String code, String defaultMessage, Throwable cause) {
		this(code, (Object[]) null, defaultMessage, cause);
	}
	
	/**
	 * Creates a new FrameworkException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	protected FrameworkException(String code, Object[] arguments, Throwable cause) {
		this(code, arguments, null, cause);
	}
	
	/**
	 * Creates a new FrameworkException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param defaultMessage the default message to be used to resolve the message of this exception
	 */
	protected FrameworkException(String code, Object[] arguments, String defaultMessage) {
		super(code, defaultMessage);
		
		Assert.notNull(code, "code must not be null");

		this.arguments = DefensiveUtil.unmodifiableList(arguments);
		this.defaultMessage = defaultMessage;
	}
	
	/**
	 * Creates a new FrameworkException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param defaultMessage the default message to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	protected FrameworkException(String code, Object[] arguments, String defaultMessage, Throwable cause) {
		super(code, defaultMessage, cause);
		
		Assert.notNull(code, "code must not be null");

		this.arguments = DefensiveUtil.unmodifiableList(arguments);
		this.defaultMessage = defaultMessage;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(this.getCode());
		if (!ObjectUtils.isEmpty(this.getArguments())) {
			sb.append("; ").append(ArrayUtils.toString(this.getArguments()));
		}
		if (StringUtils.hasText(this.getDefaultMessage())) {
			sb.append("; ").append(this.getDefaultMessage());
		}
		return sb.append("]").toString();
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.MessageSourceResolvable#getCodes()
	 */
	@Override
	public String[] getCodes() {
		
		return new String[] { this.getCode() };
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.MessageSourceResolvable#getArguments()
	 */
	@Override
	public Object[] getArguments() {
		
		return this.arguments == null
				? null
				: this.arguments.toArray(new Object[this.arguments.size()]);
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.MessageSourceResolvable#getDefaultMessage()
	 */
	@Override
	public String getDefaultMessage() {
		
		return this.defaultMessage;
	}
	
	/* (non-Javadoc)
	 * @see org.terasoluna.gfw.common.exception.SystemException#getCode()
	 */
	@Override
	public String getCode() {
		
		return prepareCode(super.getCode());
	}
	
	/**
	 * Framework exceptions will prepend their package to the code to prevent code collisions.
	 * @param code the code
	 * @return the string
	 */
	protected String prepareCode(String code) {
		
		return String.format("e.%s.%s", this.getClass().getPackage().getName(), code);
	}
}
