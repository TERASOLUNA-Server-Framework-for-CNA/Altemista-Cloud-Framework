package cloud.altemista.fwk.core.i18n;

/*
 * #%L
 * altemista-cloud framework core
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.terasoluna.gfw.common.codelist.i18n.I18nCodeList;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

/**
 * I18nCodeList and DefaultLocaleMessageSource implementation with support for the parameterization 
 * and internationalization of such messages.
 * Extends {@link org.springframework.context.MessageSource} with methods that does not require an specific Locale,
 * using the current locale given by the method getCurrentLocale().
 * @author NTT DATA
 */
public class PropertiesI18nCodeList extends ReloadableResourceBundleMessageSource
		implements I18nCodeList, BeanNameAware {

	/** LOGGER Logger. */
	private static final Logger logger = LoggerFactory.getLogger(PropertiesI18nCodeList.class); // NOSONAR
	
	/**
	 * Supplier to return a {@link LinkedHashMap} object.
	 */
	private static final Supplier<Map<String, String>> LINKED_HASH_MAP_SUPPLIER = new Supplier<Map<String, String>>() {
		@Override
		public Map<String, String> get() {
			return Maps.newLinkedHashMap();
		}
	};
	
	/** Property to hold codelist Id */
	private String codeListId;
	
	/** codelist table. */
	private Table<Locale, String, String> codeListTable;
	

	/** Default application locale */
	private Locale defaultLocale = null;

	/**
	 * Try to resolve the message.
	 * @param code the code to lookup up
	 * @param args  array of arguments that will be filled in for params within the message
	 * @param defaultMessage String to return if the lookup fails
	 * @return the resolved message
	 */
	public String getMessage(String code, Object[] args, String defaultMessage) {
		return this.getMessage(code, args, defaultMessage, this.getCurrentLocale());

	}

	/**
	 * Try to resolve the message.
	 * @param code the code to lookup up
	 * @param args  array of arguments that will be filled in for params within the message
	 * @return the resolved message
	 */
	public final String getMessage(String code, Object[] args) {
		return this.getMessage(code, args, null, this.getCurrentLocale());

	}

	/**
	 * Try to resolve the message.
	 * @param resolvable object suitable for message resolution
	 * @return the resolved message
	 */
	public String getMessage(MessageSourceResolvable resolvable) {
		return this.getMessage(resolvable, this.getCurrentLocale());
	}
	
	/* (non-Javadoc)
	 * @see org.terasoluna.gfw.common.codelist.i18n.I18nCodeList#asMap(java.util.Locale)
	 */
	@Override
	public Map<String, String> asMap(Locale locale) {
		if(!codeListTable.containsRow(locale)) {
			PropertiesHolder propertiesHolder =	getMergedProperties(locale);
			Properties properties = propertiesHolder.getProperties();
			Enumeration<Object> e = properties.keys();
			while( e.hasMoreElements() ){
			  String key = (String)e.nextElement();
			  String value =  (String)properties.get( key );
			  codeListTable.put(locale, key, value);	
			}
		}
		return codeListTable.row(locale);

	}

	/**
	 * Returns a codelist as map for the default locale ({@link Locale#getDefault()}).
	 * If there is no codelist for the locale, returns an empty map.
	 * @return the ma
	 * @see org.terasoluna.gfw.common.codelist.CodeList#asMap()
	 */
	@Override
	public Map<String, String> asMap() {

		return asMap(Locale.getDefault());
	}

	/**
	 * Returns the default application locale
	 * @return the default application locale, or the default locale for this instance
     * of the Java Virtual Machine if the default application locale is not set
	 */
	public Locale getCurrentLocale() {
		return (this.defaultLocale != null) ? this.defaultLocale : Locale.getDefault();
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
	 */
	@Override
	public void setBeanName(String beanName) {
		this.codeListId = beanName;
	}

	/* (non-Javadoc)
	 * @see org.terasoluna.gfw.common.codelist.CodeList#getCodeListId()
	 */
	@Override
	public String getCodeListId() {
		return codeListId;
	}
	

	/**
	 * Sets the default locale.
	 *
	 * @param defaultLocale the new default locale
	 */
	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = LocaleUtils.toLocale(defaultLocale);
	}
	
	
	/**
	 * Set an array of basenames, considering that basenames ending with a dot (".") as folders
	 * that contain nested resources (e.g.: "config.i18n.").
	 * @param basenames array of basenames
	 * @see org.springframework.context.support.ResourceBundleMessageSource#setBasenames(java.lang.String[])
	 */
	@Override
	public void setBasenames(String... basenames) {
		
		if (basenames == null) {
			// (lets the original setBasenames() implementation deal with the null value)
			super.setBasenames(basenames); // NOSONAR
			return;
		}
			
		List<String> list = new ArrayList<String>();
		for (String basename : basenames) {
			try {
				boolean isFolder = basename.endsWith(".") || basename.endsWith("/");
				if (isFolder) {
					list.addAll(this.getNestedResources(basename));
					
				} else {
					list.add(basename);
				}

			} catch (Exception e) {
				logger.error("There was a problem during basename resolution", e); // NOSONAR
			}
		}

		super.setBasenames(list.toArray(new String[list.size()]));
	}
	
	/**
	 * Inits the it.
	 */
	@PostConstruct
	public void initIt() {
	  this.codeListTable = createTable();
	}
	
	
	/**
	 * Returns a list of the resources contained in the basename (that should be a folder).
	 *
	 * @param folderResource the basename (that should be a folder)
	 * @return List of String with the nested resources
	 */
	private List<String> getNestedResources(final String folderResource) {
		
		// Converts the basename as a locationPattern
		final String locationPattern = String.format("classpath:%s**.properties",
				folderResource.replaceAll("\\.", "/"));

		Set<String> resourceList = new TreeSet<String>();
		try {
			// Locates the resources under the basename
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources(locationPattern);
			
			// For each located resource
			if (resources != null) {
				for (Resource resource : resources) {
					this.appendNestedResource(resourceList, folderResource, resource);
				}
			}
			
		} catch (IOException e) {
			logger.error("There was a problem during basename resolution", e); // NOSONAR
		}

		return new ArrayList<String>(resourceList);
	}

	private void appendNestedResource(Set<String> resourceList, String folderResource, Resource resource) {
		
		try {
			String resourceName = folderResource + this.getCleanFileName(resource.getFilename());
			resourceList.add("classpath:"+ resourceName.replaceAll("\\.", "/"));
			logger.info("Resolved resource: " + resourceName);
			
		} catch (Exception e) {
			logger.error("There was a problem during basename resolution", e); // NOSONAR
		}
	}
	
	
	/**
	 * Clears the extension and the locale suffix (e.g.: "_es_ES") from the filename of a resource
	 * @param resourceFilename the filename of the resource
	 * @return String with the filename without extension and the locale suffix
	 */
	private String getCleanFileName(final String resourceFilename) {
		
		int index = resourceFilename.lastIndexOf('.');
		String fileNameWOEx = (index > 0 && index <= resourceFilename.length() - 2)
				? resourceFilename.substring(0, index)
				: resourceFilename;
		return fileNameWOEx.split("_")[0];
	}
	
    /**
     * create table which consist of {@link LinkedHashMap} factory.
     * @return table
     */
    private Table<Locale, String, String> createTable() {
        Map<Locale, Map<String, String>> backingMap = Maps.newLinkedHashMap();
        return Tables.newCustomTable(backingMap, LINKED_HASH_MAP_SUPPLIER);
    }
}
