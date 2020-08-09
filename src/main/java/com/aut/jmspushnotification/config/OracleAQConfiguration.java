package com.aut.jmspushnotification.config;

import java.sql.SQLException;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jms.core.JmsTemplate;

import oracle.jms.AQjmsFactory;

@Configuration
public class OracleAQConfiguration {
	
	 @Value("${spring.datasource.url}")
	    private String dburl;

	    @Value("${spring.datasource.username}")
	    private String username;

	    @Value("${spring.datasource.password}")
	    private String password;

	    @Value("${spring.datasource.driver-class-name}")
	    private String driverName;
	    
	    
	    @Bean
	    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
	        DataSourceTransactionManager manager = new DataSourceTransactionManager();
	        manager.setDataSource(dataSource);
	        return manager;
	    }
	    
	    @Bean
	    public ConnectionFactory connectionFactory(DataSource dataSource) throws JMSException, SQLException {
	        Properties info = new Properties();
	        info.put("driver-name", driverName);
	        info.put("user", username);
	        info.put("password", password);
	       // return AQjmsFactory.getQueueConnectionFactory(dataSource.unwrap(OracleDataSource.class));
	        return AQjmsFactory.getQueueConnectionFactory(dburl, info);
	    }
	    
	    @Bean
	    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
	        JmsTemplate jmsTemplate = new JmsTemplate();
	        jmsTemplate.setSessionTransacted(true);
	        jmsTemplate.setConnectionFactory(connectionFactory);
	        return jmsTemplate;
	    }

}
