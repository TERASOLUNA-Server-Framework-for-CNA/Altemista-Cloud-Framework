package com.mycompany.application.module.repository;

import org.springframework.data.repository.CrudRepository;

import com.mycompany.application.module.entity.tasks;

public interface TaskRepository extends CrudRepository<tasks, Integer> {
	
	public tasks findByDescription(String description);
	
}

