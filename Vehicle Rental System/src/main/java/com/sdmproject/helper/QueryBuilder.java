package com.sdmproject.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QueryBuilder {
	private HashMap<String, String> filterParam = new HashMap<String, String>();

	private String tableName;
	
	private String fields = "*";

	private String orderByString = "";

	public void add(String param, String paramValue) {
		filterParam.put(param, paramValue);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void join(String table2) {
		tableName += " LEFT OUTER JOIN " + table2;
	}

	public void setOrderBy(String columnname, String order) {
		orderByString = " order by " + columnname + " " + order;
	}

	public void setFields(String field) {
		this.fields = field;
	}
	
	public String getSQLQuery() {
		String query = "Select "+fields+" from " + tableName;
		List<String> paramList = new ArrayList<String>();
		if (filterParam.size() > 0) {
			Iterator it = filterParam.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				paramList.add(pair.getKey() + "" + pair.getValue() + " ? ");
				it.remove();
			}

			String whereClause = " where " + String.join(" and ", paramList);
			query += whereClause;
		}

		query += orderByString;
		return query;
	}
}
