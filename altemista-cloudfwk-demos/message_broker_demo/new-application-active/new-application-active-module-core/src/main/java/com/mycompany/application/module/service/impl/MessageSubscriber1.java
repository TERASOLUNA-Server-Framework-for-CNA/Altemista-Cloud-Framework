package com.mycompany.application.module.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MessageSubscriber1 implements MessageListener {

  @Override
  public void onMessage(Message message) {
    if (message instanceof TextMessage) {
      try {
        String msg = ((TextMessage) message).getText();
        System.out.println("Message consumed by MessageSubscriber1 : " + msg);
      } catch (JMSException ex) {
        throw new RuntimeException(ex);
      }
    } else {
      throw new IllegalArgumentException("Message must be of type TextMessage");
    }
  }

}