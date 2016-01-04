package de.brockhaus.m2m.receiver.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
/**
 * 
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jun 5, 2015
 *
 */
public class MQTTSubscriptionCallBack implements MqttCallback {

	public void connectionLost(Throwable cause) {
	}

	public void messageArrived(MqttTopic topic, MqttMessage message) {
		System.out.println("Message arrived. Topic: " + topic.getName()
				+ " Message: " + message.toString());

		if ("home/LWT".equals(topic.getName())) {
			System.err.println("Sensor gone!");
		}
	}


	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("message arrived");
		System.out.println("Message arrived. Topic: " + topic
				+ " Message: " + message.toString());
		
	}
}
