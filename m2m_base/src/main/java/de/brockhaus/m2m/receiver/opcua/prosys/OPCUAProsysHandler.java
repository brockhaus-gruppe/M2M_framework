package de.brockhaus.m2m.receiver.opcua.prosys;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
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
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.transport.security.SecurityMode;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.MonitoredDataItemListener;
import com.prosysopc.ua.client.MonitoredItem;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.UaClient;

import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.receiver.opcua.M2MMessageOpcUaReceiver;
import de.brockhaus.m2m.receiver.opcua.OPCUAHandler;

/**
 * The Prosys proprietary handler to read the data from an OPC Server.
 * 
 * It might look somehow strange how we used the parameters during
 * configureRead(); but all of this deals with the hierarchical nodes within
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
 * @author mbohnen, Jan 12, 2016
 *
 */
public class OPCUAProsysHandler implements MonitoredDataItemListener, OPCUAHandler {

	// just a logger
	public static final Logger LOG = Logger.getLogger(OPCUAProsysHandler.class);

	// the handler for dealing with the messages
	private M2MMessageOpcUaReceiver receiver;

	// where we are connecting to
	private String serverUri;

	// create an UaClient object which encapsulates the connection to the OPC UA
	// server
	private UaClient client;

	// define a target node Id for the selected node
	NodeId target;

	// create an array of tags which can be read or written
	ArrayList<NodeId> TagsArray = new ArrayList<NodeId>();

	// define a list of references(children or hierarchical relationships)
	// for each selected node
	List<ReferenceDescription> references = new ArrayList<ReferenceDescription>();

	// select the tags corresponding to "Inputs"
	int[] readTags;

	// allow to monitor variables that are changing in the server
	Subscription subscription = new Subscription();

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
	public OPCUAProsysHandler(M2MMessageOpcUaReceiver receiver) {
		
		this.receiver = receiver;
		
		if (!receiver.isSimMode()) {
			try {
				this.init();
			} catch (URISyntaxException | ServiceResultException | ServiceException | StatusException e) {
				LOG.error(e);
			}
		} 
	}

	private void init() throws URISyntaxException, ServiceResultException, ServiceException, StatusException {

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

		if (printStructure) {
			LOG.info(this.printStructure());
		}

		this.configureRead();

	}

	// tiny little helper to visualize the OPC structure
	public String printStructure() {
		// TODO implement
		return "the master structure";
	}

	private void configureRead() throws ServiceResultException, ServiceException, StatusException {

		// populate references
		NodeId nodeId = Identifiers.RootFolder;
		references = client.getAddressSpace().browse(nodeId);
		// browse(nodeId);

		// select the Objects node & browse the references for this node
		nodeId = selectNode(1);
		references = client.getAddressSpace().browse(nodeId);
		// browse(nodeId);

		// select the Channel node "Siemens PLC S7-1200" & browse its references
		nodeId = selectNode(14);
		references = client.getAddressSpace().browse(nodeId);
		// browse(nodeId);

		// select the Device node "s7-1200" & browse its references
		nodeId = selectNode(2);
		references = client.getAddressSpace().browse(nodeId);
		// browse(nodeId);

		// select the node "Inputs" & browse its references
		nodeId = selectNode(3);
		references = client.getAddressSpace().browse(nodeId);
		// browse(nodeId);

		for (int i = 0; i < readTags.length; i++)
			TagsArray.add(i, selectNode(readTags[i]));

		// for all tags we're interested in
		for (int i = 0; i < TagsArray.size(); i++) {
			// include a number of monitored items, which you listen to
			MonitoredDataItem item = new MonitoredDataItem(TagsArray.get(i), attributeId, MonitoringMode.Reporting);

			// add the monitored item to the subscription
			subscription.addItem(item);

			// add the subscription to the client
			client.addSubscription(subscription);

			// establish a listener for each item
			item.setDataChangeListener(this);
		}

		LOG.info("[-- READING VALUES CHANGES FROM THE SERVER NODES --]");
	}

	/*
	 * select the next node according to its integer value in the list of
	 * references of the current node
	 */
	public NodeId selectNode(int selection) throws ServiceResultException {
		ReferenceDescription r = references.get(selection);
		target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
		return target;
	}

	@Override
	public void onDataChange(MonitoredDataItem item, DataValue oldVal, DataValue newVal) {
		LOG.debug("Change detected");

		MonitoredItem mItem = item;

		LOG.debug("\nNodeId: " + mItem.getNodeId().getValue() + "\n" + "attribute: " + mItem.getAttributeId() + "\n"
				+ "value: " + newVal.getValue().toString() + "\n");

		M2MSensorMessage msg = new M2MSensorMessage();
		msg.setSensorId(mItem.getNodeId().getValue().toString());
		msg.setValue(newVal.getValue().toString());
		msg.setTime(new Date(newVal.getServerTimestamp().getTimeInMillis()));
		
		//TODO mapping of OPCDataTypes
		msg.setDatatype(M2MDataType.BOOLEAN);

		this.receiver.handleMessage(msg);
	}
	
	@Override
	public void startSimulation() {
		try {
			new OPCProsysSimServer(this).sendMessages();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public M2MMessageOpcUaReceiver getReceiver() {
		return receiver;
	}

	public void setReceiver(M2MMessageOpcUaReceiver receiver) {
		this.receiver = receiver;
	}


	public String getServerUri() {
		return serverUri;
	}

	public void setServerUri(String serverUri) {
		this.serverUri = serverUri;
	}

	public ArrayList<NodeId> getTagsArray() {
		return TagsArray;
	}

	public void setTagsArray(ArrayList<NodeId> tagsArray) {
		TagsArray = tagsArray;
	}

	public int[] getReadTags() {
		return readTags;
	}

	public void setReadTags(int[] readTags) {
		this.readTags = readTags;
	}	
}
