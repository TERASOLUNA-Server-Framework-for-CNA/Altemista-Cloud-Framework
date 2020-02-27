package cloud.altemista.fwk.it.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/*
 * #%L
 * altemista-cloud web application: JSP implementation (with Apache Tiles) integration tests
 * %%
 * Copyright (C) 2017 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.terasoluna.gfw.common.codelist.i18n.I18nCodeList;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;
import cloud.altemista.fwk.common.exception.CommonException;
import cloud.altemista.fwk.it.service.CoreDummyService;

/**
 * Simple REST controller to check the application has been successfully
 * deployed
 * 
 * @author NTT DATA
 */
@Controller
@RequestMapping(value = TilesAliveController.MAPPING, method = RequestMethod.GET)
@TransactionTokenCheck("transactionTokenCheckExample")
public class TilesAliveController {

	/** MAPPING String */
	public static final String MAPPING = "/tilesalive";

	@Inject
	@Named("CL_I18N_PRICE")
	I18nCodeList i18nCodeList;

	@Autowired
	private CoreDummyService dummyService;
	
	
	/**
	 * Constructor
	 */
	public TilesAliveController() {
		super();
	}

	/**
	 * Simple REST service to check the application has been successfully
	 * deployed
	 * 
	 * @return Object
	 */
	@RequestMapping
	public String alive() {

		return "pages/blank";
	}

	@RequestMapping("exception")
	public String exception() {

		throw new CommonException("dummy");

	}

	@RequestMapping("serviceException")
	public String serviceException() {
		dummyService.alwaysThrowsApplicationExceptionWithMessage();
		return "";

	}

	@RequestMapping("nullpointerException")
	public String nullpointerException() {
		throw new NullPointerException();
	}

	@RequestMapping(params = "initToken", method = RequestMethod.POST)
	@TransactionTokenCheck(type = TransactionTokenType.BEGIN) // (2)
	public String initToken() {
		return "Token init";
	}

	@RequestMapping(params = "checkToken", method = RequestMethod.POST)
	@TransactionTokenCheck
	public String checkToken() {
		return "Token check";
	}

}
