package de.brockhaus.m2m.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Later on everything will be configured by Spring DI, then it will be a Singleton automatically
 * 
 * TODO Spring DI
 * 
 * Project: integration_common
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jun 4, 2015
 *
 */
public class MQTTUtil {

	// a public broker: "tcp://broker.mqttdashboard.com:1883";
	// the local broker: "tcp://localhost:1883"
	public String brokerURL = "tcp://localhost:1883";

	public enum ClientType {
		TYPE_PUBLISHER, TYPE_SUBSCRIBER
	};

	private MqttClient client;

	
	public  MQTTUtil() {
		// lazy
	}


	public MqttClient getClient(ClientType type) throws MqttException {
		String clientId = "";

		switch (type) {
		case TYPE_PUBLISHER:
			clientId = this.getMacAddress() + "-pub";
			break;
		case TYPE_SUBSCRIBER:
			clientId = this.getMacAddress() + "-sub";
			break;
		}

		client = new MqttClient(brokerURL, clientId);
		
		return client;
	}

	// getting the unique MAC address
	private String getMacAddress() {

		StringBuilder sb = new StringBuilder();
		 String result = "";

	        try {
	            for (NetworkInterface ni : Collections.list(
	                    NetworkInterface.getNetworkInterfaces())) {
	                byte[] hardwareAddress = ni.getHardwareAddress();

	                if (hardwareAddress != null) {
	                    for (int i = 0; i < hardwareAddress.length; i++)
	                        result += String.format((i == 0 ? "" : "-") + "%02X", hardwareAddress[i]);

	                    return result;
	                }
	            }

	        } catch (SocketException e) {
	            System.out.println("Could not find out MAC Adress. Exiting Application ");
	            System.exit(1);
	        }
	        return result;
	}

	public String getBrokerURL() {
		return brokerURL;
	}

	public void setBrokerURL(String brokerURL) {
		this.brokerURL = brokerURL;
	}
}
