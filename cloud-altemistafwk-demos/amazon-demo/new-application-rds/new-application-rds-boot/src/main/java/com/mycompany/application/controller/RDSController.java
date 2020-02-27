package com.mycompany.application.controller;

import java.io.IOException;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.application.module.entity.tasks;
import com.mycompany.application.module.repository.TaskRepository;

@RestController
public class RDSController {
	
	@Autowired
	private TaskRepository repository;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void save (@RequestParam(value = "description") String description) throws IOException {
		
		repository.save(new tasks(description, new Timestamp(System.currentTimeMillis())));
	}
	
	@RequestMapping(value = "/getOne", method = RequestMethod.GET)
	public tasks getOne (@RequestParam(value = "description") String description) throws IOException {
		
		return repository.findByDescription(description);

	}
	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public Iterable<tasks> getAll () throws IOException {
		
		return repository.findAll();
		
	}

}
