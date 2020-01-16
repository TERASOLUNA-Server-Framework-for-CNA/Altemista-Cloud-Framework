package com.mycompany.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.application.module.amazon.SimpleEmailService;


@RestController
public class HomeController {
	
	@Autowired
	SimpleEmailService mailSender;
	
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public String send(@RequestParam String message) {
		mailSender.sendMessage(message);
		
		return "message sent !";
	}
}
