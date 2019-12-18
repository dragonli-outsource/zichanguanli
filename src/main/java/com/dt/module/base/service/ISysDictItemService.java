package com.dt.module.base.service;

import com.dt.module.base.entity.SysDictItem;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author algernonking
 * @since 2018-07-24
 */
public interface ISysDictItemService extends IService<SysDictItem> {
	List<SysDictItem> selectDictItemByDict(String dictId);
}
