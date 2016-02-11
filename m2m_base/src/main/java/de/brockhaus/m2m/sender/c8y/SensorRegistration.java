package de.brockhaus.m2m.sender.c8y;

import java.util.ArrayList;

import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.inventory.ManagedObject;

public class SensorRegistration 
{
	private static String url ="https://brockhaus.cumulocity.com";
	private static String user="jperez@brockhaus-gruppe.de";
	private static String pwd = "brockhaus";
	private static GId gid = new GId("10979");
	private static ArrayList<String> sensors = new ArrayList<String>();
			
	public static void main(String[] args)
	{
		// connect the agent to the platform
		Platform platform = new PlatformImpl(url, new CumulocityCredentials(user, pwd));
		// retrieve a handle to the Cumulocity inventory
		InventoryApi inventory = platform.getInventoryApi();
		/* create a managed object which is associated with the main device whose device
		 * ID is 10979
		 */	
		ManagedObject mo = inventory.getManagedObjectApi(gid);
		// initialize the sensors list
		initSensors();
		// register the sensors in C8y
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
		sensors.add("Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor conveyer belt swap");
		sensors.add("Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor drilling machine");
		sensors.add("Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor loading station");
		sensors.add("Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor milling machine");
		sensors.add("Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor slider 1");
		sensors.add("Siemens PLC S7-1200.s7-1200.Inputs.Push-button slider 1 front");
		sensors.add("Siemens PLC S7-1200.s7-1200.Inputs.Push-button slider 1 rear");
		sensors.add("Siemens PLC S7-1200.s7-1200.Inputs.Push-button slider 2 front");
		sensors.add("Siemens PLC S7-1200.s7-1200.Inputs.Push-button slider 2 rear");	
	}
}