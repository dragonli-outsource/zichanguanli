package com.dt.module.flow.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.handler.NodeEventHandler;
import com.bstek.uflo.process.node.Node;
import com.dt.module.cmdb.service.impl.ResActionService;
import com.dt.module.flow.entity.SysProcessData;
import com.dt.module.flow.service.ISysProcessDataService;
import com.dt.module.flow.service.impl.SysUfloProcessService;

/**
 * @author: algernonking
 * @date: Nov 30, 2019 9:04:15 AM
 * @Description: TODO
 */

//流程节点处理
@Component
public class UfloNodeEndEventHandler implements NodeEventHandler {

	@Autowired
	ISysProcessDataService SysProcessDataServiceImpl;

	@Override
	public void enter(Node node, ProcessInstance processInstance, Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void leave(Node node, ProcessInstance processInstance, Context context) {
		// TODO Auto-generated method stub

		String busid = processInstance.getBusinessId();

		if (busid != null && !busid.equals("")) {
			QueryWrapper<SysProcessData> qw = new QueryWrapper<SysProcessData>();
			qw.eq("busid", busid);
			SysProcessData sd = SysProcessDataServiceImpl.getOne(qw);
			String pdtype = sd.getPtype();
			if (SysUfloProcessService.P_TYPE_FINISH.equals(pdtype)) {
				// 流程已经处理过不需要处理
			} else {
				// 更新流程总表
				UpdateWrapper<SysProcessData> uw = new UpdateWrapper<SysProcessData>();
				uw.eq("busid", busid);
				uw.set("pstatus", SysUfloProcessService.P_TYPE_FINISH);
				// 流程类型处理
				if (pdtype != null) {
					if (pdtype.equals("LY") || pdtype.equals("JY") || pdtype.equals("ZY")) {
						uw.set("pstatusdtl", SysUfloProcessService.P_STATUS_APPROVALSUCCESS);
					}
				}
				SysProcessDataServiceImpl.update(uw);
			}

		}

	}

}
