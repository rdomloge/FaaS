package com.example.faas.reactor.fnstore;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.faas.common.FunctionDefinition;
import com.example.faas.common.LibResource;
import com.example.faas.dto.JobRequest;
import com.example.faas.ex.FunctionPreparationException;
import com.sqlite.model.Function;
import com.sqlite.repository.FunctionRepository;

@Service
public class DatabaseDefinitionPersistence implements DefinitionPersistence {

	@Autowired
	private FunctionRepository functionRepo;
	
	public FunctionDefinition load(JobRequest request) throws FunctionPreparationException {
		
		FunctionDefinition def = null;
		LibResource[] libs = null;
		
		try {
			Function function = functionRepo.findByFunctionName(request.getFunctionName());
			
			libs = new LibResource[function.getLibs().size()];
			for (int i = 0; i < function.getLibs().size(); i++) {
				LibResource lib = new DatabaseBackedLibResource(
						function.getLibs().get(i).getFilepath(),
						function.getLibs().get(i).getFilename(),
						function.getLibs().get(i).getFile());
				libs[i] = lib;
			}
			
			def = new FunctionDefinition(function.getName(), new String(function.getFile()), function.getClassname(), "", libs);
		} catch (SQLException e) {
			throw new FunctionPreparationException(e);
		}

		return def;
	}

}