package de.brockhaus.m2m.handler.aggregate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.handler.AbstractM2MMessageHandler;
import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MMessage;
import de.brockhaus.m2m.message.M2MMessageHandler;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;

/**
 * An Aggregation of incoming sensor messages. If there is a numerical value an
 * aggregation to maximum, to minimum or to averages will be performed. Any
 * other value will be left unchanged.
 * 
 * The result is a multi message as well but with all numerical values
 * 'condensed' and all the other values left unchanged.
 * 	
 	<!-- Aggregating the data -->
	<bean name="dataaggregation_handler"
		class="de.brockhaus.m2m.handler.aggregate.DataAggregationHandler"
		scope="singleton" >

		<!-- the accepted message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.message.M2MMultiMessage</value>
    	</constructor-arg>
    	<!-- the sent message type -->
		<constructor-arg>
        	<value type="java.lang.String">de.brockhaus.m2m.message.M2MMultiMessage</value>
    	</constructor-arg>
    	
    	<!-- who might pass (if not configured by config service) -->
		<property name="aggregationType" value="MAX" />

	</bean>	
 *
 * Project: m2m-base
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author mbohnen, Dec 9, 2015
 *
 */
public class DataAggregationHandler extends AbstractM2MMessageHandler implements M2MMessageHandler {
	
	private static final Logger LOG = Logger.getLogger(DataAggregationHandler.class);

	public enum AggregationType {
		MIN, MAX, AVG
	};

	private AggregationType aggregationType = AggregationType.MIN;

	public DataAggregationHandler() {
		super();
	}

	public DataAggregationHandler(String inTypeClassName, String outTypeClassName) {
		super(inTypeClassName, outTypeClassName);
	}

	@Override
	public <T extends M2MMessage> void handleMessage(T message) {
		LOG.debug("handling message");
		
		// what we get
		M2MMultiMessage multi = (M2MMultiMessage) message;
		// what we'll send
		M2MMultiMessage newMulti = new M2MMultiMessage();
		// the sensor messages included
		List<M2MSensorMessage> messages = multi.getSensorDataMessageList();

		// a per sensor mapping of all the values
		HashMap<String, List<Float>> perSensorMap = new HashMap<String, List<Float>>();

		// this loop goes through every message and aggregates to a per sensor
		// level by means of:
		// sensor A: [value 1, value 2, ...] if numerical
		for (M2MSensorMessage m2mSensorMessage : messages) {

			M2MDataType datatype = m2mSensorMessage.getDatatype();

			// only if we have numeric values
			if (datatype == M2MDataType.FLOAT) {

				if (!perSensorMap.containsKey(m2mSensorMessage.getSensorId())) {
					List<Float> values = new ArrayList<Float>();
					values.add(new Float(m2mSensorMessage.getValue()));
					perSensorMap.put(m2mSensorMessage.getSensorId(), values);

				} else {
					perSensorMap.get(m2mSensorMessage.getSensorId()).add(new Float(m2mSensorMessage.getValue()));
				}

			} else {
				// keeping the old ones which aren't of data type float
				newMulti.getSensorDataMessageList().add(m2mSensorMessage);
				break;
			}
		}

		// as we have a per sensor mapping now, we can calculate the
		// 'aggregation'
		for (String sensor : perSensorMap.keySet()) {
			List<Float> values = perSensorMap.get(sensor);

			Float result = 0f;
			switch (this.aggregationType) {
			case MAX:
				result = this.getMax(values);
				break;
			case MIN:
				result = this.getMin(values);
				break;
			case AVG:
				result = this.getAvg(values);
			}
			
			M2MSensorMessage newMessage = new M2MSensorMessage();
			newMessage.setSensorId(sensor);
			newMessage.setDatatype(M2MDataType.FLOAT);
			newMessage.setTime(new Date(System.currentTimeMillis()));
			newMessage.setValue(result.toString());
			
			newMulti.getSensorDataMessageList().add(newMessage);

		}
		
		this.setMessage(newMulti);

	}

	private float getMax(List<Float> values) {
		Float max = Collections.max(values);
		return max;
	}

	private float getMin(List<Float> values) {
		Float min = Collections.min(values);
		return min;
	}

	private float getAvg(List<Float> values) {
		int number = values.size();
		float current = 0f;
		for (Float value : values) {
			current += value;
		}
		return current / number;
	}

	public AggregationType getAggregationType() {
		return aggregationType;
	}

	public void setAggregationType(AggregationType aggregationType) {
		this.aggregationType = aggregationType;
	}

}
