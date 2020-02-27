package com.mycompany.altemistamoviesui.webapp.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import cloud.altemista.fwk.demo.altemistamoviesrecomend.microservice.domain.RecommendationDTO;
import cloud.altemista.fwk.demo.altemistamoviesusers.microservice.domain.UserDTO;

import com.mycompany.altemistamoviesui.webapp.utils.AbstractController;

@Controller
@RequestMapping(UserController.ENTITY_MAPPING)
public class UserController extends AbstractController<UserDTO> {
	
	private static final String RECOMMENDATIONS = "recommendations";
	private static final String RECOMMENDATIONS_MAPPING = "/recommendations";

	protected static final String ENTITY_MAPPING = "/users";

	private static final String ENTITY_NAME = "user";
	private static final String ENTITY_WEB_ENDPOINT = "users";
	
	protected final ParameterizedTypeReference<List<RecommendationDTO>> responseEntityRecommendationParameterizedTypeReference =
			new ParameterizedTypeReference<List<RecommendationDTO>>() {};
			
	
	@Autowired
	private RecommendationsController recommendationsController;

	@Inject
	public UserController(@Value("${microservice.demo.users.name}") String microserviceDemoName) throws NoSuchMethodException, SecurityException {
		super(microserviceDemoName, ENTITY_WEB_ENDPOINT, ENTITY_NAME, 
				new ParameterizedTypeReference<List<UserDTO>>(){}, new ParameterizedTypeReference<UserDTO>(){});
	}
	
	@RequestMapping(value=RECOMMENDATIONS_MAPPING + "/{userId}", method=RequestMethod.POST)
	public String getRecommendations(Model model, @PathVariable("userId") Long userId) {
		
		URI composeURI = composeURI(userId.toString());
		ResponseEntity<UserDTO> userDTOResponseEntity = template.getForEntity(composeURI, UserDTO.class);
		UserDTO userDTO = userDTOResponseEntity.getBody();
		
		composeURI = composeURI("findByUserID/"+userDTO.getId(), recommendationsController.getMicroserviceName());
		ResponseEntity<List<RecommendationDTO>> allRecommendationsByUserResponseEntity = 
				template.exchange(composeURI, HttpMethod.GET, null, responseEntityRecommendationParameterizedTypeReference);
		
		List<RecommendationDTO> recommendations = allRecommendationsByUserResponseEntity.getBody();
		
		model.addAttribute(RECOMMENDATIONS, recommendations);
		model.addAttribute(ENTITY_NAME, userDTO);
		return ENTITY_NAME;
		
	}
	
	@RequestMapping(value=RECOMMENDATIONS + "/" + NEW + "/{userId}", method=RequestMethod.POST)
	public String addUserRecommendation(Model model, @PathVariable("userId") Long userId) {
		
		URI composeURI = composeURI(userId.toString());
		ResponseEntity<UserDTO> userDTOResponseEntity = template.getForEntity(composeURI, UserDTO.class);
		UserDTO userDTO = userDTOResponseEntity.getBody();
		
		recommendationsController.addMoviesToModel(model);
		
		List<UserDTO> users = new ArrayList<UserDTO>();
		users.add(userDTO);
		
		model.addAttribute("users", users);
		model.addAttribute("recommendation", new RecommendationDTO());
		model.addAttribute("userDTO", userDTO);
		return RecommendationsController.ENTITY_NAME;
		
	}

	@Override
	public Class<UserDTO> getEntityClass() {
		return UserDTO.class;
	}

}
