package com.cole2sworld.ColeBans.framework;

public class BadAPIKeyException extends Exception {
	private static final long serialVersionUID = 8392525539750353813L;
	private String detail = null;
	public BadAPIKeyException(String detail) {
		this.detail = detail;
	}
	public String getMessage() {
		return detail;
	}
}
