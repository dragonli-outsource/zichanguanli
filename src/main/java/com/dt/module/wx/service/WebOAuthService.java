package com.dt.module.wx.service;

import org.springframework.stereotype.Service;

import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.Update;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ToolUtil;

/**
 * @author: algernonking
 * @date: 2018年5月21日 下午1:20:32
 * @Description: TODO
 */
@Service
public class WebOAuthService extends BaseService {
	public R queryWebOAuth() {
		String sql = "select * from wx_web_auth where dr=0";
		return R.SUCCESS_OPER(db.query(sql).toJsonArrayWithJsonObject());
	}

	public R delWebOAuth(String id) {
		Update me = new Update("wx_web_auth");
		me.setIf("dr", 1);
		me.where().and("id=?", id);
		db.execute(me);
		return R.SUCCESS_OPER();
	}

	public R queryWebOAuthById(String id) {
		String sql = "select * from wx_web_auth where dr=0 and id=?";
		return R.SUCCESS_OPER(db.uniqueRecord(sql, id).toJsonObject());
	}

	public R updateWebOAuth(TypedHashMap<String, Object> ps) {
		String state = ps.getString("state");
		String id = ps.getString("id");
		if (ToolUtil.isOneEmpty(state, id)) {
			return R.FAILURE_NO_PERMITION();
		}
		Rcd ck = db.uniqueRecord("select * from wx_web_auth where id<>? and state=?", id, state);
		if (ck != null) {
			return R.FAILURE("state 已存在");
		}
		Update me = new Update("wx_web_auth");
		me.setIf("name", ps.getString("name", ""));
		me.setIf("value", ps.getString("value", ""));
		me.setIf("login", ps.getString("login", ""));
		me.setIf("state", ps.getString("state"));
		me.where().and("id=?", ps.getString("id", ""));
		db.execute(me);
		return R.SUCCESS_OPER();
	}

	public R queryWebOAuthByStateCanAdd(String state) {
		if (ToolUtil.isEmpty(state)) {
			return R.FAILURE_NO_DATA();
		}
		Rcd rs = db.uniqueRecord("select * from wx_web_auth where state=?", state);
		if (ToolUtil.isEmpty(rs)) {
			return R.SUCCESS_OPER(state);
		} else {
			return R.FAILURE("state已经存在");
		}

	}

	public R addWebOAuth(TypedHashMap<String, Object> ps) {
		String state = ps.getString("state");
		R ck = queryWebOAuthByStateCanAdd(state);
		if (ck.isFailed()) {
			return ck;
		}
		Insert me = new Insert("wx_web_auth");
		me.setIf("name", ps.getString("name", ""));
		me.setIf("value", ps.getString("value", ""));
		me.setIf("login", ps.getString("login", ""));
		me.setIf("state", ps.getString("state"));
		me.setIf("id", db.getUUID());
		me.set("dr", 0);
		db.execute(me);
		return R.SUCCESS_OPER();
	}
}
