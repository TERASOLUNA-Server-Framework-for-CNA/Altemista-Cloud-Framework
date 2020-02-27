package cloud.altemista.fwk.it.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

/*
 * #%L
 * cloud-altemistafwk core integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.terasoluna.gfw.common.codelist.CodeList;
import org.terasoluna.gfw.common.codelist.i18n.I18nCodeList;
import cloud.altemista.fwk.core.exception.CoreException;
import cloud.altemista.fwk.it.service.CoreDummyService;
import cloud.altemista.fwk.it.util.ITControllerUtil;

/**
 * Simple REST controller to check the application has been successfully deployed
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = AliveController.MAPPING, method = RequestMethod.GET)
public class AliveController {
	
	/** MAPPING String */
	public static final String MAPPING = "/alive";
	
	@Autowired
	private CoreDummyService dummyService;
	
	@Inject
	@Named("CL_I18N")
	private I18nCodeList i18nCodeList;
	
	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private MessageSource messageSource;
	
	/**
	 * Simple REST service to check the application has been successfully deployed 
	 * @return Object
	 */
	@RequestMapping
	public Object alive() {
		
		return "I'm alive!";
	}
	
	@RequestMapping("/2")
	public String exceptionLogger() {
		
		try {
			this.dummyService.alwaysThrowsApplicationExceptionWithMessage();
			return "";
			
		} catch (CoreException ex) {
			String exceptionMessage = this.i18nCodeList.asMap().get(ex.getCode());
			ITControllerUtil.assertTrue(
					StringUtils.equals("Dummy message", exceptionMessage)
					|| StringUtils.equals("Mensaje de prueba", exceptionMessage));
			throw ex;
		}
	}

	@RequestMapping("/3")
	public String i18nDefaultLocaleTest() {
		
		 final Locale locale = new Locale("en", "EN");
		 
		 String message = this.i18nCodeList.asMap(locale).get("e.cloud.altemista.fwk.core.exception.dummy");
		 ITControllerUtil.assertEquals("Dummy message", message);
		 return message;
	
	}
	
	@RequestMapping("/4")
	public String i18nTest() {
		
		final Locale locale = new Locale("es", "ES");
		
		String message = this.i18nCodeList.asMap(locale).get("e.cloud.altemista.fwk.core.exception.dummy");
		ITControllerUtil.assertEquals("Mensaje de prueba", message);
		return message;
	
	}
	
	
	@RequestMapping("/5")
	public String checkCodeListBeans() {
		
		final Locale locale = new Locale("en", "EN");

		Map<String, CodeList> definedCodeLists = BeanFactoryUtils.beansOfTypeIncludingAncestors(
				appContext, CodeList.class, false, false);

		Map<String, String> codeListMap = new HashMap<String, String>();
		for (CodeList codeList : definedCodeLists.values()) {
			if (codeList instanceof I18nCodeList) {
				I18nCodeList lI18nCodeList = (I18nCodeList) codeList;
				codeListMap = lI18nCodeList.asMap(locale);
			}
		}
		return codeListMap.toString();

	}
	
	@RequestMapping("/6")
	public String exceptionMessageSource() {
		
		final Locale locale = new Locale("es", "ES");
		
		try {
			this.dummyService.alwaysThrowsApplicationExceptionWithMessage();
			return "";
			
		} catch(CoreException ex) {
			String exceptionMessage = this.messageSource.getMessage(ex, locale);
			ITControllerUtil.assertEquals("Mensaje de prueba", exceptionMessage);
			throw ex;
		}
	}

}
