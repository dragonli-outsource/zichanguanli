package com.dt.module.flow.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
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
 * @since 2019-12-03
 */
 
@TableName("sys_process_data")
 
public class SysProcessData extends BaseModel<SysProcessData> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    @TableField("busid")
    private String busid;
    @TableField("ptitle")
    private String ptitle;
    @TableField("pstartuserid")
    private String pstartuserid;
    @TableField("pstartusername")
    private String pstartusername;
    @TableField("processkey")
    private String processkey;
    @TableField("processname")
    private String processname;
    @TableField("processversion")
    private String processversion;
    @TableField("processInstanceId")
    private String processInstanceId;
    @TableField("pstatus")
    private String pstatus;
    @TableField("pstatusdtl")
    private String pstatusdtl;
    @TableField("ptype")
    private String ptype;
    @TableField("duuid")
    private String duuid;
    @TableField("dtitle")
    private String dtitle;
    @TableField("dct")
    private String dct;
    @TableField("dname")
    private String dname;
    @TableField("dmark")
    private String dmark;
    @TableField("dmessage")
    private String dmessage;
    @TableField("dsex")
    private String dsex;
    @TableField("dstatus")
    private String dstatus;
    @TableField("dtype")
    private String dtype;
    @TableField("dpwd")
    private String dpwd;
    @TableField("daddr")
    private String daddr;
    @TableField("dcontact")
    private String dcontact;
    @TableField("dpic")
    private String dpic;
    @TableField("dfile")
    private String dfile;
    @TableField("duser")
    private String duser;
    @TableField("dresult")
    private String dresult;
    @TableField("dtotal")
    private BigDecimal dtotal;
    @TableField("dbacktime")
    private Date dbacktime;
    @TableField("df1")
    private String df1;
    @TableField("df2")
    private String df2;
    @TableField("df3")
    private String df3;
    @TableField("df4")
    private String df4;
    @TableField("df5")
    private String df5;
    @TableField("df6")
    private String df6;
    @TableField("df7")
    private String df7;
    @TableField("df8")
    private String df8;
    @TableField("df9")
    private String df9;
    @TableField("df10")
    private String df10;
    @TableField("dn1")
    private BigDecimal dn1;
    @TableField("dn2")
    private BigDecimal dn2;
    @TableField("dn3")
    private BigDecimal dn3;
    @TableField("dn4")
    private BigDecimal dn4;
    @TableField("dn5")
    private BigDecimal dn5;
    @TableField("dn6")
    private BigDecimal dn6;
    @TableField("dn7")
    private BigDecimal dn7;
    @TableField("dn8")
    private BigDecimal dn8;
    @TableField("dn9")
    private BigDecimal dn9;
    @TableField("dn10")
    private BigDecimal dn10;
    @TableField("dmethod")
    private String dmethod;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusid() {
        return busid;
    }

    public void setBusid(String busid) {
        this.busid = busid;
    }

    public String getPtitle() {
        return ptitle;
    }

    public void setPtitle(String ptitle) {
        this.ptitle = ptitle;
    }

    public String getPstartuserid() {
        return pstartuserid;
    }

    public void setPstartuserid(String pstartuserid) {
        this.pstartuserid = pstartuserid;
    }

    public String getPstartusername() {
        return pstartusername;
    }

    public void setPstartusername(String pstartusername) {
        this.pstartusername = pstartusername;
    }

    public String getProcesskey() {
        return processkey;
    }

    public void setProcesskey(String processkey) {
        this.processkey = processkey;
    }

    public String getProcessname() {
        return processname;
    }

    public void setProcessname(String processname) {
        this.processname = processname;
    }

    public String getProcessversion() {
        return processversion;
    }

    public void setProcessversion(String processversion) {
        this.processversion = processversion;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    public String getPstatusdtl() {
        return pstatusdtl;
    }

    public void setPstatusdtl(String pstatusdtl) {
        this.pstatusdtl = pstatusdtl;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getDuuid() {
        return duuid;
    }

    public void setDuuid(String duuid) {
        this.duuid = duuid;
    }

    public String getDtitle() {
        return dtitle;
    }

    public void setDtitle(String dtitle) {
        this.dtitle = dtitle;
    }

    public String getDct() {
        return dct;
    }

    public void setDct(String dct) {
        this.dct = dct;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDmark() {
        return dmark;
    }

    public void setDmark(String dmark) {
        this.dmark = dmark;
    }

    public String getDmessage() {
        return dmessage;
    }

    public void setDmessage(String dmessage) {
        this.dmessage = dmessage;
    }

    public String getDsex() {
        return dsex;
    }

    public void setDsex(String dsex) {
        this.dsex = dsex;
    }

    public String getDstatus() {
        return dstatus;
    }

    public void setDstatus(String dstatus) {
        this.dstatus = dstatus;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getDpwd() {
        return dpwd;
    }

    public void setDpwd(String dpwd) {
        this.dpwd = dpwd;
    }

    public String getDaddr() {
        return daddr;
    }

    public void setDaddr(String daddr) {
        this.daddr = daddr;
    }

    public String getDcontact() {
        return dcontact;
    }

    public void setDcontact(String dcontact) {
        this.dcontact = dcontact;
    }

    public String getDpic() {
        return dpic;
    }

    public void setDpic(String dpic) {
        this.dpic = dpic;
    }

    public String getDfile() {
        return dfile;
    }

    public void setDfile(String dfile) {
        this.dfile = dfile;
    }

    public String getDuser() {
        return duser;
    }

    public void setDuser(String duser) {
        this.duser = duser;
    }

    public String getDresult() {
        return dresult;
    }

    public void setDresult(String dresult) {
        this.dresult = dresult;
    }

    public BigDecimal getDtotal() {
        return dtotal;
    }

    public void setDtotal(BigDecimal dtotal) {
        this.dtotal = dtotal;
    }

    public Date getDbacktime() {
        return dbacktime;
    }

    public void setDbacktime(Date dbacktime) {
        this.dbacktime = dbacktime;
    }

    public String getDf1() {
        return df1;
    }

    public void setDf1(String df1) {
        this.df1 = df1;
    }

    public String getDf2() {
        return df2;
    }

    public void setDf2(String df2) {
        this.df2 = df2;
    }

    public String getDf3() {
        return df3;
    }

    public void setDf3(String df3) {
        this.df3 = df3;
    }

    public String getDf4() {
        return df4;
    }

    public void setDf4(String df4) {
        this.df4 = df4;
    }

    public String getDf5() {
        return df5;
    }

    public void setDf5(String df5) {
        this.df5 = df5;
    }

    public String getDf6() {
        return df6;
    }

    public void setDf6(String df6) {
        this.df6 = df6;
    }

    public String getDf7() {
        return df7;
    }

    public void setDf7(String df7) {
        this.df7 = df7;
    }

    public String getDf8() {
        return df8;
    }

    public void setDf8(String df8) {
        this.df8 = df8;
    }

    public String getDf9() {
        return df9;
    }

    public void setDf9(String df9) {
        this.df9 = df9;
    }

    public String getDf10() {
        return df10;
    }

    public void setDf10(String df10) {
        this.df10 = df10;
    }

    public BigDecimal getDn1() {
        return dn1;
    }

    public void setDn1(BigDecimal dn1) {
        this.dn1 = dn1;
    }

    public BigDecimal getDn2() {
        return dn2;
    }

    public void setDn2(BigDecimal dn2) {
        this.dn2 = dn2;
    }

    public BigDecimal getDn3() {
        return dn3;
    }

    public void setDn3(BigDecimal dn3) {
        this.dn3 = dn3;
    }

    public BigDecimal getDn4() {
        return dn4;
    }

    public void setDn4(BigDecimal dn4) {
        this.dn4 = dn4;
    }

    public BigDecimal getDn5() {
        return dn5;
    }

    public void setDn5(BigDecimal dn5) {
        this.dn5 = dn5;
    }

    public BigDecimal getDn6() {
        return dn6;
    }

    public void setDn6(BigDecimal dn6) {
        this.dn6 = dn6;
    }

    public BigDecimal getDn7() {
        return dn7;
    }

    public void setDn7(BigDecimal dn7) {
        this.dn7 = dn7;
    }

    public BigDecimal getDn8() {
        return dn8;
    }

    public void setDn8(BigDecimal dn8) {
        this.dn8 = dn8;
    }

    public BigDecimal getDn9() {
        return dn9;
    }

    public void setDn9(BigDecimal dn9) {
        this.dn9 = dn9;
    }

    public BigDecimal getDn10() {
        return dn10;
    }

    public void setDn10(BigDecimal dn10) {
        this.dn10 = dn10;
    }

    public String getDmethod() {
        return dmethod;
    }

    public void setDmethod(String dmethod) {
        this.dmethod = dmethod;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysProcessData{" +
        "id=" + id +
        ", busid=" + busid +
        ", ptitle=" + ptitle +
        ", pstartuserid=" + pstartuserid +
        ", pstartusername=" + pstartusername +
        ", processkey=" + processkey +
        ", processname=" + processname +
        ", processversion=" + processversion +
        ", processInstanceId=" + processInstanceId +
        ", pstatus=" + pstatus +
        ", pstatusdtl=" + pstatusdtl +
        ", ptype=" + ptype +
        ", duuid=" + duuid +
        ", dtitle=" + dtitle +
        ", dct=" + dct +
        ", dname=" + dname +
        ", dmark=" + dmark +
        ", dmessage=" + dmessage +
        ", dsex=" + dsex +
        ", dstatus=" + dstatus +
        ", dtype=" + dtype +
        ", dpwd=" + dpwd +
        ", daddr=" + daddr +
        ", dcontact=" + dcontact +
        ", dpic=" + dpic +
        ", dfile=" + dfile +
        ", duser=" + duser +
        ", dresult=" + dresult +
        ", dtotal=" + dtotal +
        ", dbacktime=" + dbacktime +
        ", df1=" + df1 +
        ", df2=" + df2 +
        ", df3=" + df3 +
        ", df4=" + df4 +
        ", df5=" + df5 +
        ", df6=" + df6 +
        ", df7=" + df7 +
        ", df8=" + df8 +
        ", df9=" + df9 +
        ", df10=" + df10 +
        ", dn1=" + dn1 +
        ", dn2=" + dn2 +
        ", dn3=" + dn3 +
        ", dn4=" + dn4 +
        ", dn5=" + dn5 +
        ", dn6=" + dn6 +
        ", dn7=" + dn7 +
        ", dn8=" + dn8 +
        ", dn9=" + dn9 +
        ", dn10=" + dn10 +
        ", dmethod=" + dmethod +
        "}";
    }
}
