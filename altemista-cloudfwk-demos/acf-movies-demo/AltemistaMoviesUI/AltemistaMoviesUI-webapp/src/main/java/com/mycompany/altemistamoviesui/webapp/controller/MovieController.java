package com.mycompany.altemistamoviesui.webapp.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.altemista.cloudfwk.demo.altemistamoviesmovies.microservice.domain.MovieDTO;

import com.mycompany.altemistamoviesui.webapp.utils.AbstractController;

@Controller
@RequestMapping(MovieController.ENTITY_MAPPING)
public class MovieController extends AbstractController<MovieDTO> {
	
	protected static final String ENTITY_MAPPING = "/movies";
	
	private static final String ENTITY_WEB_ENDPOINT = "movies";
	private static final String ENTITY_NAME = "movie";
	
	@Inject
	public MovieController(@Value("${microservice.demo.movies.name}") String microserviceDemoName) throws NoSuchMethodException, SecurityException {
		super(microserviceDemoName, ENTITY_WEB_ENDPOINT, ENTITY_NAME, 
				new ParameterizedTypeReference<List<MovieDTO>>(){}, new ParameterizedTypeReference<MovieDTO>(){});
	}

	@Override
	public Class<MovieDTO> getEntityClass() {
		return MovieDTO.class;
	}

}
