package org.altemista.cloudfwk.test.reporting;

/*
 * #%L
 * altemista-cloud reporting CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.altemista.cloudfwk.core.reporting.TemplateResolver;
import org.altemista.cloudfwk.test.AbstractSpringContextTest;

/**
 * Test the default template resolver implementation
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/altemista-cloudfwk-test-reporting.xml")
public class PrefixSuffixTemplateResolverTest extends AbstractSpringContextTest {

	@Autowired
	private TemplateResolver templateResolver;
	
	@Test
	public void sanityChecks() {
		
		Assert.assertNotNull(templateResolver);
		Assert.assertNull(templateResolver.getTemplate(null));
		Assert.assertNull(templateResolver.getTemplate(""));
		Assert.assertNull(templateResolver.getTemplate("   "));
	}
	
	@Test
	public void testTemplateResolver() {
		
		Assert.assertNotNull(templateResolver.getTemplate("blank"));
	}
}
