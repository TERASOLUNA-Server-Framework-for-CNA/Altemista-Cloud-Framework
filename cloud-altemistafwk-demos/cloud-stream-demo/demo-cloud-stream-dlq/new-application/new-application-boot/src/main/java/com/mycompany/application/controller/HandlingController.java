package com.mycompany.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.mycompany.application.module.stream.HandlingService;

@RestController
public class HandlingController {
	@Autowired
    private HandlingService handlingService;

    @GetMapping("/handling")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String send() {
    	handlingService.sendMessage(System.currentTimeMillis());
    	return "message sent";
    }
}