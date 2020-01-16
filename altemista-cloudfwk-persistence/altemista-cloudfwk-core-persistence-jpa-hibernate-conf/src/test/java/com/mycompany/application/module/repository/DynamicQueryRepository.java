//tag::example[]
package com.mycompany.application.module.repository;
//end::example[]

/*
 * #%L
 * altemista-cloud persistence: JPA (Hibernate provider) CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */
//tag::example[]

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
//end::example[]
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.application.module.model.Demo;

@Transactional(propagation = Propagation.MANDATORY)
//tag::example[]
public interface DynamicQueryRepository extends QuerydslPredicateExecutor<Demo> {
	
	// (empty)
}
//end::example[]
