package com.dt.module.ct.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 
 * </p>
 *
 * @author algernonking
 * @since 2018-07-28
 */
@TableName("CT_BANNER")
public class CtBanner extends BaseModel<CtBanner> {

    private static final long serialVersionUID = 1L;

    @TableId("BANNER_ID")
    private String bannerId;
    @TableField("NAME")
    private String name;
    @TableField("TYPE")
    private String type;
    @TableField("IS_USED")
    private String isUsed;


    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    protected Serializable pkVal() {
        return this.bannerId;
    }

    @Override
    public String toString() {
        return "CtBanner{" +
        ", bannerId=" + bannerId +
        ", name=" + name +
        ", type=" + type +
        ", isUsed=" + isUsed +
        "}";
    }
}
