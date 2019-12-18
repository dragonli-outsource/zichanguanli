package com.dt.module.flow.base;

import org.springframework.stereotype.Component;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.handler.NodeEventHandler;
import com.bstek.uflo.process.node.Node;

/**
 * @author: algernonking
 * @date: Nov 30, 2019 9:04:15 AM
 * @Description: TODO
 */

//流程节点处理
@Component
public class UfloNodeEventHandler implements NodeEventHandler {

	/*
	 * (non Javadoc)
	 * 
	 * @Title: enter
	 * 
	 * @Description: TODO
	 * 
	 * @param node
	 * 
	 * @param processInstance
	 * 
	 * @param context
	 * 
	 * @see
	 * com.bstek.uflo.process.handler.NodeEventHandler#enter(com.bstek.uflo.process.
	 * node.Node, com.bstek.uflo.model.ProcessInstance, com.bstek.uflo.env.Context)
	 */
	@Override
	public void enter(Node node, ProcessInstance processInstance, Context context) {
		// TODO Auto-generated method stub
		System.out.println("enter");
		System.out.println("processInstance.getBusinessId()" + processInstance.getBusinessId());
		System.out.println("processInstance.getCurrentTask()" + processInstance.getCurrentTask());
		System.out.println("processInstance.getProcessId()" + processInstance.getProcessId());
		System.out.println("processInstance.getSubject()" + processInstance.getSubject());
		System.out.println("processInstance.getCurrentNode()" + processInstance.getCurrentNode());

		System.out.println("node.getDescription()" + node.getDescription());
		System.out.println("node.getLabel()" + node.getLabel());
		System.out.println("node.getType()" + node.getType());
		System.out.println("node.getName()" + node.getName());
		System.out.println("node.getProcessId()" + node.getProcessId());

		System.out.println("context.toString()" + context.toString());

	}

	/*
	 * (non Javadoc)
	 * 
	 * @Title: leave
	 * 
	 * @Description: TODO
	 * 
	 * @param node
	 * 
	 * @param processInstance
	 * 
	 * @param context
	 * 
	 * @see
	 * com.bstek.uflo.process.handler.NodeEventHandler#leave(com.bstek.uflo.process.
	 * node.Node, com.bstek.uflo.model.ProcessInstance, com.bstek.uflo.env.Context)
	 */
	@Override
	public void leave(Node node, ProcessInstance processInstance, Context context) {
		// TODO Auto-generated method stub
		System.out.println("leave");
		System.out.println("processInstance.getBusinessId()" + processInstance.getBusinessId());
		System.out.println("processInstance.getCurrentTask()" + processInstance.getCurrentTask());
		System.out.println("processInstance.getProcessId()" + processInstance.getProcessId());
		System.out.println("processInstance.getSubject()" + processInstance.getSubject());
		System.out.println("processInstance.getCurrentNode()" + processInstance.getCurrentNode());

		System.out.println("node.getDescription()" + node.getDescription());
		System.out.println("node.getLabel()" + node.getLabel());
		System.out.println("node.getType()" + node.getType());
		System.out.println("node.getName()" + node.getName());
		System.out.println("node.getProcessId()" + node.getProcessId());

		System.out.println("context.toString()" + context.toString());

	}

}
