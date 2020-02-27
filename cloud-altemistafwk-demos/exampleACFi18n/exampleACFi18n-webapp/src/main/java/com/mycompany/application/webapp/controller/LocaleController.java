/**
 * Application-wide Spring MVC controllers, served at "/app/*".
 */
package com.mycompany.application.webapp.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
@Controller
public class LocaleController {
 
   private static final Logger logger = LoggerFactory.getLogger(LocaleController.class);
	
   @RequestMapping(value = "/locale", method = RequestMethod.GET)
   public String getLocalePage(Locale locale){
	   logger.info(" The client locale is {}.", locale);
       return "index.jsp";
   }
}