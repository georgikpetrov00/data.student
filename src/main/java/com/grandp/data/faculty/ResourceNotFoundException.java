package com.grandp.data.faculty;

public class ResourceNotFoundException extends RuntimeException{

	public ResourceNotFoundException() {
		
	}
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
	
	public ResourceNotFoundException(String message, Throwable t) {
		super(message, t);
	}
	
}
