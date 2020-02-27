package ${package}.${appShortId}.${businessShortId}.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.spring.data.documentdb.repository.config.EnableDocumentDbRepositories;

@Configuration
@EnableDocumentDbRepositories(basePackages="${groupId}.${appShortId}.${businessShortId}.repository")
public class CosmosConfiguration  {

	@Bean(name = "cosmosdbObjectMapper")
	   public ObjectMapper objectMapper() {
	      return new ObjectMapper(); // Do configuration to the ObjectMapper if required
	   }
}
