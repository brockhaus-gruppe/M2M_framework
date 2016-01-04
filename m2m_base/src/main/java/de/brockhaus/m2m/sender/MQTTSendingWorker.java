package de.brockhaus.m2m.sender;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.util.JSONBuilderParserUtil;
import de.brockhaus.m2m.util.MQTTUtil;
import de.brockhaus.m2m.util.MQTTUtil.ClientType;

/**
 * Sending to a MQTT broker using eclipse paho ...
 * 
 * TODO: Spring DI
 * 
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jun 12, 2015
 *
 */
public class MQTTSendingWorker extends AbstractM2MMessageHandler implements M2MSendingWorker {

	private MqttClient client;
	
	// you can hierarchically structure further on, i.e. for every sensor
	private String mqttTopicName = "sensors/ABC-123";
	
	private MqttTopic topic;


	public MQTTSendingWorker(M2MMessageHandler next, String inTypeClassName,
			String outTypeClassName) {
		super(next, inTypeClassName, outTypeClassName);
	}

	@Override
	public void doSend(M2MMessage message) {
		MqttMessage msg = new MqttMessage();
		String json = JSONBuilderParserUtil.getInstance().toJSON(message);
		
		msg.setPayload(json.getBytes());
		
		try {
			topic.publish(msg);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		try {
			//TODO Spring DI
			client = new MQTTUtil().getClient(ClientType.TYPE_PUBLISHER);
			client.connect();
			topic = client.getTopic(mqttTopicName);
		} catch (MqttException e) {
			e.printStackTrace();
		}	
	}
	
	public void cleanup() {
		// lazy
	}

	@Override
	protected <T extends M2MMessage> void handleMessage(T message) {
		this.doSend(message);
	}

	public String getMqttTopicName() {
		return mqttTopicName;
	}

	public void setMqttTopicName(String mqttTopicName) {
		this.mqttTopicName = mqttTopicName;
	}
}
