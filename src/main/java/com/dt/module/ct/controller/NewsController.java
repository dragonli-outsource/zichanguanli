package com.dt.module.ct.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.bus_enum.ctCtTypeEnum;
import com.dt.module.ct.entity.CtContent;
import com.dt.module.ct.service.ICtContentService;;

/**
 * @author: algernonking
 * @date: 2017年8月11日 下午1:00:33
 * @Description: TODO pageSize pageIndex sdate edate sort
 */
@Controller
@RequestMapping(value = "/api")
public class NewsController extends BaseController {

	@Autowired
	ICtContentService CtContentServiceImpl;

	@ResponseBody
	@Acl(info = "根据Id查询", value = Acl.ACL_ALLOW)
	@RequestMapping(value = "/news/queryNewsById.do")
	public R selectById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
		return R.SUCCESS_OPER(CtContentServiceImpl.getById(id));
	}

	/**
	 * @Description: 删除新闻
	 */
	@RequestMapping(value = "/news/deleteNews.do")
	@ResponseBody
	@Acl(value = Acl.ACL_DENY, info = "删除新闻")
	public R deleteNews(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
		return R.SUCCESS_OPER(CtContentServiceImpl.removeById(id));
	}

	/**
	 * @Description: 发布新闻
	 */
	@RequestMapping(value = "/news/publishNews.do")
	@ResponseBody
	@Acl(value = Acl.ACL_DENY, info = "发布新闻")
	public R publishNews(CtContent entity) {
		entity.setType(ctCtTypeEnum.NEWS.getValue().toString());
		return R.SUCCESS_OPER(CtContentServiceImpl.saveOrUpdate(entity));
	}

	/**
	 * @Description: 查询新闻分页,用于datatable
	 */
	@RequestMapping(value = "/news/queryNewsByPage.do")
	@ResponseBody
	@Acl(value = Acl.ACL_ALLOW, info = "分页查询新闻")
	public R queryNewsByDatatable(String start, String length, String pageSize, String pageIndex) {

		JSONObject respar = DbUtil.formatPageParameter(start, length, pageSize, pageIndex);
		if (ToolUtil.isEmpty(respar)) {
			return R.FAILURE_REQ_PARAM_ERROR();
		}
		int pagesize = respar.getIntValue("pagesize");
		int pageindex = respar.getIntValue("pageindex");
		QueryWrapper<CtContent> ew = new QueryWrapper<CtContent>();
		ew.and(i -> i.eq("type", ctCtTypeEnum.NEWS.getValue()));
		ew.orderByAsc("create_time");
		IPage<CtContent> pdata = CtContentServiceImpl.page(new Page<CtContent>(pageindex, pagesize), ew);
		JSONObject retrunObject = new JSONObject();
		retrunObject.put("iTotalRecords", pdata.getTotal());
		retrunObject.put("iTotalDisplayRecords", pdata.getTotal());
		retrunObject.put("data", JSONArray.parseArray(JSON.toJSONString(pdata.getRecords(),
				SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect)));
		return R.clearAttachDirect(retrunObject);

	}
}
