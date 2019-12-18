package com.dt.module.flow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.flow.entity.SysProcessData;
import com.dt.module.flow.service.ISysProcessDataService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author algernonking
 * @since 2019-11-30
 */
@Service
public class SysProcessDataExtService extends BaseService {

	@Autowired
	ISysProcessDataService SysProcessDataServiceImpl;

	public R saveBusinessData(SysProcessData data) {
		SysProcessDataServiceImpl.save(data);
		return R.SUCCESS_OPER();
	}

	public R saveBusinessData(TypedHashMap<String, Object> ps) {
		SysProcessData entity = new SysProcessData();
		String id = ps.getString("id");
		if (ToolUtil.isEmpty(id)) {
			id = ToolUtil.getUUID();
		}
		entity.setDmessage(ps.getString("dmessage", ""));
		entity.setDmark(ps.getString("dmark", ""));
		entity.setDname(ps.getString("dname", ""));
		SysProcessDataServiceImpl.saveOrUpdate(entity);
		return R.SUCCESS_OPER(id);
	}
}
