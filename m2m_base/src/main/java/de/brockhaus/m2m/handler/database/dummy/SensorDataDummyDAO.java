package de.brockhaus.m2m.handler.database.dummy;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.database.SensorDataDAO;
import de.brockhaus.m2m.message.M2MSensorMessage;

/**
 * Just a dummy implementation for testing purposes only.
 * 
 * Example config:
 * 
	<bean name="dummy-dao"
		class="de.brockhaus.m2m.handler.database.dummy.SensorDataDummyDAO" scope="singleton" />
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 1, 2015
 *
 */
public class SensorDataDummyDAO implements SensorDataDAO {
	
	private static final Logger LOG = Logger.getLogger(SensorDataDummyDAO.class);

	public void insertSensorData(M2MSensorMessage data) {
		LOG.info("insert: " + data.getSensorId() + " -> " + data.getValue());
		
	}

	public void bulkInsertOfSensorData(List<M2MSensorMessage> list) {
		LOG.info("bulk insert of " + list.size() + " sensor messages");
		
	}

	public List<M2MSensorMessage> findBySensorIDAndTimeInterval(
			String sensorId, Date from, Date to) {
		LOG.info("find by ID and time interval");
		return null;
	}

	public List<M2MSensorMessage> findByTimeInterval(Date from, Date to) {
		LOG.info("find by timer intervall");
		return null;
	}
}
