package com.dt.module.flow.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 
 * </p>
 *
 * @author algernonking
 * @since 2019-11-30
 */
 
@TableName("sys_process_class")
 
public class SysProcessClass extends BaseModel<SysProcessClass> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("name")
    private String name;
    @TableField("mark")
    private String mark;
    @TableField("type")
    private String type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysProcessClass{" +
        "id=" + id +
        ", name=" + name +
        ", mark=" + mark +
        ", type=" + type +
        "}";
    }
}
