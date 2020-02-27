/**
 * 
 */
package cloud.altemista.fwk.test.rules.model;

/*
 * #%L
 * altemista-cloud rules engine: Drools implementation CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.ArrayList;
import java.util.List;

/**
 * Simple bean for testing purposes only
 * @author NTT DATA
 */
public class DummyBean {

	private int number;
	
	private boolean flag;
	
	private List<String> messages = new ArrayList<String>();
	
	/**
	 * Default constructor
	 */
	public DummyBean() {
		super();
	}
	
	/**
	 * Adds a message
	 * @param message String with the message
	 */
	public void addMessage(String message) {
		
		this.messages.add(message);
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/**
	 * @return the messages
	 */
	public List<String> getMessages() {
		return messages;
	}

}
