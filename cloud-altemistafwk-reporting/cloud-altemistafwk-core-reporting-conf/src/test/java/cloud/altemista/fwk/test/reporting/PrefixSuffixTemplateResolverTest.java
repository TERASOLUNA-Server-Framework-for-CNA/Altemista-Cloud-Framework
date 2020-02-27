package cloud.altemista.fwk.test.reporting;

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
import cloud.altemista.fwk.core.reporting.TemplateResolver;
import cloud.altemista.fwk.test.AbstractSpringContextTest;

/**
 * Test the default template resolver implementation
 * @author NTT DATA
 */
@ContextConfiguration("classpath:spring/cloud-altemistafwk-test-reporting.xml")
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
