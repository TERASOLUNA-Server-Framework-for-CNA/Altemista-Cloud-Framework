package org.altemista.cloudfwk.demo.altemistamoviesmovies.microservice;

import org.altemista.cloudfwk.config.boot.AltemistaCloudfwkApplicationBuilder;
import org.altemista.cloudfwk.config.microservice.AltemistaCloudfwkMicroserviceBuilder;
import org.altemista.cloudfwk.config.microservice.AltemistaCloudfwkMicroservice;

/**
 * The Class AltemistaMoviesMoviesApplication.
 */
@AltemistaCloudfwkMicroservice
public class AltemistaMoviesMoviesApplication extends AltemistaCloudfwkMicroserviceBuilder {

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        configureApplication(new AltemistaCloudfwkApplicationBuilder(), AltemistaMoviesMoviesApplication.class).run(args);
    }
	
}
