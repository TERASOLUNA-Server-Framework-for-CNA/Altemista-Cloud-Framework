package org.altemista.cloudfwk.demo.altemistamoviesrecomend.microservice.domain;

/**
 * The Class RecommendationDTO.
 */
public class RecommendationDTO {
	
	/** The id. */
	Long id;
	
	/** The user id. */
	private Long userId;
	
	/** The user first name. */
	private String userFirstName;
	
	/** The user last name. */
	private String userLastName;
	
	/** The movie id. */
	private Long movieId;
	
	/** The movie title. */
	private String movieTitle;
	
	/** The comment. */
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
	 * Gets the user first name.
	 *
	 * @return the user first name
	 */
	public String getUserFirstName() {
		return userFirstName;
	}
	
	/**
	 * Sets the user first name.
	 *
	 * @param userFirstName the new user first name
	 */
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	
	/**
	 * Gets the user last name.
	 *
	 * @return the user last name
	 */
	public String getUserLastName() {
		return userLastName;
	}
	
	/**
	 * Sets the user last name.
	 *
	 * @param userLastName the new user last name
	 */
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
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
	 * Gets the movie title.
	 *
	 * @return the movie title
	 */
	public String getMovieTitle() {
		return movieTitle;
	}
	
	/**
	 * Sets the movie title.
	 *
	 * @param movieTitle the new movie title
	 */
	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
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
