package com.mycompany.application.module.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.application.module.service.IntegrationHTTPService;

@RestController
@RequestMapping("http")
public class IntegrationHTTPController {
	
	@Autowired
	private IntegrationHTTPService integrationHTTPService;

	@RequestMapping
	public String get() {
		return "OK!";
	}
	
	@RequestMapping("send")
	public ResponseEntity<Map<String,String>> send (@RequestParam String message) {
		Map<String, String> result = new HashMap<String,String>();
		String returnMessage = integrationHTTPService.send(message);
		result.put("message", returnMessage);
		return ResponseEntity.ok(result);
	}
	
}
