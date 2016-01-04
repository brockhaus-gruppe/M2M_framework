package de.brockhaus.m2m.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * 
 	<!-- the datasource -->
	<bean name="datasource"
		class="de.brockhaus.m2m.receiver.rdbms.M2MJdbcDataSource"
		scope="singleton" >
		
		<!-- the jdbc driver -->
		<property name = "driver">
			<value>ord.acme.jdbc</value>
		</property>
		
		<!-- where to find the database -->
    	<property name = "url">
			<value>5000</value>
		</property>
		
		<!-- user -->
		<property name="username">
			<value>johndoe</value>
		</property>
		
		<!-- password -->
		<property name="password">
			<value>topsecret</value>
		</property>
	</bean>	
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 11, 2015
 *
 */
public class M2MJdbcDataSource {

	private String driver;
	private String url;
	private String username;
	private String password;
	

	public Connection getConnection() throws SQLException {
		Connection con = null;
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(this.url, this.username, this.password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}

	public Connection getConnection(String username, String password) throws SQLException {
		Connection con = null;
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(this.url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	// getters and setters

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
