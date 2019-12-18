package com.dt.module.db;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dt.core.dao.SpringMySQLDao;

import com.dt.core.tool.lang.SpringContextUtil;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DB extends SpringMySQLDao {
	private static Logger _log = LoggerFactory.getLogger(DB.class);

	public static DB instance() {
		return SpringContextUtil.getBean(DB.class);
	}

	private String dbname = "db";

	@Resource(name = "db")
	public void setDataSource(DataSource dataSource) {
		_log.info(getDBType() + " " + dbname + " setDataSource");
		super.setDataSource(dataSource);
	}
}
