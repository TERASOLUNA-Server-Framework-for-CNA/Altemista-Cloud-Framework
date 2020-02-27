package cloud.altemista.fwk.plugin.core.util;

import java.io.PrintStream;

import cloud.altemista.fwk.plugin.core.model.ConsoleProvider;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Convenience class to statically access the output and the error console provider
 * @author NTT DATA
 */
public class ConsoleHolder {
	
	/** The current console provider */
	private static ConsoleProvider currentProvider;
	static {
		reset();
	}
	
	/**
	 * Resets the console provider
	 */
	public static void reset() {
		
		set(null);
	}
	
	/**
	 * Sets the current console provider
	 * @param provider ConsoleProvider
	 */
	public static void set(ConsoleProvider provider) {
		
		currentProvider = ObjectUtils.defaultIfNull(provider, ConsoleProvider.SYSTEM);
	}
	
	/**
	 * @return the current output console
	 */
	public static PrintStream getOut() {
		
		return ObjectUtils.firstNonNull(currentProvider.getOut(), ConsoleProvider.SYSTEM.getOut());
	}
	
	/**
	 * @return the current error console
	 */
	public static PrintStream getErr() {
		
		return ObjectUtils.firstNonNull(currentProvider.getErr(), ConsoleProvider.SYSTEM.getErr());
	}
}
