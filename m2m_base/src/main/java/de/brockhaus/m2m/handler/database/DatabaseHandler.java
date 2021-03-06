package de.brockhaus.m2m.handler.database;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;

/**
 * 
 * Storing the sensor data out of a M2MMultiMessage into a database.
 * The concrete database is injected, you might make use of the SensorDataDAO interface.
 * 
 * Example config:
 * 
 	<!-- storing sensor message 2 persistent storage -->
	<bean name="db-handler"
		class="de.brockhaus.m2m.handler.database.DatabaseHandler" scope="singleton">

		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.handler.M2MMultiMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.handler.M2MMultiMessage</value>
    	</constructor-arg>
    	
    	<!-- which dao is to be used? -->
    	<property name="dao" ref = "dummy-dao" />
			  	
	</bean>
	
	<!-- ********************************** utilities and other ********************************** -->
	<bean name="dummy-dao"
		class="de.brockhaus.m2m.handler.database.dummy.SensorDataDummyDAO" scope="singleton" />
		
	<bean name="cassandra-dao"
		class="de.brockhaus.m2m.handler.database.cassandra.SensorDataCassandraDAO" scope="singleton" 
		init-method = "init"
		destroy-method = "shutDown">
	
		<constructor-arg ref="cassandra-config" />
	</bean>
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 11, 2015
 *
 */
public class DatabaseHandler extends AbstractM2MMessageHandler implements M2MMessageHandler {
	
	private static final Logger LOG = Logger.getLogger(DatabaseHandler.class);
	
	// the dao used
	private SensorDataDAO dao;

	public DatabaseHandler() {
		super();
		
	}

	public DatabaseHandler(String inTypeClassName, String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message");
		if(message instanceof M2MMultiMessage) {
			this.dao.bulkInsertOfSensorData(((M2MMultiMessage) message).getSensorDataMessageList());
		} else {
			this.dao.insertSensorData((M2MSensorMessage) message);
		}
	}

	public SensorDataDAO getDao() {
		return dao;
	}

	public void setDao(SensorDataDAO dao) {
		this.dao = dao;
	}
}
