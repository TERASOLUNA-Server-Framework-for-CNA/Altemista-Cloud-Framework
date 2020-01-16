package com.mycompany.application.module.model;

public class Employee {
	
	private String name;
	
	private String surname;
	
	private String gender;
	
	private String position;
	
	public Employee(String name, String surname, String gender, String position){
		
		this.name= name;
		this.surname = surname;
		this.gender = gender;
		this.position = position;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	

}
