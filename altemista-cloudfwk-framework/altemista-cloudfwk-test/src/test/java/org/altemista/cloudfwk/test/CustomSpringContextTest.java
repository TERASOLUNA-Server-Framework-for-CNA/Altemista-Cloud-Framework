package org.altemista.cloudfwk.test;

/*
 * #%L
 * Test dependency for altemista-cloud framework and application unit tests
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;


/**
 * Tests the convenience base class for unit tests that needs a ACF Spring context.
 * @author NTT DATA
 */
// tag::example[]
@ContextConfiguration("classpath:spring/example.xml") //<1>
public class CustomSpringContextTest extends AbstractSpringContextTest {

	@Test
	public void test() {
		// (...)
	}
}
// end::example[]
