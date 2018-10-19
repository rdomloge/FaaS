package com.example.faas.sqlite.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;

import com.example.faas.sqlite.FileHelper;
import com.example.faas.sqlite.model.Function;
import com.example.faas.sqlite.model.Functions;
import com.example.faas.sqlite.model.Lib;
import com.example.faas.sqlite.model.Property;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
@PropertySource("classpath:application.properties")
public class DbInitInsertFunctions {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DbInitInsertFunctions.class);

	@Autowired
	private DataSource dataSource;

	@Value("${functions.xml.file}")
	private String xmlFilename;

	@PostConstruct
	@DependsOn("DbInitConfig")
	public void initialize() throws FileNotFoundException, IOException {
		try (Connection connection = dataSource.getConnection()) {

			Functions functions = parseFunctionsXml();

			int functionId = 1;
			int propertyId = 1;
			int libId = 1;
			for (Function function : functions.getFunctions()) {
				populateFunctionTable(connection, functionId, function.getName(), function.getClassname(),
						function.getFilepath(), function.getFilename());
				for (Property property : function.getConfig()) {
					populateConfigTable(connection, propertyId++, property.getKey(), property.getValue(), functionId);
				}
				for (Lib lib : function.getLibs()) {
					populateResourceTable(connection, libId++, lib.getFilepath(), lib.getFilename(), functionId);
				}
				functionId++;
			}
		} catch (SQLException e) {
			LOGGER.error("Could not load data", e);
		}
	}

	private Functions parseFunctionsXml() throws FileNotFoundException, IOException {
		System.out.println("Parsing XML functions file: " + xmlFilename);

		// TODO Find on classpath
		File file = new File(xmlFilename);

		XmlMapper xmlMapper = new XmlMapper();
		String xml = FileHelper.inputStreamToString(new FileInputStream(file));
		Functions functions = xmlMapper.readValue(xml, Functions.class);

		return functions;
	}

	private void populateConfigTable(Connection connection, int id, String key, String value, int functionId)
			throws SQLException {

		try (Statement statement = connection.createStatement()) {
			String sql = "INSERT INTO config (id, key, value, function_id) VALUES (%d, '%s', '%s', %d)";
			statement.executeUpdate(String.format(sql, id, key, value, functionId));
		}

	}

	private void uploadFileToDatabase(Connection connection, String updateSql, int id, String filepath, String filename)
			throws IOException, SQLException {
		byte[] blob;
		blob = FileHelper.readFile(filepath + filename);
		try (PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
			pstmt.setBytes(1, blob);
			pstmt.setInt(2, id);

			pstmt.executeUpdate();
		}
	}

	private void populateFunctionTable(Connection connection, int id, String functionName, String classname,
			String filepath, String filename) throws SQLException {
		try (Statement statement = connection.createStatement()) {
			String sql = "INSERT INTO function (id, name, classname, filepath, filename) VALUES (%d, '%s', '%s', '%s', '%s');";
			statement.executeUpdate(String.format(sql, id, functionName, classname, filename, filepath));

			String updateSql = "UPDATE function SET file = ? WHERE id = ?";
			uploadFileToDatabase(connection, updateSql, id, filepath, filename);
		} catch (IOException e) {
			System.err.printf("ERROR: %s !!!!!!!!!!!!!\n", e.getMessage(), filepath + filename);
		}
	}

	private void populateResourceTable(Connection connection, int id, String filepath, String filename, int functionId)
			throws SQLException {

		try (Statement statement = connection.createStatement()) {
			String sql = "INSERT INTO resource (id, filepath, filename, function_id) VALUES (%d, '%s', '%s', %d)";
			statement.executeUpdate(String.format(sql, id, filepath, filename, functionId));

			String updateSql = "UPDATE resource SET file = ? WHERE id = ?";
			uploadFileToDatabase(connection, updateSql, id, filepath, filename);
		} catch (IOException e) {
			System.err.printf("ERROR: %s !!!!!!!!!!!!!\n", e.getMessage(), filepath + filename);
		}

	}


}
