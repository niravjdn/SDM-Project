package com.sdmproject.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Vehicle;
import com.sdmproject.ormwrapper.MySQL;

public class Table<ObjectType, KeyType> {

	private SQL sql;
	private String prefix;
	private String tableName;
	private List<String> fieldNames;
	private Map<String, Field> fieldReflection = new HashMap<>();
	private Map<Field, DatabaseField> fieldDescriptors = new HashMap<>();
	private String keyField;
	private Class<ObjectType> objectClass;

	public ObjectType queryById(KeyType id) {
		QueryResult result = builder().where().eq(getColName(keyField), id).query();
		return result.first();
	}

	public List<ObjectType> queryForEq(String colName, Object value) {
		QueryResult result = builder().where().eq(colName, value).query();
		return result.all();
	}

	public List<ObjectType> queryForAll() {
		QueryResult result = builder().query();
		return result.all();
	}
	
	public List<ObjectType> queryForAllWithSort(String param, String order) {
		QueryResult result = builder().order(param, order.equals("desc")).query();
		return result.all();
	}
	
	public List<Integer> queryForIdWithSort(String property, String sortOrder) {
		QueryBuilder builder = builder();
		return builder.select("id").order(property, sortOrder.equals("desc")).queryForIDWithDefaultSortByID();
	}
	
	public List<ObjectType> queryForAllSelectedColumn(String[] columns) {
		QueryBuilder builder = builder();
		for(String col : columns) {
			builder.select(col);
		}
		QueryResult result = builder().queryForSelectedField();
		return result.all();
	}

	public int queryForTotalCount() {
		return builder().count();
	}

	
	public List<ObjectType> queryForCustomBuilder(QueryBuilder builder) {
		QueryResult result = builder.query();
		return result.all();
	}

	public List<ObjectType> queryForParamsEquality(String[] param, Object[] value) {
		QueryBuilder builder = builder();

		builder.where().eq(param[0], value[0]);

		for (int i = 1; i < param.length; i++) {
			builder.and().eq(param[i], value[i]);
		}

		QueryResult result = builder.query();
		return result.all();
	}

	public int countForParam(String[] param, Object[] value) {
		return builder().where().eq(param[0],value[0]).count();
	}
	
	public List<ObjectType> queryForParamsForDifferentOperations(String[] param, String[] ops, Object[] value){
    	QueryBuilder builder =  builder();
    	
    	generateBuilder(param, ops, value, builder);
    	
        QueryResult result = builder.query();
        return result.all();
    }

	private void generateBuilder(String[] param, String[] ops, Object[] value, QueryBuilder builder) {
		if(param.length < 1) {
			return;
		}
		
		switch (ops[0]) {
		case ">":
			builder.where().greaterThan(param[0],value[0]);
			break;

		case "=":
			builder.where().eq(param[0],value[0]);	
			break;
		case "<":
			builder.where().lessThan(param[0],value[0]);
			break;
		}

    	for(int i=1; i<param.length; i++) {
    		switch (ops[i]) {
    		case ">":
    			builder.and().greaterThan(param[i],value[i]);
    			break;

    		case "=":
    			builder.and().eq(param[i],value[i]);	
    			break;
    		case "<":
    			builder.and().lessThan(param[i],value[i]);
    			break;
    		}
    	}
	}
	
	public List<ObjectType> queryForParamsForDifferentOperationsWithSort(String[] param, 
			String[] ops, Object[] value, String sortProperty, boolean isDesc){
    	QueryBuilder builder =  builder();
    	
    	generateBuilder(param, ops, value, builder);
    	
    	if(sortProperty.equals("")) {
    		builder.order(keyField, false);
    	}else {
    		builder.order(sortProperty, isDesc);
    	}
    	
        QueryResult result = builder.query();
        return result.all();
    }
	
	public List<Integer> queryIDParamsForDifferentOperationsWithSort(String[] param, 
			String[] ops, Object[] value, String sortProperty, boolean isDesc){
    	QueryBuilder builder =  builder();
    	
    	generateBuilder(param, ops, value, builder);
    	
    	if(sortProperty.equals("")) {
    		builder.order(keyField, false);
    	}else {
    		builder.order(sortProperty, isDesc);
    	}
    	
        return builder.queryForIDWithDefaultSortByID();
    }

	public List<ObjectType> queryForSQL(String query) {
		QueryResult result = builder().queryUsingSQLString(query);
		return result.all();
	}

	public ObjectType query(ObjectType object) {
		return builder().where().eq(getColName(keyField), getKeyValue(object)).query(object);
	}

	public QueryBuilder builder() {
		return new QueryBuilder(this);
	}

	public void create(ObjectType object) {
		builder().insert(object);
	}

	public void delete(ObjectType object) {
		deleteById(getKeyValue(object));
	}

	public void deleteById(KeyType id) {
		builder().where().eq(getColName(keyField), id).delete();
	}

	public void update(ObjectType object) {
		builder().where().eq(getColName(keyField), getKeyValue(object)).update(object);
	}

	public ObjectType reload(ObjectType object) {
		return queryById(getKeyValue(object));
	}

	public String getTableName() {
		return prefix + tableName;
	}

	public String getKeyColName() {
		return getColName(keyField);
	}

	public String getColName(String fieldName) {
		Field field = fieldReflection.get(fieldName);
		DatabaseField descriptor = fieldDescriptors.get(field);
		String colName = descriptor.columnName();
		if (colName.length() == 0)
			colName = fieldName;
		return colName;
	}

	private KeyType getKeyValue(ObjectType object) {
		try {
			return (KeyType) fieldReflection.get(keyField).get(object);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String convert(Class type, Object value, String dateFormat) {
		System.err.println(type);
		if (value == null)
			return null;
		if (type.isEnum())
			return ((Enum<?>) value).name();
		if (type.equals(boolean.class))
			return ((boolean) value) ? "1" : "0";
		if (type.equals(UUID.class))
			return value.toString();
		if(type.equals(Date.class)) {
			//parse according to date format
			//yyyy-MM-dd  hh:mm:ss
			 DateFormat df = new SimpleDateFormat(dateFormat);  
             System.out.println(value+" date here "+dateFormat);  
             return df.format(value);
			
		}
		return String.valueOf(value);
	}
	

	public SQL getConnection() {
		return sql;
	}

	public Table(Class<ObjectType> clazz) {
		this("", clazz);
	}

	public Table(String prefix, Class<ObjectType> clazz) {
		this.sql = new MySQL();
		this.prefix = prefix;
		this.objectClass = clazz;
		DatabaseTable tableInfo = objectClass.getDeclaredAnnotation(DatabaseTable.class);
		this.tableName = tableInfo.value();
		fieldNames = new ArrayList<>();
		for (Field field : objectClass.getDeclaredFields()) {
			DatabaseField[] annotations = field.getDeclaredAnnotationsByType(DatabaseField.class);
			if (annotations.length > 0) {
				field.setAccessible(true);
				fieldNames.add(field.getName());
				DatabaseField colInfo = annotations[0];
				fieldReflection.put(field.getName(), field);
				fieldDescriptors.put(field, colInfo);
				if (colInfo.id())
					keyField = field.getName();
			}
		}
	}

	public Table<ObjectType, KeyType> events() {
		return this;
	}

	public class QueryBuilder {

		private Table table;

		private StringBuilder querySelector = new StringBuilder();
		private List<Object> parameters = new ArrayList<>();
		private List<String> columns = new ArrayList<>();

		public QueryBuilder(Table table) {
			this.table = table;
		}

		
		public QueryBuilder where() {
			querySelector.append(" WHERE");
			return this;
		}


		public int quryForTotalCount() {
			return builder().count();
		}
		
		public QueryBuilder c(String custom, Object... params) {
			querySelector.append(custom);
			for (Object param : params) {
				parameters.add(sqlSafe(param));
			}
			return this;
		}

		public QueryBuilder eq(String columnName, Object value) {
			querySelector.append(" `" + columnName + "`=?");
			parameters.add(sqlSafe(value));
			return this;
		}

		public QueryBuilder select(String col) {
			columns.add(col);
			return this;
		}

		
		public QueryBuilder lessThan(String columnName, Object value) {
			querySelector.append(" `" + columnName + "`<?");
			parameters.add(sqlSafe(value));
			return this;
		}

		public QueryBuilder greaterThan(String columnName, Object value) {
			querySelector.append(" `" + columnName + "`>?");
			parameters.add(sqlSafe(value));
			return this;
		}

		public QueryBuilder like(String columnName, Object search) {
			querySelector.append(" `" + columnName + "` LIKE ?");
			parameters.add(sqlSafe(search));
			return this;
		}

		public QueryBuilder and() {
			querySelector.append(" AND");
			return this;
		}

		public QueryBuilder or() {
			querySelector.append(" OR");
			return this;
		}

		public QueryBuilder isNull(String columnName) {
			querySelector.append(" `" + columnName + "` IS NULL");
			return this;
		}

		public QueryBuilder notNull(String columnName) {
			querySelector.append(" `" + columnName + "` IS NOT NULL");
			return this;
		}

		public QueryBuilder limit(int amount) {
			querySelector.append(" LIMIT " + amount);
			return this;
		}

		public QueryBuilder order(String column, boolean desc) {
			querySelector.append(" ORDER BY `" + column + "`" + (desc ? " DESC" : ""));
			return this;
		}

		private Object sqlSafe(Object object) {
			if (object.getClass().equals(UUID.class)) {
				return object.toString();
			}
			return object;
		}

		public void delete() {
			sql.write("DELETE FROM `" + getTableName() + "`" + querySelector.toString() + ";", parameters.toArray());
		}

		public QueryResult query() {
			String query = "SELECT * FROM `" + getTableName() + "`" + querySelector.toString() + ";";
			System.out.println(query);
			ResultSet rs = sql.read(query, parameters.toArray());
			System.out.println(parameters);
			List<ObjectType> objects = parseResult(rs);
			sql.close(rs);
			return new QueryResult(objects);
		}
		
		
		public QueryResult queryForSelectedField() {
			String query = "SELECT "+ String.join(",", columns) +" FROM `" + getTableName() + "`" + querySelector.toString() + ";";
			System.out.println(query);
			ResultSet rs = sql.read(query, parameters.toArray());
			System.out.println(parameters);
			List<ObjectType> objects = parseResult(rs);
			sql.close(rs);
			return new QueryResult(objects);
		}
		
		public List<Integer> queryForIDWithDefaultSortByID() {
			String query = "SELECT id FROM `" + getTableName() + "`" + querySelector.toString() + ";";
			System.out.println(query);
			ResultSet rs = sql.read(query, parameters.toArray());
			List<Integer> list = new ArrayList<Integer>();
			try {
				while (rs.next())
					 list .add(rs.getInt(1));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sql.close(rs);
			return list;
		}

		
		public QueryResult queryUsingSQLString(String query) {
			ResultSet rs = sql.read(query, parameters.toArray());
			List<ObjectType> objects = parseResult(rs);
			sql.close(rs);
			return new QueryResult(objects);
		}

		public ObjectType query(ObjectType object) {
			ResultSet rs = sql.read("SELECT * FROM `" + getTableName() + "`" + querySelector.toString() + ";",
					parameters.toArray());
			try {
				if (rs.next())
					parseResult(rs, object);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sql.close(rs);
			return object;
		}

		public int count() {
			ResultSet rs = sql.read("SELECT COUNT(*) FROM `" + getTableName() + "`" + querySelector.toString() + ";",
					parameters.toArray());
			try {
				if (rs.next())
					return rs.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sql.close(rs);
			return -1;
		}

		public void update(ObjectType object) {
			Map<String, String> data = parseObject(object);
			String idColName = getColName(keyField);
			String updateData = "";
			List<Object> updateParameters = new ArrayList<>();
			for (String colName : data.keySet())
				if (!idColName.equals(colName)) {
					updateData += ",`" + colName + "`=?";
					updateParameters.add(data.get(colName));
				}
			if (updateData.length() > 0)
				updateData = updateData.substring(1);
			for (Object param : parameters)
				updateParameters.add(param);
			sql.write("UPDATE `" + getTableName() + "` SET " + updateData + querySelector.toString() + ";",
					updateParameters.toArray());
		}

		public void insert(ObjectType object) {
			Map<String, String> data = parseObject(object);
			String idColName = getColName(keyField);
			String insertData = "";
			String insertKeys = "";
			for (String colName : data.keySet()) {
				if (idColName.equals(colName) && fieldDescriptors.get(fieldReflection.get(keyField)).ai())
					continue;
				insertKeys += ",`" + colName + "`";
				insertData += ",?";
				parameters.add(data.get(colName));

			}
			if (insertData.length() > 0)
				insertData = insertData.substring(1);
			if (insertKeys.length() > 0)
				insertKeys = insertKeys.substring(1);
			String q = "INSERT INTO `" + getTableName() + "` (" + insertKeys + ") VALUES (" + insertData + ");";
			System.out.println(q);
			sql.write(q, parameters.toArray());
		}

		private Map<String, String> parseObject(ObjectType object) {
			Map<String, String> data = new HashMap<>();
			for (String fieldName : fieldReflection.keySet()) {
				Field field = fieldReflection.get(fieldName);
				
				try {
					String dateFormat = fieldDescriptors.get(field).dateFormat();
					System.out.println("01021020102"+dateFormat);
					String value = convert(field.getType(), field.get(object), dateFormat);
					String colName = getColName(fieldName);
					data.put(colName, value);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			return data;
		}

		private List<ObjectType> parseResult(ResultSet resultSet) {
			List<ObjectType> objects = new ArrayList<>();
			try {
				while (resultSet.next()) {
					objects.add(parseResult(resultSet, objectClass.newInstance()));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return objects;
		}

		private ObjectType parseResult(ResultSet resultSet, ObjectType object) {
			try {
				for (String fieldName : fieldReflection.keySet()) {
					Field field = fieldReflection.get(fieldName);
					DatabaseField descriptor = fieldDescriptors.get(field);
					String colName = descriptor.columnName();
					if (colName.length() == 0)
						colName = fieldName;
					if(descriptor.otherClassReference()) {
						//this is referencing to other class // do call to that class and parse and set
						Type t2 = field.getType();
						if(t2.equals(ClientRecord.class)) {
							Table t = new Table<ClientRecord, Integer>(ClientRecord.class);
							Object id = getValue(resultSet, colName, int.class);
							System.out.println("Id - "+id);
							ClientRecord clientInfo =  (ClientRecord) t.queryById(id);
							System.out.println(clientInfo);
							field.set(object, clientInfo);
						}else {
							Table t = new Table<Vehicle, Integer>(Vehicle.class);
							Object id = getValue(resultSet, colName, int.class);
							System.out.println("Id - "+id);
							Vehicle vehicle =  (Vehicle) t.queryById(id);
							System.out.println(vehicle);
							field.set(object, vehicle);
						}
					}else {
						field.set(object, getValue(resultSet, colName, field.getType()));
					}
					
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return object;
		}

		private Object getValue(ResultSet resultSet, String colName, Class type) throws SQLException {
			Object value = null;
			if (resultSet.getString(colName) == null)
				return null;
			if (type.isEnum())
				value = Enum.valueOf((Class<Enum>) type, resultSet.getString(colName));
			if (type.equals(int.class))
				value = resultSet.getInt(colName);
			if (type.equals(short.class))
				value = resultSet.getShort(colName);
			if (type.equals(double.class))
				value = resultSet.getDouble(colName);
			if (type.equals(long.class))
				value = resultSet.getLong(colName);
			if (type.equals(boolean.class))
				value = resultSet.getBoolean(colName);
			if (type.equals(float.class))
				value = resultSet.getFloat(colName);
			if (type.equals(Timestamp.class))
				value = resultSet.getTimestamp(colName);
			// edit here to change time stamp
			if (type.equals(Date.class))
				value = resultSet.getTimestamp(colName);
			if (type.equals(Time.class))
				value = resultSet.getTime(colName);
			if (type.equals(UUID.class))
				value = UUID.fromString(resultSet.getString(colName));
			if (value == null)
				value = resultSet.getString(colName);
			return value;
		}

	}

	public class QueryResult {
		private List<ObjectType> all;

		public QueryResult(List<ObjectType> all) {
			this.all = all;
		}

		public List<ObjectType> all() {
			return this.all;
		}

		public ObjectType first() {
			return all.size() > 0 ? get(0) : null;
		}

		public ObjectType get(int index) {
			return all.get(index);
		}

		public int count() {
			return all.size();
		}
	}

	
}
