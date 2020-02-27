package cloud.altemista.fwk.demo.altemistamoviesusers.microservice;

import cloud.altemista.fwk.config.boot.CloudAltemistafwkApplicationBuilder;
import cloud.altemista.fwk.config.microservice.CloudAltemistafwkMicroserviceBuilder;
import cloud.altemista.fwk.config.microservice.CloudAltemistafwkMicroservice;

/**
 * The Class AltemistaMoviesUsersApplication.
 */
@CloudAltemistafwkMicroservice
public class AltemistaMoviesUsersApplication extends CloudAltemistafwkMicroserviceBuilder {

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        configureApplication(new CloudAltemistafwkApplicationBuilder(), AltemistaMoviesUsersApplication.class).run(args);
    }
	
}
