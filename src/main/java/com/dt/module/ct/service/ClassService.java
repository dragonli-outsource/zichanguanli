package com.dt.module.ct.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.SQL;
import com.dt.core.dao.sql.Update;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ToolUtil;

/**
 * @author: algernonking
 * @date: 2017年11月17日 上午8:19:03
 * @Description: 单独分类的服务
 */
@Service
public class ClassService extends BaseService {

	public static String CLASS_TYPE_MICROSHOP_INDEX = "microshopindex";
	public static String MODULE_TYPE_MALL = "mall";

	public R addClass(TypedHashMap<String, Object> ps, String type) {
		Insert me = new Insert("sys_ct_class");
		me.set("class_id", db.getUUID());
		me.set("is_delete", "N");
		me.setIf("type", type);
		me.setIf("name", ps.getString("name"));
		me.setIf("is_used", ps.getString("is_used"));
		me.setIf("pic_id", ps.getString("pic_id"));
		me.setIf("od", ps.getString("od"));
		me.setIf("mark", ps.getString("mark"));
		me.setIf("module", ps.getString("module"));
		me.setIf("status", ps.getString("status"));
	    me.setIf("col_a", ps.getString("col_a"));
        me.setIf("col_b", ps.getString("col_b"));
        me.setIf("col_c", ps.getString("col_c"));
        me.setIf("col_d", ps.getString("col_d"));
        me.setIf("col_e", ps.getString("col_e"));
		db.execute(me);
		return R.SUCCESS_OPER();
	}

	public R deleteClass(String class_id) {
		Update me = new Update("sys_ct_class");
		me.set("is_delete", "Y");
		me.where().and("class_id=?", class_id);
		db.execute(me);
		return R.SUCCESS_OPER();
	}

	public R updateClass(TypedHashMap<String, Object> ps, String type) {
		Update me = new Update("sys_ct_class");
		me.setIf("name", ps.getString("name"));
		me.setIf("pic_id", ps.getString("pic_id"));
		me.setIf("is_used", ps.getString("is_used"));
		me.setIf("od", ps.getString("od"));
		me.setIf("mark", ps.getString("mark"));
		me.setIf("module", ps.getString("module"));
		me.setIf("status", ps.getString("status"));
	    me.setIf("col_a", ps.getString("col_a"));
        me.setIf("col_b", ps.getString("col_b"));
        me.setIf("col_c", ps.getString("col_c"));
        me.setIf("col_d", ps.getString("col_d"));
        me.setIf("col_e", ps.getString("col_e"));
		me.where().and("class_id=?", ps.getString("class_id")).and("type=?", type);
		db.execute(me);
		return R.SUCCESS_OPER();
	}

	/* type必须有 */
	public R queryClass(String type, String is_used, String status) {
		String sql = "select * from sys_ct_class where dr='0' ";
		if (ToolUtil.isEmpty(type)) {
			type = "";
		}
		sql += " and type='" + type + "' ";

		if (ToolUtil.isNotEmpty(is_used)) {
			sql += " and is_used='" + is_used + "' ";
		}
		if (ToolUtil.isNotEmpty(status)) {
			sql += " and status='" + status + "' ";
		}

		sql += " order by od";
		return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
	}

	/* type,module必须有 */
	public R queryClassWithModule(String type, String is_used, String module, String status) {
		String sql = "select * from sys_ct_class where dr='0' ";
		if (ToolUtil.isEmpty(type)) {
			type = "";
		}
		sql += " and type='" + type + "' ";

		if (ToolUtil.isEmpty(module)) {
			module = "";
		}
		sql += " and module='" + module + "' ";

		if (ToolUtil.isNotEmpty(is_used)) {
			sql += " and is_used='" + is_used + "' ";
		}
		if (ToolUtil.isNotEmpty(status)) {
			sql += " and status='" + status + "' ";
		}
		sql += " order by od";
		return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
	}

	public R queryClassById(String class_id) {

		Rcd rs = db.uniqueRecord("select * from sys_ct_class where dr='0' and class_id=? order by od", class_id);
		if (ToolUtil.isEmpty(rs)) {
			return R.FAILURE_NO_DATA();
		} else {
			return R.SUCCESS_OPER(rs.toJsonObject());
		}
	}

	public R addClassItem(TypedHashMap<String, Object> ps) {
		Insert me = new Insert("sys_ct_class_item");
		me.set("id", db.getUUID());
		me.setIf("class_id", ps.getString("class_id"));
		me.setIf("is_used", ps.getString("is_used"));
		me.setIf("value", ps.getString("value"));
		me.setIf("od", ps.getString("od"));
		me.setIf("mark", ps.getString("mark"));
		me.setIf("status", ps.getString("status"));
		me.setIf("is_delete", "N");
	    me.setIf("col_a", ps.getString("col_a"));
        me.setIf("col_b", ps.getString("col_b"));
        me.setIf("col_c", ps.getString("col_c"));
        me.setIf("col_d", ps.getString("col_d"));
        me.setIf("col_e", ps.getString("col_e"));
		me.setIf("pic_id", ps.getString("pic_id"));
		db.execute(me);
		return R.SUCCESS_OPER();
	}

	public R addClassItems(TypedHashMap<String, Object> ps) {
		// IDS
		String ids = ps.getString("ids");
		if (ToolUtil.isEmpty(ids)) {
			return R.FAILURE_REQ_PARAM_ERROR();
		}
		JSONArray idsarr = (JSONArray) JSONArray.parse(ids);
		List<SQL> sqls = new ArrayList<SQL>();
		for (int i = 0; i < idsarr.size(); i++) {
			Insert me = new Insert("sys_ct_class_item");
			me.set("id", db.getUUID());
			me.setIf("class_id", ps.getString("class_id"));
			me.setIf("is_delete", "N");
			me.setIf("is_used", "Y");
			me.setIf("value", idsarr.getString(i));
			me.setIf("od", ps.getString("od"));
			me.setIf("mark", ps.getString("mark"));
		    me.setIf("col_a", ps.getString("col_a"));
	        me.setIf("col_b", ps.getString("col_b"));
	        me.setIf("col_c", ps.getString("col_c"));
	        me.setIf("col_d", ps.getString("col_d"));
	        me.setIf("col_e", ps.getString("col_e"));
			me.setIf("status", ps.getString("status"));
			me.setIf("pic_id", ps.getString("pic_id"));
			sqls.add(me);
		}
		if (sqls.size() > 0) {
			db.executeSQLList(sqls);
		}

		return R.SUCCESS_OPER();
	}

	public R deleteClassItem(String id) {
		Update me = new Update("sys_ct_class_item");
		me.set("is_delete", "Y");
		me.where().and("id=?", id).execute();
		db.execute(me);
		return R.SUCCESS_OPER();
	}

	public R updateClassItem(TypedHashMap<String, Object> ps) {
		Update me = new Update("sys_ct_class_item");
		me.setIf("is_used", ps.getString("is_used"));
		me.setIf("status", ps.getString("status"));
		me.setIf("value", ps.getString("value"));
		me.setIf("od", ps.getString("od"));
	    me.setIf("col_a", ps.getString("col_a"));
        me.setIf("col_b", ps.getString("col_b"));
        me.setIf("col_c", ps.getString("col_c"));
        me.setIf("col_d", ps.getString("col_d"));
        me.setIf("col_e", ps.getString("col_e"));
		me.setIf("pic_id", ps.getString("pic_id"));
		me.setIf("mark", ps.getString("mark"));
		db.execute(me);
		return R.SUCCESS_OPER();
	}

	public R queryClassItem(String class_id, String is_used) {

		String sql = "select * from sys_ct_class_item where dr='0'";
		if (ToolUtil.isNotEmpty(class_id)) {
			sql += " and class_id='" + class_id + "' ";
		}
		if (ToolUtil.isNotEmpty(is_used)) {
			sql += " and is_used='" + is_used + "' ";
		}
		sql = sql + " order by od";
		return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
	}

	public R queryClassItemById(String id) {
		Rcd rs = db.uniqueRecord("select * from sys_ct_class_item where id=?", id);
		if (ToolUtil.isEmpty(rs)) {
			return R.FAILURE_NO_DATA();
		} else {
			return R.SUCCESS_OPER(rs.toJsonObject());
		}

	}
}
