package com.dt.module.base.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseCodeMsgEnum;
import com.dt.core.common.base.BaseCommon;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.shiro.ShiroKit;
import com.dt.core.shiro.ShiroUser;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.service.ISysMenusService;
import com.dt.module.base.service.ISysUserInfoService;
import com.dt.module.base.service.impl.LoginService;

@Controller
@RequestMapping("/api")
public class LoginController extends BaseController {
	private static Logger _log = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	LoginService loginService = null;

	@Autowired
	ISysUserInfoService SysUserInfoServiceImpl;

	@Autowired
	ISysMenusService SysMenusServiceImpl;

	/**
	 * @Description: user,pwd,type,client必须部不为空
	 */
	@Acl(value = Acl.ACL_ALLOW, info = "登录")
	@RequestMapping(value = "/user/login.do")
	@ResponseBody
	public R login(String user, String pwd, String type, String client, HttpServletRequest request) {

		// 验证账户是否有效
		R vlrs = loginService.validLogin(user, type, client);
		if (vlrs.isFailed()) {
			return vlrs;
		}
		String user_id = vlrs.getData().toString();
		// 登录操作
		Subject currentUser = ShiroKit.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user_id, pwd == null ? null : pwd.toCharArray());
		token.setRememberMe(true);
		String error = "";
		try {
			currentUser.login(token);
		} catch (UnknownAccountException e) {
			error = "账户不存在";
		} catch (IncorrectCredentialsException e) {
			error = "账号密码错误";
		} catch (ExcessiveAttemptsException e) {
			// TODO: handle exception
			error = "登录失败多次，账户锁定10分钟";
		} catch (LockedAccountException e) {
			error = "账户已被锁定";
		} catch (AuthenticationException e) {
			error = "其他错误：" + e.getMessage();
		}
		if (ToolUtil.isNotEmpty(error)) {
			return R.FAILURE(error);
		}

		// 用户登录成功
		ShiroUser shiroUser = ShiroKit.getUser();
		super.getSession().setAttribute("shiroUser", shiroUser);
		super.getSession().setAttribute("user_id", shiroUser.id);
		ShiroKit.getSession().setAttribute("sessionFlag", true);

		JSONObject r = new JSONObject();

		JSONObject u = JSONObject.parseObject(
				JSON.toJSONString(SysUserInfoServiceImpl.getById(user_id), SerializerFeature.WriteDateUseDateFormat));
		// 覆盖重要信息
		u.put("pwd", "********");
		r.put("user_info", u);
		// 菜单列表
		JSONArray systems = null;
		if (BaseCommon.isSuperAdmin(shiroUser.id)) {
			systems = ConvertUtil.toJSONArryFromEntityList(SysMenusServiceImpl.list());
			r.put("systems", systems);
		} else {
			systems = ConvertUtil.toJSONArryFromEntityList(SysUserInfoServiceImpl.listMyMenus(this.getUserId()));
			r.put("systems", systems);
		}

		// 获取当前需要显示的菜单
		String cur_system = "";
		 
		String tab_system = (u.getString("systemId")==null?"":u.getString("systemId"));
		if (systems.size() == 0) {
			cur_system = "";
		} else {
			for (int i = 0; i < systems.size(); i++) {
				//判断当前数据库中选择的system是否存在用户可选列表中
				if ( systems.getJSONObject(i).getString("menuId").equals(tab_system)){
					cur_system = systems.getJSONObject(i).getString("menuId");
					break;
				}
			}
			if (ToolUtil.isEmpty(cur_system)||cur_system.equals("")) {
				cur_system = systems.getJSONObject(0).getString("menuId");
			}

		}
		r.put("cur_system", cur_system);
		r.put("token", super.getSession().getId());
		_log.info("login:" + r.toJSONString());
		return R.SUCCESS(BaseCodeMsgEnum.USER_LOGIN_SUCCESS.getMessage(), r);
	}

	@Acl(value = Acl.ACL_ALLOW, info = "登录")
	@RequestMapping(value = "/user/loginFast.do")
	@ResponseBody
	public R loginFast(String user, String pwd, String type, String client, HttpServletRequest request) {

		// 验证账户是否有效
		R vlrs = loginService.validLogin(user, type, client);
		if (vlrs.isFailed()) {
			return vlrs;
		}
		String user_id = vlrs.getData().toString();
		// 登录操作
		Subject currentUser = ShiroKit.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user_id, pwd == null ? null : pwd.toCharArray());
		token.setRememberMe(true);
		String error = "";
		try {
			currentUser.login(token);
		} catch (UnknownAccountException e) {
			error = "账户不存在";
		} catch (IncorrectCredentialsException e) {
			error = "账号密码错误";
		} catch (ExcessiveAttemptsException e) {
			// TODO: handle exception
			error = "登录失败多次，账户锁定10分钟";
		} catch (LockedAccountException e) {
			error = "账户已被锁定";
		} catch (AuthenticationException e) {
			error = "其他错误：" + e.getMessage();
		}
		if (ToolUtil.isNotEmpty(error)) {
			return R.FAILURE(error);
		}

		// 用户登录成功
		ShiroUser shiroUser = ShiroKit.getUser();
		super.getSession().setAttribute("shiroUser", shiroUser);
		super.getSession().setAttribute("user_id", shiroUser.id);
		ShiroKit.getSession().setAttribute("sessionFlag", true);

		JSONObject r = new JSONObject();
		JSONObject u = JSONObject.parseObject(
				JSON.toJSONString(SysUserInfoServiceImpl.getById(user_id), SerializerFeature.WriteDateUseDateFormat));
		// 覆盖重要信息
		u.put("pwd", "********");
		r.put("user_info", u);
		r.put("token", super.getSession().getId());
		_log.info("login:" + r.toJSONString());
		return R.SUCCESS(BaseCodeMsgEnum.USER_LOGIN_SUCCESS.getMessage(), r);
	}

	@RequestMapping(value = "/user/checkLogin.do")
	@ResponseBody
	@Acl(value = Acl.ACL_ALLOW, info = "检测登录")
	public R checkLogin() throws IOException {
		if (ShiroKit.isAuthenticated()) {
			return R.SUCCESS(BaseCodeMsgEnum.USER_ALREADY_LOGIN.getMessage());
		} else {
			return R.FAILURE_NOT_LOGIN();
		}
	}

	/**
	 * @Description: 实体已在shiro中实现
	 */
	@RequestMapping(value = "/user/logout.do")
	@ResponseBody
	@Acl(value = Acl.ACL_ALLOW, info = "退出")
	public R loginout() throws IOException {
		return R.SUCCESS("成功退出");
	}

}
