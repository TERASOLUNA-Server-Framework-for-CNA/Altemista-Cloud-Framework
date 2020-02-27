package cloud.altemista.fwk.test.proxy;

/*
 * #%L
 * altemista-cloud common: connectivity utilities
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.net.Proxy;
import java.net.URI;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import cloud.altemista.fwk.common.connection.model.ProxyBean;
import cloud.altemista.fwk.common.connection.util.ThreadLocalProxySelector;

public class ProxyTest {

	@Test
	public void example() {
		
		// tag::usage[]
		final ProxyBean proxy = new ProxyBean(); // <1>
		proxy.setHostname("8.8.8.8"); // <2>
		proxy.setPort(8080);
		proxy.setUsername("John Doe"); // <3>
		proxy.setPassword("********");
		
		// end::usage[]
		
		// (for coverage improvements)
		Assert.assertEquals("8.8.8.8", proxy.getHostname());
		Assert.assertEquals(8080, proxy.getPort());
		Assert.assertEquals("John Doe", proxy.getUsername());
		Assert.assertEquals("********", proxy.getPassword());
		
		// tag::usage[]
		ThreadLocalProxySelector.setProxy(proxy); // <4>
		try {
			// your connection here
			
			// end::usage[]
			
			// (for coverage improvements)
			List<Proxy> list = ThreadLocalProxySelector.getDefault().select(URI.create("http://localhost/"));
			Assert.assertNotNull(list);
			Assert.assertFalse(list.isEmpty());
			Assert.assertEquals(1, list.size());
			Assert.assertNotNull(CollectionUtils.extractSingleton(list));
			String proxyString = CollectionUtils.extractSingleton(list).toString();
			Assert.assertNotEquals("DIRECT", proxyString);
			Assert.assertTrue(StringUtils.contains(proxyString, "8.8.8.8"));
			
		// tag::usage[]
		} finally {
			ThreadLocalProxySelector.resetProxy(); // <5>
		}
		// end::usage[]
	}
}
