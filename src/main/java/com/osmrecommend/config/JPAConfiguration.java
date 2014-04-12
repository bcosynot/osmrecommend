package com.osmrecommend.config;

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
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate3.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.osmrecommend.dao.AreaDAO;
import com.osmrecommend.dao.NodeDAO;
import com.osmrecommend.data.event.dao.EditDAO;


@EnableJpaRepositories(basePackages = "com.osmrecommend.persistence.repositories")
@ComponentScan(basePackages = "com.osmrecommend")
public class JPAConfiguration {
	
	@Bean
	public DriverManagerDataSource dataSource() throws SQLException {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		return dataSource;
	}
	
	@Bean
	public EntityManagerFactory entityManagerFactory() throws SQLException {
		
		CompositeConfiguration config = new CompositeConfiguration();
		try {
			config.addConfiguration(new PropertiesConfiguration("app.properties"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		DBConnectionConfig dbConnectionConfig = new DBConnectionConfig();
		
		Map<String, String> dbConnectionProperties = new HashMap<String, String>();
		dbConnectionProperties.put("hibernate.default_schema", dbConnectionConfig.getSchema());
		dbConnectionProperties.put("hibernate.dialect", "org.hibernate.spatial.dialect.postgis.PostgisDialect");
		
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    vendorAdapter.setGenerateDdl(true);
	    vendorAdapter.setDatabase(Database.POSTGRESQL);

	    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
	    factory.setJpaVendorAdapter(vendorAdapter);
	    factory.setPackagesToScan("com.osmrecommend.persistence");
	    DriverManagerDataSource ds = dataSource();
	    ds.setDriverClassName(dbConnectionConfig.getDriverClass());
	    ds.setUsername(dbConnectionConfig.getUser());
	    ds.setUrl(dbConnectionConfig.getConnectionURL());
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
	public PlatformTransactionManager transactionManager() throws SQLException {

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
	public AreaDAO areaDAO() {
		return new AreaDAO();
	}
	
	
}
