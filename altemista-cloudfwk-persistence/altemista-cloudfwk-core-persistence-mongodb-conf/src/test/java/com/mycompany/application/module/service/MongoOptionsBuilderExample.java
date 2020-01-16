package com.mycompany.application.module.service;

/*
 * #%L
 * altemista-cloud persistence: MongoDB (Spring Data) CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;

//tag::example[]
@Configuration
public class MongoOptionsBuilderExample {

	@Bean(name = "mongoOptions")
	public MongoClientOptions mongoOptions() {
		
		return MongoClientOptions.builder()
				.alwaysUseMBeans(false)
				.maxWaitTime(10000)
				.connectionsPerHost(10)
				.build();
	}

	@Bean(name = "mongoCredentials")
	public List<MongoCredential> mongoCredentials() {
		
		return Collections.singletonList(
				MongoCredential.createCredential("user", "database", "password".toCharArray())
			);
	}
}
// end::example[]
