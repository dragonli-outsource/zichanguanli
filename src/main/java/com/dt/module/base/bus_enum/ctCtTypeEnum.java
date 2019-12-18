package com.dt.module.base.bus_enum;

import java.io.Serializable;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ctCtTypeEnum implements IEnum<Serializable> {
	NEWS("news", "新闻"), DOC("doc", "文档"),COMPANY("company", "公司");

	private String code;
	private String desc;

	ctCtTypeEnum(final String code, final String desc) {
		this.code = code;
		this.desc = desc;
	}

	@Override
	public Serializable getValue() {

		return this.code;
	}

	@JsonValue
	public String getDesc() {
		return this.desc;
	}
}
