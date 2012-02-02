package org.webguitoolkit.tools.document.impl;

public class AccessDeniedException extends DocumentRepositoryException {
	private static final long serialVersionUID = 1L;
	private String errorCode;

	public AccessDeniedException(String message) {
		super(message);
	}

	public AccessDeniedException(Exception e) {
		super(e);
	}

	public AccessDeniedException(Exception e, String errorCode) {
		super(e);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
