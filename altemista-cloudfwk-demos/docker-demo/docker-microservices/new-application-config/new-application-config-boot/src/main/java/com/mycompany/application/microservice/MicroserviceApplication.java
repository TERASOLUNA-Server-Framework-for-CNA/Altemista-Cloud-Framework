package com.mycompany.application.microservice;

import org.altemista.cloudfwk.config.boot.AltemistaCloudfwkApplicationBuilder;
import org.altemista.cloudfwk.config.microservice.AltemistaCloudfwkMicroserviceBuilder;
import org.altemista.cloudfwk.config.microservice.AltemistaCloudfwkMicroservice;

@AltemistaCloudfwkMicroservice
public class MicroserviceApplication extends AltemistaCloudfwkMicroserviceBuilder {

    public static void main(String[] args) {
        configureApplication(new AltemistaCloudfwkApplicationBuilder(), MicroserviceApplication.class).run(args);
    }
	
}
