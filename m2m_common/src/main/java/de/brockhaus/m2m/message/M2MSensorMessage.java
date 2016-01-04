package de.brockhaus.m2m.message;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * Project: comcommon
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 16, 2015
 *
 */
public class M2MSensorMessage extends M2MMessage {
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:mm:ss", timezone="CET")
	private Date time;
	
	private M2MDataType datatype;
	
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

}
