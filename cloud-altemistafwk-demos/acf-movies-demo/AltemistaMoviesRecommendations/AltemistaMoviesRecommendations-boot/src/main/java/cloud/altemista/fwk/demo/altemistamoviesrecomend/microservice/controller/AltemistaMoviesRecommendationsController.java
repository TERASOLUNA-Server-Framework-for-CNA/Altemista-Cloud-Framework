package cloud.altemista.fwk.demo.altemistamoviesrecomend.microservice.controller;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.domain.MovieDTO;
import cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.service.AltemistaMoviesMovies;
import cloud.altemista.fwk.demo.altemistamoviesrecomend.datacore.entity.Recommendation;
import cloud.altemista.fwk.demo.altemistamoviesrecomend.datacore.repository.RecommendationRepository;
import cloud.altemista.fwk.demo.altemistamoviesrecomend.microservice.domain.RecommendationDTO;
import cloud.altemista.fwk.demo.altemistamoviesrecomend.microservice.service.AltemistaMoviesRecommendations;
import cloud.altemista.fwk.demo.altemistamoviesusers.microservice.domain.UserDTO;
import cloud.altemista.fwk.demo.altemistamoviesusers.microservice.service.AltemistaMoviesUsers;

/**
 * The Class AltemistaMoviesRecommendationsController.
 */
@RestController
public class AltemistaMoviesRecommendationsController implements AltemistaMoviesRecommendations {

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/** The recommendation repository. */
	@Autowired
	private RecommendationRepository recommendationRepository;

	/** The micro movies. */
	@Autowired
	private AltemistaMoviesMovies microMovies;

	/** The micro users. */
	@Autowired
	private AltemistaMoviesUsers microUsers;

	/** The model mapper. */
	@Autowired
	private ModelMapper modelMapper;

	/** The target list type. */
	Type targetListType = new TypeToken<List<RecommendationDTO>>() {}.getType();

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.demo.altemistamoviesrecomend.microservice.service.AltemistaMoviesRecommendations#listAllRecommendations()
	 */
	@Override
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<RecommendationDTO>> listAllRecommendations() {
		List<Recommendation> recommendations = (List<Recommendation>) recommendationRepository.findAll();
		if(recommendations.isEmpty()){
			return new ResponseEntity<List<RecommendationDTO>>(HttpStatus.NO_CONTENT);
		}
		List<RecommendationDTO> recommendationsDTO = modelMapper.map(recommendations, targetListType);
		return new ResponseEntity<List<RecommendationDTO>>(recommendationsDTO, HttpStatus.OK);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.demo.altemistamoviesrecomend.microservice.service.AltemistaMoviesRecommendations#getRecommendation(long)
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<RecommendationDTO> getRecommendation(@PathVariable("id") long id) {
		logger.info("Fetching Recommendation with id " + id);
		Recommendation recommendation = recommendationRepository.findById(id).get();
		if (recommendation == null) {
			logger.info("Recommendation " + id + " not found");
			return new ResponseEntity<RecommendationDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RecommendationDTO>(modelMapper.map(recommendation, RecommendationDTO.class), HttpStatus.OK);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.demo.altemistamoviesrecomend.microservice.service.AltemistaMoviesRecommendations#createRecommendation(cloud.altemista.fwk.demo.altemistamoviesrecomend.microservice.domain.RecommendationDTO)
	 */
	@Override
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<RecommendationDTO> createRecommendation(@RequestBody RecommendationDTO recommendationDTO) {
		logger.info("Creating Recommendation by User with id " + recommendationDTO.getUserId() + " about Movie with id " + recommendationDTO.getMovieId());

		if (recommendationDTO.getId()!=null) {
			logger.info("The Recommendation ID must be null");
			return new ResponseEntity<RecommendationDTO>(HttpStatus.BAD_REQUEST);
		}

		List<Recommendation> findByUserIdAndMovieId = recommendationRepository.findByUserIdAndMovieId(recommendationDTO.getUserId(),recommendationDTO.getMovieId());
		if (findByUserIdAndMovieId!=null && !findByUserIdAndMovieId.isEmpty()) {
			logger.info("A Recommendation by User with id " + recommendationDTO.getUserId() + " about Movie with id " + recommendationDTO.getMovieId() + " already exist");
			return new ResponseEntity<RecommendationDTO>(HttpStatus.CONFLICT);
		}

		Recommendation savedRecommendation = recommendationRepository.save(modelMapper.map(recommendationDTO, Recommendation.class));
		RecommendationDTO savedRecommendationDTO = modelMapper.map(savedRecommendation, RecommendationDTO.class);

		return new ResponseEntity<RecommendationDTO>(savedRecommendationDTO, HttpStatus.CREATED);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.demo.altemistamoviesrecomend.microservice.service.AltemistaMoviesRecommendations#updateRecommendation(long, cloud.altemista.fwk.demo.altemistamoviesrecomend.microservice.domain.RecommendationDTO)
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<RecommendationDTO> updateRecommendation(@PathVariable("id") long id, @RequestBody RecommendationDTO recommendationDTO) {
		logger.info("Updating Recommendation " + id);

		if (recommendationDTO.getId()==null || id!=recommendationDTO.getId().longValue()){
			logger.info("Recommendation id " + recommendationDTO.getId() + " not equals id param "+id);
			return new ResponseEntity<RecommendationDTO>(HttpStatus.BAD_REQUEST);
		}
		if (recommendationRepository.findById(id)==null) {
			logger.info("Recommendation with id " + id + " not found");
			return new ResponseEntity<RecommendationDTO>(HttpStatus.NOT_FOUND);
		}

		recommendationDTO.setId(id);
		Recommendation savedRecommendation = recommendationRepository.save(modelMapper.map(recommendationDTO, Recommendation.class));
		recommendationDTO = modelMapper.map(savedRecommendation, RecommendationDTO.class);
		return new ResponseEntity<RecommendationDTO>(recommendationDTO, HttpStatus.OK);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.demo.altemistamoviesrecomend.microservice.service.AltemistaMoviesRecommendations#deleteRecommendation(long)
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<RecommendationDTO> deleteRecommendation(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting Recommendation with id " + id);

		if (recommendationRepository.findById(id) == null) {
			logger.info("Unable to delete. Recommendation with id " + id + " not found");
			return new ResponseEntity<RecommendationDTO>(HttpStatus.NOT_FOUND);
		}

		recommendationRepository.deleteById(id);
		return new ResponseEntity<RecommendationDTO>(HttpStatus.NO_CONTENT);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.demo.altemistamoviesrecomend.microservice.service.AltemistaMoviesRecommendations#findByUserID(java.lang.Long)
	 */
	@Override
	@RequestMapping(value = "/findByUserID/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<RecommendationDTO>> findByUserID(@PathVariable("userId") Long userId) {
		List<Recommendation> recommendationsByUserID = recommendationRepository.findByUserId(userId);

		Type targetListType = new TypeToken<List<RecommendationDTO>>() {}.getType();
		List<RecommendationDTO> recommendations = modelMapper.map(recommendationsByUserID, targetListType);

		for (RecommendationDTO recommendation : recommendations) {
			ResponseEntity<MovieDTO> movie = microMovies.getMovie(recommendation.getMovieId());
			MovieDTO movieDTO = movie.getBody();
			recommendation.setMovieTitle(movieDTO.getTitle());
			ResponseEntity<UserDTO> user = microUsers.getUser(recommendation.getUserId());
			UserDTO userDTO = user.getBody();
			recommendation.setUserFirstName(userDTO.getFirstName());
			recommendation.setUserLastName(userDTO.getLastName());
		}

		return new ResponseEntity<List<RecommendationDTO>>(recommendations, HttpStatus.OK);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.demo.altemistamoviesusers.microservice.service.AltemistaMoviesUsers#queryHealth()
	 */
	@Override
	@RequestMapping(value = "/queryHealth", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Map<String, String>> queryHealth() {
		return new ResponseEntity<Map<String, String>>(
				Collections.singletonMap("response", "AltemistaMoviesRecommendations microservice is Alive"),HttpStatus.OK);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.demo.altemistamoviesrecomend.microservice.service.AltemistaMoviesRecommendations#queryHealthAll()
	 */
	@Override
	@RequestMapping(value = "/queryHealthAll", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Map<String, String>> queryHealthAll() {
		
		StringBuilder result = new StringBuilder();
		
		try {
			ResponseEntity<Map<String, String>> queryHealthUsers = microUsers.queryHealth();
			result.append(queryHealthUsers.getBody().get("response")).append(";");
		} catch (Exception e) {
			result.append("AltemistaMoviesUsers is dead;");
		}
		
		try {
			ResponseEntity<Map<String, String>> queryHealthMovies = microMovies.queryHealth();
			result.append(queryHealthMovies.getBody().get("response")).append(";");
		} catch (Exception e) {
			result.append("AltemistaMoviesMovies is dead;");
		}
		
		ResponseEntity<Map<String, String>> queryHealth = queryHealth();
		
		result.append(queryHealth.getBody().get("response")).append(";");
		
		return new ResponseEntity<Map<String, String>>(
				Collections.singletonMap("response", result.toString()), HttpStatus.OK);
	}

}