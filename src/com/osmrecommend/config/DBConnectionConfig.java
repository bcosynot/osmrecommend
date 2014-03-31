package com.osmrecommend.config;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class DBConnectionConfig {

	private String host;
	
	private String database;
	
	private String user;
	
	private String password;
	
	private String schema;
	
	private String driverClassName;
	
	private String databaseType;
	
	private String port;
	
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
			config.addConfiguration(new PropertiesConfiguration("db.properties"));
			host = config.getString("postgresHost");
			database = config.getString("postgresDB");
			user = config.getString("postgresUser");
			password = config.getString("postgresPassword");
			schema = config.getString("defaultSchema");
			driverClassName = config.getString("driverClass");
			databaseType = config.getString("databaseType");
			port = config.getString("port");
		} catch (ConfigurationException e) {
			e.printStackTrace();
			setDefaults();
		}
		
	}

	private void setDefaults() {
		
		host = "localhost";
		database = "osm";
		user = "osm";
		password = "";
		schema = "public";
		driverClassName = "org.postgresql.Driver";
		databaseType = "postgresql";
		port = "3306";
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
		this.host = host;
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
		this.database = database;
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
		this.user = user;
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
		this.password = password;
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
		this.schema = schema;
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
		this.databaseType = databaseType;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	
	
}
