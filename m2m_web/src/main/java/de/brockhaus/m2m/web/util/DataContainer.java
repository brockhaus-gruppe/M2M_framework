package de.brockhaus.m2m.web.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;

/**
 * Replaced by CircularBufferDataContainer ... 
 * 
 * Project: communication.web
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 4, 2015
 *
 */
@ApplicationScoped
public class DataContainer {
	
	// @see http://www.programcreek.com/2014/01/java-util-concurrentmodificationexception/
	private List<String> data = new CopyOnWriteArrayList<String>();
	
	public void addValue(String value) {
		data.add(value);
	}
	
	public void removeValue(String value) {
		data.remove(value);
	}
	
	public int getSize() {
		return data.size();
	}
	
	public List<String> getValues() {
		return this.data;
	}

}
