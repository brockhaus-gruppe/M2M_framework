package de.brockhaus.m2m.handler.database.rdbms.jdbc;

import java.util.Date;
import java.util.List;

import de.brockhaus.m2m.handler.database.SensorDataDAO;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.util.M2MJdbcDataSource;

/**
 * TODO implement
 * 
 * Supports a flat tabular structure only. For more sophisticated things check the JPA
 * handler.
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 26, 2015
 *
 */
public class SensorDataJdbcDAO implements SensorDataDAO {
	
	private M2MJdbcDataSource datasource;

	@Override
	public void insertSensorData(M2MSensorMessage data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bulkInsertOfSensorData(List<M2MSensorMessage> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<M2MSensorMessage> findBySensorIDAndTimeInterval(String sensorId, Date from, Date to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<M2MSensorMessage> findByTimeInterval(Date from, Date to) {
		// TODO Auto-generated method stub
		return null;
	}

}
