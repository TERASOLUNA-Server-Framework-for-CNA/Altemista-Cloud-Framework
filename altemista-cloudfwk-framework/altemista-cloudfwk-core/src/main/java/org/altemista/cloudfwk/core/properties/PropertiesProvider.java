package org.altemista.cloudfwk.core.properties;

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



/**
 * Defines the operations shared by all the possible properties providers (BBDD, file, ...)
 * @author NTT DATA
 */
public interface PropertiesProvider {

    
	/**
	 * Searches for the property with the specified key in this property list.
	 *
	 * @param key the property key
     * @return the value in this property list with the specified key value.
	 */
    String getProperty(String key);
    
	/**
	 * Searches for the property with the specified key in this property list.
	 *
	 * @param key the property key
     * @param defaultValue a default value.
     * @return the value in this property list with the specified key value.
	 */
    String getProperty(String key, String defaultValue);
    
}
