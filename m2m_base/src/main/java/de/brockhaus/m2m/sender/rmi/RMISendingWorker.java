package de.brockhaus.m2m.sender.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.receiver.rmi.M2MMessageRMIReceiver;
import de.brockhaus.m2m.sender.M2MSendingWorker;

/**
 * Sending through RMI ... check for the configuration of the receiving part. 
 *
 * Config example

	<!-- RMI based sending -->
	<bean name="rmi_sender"
		class="de.brockhaus.m2m.sender.rmi.RMISendingWorker" scope="singleton" init-method="init" destroy-method="cleanUp">
		

		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.handler.M2MPlainTextMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.handler.M2MSensorMessage</value>
    	</constructor-arg>
    	
    	<!-- IP or DNS name -->
		<property name="host">
			<value>localhost</value>
		</property>
		
		<!-- the port used -->
		<property name="port">
			<value>1099</value>
		</property>
    	
    	<!-- under which name bound to Naming -->
    	<property name="bindingName">
			<value>m2m_rmi_receiver</value>
		</property>
	
	</bean>

 * 
 * Project: communication.sender
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 27, 2015
 *
 */
public class RMISendingWorker extends AbstractM2MMessageHandler implements M2MSendingWorker {
	
	private static final Logger LOG = Logger.getLogger(RMISendingWorker.class);
	
	private String host;
	private String port;
	private String bindingName;
	private M2MMessageRMIReceiver service;

	
	public RMISendingWorker() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RMISendingWorker(String inTypeClassName,	String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);
	}

	@Override
	public void doSend(M2MMessage message) {
		try {
			service.dealWithMessage(message);
		} catch (RemoteException | M2MCommunicationException e) {
			e.printStackTrace();
		}
		
	}
	
	public void init() {
		try {
			service = (M2MMessageRMIReceiver) Naming.lookup("rmi://" + this.host + ":" + this.port + "/" + this.bindingName);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message");
		this.doSend(message);		
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getBindingName() {
		return bindingName;
	}

	public void setBindingName(String bindingName) {
		this.bindingName = bindingName;
	}
}
