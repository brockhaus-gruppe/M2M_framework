package de.brockhaus.m2m.handler.database.cassandra;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

/**
 * 
 * Somehow a DataSource with slightly extended functionality ...
 * 
 * For data scheme related to time series data see: 
 * https://academy.datastax.com/demos/getting-started-time-series-data-modeling
 * http://de.slideshare.net/patrickmcfadin/apache-cassandra-apache-spark-for-time-series-data
 * http://www.bigdatatidbits.cc/2014/02/cassandra-modeling-with-cql-for-simple.html
 * 
 * TODO enhance PK with more sophisticated date and introduce TTL (preferably on a per-sensor base)
 * 
 * Example config:
 * 
 	<bean name="cassandra-config"
		class="de.brockhaus.m2m.handler.database.cassandra.CassandraDBUtil" scope="singleton">
		
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
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Mar 27, 2015
 *
 */
public class CassandraDataSource {
	
	// just a logger
	private static final Logger LOG = Logger.getLogger(CassandraDataSource.class);
		
	// are we connected?
	private boolean connected;
	
	// where to connect to?
	private String hostIP = "127.0.0.1";
	
	//similar to database
	private String keyspace = "test";
	
	private Cluster cluster;
	private Session session;
	

	/**
	 * Connects to a given IP
	 * @param node
	 */
	public Session connect(String node, String keyspace) {
		
		this.keyspace = keyspace;
		cluster = Cluster.builder().addContactPoint(node).build();
		session = cluster.connect(keyspace);
		
		Metadata metadata = cluster.getMetadata();
		LOG.debug(System.out.printf("Connected to cluster: %s\n ", metadata.getClusterName()));
		
		for (Host host : metadata.getAllHosts()) {
			LOG.trace(System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack()));
		}
		
		this.connected = true;
		
		return this.session;
	}
	
	/**
	 * 
	 * @return
	 */
	public Session connect() {
		return this.connect(this.hostIP, this.keyspace);
	}
	
	/**
	 * Example: CREATE TABLE sensor_data ( sensor_id text, datatype text, time timestamp, value text,  PRIMARY KEY(sensor_id, time));
	 * 
	 * @param tableName
	 * @param pk
	 * @param columns
	 */
	public void createTableWithCompoundKey(String tableName, List<String> pk, Map<String, CassandraDataType> columns) {

		if(! connected) {
			this.connect();
		}
		
		StringBuffer cql = new StringBuffer();
		cql.append("CREATE TABLE ");
		cql.append(tableName);
		cql.append(" ( ");
		
		for (String column : columns.keySet()) {
			cql.append(column + " " + columns.get(column).toString().toLowerCase());	
			cql.append(", ");
		}
		
		cql.append(" PRIMARY KEY(");
		Iterator<String> iter = pk.iterator();
		while(iter.hasNext()) {
			cql.append(iter.next());
			if(!iter.hasNext()) {
				break;
			}
			cql.append(", ");
		}
		cql.append("));");

		
		System.out.println(cql.toString());
		
		this.createTable(cql.toString());
		
	}
	
	/**
	 * the typical cql: CREATE TABLE sensor ( id text, time timestamp, datatype text, PRIMARY KEY(id, time));
	 * @param cql
	 */
	public void createTable(String cql) {
		if(! connected) {
			this.connect(this.hostIP, this.keyspace);
		}
		
		session.execute(cql);	
	}
	
	public void createKeyspace(String name) {
		session.execute("CREATE KEYSPACE " + name + " WITH replication = {'class':'SimpleStrategy', 'replication_factor':3};");
	}
	
	public void close() {
		cluster.close();
	}

	public String getHostIP() {
		return hostIP;
	}

	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}

	public String getKeyspace() {
		return keyspace;
	}

	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}
}