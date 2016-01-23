package de.brockhaus.m2m.message;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The message abstraction we will send around ...
 * Later on we might add fields common to all messages
 *
 * Project: m2m-common
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 22, 2016
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

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
		         .append(this.getSensorId())
		         .toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		
		if(! (other instanceof M2MMessage)) {
			return false;
		}
		
	    return new EqualsBuilder()
	    	         .append(this.getSensorId(), ((M2MMessage) other).getSensorId())
	    	         .isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(this.getSensorId()).toString();
	}
}
