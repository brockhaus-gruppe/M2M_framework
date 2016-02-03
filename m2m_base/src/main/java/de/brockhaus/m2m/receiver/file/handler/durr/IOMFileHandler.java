package de.brockhaus.m2m.receiver.file.handler.durr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import de.brockhaus.m2m.message.M2MDataType;
import de.brockhaus.m2m.message.M2MMultiMessage;
import de.brockhaus.m2m.message.M2MSensorMessage;
import de.brockhaus.m2m.receiver.file.FileEvent;
import de.brockhaus.m2m.receiver.file.FileEventHandler;
import de.brockhaus.m2m.receiver.file.FileHandlerCallback;

/**
 * The specific implementation of a handler capable of DÜRRs IOM files.
 * 
 * All sensor data for a day will be stored within a certain sub-directory representing the day.
 * Each sensor has its own file, the name of the sensor is part of the filename, e.g.:
 * 
 * -> PT_DS1_316233.ED01_AB219_M04.AS.V2251_Setpoint.csv
 *  
 * 
 * Project: communication.sender
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 * 
 * @author mbohnen, Apr 12, 2015
 *
 */
public class IOMFileHandler implements FileEventHandler {
	
	private static final Logger LOG = Logger.getLogger(IOMFileHandler.class);

	private File file;
	private FileHandlerCallback callback;
	private M2MMultiMessage multiMsg = new M2MMultiMessage();

	private String sensorId;

	public void handleFile(File file, FileEvent event, FileHandlerCallback callback) {

		this.file = file;
		this.callback = callback;

		this.readFile();
	}

	private void readFile() {
		
		// getting the partial date out of the path of the file (name of subdirectory)
		String path = file.getParent();
		int i = path.lastIndexOf('/');
		String partialdate = path.substring(i+1);
		
		// getting the name of sensor out of the filename 
		// (e.g.: PT_DS1_316233.ED01_AB219_M04.AS.V2251_Setpoint.csv)
		String filename = file.getName();
		int j = filename.lastIndexOf('_');
		sensorId = filename.substring(0, j);
		
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			 
			String line = null;
			while ((line = br.readLine()) != null) {
				// removing annoying characters
				char[] obsolete = {'"'};
				line = line.replace(new String(obsolete), "");
				String[] parts = line.split(";");
				ArrayList<String> list = new ArrayList<String>();
				list.addAll(Arrays.asList(parts));
				list.add(partialdate);
				
				this.createMessage(list);
			}
		 
			br.close();
			
			// invoking callback
			this.callback.handleEventResult(this.multiMsg);
			
			// flushing old data
			this.multiMsg.getSensorDataMessageList().clear();
			
		} catch (FileNotFoundException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		}
	}

	// nasty hack ... improve!
	private void createMessage(List<String> parts) {
		// e.g.: 00:07:54
		String time = parts.get(0);
		// e.g.:  20120331
		String date = parts.get(3);
		
		String value = parts.get(1);
		
		LocalTime locTime = LocalTime.parse(time.trim(), DateTimeFormatter.ofPattern("HH:mm:ss"));
		LocalDate locDate = LocalDate.parse(date.trim(), DateTimeFormatter.ofPattern("yyyyMMdd"));
		LocalDateTime dateTime = LocalDateTime.of(locDate, locTime);
		
		M2MSensorMessage msg = new M2MSensorMessage();
		msg.setSensorId(this.sensorId);
		msg.setDatatype(M2MDataType.FLOAT);
		
		// playing around, check here: 
		// http://stackoverflow.com/questions/19431234/converting-between-java-time-localdatetime-and-java-util-date
		msg.setTime(Date.from(dateTime.toInstant(ZoneOffset.UTC)));
		msg.setValue(value);
		
		this.multiMsg.getSensorDataMessageList().add(msg);
		LOG.trace("Messages read: " + this.multiMsg.getSensorDataMessageList().size());
	}
}
