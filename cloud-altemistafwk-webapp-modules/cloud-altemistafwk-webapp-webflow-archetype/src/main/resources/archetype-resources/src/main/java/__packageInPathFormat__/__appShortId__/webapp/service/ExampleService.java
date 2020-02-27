package ${groupId}.${appShortId}.webapp.service;

import org.springframework.stereotype.Component;
import ${groupId}.${appShortId}.webapp.model.Example;

@Component("webflowExampleService")
public class ExampleService {
	
	public String exampleMethod(Example model) {
		return String.format("Hello, %s!", model.getName());
	}
}
