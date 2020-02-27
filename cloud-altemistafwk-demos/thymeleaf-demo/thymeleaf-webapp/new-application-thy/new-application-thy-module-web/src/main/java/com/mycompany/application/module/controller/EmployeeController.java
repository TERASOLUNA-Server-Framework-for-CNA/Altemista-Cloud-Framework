package com.mycompany.application.module.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mycompany.application.module.form.EmployeeForm;
import com.mycompany.application.module.model.Employee;

@Controller
public class EmployeeController
{
    
    @Value("${welcome.message}")
    private String message;
 
    @Value("${error.message}")
    private String errorMessage;
    
    private static List<Employee> employees = new ArrayList<Employee>();
    
    static {
    	employees.add(new Employee("Bill", "Gates", "M","1"));
    	employees.add(new Employee("Marie", "Curie", "F", "2"));
    }
    
    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model) {
 
        model.addAttribute("message", message);
 
        return "index";
    }


    @RequestMapping(value = { "/employeeList" }, method = RequestMethod.GET)
    public String employeeList(Model model) {
    	
        model.addAttribute("employees", employees);
 
        return "employeeList";
    }
 
    @RequestMapping(value = { "/addEmployee" }, method = RequestMethod.GET)
    public String showAddEmployeePage(Model model) {
 
        EmployeeForm employeeForm = new EmployeeForm();
        model.addAttribute("employeeForm", employeeForm);
 
        return "addEmployee";
    }
 
    @RequestMapping(value = { "/addEmployee" }, method = RequestMethod.POST)
    public String saveEmployee(Model model, //
            @ModelAttribute("employeeForm") EmployeeForm employeeForm) {
 
        String firstName = employeeForm.getName();
        String lastName = employeeForm.getSurname();
        String gender = employeeForm.getGender();
        String position = employeeForm.getPosition();
 
        if (firstName != null && firstName.length() > 0 //
                && lastName != null && lastName.length() > 0
        	&& position != null && position.length() > 0) {
            Employee newEmployee = new Employee(firstName, lastName, gender, position);
            employees.add(newEmployee);
 
            return "redirect:/employeeList";
        }
 
        model.addAttribute("errorMessage", errorMessage);
        return "addEmployee";
    }
}