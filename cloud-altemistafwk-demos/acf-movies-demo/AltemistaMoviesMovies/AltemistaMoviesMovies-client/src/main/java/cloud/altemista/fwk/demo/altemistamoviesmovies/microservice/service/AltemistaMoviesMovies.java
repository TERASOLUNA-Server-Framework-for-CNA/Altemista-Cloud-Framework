package cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.domain.MovieDTO;

/**
 * The Interface AltemistaMoviesMovies.
 */
@FeignClient("AltemistaMoviesMovies")
public interface AltemistaMoviesMovies {

	/**
	 * List all movies.
	 *
	 * @param page the page
	 * @return the response entity
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	ResponseEntity<List<MovieDTO>> listAllMovies(Pageable page);

	/**
	 * Gets the movie.
	 *
	 * @param id the id
	 * @return the movie
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	ResponseEntity<MovieDTO> getMovie(@PathVariable("id") long id);

	/**
	 * Creates the movie.
	 *
	 * @param movieDTO the movie DTO
	 * @return the response entity
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	ResponseEntity<MovieDTO> createMovie(@RequestBody MovieDTO movieDTO);

	/**
	 * Update movie.
	 *
	 * @param id the id
	 * @param movieDTO the movie DTO
	 * @return the response entity
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	ResponseEntity<MovieDTO> updateMovie(@PathVariable("id") long id, @RequestBody MovieDTO movieDTO);

	/**
	 * Delete movie.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	ResponseEntity<MovieDTO> deleteMovie(@PathVariable("id") long id);
	
	/**
	 * Query health.
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/queryHealth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody ResponseEntity<Map<String, String>> queryHealth();

}
