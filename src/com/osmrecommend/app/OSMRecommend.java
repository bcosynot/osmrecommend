package com.osmrecommend.app;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openstreetmap.osmosis.core.database.DatabaseLoginCredentials;
import org.openstreetmap.osmosis.core.database.DatabaseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OSMRecommend {
	
	private static Logger log = LoggerFactory.getLogger(OSMRecommend.class);
	
	public static void main(String args[]) {
		
		log.debug("inside OSMRecommend:psvm");
		
		CompositeConfiguration config = new CompositeConfiguration();
		try {
			config.addConfiguration(new PropertiesConfiguration("app.properties"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		String host = config.getString("postgresHost");
		String database = config.getString("postgresDB");
		String user = config.getString("postgresUser");
		String password = config.getString("postgresPassword");
		 
		
		DatabaseLoginCredentials DatabaseLoginCredentials = 
			new DatabaseLoginCredentials(host, database, user, password, false, false, DatabaseType.POSTGRESQL);
	}	
	
}
