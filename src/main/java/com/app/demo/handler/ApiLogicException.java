package com.app.demo.handler;

import lombok.Data;

@Data
public class ApiLogicException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private int code;

	public ApiLogicException(String message) {
		super(message);
	}

	public ApiLogicException(int code, String message) {
		super(message);
		this.code = code;
	}

}
