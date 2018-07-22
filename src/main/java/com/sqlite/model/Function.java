package com.sqlite.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Function {

	@JsonIgnore
	private byte[] file;

	@JsonIgnore
	private int id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("classname")
	private String classname;

	@JsonProperty("filepath")
	private String filepath;

	@JsonProperty("filename")
	private String filename;

	@JsonProperty("libs")
	private List<Lib> libs;

	public Function() {
		this.libs = new ArrayList<>();
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public void addLib(Lib lib) {
		libs.add(lib);
	}

	public List<Lib> getLibs() {
		return libs;
	}

	public void setLibs(List<Lib> libs) {
		this.libs = libs;
	}
}
