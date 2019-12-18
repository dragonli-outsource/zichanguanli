package com.dt.module.base.mapper;

import com.dt.module.base.entity.SysMenus;
import com.dt.module.base.entity.SysUserInfo;

import java.util.HashMap;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author algernonking
 * @since 2018-07-24
 */
public interface SysUserInfoMapper extends BaseMapper<SysUserInfo> {
	//查询某个用户的菜单
	List<SysMenus> listMyMenus(String user_id);
	
	List<HashMap<String, Object>> listUserRoles(String user_id);
}
