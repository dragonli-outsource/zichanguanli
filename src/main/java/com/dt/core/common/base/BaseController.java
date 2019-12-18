package com.dt.core.common.base;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.InvalidSessionException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import com.dt.core.tool.lang.PropertiesFileUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.core.tool.util.support.StrKit;

 
public class BaseController extends BaseSC {

	@InitBinder
	public void initBinder(WebDataBinder binder){
		binder.registerCustomEditor(Date.class, new DateEditor());
	}

		
	/**
	 * 统一异常处理
	 * 
	 * @param request
	 * @param response
	 * @param exception
	 */
	@ExceptionHandler
	public String exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
		String msg = ExceptionUtils.getRootCauseMessage(exception) == null ? ""
				: ExceptionUtils.getRootCauseMessage(exception);
		exception.printStackTrace();
		// System.out.println();
		request.setAttribute("ex", exception);
		if (null != request.getHeader("X-Requested-With")
				&& "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
			request.setAttribute("requestHeader", "ajax");
		}

		if (isReturnJSON(request)) {
			try {
				response.setCharacterEncoding("UTF-8");
				response.setHeader("content-type", "text/html;charset=UTF-8");
				response.getWriter().print(R.FAILURE(msg));
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		// shiro没有权限异常
		if (exception instanceof UnauthorizedException) {
			return "/403.jsp";
		}
		// shiro会话已过期异常
		if (exception instanceof InvalidSessionException) {
			return "/error.jsp";
		}
		return "/error.jsp";
	}

	private Boolean isReturnJSON(HttpServletRequest httpRequest) {
		Boolean res = false;
		if (HttpKit.isAjax(httpRequest) || StrKit.endWith(httpRequest.getRequestURL() + "", ".do", true)) {
			res = true;
		}
		return res;
	}

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

	

	protected Integer getSystemInvokCount() {
		return (Integer) this.getHttpServletRequest().getServletContext().getAttribute("systemCount");
	}

	/**
	 * 删除cookie
	 */
	protected void deleteCookieByName(String cookieName) {
		Cookie[] cookies = this.getHttpServletRequest().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieName)) {
				Cookie temp = new Cookie(cookie.getName(), "");
				temp.setMaxAge(0);
				this.getHttpServletResponse().addCookie(temp);
			}
		}
	}

	public String warpObject(Object o) {
		if (o instanceof R) {
			return ((R) o).asJsonStr();
		} else {
			return o.toString();
		}
	}

	/**
	 * 返回jsp视图
	 * 
	 * @param path
	 * @return
	 */
	public static String jsp(String path) {
		return path.concat("");
	}

	/**
	 * 返回thymeleaf视图
	 * 
	 * @param path
	 * @return
	 */
	public static String thymeleaf(String path) {
		String folder = PropertiesFileUtil.getInstance().get("app.name");
		return "/".concat(folder).concat(path).concat(".html");
	}
}
