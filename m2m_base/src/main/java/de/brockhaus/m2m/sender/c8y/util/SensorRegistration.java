package de.brockhaus.m2m.sender.c8y.util;

import java.util.ArrayList;

import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.inventory.ManagedObject;

/**
 * 
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author jperez, Feb 12, 2016
 *
 */
public class SensorRegistration 
{
	private static String url ="https://brockhaus.cumulocity.com";
	private static String user="jperez@brockhaus-gruppe.de";
	private static String pwd = "brockhaus";
	private static GId gid = new GId("10979");
	private static ArrayList<String> sensors;
	
	//TODO Dependency Injection
	private static SensorIdHelper idHelper = new SensorIdHelper();
	
			
	public static void main(String[] args)
	{
		// connect the agent to the platform
		Platform platform = new PlatformImpl(url, new CumulocityCredentials(user, pwd));
		// retrieve a handle to the Cumulocity inventory
		
		InventoryApi inventory = platform.getInventoryApi();
		
		/* create a managed object which is associated with the main device whose device ID is 10979*/	
		ManagedObject mo = inventory.getManagedObjectApi(gid);
		
		// initialize the sensors list
		initSensors();
		
		// register the sensors to C8y
		for(int i = 0; i < sensors.size(); i++)
		{
			// create a managed object representation for the sensor
			ManagedObjectRepresentation morr = new ManagedObjectRepresentation();
			
			// assign the sensor managed object its name
			morr.setName(sensors.get(i));
			
			// create the sensor managed object in the inventory and assign it a device ID
			morr = inventory.create(morr);
			
			// add the sensor as a children device to the main device
			mo.addChildDevice(morr.getId());
		}
		
		System.exit(0);	
	}
	
	public static void initSensors()
	{
		sensors = new ArrayList<String>(idHelper.getSensorIds().values());
	}
}