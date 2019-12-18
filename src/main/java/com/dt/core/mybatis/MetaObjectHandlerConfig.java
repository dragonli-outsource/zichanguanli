package com.dt.core.mybatis;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.dt.core.tool.util.support.HttpKit;

/**
 * @author: algernonking
 * @date: 2018年7月22日 下午4:50:31
 * @Description: TODO
 */
@Component
public class MetaObjectHandlerConfig  implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		
		String user_id = HttpKit.getRequest().getSession().getAttribute("user_id") == null ? "null"
				: HttpKit.getRequest().getSession().getAttribute("user_id").toString();
		setFieldValByName("createTime", new Date(), metaObject);
		setFieldValByName("createBy", user_id, metaObject);
		setFieldValByName("updateTime", new Date(), metaObject);
		setFieldValByName("updateBy", user_id, metaObject);
		setFieldValByName("dr", "0", metaObject);

	}

	@Override
	public void updateFill(MetaObject metaObject) {
		String user_id = HttpKit.getRequest().getSession().getAttribute("user_id") == null ? "null"
				: HttpKit.getRequest().getSession().getAttribute("user_id").toString();
		setFieldValByName("updateTime", new Date(), metaObject);
		setFieldValByName("updateBy", user_id, metaObject);
	}
}
