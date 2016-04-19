package de.brockhaus.m2m.startup.c8y;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

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
import de.brockhaus.m2m.integration.config.c8y.C8YDeviceMapping;

/**
 * A utility which allows the registration of devices in cumulocity. 
 * Only run once, delete entities in cumulocity beforehand !
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author jperez, Feb 12, 2016
 *
 */
public class C8YDevicesRegistrationUtil 
{
	private static final Logger LOG = Logger.getLogger(C8YDevicesRegistrationUtil.class);
	
	private String url;
	private String user;
	private String pwd;
	private List<String> gids;
	private String[] deviceType = {"inputs devices", "outputs devices"};
	/*
	 *  deviceMappings array has two elements: [0] for the inputs devices 
	 *  and [1] for the outputs devices
	 */
	private List<C8YDeviceMapping>[] deviceMappings;
	/*
	 *  updatedDeviceMappings array has two elements: [0] for the inputs devices 
	 *  and [1] for the outputs devices
	 */
	private List<C8YDeviceMapping>[] updatedDeviceMappings;
	private GId gid;
	private ManagedObject mo;
	private InventoryApi inventory;

	private static ConfigurationServiceLocal configService = ConfigurationServiceFactory.getConfigurationServiceLocal();

	public static void main(String[] args) {
		
		C8YDevicesRegistrationUtil util = new C8YDevicesRegistrationUtil();
		// initialize the devices list
		util.init();
		util.doRegister();
		util.writeUpdatedConfig();
		
//		System.exit(0);
		
	}
	
	public void init() {
		//getting the data from config service: inputs_devices, outputs_devices
		Configuration config = configService.getConfig();
		for(int i = 0; i <= 1; i++) {
			HashMap<String, String> devices = config.getConfigForElement(deviceType[i]);
			deviceMappings[i] = new ArrayList<C8YDeviceMapping>();
				Collection<String> deviceMappingData = devices.values();
		
			for (String deviceMappingString : deviceMappingData) {
				// ArrayIndex, own GId, parent GId, device name
				//"0;20468;10979;Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor conveyer belt swap",
				String[] data = deviceMappingString.split(";");
				deviceMappings[i].add(new C8YDeviceMapping(new Integer(data[0]), data[3], data[1], data[2]));
			}
		}
		
		//getting the data from config service: credentials
		HashMap<String, String> credentials = config.getConfigForElement("credentials");
		this.user = credentials.get("user");
		this.pwd = credentials.get("pwd");
		this.url = credentials.get("url");
		
		//getting the data from config service: main device
		HashMap<String, String> devices = config.getConfigForElement("main device");
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
		for(int i = 0; i <= 1; i++) {

			for (C8YDeviceMapping mapping : this.deviceMappings[i]) {
				// create a managed object representation for the device
				ManagedObjectRepresentation morr = new ManagedObjectRepresentation();

				// assign the device managed object its name
				morr.setName(mapping.getDeviceName());

				// create the device managed object in the inventory and assign it a
				// device ID
				morr = inventory.create(morr);
			
				GId childGId = morr.getId();
				LOG.debug("creating child device: " + mapping.getDeviceName() + " and GId: " + childGId);

				// add the device as a children device to the main device
				mo.addChildDevice(childGId);
			
				mapping.setOwnGId(childGId.getValue());
				this.updatedDeviceMappings[i].add(mapping);
			}
		}
	}
	
	private void writeUpdatedConfig() {
		for(int i = 0; i <= 1; i++) {
		
			HashMap<String, String> updated = new HashMap<String, String>();
			for (C8YDeviceMapping mapping : updatedDeviceMappings[i]) {
				updated.put(new Integer(mapping.getArrayIndex()).toString(), mapping.toString());
			}
		
			Configuration config = this.configService.getConfig();
			config.setConfigForElement(deviceType[i], updated);
		
			this.configService.updateConfig(config);
		
		}
	}
}