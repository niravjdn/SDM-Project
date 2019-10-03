package com.sdmproject.beans;

import java.util.HashMap;
import java.util.Map;

public class FilterBean {
	private static Map<String, String> map ;
	
	//private constructor to prevent instantiation
	private FilterBean() {
		System.out.println("intialize");
	}
	
	public static Map<String, String> getMap() {
		//only one instance will be created
		if(map == null)
			map = new HashMap<String, String>();
		return map;
	}
}
