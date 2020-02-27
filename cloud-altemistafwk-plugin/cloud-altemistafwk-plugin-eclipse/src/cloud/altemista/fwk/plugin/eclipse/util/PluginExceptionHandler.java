package cloud.altemista.fwk.plugin.eclipse.util;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import cloud.altemista.fwk.plugin.core.exception.PluginException;
import cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler;

/**
 * Opens an error dialog to display an internal TSF+ plug-in exception, chaining all the known reasons
 * @author NTT DATA
 */
public class PluginExceptionHandler implements EclipseExceptionHandler.Handler {
	
	/** The plug-in ID */
	private final String pluginId;
	
	/**
	 * Constructor
	 * @param pPluginId The plug-in ID
	 */
	public PluginExceptionHandler(String pPluginId) {
		super();
		
		this.pluginId = StringUtils.defaultIfEmpty(pPluginId, "unknown");
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler.Handler#canHandle(java.lang.Throwable)
	 */
	@Override
	public boolean canHandle(Throwable t) {
		
		return t instanceof PluginException;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler.Handler#handle(org.eclipse.swt.widgets.Shell, java.lang.Throwable)
	 */
	@Override
	public Throwable handle(Shell shell, Throwable t) {
		
		// (sanity checks)
		if ((shell == null) || (!(t instanceof PluginException))) {
			return null;
		}
		
		// Chains all the reasons as statuses
		final PluginException e = (PluginException) t;
		IStatus status = null;
		if (e.getCause() == null) {
			status = new Status(IStatus.ERROR, this.pluginId, 0, e.getReason(), e);
			
		} else {
			status = new MultiStatus(this.pluginId, 0, e.getReason(), e);
			for (Throwable it = e.getCause(); it instanceof PluginException; it = it.getCause()) {
				final String reason = ((PluginException) it).getReason();
				((MultiStatus) status).add(new Status(IStatus.ERROR, this.pluginId, 0, reason, it));
			}
		}
		
		// Error dialog
		ErrorDialog.openError(shell, e.getClass().getName(), e.getShortMessage(), status);
		
		return null;
	}

}
