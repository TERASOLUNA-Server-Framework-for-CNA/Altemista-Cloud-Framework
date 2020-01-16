package com.mycompany.application;

import org.altemista.cloudfwk.config.boot.AltemistaCloudfwkApplicationBuilder;
import org.altemista.cloudfwk.config.boot.AltemistaCloudfwkApplicationServletInitializer;

/**
 * Main class to bootstrap and launch the Spring Boot-based ACF application
 */
public class Main extends AltemistaCloudfwkApplicationServletInitializer{
	
	/**
	 * Private default constructor (non instanceable class)
	
	private Main() {
		super();
	}
	 */
	/**
	 * Bootstraps and launches the Spring Boot-based ACF application
	 * @param args optional command line arguments
	 */
	public static void main(String[] args) {
		
		new AltemistaCloudfwkApplicationBuilder().run(args);
	}
}
