package org.altemista.cloudfwk.demo.altemistamoviesrecomend.datacore.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * The Class Recommendation.
 */
@Entity
public class Recommendation {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	/** The user id. */
	@NotNull
	private Long userId;

	/** The movie id. */
	@NotNull
	private Long movieId;

	/** The comment. */
	@NotNull
	private String comment;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the movie id.
	 *
	 * @return the movie id
	 */
	public Long getMovieId() {
		return movieId;
	}

	/**
	 * Sets the movie id.
	 *
	 * @param movieId the new movie id
	 */
	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	/**
	 * Gets the comment.
	 *
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the comment.
	 *
	 * @param comment the new comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

}
