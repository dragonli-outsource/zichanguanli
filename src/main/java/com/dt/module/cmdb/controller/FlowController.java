package com.dt.module.cmdb.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bstek.uflo.console.handler.impl.PageData;
import com.bstek.uflo.model.HistoryTask;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.query.HistoryTaskQuery;
import com.bstek.uflo.query.TaskQuery;
import com.bstek.uflo.service.HistoryService;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.StartProcessInfo;
import com.bstek.uflo.service.TaskService;
import com.bstek.uflo.utils.EnvironmentUtils;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.base.service.ISysUserInfoService;
import com.dt.module.cmdb.service.IResActionItemService;
import com.dt.module.cmdb.service.impl.ResActionService;
import com.dt.module.cmdb.service.impl.ResExtService;
import com.dt.module.flow.entity.SysProcessData;
import com.dt.module.flow.service.ISysProcessClassItemService;
import com.dt.module.flow.service.ISysProcessDataService;
import com.dt.module.flow.service.impl.SysUfloProcessService;

/**
 * @author: algernonking
 * @date: Dec 2, 2019 2:31:20 PM
 * @Description: TODO
 */
@Controller
@RequestMapping("/api/cmdb/flow")
public class FlowController extends BaseController {

	@Autowired
	SysUfloProcessService sysUfloProcessService;

	@Autowired
	IResActionItemService ResActionItemServiceImpl;

	@Autowired
	ResExtService resExtService;

	@Autowired
	ISysUserInfoService SysUserInfoServiceImpl;

	@Autowired
	private ProcessService processService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	ISysProcessClassItemService SysProcessClassItemServiceImpl;

	@Autowired
	ISysProcessDataService SysProcessDataServiceImpl;

	@ResponseBody
	@Acl(info = "", value = Acl.ACL_USER)
	@RequestMapping(value = "/zc/myProcessTodo.do")
	public R loadTodo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String loginUsername = EnvironmentUtils.getEnvironment().getLoginUser();
		String taskName = req.getParameter("taskName");
		int pageSize = Integer.valueOf(req.getParameter("pageSize"));
		int pageIndex = Integer.valueOf(req.getParameter("pageIndex"));
		int firstResult = (pageIndex - 1) * pageSize;
		TaskQuery query = taskService.createTaskQuery();
		query.addTaskState(TaskState.Created);
		query.addTaskState(TaskState.InProgress);
		query.addTaskState(TaskState.Ready);
		query.addTaskState(TaskState.Suspended);
		query.addTaskState(TaskState.Reserved);
		query.addAssignee(loginUsername).addOrderDesc("createDate").page(firstResult, pageSize);
		if (StringUtils.isNotBlank(taskName)) {
			query.nameLike("%" + taskName + "%");
		}
		List<Task> tasks = query.list();
		return R.SUCCESS_OPER(JSONArray.parseArray(JSON.toJSONString(tasks, SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.DisableCircularReferenceDetect)));
	}

	@ResponseBody
	@Acl(info = "", value = Acl.ACL_USER)
	@RequestMapping(value = "/zc/myProcessloadHistory.do")
	public R loadHistory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String loginUsername = EnvironmentUtils.getEnvironment().getLoginUser();
		int pageSize = Integer.valueOf(req.getParameter("pageSize"));
		int pageIndex = Integer.valueOf(req.getParameter("pageIndex"));
		String taskName = req.getParameter("taskName");
		int firstResult = (pageIndex - 1) * pageSize;
		HistoryTaskQuery query = historyService.createHistoryTaskQuery();
		if (StringUtils.isNotBlank(taskName)) {
			query.nameLike("%" + taskName + "%");
		}
		query.assignee(loginUsername).addOrderDesc("endDate").page(firstResult, pageSize);
		int total = query.count();
		List<HistoryTask> tasks = query.list();
		JSONObject retrunObject = new JSONObject();
		retrunObject.put("iTotalRecords", total);
		retrunObject.put("iTotalDisplayRecords", total);
		retrunObject.put("data", JSONArray.parseArray(JSON.toJSONString(tasks, SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.DisableCircularReferenceDetect)));
		return R.clearAttachDirect(retrunObject);
	}

	@ResponseBody
	@Acl(info = "发起流程", value = Acl.ACL_USER)
	@RequestMapping(value = "/zc/startProcess.do")
	public R startProcess(String spmethod, String processkey) {
		String busid = ToolUtil.getUUID();
		TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
		UpdateWrapper<SysProcessData> up = new UpdateWrapper<SysProcessData>();
		SysProcessData sd = SysProcessDataServiceImpl.getById(ps.getString("id"));
		if (sd == null) {
			return R.FAILURE("不存在流程数据");
		}
		if ("1".equals(spmethod)) {
			// 需要审批
			if (sd.getProcessInstanceId() != null) {
				return R.FAILURE("已有流程,无法重复提交");
			}

			StartProcessInfo startProcessInfo = new StartProcessInfo(EnvironmentUtils.getEnvironment().getLoginUser());
			startProcessInfo.setCompleteStartTask(true);
			startProcessInfo.setBusinessId(busid);
			startProcessInfo.setTag(sd.getPtype());
			startProcessInfo.setSubject(sd.getDtitle());
			startProcessInfo.setCompleteStartTaskOpinion("发起流程");
			ProcessInstance inst = processService.startProcessByKey(processkey, startProcessInfo);
			// 插入流程数据
			up.set("busid", busid);
			up.set("processkey", processkey);
			up.set("ptitle", sd.getDtitle());
			up.set("pstatus", SysUfloProcessService.P_TYPE_RUNNING);
			up.set("pstatusdtl", SysUfloProcessService.P_STATUS_INREVIEW);
			up.set("processInstanceId", inst.getId() + "");
			up.set("pstartuserid", getUserId());
			up.set("pstartusername", SysUserInfoServiceImpl.getById(this.getUserId()).getName());

		} else {
			up.set("pstatusdtl", SysUfloProcessService.P_STATUS_APPROVALSUCCESS);
		}
		up.set(spmethod != null, "dmethod", spmethod);
		up.eq("id", ps.getString("id"));
		SysProcessDataServiceImpl.update(up);
		return R.SUCCESS_OPER();
	}

	@RequestMapping("/zc/computeTask.do")
	@ResponseBody
	@Acl(info = "", value = Acl.ACL_USER)
	public R computeTask(String variables, String taskId, String opinion) {
		R r = sysUfloProcessService.computeTask(variables, taskId, opinion);
		 
		return r;
	}
	@RequestMapping("/zc/refuseTask.do")
	@ResponseBody
	@Acl(info = "", value = Acl.ACL_USER)
	public R refuseTask( String taskId, String opinion) {
		R r = sysUfloProcessService.refuseTask( taskId, opinion);
		 
		return r;
	}
}
