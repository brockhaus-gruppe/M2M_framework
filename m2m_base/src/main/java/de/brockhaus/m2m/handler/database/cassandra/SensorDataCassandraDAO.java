package de.brockhaus.m2m.handler.database.cassandra;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import de.brockhaus.m2m.handler.database.SensorDataDAO;
import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MSensorMessage;

/**
 * The concrete DAO for Cassandra
 * 
 * The DDL for the table is: 
 * 
 * CREATE TABLE sensor_data (
 * 		sensor_id text, 
 * 		datatype text, 
 * 		time timestamp, 
 * 		string_value text, 
 * 		boolean_value boolean, 
 * 		float_value float, 
 * 		PRIMARY KEY(sensor_id, time)
 * );
 * 
 * Obviously this is the most simple table design one can think of, we might change it later, when need a more 
 * sophisticated aggregation over the time line as well as a dedicated TTL 
 * 
 * Regarding the table design check: 
 * http://planetcassandra.org/getting-started-with-time-series-data-modeling/
 * http://stackoverflow.com/questions/29806707/data-scheme-cassandra-using-various-data-types
 * http://www.datastax.com/dev/blog/advanced-time-series-with-cassandra
 * 
 * TODO: configure TTL 4 individual sensors
 * TODO: enhance data model for hourly/daily/monthly rows
 * 
 * Example config:
 * 
	<bean name="cassandra-dao"
		class="de.brockhaus.m2m.handler.database.cassandra.SensorDataCassandraDAO" scope="singleton" 
		init-method = "init"
		destroy-method = "shutDown">
	
		<constructor-arg ref="cassandra-datasource" />
	</bean>
		
		
	<bean name="cassandra-datasource"
		class="de.brockhaus.m2m.handler.database.cassandra.CassandraDataSource" scope="singleton">
		
		<property name="hostIP">
			<value>127.0.0.1</value>
		</property>
		
		<property name="keyspace">
			<value>test</value>
		</property>
	</bean>
 * 
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author mbohnen, Mar 27, 2015
 *
 */
public class SensorDataCassandraDAO implements SensorDataDAO {

	private static final Logger LOG = Logger
			.getLogger(SensorDataCassandraDAO.class);

	private CassandraDataSource datasource;

	private Session session;

	public SensorDataCassandraDAO(CassandraDataSource datasource) {
		this.datasource = datasource;
	}

	public void insertSensorData(M2MSensorMessage data) {
		LOG.info("inserting");

		M2MDataType type = data.getDatatype();
		switch (type) {
		case BOOLEAN:
			this.insertBoolean(data);
			break;
		case FLOAT:
			this.insertFloat(data);
			break;
		case STRING:
			this.insertString(data);
		}
	}

	private void insertString(M2MSensorMessage data) {
		LOG.debug("inserting a string value");

		String cql = "INSERT INTO sensor_data(SENSOR_ID, DATATYPE, TIME, STRING_VALUE) VALUES(?, ?, ?, ?)";

		PreparedStatement pStat = session.prepare(cql);
		BoundStatement bStat = new BoundStatement(pStat);
		bStat.bind(data.getSensorId(), data.getDatatype().toString(), data.getTime(), data.getValue());
		session.execute(bStat);
	}

	private void insertFloat(M2MSensorMessage data) {
		Float value = new Float(data.getValue());
		LOG.debug("inserting a float value: " + value);

		String cql = "INSERT INTO sensor_data(SENSOR_ID, DATATYPE, TIME, FLOAT_VALUE) VALUES(?, ?, ?, ?)";

		PreparedStatement pStat = session.prepare(cql);
		BoundStatement bStat = new BoundStatement(pStat);
		bStat.bind(data.getSensorId(), data.getDatatype().toString(), data.getTime(), value);
		session.execute(bStat);

	}

	private void insertBoolean(M2MSensorMessage data) {
		LOG.debug("inserting a boolean value");

		String cql = "INSERT INTO sensor_data(SENSOR_ID, DATATYPE, TIME, BOOLEAN_VALUE) VALUES(?, ?, ?, ?)";

		PreparedStatement pStat = session.prepare(cql);
		BoundStatement bStat = new BoundStatement(pStat);
		bStat.bind(data.getSensorId(), data.getDatatype(), data.getTime(),
				new Boolean(data.getValue()));
		session.execute(bStat);

	}

	public void bulkInsertOfSensorData(List<M2MSensorMessage> list) {
		
		for (M2MSensorMessage sensorData : list) {
			this.insertSensorData(sensorData);
		}	
	}

	public List<M2MSensorMessage> findBySensorIDAndTimeInterval(
			String sensorId, Date from, Date to) {

		List<M2MSensorMessage> tos = new ArrayList<M2MSensorMessage>();

		// TODO: checkout if this is necessary
		Timestamp fromTs = new Timestamp(from.getTime());
		Timestamp toTs = new Timestamp(to.getTime());

		String cql = "SELECT * FROM sensor_data WHERE SENSOR_ID = ? AND TIME > ? AND TIME < ?";

		PreparedStatement pStat = session.prepare(cql);
		BoundStatement bStat = new BoundStatement(pStat);
		bStat.bind(sensorId, fromTs, toTs);

		ResultSet res = session.execute(bStat);

		for (Row row : res) {
			M2MSensorMessage sensorData = new M2MSensorMessage();
			sensorData.setDatatype(M2MDataType.valueOf(row
					.getString("DATATYPE")));
			sensorData.setSensorId(row.getString("SENSOR_ID"));
			sensorData.setTime(row.getDate("TIME"));
			sensorData.setValue(row.getString("VALUE"));

			tos.add(sensorData);
		}

		return tos;
	}

	public List<M2MSensorMessage> findByTimeInterval(Date from, Date to) {

		List<M2MSensorMessage> tos = new ArrayList<M2MSensorMessage>();

		// TODO: checkout if this is necessary
		Timestamp fromTs = new Timestamp(from.getTime());
		Timestamp toTs = new Timestamp(to.getTime());

		String cql = "SELECT * FROM sensor_data WHERE TIME > ? AND TIME < ?";

		PreparedStatement pStat = session.prepare(cql);
		BoundStatement bStat = new BoundStatement(pStat);
		bStat.bind(fromTs, toTs);

		ResultSet res = session.execute(bStat);

		for (Row row : res) {
			M2MSensorMessage sensorData = new M2MSensorMessage();
			sensorData.setDatatype(M2MDataType.valueOf(row
					.getString("DATATYPE")));
			sensorData.setSensorId(row.getString("SENSOR_ID"));
			sensorData.setTime(row.getDate("TIME"));
			sensorData.setValue(row.getString("VALUE"));

			tos.add(sensorData);
		}

		return tos;
	}
	
	
	public void init() {
		LOG.debug("init");
		session = datasource.connect();
	}
	
	// housekeeping
	public void shutDown() {
		LOG.debug("cleaning up");
		this.session.close();
	}

}
