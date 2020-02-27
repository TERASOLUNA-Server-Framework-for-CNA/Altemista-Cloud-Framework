package com.mycompany.application.module.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.application.module.service.IntegrationAMQPService;

@RestController
@RequestMapping("amqp")
public class IntegrationAMQPController {
	
	@Autowired 
	private IntegrationAMQPService service;

	@RequestMapping
	public ResponseEntity<Map<String,String>> sendMessage(String message) {
		Map<String,String> result = new HashMap<String,String>();
		service.sendMessage(message);
		result.put("result", "Message sended!!!");
		return ResponseEntity.ok(result);
	}
}
