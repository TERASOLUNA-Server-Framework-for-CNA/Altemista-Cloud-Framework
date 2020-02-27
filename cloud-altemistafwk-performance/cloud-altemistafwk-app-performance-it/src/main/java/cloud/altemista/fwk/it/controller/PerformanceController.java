/**
 * 
 */
package cloud.altemista.fwk.it.controller;

/*
 * #%L
 * altemista-cloud performance module integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import cloud.altemista.fwk.core.performance.MeasuresStorage;
import cloud.altemista.fwk.core.performance.model.MeasuredTask;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * A controller with methods that illustrates the performance module
 * @author NTT DATA
 */
@Controller
@RequestMapping(value = PerformanceController.MAPPING, method = RequestMethod.GET)
public class PerformanceController {
	
	/** MAPPING String */
	public static final String MAPPING = "/measures";
	
	/** The methodExecutionMeasuresStorage MeasuresStorage */
	@Autowired
	@Qualifier("methodExecutionMeasuresStorageInternal")
	private MeasuresStorage methodExecutionMeasuresStorage;
	
	/** The jdbcMeasuresStorage MeasuresStorage */
	@Autowired
	@Qualifier("jdbcMeasuresStorageInternal")
	private MeasuresStorage jdbcMeasuresStorage;
	
	// Method execution
	
	/**
	 * Clears the information of the MeasuresStorage of service methods execution
	 * @return ModelAndView
	 */
	@RequestMapping("/method/clear")
	public ModelAndView clearMethodExecutionMeasuresStorageModelAndView() {
		
		this.methodExecutionMeasuresStorage.clear();
		return this.getMethodExecutionMeasuresStorageModelAndView();
	}
	
	/**
	 * Clears the information of the MeasuresStorage of service methods execution
	 * @return List of MeasuredTask
	 */
	@JsonView(MeasuredTask.View.class)
	@RequestMapping("/method/clear/json")
	@ResponseBody
	public List<MeasuredTask> clearMethodExecutionMeasuresStorage() {
		
		this.methodExecutionMeasuresStorage.clear();
		return this.getMethodExecutionMeasuresStorage();
	}
	
	/**
	 * Retrieves the information of the MeasuresStorage of service methods execution
	 * @return ModelAndView
	 */
	@RequestMapping("/method")
	public ModelAndView getMethodExecutionMeasuresStorageModelAndView() {
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "Method execution measures");
		model.put("tasks", this.getMethodExecutionMeasuresStorage());
		
		return new ModelAndView("tasks", model);
	}
	
	/**
	 * Retrieves the information of the MeasuresStorage of service methods execution
	 * @return List of MeasuredTask
	 */
	@JsonView(MeasuredTask.View.class)
	@RequestMapping("/method/json")
	@ResponseBody
	public List<MeasuredTask> getMethodExecutionMeasuresStorage() {
		
		return this.methodExecutionMeasuresStorage.get();
	}
	
	// JDBC
	
	/**
	 * Clears the information of the MeasuresStorage of JDBC
	 * @return ModelAndView
	 */
	@RequestMapping("/jdbc/clear")
	public ModelAndView clearJdbcMeasuresStorageModelAndView() {
		
		this.jdbcMeasuresStorage.clear();
		return this.getJdbcMeasuresStorageModelAndView();
	}
	
	/**
	 * Clears the information of the MeasuresStorage of JDBC
	 * @return List of MeasuredTask
	 */
	@JsonView(MeasuredTask.View.class)
	@RequestMapping("/jdbc/clear/json")
	@ResponseBody
	public List<MeasuredTask> clearJdbcMeasuresStorage() {
		
		this.jdbcMeasuresStorage.clear();
		return this.getJdbcMeasuresStorage();
	}
	
	/**
	 * Retrieves the information of the MeasuresStorage of JDBC
	 * @return ModelAndView
	 */
	@RequestMapping("/jdbc")
	public ModelAndView getJdbcMeasuresStorageModelAndView() {
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", "JDBC measures");
		model.put("tasks", this.getJdbcMeasuresStorage());
		
		return new ModelAndView("tasks", model);
	}
	
	/**
	 * Retrieves the information of the MeasuresStorage of JDBC
	 * @return List of MeasuredTask
	 */
	@JsonView(MeasuredTask.View.class)
	@RequestMapping("/jdbc/json")
	@ResponseBody
	public List<MeasuredTask> getJdbcMeasuresStorage() {
		
		return this.jdbcMeasuresStorage.get();
	}
}
