/**
 * 
 */
package org.altemista.cloudfwk.rules.drools.resolver.impl;

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


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.drools.compiler.kproject.models.KieModuleModelImpl;
import org.drools.core.io.impl.ClassPathResource;
import org.drools.core.io.impl.FileSystemResource;
import org.drools.core.io.impl.InputStreamResource;
import org.drools.core.io.impl.UrlResource;
import org.drools.core.io.internal.InternalResource;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.altemista.cloudfwk.core.rules.exception.RulesEngineException;
import org.altemista.cloudfwk.core.rules.ruleset.RulesetContainer;
import org.altemista.cloudfwk.core.rules.ruleset.impl.AbstractRulesetContainerImpl;

/**
 * RulesetContainer implementation that builds a KieModule
 * (i.e.: identifies rules declared in a "META-INF/kmodule.xml" file).
 * The URL can be either a "classpath:" pseudo URL, a "file:" URL, or an actual URL
 * @author NTT DATA
 */
public class DroolsUrlRulesetContainerImpl extends AbstractRulesetContainerImpl<KieContainer>
		implements RulesetContainer<KieContainer>, InitializingBean {
	
	/** URL with the KieModule location. Can be a "classpath:" pseudo URL, a "file:" URL, or an actual URL */
	private String url;

	/** If the URL is a remote resource and authentication is required, the username for the authentication */
	private String username;

	/** If the URL is a remote resource and authentication is required, the password for the authentication */
	private String password;

	/** If the URL is a remote resource, flags that authentication is required */
	private boolean basicAuthentication;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		Assert.notNull(this.url);
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.core.rules.ruleset.impl.AbstractRulesetContainerImpl#initContainer()
	 */
	@Override
	protected KieContainer initContainer() {
	
		// If the URL is a "classpath:" pseudo URL, uses newClassPathResource()
		if (StringUtils.startsWith(this.url, ResourceUtils.CLASSPATH_URL_PREFIX)) {
			return initClasspathKieContainer();
		}
		
		// If the URL is a "file:" URL, uses newFileSystemResource()
		if (StringUtils.startsWith(this.url, ResourceUtils.FILE_URL_PREFIX)) {
			return initFileSystemKieContainer();
		}
		
		// Otherwise, uses newUrlResource()
		return initUrlKieContainer();
	}

	/**
	 * Initializes the KieContainer from the classpath
	 * @return KieContainer
	 */
	private KieContainer initClasspathKieContainer() {
		
		//final KieServices kieServices = KieServices.Factory.get();
		
		final String path = StringUtils.removeStart(this.url, ResourceUtils.CLASSPATH_URL_PREFIX);
		
		return this.initKieContainer((ClassPathResource) new DirectoryClassPathResource(path));
	}

	/**
	 * Initializes the KieContainer from the file system
	 * @return KieContainer
	 */
	private KieContainer initFileSystemKieContainer() {
		
		final KieServices kieServices = KieServices.Factory.get();
		
		final String path = StringUtils.removeStart(this.url, ResourceUtils.FILE_URL_PREFIX);
		return this.initKieContainer((FileSystemResource) kieServices.getResources().newFileSystemResource(path));
	}

	/**
	 * Initializes the KieContainer from an URL
	 * @return KieContainer
	 */
	private KieContainer initUrlKieContainer() {
		
		final KieServices kieServices = KieServices.Factory.get();
		
		UrlResource urlResource = (UrlResource) kieServices.getResources().newUrlResource(this.url);
		if (StringUtils.isNotEmpty(this.username)) {
			urlResource.setUsername(this.username);
		}
		if (StringUtils.isNotEmpty(this.password)) {
			urlResource.setPassword(this.password);
		}
		if (this.basicAuthentication) {
			urlResource.setBasicAuthentication("enabled");
		}
		
		InputStream inputStream = null;
		try {
			inputStream = urlResource.getInputStream();
			return this.initKieContainer((InputStreamResource) kieServices.getResources().newInputStreamResource(inputStream));
			
		} catch (IOException e) {
			throw new RulesEngineException("io_exception", new Object[]{ this.url }, e);
			
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	/**
	 * Initializes the KieContainer with a new KieModule built from an internal resource
	 * @param internalResource InternalResource to build the KieModule
	 * @return KieContainer
	 */
	private KieContainer initKieContainer(InternalResource internalResource) {
		
		final KieServices kieServices = KieServices.Factory.get();
		
		// (despite the parameter is declared as Resource, it MUST be an InternalResource)
		// @see org.drools.compiler.kie.builder.impl.KieRepositoryImpl#getKieModule(Resource)
		KieModule kieModule = kieServices.getRepository().addKieModule(internalResource);
		
		// Builds the KieContainer using the KieModule
		return kieServices.newKieContainer(kieModule.getReleaseId());
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param basicAuthentication the basicAuthentication to set
	 */
	public void setBasicAuthentication(boolean basicAuthentication) {
		this.basicAuthentication = basicAuthentication;
	}
}

/**
 * This Class fixes TSFPLUS-27, where a Jar could not be read as directory
 * @author NTT DATA
 *
 */
class DirectoryClassPathResource extends ClassPathResource{

	/*
	 *Constructors from super class 
	 */
	/**
	 * {@inheritDoc}
	 */
	public DirectoryClassPathResource(String path) {
		super(path);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDirectory() {
		// Drools constructs the URL using this value, if its true appends "/META-INF/kmodule.xml"
		// But if its false, appends "jar:...." And "...!/" for accesing, jars,
		// Normaly on classpath the URL has already the jar accesor, so it breaks
		// Quick Fix: Return true allways
		// Real Fix, check the existence of <path>/META-INF/kmodule.xml
		
		try {
			String stUrl = this.getURL().toExternalForm()+"/"+KieModuleModelImpl.KMODULE_JAR_PATH;
			URL url = new URL(stUrl);
			URLConnection conn = url.openConnection();
			boolean exists = conn.getContentLengthLong()>0;
			return exists;
		}catch (Exception e) {
			//If an exception occurrs, the path is not a directory, so return false
			return false;
		}
	}
	
}
