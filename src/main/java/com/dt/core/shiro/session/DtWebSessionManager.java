package com.dt.core.shiro.session;

import java.io.Serializable;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import com.dt.core.tool.util.ToolUtil;

/**
 * @author: algernonking
 * @date: 2017年11月27日 下午2:54:32
 * @Description: 实现将token存入session
 */
public class DtWebSessionManager extends DefaultWebSessionManager {

	public DtWebSessionManager() {
		super();
	}

	@Override
	protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
		// 如果参数中包含“__sid”参数，则使用此sid会话。
		// 例如：http://localhost/project?__sid=xxx&__cookie=true
		// 其实这里还可以使用如下参数：cookie中的session名称：如：JSESSIONID=xxx,路径中的
		// ;JESSIONID=xxx，但建议还是使用
		// __sid参数。
		String sid = ToolUtil.isEmpty(request.getParameter("_token")) == true ? request.getParameter("token")
				: request.getParameter("_token");
		if (ToolUtil.isNotEmpty(sid)) {
			// __cookie 将__cookie
			// DT_SESSIONID=faf0264d-0661-40be-9978-ff69314a2985 写入到浏览器中
			// 是否将sid保存到cookie，浏览器模式下使用此参数。
			if (WebUtils.isTrue(request, "__cookie")) {
				HttpServletRequest rq = (HttpServletRequest) request;
				HttpServletResponse rs = (HttpServletResponse) response;
				Cookie template = getSessionIdCookie();
				Cookie cookie = new SimpleCookie(template);
				cookie.setValue(sid);
				cookie.saveTo(rq, rs);
			}
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
					ShiroHttpServletRequest.URL_SESSION_ID_SOURCE); // session来源与url
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sid);
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
			return sid;
		} else {
			return super.getSessionId(request, response);
		}
	}

}
