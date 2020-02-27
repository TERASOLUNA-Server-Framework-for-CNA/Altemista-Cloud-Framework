/**
 * 
 */
package cloud.altemista.fwk.rules.drools.impl;

/*
 * #%L
 * altemista-cloud rules engine: Drools implementation
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.kie.api.runtime.Globals;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.util.Assert;
import cloud.altemista.fwk.core.rules.session.StatefulRuleSession;

/**
 * Drools-based implementation of an stateful rule session
 * @author NTT DATA
 */
public class DroolsStatefulRuleSessionImpl implements StatefulRuleSession {
	
	/** The KieSession wrapped by this rule session */
	private KieSession kieSession;
	
	private Map<String, FactHandle> factHandleMap;

	/**
	 * Constructor
	 * @param kieSession The KieSession wrapped by this stateful rule session
	 */
	public DroolsStatefulRuleSessionImpl(KieSession kieSession) {
		Assert.notNull(kieSession);
		this.kieSession = kieSession;
		this.factHandleMap = new HashMap<String, FactHandle>();
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.rules.session.StatefulRuleSession#release()
	 */
	@Override
	public void release() {
		this.kieSession.dispose();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.rules.session.StatefulRuleSession#executeRules()
	 */
	@Override
	public void executeRules() {
		this.kieSession.fireAllRules();
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.rules.session.StatefulRuleSession#addObject(java.lang.Object)
	 */
	@Override
	public String addObject(Object bean) {
		if (bean == null) {
			return null;
		}
		FactHandle handle = this.kieSession.insert(bean);
		String id = handle.toExternalForm();
		this.factHandleMap.put(id, handle);
		return id;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.rules.session.StatefulRuleSession#getObject(java.lang.String)
	 */
	@Override
	public Object getObject(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		FactHandle handle = this.factHandleMap.get(id);
		if (handle == null) {
			return null;
		}
		return this.kieSession.getObject(handle);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.rules.session.StatefulRuleSession#getObjects()
	 */
	@Override
	public Map<String, Object> getObjects() {	
		Map<String, Object> beans = new HashMap<String, Object>();
		for (Entry<String, FactHandle> entry : this.factHandleMap.entrySet()) {
			beans.put(entry.getKey(), this.kieSession.getObject(entry.getValue()));
		}
		return beans;
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.rules.session.StatefulRuleSession#removeObject(java.lang.String)
	 */
	@Override
	public void removeObject(String id) {
		if (StringUtils.isNotEmpty(id)) {
			FactHandle handle = this.factHandleMap.get(id);
			if (handle != null) {
				this.kieSession.delete(handle);
				this.factHandleMap.remove(id);
			}
		}
	}

	/* 
	 * @see cloud.altemista.fwk.core.rules.session.StatefulRuleSession#updateObject(java.lang.String, java.lang.Object)
	 * If bean parameter is null, it will be remove from session.
	 */
	@Override
	public void updateObject(String id, Object bean) {
		if (bean == null) {
			// If the bean is null, it will be remove from session
			this.removeObject(id);
		}
		if (StringUtils.isNotEmpty(id)) {
			FactHandle handle = this.factHandleMap.get(id);
			if (handle != null) {
				this.kieSession.update(handle, bean);
			}
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.rules.session.StatefulRuleSession#reset()
	 */
	@Override
	public void reset() {
		Iterator<FactHandle> it = this.factHandleMap.values().iterator();
		while (it.hasNext()) {
			this.kieSession.delete(it.next());
			it.remove();
		}
		this.factHandleMap.clear();
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.rules.session.StatefulRuleSession#addGlobal(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addGlobal(String id, Object value) {
		if (StringUtils.isNotEmpty(id) && value != null) {
			this.kieSession.setGlobal(id, value);
		}
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.rules.session.StatefulRuleSession#getGlobal(java.lang.String)
	 */
	@Override
	public Object getGlobal(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		return this.kieSession.getGlobal(id);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.core.rules.session.StatefulRuleSession#getGlobals()
	 */
	@Override
	public Map<String, Object> getGlobals() {
		Globals droolsGlobals = this.kieSession.getGlobals();
		Iterator<String> it = droolsGlobals.getGlobalKeys().iterator();
		Map<String, Object> globals = new HashMap<String, Object>();
		while (it.hasNext()) {
			String key = it.next();
			globals.put(key, droolsGlobals.get(key));
		}
		return globals;
	}

}
