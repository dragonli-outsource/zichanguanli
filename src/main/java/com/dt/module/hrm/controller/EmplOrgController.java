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
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.hrm.service.EmplOrgService;
import com.dt.module.hrm.service.EmplService;

@Controller
@RequestMapping("/api")
public class EmplOrgController extends BaseController {
	@Autowired
	private EmplOrgService emplOrgService;
	@Autowired
	private EmplService emplService;

	@ResponseBody
	@Acl(info = "添加组织节点")
	@RequestMapping("/hrm/orgNodeDelete.do")
	public R orgNodeDelete(String node_id) {
		return emplOrgService.deleteEmplOrg(node_id);
	}

	@RequestMapping("/hrm/queryEmplByOrg.do")
	@ResponseBody
	@Acl(info = "查询组织人员")
	public R queryEmplByOrg(String node_id) throws IOException {
		return emplService.queryEmplByOrg(node_id);
	}

	@RequestMapping("/hrm/orgNodeSave.do")
	@ResponseBody
	@Acl(info = "保存节点")
	@Transactional
	public R orgNodeSave() {
		TypedHashMap<String, Object> ps = (TypedHashMap<String, Object>) HttpKit.getRequestParameters();
		String id = ps.getString("node_id");
		if (ToolUtil.isEmpty(id)) {
			return emplOrgService.addEmplOrg(ps);
		} else {
			return emplOrgService.updateEmplOrg(ps);
		}
	}

	@RequestMapping("/hrm/orgNodeQuery.do")
	@ResponseBody
	@Acl(info = "查询组织节点")
	public R orgNodeQuery(String node_id) {
		return emplOrgService.queryEmplOrgById(node_id);
	}

	@RequestMapping("/hrm/orgNodeTreeQuery.do")
	@ResponseBody
	@Acl(info = "查询组织")
	public R orgNodeTreeQuery(String org_id) {
		return emplOrgService.queryEmplOrgNodeTree(org_id);
	}

	@RequestMapping("/hrm/orgQueryLevelList.do")
	@ResponseBody
	@Acl(info = "查询组织")
	public R orgQueryLevelList() {
		return emplOrgService.queryEmplOrgLevelList();
	}

	@RequestMapping("/hrm/orgQuery.do")
	@ResponseBody
	@Acl(info = "查询组织")
	public R orgQuery() {
		return emplOrgService.queryEmplOrg();
	}
}
