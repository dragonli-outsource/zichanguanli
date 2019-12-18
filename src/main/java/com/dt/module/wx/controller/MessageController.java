package com.dt.module.wx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.util.TypedHashMap;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.wx.service.MessageService;

/**
 * @author: jinjie
 * @date: 2018年5月7日 下午2:46:41
 * @Description: TODO
 */
@Controller
@RequestMapping("/api")
public class MessageController extends BaseController {

	@Autowired
	private MessageService messageService;

	@RequestMapping("/wx/deleteMessage.do")
	@ResponseBody
	@Acl(value = Acl.ACL_DENY, info = "删除消息")
	public R deleteMessage(String id) {
		return messageService.deleteMessage(id);
	}

	@RequestMapping("/wx/saveMessage.do")
	@ResponseBody
	@Acl(value = Acl.ACL_DENY, info = "更新消息")
	public R saveMessage() {
		TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
		if (ToolUtil.isEmpty(ps.getString("id"))) {
			return messageService.addMessage(ps);
		} else {
			return messageService.updateMessage(ps);
		}
	}

	@RequestMapping("/wx/queryMessageById.do")
	@ResponseBody
	@Acl(value = Acl.ACL_DENY, info = "查询消息")
	public R queryMessageById(String id) {
		return messageService.queryMessageById(id);
	}

	@RequestMapping("/wx/queryMessages.do")
	@ResponseBody
	@Acl(value = Acl.ACL_DENY, info = "查询消息")
	public R queryMessages(String funtype) {
		return messageService.queryMessages(funtype);
	}

	// 图文
	@RequestMapping("/wx/queryImageTextMessagesGroup.do")
	@ResponseBody
	@Acl(value = Acl.ACL_DENY, info = "查询图文消息")
	public R queryImageTextMessagesGroup() {
		return messageService.queryImageTextMessagesGroup();
	}

	@RequestMapping("/wx/queryImageTextMessages.do")
	@ResponseBody
	@Acl(value = Acl.ACL_DENY, info = "查询图文消息")
	public R queryImageTextMessages(String id) {
		return messageService.queryImageTextMessages(id);
	}

	@RequestMapping("/wx/deleteImageTextMessage.do")
	@ResponseBody
	@Acl(value = Acl.ACL_DENY, info = "删除图文消息")
	public R deleteImageTextMessage(String id) {
		return messageService.deleteImageTextMessage(id);
	}

	@RequestMapping("/wx/saveImageTextMessage.do")
	@ResponseBody
	@Acl(value = Acl.ACL_DENY, info = "更新图文消息")
	public R saveImageTextMessage() {
		TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
		if (ToolUtil.isEmpty(ps.getString("id"))) {
			return messageService.addImageTextMessage(ps);
		} else {
			return messageService.updateImageTextMessage(ps);
		}
	}

	@RequestMapping("/wx/queryImageTextMessageById.do")
	@ResponseBody
	@Acl(value = Acl.ACL_DENY, info = "查询图文消息")
	public R queryImageTextMessageById(String id) {
		return messageService.queryImageTextMessageById(id);
	}

	@RequestMapping("/wx/addSc.do")
	@ResponseBody
	@Acl(value = Acl.ACL_DENY, info = "添加素材")
	public R addSc(String id) {
		TypedHashMap<String, Object> ps = HttpKit.getRequestParameters();
		return messageService.addSc(ps);
	}

	@RequestMapping("/wx/queryScs.do")
	@ResponseBody
	@Acl(value = Acl.ACL_DENY, info = "查询素材")
	public R queryScs() {
		return messageService.queryScs();
	}
}
