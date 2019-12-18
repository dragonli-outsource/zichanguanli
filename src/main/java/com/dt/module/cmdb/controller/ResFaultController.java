package com.dt.module.cmdb.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.Update;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.cmdb.service.impl.ResExtService;

/**
 * @author: algernonking
 * @date: Oct 30, 2019 5:42:36 AM
 * @Description: TODO
 */
@Controller
@RequestMapping("/api/base/res/")
public class ResFaultController extends BaseController {

	@Autowired
	ResExtService resExtService;

	@ResponseBody
	@Acl(info = "", value = Acl.ACL_ALLOW)
	@RequestMapping(value = "/queryAllResFault.do")
	public R queryAllResFault(String search) {

		String sql = "select a.id f_id, (select count(1) from res_fault_file where faultid=a.id) file_cnt,"
				+ ResExtService.resSqlbody + " t.*,DATE_FORMAT(a.create_time,'%Y-%m-%d %T') f_create_time ,a.*\n"
				+ " from res_fault a,res t where a.f_res_id=t.id and a.dr='0' and t.dr='0' order by a.create_time desc";

		return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
	}

	@ResponseBody
	@Acl(info = "", value = Acl.ACL_USER)
	@RequestMapping(value = "/removefault.do")
	public R removefault(String id) {
		if (ToolUtil.isEmpty(id)) {
			return R.FAILURE_REQ_PARAM_ERROR();
		}
		Update me = new Update("res_fault");
		me.set("dr", "1");
		me.where().and("id=?", id);
		db.execute(me);
		return R.SUCCESS_OPER();
	}

	@ResponseBody
	@Acl(info = "", value = Acl.ACL_USER)
	@RequestMapping(value = "/savefault.do")
	@Transactional
	public R addfault(String id, String f_res_id, String f_reason, String f_mark, String files, String f_processtime,
			String f_processuser) {

		if (ToolUtil.isOneEmpty(f_res_id, f_reason)) {
			return R.FAILURE_REQ_PARAM_ERROR();
		}

		Date date = new Date(); // 获取一个Date对象
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 创建一个格式化日期对象
		String nowtime = simpleDateFormat.format(date);

		Insert ins = new Insert("res_fault");
		String uid = db.getUUID();
		ins.set("id", uid);
		ins.set("f_res_id", f_res_id);
		ins.set("f_uuid", resExtService.createUuid(ResExtService.UUID_BX));
		ins.setIf("f_mark", f_mark);
		ins.setIf("f_reason", f_reason);
		ins.setIf("f_oper_user", this.getUserId());
		ins.setIf("f_oper_time", nowtime);
		ins.setIf("f_processtime", f_processtime);
		ins.setIf("f_processuser", f_processuser);

		ins.setIf("dr", "0");
		ins.setIf("update_time", nowtime);
		ins.setIf("update_by", this.getUserId());
		ins.setIf("create_by", this.getUserId());
		ins.setIf("create_time", nowtime);

		db.execute(ins);
		if (ToolUtil.isNotEmpty(files)) {
			String[] files_arr = files.split("#");
			for (int i = 0; i < files_arr.length; i++) {
				Insert me = new Insert("res_fault_file");
				me.set("id", db.getUUID());
				me.setIf("faultid", uid);
				me.setIf("fileid", files_arr[i]);
				db.execute(me);
			}
		}
		return R.SUCCESS_OPER();
	}

}
