package com.dt.core.dao.sql;

import com.dt.core.dao.SpringDAO;

public interface ExecutableSQL extends SQL {
	public Integer execute();

	public SpringDAO getDao();

	public ExecutableSQL setDao(SpringDAO dao);
}
