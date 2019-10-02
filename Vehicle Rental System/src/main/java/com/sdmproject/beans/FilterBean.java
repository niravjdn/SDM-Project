package com.sdmproject.beans;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class FilterBean {
	public Map<String, String> map ;
	public String s = "s";
	public FilterBean() {
		System.out.println("intialize");
		map = new HashMap<String, String>();
	}
	
	public Map<String, String> getMap() {
		return map;
	}
}
