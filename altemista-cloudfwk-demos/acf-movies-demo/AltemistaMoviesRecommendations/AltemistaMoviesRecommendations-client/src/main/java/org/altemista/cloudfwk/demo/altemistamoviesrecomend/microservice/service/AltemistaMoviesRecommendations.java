package org.altemista.cloudfwk.demo.altemistamoviesrecomend.microservice.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.altemista.cloudfwk.demo.altemistamoviesrecomend.microservice.domain.RecommendationDTO;

/**
 * The Interface AltemistaMoviesRecommendations.
 */
@FeignClient("AltemistaMoviesRecommendations")
public interface AltemistaMoviesRecommendations {

	/**
	 * List all recommendations.
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	ResponseEntity<List<RecommendationDTO>> listAllRecommendations();

	/**
	 * Gets the recommendation.
	 *
	 * @param id the id
	 * @return the recommendation
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	ResponseEntity<RecommendationDTO> getRecommendation(@PathVariable("id") long id);

	/**
	 * Creates the recommendation.
	 *
	 * @param recommendationDTO the recommendation DTO
	 * @return the response entity
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	ResponseEntity<RecommendationDTO> createRecommendation(@RequestBody RecommendationDTO recommendationDTO);

	/**
	 * Update recommendation.
	 *
	 * @param id the id
	 * @param recommendationDTO the recommendation DTO
	 * @return the response entity
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	ResponseEntity<RecommendationDTO> updateRecommendation(@PathVariable("id") long id, @RequestBody RecommendationDTO recommendationDTO);

	/**
	 * Delete recommendation.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	ResponseEntity<RecommendationDTO> deleteRecommendation(@PathVariable("id") long id);

	/**
	 * Find by user ID.
	 *
	 * @param userId the user id
	 * @return the response entity
	 */
	@RequestMapping(value = "/findByUserID/{userId}", method = RequestMethod.GET)
	ResponseEntity<List<RecommendationDTO>> findByUserID(@PathVariable("userId") Long userId);
	
	/**
	 * Query health.
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/queryHealth", method = RequestMethod.GET)
	@ResponseBody ResponseEntity<Map<String, String>> queryHealth();
	
	
	/**
	 * Query health all.
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/queryHealthAll", method = RequestMethod.GET)
	@ResponseBody ResponseEntity<Map<String, String>> queryHealthAll();

}