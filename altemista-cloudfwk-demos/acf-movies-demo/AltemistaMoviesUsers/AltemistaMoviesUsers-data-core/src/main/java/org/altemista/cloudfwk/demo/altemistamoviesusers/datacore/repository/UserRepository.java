package org.altemista.cloudfwk.demo.altemistamoviesusers.datacore.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.altemista.cloudfwk.demo.altemistamoviesusers.datacore.entity.User;

/**
 * The Interface UserRepository.
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	/**
	 * Find by last name.
	 *
	 * @param lastName the last name
	 * @return the list
	 */
	List<User> findByLastName(@Param("lastName") String lastName);

	/**
	 * Find by first name and last name.
	 *
	 * @param firstName the first name
	 * @param lastName the last name
	 * @return the list
	 */
	List<User> findByFirstNameAndLastName(String firstName, String lastName);

}
