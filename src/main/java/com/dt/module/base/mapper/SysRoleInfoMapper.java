package com.dt.module.base.mapper;

import com.dt.module.base.entity.SysRoleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author algernonking
 * @since 2018-07-24
 */
public interface SysRoleInfoMapper extends BaseMapper<SysRoleInfo> {

	Integer isUsed(String id);
}
