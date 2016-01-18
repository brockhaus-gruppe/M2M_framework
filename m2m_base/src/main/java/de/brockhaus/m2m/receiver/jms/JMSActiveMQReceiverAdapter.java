package de.brockhaus.m2m.receiver.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MCommunicationException;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MPlainTextMessage;

/**
 * The ActiveMQ receiver ... not durable!
 * 
 * Example config:
 * 
	<!-- JMS based receiving using ActiveMQ -->
	<bean name="jms_adapter"
		class="de.brockhaus.m2m.receiver.jms.JMSActiveMQReceiverAdapter" 
		scope="singleton" 
		init-method="init" 
		destroy-method="cleanUp">
		
		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.handler.M2MPlainTextMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.handler.M2MPlainTextMessage</value>
    	</constructor-arg>
    	
    	<!-- who deals with the file once content is sent -->
		<property name="msgDestName">
			<value>myTopic</value>
		</property>
    	
    	<!-- how to send, @see: http://activemq.apache.org/activemq-4-connection-uris.html -->
    	<property name="brokerConfig">
			<value>tcp://localhost:61616?jms.useAsyncSend=true</value>
		</property>
	
	</bean>
 * 
 * Project: communication.receiver
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, Apr 20, 2015
 *
 */
public class JMSActiveMQReceiverAdapter extends AbstractM2MMessageHandler
		implements MessageListener {
	
	private static final Logger LOG = Logger.getLogger(JMSActiveMQReceiverAdapter.class);

	private ConnectionFactory factory;
	private Connection connection;
	private Session session;
	private Destination destination;
	private MessageConsumer consumer;

	private String msgDestName;

	private String brokerConfig;
	
	private int messagesReceived = 0;

	private M2MPlainTextMessage plainTextMessage;

	public JMSActiveMQReceiverAdapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JMSActiveMQReceiverAdapter(String inTypeClassName, String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);
	}

	// invoked on every message ...
	public void onMessage(Message msg) {
		this.messagesReceived++;
		LOG.debug("Message received in this period: " + this.messagesReceived);
		try {
			if (msg instanceof TextMessage) {	
				TextMessage text = (TextMessage) msg;
				LOG.debug("Message is : " + text.getText());
				plainTextMessage = new M2MPlainTextMessage();
				plainTextMessage.setSensordata(text.getText());
				
				// continue
				super.onMessageEvent(plainTextMessage);
			} else {
				LOG.error("Can't handle message type: " + msg.getClass().getSimpleName());
			}
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (M2MCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		// nothing to do here ... all is done within onMessage()
	}

	public void init() {
		try {

			factory = new ActiveMQConnectionFactory(brokerConfig);
			connection = factory.createConnection();
			connection.start();

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic(this.msgDestName);
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(this);
			
			LOG.info("JMS receiver up and listening to: " + this.msgDestName);
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cleanup() {
		LOG.debug("housekeeping");

		try {
			this.session.close();
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

	public String getBrokerConfig() {
		return brokerConfig;
	}

	public void setBrokerConfig(String brokerConfig) {
		this.brokerConfig = brokerConfig;
	}
}
