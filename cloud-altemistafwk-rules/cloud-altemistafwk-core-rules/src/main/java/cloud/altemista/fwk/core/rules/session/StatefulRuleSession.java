/**
 * 
 */
package cloud.altemista.fwk.core.rules.session;

/*
 * #%L
 * altemista-cloud rules engine
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.Map;

/**
 * Represents an stateful rule session with the rules engine
 * @author NTT DATA
 */
public interface StatefulRuleSession {

	/**
	 * Releases this stateful rule session.<br>
	 * This method <b>must</b> always be called after finishing using the session
	 */
	void release();
	
	
	/**
	 * Executes all the rules over the session.
	 */
	void executeRules();
	
	/**
	 * Returns the value of the global according the identifier provided.
	 * @param id
	 * @return global
	 */
	Object getGlobal(String id);
	
	/**
	 * Returns all the globals.
	 * @param id
	 * @return map with key/value of all the globals
	 */
	Map<String, Object> getGlobals();
	
	/**
	 * Adds one global value.
	 * @param id
	 * @param value
	 */
	void addGlobal(String id, Object value);
	

	/**
	 * Adds one bean to the session.
	 * @param bean
	 * @return String identifier of the object added.
	 */
	String addObject(Object bean);
	
	/**
	 * Returns the bean according its identifier.
	 * @param id
	 * @return bean
	 */
	Object getObject(String id);
	
	/**
	 * Returns all the objects.
	 * @param id
	 * @return bean
	 */
	Map<String, Object> getObjects();
	
	
	/**
	 * Removes the bean according the identifier provided
	 * @param id
	 */
	void removeObject(String id);
	
	
	/**
	 * Replaces the bean with the identifier provided with the new bean given.
	 * @param id
	 * @param bean
	 */
	void updateObject(String id, Object bean);
	
	
	/**
	 * Clean every bean object stored in the session.
	 */
	void reset();
}

