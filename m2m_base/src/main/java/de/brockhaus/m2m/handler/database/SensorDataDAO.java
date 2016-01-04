package de.brockhaus.m2m.handler.database;

import java.util.Date;
import java.util.List;

import de.brockhaus.m2m.message.M2MSensorMessage;

/**
 * Just the interface for all sensor data DAOs regardless which concrete storage is used.
 * 
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Mar 27, 2015
 *
 */
public interface SensorDataDAO {

	void insertSensorData(M2MSensorMessage data);
	
	void bulkInsertOfSensorData(List<M2MSensorMessage> list);
	
	List<M2MSensorMessage> findBySensorIDAndTimeInterval(String sensorId, Date from, Date to);
	
	List<M2MSensorMessage> findByTimeInterval(Date from, Date to);
}
