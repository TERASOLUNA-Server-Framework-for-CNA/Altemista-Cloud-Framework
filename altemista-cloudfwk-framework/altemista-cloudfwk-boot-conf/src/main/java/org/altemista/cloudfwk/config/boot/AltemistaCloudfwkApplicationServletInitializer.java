package org.altemista.cloudfwk.config.boot;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.altemista.cloudfwk.core.AltemistaCloudfwkApplicationContextInitializer;


@Configuration
@Import(AltemistaCloudfwkApplication.class)
public class AltemistaCloudfwkApplicationServletInitializer extends SpringBootServletInitializer  {

	public SpringApplicationBuilder configure( SpringApplicationBuilder application) {
		application.initializers(new ApplicationContextInitializer[]{new AltemistaCloudfwkApplicationContextInitializer()});
		return application;
    }

}
