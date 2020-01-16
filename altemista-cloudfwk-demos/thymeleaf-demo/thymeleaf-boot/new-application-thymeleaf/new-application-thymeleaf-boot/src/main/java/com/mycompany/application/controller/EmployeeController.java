package com.mycompany.application.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;

import com.mycompany.application.module.form.EmployeeForm;
import com.mycompany.application.module.model.Employee;

@Controller
public class EmployeeController
{
//    private static List employeeList = new ArrayList();
    
    // Inject via application.properties
    @Value("${welcome.message}")
    private String message;
 
    @Value("${error.message}")
    private String errorMessage;
    
    private static List<Employee> employees = new ArrayList<Employee>();
    
    static {
    	employees.add(new Employee(1,"Bill", "Gates"));
    	employees.add(new Employee(2, "Steve", "Jobs"));
    }
    
    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model) {
 
        model.addAttribute("message", message);
 
        return "index";
    }

//    @RequestMapping(value = "/display", method = RequestMethod.POST)
//    public ModelAndView saveEmployee(@ModelAttribute Employee employee)
//    {
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("employeeDetails");
//        employeeList.add(employee);
//        mav.addObject("employeeList", employeeList);
//        return mav;
//    }
    

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
 
        if (firstName != null && firstName.length() > 0 //
                && lastName != null && lastName.length() > 0) {
            Employee newEmployee = new Employee(employees.size() + 1,firstName, lastName);
            employees.add(newEmployee);
 
            return "redirect:/employeeList";
        }
 
        model.addAttribute("errorMessage", errorMessage);
        return "addEmployee";
    }
}