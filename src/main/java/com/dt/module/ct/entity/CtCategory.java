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
 * @since 2018-07-30
 */
@TableName("CT_CATEGORY")
public class CtCategory extends BaseModel<CtCategory> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    @TableField("ROOT")
    private String root;
    @TableField("NAME")
    private String name;
    @TableField("MPIC")
    private String mpic;
    @TableField("PARENT_ID")
    private String parentId;
    @TableField("ROUTE")
    private String route;
    @TableField("MARK")
    private String mark;
    @TableField("NODE_LEVEL")
    private String nodeLevel;
    @TableField("OD")
    private String od;
    @TableField("ISACTION")
    private String isaction;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMpic() {
        return mpic;
    }

    public void setMpic(String mpic) {
        this.mpic = mpic;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(String nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public String getOd() {
        return od;
    }

    public void setOd(String od) {
        this.od = od;
    }

    public String getIsaction() {
        return isaction;
    }

    public void setIsaction(String isaction) {
        this.isaction = isaction;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CtCategory{" +
        ", id=" + id +
        ", root=" + root +
        ", name=" + name +
        ", mpic=" + mpic +
        ", parentId=" + parentId +
        ", route=" + route +
        ", mark=" + mark +
        ", nodeLevel=" + nodeLevel +
        ", od=" + od +
        ", isaction=" + isaction +
        "}";
    }
}
