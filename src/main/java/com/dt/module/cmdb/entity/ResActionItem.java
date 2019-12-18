package com.dt.module.cmdb.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dt.core.common.base.BaseModel;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
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
 
@TableName("res_action_item")
 
public class ResActionItem extends BaseModel<ResActionItem> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("actuuid")
    private String actuuid;
    @TableField("status")
    private String status;
    @TableField("backtime")
    private Date backtime;
    @TableField("resid")
    private String resid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActuuid() {
        return actuuid;
    }

    public void setActuuid(String actuuid) {
        this.actuuid = actuuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getBacktime() {
        return backtime;
    }

    public void setBacktime(Date backtime) {
        this.backtime = backtime;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ResActionItem{" +
        "id=" + id +
        ", actuuid=" + actuuid +
        ", status=" + status +
        ", backtime=" + backtime +
        ", resid=" + resid +
        "}";
    }
}
