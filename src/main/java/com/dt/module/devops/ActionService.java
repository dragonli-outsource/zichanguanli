package com.dt.module.devops;

import com.dt.module.om.term.entity.Machine;
import com.dt.module.om.term.websocket.SftpClient;
import com.dt.module.om.util.RemoteShellExecutor;

/**
 * @author: algernonking
 * @date: Nov 27, 2019 7:41:40 PM
 * @Description: TODO
 */
public class ActionService {

	/**
	 * @Title: main
	 * @Description: TODO
	 * @param args
	 * @return: void
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SftpClient sftp = new SftpClient();
		Machine m = new Machine("localhost", "39.105.191.22", "root", "qwIMs@j*7arv", 12500);
		RemoteShellExecutor executor = new RemoteShellExecutor("39.105.191.22", "root", "qwIMs@j*7arv", 12500);

	}

}
