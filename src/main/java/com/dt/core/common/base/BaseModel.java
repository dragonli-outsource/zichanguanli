package com.dt.core.common.base;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;


@SuppressWarnings("rawtypes")
public class BaseModel<T> extends Model implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableField(value = "DR", fill = FieldFill.INSERT)
	@TableLogic
	private String dr;
	@TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
	private String createBy;
	@TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	@TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
	private String updateBy;

	/**
	 * @return the dr
	 */
	public String getDr() {
		return dr;
	}

	/**
	 * @param dr the dr to set
	 */
	public void setDr(String dr) {
		this.dr = dr;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the createBy
	 */
	public String getCreateBy() {
		return createBy;
	}

	/**
	 * @param createBy the createBy to set
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the updateBy
	 */
	public String getUpdateBy() {
		return updateBy;
	}

	/**
	 * @param updateBy the updateBy to set
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	/*
	 * (non Javadoc)
	 * 
	 * @Title: pkVal
	 * 
	 * @Description: TODO
	 * 
	 * @return
	 * 
	 * @see com.baomidou.mybatisplus.extension.activerecord.Model#pkVal()
	 */
	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
	   return this.pkVal();
	}

}