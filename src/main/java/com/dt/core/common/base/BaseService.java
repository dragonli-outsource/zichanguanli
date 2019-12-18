package com.dt.core.common.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dt.core.tool.util.support.HttpKit;

/**
 * @author: algernonking
 * @date: 2017年8月3日 上午6:27:47
 * @Description: TODO
 */
public class BaseService extends BaseSC {

	//private static Logger _log = LoggerFactory.getLogger(BaseService.class);

	protected HttpServletRequest getHttpServletRequest() {
		return HttpKit.getRequest();
	}

	protected HttpServletResponse getHttpServletResponse() {
		return HttpKit.getResponse();
	}

	protected HttpSession getSession() {
		return HttpKit.getRequest().getSession();
	}

	protected HttpSession getSession(Boolean flag) {
		return HttpKit.getRequest().getSession(flag);
	}

	protected String getPara(String name) {
		return HttpKit.getRequest().getParameter(name);
	}

	protected void setAttr(String name, Object value) {
		HttpKit.getRequest().setAttribute(name, value);
	}

}
