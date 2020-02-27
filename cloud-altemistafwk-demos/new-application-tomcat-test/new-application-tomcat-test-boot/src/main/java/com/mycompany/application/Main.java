package com.mycompany.application;

import cloud.altemista.fwk.config.boot.CloudAltemistafwkApplicationBuilder;
import cloud.altemista.fwk.config.boot.CloudAltemistafwkApplicationServletInitializer;

/**
 * Main class to bootstrap and launch the Spring Boot-based ACF application
 */
public class Main extends CloudAltemistafwkApplicationServletInitializer{
	
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
		
		new CloudAltemistafwkApplicationBuilder().run(args);
	}
}
