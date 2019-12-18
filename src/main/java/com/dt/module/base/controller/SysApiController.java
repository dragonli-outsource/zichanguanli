package com.dt.module.base.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.base.entity.SysApi;
import com.dt.module.base.service.ISysApiService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author algernonking
 * @since 2018-07-27
 */
@Controller
@RequestMapping("/api/sysApi")
public class SysApiController extends BaseController {


	@Autowired
	ISysApiService SysApiServiceImpl;


	@ResponseBody
	@Acl(info = "根据Id删除", value = Acl.ACL_DENY)
	@RequestMapping(value = "/deleteById.do")
	public R deleteById(@RequestParam(value = "id", required = true, defaultValue = "")String id) {
		return R.SUCCESS_OPER(SysApiServiceImpl.removeById(id));
	}

	@ResponseBody
	@Acl(info = "根据Id查询", value = Acl.ACL_DENY)
	@RequestMapping(value = "/selectById.do")
	public R selectById(@RequestParam(value = "id", required = true, defaultValue = "")String id) {
		return R.SUCCESS_OPER(SysApiServiceImpl.getById(id));
	}

	@ResponseBody
	@Acl(info = "插入", value = Acl.ACL_DENY)
	@RequestMapping(value = "/insert.do")
	public R insert(SysApi entity) {
		return R.SUCCESS_OPER(SysApiServiceImpl.save(entity));
	}

	@ResponseBody
	@Acl(info = "根据Id更新", value = Acl.ACL_DENY)
	@RequestMapping(value = "/updateById.do")
	public R updateById(SysApi entity) {
		return R.SUCCESS_OPER(SysApiServiceImpl.updateById(entity));
	}

	@ResponseBody
	@Acl(info = "存在则更新,否则插入", value = Acl.ACL_DENY)
	@RequestMapping(value = "/insertOrUpdate.do")
	public R insertOrUpdate(SysApi entity) {
		return R.SUCCESS_OPER(SysApiServiceImpl.saveOrUpdate(entity));
	}

	@ResponseBody
	@Acl(info = "查询所有,无分页", value = Acl.ACL_DENY)
	@RequestMapping(value = "/selectList.do")
	public R selectList() {
		return R.SUCCESS_OPER(SysApiServiceImpl.list(null));
	}
 

}

