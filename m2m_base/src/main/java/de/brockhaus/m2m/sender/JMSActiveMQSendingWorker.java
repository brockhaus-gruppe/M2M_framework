package de.brockhaus.m2m.sender;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MPlainTextMessage;

/**
 * Sending to a topic, the receiving adapter might be a Message-driven Bean or a standalone solution
 * like ActiveMQ
 * 
 * Config example:

	<!-- JMS based sending using ActiveMQ -->
	<bean name="jms-sender"
		class="de.brockhaus.m2m.sender.JMSActiveMQSendingWorker" scope="singleton" init-method="init" destroy-method="cleanUp">
		
		<!-- the last in line, no following handler -->
		<constructor-arg name = "next">
			<null />
		</constructor-arg>

		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.handler.M2MPlainTextMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.handler.M2MSensorMessage</value>
    	</constructor-arg>
    	
    	<!-- who deals with the file once content is sent -->
		<property name="msgDestName">
			<value>myTopic</value>
		</property>
		
		<!-- terminate VM -->
		<property name="shutdownAfterSending">
			<value>true</value>
		</property>
    	
    	<!-- how to send, @see: http://activemq.apache.org/activemq-4-connection-uris.html -->
    	<property name="brokerConfig">
			<value>tcp://localhost:61616?jms.useAsyncSend=true</value>
		</property>
	
	</bean>


 * 
 * Project: communication.sender
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 11, 2015
 *
 */
public class JMSActiveMQSendingWorker extends AbstractM2MMessageHandler {
	
	private static final Logger LOG = Logger.getLogger(JMSActiveMQSendingWorker.class);
	
	private Session session;
	private Destination destination;
	private MessageProducer producer;
	private Connection connection;

	private String msgDestName;
	private String brokerConfig;
	
	private boolean shutdownAfterSending;
	

	public JMSActiveMQSendingWorker() {
		super();
	}

	public JMSActiveMQSendingWorker(M2MMessageHandler next,
			String inTypeClassName, String outTypeClassName) {
		super(next, inTypeClassName, outTypeClassName);
	}

	public void doSend(M2MMessage msg) {
		
		// are we ready?
		if(shutdownAfterSending) {
			this.init();
		}
		
		try {
			TextMessage message = session.createTextMessage();
			message.setText(((M2MPlainTextMessage) msg).getSensordata());
			producer.send(message);
			LOG.info("Sending message to: " + this.msgDestName);
			LOG.debug("Sending message to: " + this.msgDestName + "\n" + message.getText());
			
			// close everything
			if(shutdownAfterSending) {
				this.cleanUp();
			}
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message");
		this.doSend(message);
		
	}
	
	public void init() {
		try {
			LOG.debug("initializing");
			// the proprietary way NOT using InitialContext
			ConnectionFactory conFactory= new ActiveMQConnectionFactory(this.brokerConfig);
			
			// Getting JMS connection from the server and starting it
			connection = conFactory.createConnection();
			connection.start();

			// JMS messages are sent and received using a Session. We will
			// create here a non-transactional session object. If you want
			// to use transactions you should set the first parameter to 'true'
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// a client can create a new Queue or Topic dynamically either by calling 
			// createQueue() or createTopic() on a JMS Session
			destination = session.createTopic(msgDestName);

			// MessageProducer is used for sending messages (as opposed
			// to MessageConsumer which is used for receiving them)
			producer = session.createProducer(destination);
			
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** housekeeping */
	private void cleanUp() {
		LOG.debug("housekeeping");
		
		try {
			this.session.close();
			this.producer.close();
			this.connection.close();
			System.exit(0);
			
		} catch (JMSException e) {		
			e.printStackTrace();
		}
	}

	public String getMsgDestName() {
		return msgDestName;
	}

	public void setMsgDestName(String msgDestName) {
		this.msgDestName = msgDestName;
	}

	public boolean isShutdownAfterSending() {
		return shutdownAfterSending;
	}

	public void setShutdownAfterSending(boolean shutdownAfterSending) {
		this.shutdownAfterSending = shutdownAfterSending;
	}

	public String getBrokerConfig() {
		return brokerConfig;
	}

	public void setBrokerConfig(String brokerConfig) {
		this.brokerConfig = brokerConfig;
	}	
	
	
}
