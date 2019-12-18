package com.dt.module.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author algernonking
 * @since 2018-07-24
 */
@TableName("SYS_FILE_CONF")
public class SysFileConf extends BaseModel<SysFileConf> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    @TableField("NAME")
    private String name;
    @TableField("PATH")
    private String path;
    @TableField("IS_USED")
    private String isUsed;
 

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysFileConf{" +
        ", id=" + id +
        ", name=" + name +
        ", path=" + path +
        ", isUsed=" + isUsed +
        "}";
    }
}
