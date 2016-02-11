package de.brockhaus.m2m.receiver.file.handler.brueckner;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.esotericsoftware.minlog.Log;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MMessageReceiverLifecycle;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MRddMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;
/**
 * 
 * Storing the sensor data out of a M2MMultiMessage into a JavaRdd for spark treatment.
 * 
 * Example config:
 * 
 	<bean name="rrd_handler"
		class="de.brockhaus.m2m.receiver.file.handler.brueckner.Message2RddHandler"
		parent="abstract_handler" init-method="init" scope="singleton">

		<!-- the accepted message type -->
		<constructor-arg>
			<value type="java.lang.String">de.brockhaus.m2m.message.M2MMultiMessage</value>
		</constructor-arg>
		<!-- the sent message type -->
		<constructor-arg>
			<value type="java.lang.String">de.brockhaus.m2m.message.M2MRddMessage</value>
		</constructor-arg>
		<property name="master">
			<value>local</value>
		</property>
		<property name="appName">
			<value>application rdd</value>
		</property>
	</bean>
	
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 *
 */
public class Message2RddHandler extends AbstractM2MMessageHandler implements M2MMessageReceiverLifecycle {
	private static Logger LOG = Logger.getLogger(Message2RddHandler.class);
	
	private String master;
	private String appName;
	private SparkConf conf;
	private JavaSparkContext sc;
	
	
	public Message2RddHandler() {
		super();
	}

	public Message2RddHandler(String inTypeClassName, String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		if(message instanceof M2MMultiMessage) {
			LOG.debug("handling message");
			this.bulkInsertOfSensorData(((M2MMultiMessage) message).getSensorDataMessageList());
		} else {
			LOG.debug("We can't handle this message");
			//this.dao.insertSensorData((M2MSensorMessage) message);
		}

	}

	public void bulkInsertOfSensorData(List<M2MSensorMessage> list){
		List<String> MessageData = new ArrayList<String>();
		for(M2MSensorMessage msg : list){
			LOG.debug("Getting message : "+msg.getSensorId()+", "+msg.getDatatype()+", "+msg.getTime()+", "+msg.getValue());
			MessageData.add(msg.getSensorId()+" "+msg.getDatatype()+" "+msg.getTime()+" "+msg.getValue());
		}
		LOG.debug("loading bulk of sensor data into rdd");
		JavaRDD<String> javaRDDMessage = sc.parallelize(MessageData);
		LOG.debug(javaRDDMessage.count()+" elements into rdd Message");
		M2MRddMessage rddMessage = new M2MRddMessage();
		rddMessage.setRddMessage(javaRDDMessage);
		this.setMessage(rddMessage);
	}
	
	public void init(){
		this.conf = new SparkConf().setMaster(master).setAppName(appName);
		this.sc = new JavaSparkContext(this.conf);
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
}
