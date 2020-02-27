/**
 * Interfaces of the Spring Data JPA repositories of module.
 */
package com.mycompany.application.module.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.mycompany.application.module.model.Demo;

public interface DemoRepository extends CrudRepository<Demo, Long> { 

  List<Demo> findByNameContains(String name); 
}