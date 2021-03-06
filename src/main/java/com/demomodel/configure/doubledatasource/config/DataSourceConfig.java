package com.demomodel.configure.doubledatasource.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
//JdbcTemplate多数据源配置
//@Configuration
public class DataSourceConfig {
	@Bean(name="dataSource")
	@Primary  //主数据源
	//@ConfigurationProperties(prefix="spring.datasource.primary")
	public DataSource dataSource(@Qualifier("dataSourceProperties") DataSourceProperties dataSourceProperties){
		//return DataSourceBuilder.create().build();
		return dataSourceProperties().initializeDataSourceBuilder().build();
	}
	
	@Primary
	@Bean(name="dataSourceProperties")
	@ConfigurationProperties(prefix="spring.datasource.primary")
	public DataSourceProperties dataSourceProperties(){
		return new DataSourceProperties();
	}
	
	
	@Bean(name="otherDataSource")
	//@ConfigurationProperties(prefix="spring.datasource.secondary")
	public DataSource otherDataSource(@Qualifier("otherDataSourceProperties") DataSourceProperties dataSourceProperties){
		//return DataSourceBuilder.create().build();
		return otherDataSourceProperties().initializeDataSourceBuilder().build();
	}
	
	
	@Bean(name="otherDataSourceProperties")
	@ConfigurationProperties(prefix="spring.datasource.secondary")
	public DataSourceProperties otherDataSourceProperties(){
		return new DataSourceProperties();
	}
	
}
//https://blog.csdn.net/feinifi/java/article/details/96750566