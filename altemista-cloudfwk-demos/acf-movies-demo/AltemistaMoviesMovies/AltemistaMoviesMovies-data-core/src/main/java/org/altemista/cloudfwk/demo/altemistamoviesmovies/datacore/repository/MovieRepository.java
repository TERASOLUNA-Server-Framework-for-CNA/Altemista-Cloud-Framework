package org.altemista.cloudfwk.demo.altemistamoviesmovies.datacore.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.altemista.cloudfwk.demo.altemistamoviesmovies.datacore.entity.Movie;

/**
 * The Interface MovieRepository.
 */
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long> {
    
	/**
	 * Find by title.
	 *
	 * @param title the title
	 * @return the list
	 */
	List<Movie> findByTitle(@Param("title") String title);
	
}