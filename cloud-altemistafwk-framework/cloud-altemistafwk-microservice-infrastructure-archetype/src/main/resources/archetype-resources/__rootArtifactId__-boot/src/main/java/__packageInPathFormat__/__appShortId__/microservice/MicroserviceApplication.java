package ${package}.${appShortId}.microservice;

import cloud.altemista.fwk.config.boot.CloudAltemistafwkApplicationBuilder;
import cloud.altemista.fwk.config.microservice.CloudAltemistafwkMicroserviceBuilder;
import cloud.altemista.fwk.config.microservice.CloudAltemistafwkMicroservice;

@CloudAltemistafwkMicroservice
public class MicroserviceApplication extends CloudAltemistafwkMicroserviceBuilder {

    public static void main(String[] args) {
        configureApplication(new CloudAltemistafwkApplicationBuilder(), MicroserviceApplication.class).run(args);
    }
	
}
