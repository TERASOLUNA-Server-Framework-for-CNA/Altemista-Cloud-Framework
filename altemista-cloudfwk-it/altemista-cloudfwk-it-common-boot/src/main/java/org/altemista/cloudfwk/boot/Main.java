/**
 * 
 */
package org.altemista.cloudfwk.boot;

import org.altemista.cloudfwk.config.boot.AltemistaCloudfwkApplicationBuilder;

/**
 * Main class to bootstrap and launch the Spring Boot application
 * @author NTT DATA
 */
public class Main {
	
	/**
	 * Private default constructor (non instanceable class)
	 */
	private Main() {
		super();
	}
	
	/**
	 * Bootstraps and launches the Spring Boot application
	 * @param args optional command line arguments
	 */
	public static void main(String[] args) {
		
		new AltemistaCloudfwkApplicationBuilder().run(args);
	}
}
