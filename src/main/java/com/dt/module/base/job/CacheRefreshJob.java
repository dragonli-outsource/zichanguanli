package com.dt.module.base.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dt.module.base.service.impl.CacheService;

/**
 * @author: algernonking
 * @date: 2018年4月4日 下午3:15:52
 * @Description: TODO
 */
public class CacheRefreshJob implements Job {
	private static Logger _log = LoggerFactory.getLogger(CacheRefreshJob.class);

	@Override
	public void execute(JobExecutionContext jc) throws JobExecutionException {
		_log.info("session clear start.");
		CacheService.me().refreshCaches();
		_log.info("session clear end.");
	}

}
