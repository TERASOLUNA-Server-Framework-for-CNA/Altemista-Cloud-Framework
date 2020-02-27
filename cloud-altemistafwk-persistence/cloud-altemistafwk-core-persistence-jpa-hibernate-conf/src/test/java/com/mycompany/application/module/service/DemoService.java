package com.mycompany.application.module.service;

import java.util.Optional;

/*-
 * #%L
 * altemista-cloud persistence: JPA (Hibernate provider) CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.application.module.model.Demo;
import com.mycompany.application.module.repository.DemoRepository;

//tag::example[]
@Service
@Transactional //<1>
public class DemoService {

	@Autowired
	private DemoRepository repository; //<2>
	
	public Optional<Demo> get(Long id) {
		return this.repository.findById(id); //<3>
	}
	
	public Iterable<Demo> search(String fragment) {
		return this.repository.findByNameContains(fragment); //<4>
	}
}
//end::example[]
