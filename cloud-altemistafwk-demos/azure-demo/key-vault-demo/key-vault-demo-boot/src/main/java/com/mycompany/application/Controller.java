package com.mycompany.application;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	 @Value("${connectionString}")
	 private String connectionString;
	
	 @GetMapping(value = "/read")
	    public String write() throws IOException {
	       return String.format("\nConnection String stored in Azure Key Vault:\n%s\n",connectionString);
	    }
}
