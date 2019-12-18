package com.dt.module.ct.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
/**
 * <p>
 * 
 * </p>
 *
 * @author algernonking
 * @since 2018-07-30
 */
@TableName("CT_CLASS_ITEM")
public class CtClassItem extends BaseModel<CtClassItem> {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;
    @TableField("CLASS_ID")
    private String classId;
    @TableField("IS_USED")
    private String isUsed;
    @TableField("VALUE")
    private String value;
    @TableField("OD")
    private String od;
    @TableField("PIC_ID")
    private String picId;
    @TableField("MARK")
    private String mark;
    @TableField("STATUS")
    private String status;
    @TableField("COL_A")
    private String colA;
    @TableField("COL_B")
    private String colB;
    @TableField("COL_C")
    private String colC;
    @TableField("COL_D")
    private String colD;
    @TableField("COL_E")
    private String colE;
    @TableField("NAME")
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOd() {
        return od;
    }

    public void setOd(String od) {
        this.od = od;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getColA() {
        return colA;
    }

    public void setColA(String colA) {
        this.colA = colA;
    }

    public String getColB() {
        return colB;
    }

    public void setColB(String colB) {
        this.colB = colB;
    }

    public String getColC() {
        return colC;
    }

    public void setColC(String colC) {
        this.colC = colC;
    }

    public String getColD() {
        return colD;
    }

    public void setColD(String colD) {
        this.colD = colD;
    }

    public String getColE() {
        return colE;
    }

    public void setColE(String colE) {
        this.colE = colE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CtClassItem{" +
        ", id=" + id +
        ", classId=" + classId +
        ", isUsed=" + isUsed +
        ", value=" + value +
        ", od=" + od +
        ", picId=" + picId +
        ", mark=" + mark +
        ", status=" + status +
        ", colA=" + colA +
        ", colB=" + colB +
        ", colC=" + colC +
        ", colD=" + colD +
        ", colE=" + colE +
        ", name=" + name +
        "}";
    }
}
