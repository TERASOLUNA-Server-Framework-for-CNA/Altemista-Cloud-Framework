package cloud.altemista.fwk.common.exception;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.util.Assert;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import cloud.altemista.fwk.common.util.DefensiveUtil;

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
 * Base for application specific exceptions.
 * Application exceptions should inherit from this class.
 * @author NTT DATA
 */
public abstract class ApplicationException extends BusinessException implements MessageSourceResolvable{
	
	/**  serialVersionUID long. */
	private static final long serialVersionUID = -8127517564433635732L;

	/** The code. */
	private final String code;

	/** The arguments (serializable by construction using {@link DefensiveUtil#unmodifiableList(Object[])}) */
	private final List<Object> arguments; // NOSONAR

	/** The default message. */
	private final String defaultMessage;
	
	/**
	 * Creates a new ApplicationException.
	 * @param code the code to be used to resolve the message of this exception
	 */
	protected ApplicationException(String code) {
		this(code, (Object[]) null, (String) null);
	}
	
	/**
	 * Creates a new ApplicationException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param defaultMessage the default message to be used to resolve the message of this exception
	 */
	protected ApplicationException(String code, String defaultMessage) {
		this(code, (Object[]) null, defaultMessage);
	}
	
	/**
	 * Creates a new ApplicationException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 */
	protected ApplicationException(String code, Object... arguments) {
		this(code, arguments, (String) null);
	}
	
	/**
	 * Creates a new ApplicationException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	protected ApplicationException(String code, Throwable cause) {
		this(code, (Object[]) null, cause);
	}

	
	/**
	 * Instantiates a new application exception.
	 *
	 * @param code the code
	 * @param defaultMessage the default message
	 * @param cause the cause
	 */
	protected ApplicationException(String code, String defaultMessage, Throwable cause) {
		this(code, (Object[]) null, defaultMessage, cause);
	}
	
	/**
	 * Creates a new ApplicationException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	protected ApplicationException(String code, Object[] arguments, Throwable cause) {
		this(code, arguments, null, cause);
	}
	
	/**
	 * Creates a new ApplicationException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param defaultMessage the default message to be used to resolve the message of this exception
	 */
	protected ApplicationException(String code, Object[] arguments, String defaultMessage) {
		super((defaultMessage != null) ? defaultMessage : code);
		
		Assert.notNull(code, "code must not be null");
		
		this.code = code;
		this.arguments = DefensiveUtil.unmodifiableList(arguments);
		this.defaultMessage = defaultMessage;
	}
	
	/**
	 * Creates a new ApplicationException.
	 * @param code the code to be used to resolve the message of this exception
	 * @param arguments the array of arguments to be used to resolve the message of this exception
	 * @param defaultMessage the default message to be used to resolve the message of this exception
	 * @param cause the cause 
	 */
	protected ApplicationException(String code, Object[] arguments, String defaultMessage, Throwable cause) {
		super(ResultMessages.error().add(ResultMessage.fromText((defaultMessage != null) ? defaultMessage : code)),
				cause);
		
		Assert.notNull(code, "code must not be null");

		this.code = code;
		this.arguments = DefensiveUtil.unmodifiableList(arguments);
		this.defaultMessage = defaultMessage;
	}
	
	
	/**
	 * Gets the code.
	 * @return the code
	 */
	protected String getCode() {
		return this.code;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(this.getCode());
		if (!CollectionUtils.isEmpty(this.arguments)) {
			sb.append("; ").append(StringUtils.join(this.arguments, ","));
		}
		if (StringUtils.isNotBlank(this.getDefaultMessage())) {
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
	
}
