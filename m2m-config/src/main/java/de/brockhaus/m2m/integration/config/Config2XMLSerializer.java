package de.brockhaus.m2m.integration.config;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.brockhaus.m2m.config.ConfigSerializer;
import de.brockhaus.m2m.config.Configuration;
import de.brockhaus.m2m.integration.annotation.XMLSerializer;

/**
 * Careful: nested maps aren't supported out of the box 
 * http://stackoverflow.com/questions/818327/jaxb-how-should-i-marshal-complex-nested-data-structures
 * 
 * Project: integration_config
 *
 * Copyright (c) by Brockhaus Group
 * www.brockhaus-gruppe.de
 * @author mbohnen, May 10, 2015
 *
 */
@XMLSerializer
public class Config2XMLSerializer implements ConfigSerializer {

	File file = new File("/home/mbohnen/Desktop/Config.xml");
	
	public void saveConfiguration(Configuration config) { 
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
 
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
 
			jaxbMarshaller.marshal(config, file);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Configuration readConfiguration() {
		Configuration config = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
			 
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			config = (Configuration) jaxbUnmarshaller.unmarshal(file);
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return config;
	}

}
