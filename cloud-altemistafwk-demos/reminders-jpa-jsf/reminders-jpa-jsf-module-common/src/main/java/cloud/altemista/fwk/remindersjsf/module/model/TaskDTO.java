package cloud.altemista.fwk.remindersjsf.module.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

/**
 * A reminder
 * @author NTT DATA
 */
@Validated
public class TaskDTO implements Serializable {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 4004439976889916661L;

	/** Marker ID value for new tasks */
	public static final long NO_ID = -1L;
	
	/** The identifier for the task */
	private long id;
	
	/** The name of the task */
	@NotNull
	private String name;
	
	/** The date when this task is to be completed */
	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date date;
	
	/** Flag to indicate the task has been completed */
	@NotNull
	private boolean completed;
	
	/**
	 * Default constructor
	 */
	public TaskDTO() {
		super();
		
		this.id = -1;
		this.name = "";
		this.date = new Date();
		this.completed = false;
	}

	/**
	 * Parameterized constructor 
	 * @param id long
	 * @param name String
	 * @param date Date
	 * @param completed boolean
	 */
	public TaskDTO(long id, String name, Date date, boolean completed) {
		super();
		
		this.id = id;
		this.name = name;
		this.date = date;
		this.completed = completed;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}
