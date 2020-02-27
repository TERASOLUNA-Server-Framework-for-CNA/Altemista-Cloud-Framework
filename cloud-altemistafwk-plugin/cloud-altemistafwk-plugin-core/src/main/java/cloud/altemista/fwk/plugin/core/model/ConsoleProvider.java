/**
 * 
 */
package cloud.altemista.fwk.plugin.core.model;

import java.io.PrintStream;

/**
 * Convenience interface for accessing the output and the error consoles
 * @author NTT DATA
 */
public interface ConsoleProvider {
	
	/**
	 * @return the output console
	 */
	PrintStream getOut();
	
	/**
	 * @return the error console
	 */
	PrintStream getErr();
	
	/**
	 * Constant ConsoleProvider instance that returns the System.out and System.err output streams
	 */
	ConsoleProvider SYSTEM = new ConsoleProvider() {
		
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.plugin.core.model.ConsoleProvider#getOut()
		 */
		@Override
		public PrintStream getOut() {
			
			return System.out;
		}
		
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.plugin.core.model.ConsoleProvider#getErr()
		 */
		@Override
		public PrintStream getErr() {
			
			return System.err;
		}
	};
}
