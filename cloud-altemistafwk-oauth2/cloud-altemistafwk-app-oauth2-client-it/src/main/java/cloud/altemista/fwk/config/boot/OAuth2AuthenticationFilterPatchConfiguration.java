package cloud.altemista.fwk.config.boot;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import cloud.altemista.fwk.it.security.oauth2.controller.OAuth2ApiController;

/**
 * Spring Boot auto-registers the {@link OAuth2AuthenticationProcessingFilter} to {@code /*}
 * when using XML-based configuration (as ACF does).<br>
 * This set-up is valid if the application is only publishing OAuth 2.0 resources,
 * but will cause troubles if the application publishes additional resources
 * (such as static resources, controllers, SOAP web services,
 * or even if the application is also an OAuth 2.0 Authorization Server).<br>
 * This {@code @Configuration} class is automatically loaded when the environment is Spring Boot-based
 * and gets ignored otherwise.
 * @author NTT DATA
 */
@Configuration
public class OAuth2AuthenticationFilterPatchConfiguration {

	/**
	 * Manually registers the {@link OAuth2AuthenticationProcessingFilter} to the proper mapping
	 * to avoid Spring Boot auto-registering it to {@code /*}.<br>
	 * This bean definition mimics the solution proposed in the documentation:<pre>
&lt;bean class="org.springframework.boot.web.servlet.FilterRegistrationBean">
  &lt;property name="filter" ref="oauth2AuthenticationFilter" />
  &lt;property name="urlPatterns" value="/api/**" />
&lt;/bean></pre>
	 * @param filter OAuth2AuthenticationProcessingFilter to register
	 * @return FilterRegistrationBean
	 */
	@Bean
	public FilterRegistrationBean oauth2AuthenticationFilterRegistrationBean(
			@Autowired OAuth2AuthenticationProcessingFilter filter) {
		
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(filter);
		bean.setUrlPatterns(Collections.singleton(OAuth2ApiController.MAPPING + "/**"));
		return bean;
	}
}
