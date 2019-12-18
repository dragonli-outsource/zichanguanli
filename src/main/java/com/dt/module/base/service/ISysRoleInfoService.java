package com.dt.module.base.service;

import com.dt.module.base.entity.SysRoleInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author algernonking
 * @since 2018-07-24
 */
public interface ISysRoleInfoService extends IService<SysRoleInfo> {
	Integer isUsed(String id);
}
