/**
 * Spring MVC controllers of the module business module, served at "/app/*".
 * I.e.: {@code @Conrtoller} annotated classes
 */
package com.mycompany.application.module.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mycompany.application.module.model.Demo;
import com.mycompany.application.module.service.DemoService;

@RestController
public class HomeController {
	
	@Autowired
	DemoService service; 

	@RequestMapping("/search")
	public String search(String name) {
		Iterable<Demo> list = service.search(name);
		return list.toString();
	}
}