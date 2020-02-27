package com.mycompany.application.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.application.module.model.Employee;

@RestController
public class HelloController {

	@PreAuthorize("hasRole('demoboot')")
	@RequestMapping("/api/hello")
	public String helloWorlwd() {
		return "Hello World!";
	}

	@PreAuthorize("hasRole('Azure ECS')")
	@RequestMapping(value = "/api/findAll", method = RequestMethod.GET, produces = "application/json")
	public List<Employee> helloWorld() {
		List<Employee> employees = new ArrayList<>();

		for (int i = 0; i < 4; i++) {
			Employee employee = new Employee(i, "name" + i, "function" + i);
			employees.add(employee);
		}
		System.out.println(employees);

		return employees;
	}

}