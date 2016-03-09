package de.brockhaus.m2m.receiver.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageReceiverLifecycle;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;

/**
 * The RMI receiving part ...
 * 
 * Example config:
 * 
 	<!-- RMI based receiving -->
	<bean name="rmi_adapter"
		class="de.brockhaus.m2m.receiver.rmi.M2MMessageRMIReceiverAdapter" 
		scope="singleton" 
		init-method="init" >
		
		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.message.M2MSensorMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.message.M2MMultiMessage</value>
    	</constructor-arg>
    	
    	<!-- which machine we're running on -->
		<property name="host">
			<value>localhost</value>
		</property>
    	<!-- which port we're using -->
		<property name="port">
			<value>1099</value>
		</property>
		<!-- under what name we're binding the stub -->
		<property name="bindingName">
			<value>m2m_rmi_receiver</value>
		</property>
	</bean>
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Jan 23, 2016
 *
 */
public class M2MMessageRMIReceiverAdapter extends AbstractM2MMessageHandler implements M2MMessageReceiverLifecycle {
	
	private static final Logger LOG = Logger.getLogger(M2MMessageRMIReceiverAdapter.class);
	
	// the port we're listening to
	private int port;
	// the host
	private String host;
	// stub name
	private String bindingName;
	
	
	public M2MMessageRMIReceiverAdapter() {
		super();
	}

	public M2MMessageRMIReceiverAdapter(String inTypeClassName, String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message");
		
		M2MMultiMessage msg = new M2MMultiMessage();
		msg.getSensorDataMessageList().add((M2MSensorMessage) message);
		super.setMessage(msg);
	}
	
	public void start() { 
		try {
			LocateRegistry.createRegistry(port);
			Naming.bind("rmi://" + host + ":" + port +"/" + bindingName, new M2MMessageRMIReceiverImpl(this));
			
			LOG.info("Server up'n'running, access through: rmi://" + host + ":" + port + "/" + bindingName);
			
		} catch (RemoteException | MalformedURLException | AlreadyBoundException e) {
			e.printStackTrace();
		}
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getBindingName() {
		return bindingName;
	}

	public void setBindingName(String bindingName) {
		this.bindingName = bindingName;
	}

	@Override
	public void stop() {
		//lazy
	}
}
