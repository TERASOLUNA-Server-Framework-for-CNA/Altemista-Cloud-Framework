// tag::annotation[]
package cloud.altemista.fwk.config.microservice.gateway.filters;
// end::annotation[]

/*-
 * #%L
 * altemista-cloud Microservices Gateway CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

// tag::annotation[]
import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class CloudAltemistafwkMicroserviceGatewayAuthenticationFilter extends ZuulFilter {
	
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		ctx.addZuulRequestHeader("Authorization", request.getHeader("Authorization")); // <1>
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return "pre";
	}
}
// end::annotation[]
