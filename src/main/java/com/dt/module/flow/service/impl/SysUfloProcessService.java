package com.dt.module.flow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bstek.uflo.command.impl.jump.JumpNode;
import com.bstek.uflo.model.HistoryTask;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.model.task.TaskState;
import com.bstek.uflo.query.HistoryTaskQuery;
import com.bstek.uflo.query.TaskQuery;
import com.bstek.uflo.service.HistoryService;
import com.bstek.uflo.service.ProcessService;
import com.bstek.uflo.service.StartProcessInfo;
import com.bstek.uflo.service.TaskOpinion;
import com.bstek.uflo.service.TaskService;
import com.bstek.uflo.utils.EnvironmentUtils;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.base.entity.SysUserInfo;
import com.dt.module.base.service.ISysUserInfoService;
import com.dt.module.flow.entity.SysProcessData;
import com.dt.module.flow.entity.TaskInfo;
import com.dt.module.flow.service.ISysProcessClassItemService;
import com.dt.module.flow.service.ISysProcessDataService;

/**
 * @author: algernonking
 * @date: Dec 1, 2019 10:02:28 AM
 * @Description: TODO
 */

@Service
public class SysUfloProcessService extends BaseService {
	public static String P_TYPE_RUNNING = "running";
	public static String P_TYPE_CANCEL = "cancel";
	public static String P_TYPE_FINISH = "finish";

	public static String P_STATUS_SFA = "submitforapproval";
	public static String P_STATUS_INREVIEW = "inreview";
	public static String P_STATUS_APPROVALSUCCESS = "success";
	public static String P_STATUS_APPROVALFAILED = "failed";
	public static String P_STATUS_CANCEL = "cancel";
	
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

	@Autowired
	ISysUserInfoService SysUserInfoServiceImpl;

	public R loadProcessInstanceData(String processInstanceId) {
		TaskQuery query = taskService.createTaskQuery();
		long processInstanceIdl = ConvertUtil.toLong(processInstanceId);
		query.rootProcessInstanceId(processInstanceIdl);
		// query.nodeName(node.getName());
		query.addTaskState(TaskState.Created);
		query.addTaskState(TaskState.InProgress);
		query.addTaskState(TaskState.Ready);
		query.addTaskState(TaskState.Suspended);
		query.addTaskState(TaskState.Reserved);
		List<Task> tasks = query.list();
		List<TaskInfo> taskinfo = buildTaskInfos(tasks);
		HistoryTaskQuery historyTaskQuery = historyService.createHistoryTaskQuery();
		historyTaskQuery.rootProcessInstanceId(processInstanceIdl);
		List<HistoryTask> historyTasks = historyTaskQuery.list();
		List<TaskInfo> histaskinfo = buildHistoryTaskInfos(historyTasks);
		JSONArray data = new JSONArray();
		if (taskinfo.size() > 0) {
			data = JSONArray.parseArray(JSON.toJSONString(taskinfo, SerializerFeature.WriteDateUseDateFormat,
					SerializerFeature.DisableCircularReferenceDetect));
		}
		if (histaskinfo.size() > 0) {
			data = JSONArray.parseArray(JSON.toJSONString(histaskinfo, SerializerFeature.WriteDateUseDateFormat,
					SerializerFeature.DisableCircularReferenceDetect));
		}

		return R.SUCCESS_OPER(data);
	}

	private List<TaskInfo> buildTaskInfos(List<Task> tasks) {
		List<TaskInfo> infos = new ArrayList<TaskInfo>();
		for (Task task : tasks) {
			TaskInfo info = new TaskInfo();
			info.setAssignee(task.getAssignee());
			info.setBusinessId(task.getBusinessId());
			info.setCreateDate(task.getCreateDate());
			info.setDescription(task.getDescription());
			info.setDuedate(task.getDuedate());
			info.setOpinion(task.getOpinion());
			info.setOwner(task.getOwner());
			info.setProcessId(task.getProcessId());
			info.setProcessInstanceId(task.getProcessInstanceId());
			info.setState(task.getState());
			info.setTaskId(task.getId());
			info.setTaskName(task.getTaskName());
			info.setType(task.getType());
			info.setUrl(task.getUrl());
			String assignee = info.getAssignee();
			if (assignee != null) {
				SysUserInfo u = SysUserInfoServiceImpl.getById(assignee);
				if (u != null) {
					info.setAssigneename(u.getName());
				}
			}
			infos.add(info);
		}
		return infos;
	}

	public R cancelTask(String taskId, String opinion) {
		TaskOpinion op = new TaskOpinion(opinion);
		long taskId_l = ConvertUtil.toLong(taskId);
		taskService.cancelTask(taskId_l, op);
		return R.SUCCESS_OPER();
	}

	public R startProcess(String key, String type) {
		if (ToolUtil.isOneEmpty(key, type)) {
			return R.FAILURE_REQ_PARAM_ERROR();
		}

		TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
		String busid = ToolUtil.getUUID();
		StartProcessInfo startProcessInfo = new StartProcessInfo(EnvironmentUtils.getEnvironment().getLoginUser());
		startProcessInfo.setBusinessId(busid);
		startProcessInfo.setCompleteStartTask(true);
		ProcessInstance inst = processService.startProcessByKey(key, startProcessInfo);
		SysProcessData pd = new SysProcessData();
		pd.setBusid(busid);
		pd.setProcesskey(key);
		pd.setPtype(type);
		pd.setPstatus(SysUfloProcessService.P_TYPE_RUNNING);
		pd.setProcessInstanceId(inst.getId() + "");
		pd.setPstartuserid(this.getUserId());
		pd.setDmessage(ps.getString("dmessage", ""));
		pd.setDmark(ps.getString("dmark", ""));
		SysProcessDataServiceImpl.save(pd);
		return R.SUCCESS_OPER();
	}

	private Map<String, Object> buildVariables(String variables) {
		if (StringUtils.isBlank(variables)) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> list = mapper.readValue(variables, ArrayList.class);
			Map<String, Object> map = new HashMap<String, Object>();
			for (Map<String, Object> m : list) {
				String key = m.get("key").toString();
				Object value = m.get("value");
				map.put(key, value);
			}
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<TaskInfo> buildHistoryTaskInfos(List<HistoryTask> tasks) {
		List<TaskInfo> infos = new ArrayList<TaskInfo>();
		for (HistoryTask task : tasks) {
			TaskInfo info = new TaskInfo();
			info.setAssignee(task.getAssignee());
			info.setBusinessId(task.getBusinessId());
			info.setCreateDate(task.getCreateDate());
			info.setDescription(task.getDescription());
			info.setDuedate(task.getDuedate());
			info.setEndDate(task.getEndDate());
			info.setOpinion(task.getOpinion());
			info.setOwner(task.getOwner());
			info.setProcessId(task.getProcessId());
			info.setProcessInstanceId(task.getProcessInstanceId());
			info.setState(task.getState());
			info.setTaskName(task.getTaskName());
			info.setTaskId(task.getId());
			info.setType(task.getType());
			info.setUrl(task.getUrl());
			String assignee = task.getAssignee();
			if (assignee != null) {
				SysUserInfo u = SysUserInfoServiceImpl.getById(assignee);
				if (u != null) {
					info.setAssigneename(u.getName());
				}
			}
			infos.add(info);
		}
		return infos;
	}

	public R computeTask(String variables, String taskId, String opinion) {

		TaskOpinion op = new TaskOpinion(opinion);
		long taskId_l = ConvertUtil.toLong(taskId);
		taskService.start(taskId_l);
		taskService.complete(taskId_l, op);
		return R.SUCCESS_OPER();
	}

	//
	public R refuseTask(String taskId, String opinion) {
		TaskOpinion op = new TaskOpinion(opinion);
		long taskId_l = ConvertUtil.toLong(taskId);
		Task tsk = taskService.getTask(taskId_l);
		String instid = tsk.getProcessInstanceId() + "";
		List<JumpNode> nodes = taskService.getAvaliableForwardTaskNodes(taskId_l);
		if (nodes.size() == 0) {
			return R.FAILURE("无法跳转至结束流程");
		}
		JumpNode jn = nodes.get(nodes.size() - 1);
		if (jn.isTask()) {
			return R.FAILURE("获取的最后一个节点不是结束流程");
		}
		taskService.forward(taskId_l, jn.getName(), op);
		// 更新状态
		UpdateWrapper<SysProcessData> uw = new UpdateWrapper<SysProcessData>();
		uw.eq("processInstanceId", instid);
		uw.set("pstatus", SysUfloProcessService.P_TYPE_FINISH);
		uw.set("pstatusdtl", P_STATUS_APPROVALFAILED);
		SysProcessDataServiceImpl.update(uw);
		return R.SUCCESS_OPER();
	}

}
