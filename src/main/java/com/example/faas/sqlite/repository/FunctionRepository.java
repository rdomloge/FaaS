package com.example.faas.sqlite.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.faas.sqlite.model.Function;
import com.example.faas.sqlite.model.Lib;
import com.example.faas.sqlite.model.Property;

@Repository
public class FunctionRepository {

	@Autowired
	private DataSource dataSource;

	public Function findByFunctionName(String functionName) throws SQLException {
		Function function = null;
		try (Connection connection = dataSource.getConnection()) {

			try (Statement statement = connection.createStatement()) {

				ResultSet functionRs = statement.executeQuery("SELECT * FROM function WHERE name = '" + functionName + "'");
				if (functionRs.next()) {
					int functionId = functionRs.getInt("id");
					function = new Function();
					function.setId(functionId);
					function.setName(functionRs.getString("name"));
					function.setClassname(functionRs.getString("classname"));
					function.setFilepath(functionRs.getString("filepath"));
					function.setFilename(functionRs.getString("filename"));
					function.setFile(functionRs.getBytes("file"));
				
					ResultSet libRs = statement.executeQuery("SELECT * FROM resource WHERE function_id = " + functionId);
					while (libRs.next()) {
						Lib lib = new Lib();
						lib.setFilepath(libRs.getString("filepath"));
						lib.setFilename(libRs.getString("filename"));
						lib.setFile(libRs.getBytes("file"));
						function.addLib(lib);
					}
					
					ResultSet configRs = statement.executeQuery("SELECT * FROM config WHERE function_id = " + functionId);
					List<Property> properties = new LinkedList<>();
					while(configRs.next()) {
						Property property = new Property();
						property.setKey(configRs.getString("key"));
						property.setValue(configRs.getString("value"));
						properties.add(property);
					}
					
					function.setConfig(properties);
				}
				else {
					throw new SQLException("Function not found: " + functionName);
				}
			}
		}
		return function;
	}
}
