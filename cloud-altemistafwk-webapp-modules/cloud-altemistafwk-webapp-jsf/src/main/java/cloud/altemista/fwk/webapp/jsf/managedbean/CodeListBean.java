package cloud.altemista.fwk.webapp.jsf.managedbean;

/*
 * #%L
 * altemista-cloud presentation layer: JSF support (PrimeFaces)
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.jsf.FacesContextUtils;
import org.terasoluna.gfw.common.codelist.CodeList;
import org.terasoluna.gfw.common.codelist.i18n.I18nCodeList;

/**
 * Exposes the codelist as a ManagedBean that can be used in JSF pages
 * 
 * @author NTT DATA
 */
@ManagedBean(name = "codeList")
@ApplicationScoped
public class CodeListBean implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1578047152683979L;

	/** logger. */
	private static final Logger logger = LoggerFactory.getLogger(CodeListBean.class);

	/**
	 * the default locale to fall back.<br>
	 * this locale is used if the requested locale is not found in i18nCodeList.
	 */
	private Locale fallbackTo = Locale.getDefault();

	/** The code list id pattern. */
	private Pattern codeListIdPattern;

	/** The code lists (HashMap to be Serializable). */
	private final Map<String, CodeList> codeLists = new HashMap<String, CodeList>(); // NOSONAR

	/**
	 * Gets CodeList with name
	 * @param name the name of codelist to return
	 * @return the content of codelist in a List
	 */
	public List<Entry<String, String>> get(String name) {

		Locale locale = LocaleContextHolder.getLocale();
		logger.debug("locale for I18nCodelist is '{}'.", locale);

		CodeList codeList = this.codeLists.get(name);
		if (codeList == null) {
			return Collections.emptyList();
		}

		if (codeList instanceof I18nCodeList) {
			return new ArrayList<Map.Entry<String, String>>(getLocalizedCodeMap((I18nCodeList) codeList, locale).entrySet());
		} else {
			return new ArrayList<Map.Entry<String, String>>(codeList.asMap().entrySet());
		}
	}

	/**
	 * Gets the code list id pattern.
	 * @return the code list id pattern
	 */
	public Pattern getCodeListIdPattern() {
		
		final ApplicationContext applicationContext =
				FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
		
		// Initializes the code list id pattern
		if (this.codeListIdPattern == null) {
			String pattern = applicationContext.getEnvironment().getProperty("codeListIdPattern");
			if (pattern == null) {
				pattern = ".+";
			}
			this.codeListIdPattern = Pattern.compile(pattern);
		}
		
		return this.codeListIdPattern;
	}

	/**
	 * Returns the map of codelists which match to the specified locale.<br>
	 * If the map of codelists which match to the specified locale does not
	 * exist, returns the map of codelists which match
	 * with fallback locale. If the map corresponding to fallback locale also
	 * does not exist, then log of WARN level is output
	 * and an empty map is returned.
	 * @param i18nCodeList International Codelist
	 * @param requestLocale Locale of request
	 * @return Map of codelists which match to the specified locale
	 */
	protected Map<String, String> getLocalizedCodeMap(I18nCodeList i18nCodeList, Locale requestLocale) {
		
		Map<String, String> codeListMap = i18nCodeList.asMap(requestLocale);
		if (codeListMap.isEmpty() && (this.fallbackTo != null && !this.fallbackTo.equals(requestLocale))) {
			logger.debug("There is no mapping for '{}'. fall back to '{}'.", requestLocale, this.fallbackTo);
			codeListMap = i18nCodeList.asMap(this.fallbackTo);
			if (codeListMap.isEmpty()) {
				logger.warn("could not fall back to '{}'.", this.fallbackTo);
			}
		}
		return codeListMap;
	}

	/**
	 * Load code lists that match with the patter defined in codeListIdPattern property.
	 */
	@PostConstruct
	public void loadCodeLists() {

		final ApplicationContext applicationContext =
				FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
		
		// Initializes the code lists (HashMap to be Serializable)
		Map<String, CodeList> definedCodeLists = BeanFactoryUtils.beansOfTypeIncludingAncestors(
				applicationContext, CodeList.class, false, false);
		for (CodeList codeList : definedCodeLists.values()) {
			String codeListId = codeList.getCodeListId();
			if (codeListId != null) {
				Matcher codeListIdMatcher = getCodeListIdPattern().matcher(codeListId);
				if (codeListIdMatcher.matches()) {
					this.codeLists.put(codeListId, codeList);
				}
			}
		}
	}

}
