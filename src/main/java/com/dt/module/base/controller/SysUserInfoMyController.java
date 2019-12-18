package com.dt.module.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.entity.SysUserInfo;
import com.dt.module.base.service.ISysUserInfoService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author algernonking
 * @since 2018-07-24
 */
@Controller
@RequestMapping("/api/sysUserInfo/my")
public class SysUserInfoMyController extends BaseController {

	@Autowired
	ISysUserInfoService SysUserInfoServiceImpl;

	@ResponseBody
	@Acl(info = "根据Id查询", value = Acl.ACL_USER)
	@RequestMapping(value = "/select.do")
	public R selectById(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
		return R.SUCCESS_OPER(SysUserInfoServiceImpl.getById(id));
	}

	@ResponseBody
	@Acl(info = "根据Id更新", value = Acl.ACL_USER)
	@RequestMapping(value = "/update.do")
	public R updateById(SysUserInfo entity) {
		return R.SUCCESS_OPER(SysUserInfoServiceImpl.updateById(entity));
	}

	@ResponseBody
	@Acl(info = "存在则更新,否则插入", value = Acl.ACL_USER)
	@RequestMapping(value = "/insertOrUpdate.do")
	public R insertOrUpdate(SysUserInfo entity) {
		return R.SUCCESS_OPER(SysUserInfoServiceImpl.saveOrUpdate(entity));
	}

	@ResponseBody
	@Acl(info = "修改密码", value = Acl.ACL_USER)
	@RequestMapping(value = "/modifypwd.do")
	public R modifypwd(@RequestParam(value = "oldpwd", required = true, defaultValue = "") String oldpwd,
			@RequestParam(value = "pwd", required = true, defaultValue = "") String pwd) {
		SysUserInfo user = SysUserInfoServiceImpl.getById(this.getUserId());
		if (oldpwd.equals(user.getPwd())) {
			return SysUserInfoServiceImpl.modifyPassword(this.getUserId(), pwd);
		} else {
			return R.FAILURE("请输入正确的密码");
		}
	}

	@ResponseBody
	@Acl(info = "修改密码", value = Acl.ACL_USER)
	@RequestMapping(value = "/saveDefMenus.do")
	public R saveDefMenus(String system) {
		return SysUserInfoServiceImpl.saveDefMenus(this.getUserId(), system);
	}

	@ResponseBody
	@Acl(info = "显示我的菜单", value = Acl.ACL_USER)
	@RequestMapping(value = "/listMyMenus.do")
	public R listMyMenus() {
		return R.SUCCESS_OPER(SysUserInfoServiceImpl.listMyMenus(this.getUserId()));

	}

	@RequestMapping("/listMyMenusById.do")
	@ResponseBody
	@Acl(value = Acl.ACL_USER, info = "查询用户菜单")
	public R getUserMenus(String menu_id) {
		if (ToolUtil.isEmpty(menu_id)) {
			return R.FAILURE_REQ_PARAM_ERROR();
		}
		return R.SUCCESS_OPER(SysUserInfoServiceImpl.listMyMenusById(getUserId(), menu_id));
	}

	@RequestMapping("/queryReceivingaddr.do")
	@ResponseBody
	@Acl(value = Acl.ACL_USER, info = "查询用户收货地址")
	public R queryReceivingaddr() {

		return SysUserInfoServiceImpl.queryReceivingaddr(this.getUserId());
	}

	@RequestMapping("/deleteReceivingaddr.do")
	@ResponseBody
	@Acl(value = Acl.ACL_USER, info = "删除收货地址")
	public R deleteReceivingaddr(String id) {
		 
		return SysUserInfoServiceImpl.deleteReceivingaddr(this.getUserId(), id);
	}

}
