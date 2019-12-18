package com.dt.module.cmdb.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.cmdb.entity.DictItemEntity;
import com.dt.module.cmdb.entity.ResEntity;
import com.dt.module.cmdb.service.impl.ResExtService;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;

/**
 * @author: algernonking
 * @date: Oct 21, 2019 7:28:06 PM
 * @Description: TODO
 */
@Controller
@RequestMapping("/api/base/res")
public class ResExtExportController extends BaseController {

	@Autowired
	ResExtService resExtService;

	@RequestMapping("/exportDictItems.do")
	@Acl(value = Acl.ACL_USER)
	public void exportDictItems(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {

		RcdSet rs = db.query("select b.name,a.name item_name from sys_dict_item a ,sys_dict b\n"
				+ "where  a.dict_id=b.dict_id and a.dr='0' and b.dr='0'\n" + "union all\n"
				+ "select '资产类型明细' name, route_name item_name from ct_category where root='3'\n" + "order by 1,2");

		List<DictItemEntity> data_excel = new ArrayList<DictItemEntity>();
		for (int i = 0; i < rs.size(); i++) {
			DictItemEntity entity = new DictItemEntity();
			entity.fullResEntity(ConvertUtil.OtherJSONObjectToFastJSONObject(rs.getRcd(i).toJsonObject()));
			data_excel.add(entity);
		}

		ExportParams parms = new ExportParams();
		parms.setSheetName("数据字典项");
		parms.setHeaderHeight(1000);

		Workbook workbook;
		workbook = ExcelExportUtil.exportExcel(parms, DictItemEntity.class, data_excel);
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-download");
		String filedisplay = "dictItem.xls";
		filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
		try {
			OutputStream out = response.getOutputStream();
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping("/exportAllRes.do")
	@Acl(value = Acl.ACL_USER)
	public void exportAllRes(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {

		TypedHashMap<String, Object> ps = (TypedHashMap<String, Object>) HttpKit.getRequestParameters();

		R res = resExtService.queryResAllGetData(ps.getString("id"), ps.getString("wb"), ps.getString("env"),
				ps.getString("recycle"), ps.getString("loc"), ps.getString("search"));

		JSONArray data = res.queryDataToJSONArray();
		List<ResEntity> data_excel = new ArrayList<ResEntity>();
		for (int i = 0; i < data.size(); i++) {
			ResEntity entity = new ResEntity();
			entity.fullResEntity(data.getJSONObject(i));
			data_excel.add(entity);
		}

		ExportParams parms = new ExportParams();
		parms.setSheetName("数据");
		parms.setHeaderHeight(1000);

		Workbook workbook;
		workbook = ExcelExportUtil.exportExcel(parms, ResEntity.class, data_excel);
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-download");
		String filedisplay = "file.xls";
		filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
		try {
			OutputStream out = response.getOutputStream();
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping("/exportServerData.do")
	@Acl(value = Acl.ACL_USER)
	public void exportServerData(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {

		TypedHashMap<String, Object> ps = (TypedHashMap<String, Object>) HttpKit.getRequestParameters();

		R res = resExtService.queryResAllGetData(ps.getString("id"), ps.getString("wb"), ps.getString("env"),
				ps.getString("recycle"), ps.getString("loc"), ps.getString("search"));

		JSONArray data = res.queryDataToJSONArray();
		List<ResEntity> data_excel = new ArrayList<ResEntity>();
		for (int i = 0; i < data.size(); i++) {
			ResEntity entity = new ResEntity();
			entity.fullResEntity(data.getJSONObject(i));
			data_excel.add(entity);
		}

		ExportParams parms = new ExportParams();
		parms.setSheetName("数据");
		parms.setHeaderHeight(1000);

		Workbook workbook;
		workbook = ExcelExportUtil.exportExcel(parms, ResEntity.class, data_excel);
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-download");
		String filedisplay = "file.xls";
		filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
		response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
		try {
			OutputStream out = response.getOutputStream();
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
