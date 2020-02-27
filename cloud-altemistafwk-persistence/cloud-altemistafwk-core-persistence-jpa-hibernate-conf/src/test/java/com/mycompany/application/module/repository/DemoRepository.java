//tag::example[]
package com.mycompany.application.module.repository;
//end::example[]

import java.util.List;

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
import org.springframework.data.repository.CrudRepository;

//end::example[]
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.application.module.model.Demo;

@Transactional(propagation = Propagation.MANDATORY)
//tag::example[]
public interface DemoRepository extends CrudRepository<Demo, Long> { // <1>

	List<Demo> findByNameContains(String name); // <2>
}
//end::example[]
