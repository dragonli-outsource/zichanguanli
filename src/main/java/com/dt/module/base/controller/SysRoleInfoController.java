package com.dt.module.base.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.base.entity.SysRoleInfo;
import com.dt.module.base.service.ISysRoleInfoService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author algernonking
 * @since 2018-07-24
 */
@Controller
@RequestMapping("/api/sysRoleInfo")
public class SysRoleInfoController extends BaseController {

	@Autowired
	ISysRoleInfoService SysRoleInfoServiceImpl;

	@ResponseBody
	@Acl(info = "根据Id删除", value = Acl.ACL_DENY)
	@RequestMapping(value = "/deleteById.do")
	public R deleteById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
		if (SysRoleInfoServiceImpl.isUsed(id) > 0) {
			return R.FAILURE("使用中,暂不可删除");
		}
		return R.SUCCESS_OPER(SysRoleInfoServiceImpl.removeById(id));
	}

	@ResponseBody
	@Acl(info = "根据Id查询", value = Acl.ACL_DENY)
	@RequestMapping(value = "/selectById.do")
	public R selectById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
		return R.SUCCESS_OPER(SysRoleInfoServiceImpl.getById(id));
	}

	@ResponseBody
	@Acl(info = "插入", value = Acl.ACL_DENY)
	@RequestMapping(value = "/insert.do")
	public R insert(SysRoleInfo entity) {
		return R.SUCCESS_OPER(SysRoleInfoServiceImpl.save(entity));
	}

	@ResponseBody
	@Acl(info = "根据Id更新", value = Acl.ACL_DENY)
	@RequestMapping(value = "/updateById.do")
	public R updateById(SysRoleInfo entity) {
		return R.SUCCESS_OPER(SysRoleInfoServiceImpl.updateById(entity));
	}

	@ResponseBody
	@Acl(info = "存在则更新,否则插入", value = Acl.ACL_DENY)
	@RequestMapping(value = "/insertOrUpdate.do")
	public R insertOrUpdate(SysRoleInfo entity) {
		return R.SUCCESS_OPER(SysRoleInfoServiceImpl.saveOrUpdate(entity));
	}

	@ResponseBody
	@Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
	@RequestMapping(value = "/selectList.do")
	public R selectList() {
		return R.SUCCESS_OPER(SysRoleInfoServiceImpl.list(null));
	}

	@RequestMapping(value = "/roleQueryFormatKV.do")
	@ResponseBody
	@Acl(info = "查询权限",value = Acl.ACL_DENY)
	public R roleQueryFormatKV() {
		List<SysRoleInfo> roles = SysRoleInfoServiceImpl.list(null);
		JSONObject obj = new JSONObject();
		for (int i = 0; i < roles.size(); i++) {
			obj.put(roles.get(i).getRoleId(), roles.get(i).getRoleName());
		}
		return R.SUCCESS_OPER(obj);
	}

}
