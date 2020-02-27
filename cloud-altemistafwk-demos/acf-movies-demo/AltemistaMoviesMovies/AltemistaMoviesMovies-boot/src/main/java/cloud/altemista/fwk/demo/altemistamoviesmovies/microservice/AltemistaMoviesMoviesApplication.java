package cloud.altemista.fwk.demo.altemistamoviesmovies.microservice;

import cloud.altemista.fwk.config.boot.CloudAltemistafwkApplicationBuilder;
import cloud.altemista.fwk.config.microservice.CloudAltemistafwkMicroserviceBuilder;
import cloud.altemista.fwk.config.microservice.CloudAltemistafwkMicroservice;

/**
 * The Class AltemistaMoviesMoviesApplication.
 */
@CloudAltemistafwkMicroservice
public class AltemistaMoviesMoviesApplication extends CloudAltemistafwkMicroserviceBuilder {

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        configureApplication(new CloudAltemistafwkApplicationBuilder(), AltemistaMoviesMoviesApplication.class).run(args);
    }
	
}
