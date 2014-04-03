package com.osmrecommend.config;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class DBConnectionConfig {

	private String host;
	
	private static String DEFAULT_HOST = "localhost";
	
	private String database;
	
	private static String DEFAULT_DATABASE = "osm";
	
	private String user;
	
	private static String DEFAULT_USER = "osm";
	
	private String password;
	
	private static String DEFAULT_PASSWORD = "osm";
	
	private String schema;
	
	private static String DEFAULT_SCHEMA = "public";
	
	private String driverClassName;
	
	private static String DEFAULT_DRIVER_CLASS_NAME = "org.postgresql.Driver";
	
	private String databaseType;
	
	private static String DEFAULT_DATABASE_TYPE = "postgresql";
	
	private String port;
	
	private static String DEFAULT_PORT = "5432";
	
	/**
	 * @param host
	 * @param database
	 * @param user
	 * @param password
	 * @param schema
	 */
	public DBConnectionConfig(String host, String port, String database,
			String user, String password, String schema,
			String driverClassName,	String databaseType) {
		super();
		this.host = host;
		this.database = database;
		this.user = user;
		this.password = password;
		this.schema = schema;
		this.driverClassName = driverClassName;
		this.databaseType = databaseType;
		this.port = port;
	}
	
	public DBConnectionConfig() {
		
		setDefaults();
		CompositeConfiguration config = new CompositeConfiguration();
		try {
			config.addConfiguration(new PropertiesConfiguration("app.properties"));
			if(null != config.getString("postgresHost")) setHost(config.getString("postgresHost"));
			if(null != config.getString("port")) setPort(config.getString("port"));
			if(null != config.getString("postgresDB")) setDatabase(config.getString("postgresDB"));
			if(null != config.getString("postgresUser")) setUser(config.getString("postgresUser"));
			if(null != config.getString("postgresPassword")) setPassword(config.getString("postgresPassword"));
			if(null != config.getString("defaultSchema")) setSchema(config.getString("defaultSchema"));
			if(null != config.getString("driverClass")) setDriverClass(config.getString("driverClass"));
			if(null != config.getString("databaseType")) setDatabaseType(config.getString("databaseType"));							
		} catch (ConfigurationException e) {
			e.printStackTrace();
			setDefaults();
		}
		
	}

	private void setDefaults() {
		
		host = DEFAULT_HOST;
		database = DEFAULT_DATABASE;
		user = DEFAULT_USER;
		password = DEFAULT_PASSWORD;
		schema = DEFAULT_SCHEMA;
		driverClassName = DEFAULT_DRIVER_CLASS_NAME;
		databaseType = DEFAULT_DATABASE_TYPE;
		port = DEFAULT_PORT;
	}
	
	public String getConnectionURL() {
		
		StringBuilder connectionURL = new StringBuilder("jdbc:");
		
		// Add database type
		connectionURL.append(databaseType);
		// Add host and port
		connectionURL.append("://");
		connectionURL.append(host);
		connectionURL.append(":");
		connectionURL.append(port);
		// Add database
		connectionURL.append("/");
		connectionURL.append(database);
		/*// Add user
		connectionURL.append(";user=");
		connectionURL.append(user);
		// Add password
		connectionURL.append(";password=");
		connectionURL.append(password);		*/
		
		return connectionURL.toString();
		
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		
		if(host.length() > 0) {
			this.host =host ;
		} else {
			this.host = DEFAULT_HOST;
		}
		
	}

	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @param database the database to set
	 */
	public void setDatabase(String database) {

		if(database.length() > 0) {
			this.database = database;
		} else {
			this.database = DEFAULT_DATABASE;
		}
		
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {

		if(user.length() > 0) {
			this.user = user;
		} else {
			this.user = DEFAULT_USER;
		}
		
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {

		if(password.length() > 0) {
			this.password = password;
		} else {
			this.password = DEFAULT_PASSWORD;
		}
		
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @param schema the schema to set
	 */
	public void setSchema(String schema) {

		if(schema.length() > 0) {
			this.schema = schema;
		} else {
			this.schema = DEFAULT_SCHEMA;
		}
		
	}

	public String getDriverClass() {
		return driverClassName;
	}

	public void setDriverClass(String driverClass) {
		this.driverClassName = driverClass;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		
		if(databaseType.length() > 0) {
			this.databaseType = databaseType;
		} else {
			this.databaseType = DEFAULT_DATABASE_TYPE;
		}
		
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		
		if(port.length() > 0) {
			this.port = port;
		} else {
			this.port = DEFAULT_PORT;
		}
		
	}
	
	
	
}
