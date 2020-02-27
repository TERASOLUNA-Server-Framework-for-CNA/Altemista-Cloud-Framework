package cloud.altemista.fwk.demo.altemistamoviesrecomend.microservice;

import org.springframework.cloud.openfeign.EnableFeignClients;
import cloud.altemista.fwk.config.boot.CloudAltemistafwkApplicationBuilder;
import cloud.altemista.fwk.config.microservice.CloudAltemistafwkMicroservice;
import cloud.altemista.fwk.config.microservice.CloudAltemistafwkMicroserviceBuilder;
import cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.service.AltemistaMoviesMovies;
import cloud.altemista.fwk.demo.altemistamoviesusers.microservice.service.AltemistaMoviesUsers;

/**
 * The Class AltemistaMoviesRecommendationsApplication.
 */
@CloudAltemistafwkMicroservice
@EnableFeignClients(basePackageClasses = {AltemistaMoviesUsers.class, AltemistaMoviesMovies.class}) 
public class AltemistaMoviesRecommendationsApplication extends CloudAltemistafwkMicroserviceBuilder {

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        configureApplication(new CloudAltemistafwkApplicationBuilder(), AltemistaMoviesRecommendationsApplication.class).run(args);
    }
	
}
