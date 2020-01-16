/**
 * Interfaces of the exposed services of module.
 */
package com.mycompany.application.module.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.application.module.model.Demo;
import com.mycompany.application.module.repository.DemoRepository;

@Service
@Transactional 
public class DemoService {

  @Autowired
  private DemoRepository repository; 

  public Iterable<Demo> search(String fragment) {
    return this.repository.findByNameContains(fragment); 
  }
}