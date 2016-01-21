package de.brockhaus.m2m.receiver.opcua.prosys;

import java.net.URISyntaxException;
import java.util.ArrayList;
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

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;

/**
 * 
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author mbohnen, Jan 12, 2016
 *
 */
public class OpcUaReceiverAdapter extends AbstractM2MMessageHandler implements MonitoredDataItemListener {

	// just a Logger
	Logger log = Logger.getLogger(this.getClass().getName());

	// TODO Spring DI
	private String serverUri = "opc.tcp://192.168.178.45:49320";

	public static final Logger LOG = Logger.getLogger(OpcUaReceiverAdapter.class);

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
	int[] readTags = {0, 1, 2, 3, 4, 5, 6, 7, 8};
	
	// allow to monitor variables that are changing in the server
	Subscription subscription = new Subscription();
	
	/* this variable selects the possible data that can be obtained from the node.
	In this case, the parameter "value" of the node (integer number 13 in the 
	node attributes list).*/
	UnsignedInteger attributeId = UnsignedInteger.valueOf(13);
	
	// define the corresponding listener that monitors and print value changes on items
//	private static MonitoredDataItemListener dataChangeListener = new MonitoredDataItemListener() {
//		@Override
//		public void onDataChange(MonitoredDataItem sender, DataValue prevValue, DataValue value) {
//			MonitoredItem i = sender;
//			println(dataValueToString(i.getNodeId(), i.getAttributeId(), value));
//		}
//	};
	
	private boolean printStructure;
	
	
	public static void main(String[] args) {
		new OpcUaReceiverAdapter();
	}
	
	// constructor
	public OpcUaReceiverAdapter() {
		try {
			this.init();
		} catch (URISyntaxException | ServiceResultException | ServiceException | StatusException e) {
			LOG.error(e);
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

		// identify the product and should therefore be the same for all
		// instances
		appDescription.setProductUri("urn:prosysopc.com:UA:OpcuaClient");

		// define the type of application
		appDescription.setApplicationType(ApplicationType.Client);

		// define the client application certificate
		ApplicationIdentity identity = new ApplicationIdentity();
		identity.setApplicationDescription(appDescription);
		
		// assign the identity to the Client
		client.setApplicationIdentity(identity);
		
		// define a limit of 1000 references per call to the server
		client.getAddressSpace().setMaxReferencesPerNode(1000);
		
		// receive only the hierarchical references between the nodes
		client.getAddressSpace().setReferenceTypeId(Identifiers.HierarchicalReferences);
		
		if(printStructure) {
			LOG.info(this.printStructure());
		}
		
		this.configureRead();
		
		// keep the things rolling
		while(true);
	}
	
	// tiny little helper to visualize the OPC structure
	public String printStructure() {
		// TODO implement
		return "the master structure";
	}

	private void configureRead() throws ServiceResultException, ServiceException, StatusException {
		
		//populate references
		NodeId nodeId = Identifiers.RootFolder;
		references = client.getAddressSpace().browse(nodeId);
//		browse(nodeId);

		// select the Objects node & browse the references for this node
		nodeId = selectNode(1);
		references = client.getAddressSpace().browse(nodeId);
//		browse(nodeId);

		// select the Channel node "Siemens PLC S7-1200" & browse its references
		nodeId = selectNode(14);
		references = client.getAddressSpace().browse(nodeId);
//		browse(nodeId);

		// select the Device node "s7-1200" & browse its references
		nodeId = selectNode(2);
		references = client.getAddressSpace().browse(nodeId);
//		browse(nodeId);

		// select the node "Inputs" & browse its references
		nodeId = selectNode(3);
		references = client.getAddressSpace().browse(nodeId);
//		browse(nodeId);

		

		for (int i = 0; i < readTags.length; i++)
			TagsArray.add(i, selectNode(readTags[i]));

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


		log.info("[-- READING VALUES CHANGES FROM THE SERVER NODES --]");
	}
	

	/* select the next node according to its integer value in the list of references of the current node */
	public NodeId selectNode(int selection) throws ServiceResultException {
		ReferenceDescription r = references.get(selection);
		target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
		return target;
	}
	
	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDataChange(MonitoredDataItem item, DataValue oldVal, DataValue newVal) {
		LOG.debug("Change detected");
		
	}

}
