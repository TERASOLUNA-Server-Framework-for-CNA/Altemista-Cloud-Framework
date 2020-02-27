package ${package}.${appShortId};

import cloud.altemista.fwk.config.boot.CloudAltemistafwkApplicationBuilder;

/**
 * Main class to bootstrap and launch the Spring Boot-based ACF application
 */
public class Main {
	
	/**
	 * Private default constructor (non instanceable class)
	 */
	private Main() {
		super();
	}
	
	/**
	 * Bootstraps and launches the Spring Boot-based ACF application
	 * @param args optional command line arguments
	 */
	public static void main(String[] args) {
		
		new CloudAltemistafwkApplicationBuilder().run(args);
	}
}
