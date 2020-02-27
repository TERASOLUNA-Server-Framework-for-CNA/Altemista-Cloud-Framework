package cloud.altemista.fwk.test.rules;

/*-
 * #%L
 * altemista-cloud rules engine: Drools implementation CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = { "classpath:spring/jarClasspath/test-app-context-rules-jar-simple.xml" })
public class JarSimpleClasspathTest extends SimpleClasspathTest {
//We do the same test but with another spring who uses a jar version of the rules
}
