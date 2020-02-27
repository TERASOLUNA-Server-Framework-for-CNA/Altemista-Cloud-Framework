package com.mycompany.altemistamoviesui.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AltemistaMoviesUIController {
	
	@Autowired
	private UserController userController;
	
	@RequestMapping("/")
	public String getIndex(Model model){
		return userController.getIndex(model);
	}

}
