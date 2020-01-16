package com.mycompany.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.application.module.service.SimpleDatabaseService;


@RestController
public class HomeController {
	
	@Autowired
	SimpleDatabaseService simpleDateBase;

	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public Integer home() {
		return simpleDateBase.countTasks();
		
	}
}
