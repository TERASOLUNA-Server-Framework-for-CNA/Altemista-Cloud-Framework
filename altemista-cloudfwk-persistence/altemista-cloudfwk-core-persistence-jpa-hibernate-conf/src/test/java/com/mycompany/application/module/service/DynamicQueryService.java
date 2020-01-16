package com.mycompany.application.module.service;

import java.util.Optional;

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


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.mycompany.application.module.model.Demo;
import com.mycompany.application.module.model.QDemo;
import com.mycompany.application.module.repository.DynamicQueryRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

public class DynamicQueryService {

	@Autowired
	private DynamicQueryRepository repository;
	
	//tag::example1[]
	public Optional<Demo> dynamicQueryExample(Long min, Long max, String namePart) {
		
		Predicate predicate = QDemo.demo.name.contains(namePart); //<1> <2>
		
		if (min != null) {
			predicate = QDemo.demo.id.goe(min).and(predicate); //<3>
		}
		if (max != null) {
			predicate = QDemo.demo.id.loe(max).and(predicate); //<3>
		}
		
		return this.repository.findOne(predicate);
	}
	// end::example1[]
	
	//tag::example3[]
	public Optional<Demo> findPersonNamed(String name) {
		return this.repository.findOne(
				DemoPredicates.nameEquals(name));
	}
	
	public Optional<Demo> findChildNamed(String name) {
		return this.repository.findOne(
				DemoPredicates.nameEquals(name)
					.and(DemoPredicates.isChild()));
	}
	// end::example3[]
	
	static
	//tag::example2[]
	public final class DemoPredicates {
		
		public static BooleanExpression nameEquals(String name) { //<1>
			return StringUtils.isBlank(name)
					? QDemo.demo.name.isNull()
					: QDemo.demo.name.eq(name); //<2>
		}
		
		// (...)
		// end::example2[]
		
		public static BooleanExpression isChild() { //<2>
			
			return QDemo.demo.age.lt(18);
		}
		
		private DemoPredicates() {}
		//tag::example2[]
	}
	// end::example2[]
}
