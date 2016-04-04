package de.brockhaus.m2m.sender.opcua.prosys;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.transport.security.SecurityMode;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.SessionActivationException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.client.AddressSpaceException;
import com.prosysopc.ua.client.ConnectException;
import com.prosysopc.ua.client.InvalidServerEndpointException;
import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.MonitoredDataItemListener;
import com.prosysopc.ua.client.MonitoredItem;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.UaClient;
import com.prosysopc.ua.nodes.UaDataType;
import com.prosysopc.ua.nodes.UaNode;
import com.prosysopc.ua.nodes.UaVariable;

import de.brockhaus.m2m.config.ConfigurationServiceLocal;
import de.brockhaus.m2m.integration.config.ConfigurationServiceFactory;
import de.brockhaus.m2m.integration.config.c8y.C8YSensorMapping;
import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.receiver.opcua.M2MMessageOpcUaReceiver;
import de.brockhaus.m2m.receiver.opcua.OPCUAHandler;
import de.brockhaus.m2m.receiver.opcua.prosys.OPCProsysSimulationServer;
import de.brockhaus.m2m.sender.opcua.OPCUASendingWorker;
import de.brockhaus.m2m.sender.c8y.util.SensorMapping;

/**
 * The Prosys proprietary handler to write the data to an OPC Server.
 * 
	<bean name="prosys_handler" class="de.brockhaus.m2m.sender.opcua.prosys.OPCUAProsysHandler"
		scope="singleton">
		<!-- OPC server uri -->
		<property name="serverUri">
			<value>opc.tcp://127.0.0.1:49320</value>
		</property>

		<!-- the tags we're dealing with -->
		<property name="writeTags">
			<value>0, 1, 2, 3, 4, 5, 6, 7, 8, 9</value>
		</property>		
	</bean>
 * 
 * It might look somehow strange how we used the parameters during
 * configureWrite(); but all of this deals with the hierarchical nodes within
 * OPC. For the current example (Fischertechnik plus S7) it looks like this:
 * 
 * [-- NODE HIERARCHICAL REFERENCES --] 
 * 0 - Views: FolderType (ReferenceType=Organizes, BrowseName=Views) 
 * 1 - Objects: FolderType (ReferenceType=Organizes, BrowseName=Objects) 
 * 2 - Types: FolderType (ReferenceType=Organizes, BrowseName=Types)
 * 
 * => we browse for Objects so we picked '1'
 * 
 * 0 - Server: ServerType (ReferenceType=Organizes, BrowseName=Server) 
 * 1 -_AdvancedTags: FolderType (ReferenceType=Organizes, BrowseName=2:_AdvancedTags) 
 * 2 - _CustomAlarms: FolderType (ReferenceType=Organizes, BrowseName=2:_CustomAlarms) 
 * 3 - _DataLogger: FolderType (ReferenceType=Organizes, BrowseName=2:_DataLogger) 
 * 4 - _EFMExporter: FolderType (ReferenceType=Organizes, BrowseName=2:_EFMExporter)
 * 5 - _IDF_for_Splunk: FolderType (ReferenceType=Organizes, BrowseName=2:_IDF_for_Splunk) 
 * 6 - _IoT_Gateway: FolderType (ReferenceType=Organizes, BrowseName=2:_IoT_Gateway) 
 * 7 - _LocalHistorian: FolderType (ReferenceType=Organizes, BrowseName=2:_LocalHistorian) 
 * 8 - _OracleConnector: FolderType (ReferenceType=Organizes, BrowseName=2:_OracleConnector) 
 * 9 - _Redundancy: FolderType (ReferenceType=Organizes, BrowseName=2:_Redundancy) 
 * 10 - _Scheduler: FolderType (ReferenceType=Organizes, BrowseName=2:_Scheduler) 
 * 11 - _SecurityPolicies: FolderType (ReferenceType=Organizes BrowseName=2:_SecurityPolicies) 
 * 12 - _SNMP Agent: FolderType (ReferenceType=Organizes, BrowseName=2:_SNMP Agent) 
 * 13 - _System: FolderType (ReferenceType=Organizes, BrowseName=2:_System) 
 * 14 - Siemens PLC S7-1200: FolderType (ReferenceType=Organizes, BrowseName=2:Siemens PLC S7-1200)
 * 
 * => we go for PLC, so we picked '14'
 * 
 * 0 - _Statistics: FolderType (ReferenceType=Organizes, BrowseName=2:_Statistics) 
 * 1 - _System: FolderType (ReferenceType=Organizes, BrowseName=2:_System) 
 * 2 - s7-1200: FolderType (ReferenceType=Organizes, BrowseName=2:s7-1200)
 * 
 * => we go for S7, so we picked '2'
 * 
 * 0 - _System: FolderType (ReferenceType=Organizes, BrowseName=2:_System) 
 * 1 - _Statistics: FolderType (ReferenceType=Organizes, BrowseName=2:_Statistics) 
 * 2 - _InternalTags: FolderType (ReferenceType=Organizes, BrowseName=2:_InternalTags) 
 * 3 - Inputs: FolderType (ReferenceType=Organizes, BrowseName=2:Inputs) 
 * 4 - Outputs: FolderType (ReferenceType=Organizes, BrowseName=2:Outputs) 
 * 5 - Tag1: BaseVariableType (ReferenceType=HasComponent, BrowseName=2:Tag1)
 * 
 * => we go for Input so we picked '3', if we would like to set values we will go for Outputs 
 * 
 * Later on we picked the relevant info about the tags: 
 * 0 - Phototransistor conveyer belt swap: BaseVariableType (ReferenceType=HasComponent, BrowseName=2:Phototransistor conveyer belt swap) 
 * 1 - Phototransistor drilling machine: BaseVariableType (ReferenceType=HasComponent, BrowseName=2:Phototransistor drilling machine) 
 * 2 - Phototransistor loading station: BaseVariableType (ReferenceType=HasComponent, BrowseName=2:Phototransistor loading station) 
 * 3 - Phototransistor milling machine: BaseVariableType (ReferenceType=HasComponent, BrowseName=2:Phototransistor milling machine) 
 * 4 - Phototransistor slider 1: BaseVariableType (ReferenceType=HasComponent, BrowseName=2:Phototransistor slider 1) 
 * 5 - Push-button slider 1 front: BaseVariableType (ReferenceType=HasComponent, BrowseName=2:Push-button slider 1 front) 
 * 6 - Push-button slider 1 rear: BaseVariableType (ReferenceType=HasComponent, BrowseName=2:Push-button slider 1 rear) 
 * 7 - Push-button slider 2 front: BaseVariableType (ReferenceType=HasComponent, BrowseName=2:Push-button slider 2 front) 
 * 8 - Push-button slider 2 rear: BaseVariableType (ReferenceType=HasComponent, BrowseName=2:Push-button slider 2 rear)
 * 
 * These are put into the TagArray ...
 *
 * 
 * TODO get the tree-like structure visualized within printStructure()
 * 
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author jperez, Feb 19, 2016
 *
 */

public class OPCUAProsysHandler implements OPCUAHandler {
	
	// just a logger
	public static final Logger LOG = Logger.getLogger(OPCUAProsysHandler.class);

	// the handler for dealing with the messages
	private OPCUASendingWorker sender;
	
	private ConfigurationServiceFactory configServiceFactory;
	
	private ArrayList<C8YSensorMapping> sensorMappings;
	
	// where we are connecting to
	private String serverUri;

	/* create an UaClient object which encapsulates the connection to the OPC UA
	 * server
	 */
	private UaClient client;

	// define a target node Id for the selected node
	NodeId targetNode;

	/* define a list of references(children or hierarchical relationships)
	 *  for each selected node
	 */
	List<ReferenceDescription> references = new ArrayList<ReferenceDescription>();

	// select the tag corresponding to "Outputs" for writing
	int targetTag;
	
	/*
	 * this variable selects the possible data that can be obtained from the
	 * node. In this case, the parameter "value" of the node (integer number 13
	 * in the node attributes list).
	 */
	UnsignedInteger attributeId = UnsignedInteger.valueOf(13);

	// should some kind of OPC structure be printed?
	private boolean printStructure;
		

	public OPCUAProsysHandler() {
	// lazy
	}
	
	// constructor
	public OPCUAProsysHandler(OPCUASendingWorker sender) {
		this.sender = sender;
	}
		
	public void start() {
		try {
			// initiate the sensorMappings
			ConfigurationServiceLocal configService = ConfigurationServiceFactory.getConfigurationServiceLocal();				
			HashMap<String, String> sensors = configService.getConfig().getConfigForElement("sensors_outputs");
			sensorMappings = new ArrayList<C8YSensorMapping>();
			Collection<String> sensorMappingData = sensors.values();
						
			for (String sensorMappingString : sensorMappingData) {
			/*
			 *  ArrayIndex, own GId, parent GId, sensor name
			 *  "0" : "0;15515;10979;Siemens PLC S7-1200.s7-1200.Outputs.motor conveyor belt drilling machine",
			 */
				String[] data = sensorMappingString.split(";");
				sensorMappings.add(new C8YSensorMapping(new Integer(data[0]), data[3], data[1], data[2]));
			}
			
			// initiate the connection to the server
			client = new UaClient(this.serverUri);

			// define the security level in the OPC UA binary communications
			client.setSecurityMode(SecurityMode.NONE);

			// create an Application Description which is sent to the server
			ApplicationDescription appDescription = new ApplicationDescription();
			appDescription.setApplicationName(new LocalizedText("OpcuaClient", Locale.ENGLISH));

			// ApplicationUri is a unique identifier for each running instance
			appDescription.setApplicationUri("urn:localhost:UA:OpcuaClient");

			// identify the product and should therefore be the same for all instances
			appDescription.setProductUri("urn:prosysopc.com:UA:OpcuaClient");

			// define the type of application
			appDescription.setApplicationType(ApplicationType.Client);

			// define the client application certificate
			final ApplicationIdentity identity = new ApplicationIdentity();
				identity.setApplicationDescription(appDescription);

			// assign the identity to the Client
			client.setApplicationIdentity(identity);

			// define a limit of 1000 references per call to the server
			client.getAddressSpace().setMaxReferencesPerNode(1000);

			// receive only the hierarchical references between the nodes
			client.getAddressSpace().setReferenceTypeId(Identifiers.HierarchicalReferences);

			// connect to the server
			client.connect();
		
			// populate references
			NodeId nodeId = Identifiers.RootFolder;
			references = client.getAddressSpace().browse(nodeId);

			// select the Objects node & browse the references for this node
			nodeId = selectNode(1);
			references = client.getAddressSpace().browse(nodeId);

			// select the Channel node "Siemens PLC S7-1200" & browse its references
			nodeId = selectNode(14);
			references = client.getAddressSpace().browse(nodeId);

			// select the Device node "s7-1200" & browse its references
			nodeId = selectNode(2);
			references = client.getAddressSpace().browse(nodeId);

			// select the node "Outputs" & browse its references
			nodeId = selectNode(4);
			references = client.getAddressSpace().browse(nodeId);				
		} 
		catch (URISyntaxException | ServiceException | StatusException | ServiceResultException e) {
				e.printStackTrace();
		}
	}
	
		// tiny little helper to visualize the OPC structure
		public String printStructure() {
			// TODO implement
			return "the master structure";
		}
	
	public void configureWrite() throws ServiceResultException, ServiceException, StatusException, AddressSpaceException, InterruptedException {
		
		LOG.debug("configure write for: " + sender.getMessage().getClass().getSimpleName());
		M2MSensorMessage msg = (M2MSensorMessage) sender.getMessage();
		String owngid = msg.getSensorId();
		String value = msg.getValue();
		setWriteTag(owngid);
		targetNode = selectNode(targetTag);
		System.out.println(targetTag);
		
		LOG.info("\n[-- WRITING VALUES TO THE SERVER NODES --]");	
		
		// select the node corresponding to the selected tag
		UaNode node = client.getAddressSpace().getNode(targetNode);
		System.out.println("Writing to node: " + node.getDisplayName().getText());
		System.out.println("Value: " + value);
		// write the value
		client.writeAttribute(targetNode, attributeId, new Boolean(value));
	}
	
	/*
	 * select the next node according to its integer value in the list of
	 * references of the current node
	 */
	public NodeId selectNode(int selection) throws ServiceResultException {
		ReferenceDescription r = references.get(selection);
		return client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
	}

	public String getServerUri() {
		return serverUri;
	}

	public void setServerUri(String serverUri) {
		this.serverUri = serverUri;
	}

	public int getTargetTag() {
		return targetTag;
	}

	public void setWriteTag(String gid) {	
		for (C8YSensorMapping temp : sensorMappings) {
			if(temp.getOwnGId().equals(gid))
				this.targetTag = temp.getArrayIndex();
				break;
		}
	}


	@Override
	public void setReceiver(M2MMessageOpcUaReceiver receiver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSender(OPCUASendingWorker sender) {
		this.sender = sender;		
	}
	
	public ConfigurationServiceFactory getConfigServiceFactory() {
		return configServiceFactory;
	}

	public void setConfigServiceFactory(ConfigurationServiceFactory configServiceFactory) {
		this.configServiceFactory = configServiceFactory;
	}
}