package com.mycompany.application.microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.application.module.bus.BusService;

@RestController
public class MicroserviceController {
	
    @Autowired
    BusService service;

    @RequestMapping("/show")
    public String getMessage() {
        return service.getProperty();
    }
    @RequestMapping("/show2")
    public String getMessage2() {
    	return service.getProperty2();
    }
    @RequestMapping("/show3")
    public String getMessage3() {
    	return service.getProperty3();
    }

}
