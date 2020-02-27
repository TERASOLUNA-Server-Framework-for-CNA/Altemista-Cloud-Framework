package com.mycompany.application.module.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class tasks {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer task_id;
	
	String description;
	
	Timestamp dateEvent;
	
	public tasks(){
		
	}
	
	public tasks(Integer task_id, String description, Timestamp dateEvent){
		 this.task_id = task_id;
		 this.description = description;
	     this.dateEvent = dateEvent;
	}
	
	public tasks(String description, Timestamp dateEvent){
		this.description = description;
		this.dateEvent = dateEvent;
	}

	public Integer getTask_id() {
		return task_id;
	}

	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getDateEvent() {
		return dateEvent;
	}

	public void setDateEvent(Timestamp dateEvent) {
		this.dateEvent = dateEvent;
	}
	
	

}
