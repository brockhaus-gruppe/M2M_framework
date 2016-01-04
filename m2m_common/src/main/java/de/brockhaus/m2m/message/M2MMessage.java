package de.brockhaus.m2m.message;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The message abstraction we will send around ...
 * Later on we might add fields common to all messages
 * 
 * Project: comcommon
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 10, 2015
 *
 */
@XmlRootElement
public abstract class M2MMessage implements Serializable {

	protected String sensorId;

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}
}
