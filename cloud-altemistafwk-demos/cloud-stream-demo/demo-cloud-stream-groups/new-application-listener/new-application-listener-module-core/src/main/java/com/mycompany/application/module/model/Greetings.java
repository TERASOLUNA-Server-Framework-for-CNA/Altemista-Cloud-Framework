package com.mycompany.application.module.model;

public class Greetings {
	private long timestamp;
    private String message;
    private String day;
    private String broker;
    
    public Greetings(long timestamp,String message, String day, String broker) {
		this.timestamp = timestamp;
		this.message = message;
		this.day = day;
		this.broker = broker;
	} 
 
    public Greetings() {
    }
    
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}

	@Override
	public String toString() {
		return "Greetings [timestamp=" + timestamp + ", message=" + message + 
				", day=" + day + ", broker=" + broker + "]";
	}
	

}
