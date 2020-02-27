/**
 * Spring MVC controllers of the module business module, served at "/app/*".
 * I.e.: {@code @Conrtoller} annotated classes
 */
package com.mycompany.application.module.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mycompany.application.module.service.RabbitMQClient;

@RestController
public class WebController {
	
	@Autowired
	RabbitMQClient rabbitMQClient;
	
	
	// One to one example
	@RequestMapping(value="/send")
	public String send(@RequestParam("msg")String msg){
		
		rabbitMQClient.sendMessage(msg);
		
		return "Sent";
	}
	@RequestMapping(value="/recieve")
	public String recieve(){
		
		rabbitMQClient.receiveMessage();
		
		return "Recieved";
	}
	
	// Publish/subscribe example
	@RequestMapping(value="/publish")
	public String publish(@RequestParam("msg")String msg){
		
		rabbitMQClient.publishMessage(msg);
		
		return "Sent";
	}
	
	@RequestMapping(value="/subscribe")
	public String subscribe(){
		
		rabbitMQClient.subcribeMessages();;
		
		return "Sent";
	}
	
}