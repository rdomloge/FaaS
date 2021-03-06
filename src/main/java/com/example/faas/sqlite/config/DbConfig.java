package com.example.faas.sqlite.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfig {

	@Bean
	public DataSource dataSource() {
	        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//	        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
//	        dataSourceBuilder.url("jdbc:sqlite:faas.db");
	        dataSourceBuilder.driverClassName("org.postgresql.Driver");
	        dataSourceBuilder.url("jdbc:postgresql://postgrescontainer:5432/faas");
	        dataSourceBuilder.password("password");
	        dataSourceBuilder.username("faas");
	        return dataSourceBuilder.build();   
	}

}