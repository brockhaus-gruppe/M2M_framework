package de.brockhaus.m2m.receiver.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.util.JSONBuilderParserUtil;
import de.brockhaus.m2m.util.MQTTUtil;
import de.brockhaus.m2m.util.MQTTUtil.ClientType;

/**
 * Test-driver to fill data into the system
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jun 4, 2015
 *
 */
public class MQTTPublisher {
	
	private MqttClient client;
	
	// you can hierarchically structure further on, i.e. for every sensor
	private final String SENSOR_TOPIC = "sensors/ABC-123";
	
	private MqttTopic topic;
	
	public static void main(String[] args) {
		MQTTPublisher client = new MQTTPublisher();
		client.init();
		client.publish();
		
		System.exit(0);
	}
	
	
	private void publish() {
		MqttMessage msg = new MqttMessage();
		M2MSensorMessage sMesg = new M2MSensorMessage();
		sMesg.setDatatype(M2MDataType.FLOAT);
		sMesg.setSensorId("ABC-123");
		
		// converting the message to JSON
		String json = JSONBuilderParserUtil.getInstance().toJSON(sMesg);
		
		msg.setPayload(json.getBytes());
		
		try {
			topic.publish(msg);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	private void init() {
		try {
			client = new MQTTUtil().getClient(ClientType.TYPE_PUBLISHER);
			client.connect();
			topic = client.getTopic(SENSOR_TOPIC);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

}
