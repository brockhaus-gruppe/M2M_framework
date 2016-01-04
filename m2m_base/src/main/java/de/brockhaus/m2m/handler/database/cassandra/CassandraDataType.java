package de.brockhaus.m2m.handler.database.cassandra;

/**
 * https://www.datastax.com/documentation/cql/3.0/cql/cql_reference/cql_data_types_c.html
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Dec 1, 2015
 *
 */
public enum CassandraDataType {
	
	TEXT(0), TIMESTAMP(1);
	
	private final int value;

    private CassandraDataType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
