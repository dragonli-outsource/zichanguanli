package com.dt.core.common.base;

import java.util.Date;

import com.dt.core.tool.encrypt.MD5Util;
import com.dt.core.tool.util.ToolUtil;

/**
 * @author: algernonking
 * @date: 2017年11月9日 下午12:28:39
 * @Description: TODO
 */
public class BaseCommon {

	public static boolean isSuperAdmin(String id) {
		return ToolUtil.isNotEmpty(id) && id.equals(getSuperAdmin()) ? true : false;
	}

	public static String getSuperAdmin() {
		return ToolUtil.isEmpty(BaseConstants.superadmin) ? MD5Util.encrypt(new Date().getTime() + "")
				: BaseConstants.superadmin;
	}

	public static void print(String value) {
		System.out.println(value);
	}

}
