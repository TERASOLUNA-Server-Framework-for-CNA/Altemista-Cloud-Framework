/**
 * 
 */
package cloud.altemista.fwk.plugin.eclipse.util;

import java.io.PrintStream;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import cloud.altemista.fwk.plugin.core.model.ConsoleProvider;

/**
 * ConsoleProvider that returns a new PrintStream to a new MessageStream of an Eclipse console
 * @author NTT DATA
 */
public class EclipseConsoleProvider implements ConsoleProvider {
	
	/** The console name */
	private final String consoleName;
	
	/**
	 * Constructor
	 * @param consoleName The console name
	 */
	public EclipseConsoleProvider(String consoleName) {
		super();
		
		this.consoleName = consoleName;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.model.ConsoleProvider#getOut()
	 */
	@Override
	public PrintStream getOut() {
		
		return new PrintStream(this.getConsole().newMessageStream());
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.plugin.core.model.ConsoleProvider#getErr()
	 */
	@Override
	public PrintStream getErr() {
		
		return new PrintStream(this.getConsole().newMessageStream());
	}
	
	/**
	 * Retrieves (or creates) the console
	 * @return MessageConsole
	 */
	private MessageConsole getConsole() {
		
		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
		
		// Looks for an existing console
		for (IConsole console : consoleManager.getConsoles()) {
			if (StringUtils.equals(console.getName(), this.consoleName)) {
				consoleManager.showConsoleView(console);
				return (MessageConsole) console;
			}
		}
		
		// Creates the new console
		MessageConsole console = new MessageConsole(this.consoleName, null);
		consoleManager.addConsoles(new IConsole[]{ console });
		return console;
	}
}
