package com.osmrecommend.config;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate3.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.osmrecommend.dao.AreaDAO;
import com.osmrecommend.dao.NodeDAO;
import com.osmrecommend.dao.WayDAO;
import com.osmrecommend.data.event.dao.EditDAO;


@EnableJpaRepositories(basePackages = "com.osmrecommend.persistence.repositories")
@ComponentScan(basePackages = "com.osmrecommend")
public class JPAConfiguration {
	
	@Bean
	public ComboPooledDataSource dataSource() throws SQLException {
		
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		
		return dataSource;
	}
	
	@Bean
	public EntityManagerFactory entityManagerFactory() throws SQLException, PropertyVetoException {
		
		CompositeConfiguration config = new CompositeConfiguration();
		try {
			config.addConfiguration(new PropertiesConfiguration("app.properties"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		DBConnectionConfig dbConnectionConfig = new DBConnectionConfig();
		
		Map<String, Object> dbConnectionProperties = new HashMap<String, Object>();
		dbConnectionProperties.put("hibernate.default_schema", dbConnectionConfig.getSchema());
		dbConnectionProperties.put("hibernate.dialect", "org.hibernate.spatial.dialect.postgis.PostgisDialect");
		dbConnectionProperties.put("hibernate.c3p0.acquire_increment", 1);
		dbConnectionProperties.put("hibernate.c3p0.min_size", 5);
		dbConnectionProperties.put("hibernate.c3p0.max_size", 20);
		dbConnectionProperties.put("hibernate.c3p0.timeout", 100);
		dbConnectionProperties.put("hibernate.c3p0.max_statements", 30);
		dbConnectionProperties.put("hibernate.c3p0.idle_test_period", 300);
		dbConnectionProperties.put("acquireIncrement", 1);
		dbConnectionProperties.put("minPoolSize", 5);
		dbConnectionProperties.put("maxPoolSize", 3);
		dbConnectionProperties.put("timeout", 100);
		dbConnectionProperties.put("maxStatements", 30);
		dbConnectionProperties.put("maxIdleTime", 300);
		
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    vendorAdapter.setGenerateDdl(true);
	    vendorAdapter.setDatabase(Database.POSTGRESQL);

	    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
	    factory.setJpaVendorAdapter(vendorAdapter);
	    factory.setPackagesToScan("com.osmrecommend.persistence");
	    ComboPooledDataSource ds = dataSource();
	    ds.setDriverClass(dbConnectionConfig.getDriverClass());
	    ds.setUser(dbConnectionConfig.getUser());
	    ds.setJdbcUrl(dbConnectionConfig.getConnectionURL());
	    ds.setPassword(dbConnectionConfig.getPassword());
	    factory.setDataSource(ds);
	    factory.setJpaPropertyMap(dbConnectionProperties);
	    factory.afterPropertiesSet();
	    
	    
	    return factory.getObject();
		
	}
	
	@Bean
	public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
		return entityManagerFactory.createEntityManager();
	}

	@Bean
	public PlatformTransactionManager transactionManager() throws SQLException, PropertyVetoException {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		return txManager;
	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}
	
	@Bean
	public EditDAO editDAO() {
		return new EditDAO();
	}
	
	@Bean
	public NodeDAO nodeDAO() {
		return new NodeDAO();
	}
	
	@Bean
	public WayDAO wayDAO() {
		return new WayDAO();
	}
	
	@Bean
	public AreaDAO areaDAO() {
		return new AreaDAO();
	}
	
	
}
