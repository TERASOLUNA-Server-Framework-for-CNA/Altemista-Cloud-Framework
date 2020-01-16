package org.altemista.cloudfwk.demo.altemistamoviesrecomend.datacore.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.altemista.cloudfwk.demo.altemistamoviesrecomend.datacore.entity.Recommendation;

/**
 * The Interface RecommendationRepository.
 */
public interface RecommendationRepository extends PagingAndSortingRepository<Recommendation, Long> {

	/**
	 * Find by user id.
	 *
	 * @param userId the user id
	 * @return the list
	 */
	List<Recommendation> findByUserId(Long userId);

	/**
	 * Find by movie id.
	 *
	 * @param movieId the movie id
	 * @return the list
	 */
	List<Recommendation> findByMovieId(Long movieId);

	/**
	 * Find by user id and movie id.
	 *
	 * @param userId the user id
	 * @param movieId the movie id
	 * @return the list
	 */
	List<Recommendation> findByUserIdAndMovieId(Long userId, Long movieId);

}
