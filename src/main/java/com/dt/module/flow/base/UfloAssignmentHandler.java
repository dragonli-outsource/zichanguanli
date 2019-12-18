package com.dt.module.flow.base;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.handler.AssignmentHandler;
import com.bstek.uflo.process.node.TaskNode;

/**
 * @author: algernonking
 * @date: Nov 30, 2019 9:04:15 AM
 * @Description: TODO
 */

//流程处理人
@Component
public class UfloAssignmentHandler implements AssignmentHandler {

	/*
	 * (non Javadoc)
	 * 
	 * @Title: handle
	 * 
	 * @Description: TODO
	 * 
	 * @param taskNode
	 * 
	 * @param processInstance
	 * 
	 * @param context
	 * 
	 * @return
	 * 
	 * @see com.bstek.uflo.process.handler.AssignmentHandler#handle(com.bstek.uflo.
	 * process.node.TaskNode, com.bstek.uflo.model.ProcessInstance,
	 * com.bstek.uflo.env.Context)
	 */
	@Override
	public Collection<String> handle(TaskNode taskNode, ProcessInstance processInstance, Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
