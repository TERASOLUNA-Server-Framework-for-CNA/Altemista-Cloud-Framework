package com.mycompany.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.application.module.amazon.SqsQueueReciever;
import com.mycompany.application.module.amazon.SqsQueueSender;


@RestController
public class HomeController {
	
	@Autowired
	SqsQueueSender mailSender;
	
	@Autowired
	SqsQueueReciever mailReciever;

	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public String send(@RequestParam String message) {
		mailSender.send(message);
		
		return "message sent !";
	}
	@RequestMapping(value = "/recieve", method = RequestMethod.GET)
	public String recieve() {
		String message = mailReciever.recieve();
		
		return "message recieve : " + message;
	}
}
