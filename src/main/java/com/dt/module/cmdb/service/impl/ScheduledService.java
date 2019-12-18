package com.dt.module.cmdb.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dt.core.common.base.BaseService;

/**
 * @author: algernonking
 * @date: Nov 2, 2019 2:40:03 PM
 * @Description: TODO
 */
@Service
public class ScheduledService extends BaseService {
	@Scheduled(cron = "0 */30 * * * ? ")
	public void checkWbAuto() {
		Date date = new Date(); // 获取一个Date对象
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 创建一个格式化日期对象
		String nowtime = simpleDateFormat.format(date);

		// 转脱保
		String sql1 = "update  res set wb='invalid' where id in (\n" + "    select t.id from (\n" + "      select id\n"
				+ "      from res\n"
				+ "      where wbout_date is not null and dr = 0 and    (wb <> 'invalid' or wb is null)   and wb_auto = '1'\n"
				+ "            and wbout_date < now()\n" + "    ) t\n" + ")";
		int invalid = db.execute(sql1);
		// 转在保
		String sql2 = "update  res set wb='valid' where id in (\n" + "    select t.id from (\n" + "  select id\n"
				+ "  from res\n"
				+ "  where wbout_date is not null and dr = 0 and (wb <> 'valid' or wb is null)  and wb_auto = '1'\n"
				+ "        and wbout_date > now())t\n" + "\n" + ")";
		int valid = db.execute(sql2);

		System.out.println("Time:" + nowtime + " | Execute Job,Check Wb Status |" + " Covert to valid Cnt:" + valid
				+ ",Covert to invalid Cnt:" + invalid);
	}
}
