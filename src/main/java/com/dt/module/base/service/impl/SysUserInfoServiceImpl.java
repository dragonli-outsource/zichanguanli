package com.dt.module.base.service.impl;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dt.core.common.base.BaseCommon;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.sql.Update;
import com.dt.core.tool.encrypt.MD5Util;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.bus_enum.userTypeEnum;
import com.dt.module.base.entity.SysMenus;
import com.dt.module.base.entity.SysUserInfo;
import com.dt.module.base.entity.SysUserReceivingaddr;
import com.dt.module.base.entity.UserShiro;
import com.dt.module.base.mapper.SysUserInfoMapper;
import com.dt.module.base.service.ISysUserInfoService;
import com.dt.module.base.service.ISysUserReceivingaddrService;
import com.dt.module.db.DB;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author algernonking
 * @since 2018-07-24
 */
@Service
public class SysUserInfoServiceImpl extends ServiceImpl<SysUserInfoMapper, SysUserInfo> implements ISysUserInfoService {

	@Autowired
	DB db;

	@Autowired
	ISysUserReceivingaddrService SysUserReceivingaddrServiceImpl;
	private static HashMap<String, JSONArray> userMenus = new HashMap<String, JSONArray>();
	private static Logger _log = LoggerFactory.getLogger(SysUserInfoServiceImpl.class);

	// 显示我的菜单
	public R saveDefMenus(String user_id, String id) {
		if (ToolUtil.isOneEmpty(user_id, id)) {
			return R.FAILURE_REQ_PARAM_ERROR();
		}
		UpdateWrapper<SysUserInfo> ew = new UpdateWrapper<SysUserInfo>();
		ew.and(i -> i.eq("user_id", user_id));
		SysUserInfo user = new SysUserInfo();

		user.setSystemId(id);
		baseMapper.update(user, ew);

		return R.SUCCESS_OPER();
	}

	// 强制修改密码
	public R changeUserPwdForce(String user_id, String pwd1, String pwd2) {
		if (ToolUtil.isOneEmpty(pwd1, pwd2)) {
			return R.FAILURE_REQ_PARAM_ERROR();
		}
		if (!pwd1.equals(pwd2)) {
			return R.FAILURE("密码输入不一致");
		}
		UpdateWrapper<SysUserInfo> ew = new UpdateWrapper<SysUserInfo>();
		ew.and(i -> i.eq("user_id", user_id));
		SysUserInfo user = new SysUserInfo();
		user.setPwd(pwd1);
		baseMapper.update(user, ew);
		return R.SUCCESS_OPER();
	}

	// 显示我的菜单
	public List<SysMenus> listMyMenus(String user_id) {
		return this.baseMapper.listMyMenus(user_id);
	}

	@Override
	public SysUserInfo selectOneByEmpl(String empl) {
		// TODO Auto-generated method stub
		QueryWrapper<SysUserInfo> ew = new QueryWrapper<SysUserInfo>();
		ew.and(i -> i.eq("empl_id", empl));
		return baseMapper.selectOne(ew);

	}

	// 修改用户密码
	public R modifyPassword(String user_id, String pwd) {
		if (ToolUtil.isOneEmpty(user_id, pwd)) {
			return R.FAILURE_REQ_PARAM_ERROR();
		}
		UpdateWrapper<SysUserInfo> ew = new UpdateWrapper<SysUserInfo>();
		ew.and(i -> i.eq("user_id", user_id));
		SysUserInfo user = new SysUserInfo();
		user.setPwd(pwd);
		baseMapper.update(user, ew);

		return R.SUCCESS_OPER();

	}

	/*
	 * @see
	 * com.dt.module.base.service.ISysUserInfoService#listByOpenId(java.lang.String)
	 */
	@Override
	public R selectByOpenId(String open_id) {
		// TODO Auto-generated method stub
		if (ToolUtil.isEmpty(open_id)) {
			return R.FAILURE_REQ_PARAM_ERROR();
		}
		QueryWrapper<SysUserInfo> ew = new QueryWrapper<SysUserInfo>();
		ew.and(i -> i.eq("open_id", open_id));
		Integer c = baseMapper.selectCount(ew);
		if (c == 1) {
			return R.SUCCESS_OPER(baseMapper.selectOne(ew));
		} else if (c == 0) {
			return R.FAILURE("不存在");
		} else {
			return R.FAILURE("存在多个");
		}

	}

	/*
	 * (non Javadoc)
	 * 
	 * @see com.dt.module.base.service.ISysUserInfoService#listUserRoles(java.lang.
	 * String)
	 */
	@Override
	public List<HashMap<String, Object>> listUserRoles(String user_id) {
		// TODO Auto-generated method stub
		System.out.println(this.baseMapper.listUserRoles(user_id));
		return this.baseMapper.listUserRoles(user_id);
	}

	/*
	 * 
	 * com.dt.module.base.service.ISysUserInfoService#addUser(com.dt.module.base.
	 * entity.SysUserInfo)
	 */
	@Override
	public R addUser(SysUserInfo user) {
		// TODO Auto-generated method stub
		String user_type = user.getUserType();

		// 判断用户类型
		if (ToolUtil.isEmpty(user_type)) {
			return R.FAILURE("请选择用户类型");
		}

		// 如果密码为空,则随机
		if (ToolUtil.isEmpty(user.getPwd())) {
			user.setPwd(ToolUtil.getUUID());
		}

//		// 如果组织ID为空，则随机
//		if (ToolUtil.isEmpty(user.getEmplId())) {
//			user.setEmplId(ToolUtil.getUUID());
//		}

		// 如果用户名称为空，则随机，用户名称username字段不能重复
		if (ToolUtil.isEmpty(user.getUserName())) {
			user.setUserName(ToolUtil.getUUID());
		} else {
			QueryWrapper<SysUserInfo> queryWrapper = new QueryWrapper<SysUserInfo>();
			queryWrapper.eq("user_name", user.getUserName());
			int c = this.baseMapper.selectCount(queryWrapper);
			if (c > 0) {
				return R.FAILURE("登录名重复,请重新输入");
			}
		}
		String emplId = ToolUtil.getUUID();
		if (userTypeEnum.SYSTEM.getValue().equals(user_type)) {
			// 无动作
		} else if (userTypeEnum.EMPL.getValue().equals(user_type)) {

			R r = getEmplNextId();
			if (r.isFailed()) {
				return r;
			}
			emplId = r.getData().toString();
		} else if (userTypeEnum.CRM.getValue().equals(user_type)) {
		} else if (userTypeEnum.WX.getValue().equals(user_type)) {
		}

		user.setEmplId(emplId);

		baseMapper.insert(user);
		QueryWrapper<SysUserInfo> queryWrapper = new QueryWrapper<SysUserInfo>();
		queryWrapper.eq("empl_id", emplId);
		return R.SUCCESS_OPER(baseMapper.selectOne(queryWrapper));

	}

	public R getEmplNextId() {

		Rcd seqrs = db.uniqueRecord(
				"select case when value is null then '50' else value end seq from sys_params where id='sys_empl_no'");
		if (ToolUtil.isEmpty(seqrs)) {
			return R.FAILURE("获取员工编号错误,无法生成员工.");
		}
		String empl_id = (ConvertUtil.toInt(seqrs.getString("seq")) + 1) + "";
		Update me = new Update("sys_params");
		me.set("value", empl_id);
		me.where().and("id=?", "sys_empl_no");
		db.execute(me);
		return R.SUCCESS_OPER(ConvertUtil.formatIntToString(empl_id, 6, 100));
	}

	// @Cacheable(value = CacheConfig.CACHE_USER_180_60, key =
	// "'user_menu_'+#user_id+#menu_id")
	public JSONArray listMyMenusById(String user_id, String menu_id) {
		// 获得所有tree的node,限制3层
		String mflag = MD5Util.encrypt(user_id + menu_id);
		if (userMenus.containsKey(mflag)) {
			_log.info("get menus from map");
		}
		String basesql = "";
		if (BaseCommon.isSuperAdmin(user_id)) {
			basesql = "select * from sys_menus_node where dr='0' and type <>'btn' and menu_id='" + menu_id
					+ "' and parent_id = ? order by sort";
		} else {
			if (db.getDBType().equals(DbUtil.TYPE_ORACLE)) {
				basesql = " select distinct level1 node_id                                                 "
						+ "   from (select *                                                               "
						+ "           from (select b.module_id,                                            "
						+ "                        c.route,                                                "
						+ "                        c.node_name,                                            "
						+ "                        decode(instr(route, '-'),                               "
						+ "                               0,                                               "
						+ "                               route,                                           "
						+ "                               substr(route, 1, instr(route, '-') - 1)) level1  "
						+ "                   from sys_user_role a, sys_role_module b, sys_menus_node c    "
						+ "                  where c.node_id = b.module_id  and c.type <>'btn'             "
						+ "                    and a.role_id = b.role_id                                   "
						+ "                    and user_id = '<#USER_ID#>')                                "
						+ "         union all                                                              "
						+ "         select *                                                               "
						+ "           from (select b.module_id,                                            "
						+ "                        c.route,                                                "
						+ "                        c.node_name,                                            "
						+ "                        decode(length(route) - length(replace(route, '-', '')), "
						+ "                               0,                                               "
						+ "                               '-1',                                            "
						+ "                               1,                                               "
						+ "                               substr(route,                                    "
						+ "                                      instr(route, '-', 1, 1) + 1,              "
						+ "                                      length(route) - instr(route, '-', 1, 1)), "
						+ "                               substr(route,                                    "
						+ "                                      instr(route, '-', 1, 1) + 1,              "
						+ "                                      instr(route, '-', 1, 2) -                 "
						+ "                                      instr(route, '-', 1, 1) - 1)) level2      "
						+ "                   from sys_user_role a, sys_role_module b, sys_menus_node c    "
						+ "                  where c.node_id = b.module_id                                 "
						+ "                    and a.role_id = b.role_id  and  and c.type <>'btn'          "
						+ "                    and user_id = '<#USER_ID#>')                                "
						+ "         union all                                                              "
						+ "         select *                                                               "
						+ "           from (select b.module_id,                                            "
						+ "                        c.route,                                                "
						+ "                        c.node_name,                                            "
						+ "                        decode(length(route) - length(replace(route, '-', '')), "
						+ "                               0,                                               "
						+ "                               '-1',                                            "
						+ "                               1,                                               "
						+ "                               '-1',                                            "
						+ "                               2,                                               "
						+ "                               substr(route,                                    "
						+ "                                      instr(route, '-', 1, 2) + 1,              "
						+ "                                      length(route) - instr(route, '-', 1, 2)), "
						+ "                               substr(route,                                    "
						+ "                                      instr(route, '-', 1, 2) + 1,              "
						+ "                                      instr(route, '-', 1, 3) -                 "
						+ "                                      instr(route, '-', 1, 2) - 1)) level3      "
						+ "                   from sys_user_role a, sys_role_module b, sys_menus_node c    "
						+ "                  where c.node_id = b.module_id                                 "
						+ "                    and a.role_id = b.role_id   and c.type <>'btn'              "
						+ "                    and user_id = '<#USER_ID#>'))                               "
						+ "  where level1 <> '-1'";
			} else if (db.getDBType().equals(DbUtil.TYPE_MYSQL)) {
				// instr(route, '-', 1, 1) 用 locate('-',route) 替换
				// instr(route, '-', 1, 2) 用case when
				// substring_index(route,'-',3)=substring_index(route,'-',2)then
				// 0 else length(substring_index(route,'-',2))+1 end 替换
				// instr(route, '-', 1, 3) 用case when
				// substring_index(route,'-',4)=substring_index(route,'-',3)then
				// 0 else length(substring_index(route,'-',3))+1 end

				basesql = "select distinct level1 node_id " + "from (select * " + "from (select b.module_id, "
						+ "c.route, " + "c.node_name, " + "case instr(route, '-') " + "when 0 then route " + "else "
						+ "substr(route, 1, instr(route, '-') - 1) " + "end level1 "
						+ "from sys_user_role a, sys_role_module b, sys_menus_node c "
						+ "where c.node_id = b.module_id " + "and a.role_id = b.role_id "
						+ "and user_id = '<#USER_ID#>') a " + "union all " + "select * " + "from ( " + " "
						+ "select b.module_id, " + "c.route, " + "c.node_name, "
						+ "case length(route) - length(replace(route, '-', '')) " + "when 0 then '-1' " + "when 1 then "
						+ "substr(route, " + "locate('-',route)+ 1, " + "length(route) - locate('-',route)) " + "else "
						+ "substr(route, " + "locate('-',route) + 1, "
						+ "case when substring_index(route,'-',3)=substring_index(route,'-',2)then 0 else length(substring_index(route,'-',2))+1 end "
						+ "- " + "locate('-',route) - 1) " + "end level2 "
						+ "from sys_user_role a, sys_role_module b, sys_menus_node c "
						+ "where c.node_id = b.module_id " + "and a.role_id = b.role_id "
						+ "and user_id = '<#USER_ID#>' " + ")  b " + "union all " + "select * " + "from ( "
						+ "select b.module_id, " + "c.route, " + "c.node_name, "
						+ "case length(route) - length(replace(route, '-', '')) " + "when  0 then '-1' "
						+ "when 1 then '-1' " + "when 2 then " + "substr(route, "
						+ "case when substring_index(route,'-',3)=substring_index(route,'-',2)then 0 else length(substring_index(route,'-',2))+1 end + 1, "
						+ "length(route) - case when substring_index(route,'-',3)=substring_index(route,'-',2)then 0 else length(substring_index(route,'-',2))+1 end) "
						+ "else " + "substr(route, "
						+ "case when substring_index(route,'-',3)=substring_index(route,'-',2)then 0 else length(substring_index(route,'-',2))+1 end + 1, "
						+ "case when substring_index(route,'-',4)=substring_index(route,'-',3)then 0 else length(substring_index(route,'-',3))+1 end - "
						+ "case when substring_index(route,'-',3)=substring_index(route,'-',2)then 0 else length(substring_index(route,'-',2))+1 end - 1) end level3 "
						+ "from sys_user_role a, sys_role_module b, sys_menus_node c "
						+ "where c.type<>'btn' and c.node_id = b.module_id " + "and a.role_id = b.role_id "
						+ "and user_id = '<#USER_ID#>' " + ") c) d " + "where level1 <> '-1'";
			}

			basesql = "select a.* from sys_menus_node a, (" + basesql + ") b "
					+ "where a.type<>'btn' and a.dr='0' and a.node_id = b.node_id and menu_id = '" + menu_id
					+ "' and parent_id = ? " + "order by sort ";
			basesql = basesql.replaceAll("<#USER_ID#>", user_id);

		}
		_log.info("getMenu sql:" + basesql + ",menu_id:" + menu_id);
		String btnsql = "select keyvalue p from sys_menus_node where dr='0' and parent_id=? and type='btn'\n"
				+ "and node_id in (select module_id from sys_user_role a,sys_role_module b  where a.user_id=? and a.role_id=b.role_id)\n";

		JSONArray r = new JSONArray();
		RcdSet first_rs = db.query(basesql, 0);
		for (int i = 0; i < first_rs.size(); i++) {
			// 处理第一层数据
			JSONObject first_obj = ConvertUtil.OtherJSONObjectToFastJSONObject(first_rs.getRcd(i).toJsonObject());
			// _log.info("显示第一层菜单数据:\n" + first_obj);
			String first_key = first_rs.getRcd(i).getString("keyvalue");
			first_obj.put("state", first_key);
			int second_pid = first_rs.getRcd(i).getInteger("node_id");
			RcdSet second_rs = db.query(basesql, second_pid);
			JSONArray second_arr = new JSONArray();
			for (int j = 0; j < second_rs.size(); j++) {
				// 处理第二层数据
				JSONObject second_obj = ConvertUtil.OtherJSONObjectToFastJSONObject(second_rs.getRcd(j).toJsonObject());
				// _log.info("显示第二层菜单数据:\n" + second_obj);
				String second_key = second_rs.getRcd(j).getString("keyvalue");
				// 菜单显示控制
				second_obj.put("state", first_key + "." + second_key);
				int third_pid = second_rs.getRcd(j).getInteger("node_id");
				RcdSet third_rs = db.query(basesql, third_pid);
				second_obj.put("children_cnt", third_rs.size());
				JSONArray third_arr = ConvertUtil.OtherJSONObjectToFastJSONArray(third_rs.toJsonArrayWithJsonObject());
				for (int f = 0; f < third_arr.size(); f++) {
					// _log.info("显示第三层菜单数据:\n" + third_arr);
					// 菜单显示控制
					third_arr.getJSONObject(f).put("state",
							first_key + "." + second_key + "." + third_arr.getJSONObject(f).getString("keyvalue"));
				}
				second_obj.put("btn_cnt", 0);
				if ("menu".equals(second_rs.getRcd(j).getString("type"))) {
					RcdSet second_btn_rs = db.query(btnsql, second_rs.getRcd(j).getString("node_id"), user_id);
					second_obj.put("btn_cnt", second_btn_rs.size());
					second_obj.put("btn",
							ConvertUtil.OtherJSONObjectToFastJSONArray(second_btn_rs.toJsonArrayWithJsonObject()));
				}

				second_obj.put("children", third_arr);
				second_arr.add(second_obj);
			}
			first_obj.put("children_cnt", second_rs.size());
			first_obj.put("children", second_arr);
			first_obj.put("btn_cnt", 0);
			if ("menu".equals(first_rs.getRcd(i).getString("type"))) {
				RcdSet first_btn_rs = db.query(btnsql, first_rs.getRcd(i).getString("node_id"), user_id);
				first_obj.put("btn_cnt", first_btn_rs.size());
				first_obj.put("btn",
						ConvertUtil.OtherJSONObjectToFastJSONArray(first_btn_rs.toJsonArrayWithJsonObject()));
			}
			r.add(first_obj);
		}
		userMenus.put(mflag, r);
		return r;
	}

	/*
	 * @Description: 获得用户信息
	 */
	public UserShiro listUserForShiro(String user_id) {
		UserShiro user = new UserShiro();
		String sql = "select * from sys_user_info a where a.user_id=?";
		// 账号状态信息
		Rcd u_rs = db.uniqueRecord(sql, user_id);
		user.setUserId(u_rs.getString("user_id"));
		user.setPassword(u_rs.getString("pwd"));
		user.setAccount(u_rs.getString("user_name"));
		user.setName(u_rs.getString("user_name"));
		user.setSalt(MD5Util.encrypt(u_rs.getString("user_id")));
		if (ToolUtil.isNotEmpty(u_rs.getString("locked")) && u_rs.getString("locked").equals("N")) {
			user.setIsLocked(false);
		}

		// 获取角色信息
		String sql2 = "select a.role_id,b.role_name from sys_user_role a,sys_role_info b where a.role_id=b.role_id and user_id=?";
		RcdSet r_rs = db.query(sql2, user_id);
		HashMap<String, String> rmap = new HashMap<String, String>();
		for (int i = 0; i < r_rs.size(); i++) {
			rmap.put(r_rs.getRcd(i).getString("role_id"), r_rs.getRcd(i).getString("role_name"));
		}
		user.setRolsSet(rmap);
		return user;
	}

	public R queryUserIdByUserName(String user_name) {
		QueryWrapper<SysUserInfo> queryWrapper = new QueryWrapper<SysUserInfo>();
		queryWrapper.eq("user_name", user_name);
		List<SysUserInfo> lists = this.baseMapper.selectList(queryWrapper);
		if (lists.size() == 0) {
			return R.FAILURE("不存在该用户");
		} else {
			return R.SUCCESS_OPER(lists.get(0).getUserId());
		}

	}

	public R queryReceivingaddr(String user_id) {
		if (user_id == null) {
			return R.FAILURE_REQ_PARAM_ERROR();
		}
		QueryWrapper<SysUserReceivingaddr> queryWrapper = new QueryWrapper<SysUserReceivingaddr>();
		queryWrapper.eq("user_id", user_id);
		return R.SUCCESS_OPER(SysUserReceivingaddrServiceImpl.list(queryWrapper));
	}

	public R deleteReceivingaddr(String user_id, String id) {
		if (ToolUtil.isOneEmpty(user_id, id)) {
			return R.FAILURE_REQ_PARAM_ERROR();
		}
		QueryWrapper<SysUserReceivingaddr> queryWrapper = new QueryWrapper<SysUserReceivingaddr>();
		queryWrapper.eq("user_id", user_id).eq("id", id);
		return R.SUCCESS_OPER(SysUserReceivingaddrServiceImpl.remove(queryWrapper));

	}

}
