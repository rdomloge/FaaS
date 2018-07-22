package com.sqlite.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sqlite.model.Function;
import com.sqlite.model.Lib;

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
					function = new Function();
					function.setId(functionRs.getInt("id"));
					function.setName(functionRs.getString("name"));
					function.setClassname(functionRs.getString("classname"));
					function.setFilepath(functionRs.getString("filepath"));
					function.setFilename(functionRs.getString("filename"));
					function.setFile(functionRs.getBytes("file"));
				
					ResultSet libRs = statement.executeQuery("SELECT * FROM resource WHERE function_id = " + functionRs.getInt("id"));
					while (libRs.next()) {
						Lib lib = new Lib();
						lib.setFilepath(libRs.getString("filepath"));
						lib.setFilename(libRs.getString("filename"));
						lib.setFile(libRs.getBytes("file"));
						function.addLib(lib);
					}
				}
				else {
					throw new SQLException("Function not found: " + functionName);
				}
			}
		}
		return function;
	}
}
