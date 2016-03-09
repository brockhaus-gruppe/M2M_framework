package de.brockhaus.m2m.receiver.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import de.brockhaus.m2m.util.MQTTUtil;
import de.brockhaus.m2m.util.MQTTUtil.ClientType;

/**
 * The 'autonomous' standalone receiver for testing purposes outside the infrastructure.
 * 
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jun 5, 2015
 *
 */
public class MQTTTestSubscriber {

	private MqttClient mqttClient;

	public static void main(String... args) {
		final MQTTTestSubscriber subscriber = new MQTTTestSubscriber();
		subscriber.start();
	}

	public MQTTTestSubscriber() {

		try {
			mqttClient = new MQTTUtil().getClient(
					ClientType.TYPE_SUBSCRIBER);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
		try {

			mqttClient.setCallback(new MQTTSubscriptionCallBack());
			mqttClient.connect();

			// Subscribe to all subtopics of sensors
			String topic = "sensors/#";
			mqttClient.subscribe(topic);

			System.out.println("Subscriber is now listening to " + topic);

		} catch (MqttException e) {
			e.printStackTrace();

		}
	}
}
