package de.brockhaus.m2m.startup.c8y;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.inventory.ManagedObject;

import de.brockhaus.m2m.config.Configuration;
import de.brockhaus.m2m.config.ConfigurationServiceLocal;
import de.brockhaus.m2m.integration.config.ConfigurationServiceFactory;
import de.brockhaus.m2m.integration.config.c8y.C8YSensorMapping;

/**
 * A utility which allows the registration of devices in cumulocity.
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author jperez, Feb 12, 2016
 *
 */
public class C8YSensorRegistrationUtil 
{
	private String url;
	private String user;
	private String pwd;
	private List<String> gids;
	private ArrayList<C8YSensorMapping> sensorMappings;
	private GId gid;
	private ManagedObject mo;
	private InventoryApi inventory;

	
	private static ConfigurationServiceLocal configService = ConfigurationServiceFactory.getConfigurationServiceLocal();

	public static void main(String[] args) {
		
		C8YSensorRegistrationUtil util = new C8YSensorRegistrationUtil();
		// initialize the sensors list
		util.init();
		util.doRegister();
		
		System.exit(0);
		
	}
	
	public void init() {
		
		//getting the data from config service: sensors
		Configuration config = configService.getConfig();
		HashMap<String, String> sensors = config.getConfigForElement("sensors");
		sensorMappings = new ArrayList<C8YSensorMapping>();
		Collection<String> sensorMappingData = sensors.values();
		
		for (String sensorMappingString : sensorMappingData) {
			// ArrayIndex, own GId, parent GId, sensor name
			//"0;10991;11654;Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor conveyer belt swap",
			String[] data = sensorMappingString.split(";");
			sensorMappings.add(new C8YSensorMapping(new Integer(data[0]), data[3], data[1], data[2]));
		}
		
		//getting the data from config service: credentials
		HashMap<String, String> credentials = config.getConfigForElement("credentials");
		this.user = credentials.get("user");
		this.pwd = credentials.get("pwd");
		this.url = credentials.get("url");
		
		//getting the data from config service: devices
		HashMap<String, String> devices = config.getConfigForElement("devices");
		gids = new ArrayList<String>(devices.values());
		
		// get the things from cumulocity
		// connect the agent to the platform
		Platform platform = new PlatformImpl(url, new CumulocityCredentials(user, pwd));
		
		// retrieve a handle to the cumulocity inventory
		inventory = platform.getInventoryApi();
		
		/* create a managed object which is associated with the main device */	
		mo = inventory.getManagedObjectApi(new GId(gids.get(0)));
	}
	
	private void doRegister() {

		for (C8YSensorMapping mapping : this.sensorMappings) {
			// create a managed object representation for the sensor
			ManagedObjectRepresentation morr = new ManagedObjectRepresentation();

			// assign the sensor managed object its name
			morr.setName(mapping.getSensorName());

			// create the sensor managed object in the inventory and assign it a
			// device ID
			morr = inventory.create(morr);

			// add the sensor as a children device to the main device
			mo.addChildDevice(morr.getId());
		}
	}
}