package com.mycompany.altemistamoviesui.webapp.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;

import com.mycompany.altemistamoviesui.webapp.controller.GatewayConfiguration;
import com.mycompany.altemistamoviesui.webapp.controller.MSDRestTemplate;

public abstract class AbstractController<T extends Object> {
	
	protected static final String ID_TEXT = "id";
	protected static final String PAGE_0_SIZE_10 = "page=0&size=10";
	protected static final String ENDPOINT = "endpoint";
	
	protected static final String SLASH  = "/";
	protected static final String CANCEL = "/cancel";
	protected static final String UPDATE = "/update/{id}";
	protected static final String EDIT   = "/edit/{id}";
	protected static final String SAVE   = "/save";
	protected static final String NEW    = "/new";
	protected static final String DELETE = "/delete/{id}";
	
	protected static final String SUCCESS_MESSAGE = "messageSuccess";
	protected static final String WARNING_MESSAGE = "messageWarning";
	protected static final String ERROR_MESSAGE = "messageError";
	
	private static final String DELETED_MESSAGE = "Deleted!!!";
	private static final String UPDATED_MESSAGE = " updated!!!";
	private static final String CREATED_MESSAGE = " created!!!";
	private static final String EXISTS_MESSAGE = " exists!!!";
	
	protected final String microserviceName;
	protected final String entityWebEndpoint;
	protected final String entityName;
	
	@Autowired
	protected MSDRestTemplate template;
	
	@Autowired
	private GatewayConfiguration gatewayConfiguration;
	
	protected final ParameterizedTypeReference<List<T>> responseListPTR;
	protected final ParameterizedTypeReference<T> responseEntityPTR;
	
	public AbstractController(String microserviceName, String entityWebEndpoint, String entityName,
			ParameterizedTypeReference<List<T>> responseListPTR, ParameterizedTypeReference<T> responseEntityPTR) 
					throws NoSuchMethodException, SecurityException {
		super();
		this.microserviceName = microserviceName;
		this.entityWebEndpoint = entityWebEndpoint;
		this.entityName = entityName;
		this.responseListPTR = responseListPTR;
		this.responseEntityPTR = responseEntityPTR;
	}

	@RequestMapping
	public String getIndex(Model model) {
		ResponseEntity<List<T>> entitysResponse = template.exchange(composeURI(null), 
				HttpMethod.GET, null, responseListPTR);
		List<T> listT = (List<T>) entitysResponse.getBody();
		model.addAttribute(entityWebEndpoint, listT);
		afterGetAll(model);
		return entityWebEndpoint;
	}

	protected void afterGetAll(Model model) {}

	@RequestMapping(CANCEL)
	public String cancel(Model model) {
		return getIndex(model);
	}
	
	@RequestMapping(NEW)
	public String getEntityForm(Model model) {
		model.addAttribute(entityName, newEntityInstance());
		afterNew(model);
		return entityName;
	}

	protected void afterNew(Model model) {}

	@RequestMapping(SAVE)
	public String saveEntity(@ModelAttribute T entity, Model model) {
		try {
			ResponseEntity<T> newEntityResponseEntity = template.postForEntity(composeURI(null), entity, getEntityClass());
			model.addAttribute(this.entityName, newEntityResponseEntity.getBody());
			model.addAttribute(SUCCESS_MESSAGE, StringUtils.capitalize(this.entityName) + CREATED_MESSAGE);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode()==HttpStatus.CONFLICT) {
				model.addAttribute(ERROR_MESSAGE, StringUtils.capitalize(this.entityName) + EXISTS_MESSAGE);
			}
		}
		return getIndex(model);
	}
	
	@RequestMapping(value=EDIT, method=RequestMethod.POST)
	public String editEntity(Model model, @PathVariable(ID_TEXT) Long id) {
		ResponseEntity<T> entityDTOResponseEntity = template.getForEntity(composeURI(id.toString()), getEntityClass());
		T entityDTO = entityDTOResponseEntity.getBody();
		model.addAttribute(this.entityName, entityDTO);
		afterEdit(model);
		return this.entityName;
	}
	
	protected void afterEdit(Model model){}
	
	@RequestMapping(value=UPDATE, method=RequestMethod.POST)
	public String updateEntity(@ModelAttribute T entity, @PathVariable(ID_TEXT) String id, Model model) {
		template.put(composeURI(id), entity);
		model.addAttribute(this.entityName, entity);
		model.addAttribute(SUCCESS_MESSAGE, StringUtils.capitalize(this.entityName) + UPDATED_MESSAGE);
		return getIndex(model);
	}
	
	@RequestMapping(DELETE)
	public String delete(Model model, @PathVariable(ID_TEXT) Long id){
		URI composeURI = composeURI(id.toString());
		template.delete(composeURI);
		model.addAttribute(WARNING_MESSAGE, DELETED_MESSAGE);
		return getIndex(model);
	}
	
	public abstract Class<T> getEntityClass();
	
	public T newEntityInstance() {
		T newInstance = null;
		try {
			newInstance = getEntityClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return newInstance;
	}
	
	protected URI composeURI(String endpoint) {
		return composeURI(endpoint, getMicroserviceName());
	}
	
	protected URI composeURI(String endpoint, String microserviceName) {
		URI uri = null;
		try {
			if (endpoint==null) endpoint = "";
			uri = new URI(gatewayConfiguration.getProtocol(), null, gatewayConfiguration.getHost(), gatewayConfiguration.getPort(), 
					SLASH + microserviceName + SLASH + endpoint, null, null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return uri;
	}
	
	public String getMicroserviceName() {
		return this.microserviceName;
	}
	
}
