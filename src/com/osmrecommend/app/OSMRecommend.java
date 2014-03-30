package com.osmrecommend.app;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openstreetmap.osmosis.core.database.DatabaseLoginCredentials;
import org.openstreetmap.osmosis.core.database.DatabaseType;
import org.openstreetmap.osmosis.pgsimple.common.DatabaseContext;
import org.openstreetmap.osmosis.pgsimple.v0_6.impl.ActionDao;
import org.openstreetmap.osmosis.pgsimple.v0_6.impl.NodeDao;
import org.openstreetmap.osmosis.pgsimple.v0_6.impl.NodeReader;
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
		 
		
		DatabaseLoginCredentials databaseLoginCredentials = 
			new DatabaseLoginCredentials(host, database, user, password, false, false, DatabaseType.POSTGRESQL);
		
		DatabaseContext databaseContext = new DatabaseContext(databaseLoginCredentials);
		System.out.println("reading..");
		/*NodeReader reader = new NodeReader(databaseContext);
		int i=0;
		while(reader.hasNext()) {
			if(i>10) break;
			System.out.println(reader.next().getId());
		}*/
		/*NodeDao nodeDao = new NodeDao(databaseContext, new ActionDao(databaseContext));
		System.out.println(nodeDao.getEntity(999646490).getId());*/
		
		/*Lenkskit*/
		
	}	
	
}
