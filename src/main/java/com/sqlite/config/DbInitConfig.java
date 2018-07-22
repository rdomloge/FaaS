package com.sqlite.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbInitConfig implements InitializingBean {

	@Autowired
	private DataSource dataSource;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();

			createFunctionTable(statement);
			createResourceTable(statement);
			
			statement.close();
			connection.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createFunctionTable(Statement statement) throws SQLException {
		statement.execute("DROP TABLE IF EXISTS function;");
		statement.executeUpdate("CREATE TABLE function(" +
				"id INTEGER Primary key, " +
				"name VARCHAR(255) not null, " +
				"classname VARCHAR(255) not null, " +
				"filepath VARCHAR(255) not null, " +
				"filename VARCHAR(255) not null, " +
				"file BLOB null);");
	}

	private void createResourceTable(Statement statement) throws SQLException {
		statement.execute("DROP TABLE IF EXISTS resource;");
		statement.executeUpdate("CREATE TABLE resource(" +
				"id INTEGER Primary key, " +
				"filepath VARCHAR(255) not null, " +
				"filename VARCHAR(255) not null, " +
				"function_id INTEGER not null, " +
				"file BLOB null);");
	}

}