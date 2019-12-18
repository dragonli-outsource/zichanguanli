package com.dt.module.base.service.impl;

import com.dt.module.base.entity.SysDictItem;
import com.dt.module.base.mapper.SysDictItemMapper;
import com.dt.module.base.service.ISysDictItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author algernonking
 * @since 2018-07-24
 */
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {

	/* (non Javadoc) 
	 * @Title: selectDictItemByDict
	 * @Description: TODO
	 * @param dictId
	 * @return 
	 * @see com.dt.module.base.service.ISysDictItemService#selectDictItemByDict(java.lang.String) 
	 */
	@Override
	public List<SysDictItem> selectDictItemByDict(String dictId) {
		// TODO Auto-generated method stub
		return this.baseMapper.selectDictItemByDict(dictId);
	}

}
