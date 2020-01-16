package com.mycompany.altemistamoviesui.webapp.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.altemista.cloudfwk.demo.altemistamoviesmovies.microservice.domain.MovieDTO;
import org.altemista.cloudfwk.demo.altemistamoviesrecomend.microservice.domain.RecommendationDTO;
import org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.domain.UserDTO;

import com.mycompany.altemistamoviesui.webapp.utils.AbstractController;

@Controller
@RequestMapping(RecommendationsController.ENTITY_MAPPING)
public class RecommendationsController extends AbstractController<RecommendationDTO> {
	
	protected static final String ENTITY_MAPPING = "/recommendations";

	public static final String ENTITY_NAME = "recommendation";
	private static final String ENTITY_WEB_ENDPOINT = "recommendations";

	protected final ParameterizedTypeReference<List<MovieDTO>> responseEntityMovieParameterizedTypeReference =
			new ParameterizedTypeReference<List<MovieDTO>>() {};
	protected final ParameterizedTypeReference<List<UserDTO>> responseEntityUserParameterizedTypeReference =
			new ParameterizedTypeReference<List<UserDTO>>() {};
			
	@Autowired
	private UserController userController;
	
	@Autowired
	private MovieController movieController;

	@Inject
	public RecommendationsController(@Value("${microservice.demo.recommendations.name}") String microserviceDemoName) throws NoSuchMethodException, SecurityException {
		super(microserviceDemoName, ENTITY_WEB_ENDPOINT, ENTITY_NAME, 
				new ParameterizedTypeReference<List<RecommendationDTO>>(){}, new ParameterizedTypeReference<RecommendationDTO>(){});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void afterGetAll(Model model) {
		super.afterGetAll(model);
		Map<String, Object> modelAsMap = model.asMap();
		List<RecommendationDTO> recommendations = (List<RecommendationDTO>) modelAsMap.get(entityWebEndpoint);
		for (RecommendationDTO recommendation : recommendations) {
			Long movieId = recommendation.getMovieId();
			ResponseEntity<MovieDTO> movieDTO = 
					template.getForEntity(composeURI(movieId.toString(), movieController.getMicroserviceName()), MovieDTO.class);
			recommendation.setMovieTitle(movieDTO.getBody().getTitle());
			Long userId = recommendation.getUserId();
			ResponseEntity<UserDTO> userDTO = 
					template.getForEntity(composeURI(userId.toString(), userController.getMicroserviceName()), UserDTO.class);
			recommendation.setUserFirstName(userDTO.getBody().getFirstName());
			recommendation.setUserLastName(userDTO.getBody().getLastName());
		}
	}
	
	@Override
	protected void afterNew(Model model) {
		super.afterNew(model);
		addMoviesToModel(model);
		addUsersToModel(model);
	}
	
	@Override
	protected void afterEdit(Model model) {
		super.afterEdit(model);
		addMoviesToModel(model);
		addUsersToModel(model);
	}

	protected void addMoviesToModel(Model model) {
		URI composeURI = composeURI(null, movieController.getMicroserviceName());
		ResponseEntity<List<MovieDTO>> moviesResponse = template.exchange(composeURI, HttpMethod.GET, null, responseEntityMovieParameterizedTypeReference);
		List<MovieDTO> movies = moviesResponse.getBody();
		model.addAttribute("movies", movies);
	}

	protected void addUsersToModel(Model model) {
		URI composeURI = composeURI(null, userController.getMicroserviceName());
		ResponseEntity<List<UserDTO>> usersResponse = template.exchange(composeURI, HttpMethod.GET, null, responseEntityUserParameterizedTypeReference);
		List<UserDTO> users = usersResponse.getBody();
		model.addAttribute("users", users);
	}

	@Override
	public Class<RecommendationDTO> getEntityClass() {
		return RecommendationDTO.class;
	}

}
