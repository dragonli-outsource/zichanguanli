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
 * @since 2019-12-01
 */
 
@TableName("sys_process_class_item")
 
public class SysProcessClassItem extends BaseModel<SysProcessClassItem> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("cid")
    private String cid;
    @TableField("name")
    private String name;
    @TableField("mark")
    private String mark;
    @TableField("processname")
    private String processname;
    @TableField("processkey")
    private String processkey;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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

    public String getProcessname() {
        return processname;
    }

    public void setProcessname(String processname) {
        this.processname = processname;
    }

    public String getProcesskey() {
        return processkey;
    }

    public void setProcesskey(String processkey) {
        this.processkey = processkey;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysProcessClassItem{" +
        "id=" + id +
        ", cid=" + cid +
        ", name=" + name +
        ", mark=" + mark +
        ", processname=" + processname +
        ", processkey=" + processkey +
        "}";
    }
}
