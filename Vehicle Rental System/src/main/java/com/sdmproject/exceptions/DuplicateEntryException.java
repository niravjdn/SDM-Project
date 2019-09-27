package com.sdmproject.exceptions;

public class DuplicateEntryException extends Exception {
	
	public DuplicateEntryException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
	
}
