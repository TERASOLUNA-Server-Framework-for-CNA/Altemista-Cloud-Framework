package com.mycompany.application.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.application.module.entity.User;
import com.mycompany.application.module.repository.UserRepository;
import com.google.common.collect.Lists;

@RestController
public class Controller {

	@Autowired
	private UserRepository repository;
	
	User testUser;

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String newIdentity(@RequestParam String firstName , @RequestParam String lastName ) {
		// Create a simple date/time ID.
		SimpleDateFormat userId = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date currentDate = new Date();
		// Create a new User class.
		testUser = new User(userId.format(currentDate), firstName, lastName);
		// Save the User class to the Azure database.Controlle
		repository.save(testUser);
		return "IdentityCreated";
	}

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public List<User> readIdentity() {
		// Retrieve all the database records.
		final Iterable<User> result = repository.findAll();
		// Display the results of the database record retrieval.
		return Lists.newArrayList(result);
	}
}
