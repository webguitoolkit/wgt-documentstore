package org.webguitoolkit.tools.document.impl;

public class LockExistedException extends DocumentRepositoryException {
	private static final long serialVersionUID = 1L;
	private String errorCode;

	public LockExistedException(String message) {
		super(message);
	}

	public LockExistedException(Exception e) {
		super(e);
	}

	public LockExistedException(Exception e, String errorCode) {
		super(e);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
