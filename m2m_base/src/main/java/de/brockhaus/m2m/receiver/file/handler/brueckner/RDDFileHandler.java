package de.brockhaus.m2m.receiver.file.handler.brueckner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import de.brockhaus.m2m.message.M2MRddMessage;
import de.brockhaus.m2m.receiver.file.FileEvent;
import de.brockhaus.m2m.receiver.file.FileEventHandler;
import de.brockhaus.m2m.receiver.file.FileHandlerCallback;
/**
 * Config example :
 * 
 	<bean name="file_read_handler" class="de.brockhaus.m2m.receiver.file.handler.durr.RDDFileHandler" >
		<property name="master">
			<value>local</value>
		</property>
		<property name="appName">
			<value>application rdd</value>
		</property>
		
	</bean>
 * 
 * 
 * Project: communication.sender
 *
 * Copyright (c) by Brockhaus Group www.brockhaus-gruppe.de
 *
 */
public class RDDFileHandler implements FileEventHandler{

	private static final Logger LOG = Logger.getLogger(RDDFileHandler.class);
	private File file;
	private FileHandlerCallback callback;
	private String master;
	private String appName;
	private SparkConf conf;
	private JavaSparkContext sc;
	private List<String> stringMessage = new ArrayList<String>();
	private M2MRddMessage rddMessage;
	private String sensorId;

	@Override
	public void handleFile(File file, FileEvent event,
			FileHandlerCallback callback) {
		this.file = file;
		this.callback = callback;

		this.readFile();

	}

	private void readFile() {
		// getting the partial date out of the path of the file (name of subdirectory)
		//String path = file.getParent();
		//int i = path.lastIndexOf('/');
		//String partialdate = path.substring(i+1);

		// getting the name of sensor out of the filename 
		// (e.g.: Anlage 1 - Daten (konsolidiert).csv)
		String filename = file.getName();
		int j = filename.lastIndexOf('-');
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
				String newLine = "";
				for(String s :parts){
					newLine = newLine.concat(" "+s);
				}

				this.stringMessage.add(newLine);
			}
			
			this.createMessage(stringMessage);
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void createMessage(List<String> parts) {
		
		JavaRDD<String> rdd = sc.parallelize(parts);
		rddMessage = new M2MRddMessage();
		rddMessage.setRddMessage(rdd);
		rddMessage.setSensorId(sensorId);
		LOG.info("the number of loaded Elements : "+rddMessage.getRddMessage().count());
		LOG.trace("the number of loaded Elements : "+rddMessage.getRddMessage().count());
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
