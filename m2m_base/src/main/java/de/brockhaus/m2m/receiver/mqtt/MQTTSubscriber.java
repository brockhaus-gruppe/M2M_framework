package de.brockhaus.m2m.receiver.mqtt;



import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.util.MQTTUtil;
import de.brockhaus.m2m.util.MQTTUtil.ClientType;

/**
 * The 'autonomous' stand-alone receiver for testing purposes outside the infrastructure.
 * 
 * TODO: Spring DI
 * 
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jun 5, 2015
 *
 */
public class MQTTSubscriber extends AbstractM2MMessageHandler  {
	
	private final static Logger LOG = Logger.getLogger(MQTTSubscriber.class);

	private MqttClient mqttClient;
	
	// Subscribe to all subtopics of sensors
	private String topic = "sensors/#";
	
	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		// TODO Auto-generated method stub	
	}

	public void init() {
		try {
			mqttClient = new MQTTUtil().getClient(ClientType.TYPE_SUBSCRIBER);
			mqttClient.setCallback(new MQTTSubscriptionCallBack());
			mqttClient.connect();

			mqttClient.subscribe(topic);

			LOG.info("Subscriber is now listening to " + topic);

		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// inner class
	class MQTTSubscriptionCallBack implements MqttCallback {

		public void connectionLost(Throwable cause) {
		}

		public void messageArrived(MqttTopic topic, MqttMessage message) {
			LOG.debug("Message arrived. Topic: " + topic.getName()
					+ " Message: " + message.toString());

			if ("home/LWT".equals(topic.getName())) {
				LOG.error("Sensor gone!");
			}
		}


		@Override
		public void deliveryComplete(IMqttDeliveryToken token) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void messageArrived(String topic, MqttMessage message) throws Exception {
			LOG.debug("Message arrived. Topic: " + topic
					+ " Message: " + message.toString());
			
		}
	}
}
