package de.brockhaus.m2m.sender.c8y;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.measurement.MeasurementApi;

import de.brockhaus.m2m.config.ConfigurationServiceLocal;
import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.integration.config.ConfigurationServiceFactory;
import de.brockhaus.m2m.integration.config.c8y.C8YDeviceMapping;
import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.sender.M2MSendingWorker;
import de.brockhaus.m2m.sender.c8y.util.DeviceMeasurement;

/**
 * Ensure the cumulocity agent (c8y-agent) is up'n'running
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author mbohnen, Jan 12, 2016
 *
 */
public class C8ySendingWorker extends AbstractM2MMessageHandler implements M2MSendingWorker {

	// just a logger
	private static final Logger LOG = Logger.getLogger(C8ySendingWorker.class);

	// cumulocity URL
	private String url;

	//cumulocity user
	private String user;
	
	// cumulocity pwd
	private String pwd;
	
	// the device we're sending to
	private String gid;
	
	// cumulocity platform
	private Platform platform;
	
	// cumulocity measurementAPI
	private MeasurementApi measurementAPI;
	
	// cumulocity inventoryAPI
	private InventoryApi inventoryAPI;
	
	// device mappings for the set of devices used
	private ArrayList<C8YDeviceMapping> deviceMappings;
	
	private ConfigurationServiceFactory configServiceFactory;
	
	public C8ySendingWorker() {
		//lazy
	}

	public C8ySendingWorker(String inTypeClassName, String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);
	}

	public void init() {
		ConfigurationServiceLocal configService = configServiceFactory.getConfigurationServiceLocal();		
		
		HashMap<String, String> devices = configService.getConfig().getConfigForElement("inputs devices");
		deviceMappings = new ArrayList<C8YDeviceMapping>();
		Collection<String> deviceMappingData = devices.values();
		
		for (String deviceMappingString : deviceMappingData) {
			// ArrayIndex, own GId, parent GId, device name
			//"0;20468;10979;Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor conveyer belt swap",
			String[] data = deviceMappingString.split(";");
			deviceMappings.add(new C8YDeviceMapping(new Integer(data[0]), data[3], data[1], data[2]));
		}
		// getting the data from config service: credentials
		HashMap<String, String> credentials = configService.getConfig().getConfigForElement("credentials");
		this.user = credentials.get("user");
		this.pwd = credentials.get("pwd");
		this.url = credentials.get("url");

		// getting the data from config service: main device
		HashMap<String, String> main_device = configService.getConfig().getConfigForElement("main device");
		ArrayList<String> gids = new ArrayList<String>(devices.values());
		this.gid = gids.get(0);

		// Create the platform instance
		platform = new PlatformImpl(url, new CumulocityCredentials(user, pwd));
		inventoryAPI = platform.getInventoryApi();
		LOG.debug("Device name: " + (inventoryAPI.get(new GId(gid)).getName()));
		// Access individual API resource object
		measurementAPI = platform.getMeasurementApi();
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		M2MMultiMessage msg = (M2MMultiMessage) message;
		for (M2MSensorMessage sensorMessage : msg.getSensorDataMessageList()) {
			try {
				this.doSend(sensorMessage);
			} catch (M2MCommunicationException e) {
				LOG.error(e);
			}
		}
	}

	@Override
	public void doSend(M2MMessage message) throws M2MCommunicationException {
		M2MSensorMessage msg = (M2MSensorMessage) message;

		// define measurement
		MeasurementRepresentation representation = new MeasurementRepresentation();
		representation.setType("");
		representation.setSource(getSource(msg.getSensorId()));
		representation.setTime(msg.getTime());

		DeviceMeasurement fragment = new DeviceMeasurement();
		representation.set(fragment);

		M2MDataType dataType = msg.getDatatype();
		switch (dataType) {
		case FLOAT:
			fragment.setValue(new BigDecimal(msg.getValue()));
			break;
		case BOOLEAN:
			fragment.setValue(new BigDecimal(this.convert2Numerical(msg.getValue())));
			break;
		default:
			throw new M2MCommunicationException("Datatype can't be handled by sender");
		}
		measurementAPI.create(representation);
	}

	private int convert2Numerical(String value) {

		// being pretty optimistic ... presuming nothing else can happen, no FooBazz or stuff like this ;-)
		if (value.toUpperCase().equals("TRUE")) {
			return 1;
		} else {
			return 0;
		}
	}
	
	private ManagedObjectRepresentation getSource(String sensorId) throws M2MCommunicationException {
		
		ManagedObjectRepresentation representation = null;
		
		for (C8YDeviceMapping c8yDeviceMapping : deviceMappings) {
			if(c8yDeviceMapping.getDeviceName().equals(sensorId)) {
				GId gid = new GId(c8yDeviceMapping.getOwnGId());
				representation = inventoryAPI.get(gid);
			}
		}
		
		return representation;
	}

	public ConfigurationServiceFactory getConfigServiceFactory() {
		return configServiceFactory;
	}

	public void setConfigServiceFactory(ConfigurationServiceFactory configServiceFactory) {
		this.configServiceFactory = configServiceFactory;
	}
}