package org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.controller;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.altemista.cloudfwk.demo.altemistamoviesusers.datacore.entity.User;
import org.altemista.cloudfwk.demo.altemistamoviesusers.datacore.repository.UserRepository;
import org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.domain.UserDTO;
import org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.service.AltemistaMoviesUsers;

/**
 * The Class AltemistaMoviesUsersController.
 */
@RestController
public class AltemistaMoviesUsersController implements AltemistaMoviesUsers {

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/** The user repository. */
	@Autowired
	private UserRepository userRepository;

	/** The model mapper. */
	@Autowired
	private ModelMapper modelMapper;

	/** The target list type. */
	Type targetListType = new TypeToken<List<UserDTO>>() {}.getType();

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.service.AltemistaMoviesUsers#listAllUsers()
	 */
	@Override
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<UserDTO>> listAllUsers() {
		List<User> users = (List<User>) userRepository.findAll();
		if(users.isEmpty()){
			return new ResponseEntity<List<UserDTO>>(HttpStatus.NO_CONTENT);
		}
		List<UserDTO> usersDTO = modelMapper.map(users, targetListType);
		return new ResponseEntity<List<UserDTO>>(usersDTO, HttpStatus.OK);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.service.AltemistaMoviesUsers#getUser(long)
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<UserDTO> getUser(@PathVariable("id") long id) {
		logger.info("Fetching User with id " + id);
		User user = userRepository.findById(id).get();
		if (user == null) {
			logger.info("User " + id + " not found");
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UserDTO>(modelMapper.map(user, UserDTO.class), HttpStatus.OK);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.service.AltemistaMoviesUsers#createUser(org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.domain.UserDTO)
	 */
	@Override
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
		logger.info("Creating User " + userDTO.getFirstName() + " " + userDTO.getLastName());

		if (userDTO.getId()!=null) {
			logger.info("The User ID must be null");
			return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
		}

		List<User> findByFirstNameAndLastName = userRepository.findByFirstNameAndLastName(userDTO.getFirstName(),userDTO.getLastName());
		if (findByFirstNameAndLastName!=null && !findByFirstNameAndLastName.isEmpty()) {
			logger.info("A User with name " + userDTO.getFirstName() + " " + userDTO.getLastName() + " already exist");
			return new ResponseEntity<UserDTO>(HttpStatus.CONFLICT);
		}

		User savedUser = userRepository.save(modelMapper.map(userDTO, User.class));
		UserDTO savedUserDTO = modelMapper.map(savedUser, UserDTO.class);

		return new ResponseEntity<UserDTO>(savedUserDTO, HttpStatus.CREATED);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.service.AltemistaMoviesUsers#updateUser(long, org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.domain.UserDTO)
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UserDTO> updateUser(@PathVariable("id") long id, @RequestBody UserDTO userDTO) {
		logger.info("Updating User " + id);

		if (userDTO.getId()==null || id!=userDTO.getId().longValue()){
			logger.info("User id " + userDTO.getId() + " not equals id param "+id);
			return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);
		}
		if (userRepository.findById(id)==null) {
			logger.info("User with id " + id + " not found");
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}

		userDTO.setId(id);
		User savedUser = userRepository.save(modelMapper.map(userDTO, User.class));
		userDTO = modelMapper.map(savedUser, UserDTO.class);
		return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
	}

	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.service.AltemistaMoviesUsers#deleteUser(long)
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<UserDTO> deleteUser(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting User with id " + id);

		if (userRepository.findById(id) == null) {
			logger.info("Unable to delete. User with id " + id + " not found");
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}

		userRepository.deleteById(id);
		return new ResponseEntity<UserDTO>(HttpStatus.NO_CONTENT);
	}
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.demo.altemistamoviesusers.microservice.service.AltemistaMoviesUsers#queryHealth()
	 */
	@Override
	@RequestMapping(value = "/queryHealth", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Map<String,String>> queryHealth() {
		return new ResponseEntity<Map<String,String>>(
				Collections.singletonMap("response", "AltemistaMoviesUsers microservice is Alive"),HttpStatus.OK);
	}

}
