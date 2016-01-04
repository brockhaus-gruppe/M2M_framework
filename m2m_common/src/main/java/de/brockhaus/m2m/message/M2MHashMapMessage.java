package de.brockhaus.m2m.message;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 *
 * Project: m2m-common
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 7, 2015
 *
 */
public class M2MHashMapMessage implements Serializable {
	
	private HashMap<String, M2MSensorMessage> messages = new HashMap<String, M2MSensorMessage>();

	public HashMap<String, M2MSensorMessage> getMessages() {
		return messages;
	}

	public void setMessages(HashMap<String, M2MSensorMessage> messages) {
		this.messages = messages;
	}

	public void addMessage(M2MSensorMessage message) {
		this.messages.put(message.getSensorId(), message);
	}
}
