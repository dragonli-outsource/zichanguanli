function config_cmdb($stateProvider, $ocLazyLoadProvider) {
	console.log("App cmdb config");
	$ocLazyLoadProvider.config({
		debug : true
	});
	
	
	$stateProvider.state('zcindex', {
		url : "/zcindex",
		templateUrl : "views/cmdb/zcindex.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([
					 {
                         serie: true,
                         name: 'angular-flot',
                         files: [ 'plugin/flot/jquery.flot.js', 'plugin/flot/jquery.flot.time.js', 'plugin/flot/jquery.flot.tooltip.min.js', 'plugin/flot/jquery.flot.spline.js', 'plugin/flot/jquery.flot.resize.js', 'plugin/flot/jquery.flot.pie.js', 'plugin/flot/curvedLines.js', 'plugin/flot/angular-flot.js', ]
                     },
                     {
					serie : true,
					files : [ 'views/cmdb/zcindex.js?v=' + version ]
				} ]);
			}
		}
	});
	
	
	
	// cmdb
	$stateProvider.state('maintain', {
		abstract : true,
		url : "/maintain",
		templateUrl : "views/common/content.html?v="+version
	}).state('maintain.faultrecord', {
		url : "/maintain_faultrecord?psBtns",
		data: { pageTitle: '报修工作'},
		templateUrl : "views/cmdb/faultrecord.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/faultrecord.js?v=' + version ]
				} ]);
			}
		}
	}).state('maintain.devsearch', {
		url : "/maintain_devsearch?psBtns",
		data: { pageTitle: '设备查询'},
		templateUrl : "views/cmdb/devsearch.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/devsearch.js?v=' + version ]
				} ]);
			}
		}
	}).state('maintain.review', {
		url : "/maintain_review?psBtns",
		data: { pageTitle: '资产复核'},
		templateUrl : "views/cmdb/review.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/review.js?v=' + version ]
				} ]);
			}
		}
	}).state('maintain.dataimport', {
		url : "/maintain_dataimport?psBtns",
		data: { pageTitle: '资产导入'},
		templateUrl : "views/cmdb/dataimport.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/dataimport.js?v=' + version ]
				} ]);
			}
		}
	}).state('maintain.dataexport', {
		url : "/maintain_dataexport",
		data: { pageTitle: '资产导出'},
		templateUrl : "views/cmdb/dataexport.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/dataexport.js?v=' + version ]
				} ]);
			}
		}
	})
	
 
		// 报表
	$stateProvider.state('cmdbresp', {
		abstract : true,
		url : "/cmdbresp",
		templateUrl : "views/common/content.html?v="+version
	}).state('cmdbresp.partzc', {
		url : "/maintain_partzc?psBtns",
		data: { pageTitle: '部门资产'},
		template:'<div ng-controller="cmdbrepPartZcCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/rep/partzj.js?v=' + version ]
				} ]);
			}
		}
	}).state('cmdbresp.zctjshow', {
		url : "/cmdbresp_zctjshow?psBtns",
		data: { pageTitle: '资产统计'},
		templateUrl : "views/cmdb/rep/zctj.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/rep/zctj.js?v=' + version ]
				} ]);
			}
		}
	}).state('cmdbresp.bashboard', {
		url : "/cmdbresp_bashboard?psBtns",
		data: { pageTitle: '展示面板'},
		templateUrl : "views/cmdb/rep/dashboard.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([
					  {
                          serie: true,
                          name: 'angular-flot',
                          files: [ 'plugin/flot/jquery.flot.js', 'plugin/flot/jquery.flot.time.js', 'plugin/flot/jquery.flot.tooltip.min.js', 'plugin/flot/jquery.flot.spline.js', 'plugin/flot/jquery.flot.resize.js', 'plugin/flot/jquery.flot.pie.js', 'plugin/flot/curvedLines.js', 'plugin/flot/angular-flot.js', ]
                      },
					{
					serie : true,
					files : [ 'views/cmdb/rep/dashboard.js?v=' + version ]
				} ]);
			}
		}
	});
	
	
	
	
	$stateProvider.state('cmsetting', {
		abstract : true,
		url : "/cmsetting",
		templateUrl : "views/common/content.html?v="+version
	}).state('cmsetting.zccat', {
		url : "/cmsetting_zccat?psBtns",
		data: { pageTitle: '资产分类'},
		templateUrl : "views/cmdb/zccategory.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/zccategory.js?v=' + version ]
				} ]);
			}
		}
	})
	
	
	$stateProvider.state('myprocess', {
		abstract : true,
		url : "/myprocess",
		templateUrl : "views/common/content.html?v="+version
	}).state('myprocess.waittask', {
		url : "/myprocess_waittask?psBtns",
		data: { pageTitle: '我的待处理任务'},
		templateUrl : "views/cmdb/flow/todo.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/flow/todo.js?v=' + version ]
				} ]);
			}
		}
	}).state('myprocess.taskfinish', {
		url : "/myprocess_taskfinish?psBtns",
		data: { pageTitle: '已处理任务'},
		templateUrl : "views/cmdb/flow/processfinish.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/flow/processfinish.js?v=' + version ]
				} ]);
			}
		}
	}).state('myprocess.myprocess', {
		url : "/myprocess_myprocess?psBtns",
		data: { pageTitle: '我的流程'},
		templateUrl : "views/cmdb/flow/myprocess.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/flow/myprocess.js?v=' + version ]
				} ]);
			}
		}
	})
	;
	// cmdb
	$stateProvider.state('xt', {
		abstract : true,
		url : "/xt",
		templateUrl : "views/common/content.html?v="+version
	}).state('xt.outercontact', {
		url : "/xt_outercontact?psBtns",
		data: { pageTitle: '外部联系人'},
		templateUrl : "views/cmdb/outercontact.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/outercontact.js?v=' + version ]
				} ]);
			}
		}
	})
	.state('xt.systemlist', {
		url : "/xt_systemlist?psBtns",
		data: { pageTitle: '信息系统清单'},
		template:'<div ng-controller="cmdbsystemListCtl" ng-include="\'views/Template/simpleToolTableTempl.html\'"></div>',
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/systemlist.js?v=' + version ]
				} ]);
			}
		}
	});
	
	
	// cmdb
	$stateProvider.state('bjmgr', {
		abstract : true,
		url : "/bjmgr",
		templateUrl : "views/common/content.html?v="+version
	}).state('bjmgr.bjpj', {
		url : "/bjmgr_bjpj",
		data: { pageTitle: 'IT备件配件',classid:'bjpj',input_type:"devbjpj"},
		templateUrl : "views/cmdb/html_genericdev.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/js_genericdev.js?v=' + version ]
				} ]);
			}
		}
	})
	
	 
	$stateProvider.state('zcmgr', {
		abstract : true,
		url : "/zcmgr",
		templateUrl : "views/common/content.html?v="+version
	}).state('zcmgr.zctz', {
		url : "/zcmgr_zctz",
		data: { pageTitle: '资产台账'},
		templateUrl : "views/cmdb/devsearch.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/devsearch.js?v=' + version ]
				} ]);
			}
		}
	}).state('zcmgr.zcdj', {
		url : "/zcmgr_zcdj",
		data: { pageTitle: '资产登记'},
		templateUrl : "views/cmdb/zcdj.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/zcdj.js?v=' + version ]
				} ]);
			}
		}
	}).state('zcmgr.zclygh', {
		url : "/zcmgr_zclygh",
		data: { pageTitle: '资产领用归还',actiontype:"LY"},
		templateUrl : "views/cmdb/zcaction.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/zcaction.js?v=' + version ]
				} ]);
			}
		}
	}).state('zcmgr.jygh', {
		url : "/zcmgr_jygh",
		data: { pageTitle: '资产借用归还',actiontype:"JY"},
		templateUrl : "views/cmdb/zcaction.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/zcaction.js?v=' + version ]
				} ]);
			}
		}
	}).state('zcmgr.zczy', {
		url : "/zcmgr_zczy",
		data: { pageTitle: '我的资产转移',actiontype:"ZY"},
		templateUrl : "views/cmdb/zcaction.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/zcaction.js?v=' + version ]
				} ]);
			}
		}
	})
	
	
	
	
	
	// cmdb
	$stateProvider.state('cf', {
		abstract : true,
		url : "/cf",
		templateUrl : "views/common/content.html?v="+version
	}).state('cf.server', {
		url : "/cf_server?psBtns",
		data: { pageTitle: '服务器',classid:'zc_server',subclass:"Y"},
		templateUrl : "views/cmdb/html_genericdev.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/js_genericdev.js?v=' + version ]
				} ]);
			}
		}
	}) 
	.state('cf.lightsw', {
		url : "/cf_lightsw?psBtns",
		data: { pageTitle: '光纤交换机',classid:'zc_lsw'},
		templateUrl : "views/cmdb/html_genericdev.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/js_genericdev.js?v=' + version ]
				} ]);
			}
		}
	})
	.state('cf.outlets', {
		url : "/cf_outlets?psBtns",
		data: { pageTitle: '网点设备',classid:"zc_website",subclass:"Y"},
		templateUrl : "views/cmdb/html_genericdev.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/js_genericdev.js?v=' + version ]
				} ]);
			}
		}
	}).state('cf.safety', {
		url : "/cf_safety?psBtns",
		data: { pageTitle: '安全设备',classid:'zc_safety',subclass:"Y"},
		templateUrl : "views/cmdb/html_genericdev.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/js_genericdev.js?v=' + version ]
				} ]);
			}
		}
	}).state('cf.storage', {
		url : "/cf_storage?psBtns",
		data: { pageTitle: '存储设备',classid:'zc_storage'},
		templateUrl : "views/cmdb/html_genericdev.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/js_genericdev.js?v=' + version ]
				} ]);
			}
		}
	})
	.state('cf.switch', {
		url : "/cf_switch?psBtns",
		data: { pageTitle: '交换机',classid:"51"},
		templateUrl : "views/cmdb/html_genericdev.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/js_genericdev.js?v=' + version ]
				} ]);
			}
		}
	}).state('cf.zcotherhard', {
		url : "/cf_zcotherhard?psBtns",
		data: { pageTitle: '其他资产',classid:"zc_other",subclass:"Y"},
		templateUrl : "views/cmdb/html_genericdev.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/js_genericdev.js?v=' + version ]
				} ]);
			}
		}
	}).state('cf.network', {
		url : "/cf_network?psBtns",
		data: { pageTitle: '网络设备',classid:"zc_network",subclass:"Y"},
		templateUrl : "views/cmdb/html_genericdev.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/js_genericdev.js?v=' + version ]
				} ]);
			}
		}
	});
	

	$stateProvider.state('softzc', {
		abstract : true,
		url : "/softzc",
		templateUrl : "views/common/content.html?v="+version
	}).state('softzc.soft', {
		url : "/softzc.soft",
		data: { pageTitle: '软件资产',classid:"softzc",input_type:"zcsofttype"},
		templateUrl : "views/cmdb/html_genericdev.html?v="+version,
		resolve : {
			loadPlugin : function($ocLazyLoad) {
				return $ocLazyLoad.load([ {
					serie : true,
					files : [ 'views/cmdb/js_genericdev.js?v=' + version ]
				} ]);
			}
		}
	})
 
	
	
}

function renderName(data, type, full) {
	var html = full.model;
	return html;

}

function renderJg(data, type, full) {
	var html="";
	if(angular.isDefined( full.rackstr)){
		 html = html+full.rackstr+"-";
	}
	if(angular.isDefined( full.frame)){
		 html = html+full.frame;
	}
	return html;
}

function renderReview(data, type, full) {
	if (data == "reviewed") {
		return "已复核"
	} else {
		return "未复核"
	}
}

function renderWb(data, type, full) {
	if (angular.isDefined(full.wb)) {
		if (full.wb == "valid") {
			return full.wbstr
		} else {
			return "<span style=\"color:red\">" + full.wbstr + "</span>"
		}
	} else {
		return "";
	}
}
function loadOpt(modal_meta, gdicts) {

 
	var item = modal_meta.meta.item;
	console.log("LoadOpt,Item:",item);

 
	// 维保自定技术
	modal_meta.meta.tbOpt = gdicts.zcwbcomoute;
 
	if (angular.isDefined( gdicts.zcwbcomoute  )  && modal_meta.meta.tbOpt.length > 0) {
		if (angular.isDefined(item) && angular.isDefined(item.wb_auto)) {
			for (var i = 0; i < gdicts.zcwbcomoute.length; i++) {
			 
				if (modal_meta.meta.tbOpt[i].dict_item_id == item.wb_auto) {
					modal_meta.meta.tbSel = modal_meta.meta.tbOpt[i];
				}
			}
		} else {
			if (gdicts.zcwbcomoute.length > 0) {
				// modal_meta.meta.tbOpt = gdicts.zcwbcomoute[];
			}
		}
	}
	
	// 品牌
	modal_meta.meta.pinpOpt = gdicts.devbrand;
	if (angular.isDefined( gdicts.devbrand )   && modal_meta.meta.pinpOpt.length > 0) {
		if (angular.isDefined(item) && angular.isDefined(item.brand)) {
			for (var i = 0; i < gdicts.devbrand.length; i++) {
			 
				if (modal_meta.meta.pinpOpt[i].dict_item_id == item.brand) {
					modal_meta.meta.pinpSel = modal_meta.meta.pinpOpt[i];
				}
			}
		} else {
			if (gdicts.devbrand.length > 0) {
			}
		}
	}

	// 部门
	modal_meta.meta.partOpt = gdicts.parts;
	if (angular.isDefined( gdicts.parts )  &&gdicts.parts.length > 0) {
		if (angular.isDefined(item) && angular.isDefined(item.part_id)) {
			for (var i = 0; i < gdicts.parts.length; i++) {
				if (gdicts.parts[i].partid == item.part_id) {
					modal_meta.meta.partSel = gdicts.parts[i];
				}
			}
		} else {
			if (gdicts.parts.length > 0) {
				// modal_meta.meta.partSel = gdicts.parts[0];
			}
		}
	}

	// 使用人
	modal_meta.meta.usedunameOpt = gdicts.partusers;
	if (angular.isDefined( gdicts.partusers )    && gdicts.partusers.length > 0) {
		if (angular.isDefined(item) && angular.isDefined(item.used_userid)) {
			for (var i = 0; i < gdicts.partusers.length; i++) {
				if (gdicts.partusers[i].user_id == item.used_userid) {
					modal_meta.meta.usedunameSel = gdicts.partusers[i];
				}
			}
		} else {
			if (gdicts.partusers.length > 0) {
				// modal_meta.meta.usedunameSel = gdicts.partusers[0];
			}
		}
	}

	// 风险等级
	modal_meta.meta.riskOpt = gdicts.devrisk;
	if (angular.isDefined( gdicts.devrisk) && gdicts.devrisk.length > 0) {
		if (angular.isDefined(item) && angular.isDefined(item.risk)) {
			for (var i = 0; i < gdicts.devrisk.length; i++) {
				if (gdicts.devrisk[i].dict_item_id == item.risk) {
					modal_meta.meta.riskSel = gdicts.devrisk[i];
				}
			}
		} else {
			if (gdicts.devrisk.length > 0) {
				modal_meta.meta.riskSel = gdicts.devrisk[0];
			}
		}
	}

	// 环境
	modal_meta.meta.envOpt = gdicts.devenv;
	if (angular.isDefined( gdicts.devenv)   &&  gdicts.devenv.length > 0) {
		if (angular.isDefined(item) && angular.isDefined(item.env)) {
			for (var i = 0; i < gdicts.devenv.length; i++) {
				if (gdicts.devenv[i].dict_item_id == item.env) {
					modal_meta.meta.envSel = gdicts.devenv[i];
				}
			}
		} else {
			if (gdicts.devenv.length > 0) {
				// modal_meta.meta.envSel = gdicts.devenv[0];
			}
		}
	}

	// 状态
	modal_meta.meta.recycelOpt = gdicts.devrecycle;
	if (angular.isDefined( gdicts.devrecycle)     && gdicts.devrecycle.length > 0) {
		if (angular.isDefined(item) && angular.isDefined(item.recycle)) {
			for (var i = 0; i < gdicts.devrecycle.length; i++) {
				if (gdicts.devrecycle[i].dict_item_id == item.recycle) {
					modal_meta.meta.recycelSel = gdicts.devrecycle[i];
				}
			}
		} else {
			if (gdicts.devrecycle.length > 0) {
				modal_meta.meta.recycelSel = gdicts.devrecycle[0];
			}
		}
	}

	// 维保
	modal_meta.meta.wbOpt = gdicts.devwb;
	if (angular.isDefined( gdicts.devwb)   && gdicts.devwb.length > 0) {
		if (angular.isDefined(item) && angular.isDefined(item.wb)) {
			for (var i = 0; i < gdicts.devwb.length; i++) {
				if (gdicts.devwb[i].dict_item_id == item.wb) {
					modal_meta.meta.wbSel = gdicts.devwb[i];
				}
			}
		} else {
			if (gdicts.devwb.length > 0) {
				modal_meta.meta.wbSel = gdicts.devwb[0];
			}
		}
	}

	// 位置
	modal_meta.meta.locOpt = gdicts.devdc;
	if (angular.isDefined( gdicts.devdc)   && gdicts.devdc.length > 0) {
		if (angular.isDefined(item) && angular.isDefined(item.loc)) {
			for (var i = 0; i < gdicts.devdc.length; i++) {
				if (gdicts.devdc[i].dict_item_id == item.loc) {
					modal_meta.meta.locSel = gdicts.devdc[i];
				}
			}
		} else {
			if (gdicts.devdc.length > 0) {
				modal_meta.meta.locSel = gdicts.devdc[0];
			}
		}
	}

	// 小类
	modal_meta.meta.typeOpt = gdicts.stype;
	if (angular.isDefined( gdicts.stype)   && gdicts.stype.length > 0) {
		if (angular.isDefined(item) && angular.isDefined(item.type)) {
			for (var i = 0; i < gdicts.stype.length; i++) {
				if (gdicts.stype[i].dict_item_id == item.type) {
					modal_meta.meta.typeSel = gdicts.stype[i];
				}
			}
		} else {
			if (gdicts.stype.length > 0) {
				modal_meta.meta.typeSel = gdicts.stype[0];
			}
		}
	}

	// 大类
	modal_meta.meta.classOpt = gdicts.btype;
	if (angular.isDefined( gdicts.btype)    &&  gdicts.btype.length > 0) {
		if (angular.isDefined(item) && angular.isDefined(item.class_id)) {
			for (var i = 0; i < gdicts.btype.length; i++) {
				if (gdicts.btype[i].dict_item_id == item.class_id) {
					modal_meta.meta.classSel = gdicts.btype[i];
				}
			}
		} else {
			if (gdicts.btype.length > 0) {
				modal_meta.meta.classSel = gdicts.btype[0];
			}
		}
	}
	
	// 机柜
	modal_meta.meta.jgOpt = gdicts.devrack;
	if (angular.isDefined( gdicts.devrack)    && gdicts.devrack.length > 0) {
		if (angular.isDefined(item) && angular.isDefined(item.rack)) {
			for (var i = 0; i < gdicts.devrack.length; i++) {
				if (gdicts.devrack[i].dict_item_id == item.rack) {
					modal_meta.meta.jgSel = gdicts.devrack[i];
				}
			}
		} else {
			if (gdicts.devrack.length > 0) {
				// modal_meta.meta.jgSel = gdicts.devrack[0];
			}
		}
	}

	
}



function modalzcActionDtlCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
		$confirm, $log, notify, $scope, $http, $rootScope, $uibModal, meta,pagetype,task,
		$uibModalInstance) {

	console.log(meta,task);

	$scope.actmsg = "操作";
	if (meta.acttype == "LY") {
		$scope.actmsg  = "领用人";
	} else if (meta.acttype == "JY") {
		$scope.actmsg = "借用人";
	}else if (meta.acttype == "ZY") {
		actbtn = "转移人";
	}
	
	
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	$scope.ctl={};
	$scope.ctl.dtitle=false;
	$scope.ctl.dct=false;
	$scope.ctl.df1=false;
	$scope.ctl.df2=false;
	$scope.ctl.pagespbtnhide=true;
	$scope.spsuggest="";
	$scope.pagetype=pagetype;
	if($scope.pagetype=="sp"){
		$scope.ctl.pagespbtnhide=false;
	}
	 
	$scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
			.withDOM('frtlip').withPaginationType('simple').withDisplayLength(
					50).withOption("ordering", false).withOption("responsive",
					false).withOption("searching", false).withOption('scrollY',
					'300px').withOption('scrollX', true).withOption(
					'bAutoWidth', true).withOption('scrollCollapse', true)
			.withOption('paging', false).withFixedColumns({
				leftColumns : 0,
				rightColumns : 0
			}).withOption('bStateSave', true).withOption('bProcessing', false)
			.withOption('bFilter', false).withOption('bInfo', false)
			.withOption('serverSide', false).withOption('aaData',
					$scope.tabdata).withOption('createdRow', function(row) {
				$compile(angular.element(row).contents())($scope);
			});
	$scope.dtColumns = [

			DTColumnBuilder.newColumn('uuid').withTitle('编号').withOption(
					'sDefaultContent', '').withOption("width", '30'),
			DTColumnBuilder.newColumn('classname').withTitle('类型').withOption(
					'sDefaultContent', '').withOption("width", '30'),
			DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('name').withTitle('型号').withOption(
					'sDefaultContent', '').withOption('width', '50')
					.renderWith(renderName),
			DTColumnBuilder.newColumn('locstr').withTitle('位置').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('part_name').withTitle('部门').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('used_username').withTitle('使用人')
					.withOption('sDefaultContent', '')
					.withOption('width', '30'),
			DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态')
					.withOption('sDefaultContent', '')
					.withOption('width', '30'),
			DTColumnBuilder.newColumn('confdesc').withTitle('配置描述').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('sn').withTitle('序列号').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('buy_timestr').withTitle('采购时间')
					.withOption('sDefaultContent', '') ]

	$scope.dtOptions.aaData = [];

	$scope.dtOptions2 = DTOptionsBuilder.fromFnPromise().withDataProp('data')
			.withDOM('frtlip').withPaginationType('simple').withDisplayLength(
					50).withOption("ordering", false).withOption("responsive",
					false).withOption("searching", false).withOption('scrollY',
					'300px').withOption('scrollX', true).withOption(
					'bAutoWidth', true).withOption('scrollCollapse', true)
			.withOption('paging', false).withFixedColumns({
				leftColumns : 0,
				rightColumns : 0
			}).withOption('bStateSave', true).withOption('bProcessing', false)
			.withOption('bFilter', false).withOption('bInfo', false)
			.withOption('serverSide', false).withOption('aaData',
					$scope.tabdata).withOption('createdRow', function(row) {
				$compile(angular.element(row).contents())($scope);
			});
	$scope.dtColumns2 = [
			DTColumnBuilder.newColumn('assigneename').withTitle('审批人').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('taskName').withTitle('任务名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('state').withTitle('状态').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('opinion').withTitle('审批意见').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('endDate').withTitle('处理时间').withOption(
					'sDefaultContent', '') ]

	$scope.dtOptions2.aaData = [];

	$scope.url = "";
	
	// 显示审批页面
	$http
			.post($rootScope.project + "/api/cmdb/resActionExt/selectById.do",
					{
						id : meta.id
					})
			.success(
					function(res) {
						if (res.success) {
							$scope.data = res.data;
							$scope.ctl.dtitle=true;
							$scope.ctl.dct=true;
							$scope.ctl.df1=true;
							$scope.ctl.df2=true;
							
							
							$scope.dtOptions.aaData = res.data.items;
							if (angular.isDefined(res.data.dmethod)
									&& res.data.dmethod == "1") {
								var url = $rootScope.project
										+ "uflo/diagram?processInstanceId="
										+ res.data.processInstanceId;
								$scope.url = url;
								$http
										.post(
												$rootScope.project
														+ "/api/flow/loadProcessInstanceData.do",
												{
													processInstanceId : res.data.processInstanceId
												})
										.success(
												function(res) {
													if (res.success) {
														$scope.dtOptions2.aaData = res.data;
													} else {
														notify({
															message : res.message
														});
													}
												})

							}
							var html = "未知"
							if (angular.isDefined(res.data.pstatusdtl)) {
								if (res.data.pstatusdtl == "submitforapproval") {
									html = "待送审"
								} else if (res.data.pstatusdtl == "inreview") {
									html = "审批中"
								} else if (res.data.pstatusdtl == "success") {
									html = "审批通过"
								} else if (res.data.pstatusdtl == "failed") {
									html = "审批失败"
								} else if (res.data.pstatusdtl == "cancel") {
									html = "审批取消"
								}
							}
							$scope.data.spstatusstr = html;
							if (angular.isDefined(res.data.dmethod)) {
								if (res.data.dmethod == "1") {
									$scope.data.dmethodstr = "需要审批"
								} else {
									$scope.data.dmethodstr = "无需审批"
								}
							}

						} else {
							notify({
								message : res.message
							});
						}
					})

	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	
	$scope.agreen=function(){
		if($scope.spsuggest.length==0){
			notify({
				message :"请输入审批意见"
			});
			return;
		}
		if($scope.spsuggest.length>248){
			notify({
				message :"已超过248,请缩减字数。"
			});
			return;
		}
		$http
		.post($rootScope.project + "/api/cmdb/flow/zc/computeTask.do",
				{taskId:task.id,opinion:$scope.spsuggest}).success(function(res) {
			if (res.success) {
				$uibModalInstance.close('OK');
			} else {
				notify({
					message : res.message
				});
			}
		})
 
	}
	
	$scope.refuse=function(){
		if($scope.spsuggest.length==0){
			notify({
				message :"请输入审批意见"
			});
			return;
		}
		if($scope.spsuggest.length>248){
			notify({
				message :"已超过248,请缩减字数。"
			});
			return;
		}
		$http
		.post($rootScope.project + "/api/cmdb/flow/zc/refuseTask.do",
				{taskId:task.id,opinion:$scope.spsuggest}).success(function(res) {
			if (res.success) {
				$uibModalInstance.close('OK');
			} else {
				notify({
					message : res.message
				});
			}
		})
		
	}

}