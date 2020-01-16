/**
 * Interfaces of the exposed services of module.
 */
package com.mycompany.application.module.service;

public interface RabbitMQClient {

	  public void sendMessage(String message);

	  public void receiveMessage();
	  
	  public void publishMessage(String message);

	  public void subcribeMessages() ;
	  
}