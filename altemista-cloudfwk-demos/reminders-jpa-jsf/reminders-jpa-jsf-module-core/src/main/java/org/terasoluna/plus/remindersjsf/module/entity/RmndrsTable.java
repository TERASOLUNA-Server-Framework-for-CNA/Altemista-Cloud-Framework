package org.altemista.cloudfwk.remindersjsf.module.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author NTT DATA
 * 
 * Main Entity which models each reminder
 *
 */
@Entity
@Table(name = "RMNDRS_TABLE")
public class RmndrsTable implements Serializable {

	private static final long serialVersionUID = -5465484691998447463L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
    private Long id;

	@Column(name="USERNAME")
    private String username;

	@Column(name="TEXT")
    private String text;

	@Column(name="DUE_DATE")
    private Date dueDate;
	
	@Column(name="COMPLETED")
	private boolean completed;
	
	
	// Getters and Setters
	
	public boolean getCompleted() {
		return completed;
	}
	
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

}