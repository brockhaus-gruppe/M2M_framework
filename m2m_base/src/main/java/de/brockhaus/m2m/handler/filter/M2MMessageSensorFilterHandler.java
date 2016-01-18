package de.brockhaus.m2m.handler.filter;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.config.ConfigurationChangeListener;
import de.brockhaus.m2m.config.ConfigurationChangeRemoteListenerStub;
import de.brockhaus.m2m.config.ConfigurationService;
import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.util.ConfigServerRMIConnector;

/**
 * A simple Filter which blocks further handling of specified sensor ids. 
 * 
 * It needs to be considered, that the list of sensors we want to filter can be configured within
 * the respective xml configuration or within the configuration service. If done within the configuration service,
 * we need to be informed about changes, so the respective interface has to be implemented.
 * 
 * Example config:
 * 
	<!-- filtering the sensor ids -->
	<bean name="sensorfilter_handler"
		class="de.brockhaus.m2m.handler.filter.M2MMessageSensorFilterHandler"
		init-method="init" 
		destroy-method="cleanUp" 
		scope="singleton" >

		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.message.M2MSensorMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.message.M2MSensorMessage</value>
    	</constructor-arg>
    	
    	<!-- who might pass (if not configured by config service) -->
		<property name="sensorIds">
			<list>
            	<value>PT_DS1_316233.ED01_FA011.AA.R244</value>
        	</list>
		</property>
		
		<property name = "useConfigService4Filtering">
			<value>true</value>
		</property>
		
		<property name = "listenerStub" ref = "remote_config_listener"/>
		
		<!-- notification of config changes -->
    	<property name = "notificationByConfigService">
			<value>true</value>
		</property>
		
		<!-- if granted -->
		<property name="nextOnPass" ref = "json_converter" />
		
		<!-- if filtered -->
		<property name="nextOnFilter">
			<null/>
		</property>	

	</bean>	
 * 
 * Project: communication.sender
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 4, 2015
 *
 */
public class M2MMessageSensorFilterHandler extends AbstractM2MMessageHandler implements ConfigurationChangeListener {
	
	// just a logger
	private static final Logger LOG = Logger.getLogger(M2MMessageSensorFilterHandler.class);
	
	// use the external config service or spring configuration (spring-beans.xml)
	private boolean useConfigService4Filtering;
	
	// the remote configuration service
	private ConfigurationService configService;
	
	// being notified in case of changes of configuration
	private boolean notificationByConfigService;
	
	// the stub we are using for RMI based callbacks from configuration service
	private ConfigurationChangeRemoteListenerStub listenerStub;

	// the sensors ids which will be pushed through
	private List<String> sensorIds;
	
	// which one is next if not filtered
	private AbstractM2MMessageHandler nextOnPass;
	
	// which one is next if filtered
	private AbstractM2MMessageHandler nextOnFilter;
	
	
	public M2MMessageSensorFilterHandler() {
		super();
	}

	public M2MMessageSensorFilterHandler(String inTypeClassName, String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message");
		String id = ((M2MSensorMessage) message).getSensorId();
		if(sensorIds.contains(id)) {
			LOG.debug("passing: " + id);
			setNext(nextOnPass);
		} else {
			LOG.debug("filtered: " + id);
			setNext(nextOnFilter);
		}
	}
	
	@Override
	public void onConfigurationChange() throws RemoteException {
		LOG.debug("been notified about configuration has changed");
		this.init();
	}
	
	public void init() {
		
		// populating the filter list from config service / file
		if(useConfigService4Filtering) {
			try {
				this.configService = ConfigServerRMIConnector.doConnect();
				this.sensorIds.addAll(configService.getConfig().getConfigForElement("sensors").keySet());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		// registering for callbacks
		if(notificationByConfigService) {
			try {
				this.listenerStub.setHandler(this);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void cleanUp() {
		
	}

	public List<String> getSensorIds() {
		return sensorIds;
	}

	public void setSensorIds(List<String> sensorIds) {
		this.sensorIds = sensorIds;
	}

	public AbstractM2MMessageHandler getNextOnPass() {
		return nextOnPass;
	}

	public void setNextOnPass(AbstractM2MMessageHandler nextOnPass) {
		this.nextOnPass = nextOnPass;
	}

	public AbstractM2MMessageHandler getNextOnFilter() {
		return nextOnFilter;
	}

	public void setNextOnFilter(AbstractM2MMessageHandler nextOnFilter) {
		this.nextOnFilter = nextOnFilter;
	}

	public boolean isUseConfigService4Filtering() {
		return useConfigService4Filtering;
	}

	public void setUseConfigService4Filtering(boolean useConfigService4Filtering) {
		this.useConfigService4Filtering = useConfigService4Filtering;
	}

	public ConfigurationService getConfigService() {
		return configService;
	}

	public void setConfigService(ConfigurationService configService) {
		this.configService = configService;
	}

	public boolean isNotificationByConfigService() {
		return notificationByConfigService;
	}

	public void setNotificationByConfigService(boolean notificationByConfigService) {
		this.notificationByConfigService = notificationByConfigService;
	}

	public ConfigurationChangeRemoteListenerStub getListenerStub() {
		return listenerStub;
	}

	public void setListenerStub(ConfigurationChangeRemoteListenerStub listenerStub) {
		this.listenerStub = listenerStub;
	}
}
