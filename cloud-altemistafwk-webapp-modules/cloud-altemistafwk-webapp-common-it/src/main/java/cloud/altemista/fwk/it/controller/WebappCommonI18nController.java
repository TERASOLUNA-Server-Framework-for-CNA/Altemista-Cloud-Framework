package cloud.altemista.fwk.it.controller;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * #%L
 * altemista-cloud web integration tests
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
import org.terasoluna.gfw.common.codelist.i18n.I18nCodeList;
import cloud.altemista.fwk.core.exception.CoreException;
import cloud.altemista.fwk.core.i18n.DefaultLocaleMessageSource;
import cloud.altemista.fwk.it.service.CoreDummyService;
import cloud.altemista.fwk.it.util.ITControllerUtil;

/**
 * REST controller to check the web i18n
 * @author NTT DATA
 */
@RestController
@RequestMapping(value = WebappCommonI18nController.MAPPING, method = RequestMethod.GET)
public class WebappCommonI18nController {

	/** MAPPING String */
	public static final String MAPPING = "/webappcommoni18n";

	@Inject
	@Named("CL_I18N_PRICE")
	private I18nCodeList i18nCodeList;

	@Autowired
	private CoreDummyService dummyService;

	/** The message resource. */
	@Autowired
	DefaultLocaleMessageSource messageResource;

	@RequestMapping("/1")
	public Object checkInjecteI18nCodeList() {

		String price0 = i18nCodeList.asMap().get("price.0");
		String price10000 = i18nCodeList.asMap().get("price.10000");
		ITControllerUtil.assertEquals("unlimited", price0);
		ITControllerUtil.assertEquals("Less than \\10,000", price10000);
		return price10000;
	}

	@RequestMapping("/2")
	public Object checkRequestI18nCodeList(HttpServletRequest request) {

		@SuppressWarnings("unchecked")
		Map<String, String> codeListMap = (Map<String, String>) request.getAttribute("CL_I18N_PRICE");
		String price0 = codeListMap.get("price.0");
		String price10000 = codeListMap.get("price.10000");
		ITControllerUtil.assertEquals("unlimited", price0);
		ITControllerUtil.assertEquals("Less than \\10,000", price10000);
		return price10000;
	}

	@RequestMapping("/3")
	public Object checkMessageSource() {
		
		String message = messageResource.getMessage("e.cloud.altemista.fwk.core.exception.dummy", null);
		ITControllerUtil.assertTrue(
				StringUtils.equals(message, "Mensaje de prueba")
				|| StringUtils.equals(message, "Dummy message"));
		return message;
	}

	@RequestMapping("/codelist")
	public Object checkRequestI18nCodeListLangParam(HttpServletRequest request) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> codeListMap = (Map<String, String>) request.getAttribute("CL_I18N_PRICE");
		String price0 = codeListMap.get("price.0");
		String price10000 = codeListMap.get("price.10000");
		ITControllerUtil.assertEquals("unlimited", price0);
		ITControllerUtil.assertEquals("10,000\u5186\u4EE5\u4E0B", price10000);
		return price10000;
	}

	@RequestMapping("/msgsrc/expectes")
	public Object checkMessageSourceExpectSpanish(HttpServletRequest request) {
		
		String message = messageResource.getMessage("e.cloud.altemista.fwk.core.exception.dummy", null);
		ITControllerUtil.assertEquals("Mensaje de prueba", message);
		return message;
	}

	@RequestMapping({ "/msgsrc/expectdefault", "/msgsrc/expecten" })
	public Object checkMessageSourceExpectEnglish(HttpServletRequest request) {
		
		String message = messageResource.getMessage("e.cloud.altemista.fwk.core.exception.dummy", null);
		ITControllerUtil.assertEquals("Dummy message", message);
		return message;
	}

	@RequestMapping("/serviceException")
	public Object serviceException(HttpServletRequest request) {

		try {
			dummyService.alwaysThrowsApplicationExceptionWithMessage();
			return "";

		} catch (CoreException ex) {
			String exceptionMessage = messageResource.getMessage(ex.getCode(), null);
			ITControllerUtil.assertEquals("Dummy message", exceptionMessage);
			throw ex;
		}
	}

}
