package com.dt.module.flow.base;

import org.springframework.stereotype.Component;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.handler.ProcessEventHandler;

/**
 * @author: algernonking
 * @date: Sep 2, 2019 11:10:47 AM
 * @Description: TODO
 */


@Component
public class UfloProcessEventHandler implements ProcessEventHandler {

	/*
	 * (non Javadoc)
	 * 
	 * @Title: end
	 * 
	 * @Description: TODO
	 * 
	 * @param arg0
	 * 
	 * @param arg1
	 * 
	 * @see
	 * com.bstek.uflo.process.handler.ProcessEventHandler#end(com.bstek.uflo.model.
	 * ProcessInstance, com.bstek.uflo.env.Context)
	 */

	/**
	 * 流程实例结束后触发的方法
	 * 
	 * @param processInstance 流程实例
	 * @param context         流程实例上下文
	 */
	@Override
	public void start(ProcessInstance arg0, Context arg1) {
		System.out.println(arg0.toString());
		System.out.println("start " + arg0.getCurrentNode() + ",'" + arg0.getCurrentTask());
	}

	/**
	 * 流程实例开始后触发的方法
	 * 
	 * @param processInstance 流程实例
	 * @param context         流程实例上下文
	 */
	@Override

	public void end(ProcessInstance arg0, Context arg1) {
		// TODO Auto-generated method stub
		System.out.println(arg0.toString());
		System.out.println("end " + arg0.getCurrentNode() + ",'" + arg0.getCurrentTask());

	}

}
