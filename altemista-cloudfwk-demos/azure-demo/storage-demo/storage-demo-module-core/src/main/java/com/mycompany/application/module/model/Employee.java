package com.mycompany.application.module.model;

public class Employee {
	int id;
	String name;
	String function;
	
	public Employee(int id, String name, String function) {
		super();
		this.id = id;
		this.name = name;
		this.function = function;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	
	
}
