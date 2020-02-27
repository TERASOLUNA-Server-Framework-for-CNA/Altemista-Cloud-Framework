package cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.controller;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import cloud.altemista.fwk.demo.altemistamoviesmovies.datacore.entity.Movie;
import cloud.altemista.fwk.demo.altemistamoviesmovies.datacore.repository.MovieRepository;
import cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.domain.MovieDTO;
import cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.service.AltemistaMoviesMovies;

/**
 * The Class AltemistaMoviesMoviesController.
 */
@RestController
public class AltemistaMoviesMoviesController implements AltemistaMoviesMovies {

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/** The movie repository. */
	@Autowired
	private MovieRepository movieRepository;

	/** The model mapper. */
	@Autowired
	private ModelMapper modelMapper;

	/** The target list type. */
	Type targetListType = new TypeToken<List<MovieDTO>>() {}.getType();

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.service.AltemistaMoviesMovies#listAllMovies()
	 */
	@Override
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<MovieDTO>> listAllMovies(Pageable page) {
		Page<Movie> findAllPage = movieRepository.findAll(page);
		List<Movie> movies = findAllPage.getContent();
		if(movies.isEmpty()){
			return new ResponseEntity<List<MovieDTO>>(HttpStatus.NO_CONTENT);
		}
		List<MovieDTO> moviesDTO = modelMapper.map(movies, targetListType);
		return new ResponseEntity<List<MovieDTO>>(moviesDTO, HttpStatus.OK);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.service.AltemistaMoviesMovies#getMovie(long)
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<MovieDTO> getMovie(@PathVariable("id") long id) {
		logger.info("Fetching Movie with id " + id);
		Movie movie = movieRepository.findById(id).get();
		if (movie == null) {
			logger.info("Movie " + id + " not found");
			return new ResponseEntity<MovieDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<MovieDTO>(modelMapper.map(movie, MovieDTO.class), HttpStatus.OK);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.service.AltemistaMoviesMovies#createMovie(cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.domain.MovieDTO)
	 */
	@Override
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieDTO movieDTO) {
		logger.info("Creating Movie " + movieDTO.getTitle());

		if (movieDTO.getId()!=null) {
			logger.info("The Movie ID must be null");
			return new ResponseEntity<MovieDTO>(HttpStatus.BAD_REQUEST);
		}

		List<Movie> findByTitle = movieRepository.findByTitle(movieDTO.getTitle());
		if (findByTitle!=null && !findByTitle.isEmpty()) {
			logger.info("A Movie with title " + movieDTO.getTitle() + " already exist");
			return new ResponseEntity<MovieDTO>(HttpStatus.CONFLICT);
		}

		Movie savedMovie = movieRepository.save(modelMapper.map(movieDTO, Movie.class));
		MovieDTO savedMovieDTO = modelMapper.map(savedMovie, MovieDTO.class);

		return new ResponseEntity<MovieDTO>(savedMovieDTO, HttpStatus.CREATED);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.service.AltemistaMoviesMovies#updateMovie(long, cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.domain.MovieDTO)
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<MovieDTO> updateMovie(@PathVariable("id") long id, @RequestBody MovieDTO movieDTO) {
		logger.info("Updating Movie " + id);

		if (movieDTO.getId()==null || id!=movieDTO.getId().longValue()){
			logger.info("Movie id " + movieDTO.getId() + " not equals id param "+id);
			return new ResponseEntity<MovieDTO>(HttpStatus.BAD_REQUEST);
		}
		if (movieRepository.findById(id)==null) {
			logger.info("Movie with id " + id + " not found");
			return new ResponseEntity<MovieDTO>(HttpStatus.NOT_FOUND);
		}

		movieDTO.setId(id);
		Movie savedMovie = movieRepository.save(modelMapper.map(movieDTO, Movie.class));
		movieDTO = modelMapper.map(savedMovie, MovieDTO.class);
		return new ResponseEntity<MovieDTO>(movieDTO, HttpStatus.OK);
	}

	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.demo.altemistamoviesmovies.microservice.service.AltemistaMoviesMovies#deleteMovie(long)
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<MovieDTO> deleteMovie(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting Movie with id " + id);

		if (movieRepository.findById(id) == null) {
			logger.info("Unable to delete. Movie with id " + id + " not found");
			return new ResponseEntity<MovieDTO>(HttpStatus.NOT_FOUND);
		}

		movieRepository.deleteById(id);
		return new ResponseEntity<MovieDTO>(HttpStatus.NO_CONTENT);
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.demo.altemistamoviesusers.microservice.service.AltemistaMoviesUsers#queryHealth()
	 */
	@Override
	@RequestMapping(value = "/queryHealth", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Map<String,String>> queryHealth() {
		return new ResponseEntity<Map<String,String>>(
				Collections.singletonMap("response", "AltemistaMoviesMovies microservice is Alive"),HttpStatus.OK);
	}

}
