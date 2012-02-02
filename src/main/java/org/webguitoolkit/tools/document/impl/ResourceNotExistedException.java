package org.webguitoolkit.tools.document.impl;

public class ResourceNotExistedException extends DocumentRepositoryException {
	private static final long serialVersionUID = 1L;
	private String errorCode;

	public ResourceNotExistedException(String message) {
		super(message);
	}

	public ResourceNotExistedException(Exception e) {
		super(e);
	}

	public ResourceNotExistedException(Exception e, String errorCode) {
		super(e);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
