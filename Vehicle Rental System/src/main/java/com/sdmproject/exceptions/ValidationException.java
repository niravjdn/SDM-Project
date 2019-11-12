package com.sdmproject.exceptions;

public class ValidationException extends Exception {
	public ValidationException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
}
