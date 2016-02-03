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
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MRddMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;

public class Message2RddHadler extends AbstractM2MMessageHandler implements M2MMessageHandler {
	private static Logger LOG = Logger.getLogger(Message2RddHadler.class);
	
	private String master;
	private String appName;
	private SparkConf conf;
	private JavaSparkContext sc;
	
	
	public Message2RddHadler() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Message2RddHadler(String inTypeClassName, String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message");
		if(message instanceof M2MMultiMessage) {
			this.bulkInsertOfSensorData(((M2MMultiMessage) message).getSensorDataMessageList());
		} else {
			//this.dao.insertSensorData((M2MSensorMessage) message);
		}

	}

	public void bulkInsertOfSensorData(List<M2MSensorMessage> list){
		List<String> MessageData = new ArrayList<String>();
		for(M2MSensorMessage msg : list){
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
	
}
