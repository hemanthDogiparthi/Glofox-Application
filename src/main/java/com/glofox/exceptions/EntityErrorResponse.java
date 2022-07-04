package com.glofox.exceptions;

import java.util.List;

public class EntityErrorResponse {
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
	
	private List<String> errorDetailsList ;

	public List<String> getErrorDetailsList() {
		return errorDetailsList;
	}

	public void setErrorDetailsList(List<String> errorDetailsList) {
		this.errorDetailsList = errorDetailsList;
	}

	public EntityErrorResponse(String errorMessage, List<String> errorDetailsList) {
		super();
		this.errorMessage = errorMessage;
		this.errorDetailsList = errorDetailsList;
	}
}
