package de.brockhaus.m2m.message;

import org.apache.spark.api.java.JavaRDD;

public class M2MRddMessage extends M2MMessage{
	private JavaRDD<String> rddMessage;

	public JavaRDD<String> getRddMessage() {
		return rddMessage;
	}

	public void setRddMessage(JavaRDD<String> rddMessage) {
		this.rddMessage = rddMessage;
	}
	
}
