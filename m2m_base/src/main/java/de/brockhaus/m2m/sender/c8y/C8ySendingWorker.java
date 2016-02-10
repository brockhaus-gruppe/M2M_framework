package de.brockhaus.m2m.sender.c8y;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.measurement.MeasurementApi;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.sender.M2MSendingWorker;

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

	private static final Logger LOG = Logger.getLogger(C8ySendingWorker.class);

	private Platform platform;
	private MeasurementApi measurement;
	private InventoryApi inventory;

	private String url = "https://brockhaus.cumulocity.com";
	private String user = "jperez@brockhaus-gruppe.de";
	private String pwd = "brockhaus";
	private String gid = "10979";
	
	//TODO change to DI
	private SensorIdHelper idHelper = new SensorIdHelper();
	
	public C8ySendingWorker() {
		//lazy
	}

	public C8ySendingWorker(String inTypeClassName, String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);
	}

	public void init() {
		// Create the platform instance
		platform = new PlatformImpl(url, new CumulocityCredentials(user, pwd));
		inventory = platform.getInventoryApi();
		LOG.debug("Device name: " + (inventory.get(new GId(gid)).getName()));
		// Access individual API resource object
		measurement = platform.getMeasurementApi();
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

		SensorMeasurement fragment = new SensorMeasurement();
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
		measurement.create(representation);
	}

	private int convert2Numerical(String value) {

		// being pretty optimistic ... nothing else can happen, no FooBazz e.g.
		// ;-)
		if (value.toUpperCase().equals("TRUE")) {
			return 1;
		} else {
			return 0;
		}
	}
	
	private ManagedObjectRepresentation getSource(String SensorId) throws M2MCommunicationException {
		GId gid = idHelper.getGIdBySensorName(SensorId);
		return inventory.get(gid);
	}
}