package com.dt.core.dao.sql;

import java.io.Serializable;
import java.util.HashMap;

public interface SQL extends Serializable {
	public static final String PNAME_PREFIX = "PARAM";

	/**
	 * IgnorColon
	 */
	public SubSQL setIgnorColon(boolean b);

	public String getSQL();

	public String getParamedSQL();

	public Object[] getParams();

	public String getParamNamedSQL();

	public HashMap<String, Object> getNamedParams();

	public boolean isEmpty();

	public boolean isAllParamsEmpty();

	public boolean isAllParamsEmpty(boolean isCE);

	public SQL top();

	public SQL parent();

	public void setParent(SQL sql);

	public void beginParamNameSQL();

	public void endParamNameSQL();

	public String getNextParamName(boolean withColon);

}
