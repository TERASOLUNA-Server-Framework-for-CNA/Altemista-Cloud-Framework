package cloud.altemista.fwk.common.eclipse.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Convenience class to statically handle Exceptions thrown during the plug-in execution in an extensible manner.
 * @author NTT DATA
 */
public final class EclipseExceptionHandler {
	
	/** The current EclipseErrorHandler */
	private static List<Handler> handlers = new ArrayList<Handler>();
	
	/**
	 * Removes all the registered EclipseErrorHandler
	 */
	public static final void clear() {
		
		handlers.clear();
	}
	
	/**
	 * Registers the default EclipseErrorHandler
	 * @param pPluginId The plug-in ID
	 * @see InvocationTargetExceptionHandler
	 * @see CoreExceptionHandler
	 * @see DefaultExceptionHandler
	 */
	public static final void registerDefaults(String pPluginId) {
		
		final String pluginId = StringUtils.defaultIfEmpty(pPluginId, "unknown");
		
		register(InvocationTargetExceptionHandler.INSTANCE);
		register(CoreExceptionHandler.INSTANCE);
		register(new DefaultExceptionHandler(pluginId));
	}
	
	/**
	 * Registers an EclipseErrorHandler
	 * @param handler the EclipseErrorHandler to register
	 */
	public static final void register(Handler handler) {
		
		// (sanity checks)
		if (handler == null) {
			return;
		}
		
		handlers.add(handler);
	}
	
	/**
	 * Opens an error dialog to display a generic Throwable, delegating to the specialized versions
	 * if the exception is InvocationTargetException, CoreExcpetion or PluginException
	 * @param shell Shell
	 * @param e Throwable to handle
	 * @return void 
	 */
	public static final void handle(Shell shell, Throwable pe) {
		
		// (sanity checks)
		if ((shell == null) || (pe == null)) {
			return;
		}
		
		// Tries all the handlers in order
		Throwable e = pe;
		loop: while (e != null) {
			for (Handler handler : EclipseExceptionHandler.handlers) {
				if (!handler.canHandle(e)) {
					continue;
				}
				
				// Handles the exception, and restart the loop if there is a chained exception
				e = handler.handle(shell, e);
				continue loop;
			}
			
			// If no handler was able to handle the exception, uses the default handler as a fallback
			if (e != null) {
				DefaultExceptionHandler.handle(shell, "unknown", e);
				break;
			}
		}
	}
	
	/**
	 * Private default constructor (non-instanceable class)
	 */
	private EclipseExceptionHandler() {
		super();
	}
	
	/**
	 * Defines how to handle Exceptions thrown during the plug-in execution.
	 * For example, opening an error dialog, unwrapping a more detailed cause, etc.
	 * @author NTT DATA
	 */
	public static interface Handler {
		
		/**
		 * Checks if this handler is able to handle one specific Throwable
		 * @param t Throwable to be handled
		 * @return true if this instanc
		 */
		boolean canHandle(Throwable t);
		
		/**
		 * Handles the Throwable
		 * @param shell Shell
		 * @param t Throwable to be handled
		 * @return null, or the Throwable to be handled instead of the original Throwable
		 * (e.g. when the handler knows how to unwrap the cause of the original Throwable)
		 */
		Throwable handle(Shell shell, Throwable t);
	}
	
	/**
	 * Handles an InvocationTargetException
	 * (usually as the result of executing <code>IRunnableContext.run()</code> inside a Wizard)
	 * returning the target exception to be processed instead.
	 * @author NTT DATA
	 */
	public static final class InvocationTargetExceptionHandler implements Handler {
		
		/** The singleton instance */
		public static final Handler INSTANCE = new InvocationTargetExceptionHandler();
		
		/**
		 * Private default constructor (singleton)
		 */
		private InvocationTargetExceptionHandler() {
			super();
		}
		
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler.Handler#canHandle(java.lang.Throwable)
		 */
		@Override
		public boolean canHandle(Throwable t) {
			
			return t instanceof InvocationTargetException;
		}

		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler.Handler#handle(org.eclipse.swt.widgets.Shell, java.lang.Throwable)
		 */
		@Override
		public Throwable handle(Shell shell, Throwable t) {
			
			final InvocationTargetException e = (InvocationTargetException) t;
			return e.getTargetException();
		}
	}
	
	/**
	 * Opens an error dialog to display a CoreException, probably from other Eclipse plug-ins
	 * @author NTT DATA
	 */
	public static final class CoreExceptionHandler implements Handler {
		
		/** The singleton instance */
		public static final Handler INSTANCE = new CoreExceptionHandler();
		
		/**
		 * Private default constructor (singleton)
		 */
		private CoreExceptionHandler() {
			super();
		}
		
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler.Handler#canHandle(java.lang.Throwable)
		 */
		@Override
		public boolean canHandle(Throwable t) {
			
			return t instanceof CoreException;
		}

		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler.Handler#handle(org.eclipse.swt.widgets.Shell, java.lang.Throwable)
		 */
		@Override
		public Throwable handle(Shell shell, Throwable t) {
			
			// (sanity checks)
			if ((shell == null) || (!(t instanceof CoreException))) {
				return null;
			}
			
			// Opens an error dialog to display a CoreException
			final CoreException e = (CoreException) t;
			final String name = e.getClass().getName();
			ErrorDialog.openError(shell, name, StringUtils.defaultIfEmpty(e.getMessage(), name), e.getStatus());
			
			return null;
		}
	}
	
	/**
	 * Opens an error dialog to display a generic Throwable
	 * @author NTT DATA
	 */
	public static final class DefaultExceptionHandler implements Handler {
		
		/** The plug-in ID */
		private final String pluginId;
		
		/**
		 * Constructor
		 * @param pluginId The plug-in ID
		 */
		public DefaultExceptionHandler(String pluginId) {
			super();
			
			this.pluginId = pluginId;
		}
		
		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler.Handler#canHandle(java.lang.Throwable)
		 */
		@Override
		public boolean canHandle(Throwable t) {
			
			return t != null;
		}

		/* (non-Javadoc)
		 * @see cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler.Handler#handle(org.eclipse.swt.widgets.Shell, java.lang.Throwable)
		 */
		@Override
		public Throwable handle(Shell shell, Throwable t) {
			
			DefaultExceptionHandler.handle(shell, this.pluginId, t);
			return null;
		}
		
		/**
		 * Handles the Throwable.
		 * Convenience static method that can be used as a fallback.
		 * @param shell Shell
		 * @param pPluginId The plug-in ID
		 * @param t Throwable to be handled
		 */
		public static final void handle(Shell shell, String pPluginId, Throwable t) {
			
			// (sanity checks)
			if ((shell == null) || (t == null)) {
				return;
			}
			final String pluginId = StringUtils.defaultIfEmpty(pPluginId, "unknown");
			
			// Error dialog
			final String name = t.getClass().getName();
			final String message = StringUtils.defaultIfEmpty(t.getMessage(), name);
			final IStatus status = new MultiStatus(pluginId, 0, message, t);
			for (StackTraceElement trace : t.getStackTrace()) {
				((MultiStatus) status).add(new Status(IStatus.ERROR, pluginId, trace.toString()));
			}
	
			ErrorDialog.openError(shell, name, "Unexpected " + name, status);
		}
	}
}
