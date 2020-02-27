package cloud.altemista.fwk.plugin.eclipse;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import cloud.altemista.fwk.plugin.core.util.ConsoleHolder;
import cloud.altemista.fwk.common.eclipse.util.EclipseExceptionHandler;
import cloud.altemista.fwk.plugin.eclipse.util.EclipseConsoleProvider;
import cloud.altemista.fwk.plugin.eclipse.util.PluginExceptionHandler;

/**
 * The activator class controls the plug-in life cycle
 * @author NTT DATA
 */
public class Activator extends AbstractUIPlugin {

	/** The plug-in ID */
	public static final String PLUGIN_ID = "cloud-altemistafwk-plugin-eclipse"; //$NON-NLS-1$

	/** The Eclipse console provider for the plug-in to use */
	private static final EclipseConsoleProvider CONSOLE_PROVIDER =
			new EclipseConsoleProvider("New ALTEMISTA Cloud Framework (ACF) application");

	/** The shared instance */
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		// Sets the proper console as the plug-in console
		ConsoleHolder.set(CONSOLE_PROVIDER);
		
		// Sets the proper exception handlers (including PluginExceptionHandler)
		EclipseExceptionHandler.clear();
		EclipseExceptionHandler.register(new PluginExceptionHandler(PLUGIN_ID));
		EclipseExceptionHandler.registerDefaults(PLUGIN_ID);
		
		Activator.plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		
		Activator.plugin.getImageRegistry().dispose();
		Activator.plugin = null;
		
		// Resets the plug-in console
		ConsoleHolder.reset();
		
		// Resets the exception handlers
		EclipseExceptionHandler.clear();
		
		super.stop(context);
	}
	
	/**
	 * Convenience method to retrieve plug-in images from the ImageRegistry 
	 * @param imageFilePath String
	 * @return Image
	 */
	public static Image getImage(String imageFilePath) {
		
		ImageRegistry imageRegistry = Activator.plugin.getImageRegistry();
		
		// Gets the image from the image registry
		Image image = imageRegistry.get(imageFilePath);
		if (image != null) {
			return image;
		}
		
		// Loads the image and puts the image into the image registry
		ImageDescriptor imageDescriptor = imageDescriptorFromPlugin(PLUGIN_ID, imageFilePath);
		imageRegistry.put(imageFilePath, imageDescriptor);
		return imageRegistry.get(imageFilePath);
	}

//	/**
//	 * Returns the shared instance
//	 * @return the shared instance
//	 */
//	public static Activator getDefault() {
//		return plugin;
//	}
//
//	/**
//	 * Returns an image descriptor for the image file at the given plug-in relative path
//	 * @param path the path
//	 * @return the image descriptor
//	 */
//	public static ImageDescriptor getImageDescriptor(String path) {
//		return imageDescriptorFromPlugin(PLUGIN_ID, path);
//	}
}
