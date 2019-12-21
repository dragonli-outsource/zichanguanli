package com.dt.module.db;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dt.core.dao.SpringMySQLDao;

import com.dt.core.tool.lang.SpringContextUtil;
import org.springframework.transaction.PlatformTransactionManager;

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

	@Bean
	@Primary
	public PlatformTransactionManager transactionManager(
			@Autowired @Qualifier("transactionManager") PlatformTransactionManager transactionManager){
		return transactionManager;
	}
}
