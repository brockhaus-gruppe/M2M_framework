package de.brockhaus.m2m.integration.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.brockhaus.m2m.config.ConfigSerializer;
import de.brockhaus.m2m.config.Configuration;
import de.brockhaus.m2m.integration.annotation.JSONSerializer;

/**
 * A serializer which writes the configuration to a JSON file. The file this serializer writes to
 * can be configured within configService.properties.
 * 
 * Project: integration_config
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author mbohnen, May 16, 2015
 *
 */
@JSONSerializer
public class Config2JSONSerializer implements ConfigSerializer {

	File file = null;
	
	public Config2JSONSerializer() {
		this.init();
	}

	private void init() {
		Properties props = new Properties();
		try {
			// load a properties file
			props.load(getClass().getClassLoader().getResourceAsStream("configService.properties"));
			this.file = new File(props.getProperty("jsonconfigfile"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// TODO throw Exception
	public void saveConfiguration(Configuration config) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.writeValue(this.file, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// TODO throw Exception
	public Configuration readConfiguration() {
		Configuration ret = null;
		ObjectMapper mapper = new ObjectMapper();

		if (this.file.exists()) {
			try {
				ret = mapper.readValue(this.file, Configuration.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			ret = new Configuration();
		}

		return ret;
	}
}
