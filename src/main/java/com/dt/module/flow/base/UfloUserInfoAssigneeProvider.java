package com.dt.module.flow.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.assign.AssigneeProvider;
import com.bstek.uflo.process.assign.Entity;
import com.bstek.uflo.process.assign.PageQuery;
import com.dt.module.base.entity.SysUserInfo;
import com.dt.module.base.service.ISysUserInfoService;

/**
 * @author: algernonking
 * @date: Nov 30, 2019 9:34:11 AM
 * @Description: TODO
 */

//指定流程参与人
@Component
public class UfloUserInfoAssigneeProvider implements AssigneeProvider {

	@Autowired
	private ISysUserInfoService SysUserInfoServiceImpl;

	/*
	 * (non Javadoc)
	 * 
	 * @Title: isTree
	 * 
	 * @Description: TODO
	 * 
	 * @return
	 * 
	 * @see com.bstek.uflo.process.assign.AssigneeProvider#isTree()
	 */
	@Override
	public boolean isTree() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non Javadoc)
	 * 
	 * @Title: getName
	 * 
	 * @Description: TODO
	 * 
	 * @return
	 * 
	 * @see com.bstek.uflo.process.assign.AssigneeProvider#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "指定用户_所有用户列表";
	}

	/*
	 * (non Javadoc)
	 * 
	 * @Title: queryEntities
	 * 
	 * @Description: TODO
	 * 
	 * @param pageQuery
	 * 
	 * @param parentId
	 * 
	 * @see
	 * com.bstek.uflo.process.assign.AssigneeProvider#queryEntities(com.bstek.uflo.
	 * process.assign.PageQuery, java.lang.String)
	 */
	@Override
	public void queryEntities(PageQuery<Entity> pageQuery, String parentId) {

		int index = pageQuery.getPageIndex();
		int size = pageQuery.getPageSize();
		int pagesize = size;
		int pageindex = index;
		IPage<SysUserInfo> pdata = SysUserInfoServiceImpl.page(new Page<SysUserInfo>(pageindex, pagesize));
		List<Entity> entitys = new ArrayList<Entity>();
		for (SysUserInfo userinfo : pdata.getRecords()) {
			entitys.add(new Entity(userinfo.getUserId(), userinfo.getName()));
		}
		
		pageQuery.setResult(entitys);
		pageQuery.setRecordCount(SysUserInfoServiceImpl.count());

	}

	/*
	 * (non Javadoc)
	 * 
	 * @Title: getUsers
	 * 
	 * @Description: TODO
	 * 
	 * @param entityId
	 * 
	 * @param context
	 * 
	 * @param processInstance
	 * 
	 * @return
	 * 
	 * @see
	 * com.bstek.uflo.process.assign.AssigneeProvider#getUsers(java.lang.String,
	 * com.bstek.uflo.env.Context, com.bstek.uflo.model.ProcessInstance)
	 */
	@Override
	public Collection<String> getUsers(String entityId, Context context, ProcessInstance processInstance) {
		// 当前指定用户,必需为userId
		List<String> users = new ArrayList<String>();
		users.add(entityId);
		return users;
	}

	@Override
	public boolean disable() {
		// TODO Auto-generated method stub
		return false;
	}

}
