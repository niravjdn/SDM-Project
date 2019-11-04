package com.sdmproject.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class AddToListForFilter {

	public static void add(ArrayList<String> param, ArrayList<String> operator, ArrayList<Object> values, 
			String column,
			String op, Object val) {
		param.add(column);
		operator.add(op);
		values.add(val);
	}
	
	public static void fillListUsingMap(ArrayList<String> param, ArrayList<String> operator, ArrayList<Object> values,
			Map.Entry<String, String> entry) {
		if(entry.getKey().equals("year")) {
			param.add(entry.getKey());
			operator.add("=");
			Calendar prevYear = Calendar.getInstance();
			prevYear.add(Calendar.YEAR, - Integer.parseInt(entry.getValue()));
			int lessThanYear = prevYear.get(Calendar.YEAR);
			values.add(lessThanYear);
		}else {
			param.add(entry.getKey());
			operator.add("=");
			values.add(entry.getValue());
		}
	}
	
}
