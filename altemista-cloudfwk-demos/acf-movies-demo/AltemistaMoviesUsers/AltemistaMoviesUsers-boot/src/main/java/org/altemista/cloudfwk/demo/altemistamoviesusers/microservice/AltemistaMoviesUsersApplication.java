package org.altemista.cloudfwk.demo.altemistamoviesusers.microservice;

import org.altemista.cloudfwk.config.boot.AltemistaCloudfwkApplicationBuilder;
import org.altemista.cloudfwk.config.microservice.AltemistaCloudfwkMicroserviceBuilder;
import org.altemista.cloudfwk.config.microservice.AltemistaCloudfwkMicroservice;

/**
 * The Class AltemistaMoviesUsersApplication.
 */
@AltemistaCloudfwkMicroservice
public class AltemistaMoviesUsersApplication extends AltemistaCloudfwkMicroserviceBuilder {

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        configureApplication(new AltemistaCloudfwkApplicationBuilder(), AltemistaMoviesUsersApplication.class).run(args);
    }
	
}
