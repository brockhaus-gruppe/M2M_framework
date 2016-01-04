package de.brockhaus.m2m.handler.database.cassandra;

/**
 * 
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 1, 2015
 *
 */
public class CassandraColumn {

	private String name;
	private CassandraDataType type;
	private boolean isKey;
	

	public CassandraColumn(String name, CassandraDataType type, boolean isKey) {
		super();
		this.name = name;
		this.type = type;
		this.isKey = isKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CassandraDataType getType() {
		return type;
	}

	public void setType(CassandraDataType type) {
		this.type = type;
	}

	public boolean isKey() {
		return isKey;
	}

	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}

}
