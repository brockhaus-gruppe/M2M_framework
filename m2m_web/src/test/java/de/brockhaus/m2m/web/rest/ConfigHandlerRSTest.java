package de.brockhaus.m2m.web.rest;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.brockhaus.m2m.config.Configuration;
import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.util.JSONBuilderParserUtil;

/**
 * 
 * Project: integration_web
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author mbohnen, May 20, 2015
 *
 */
public class ConfigHandlerRSTest {

	private static Logger LOG = Logger.getLogger(ConfigHandlerRSTest.class);

	public static void main(String[] args) {
		ConfigHandlerRSTest test = new ConfigHandlerRSTest();

		test.testSetConfig4Element();
//		test.testPing();
	}

	// setting a configuration
	private void testSetConfig4Element() {

		String url = "http://localhost:8080/sensorData/rest/config/setConfig4Element";
		Client client = Client.create();	 
		WebResource webResource = client.resource(url);
 
		Configuration config = new Configuration();
		
		HashMap<String,String> values = new HashMap<String, String>();
		values.put("PT_DS1_316233.ED01_AB219_M04.AS.V2251", M2MDataType.FLOAT.toString());
		values.put("PT_DS1_316233.ED01_AB219_M04.AS.V2253", M2MDataType.FLOAT.toString());
		values.put("PT_DS1_316233.ED01_AB219_M04.AS.V2254", M2MDataType.FLOAT.toString());
		values.put("PT_DS1_316233.ED01_FA011.AA.R244", M2MDataType.FLOAT.toString());
		values.put("PT_DS1_316233.ED01_FA011.AA.R244", M2MDataType.FLOAT.toString());
		values.put("ABC123", M2MDataType.FLOAT.toString());
	
		// if commented all values will be gone
		config.setConfigForElement("sensors", values);

		String input = JSONBuilderParserUtil.getInstance().toJSON(config);
 
		// off we go
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
 
	}

	private void testPing() {
		
		String url = "http://localhost:8080/sensorData/rest/config/ping";
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type("aplication/json").get(ClientResponse.class);
		
		LOG.debug(response.getEntity(String.class));
	}
}
