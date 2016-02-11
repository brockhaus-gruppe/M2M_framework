package de.brockhaus.m2m.sender.c8y;

import java.util.HashMap;

import com.cumulocity.model.idtype.GId;

import de.brockhaus.m2m.message.M2MCommunicationException;

public class SensorIdHelper {
	
	private HashMap<String, String> sensorIds = new HashMap<String, String>();
	
	public SensorIdHelper() {
		this.init();
	}
	
	//TODO change to DI
	private void init() {
		sensorIds.put("Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor conveyer belt swap", "10991");
		sensorIds.put("Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor drilling machine", "10992");
		sensorIds.put("Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor loading station", "10993");
		sensorIds.put("Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor milling machine", "10994");
		sensorIds.put("Siemens PLC S7-1200.s7-1200.Inputs.Phototransistor slider 1", "10995");
		sensorIds.put("Siemens PLC S7-1200.s7-1200.Inputs.Push-button slider 1 front", "10996");
		sensorIds.put("Siemens PLC S7-1200.s7-1200.Inputs.Push-button slider 1 rear", "10997");
		sensorIds.put("Siemens PLC S7-1200.s7-1200.Inputs.Push-button slider 2 front", "10998");
		sensorIds.put("Siemens PLC S7-1200.s7-1200.Inputs.Push-button slider 2 rear", "10999");
	}
	
	
	public GId getGIdBySensorName(String sensorName) throws M2MCommunicationException {
		String gid = (sensorIds.get(sensorName));
		if(null != gid) {
			return new GId(gid);	
		} else {
			throw new M2MCommunicationException("Sensor is not included in the sensor list");
		}
		
	}
	
}
