package de.brockhaus.m2m.message;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 *
 * Project: m2m-common
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 23, 2016
 *
 */
public class M2MSensorMessage extends M2MMessage {
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:mm:ss", timezone="CET")
	private Date time;
	
	private M2MDataType datatype;
	
	// every type will be represented as a string
	private String value;

	
	public M2MSensorMessage() {
		super();
	}

	public M2MSensorMessage(String sensorid, Date time, M2MDataType datatype,
			String value) {
		super();
		super.sensorId = sensorid;
		this.time = time;
		this.datatype = datatype;
		this.value = value;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public M2MDataType getDatatype() {
		return datatype;
	}

	public void setDatatype(M2MDataType datatype) {
		this.datatype = datatype;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.appendSuper(super.hashCode())
		        .append(this.getTime())
		        .append(this.getValue())
		        .toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		
		if(! (other instanceof M2MSensorMessage)) {
			return false;
		}
		
	    return new EqualsBuilder()
	    	         .appendSuper(super.equals(other))
	    	         .append(this.getTime(), ((M2MSensorMessage) other).getTime())
	    	         .append(this.getValue(), ((M2MSensorMessage) other).getValue())
	    	         .isEquals();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append(this.getTime())
				.append(this.getValue())
				.append(this.getDatatype())
				.toString();
	}
}
