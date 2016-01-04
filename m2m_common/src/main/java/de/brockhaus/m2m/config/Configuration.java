package de.brockhaus.m2m.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * A generic thing to define, store and retrieve values configuring the framework.
 * If using JSON, a configuration might look like this:
 {
  "configData" : {
    "sensors" : {
      "PT_DS1_316233.ED01_FA011.AA.R244" : "FLOAT",
      "PT_DS1_316233.ED01_AB219_M04.AS.V2251" : "FLOAT",
      "PT_DS1_316233.ED01_AB219_M04.AS.V2254" : "FLOAT",
      "PT_DS1_316233.ED01_AB219_M04.AS.V2253" : "FLOAT"
    },
    "sensor_ttl" : {
      "PT_DS1_316233.ED01_FA011.AA.R244" : "604800000",
      "PT_DS1_316233.ED01_AB219_M04.AS.V2251" : "604800000",
      "PT_DS1_316233.ED01_AB219_M04.AS.V2254" : "604800000",
      "PT_DS1_316233.ED01_AB219_M04.AS.V2253" : "604800000"
    }
  }
}
 * Project: integration_config
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 5, 2015
 *
 */
// @Singleton // will not work with Jersey unfortunately
@XmlRootElement
public class Configuration implements Serializable {
	
	/** 
	 * collection holding the config values, e.g.
	 * 	CircularBufferDataContainer, capacity, 3
	 * 		
	 */
	public HashMap<String, HashMap<String,String>> configData = new HashMap<String, HashMap<String, String>>();
	
	
	public Configuration() {

	}
	
	@JsonIgnore
	public HashMap<String, String> getConfigForElement(String key) {
		HashMap<String, String> ret = new HashMap<String, String>();
		if(null != this.configData.get(key)) {
			ret = this.configData.get(key);
		}
		return ret;
	}
	
	@JsonIgnore
	public Set<String> getAllElements() {
		Set<String> ret = new HashSet<String>();
		if(null != configData.keySet()) {
			ret = configData.keySet();
		}
		return ret;
	}
	
	@JsonIgnore
	public HashMap<String, String> getAllEntriesForElement(String element) {
		HashMap<String, String> ret = new HashMap<String, String>();
		if(null != this.configData.get(element)) {
			ret = this.configData.get(element);
		}
		return ret;
	}
	
	@JsonIgnore
	public void setConfigForElement(String key, HashMap<String, String> values) {
		this.configData.put(key, values);
	}
}
