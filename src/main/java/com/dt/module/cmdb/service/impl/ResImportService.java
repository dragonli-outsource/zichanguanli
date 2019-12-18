package com.dt.module.cmdb.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dt.core.cache.CacheConfig;
import com.dt.core.common.base.BaseService;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.sql.Insert;
import com.dt.core.dao.sql.Update;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.cmdb.entity.ResEntity;
import com.dt.module.cmdb.entity.ResImportResultEntity;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * @author: algernonking
 * @date: Nov 3, 2019 8:31:41 AM
 * @Description: TODO
 */
@Service
public class ResImportService extends BaseService {

	@Autowired
	ResExtService resExtService;

	public static void main(String[] args) {
		String sql = "com.dt.module.cmdb.service.ResEntity@333fa13b[uuid=83E6-48C3-B47,classname=存储设备,typestr=<null>,name=存储设备,locstr=营业网点,brandstr=惠普,model=12,sn=22,wbstr=在保,wbout_datestr=2021-08-21,rackstr=<null>,frame=<null>,locdtl= 不知道,recyclestr=未上架,envstr=生产,riskstr=未评级,part_fullname=总行营业部,mgr_part_fullname=<null>,used_username=寿其伟,buy_timestr=2019-11-01,buy_price=0,confdesc=<null>,mark=<null>,processmsg=资产编号不存在]\n";

		System.out.println(sql.substring(sql.indexOf("[")));
		// TODO Auto-generated method stub

	}

	public R demoRun() {
		try {
			ImportParams params = new ImportParams();

			params.setHeadRows(1);
			params.setTitleRows(0);
			params.setStartSheetIndex(0);
			List<ResEntity> result = ExcelImportUtil.importExcel(new File("/Users/algernonking/Downloads/file.xls"),
					ResEntity.class, params);
			executeEntitysImort(result, "update");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return R.SUCCESS_OPER();
	}

	public R importResNormal(String file, String type) {
		R r = R.SUCCESS_OPER();
		try {
			ImportParams params = new ImportParams();

			params.setHeadRows(1);
			params.setTitleRows(0);
			params.setStartSheetIndex(0);
			List<ResEntity> result = ExcelImportUtil.importExcel(new File(file), ResEntity.class, params);
			r = executeEntitysImort(result, type);
		} catch (Exception e) {
			e.printStackTrace();
			return R.FAILURE("导入数据异常");
		}
		return r;
	}

	@Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkBuyPrice'+#value")
	public R checkBuyPrice(String value) {
		if (ToolUtil.isEmpty(value)) {
			return R.SUCCESS_OPER("0.0");
		}
		try {
			// 支持科学计数法
			Double r = new BigDecimal(value.trim()).doubleValue();
			return R.SUCCESS_OPER(r.toString());
		} catch (Exception e) {
			return R.FAILURE("时间转换出错,请优先保证数据正确");
		}
	}

	// 检查时间
	@Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkDateTime'+#value")
	public R checkDateTime(String value) {
		if (ToolUtil.isEmpty(value)) {
			return R.SUCCESS();
		}
		return R.SUCCESS();
	}

	// 检查资产编号
	public int checkUUid(String uuid) {
		return db.uniqueRecord("select count(1) cnt from res where dr='0' and uuid=?", uuid).getInteger("cnt");
	}

	// 检查数据字典
	@Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkDictItem'+#dict+'_'+#name")
	public R checkDictItem(String dict, String name) {
		// 大类为空,则失败
		if ("devclass".equals(dict)) {
			if (ToolUtil.isEmpty(name)) {
				return R.FAILURE("大类不允许为空或该行为空");
			}
		}
		// 其他为空，判断为成功
		if (ToolUtil.isEmpty(name)) {
			return R.SUCCESS_OPER("0");
		}
		Rcd rs = db.uniqueRecord("select dict_item_id from dt.sys_dict_item where  dr='0' and dict_id=? and name=?",
				dict, name);
		if (rs == null) {
			return R.FAILURE("无法匹配数据字典项目:Dict:" + dict + ",value:" + name);
		}
		return R.SUCCESS_OPER(rs.toJsonObject());
	}

	@Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkDictItem'+#name")
	public R checkZCClass(String name) {
		// 大类为空,则失败

		if (ToolUtil.isEmpty(name)) {
			return R.FAILURE("大类不允许为空或该行为空");
		}

		// 其他为空，判断为成功
		if (ToolUtil.isEmpty(name)) {
			return R.SUCCESS_OPER("0");
		}
		Rcd rs = db.uniqueRecord("select * from ct_category  where dr='0' and route_name=?", name);
		if (rs == null) {
			return R.FAILURE("无法匹配大类," + name);
		}

		return R.SUCCESS_OPER(rs.toJsonObject());
	}

//	// 检查数据字典,小类
//	@Cacheable(value = CacheConfig.CACHE_PUBLIC_5_2, key = "'checkDictItemSub'+#dict+'_'+#name+'_'+#dl")
//	public R checkDictItemSub(String dict, String name, String dl) {
//
//		Rcd rs = db.uniqueRecord(
//				"select * from sys_dict_item  where dict_id in (" + dict + ") and dr='0' and name=? and code=?", name,
//				dl);
//		if (rs == null) {
//			return R.FAILURE("无法匹配数据字典项目:Dict:" + dict + ",value:" + name);
//		}
// 
//		return R.SUCCESS_OPER(rs.toJsonObject());
//	}

	public R checkResEntity(ResEntity re, String type, String importlabel) {
		Date date = new Date(); // 获取一个Date对象
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 创建一个格式化日期对象
		String nowtime = simpleDateFormat.format(date);
		String sql = "";
		int uuidR = checkUUid(re.getUuid());

		R buypriceR = checkBuyPrice(re.getBuy_price());
		if (buypriceR.isFailed()) {
			return R.FAILURE(buypriceR.getMessage());
		}
		R checkBuyTimeR = checkDateTime(re.getBuy_price());
		if (checkBuyTimeR.isFailed()) {
			return R.FAILURE(checkBuyTimeR.getMessage());
		}

		R checkwboutdateR = checkDateTime(re.getWbout_datestr());
		if (checkwboutdateR.isFailed()) {
			return R.FAILURE(checkwboutdateR.getMessage());
		}

		// 数据字典选项
		R classR = checkZCClass(re.getClassfullname());
		if (classR.isFailed()) {
			return R.FAILURE(classR.getMessage());
		}

		R rackR = checkDictItem("devrack", re.getRackstr());
		if (rackR.isFailed()) {
			return R.FAILURE(rackR.getMessage());
		}

		R brandR = checkDictItem("devbrand", re.getBrandstr());
		if (brandR.isFailed()) {
			return R.FAILURE(brandR.getMessage());
		}

		R recycleR = checkDictItem("devrecycle", re.getRecyclestr());
		if (recycleR.isFailed()) {
			return R.FAILURE(recycleR.getMessage());
		}

		R wbR = checkDictItem("devwb", re.getWbstr());
		if (wbR.isFailed()) {
			return R.FAILURE(wbR.getMessage());
		}
		R riskR = checkDictItem("devrisk", re.getRiskstr());
		if (riskR.isFailed()) {
			return R.FAILURE(riskR.getMessage());
		}

		R locR = checkDictItem("devdc", re.getLocstr());
		if (locR.isFailed()) {
			return R.FAILURE(locR.getMessage());
		}

		R envR = checkDictItem("devenv", re.getEnvstr());
		if (envR.isFailed()) {
			return R.FAILURE(envR.getMessage());
		}

		R zcwbcomouteR = checkDictItem("zcwbcomoute", re.getWb_autostr());
		if (zcwbcomouteR.isFailed()) {
			return R.FAILURE(zcwbcomouteR.getMessage());
		}

//		// 处理小类
//		String typestr = null;
//		if (ToolUtil.isNotEmpty(re.getTypestr())) {
//			// 支持的小类类型:网点、服务器、电脑、安全设备,IT备件
//			R r = checkDictItemSub("'devsafety','devdotequipment','devservertype','devcompute','devbjpj'", re.getTypestr(),
//					classR.queryDataToJSONObject().getString("dict_item_id"));
//			if (r.isFailed()) {
//				return R.FAILURE(r.getMessage());
//			} else {
//				typestr = r.queryDataToJSONObject().getString("dict_item_id");
//			}
//		}

		if (type.equals("insert")) {
			Insert me = new Insert("res");
			me.set("importlabel", importlabel);
			me.set("id", db.getUUID());
			me.set("dr", "0");
			me.setIf("changestate", "updated");
			me.setIf("create_time", nowtime);
			me.setIf("create_by", this.getUserId());
			me.setIf("update_time", nowtime);
			me.setIf("update_by", this.getUserId());
			/////////////// 开始处理///////////
			me.setIf("fs1", re.getFs1());
			me.setIf("fs2", re.getFs2());
			me.setIf("fs20", re.getFs20());
			me.setIf("frame", re.getFrame());
			me.setIf("model", re.getModel());
			me.setIf("confdesc", re.getConfdesc());
			me.setIf("mark", re.getMark());
			me.setIf("locdtl", re.getLocdtl());
			me.setIf("sn", re.getSn());
			me.setIf("buy_price", re.getBuy_price());
			me.setIf("buy_time", re.getBuy_timestr() == null ? null : re.getBuy_timestr() + " 01:00:00");
			me.setIf("wbout_date", re.getWbout_datestr() == null ? null : re.getWbout_datestr() + " 01:00:00");

			// 数据字典匹配
//			me.setIf("type", typestr);
			me.setIf("class_id", classR.queryDataToJSONObject().getString("id"));
			me.setIf("rack", rackR.queryDataToJSONObject().getString("dict_item_id"));
			me.setIf("brand", brandR.queryDataToJSONObject().getString("dict_item_id"));
			me.setIf("recycle", recycleR.queryDataToJSONObject().getString("dict_item_id"));
			me.setIf("wb", wbR.queryDataToJSONObject().getString("dict_item_id"));
			me.setIf("risk", riskR.queryDataToJSONObject().getString("dict_item_id"));
			me.setIf("loc", locR.queryDataToJSONObject().getString("dict_item_id"));
			me.setIf("env", envR.queryDataToJSONObject().getString("dict_item_id"));
			me.setIf("wb_auto", zcwbcomouteR.queryDataToJSONObject().getString("dict_item_id"));

			// 处理资产编号,必需不存在
			if (ToolUtil.isEmpty(re.getUuid())) {
				// 插入时候，无编号自动生产
				me.setIf("uuid", resExtService.createUuid(ResExtService.UUID_ZC));
			} else {
				// 如果有编号,如果编号重复不可插入,否则按照指定编号插入
				if (uuidR > 0) {
					return R.FAILURE("资产编号重复");
				} else {
					me.setIf("uuid", re.getUuid());
				}
			}
			sql = me.getSQL();
		} else if (type.equals("update")) {
			Update me = new Update("res");
			me.set("importlabel", importlabel);
			me.setIf("changestate", "updated");
			me.setIf("update_time", nowtime);
			me.setIf("update_by", this.getUserId());
			/////////////// 开始处理////////////
			me.setIf("fs1", re.getFs1());
			me.setIf("fs2", re.getFs2());
			me.setIf("fs20", re.getFs20());
			me.setIf("frame", re.getFrame());
			me.setIf("model", re.getModel());
			me.setIf("confdesc", re.getConfdesc());
			me.setIf("mark", re.getMark());
			me.setIf("locdtl", re.getLocdtl());
			me.setIf("sn", re.getSn());
			me.setIf("buy_price", re.getBuy_price());
			me.setIf("buy_time", re.getBuy_timestr() == null ? null : re.getBuy_timestr() + " 01:00:00");
			me.setIf("wbout_date", re.getWbout_datestr() == null ? null : re.getWbout_datestr() + " 01:00:00");

			// 数据字典匹配
//			me.setIf("type", typestr);
			me.setIf("class_id", classR.queryDataToJSONObject().getString("id"));
			me.setIf("rack", rackR.queryDataToJSONObject().getString("dict_item_id"));
			me.setIf("brand", brandR.queryDataToJSONObject().getString("dict_item_id"));
			me.setIf("recycle", recycleR.queryDataToJSONObject().getString("dict_item_id"));
			me.setIf("wb", wbR.queryDataToJSONObject().getString("dict_item_id"));
			me.setIf("risk", riskR.queryDataToJSONObject().getString("dict_item_id"));
			me.setIf("loc", locR.queryDataToJSONObject().getString("dict_item_id"));
			me.setIf("env", envR.queryDataToJSONObject().getString("dict_item_id"));
			me.setIf("wb_auto", zcwbcomouteR.queryDataToJSONObject().getString("dict_item_id"));

			// 处理资产编号,必需一条
			if (uuidR == 1) {
				me.set("uuid", re.getUuid());
			} else {
				return R.FAILURE("资产编号不存在");
			}
			me.where().and("uuid=?", re.getUuid());
			sql = me.getSQL();
		}
		return R.SUCCESS_OPER(sql);
	}

	private ResImportResultEntity checkResEntitys(List<ResEntity> result, String type) {
		String importlabel = ToolUtil.getUUID();
		ResImportResultEntity cres = new ResImportResultEntity();
		for (int i = 0; i < result.size(); i++) {
			R r = checkResEntity(result.get(i), type, importlabel);
			if (r.isSuccess()) {
				cres.addSuccess(r.getData().toString());
			} else {
				result.get(i).setProcessmsg(r.getMessage());
				cres.addFailed(result.get(i));
			}
		}
		return cres;
	}

	public R executeEntitysImort(List<ResEntity> resultdata, String type) {
		ResImportResultEntity result = checkResEntitys(resultdata, type);
		result.printResult();
		if (!result.is_success_all) {
			return R.FAILURE("操作失败", result.covertJSONObjectResult());
		}
		db.executeStringList(result.success_cmds);
		resExtService.checkWbMethod();
		return R.SUCCESS_OPER();

	}

}
