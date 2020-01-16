package org.altemista.cloudfwk.it.integration.ws.soap.impl;

import org.springframework.stereotype.Service;
import org.altemista.cloudfwk.it.integration.ws.soap.CustomWebservice;

@Service("customWebservice")
public class CustomWebserviceImpl implements CustomWebservice {

	@Override
	public String testWebserviceMethod(String param) {
		return "Web Service Response: " + param;
	}

}
