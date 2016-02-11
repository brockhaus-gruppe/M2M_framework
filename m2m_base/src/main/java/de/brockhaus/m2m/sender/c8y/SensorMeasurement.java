package de.brockhaus.m2m.sender.c8y;

import java.math.BigDecimal;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;

import com.cumulocity.model.measurement.MeasurementValue;

public class SensorMeasurement extends AbstractDynamicProperties {
	
	private static final long serialVersionUID = 1L;
	public static final String SENSOR_UNIT = "";
    private MeasurementValue v = new MeasurementValue(SENSOR_UNIT);
    
    @JSONProperty("Value")
    public MeasurementValue getV() {
        return v;
    }

    public void setV(MeasurementValue v) {
        this.v = v;
    }

    @JSONProperty(ignore = true)
    public BigDecimal getValue() {
        return v == null ? null : v.getValue();
    }

    public void setValue(BigDecimal value) {
        v = new MeasurementValue(SENSOR_UNIT);
        v.setValue(value);
    }
}