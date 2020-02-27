package cloud.altemista.fwk.config.boot;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import cloud.altemista.fwk.core.CloudAltemistafwkApplicationContextInitializer;


@Configuration
@Import(CloudAltemistafwkApplication.class)
public class CloudAltemistafwkApplicationServletInitializer extends SpringBootServletInitializer  {

	public SpringApplicationBuilder configure( SpringApplicationBuilder application) {
		application.initializers(new ApplicationContextInitializer[]{new CloudAltemistafwkApplicationContextInitializer()});
		return application;
    }

}
