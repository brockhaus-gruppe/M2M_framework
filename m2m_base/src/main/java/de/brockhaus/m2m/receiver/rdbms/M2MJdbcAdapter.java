package de.brockhaus.m2m.receiver.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.util.M2MJdbcDataSource;

/**
 * Obviously no message will come in but a multi message will be produced.
 * Triggered by a schedule, the relevant query will be executed in an interval. 
 * 
 * TODO enhance query by parameters
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 11, 2015
 *
 */
public class M2MJdbcAdapter extends AbstractM2MMessageHandler implements Runnable {
	
	// just a logger
	private static final Logger LOG = Logger.getLogger(M2MJdbcAdapter.class);

	// the home grown data source
	private M2MJdbcDataSource datasource;
	
	// a connection to a database
	private Connection con;
	
	// only a static query by now
	private String query;
	
	// the field mapping of the M2MSensorMessage and the ResultSet
	private Map<String, String> sensorMessageMapping = new HashMap<String, String>();
	
	// setting the last access so we can get new data only
	private Date lastAccess;

	
	public M2MJdbcAdapter() {
		super();
	}

	public M2MJdbcAdapter(M2MMessageHandler next, String inTypeClassName, String outTypeClassName) {
		super(next, inTypeClassName, outTypeClassName);
	}

	public void init() {
		try {
			con = this.datasource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message");
		try {
			super.doChain(message);
		} catch (M2MCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void doQuery() {
		
		this.lastAccess = new Date(System.currentTimeMillis());
		LOG.debug("Querying: " + query);
		
		M2MMultiMessage multi = null;
		try {
			PreparedStatement stat = this.con.prepareStatement(this.query);
			
			ResultSet res = stat.executeQuery();
			
			multi = new M2MMultiMessage();
			while(res.next()) {
				M2MSensorMessage msg = new M2MSensorMessage();
				
				// check the mapping within this spring bean!
				msg.setSensorId(res.getString(this.sensorMessageMapping.get("sensorId")));
				msg.setDatatype(M2MDataType.valueOf(res.getString(this.sensorMessageMapping.get("datatype"))));
				msg.setTime(res.getTimestamp(this.sensorMessageMapping.get("time")));
				msg.setValue(res.getString(this.sensorMessageMapping.get("value")));
				
				multi.getSensorDataMessageList().add(msg);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.handleMessage(multi);	
	}

	@Override
	public void run() {
		this.doQuery();
	}
	
	// getters and setters as needed by Spring DI
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public M2MJdbcDataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(M2MJdbcDataSource datasource) {
		this.datasource = datasource;
	}

	public Map<String, String> getSensorMessageMapping() {
		return sensorMessageMapping;
	}

	public void setSensorMessageMapping(Map<String, String> sensorMessageMapping) {
		this.sensorMessageMapping = sensorMessageMapping;
	}
}
