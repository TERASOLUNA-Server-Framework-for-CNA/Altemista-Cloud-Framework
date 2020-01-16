package org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.domain.UserDTO;

/**
 * The Interface AltemistaMoviesUsers.
 */
@FeignClient("AltemistaMoviesUsers")
public interface AltemistaMoviesUsers {

	/**
	 * List all users.
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	ResponseEntity<List<UserDTO>> listAllUsers();

	/**
	 * Gets the user.
	 *
	 * @param id the id
	 * @return the user
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody ResponseEntity<UserDTO> getUser(@PathVariable("id") long id);

	/**
	 * Creates the user.
	 *
	 * @param userDTO the user DTO
	 * @return the response entity
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO);

	/**
	 * Update user.
	 *
	 * @param id the id
	 * @param userDTO the user DTO
	 * @return the response entity
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	ResponseEntity<UserDTO> updateUser(@PathVariable("id") long id, @RequestBody UserDTO userDTO);

	/**
	 * Delete user.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	ResponseEntity<UserDTO> deleteUser(@PathVariable("id") long id);
	
	/**
	 * Query health.
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/queryHealth", method = RequestMethod.GET)
	@ResponseBody ResponseEntity<Map<String, String>> queryHealth();

}
