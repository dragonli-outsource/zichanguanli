package com.dt.module.base.service;

import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dt.core.common.base.R;
import com.dt.module.base.entity.SysMenus;
import com.dt.module.base.entity.SysUserInfo;
import com.dt.module.base.entity.UserShiro;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author algernonking
 * @since 2018-07-24
 */
public interface ISysUserInfoService extends IService<SysUserInfo> {

	List<SysMenus> listMyMenus(String user_id);

	JSONArray listMyMenusById(String user_id, String menu_id);

	R saveDefMenus(String user_id, String id);

	R selectByOpenId(String open_id);

	R modifyPassword(String user_id, String pwd);

	R addUser(SysUserInfo user);

	List<HashMap<String, Object>> listUserRoles(String user_id);

	UserShiro listUserForShiro(String user_id);

	R queryUserIdByUserName(String user_id);
	
	R queryReceivingaddr(String user_id);
	
	R deleteReceivingaddr(String user_id, String id);
 
	SysUserInfo selectOneByEmpl(String empl);

	R changeUserPwdForce(String user_id, String pwd1, String pwd2);
}
