package com.dt.module.hrm.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.hrm.service.EmplService;

@Controller
@RequestMapping("/api")
public class EmplController extends BaseController {

	@Autowired
	private EmplService emplService;

	@RequestMapping("/hrm/employeeAdd.do")
	@ResponseBody
	@Acl(info = "添加人员")
	public R employeeAdd() {
		TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
		return emplService.addEmployee(ps);
	}

	@RequestMapping("/hrm/employeeUpdate.do")
	@ResponseBody
	@Acl(info = "更新人员")
	public R employeeUpdate() {
		TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
		return emplService.updateEmployee(ps);
	}

	@RequestMapping("/hrm/employeeQueryList.do")
	@ResponseBody
	@Acl(info = "查询人员")
	public R employeeQueryList() {
		return emplService.queryEmplList(HttpKit.getRequestParameters());
	}

	@RequestMapping("/hrm/employeeQueryById.do")
	@ResponseBody
	@Acl(info = "查询人员信息")
	public R employeeQueryById(String empl_id) {
		return emplService.queryEmplById(empl_id);
	}

	@RequestMapping("/hrm/employeeDelete.do")
	@ResponseBody
	@Acl(info = "删除人员")
	@Transactional
	public R employeeDelete(String empl_id) throws IOException {
		return emplService.delEmployee(empl_id);
	}
}
