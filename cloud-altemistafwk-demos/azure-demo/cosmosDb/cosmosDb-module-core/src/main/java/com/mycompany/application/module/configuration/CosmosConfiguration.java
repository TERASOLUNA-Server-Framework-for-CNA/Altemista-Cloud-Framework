package com.mycompany.application.module.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.spring.data.documentdb.repository.config.EnableDocumentDbRepositories;
import com.mycompany.application.module.repository.UserRepository;

@Configuration
@EnableDocumentDbRepositories(basePackages="com.mycompany.application.module.repository") 
public class CosmosConfiguration  {

	@Bean(name = "cosmosdbObjectMapper")
	   public ObjectMapper objectMapper() {
	      return new ObjectMapper(); // Do configuration to the ObjectMapper if required
	   }
}
