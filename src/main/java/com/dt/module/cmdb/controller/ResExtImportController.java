package com.dt.module.cmdb.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.module.base.controller.FileUpDownController;
import com.dt.module.cmdb.service.impl.ResExtService;
import com.dt.module.cmdb.service.impl.ResImportService;

/**
 * @author: algernonking
 * @date: Oct 21, 2019 7:28:06 PM
 * @Description: TODO
 */
@Controller
@RequestMapping("/api/base/res")
public class ResExtImportController extends BaseController {

	@Autowired
	ResExtService resExtService;

	@Autowired
	ResImportService resImportService;

	@RequestMapping("/importResData.do")
	@Acl(value = Acl.ACL_USER)
	@ResponseBody
	public R importResData(String type, String id, HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String sql = "select * from sys_files where id=?";
		Rcd set = db.uniqueRecord(sql, id);
		String fileurl = set.getString("path");
		String filePath = FileUpDownController.getWebRootDir() + ".." + File.separatorChar + fileurl;
		return resImportService.importResNormal(filePath, type);

	}

}
