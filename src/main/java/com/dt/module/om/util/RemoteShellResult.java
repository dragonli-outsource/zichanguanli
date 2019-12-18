package com.dt.module.om.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: algernonking
 * @date: 2017年12月25日 下午9:58:55
 * @Description: TODO
 */
public class RemoteShellResult {

	public int code;
	public StringBuffer result = new StringBuffer();
	private static Logger _log = LoggerFactory.getLogger(RemoteShellResult.class);

	public static RemoteShellResult setData(int code, StringBuffer result) {
		RemoteShellResult res = new RemoteShellResult();
		res.result = result;
		res.code = code;
		return res;
	}

	public void print() {
		_log.info("code:\n" + code);
		_log.info("result:\n" + result);
	}

}
