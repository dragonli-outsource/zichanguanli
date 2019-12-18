package com.dt.module.base.job;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dt.module.base.service.impl.JobService;
import com.dt.module.db.DB;

/**
 * @author: algernonking
 * @date: Nov 7, 2017 10:27:05 AM
 * @Description: TODO
 */
public class ClearSessionJob implements Job {
	private static Logger _log = LoggerFactory.getLogger(ClearSessionJob.class);

	@Override
	public void execute(JobExecutionContext jc) throws JobExecutionException {
		_log.info("session clear start.");
		List<String> sqls = new ArrayList<String>();
		// 删除所有user_id为空的
		//sqls.add("delete from sys_session where user_id is null");
		// 删除90天为访问的类型为web
		//sqls.add("delete from sys_session where client='web' and lastaccess<"+ DbUtil.getDbDayBeforeString(DB.instance().getDBType(), "90"));
		DB.instance().executeStringList(sqls);
		JobService.me().finishedJobUpdate(jc);
		_log.info("session clear end.");
	}
}
