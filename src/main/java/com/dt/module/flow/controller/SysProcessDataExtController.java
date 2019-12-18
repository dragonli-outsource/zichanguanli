package com.dt.module.flow.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import com.dt.module.flow.entity.SysProcessData;
import com.dt.module.flow.service.ISysProcessDataService;
import org.springframework.beans.factory.annotation.Autowired;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.R;
import org.springframework.web.bind.annotation.ResponseBody;
import com.dt.core.tool.util.DbUtil;
import com.alibaba.fastjson.JSONObject;
import com.dt.core.tool.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import com.dt.core.common.base.BaseController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author algernonking
 * @since 2019-12-03
 */
@Controller
@RequestMapping("/api/flow/sysProcessDataExt")
public class SysProcessDataExtController extends BaseController {

	@Autowired
	ISysProcessDataService SysProcessDataServiceImpl;

	@ResponseBody
	@Acl(info = "根据Id查询", value = Acl.ACL_USER)
	@RequestMapping(value = "/selectByBusinessId.do")
	public R selectById(@RequestParam(value = "businessid", required = true, defaultValue = "") String businessid) {
		QueryWrapper<SysProcessData> qw = new QueryWrapper<SysProcessData>();
		qw.eq("busid", businessid);
		return R.SUCCESS_OPER(SysProcessDataServiceImpl.getOne(qw));
	}

	@ResponseBody
	@Acl(info = "查询所有,无分页", value = Acl.ACL_USER)
	@RequestMapping(value = "/selectListByMy.do")
	public R selectList() {
		QueryWrapper<SysProcessData> qw = new QueryWrapper<SysProcessData>();
		qw.eq("pstartuserid", this.getUserId());
		qw.orderByDesc("create_time");
		return R.SUCCESS_OPER(SysProcessDataServiceImpl.list(qw));
	}

}
