package cloud.altemista.fwk.remindersjsf.module.exception;

import cloud.altemista.fwk.common.exception.ApplicationException;

/**
 * Business exception of the demo application
 * @author NTT DATA
 */
public class RemindersException extends ApplicationException {

	/** The serial version UID */
	private static final long serialVersionUID = 198879749798042720L;

	/**
	 * Constructor
	 * @param code the code to be used to resolve the message of this exception
	 */
	public RemindersException(String code) {
		super(code);
	}

}
