package com.dt.core.dao;

import java.util.ArrayList;
import java.util.HashMap;

public class RcdMetaData {
	private int columnCount = 0;

	public int getColumnCount() {
		return columnCount;
	}

	protected void setColumnCount(int c) {
		columnCount = c;
	}

	private ArrayList<String> catalogNames = new ArrayList<String>();

	protected void addCatalogName(String val) {
		catalogNames.add(val);
	}

	public String getCatalogName(int i) {
		return catalogNames.get(i);
	}

	private ArrayList<String> columnClassName = new ArrayList<String>();

	protected void addColumnClassName(String val) {
		columnClassName.add(val);
	}

	public String getColumnClassName(int i) {
		return columnClassName.get(i);
	}

	private ArrayList<String> columnLabel = new ArrayList<String>();

	protected void addColumnLabel(String val) {
		columnLabel.add(val);
	}

	public String getColumnLabel(int i) {
		return columnLabel.get(i).toLowerCase();
	}

	public String[] getColumnLabels() {
		String[] arr = new String[columnLabel.size()];
		return columnLabel.toArray(arr);
	}

	private ArrayList<Integer> columnType = new ArrayList<Integer>();

	protected void addColumnType(Integer val) {
		columnType.add(val);
	}

	public Integer getColumnType(int i) {
		return columnType.get(i);
	}

	private ArrayList<ColumnDataGenerater> columnDataGeneraters = new ArrayList<ColumnDataGenerater>();

	protected void addColumnDataGenerater(ColumnDataGenerater val) {
		columnDataGeneraters.add(val);
	}

	public ColumnDataGenerater getColumnDataGenerater(int i) {
		return columnDataGeneraters.get(i);
	}

	private ArrayList<String> columnTypeName = new ArrayList<String>();

	protected void addColumnTypeName(String val) {
		columnTypeName.add(val);
	}

	public String getColumnTypeName(int i) {
		return columnTypeName.get(i);
	}

	private ArrayList<String> schemaName = new ArrayList<String>();

	protected void addSchemaName(String val) {
		schemaName.add(val);
	}

	public String getSchemaName(int i) {
		return schemaName.get(i);
	}

	private ArrayList<String> tableName = new ArrayList<String>();

	protected void addTableName(String val) {
		tableName.add(val);
	}

	public String getTableName(int i) {
		return tableName.get(i);
	}

	private HashMap<String, Integer> nameIndexMap = new HashMap<String, Integer>();

	protected void setMap(String name, int idx) {
		nameIndexMap.put(name.toUpperCase(), idx);
	}

	public int name2index(String name) {
		Integer i = nameIndexMap.get(name.toUpperCase());
		if (i == null) {
			// System.out.println("NO "+ name+ " IN \n"+this.sql);
			// (new Throwable()).printStackTrace();
			return -1;
		} else {
			return i;
		}
	}

	@SuppressWarnings("unused")
	private String getExtJsStoreColumnType(int i) {

		// [{"name":"MC","type":"string"},{"name":"CKD","type":"java.math.BigDecimal"}]
		String na = columnClassName.get(i);

		if (na.equals("java.lang.String")) {
			na = null;
		} else if (na.equals("java.math.BigDecimal")) {
			na = "float";
		} else if (na.equals("java.util.Date") || na.equals("java.sql.Date") || na.equals("java.sql.Timestamp")) {
			na = "date";
		} else if (na.equals("java.lang.Boolean")) {
			na = "bool";
		} else if (na.equals("java.lang.Integer")) {
			na = "int";
		}

		return na;
	}

	private String sql = null;

	public String getSql() {
		return sql;
	}

	protected void setSql(String sql) {
		this.sql = sql;
	}

}
