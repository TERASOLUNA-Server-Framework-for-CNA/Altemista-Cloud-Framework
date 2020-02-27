/**
 * Spring MVC controllers of the module business module, served at "/app/*".
 * I.e.: {@code @Conrtoller} annotated classes
 */
package com.mycompany.application.module.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mycompany.application.module.service.ActiveMQService;

@RestController
public class HomeController {
	
	@Autowired
	ActiveMQService jmsTemplate;
	
	// one to one example
	@RequestMapping(value="/send")
	public String send(@RequestParam("msg")String msg){
		
		jmsTemplate.sendMessage(msg);
		
		return "Sent";
	}
	@RequestMapping(value="/recieve")
	public String recieve(){
		
		jmsTemplate.receiveMessage();
		
		return "Recieved";
	}
	
	// Publish/subscribe example
	@RequestMapping(value="/publish")
	public String publish(@RequestParam("msg")String msg){
		
		jmsTemplate.publishMessage(msg);
		
		return "Sent";
	}
	
}