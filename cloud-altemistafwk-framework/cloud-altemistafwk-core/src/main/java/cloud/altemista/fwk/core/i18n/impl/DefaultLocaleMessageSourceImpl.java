package cloud.altemista.fwk.core.i18n.impl;

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
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import cloud.altemista.fwk.core.i18n.DefaultLocaleMessageSource;

/**
 * Implementation of the custom interface for resolving messages
 * without the need to specify a Locale.
 * @author NTT DATA
 */
public class DefaultLocaleMessageSourceImpl extends ResourceBundleMessageSource implements DefaultLocaleMessageSource {

	/** LOGGER Logger */
	private static final Logger logger = LoggerFactory.getLogger(DefaultLocaleMessageSourceImpl.class); // NOSONAR

	/** Default application locale */
	private Locale defaultLocale = null;

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.i18n.DefaultLocaleMessageSource#getCurrentLocale()
	 */
	@Override
	public Locale getCurrentLocale() {
		
		// Get locale from context 
		Locale locale = LocaleContextHolder.getLocale();
		if (locale == null) {
			if (this.defaultLocale != null) {
				locale = this.defaultLocale;
			} else {
				 locale = Locale.getDefault();
			}
		}
		return locale;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.i18n.DefaultLocaleMessageSource#setDefaultLocale(java.lang.String)
	 */
	@Override
	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = LocaleUtils.toLocale(defaultLocale);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.i18n.DefaultLocaleMessageSource#getMessage(java.lang.String, java.lang.Object[], java.lang.String)
	 */
	@Override
	public String getMessage(String code, Object[] args, String defaultMessage) {
		return this.getMessage(code, args, defaultMessage, this.getCurrentLocale());

	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.i18n.DefaultLocaleMessageSource#getMessage(java.lang.String, java.lang.Object[])
	 */
	@Override
	public String getMessage(String code, Object[] args) {
		return this.getMessage(code, args, null, this.getCurrentLocale());

	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.i18n.DefaultLocaleMessageSource#getMessage(org.springframework.context.MessageSourceResolvable)
	 */
	@Override
	public String getMessage(MessageSourceResolvable resolvable) {
		return this.getMessage(resolvable, this.getCurrentLocale());
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
	 * Returns a list of the resources contained in the basename (that should be a folder)
	 * @param folderResource the basename (that should be a folder)
	 * @return List of String with the nested resources
	 */
	private List<String> getNestedResources(final String folderResource) {
		
		Resource[] resources = null;
		try {
			// Converts the basename as a locationPattern
			final String locationPattern = String.format(
					"classpath*:%s**.properties", folderResource.replaceAll("\\.", "/"));

			// Locates the resources under the basename
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			resources = resolver.getResources(locationPattern);
			
		} catch (IOException e) {
			logger.info("There was a problem during basename resolution", e); // NOSONAR
		}

		// (no resources located)
		if (resources == null) {
			return Collections.emptyList();
		}
		
		// For each located resource
		Set<String> resourceList = new TreeSet<String>();
		for (Resource resource : resources) {
			this.appendNestedResource(resourceList, folderResource, resource);
		}
		return new ArrayList<String>(resourceList);
	}

	/**
	 * Convenience method to add a nested resource to a set of resources
	 * @param resourceList Set with the nested resources
	 * @param folderResource the basename
	 * @param resource the nested resource
	 */
	private void appendNestedResource(Set<String> resourceList, String folderResource, Resource resource) {
	
		String resourceName = folderResource + this.getCleanFileName(resource.getFilename());
		resourceList.add(resourceName);
		logger.info("Resolved resource: " + resourceName);
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
}
