package com.sdmproject.beans;

import java.util.HashMap;
import java.util.Map;

public class FilterBean {
	private Map<String, String> map;

	private static FilterBean instance;

	// private constructor to prevent instantiation
	private FilterBean() {
		System.out.println("intialize");
	}

	public static FilterBean getInstance() {
		// only one instance will be created
		if (instance == null)
			instance = new FilterBean();
		return instance;
	}

	public Map<String, String> getMap() {
		return map;
	}
}
