package org.webguitoolkit.tools.document.impl;

public class ResourceAlreadyExistException extends DocumentRepositoryException {
	private static final long serialVersionUID = 1L;
	private String errorCode;

	public ResourceAlreadyExistException(String message) {
		super(message);
	}
	
	public ResourceAlreadyExistException(Exception e) {
		super(e);
	}

	public ResourceAlreadyExistException(Exception e, String errorCode) {
		super(e);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
