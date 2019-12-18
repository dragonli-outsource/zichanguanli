package com.dt.module.flow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.module.flow.entity.SysProcessClassItem;
import com.dt.module.flow.service.ISysProcessClassItemService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author algernonking
 * @since 2019-11-30
 */
@Controller
@RequestMapping("/api/flow/sysProcessClassItemExt")
public class SysProcessClassItemExtController extends BaseController {

	@Autowired
	ISysProcessClassItemService SysProcessClassItemServiceImpl;

	@ResponseBody
	@Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
	@RequestMapping(value = "/selectList.do")
	public R selectList(String cid) {

		QueryWrapper<SysProcessClassItem> ew = new QueryWrapper<SysProcessClassItem>();
		ew.eq("cid", cid);
		return R.SUCCESS_OPER(SysProcessClassItemServiceImpl.list(ew));
	}

}
