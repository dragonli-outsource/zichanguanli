package com.dt.core.tool.lang;

import java.io.File;

import com.dt.core.tool.util.support.LangKit;

/**
 * @author: algernonking
 * @date: 2018年6月3日 下午5:07:24
 * @Description: TODO
 */
public class FilesUtils {

	/**
	 * 从 CLASSPATH 下或从指定的本机器路径下寻找一个文件
	 * 
	 * @param path
	 *            文件路径
	 * 
	 * @return 文件对象，如果不存在，则抛出一个运行时异常
	 */
	public static File checkFile(String path) {
		File f = findFile(path);
		if (null == f)
			throw LangKit.makeThrow("Fail to found file '%s'", path);
		return f;
	}

	/**
	 * 从 CLASSPATH 下或从指定的本机器路径下寻找一个文件
	 * 
	 * @param path
	 *            文件路径
	 * 
	 * @return 文件对象，如果不存在，则为 null
	 */
	public static File findFile(String path) {
		return findFile(path);
	}

}
