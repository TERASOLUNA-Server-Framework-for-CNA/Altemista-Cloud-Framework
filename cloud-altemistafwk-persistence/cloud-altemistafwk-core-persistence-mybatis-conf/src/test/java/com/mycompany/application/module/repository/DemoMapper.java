//tag::example[]
package com.mycompany.application.module.repository;
//end::example[]

/*
 * #%L
 * altemista-cloud persistence: MyBatis CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

//tag::example[]

public interface DemoMapper {
	
	String selectSalutation(String name);
}
//end::example[]
