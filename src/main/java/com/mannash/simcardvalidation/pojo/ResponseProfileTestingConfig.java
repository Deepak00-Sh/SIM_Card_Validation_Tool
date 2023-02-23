package com.mannash.simcardvalidation.pojo;

import java.util.List;

public class ResponseProfileTestingConfig {
	
	List<String> fileSystemConfig;
	List<String> fileContentConfig;
	public List<String> getFileSystemConfig() {
		return fileSystemConfig;
	}
	public void setFileSystemConfig(List<String> fileSystemConfig) {
		this.fileSystemConfig = fileSystemConfig;
	}
	public List<String> getFileContentConfig() {
		return fileContentConfig;
	}
	public void setFileContentConfig(List<String> fileContentConfig) {
		this.fileContentConfig = fileContentConfig;
	}
}
