//tag::example[]
package com.mycompany.application.module.model;
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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
//tag::example[]

@Entity // <1>
@Table(name = "DEMO_TABLE") // <2>
public class Demo implements Serializable {
	
	private static final long serialVersionUID = 4679200693272076037L;

	@Id // <4>
	@Column(name = "IDENTIFIER", nullable = false, unique = true) // <3>
	private Long id;
	
	@Column(name = "DESCRIPTION") // <3>
	private String name;
	
	@Column(name = "AGE") // <3>
	private Integer age;
	//end::example[]

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
//tag::example[]
}
//end::example[]
