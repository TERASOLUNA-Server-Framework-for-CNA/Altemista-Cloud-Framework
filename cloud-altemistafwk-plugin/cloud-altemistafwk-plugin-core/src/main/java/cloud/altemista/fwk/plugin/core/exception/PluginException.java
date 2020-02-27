/**
 * 
 */
package cloud.altemista.fwk.plugin.core.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * Plug-in exception, usually non-recoverable and to be reviewed by the final user
 * @author NTT DATA
 */
public class PluginException extends RuntimeException {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 50817115420305808L;
	
	/** Brief and non-technical description of the problem (e.g.: the title of the dialog that shows the exception) */
	private String shortMessage;
	
	/** Non-technical explanation of what caused the problem to hint the final user how to solve it */
	private String reason;
	
	/** Arguments that will be passed to the messages */
	private transient Object[] arguments;
	
	/** Cached version of the shortMessage once the arguments have been applied */
	private transient String formattedShortMessage;
	
	/** Cached version of the reason once the arguments have been applied */
	private transient String formattedReason;
	
	/** Cached version of the message once the arguments have been applied */
	private transient String formatedMessage;
	
	/**
	 * Constructor
	 * @param shortMessage Brief and non-technical description of the problem
	 * @param reason Non-technical explanation of what caused the problem to hint the final user how to solve it
	 * @param message Detail message with the technical description of the problem
	 * @param arguments Arguments that will be passed to the messages
	 */
	public PluginException(String shortMessage, String reason, String message, Object... arguments) {
		super(message);
		
		this.shortMessage = shortMessage;
		this.reason = reason;
		this.arguments = ((arguments == null) || (arguments.length == 0)) ? null : arguments;
	}
	
	/**
	 * Constructor
	 * @param cause the cause
	 * @param shortMessage Brief and non-technical description of the problem
	 * @param reason Non-technical explanation of what caused the problem to hint the final user how to solve it
	 * @param message Detail message with the technical description of the problem
	 * @param arguments Arguments that will be passed to the messages
	 */
	public PluginException(Throwable cause, String shortMessage, String reason, String message, Object... arguments) {
		super(message, cause);
		
		this.shortMessage = shortMessage;
		this.reason = reason;
		this.arguments = ((arguments == null) || (arguments.length == 0)) ? null : arguments;
	}
	
	/**
	 * Formats (if necessary) and returns the brief and non-technical description of the problem
	 * @return the brief and non-technical description of the problem
	 */
	public String getShortMessage() {
		
		if (this.formattedShortMessage == null) {
			this.formattedShortMessage = this.formatMessage(this.shortMessage);
		}
		return this.formattedShortMessage;
	}
	
	/**
	 * Formats (if necessary) and returns the non-technical explanation of what caused the problem
	 * @return non-technical explanation of what caused the problem
	 */
	public String getReason() {
		
		if (this.formattedReason == null) {
			this.formattedReason = this.formatMessage(this.reason);
		}
		return this.formattedReason;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		
		if (this.formatedMessage == null) {
			this.formatedMessage = this.formatMessage(super.getMessage());
		}
		return this.formatedMessage;
	}

	/**
	 * Convenience method to apply the arguments to the shortMessage, reason or message
	 * @param format the unformatted message
	 * @return the formatted message
	 */
	protected String formatMessage(String format) {
		
		// (sanity check)
		if (this.arguments == null) {
			return format;
		}
		
		try {
			// Formats the message
			return String.format(format, this.arguments);
			
		} catch (Exception e) { // NOSONAR
			// Default value (to avoid this exception hinders the real exception) 
			return format + ';' + StringUtils.join(this.arguments, ';');
		}
	}
}
