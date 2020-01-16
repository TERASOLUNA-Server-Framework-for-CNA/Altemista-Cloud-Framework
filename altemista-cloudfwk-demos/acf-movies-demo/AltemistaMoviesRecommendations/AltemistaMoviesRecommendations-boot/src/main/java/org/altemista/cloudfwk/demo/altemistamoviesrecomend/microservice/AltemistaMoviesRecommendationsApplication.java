package org.altemista.cloudfwk.demo.altemistamoviesrecomend.microservice;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.altemista.cloudfwk.config.boot.AltemistaCloudfwkApplicationBuilder;
import org.altemista.cloudfwk.config.microservice.AltemistaCloudfwkMicroservice;
import org.altemista.cloudfwk.config.microservice.AltemistaCloudfwkMicroserviceBuilder;
import org.altemista.cloudfwk.demo.altemistamoviesmovies.microservice.service.AltemistaMoviesMovies;
import org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.service.AltemistaMoviesUsers;

/**
 * The Class AltemistaMoviesRecommendationsApplication.
 */
@AltemistaCloudfwkMicroservice
@EnableFeignClients(basePackageClasses = {AltemistaMoviesUsers.class, AltemistaMoviesMovies.class}) 
public class AltemistaMoviesRecommendationsApplication extends AltemistaCloudfwkMicroserviceBuilder {

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        configureApplication(new AltemistaCloudfwkApplicationBuilder(), AltemistaMoviesRecommendationsApplication.class).run(args);
    }
	
}
