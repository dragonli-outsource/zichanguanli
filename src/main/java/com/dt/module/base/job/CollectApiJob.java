package com.dt.module.base.job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.dt.core.annotion.Acl;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.SQL;
import com.dt.core.tool.lang.SpringContextUtil;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.service.impl.JobService;
import com.dt.module.db.DB;

/** 
 * @author: algernonking
 * @date: 2017年11月9日 下午2:34:37 
 * @Description: TODO 
 */
public class CollectApiJob implements Job {
	private static Logger _log = LoggerFactory.getLogger(CollectApiJob.class);

	@Override
	public void execute(JobExecutionContext jc) throws JobExecutionException {
		_log.info("collect urls start.");
		List<SQL> sqls = new ArrayList<SQL>();
		WebApplicationContext wc = (WebApplicationContext) SpringContextUtil.getApplicationContext();
		RequestMappingHandlerMapping bean = wc.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
			RequestMappingInfo rmi = entry.getKey();
			PatternsRequestCondition pc = rmi.getPatternsCondition();
			Set<String> pSet = pc.getPatterns();
			HandlerMethod hm = entry.getValue();
			Acl am = ((HandlerMethod) hm).getMethodAnnotation(Acl.class);
			if (ToolUtil.isNotEmpty(am)) {
				String aclvalue = am.value();
				String aclinfo = am.info();
				String type = am.type();
				Iterator<String> it = pSet.iterator();
				while (it.hasNext()) {
					String str = it.next();
					Insert me = new Insert("sys_api");
					me.set("id", DB.instance().getUUID());
					me.setIf("ct", str);
					me.setIf("dr", "0");
					me.setIf("ctacl", aclvalue);
					me.setIf("apitype", "url");
					me.setIf("info", aclinfo);
					me.setIf("type", type);
					me.setSE("rectime", DbUtil.getDbDateString(DB.instance().getDBType()));
					_log.info(str + "," + aclvalue + "," + me.getSQL());
					sqls.add(me);
				}
			}

		}
		if (sqls.size() > 0) {
			_log.info("Save collect Api.");
			DB.instance().tabTruncate("sys_api");
			DB.instance().executeSQLList(sqls);
		} else {
			_log.info("Save collect Api failed.");
		}
		JobService.me().finishedJobUpdate(jc);
	}
}


