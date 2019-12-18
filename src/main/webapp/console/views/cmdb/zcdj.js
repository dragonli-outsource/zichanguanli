
function genericzcdjCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
		$log, notify,$scope, $http, $rootScope, $uibModal, $window,$state) {

	var pbtns=$rootScope.curMemuBtns;
	var gclass_id='normalclass';
	$scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data').withDOM('frtlip')
			.withPaginationType('full_numbers').withDisplayLength(100)
			.withOption("ordering", false).withOption("responsive", false)
			.withOption("searching", false).withOption('scrollY', '600px')
			.withOption('scrollX', true).withOption('bAutoWidth', true)
			.withOption('scrollCollapse', true).withOption('paging', true)
			.withFixedColumns({
				leftColumns : 0,
				rightColumns : 0
			}).withOption('bStateSave', true).withOption('bProcessing', false)
			.withOption('bFilter', false).withOption('bInfo', false)
			.withOption('serverSide', false).withOption('aaData',
					$scope.tabdata).withOption('createdRow', function(row) {
				$compile(angular.element(row).contents())($scope);
			}).withOption(
					'headerCallback',
					function(header) {
						if ((!angular.isDefined($scope.headerCompiled))
								|| $scope.headerCompiled) {
							$scope.headerCompiled = true;
							$compile(angular.element(header).contents())
									($scope);
						}
					}).withOption("select", {
				style : 'multi',
				selector : 'td:first-child' 
			}).withButtons([
	            {
	                extend: 'colvis',
	                text: '显示隐藏列',
	                fnLabel: function ( dt, idx, title ) {
	                	console.log(dt,idx,title);
	                    return (idx+1)+': '+title;
	                } 
	            },
	            {
	            	extend:'csv',
	                text: 'Excel(当前页)',
	                exportOptions: {
	                	columns: ':visible',
	                	trim:true,
	                    modifier: {
	                        page: 'current'
	                    }
	                }
	            },
	            {
	            	extend:'print',
	                text: '打印(当前页)',
	                exportOptions: {
	                	 columns: ':visible',
	                	 stripHtml: false,
	                	 columns: ':visible',
	                	 modifier: {
		                        page: 'current'
		                    }
	                }
	            }
	        ]);

	function stateChange(iColumn, bVisible) {
        console.log('The column', iColumn, ' has changed its status to', bVisible);
    }
	$scope.dtInstance = {}
	$scope.selectCheckBoxAll = function(selected) {
		if (selected) {
			$scope.dtInstance.DataTable.rows().select();
			console.log($scope.dtInstance.DataTable)
				console.log($scope.dtInstance);
		} else {
			$scope.dtInstance.DataTable.rows().deselect();
			console.log($scope.dtInstance.DataTable)
			console.log($scope.dtInstance);
		}
	}

	var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';
	$scope.dtColumns = [];
	$scope.dtColumns.push(DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
	'select-checkbox checkbox_center').renderWith(function() {
		return ""
	}));
	$scope.dtColumns.push(DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
			'sDefaultContent', '').withOption("width", '30'));
	$scope.dtColumns.push(DTColumnBuilder.newColumn('classname').withTitle('资产类型').withOption(
				 'sDefaultContent', '').withOption("width", '30'));
	$scope.dtColumns.push(DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
			 'sDefaultContent', '').withOption('width', '30'));			 
	$scope.dtColumns.push( DTColumnBuilder.newColumn('name').withTitle('型号').withOption(
			 'sDefaultContent', '').withOption('width', '50')
			 .renderWith(renderName));		
	$scope.dtColumns.push( DTColumnBuilder.newColumn('locstr').withTitle('位置').withOption(
			 'sDefaultContent', '').withOption('width', '30'));		
	$scope.dtColumns.push( DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态').withOption(
			 'sDefaultContent', '').withOption('width', '30'));		
	$scope.dtColumns.push(  DTColumnBuilder.newColumn('sn').withTitle('序列号').withOption(
			 'sDefaultContent', ''));	
	$scope.dtColumns.push(  DTColumnBuilder.newColumn('confdesc').withTitle('配置描述').withOption(
			 'sDefaultContent', ''));	
	$scope.dtColumns.push(  DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
			 'sDefaultContent', ''));	
	$scope.dtColumns.push( DTColumnBuilder.newColumn('buy_timestr').withTitle('采购时间')
			 .withOption('sDefaultContent', ''));	
	$scope.dtColumns.push( DTColumnBuilder.newColumn('wbstr').withTitle('维保状态').withOption(
			 'sDefaultContent', '').withOption('width', '30').renderWith(renderWb));
	$scope.dtColumns.push(  DTColumnBuilder.newColumn('wbout_datestr').withTitle('脱保时间')
			 .withOption('sDefaultContent', ''));			
	

	$scope.query = function() {
		flush();
	}

	var meta = {
		tablehide : false,
		toolsbtn : [
				{
					id : "btn",
					label : "",
					type : "btn",
					show:true,
					template : ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">搜索</button>'
				},
				{
					id : "btn2",
					label : "",
					type : "btn",
					show:false,
					priv:"insert",
					template : ' <button ng-click="save(0)" class="btn btn-sm btn-primary" type="submit">新增</button>'
				},
				{
					id : "btn2",
					label : "",
					type : "btn",
					show:false,
					priv:"update",
					template : ' <button ng-click="save(1)" class="btn btn-sm btn-primary" type="submit">更新</button>'
				},				
				{
					id : "btn2",
					label : "",
					type : "btn",
					show:true,
					template : ' <button ng-click="detail()" class="btn btn-sm btn-primary" type="submit">详情</button>'
				},
				{
					id : "btn2",
					label : "",
					type : "btn",
					show:false,
					priv:"remove",
					template : ' <button ng-click="del()" class="btn btn-sm btn-primary" type="submit">删除</button>'
				},
				{
					id : "btn3",
					label : "",
					type : "btn",
					show:false,
					priv:"exportfile",
					template : ' <button ng-click="filedown()" class="btn btn-sm btn-primary" type="submit">全部导出(Excel)</button>'
				} ],
		tools : [ {
			id : "select",
			label : "位置",
			type : "select",
			disablesearch : true,
			show:true,
			dataOpt : [],
			dataSel : ""
		}, {
			id : "select",
			label : "状态",
			type : "select",
			disablesearch : true,
			show:true,
			dataOpt : [],
			dataSel : ""
		}, {
			id : "input",
			show:true,
			label : "内容",
			placeholder : "输入型号、编号、序列号",
			type : "input",
			ct : ""
		} ]
	};
 
	$scope.meta=meta;
	privNormalCompute($scope.meta.toolsbtn, pbtns);

	function flush() {
		var ps = {}
		ps.class_id = gclass_id;
		ps.loc = $scope.meta.tools[0].dataSel.dict_item_id;
		ps.recycle = $scope.meta.tools[1].dataSel.dict_item_id;
		ps.search = $scope.meta.tools[2].ct;
		$http.post($rootScope.project + "/api/base/res/queryResAllByClass.do", ps)
				.success(function(res) {
					if (res.success) {
						$scope.dtOptions.aaData = res.data;
					} else {
						notify({
							message : res.message
						});
					}
				})
				 	
	}

	$scope.filedown = function() {
		var ps = {}
		ps.id = gclass_id;
		ps.loc = $scope.meta.tools[0].dataSel.dict_item_id;
		ps.recycle = $scope.meta.tools[1].dataSel.dict_item_id;
		ps.search = $scope.meta.tools[2].ct;
		$window.open($rootScope.project
				+ "/api/base/res/exportServerData.do?id=" + ps.id + "&loc="
				+ ps.loc + "&env=" + ps.env + "&wb=" + ps.wb + "&recycle="
				+ ps.recycle + "&search=" + ps.search);
	}

	var gdicts = {};
	var dicts = "zcwbcomoute,devbrand,devrecycle,devwb,devdc";
	
	// 判断输入框
	var subclass="N";
	if (angular.isDefined($state.router.globals.current.data.subclass)) {
		subclass=gclass_id;
	}
	$http
			.post($rootScope.project + "/api/base/res/queryDictFast.do", {
				dicts : dicts,
				parts : "Y",
				partusers : "Y",
				normalclass:"Y",
			})
			.success(
					function(res) {
						if (res.success) {
							gdicts = res.data;
							
					
 						
							// 未使用
							gdicts.stype = [];
							
							// 填充行数据
							var tenv = [];
							angular.copy(gdicts.devenv, tenv);
							var twb = [];
							angular.copy(gdicts.devwb, twb);
							var tloc = [];
							angular.copy(gdicts.devdc, tloc);
							var trecycle = [];
							angular.copy(gdicts.devrecycle, trecycle);
							var parts = [];
							angular.copy(gdicts.parts, parts);
							var partusers = [];
							angular.copy(gdicts.partusers, partusers);
							
							tloc.unshift({
								dict_item_id : "all",
								name : "全部"
							});
							$scope.meta.tools[0].dataOpt = tloc;
							$scope.meta.tools[0].dataSel = tloc[0];
							
						
							trecycle.unshift({
								dict_item_id : "all",
								name : "全部"
							});
							$scope.meta.tools[1].dataOpt = trecycle;
							$scope.meta.tools[1].dataSel = trecycle[0];
							flush();
						} else {
							notify({
								message : res.message
							});
						}
					})
					
					
					
	function getSelectRows() {
		var data = $scope.dtInstance.DataTable.rows({
			selected : true
		})[0];
		if (data.length == 0) {
			notify({
				message : "请至少选择一项"
			});
			return;
		} else if (data.length > 1000) {
			notify({
				message : "不允许超过1000个"
			});
			return;
		} else {
			var res = [];
			console.log("sel:", data);
			for (var i = 0; i < data.length; i++) {
				res.push($scope.dtOptions.aaData[data[i]].id)
			}
			return angular.toJson(res);
		}
	}	
	$scope.batchupate=function(){
	
		var selrows=getSelectRows();
		var ps={};
		if (angular.isDefined(selrows)) {
			ps.selrows=selrows;
			ps.gdicts=gdicts;
			var modalInstance = $uibModal.open({
				backdrop : true,
				templateUrl : 'views/cmdb/modal_batchUpdateRes.html',
				controller : modalresBatchUpdateCtl,
				size : 'blg',
				resolve : { // 调用控制器与modal控制器中传递值
					meta : function() {
						return ps;
					}
				}
			});

			modalInstance.result.then(function(result) {
				$log.log("result", result);
				if (result == "OK") {
					flush();
				}
		 
 
			}, function(reason) {
				// 点击空白区域，总会输出backdrop click，点击取消，则会cancel
				$log.log("reason", reason)
			}); 
		}
	}

	$scope.del = function() {
		var selrows=getSelectRows();
		console.log(selrows);
		if (angular.isDefined(selrows)) {
			$confirm({
				text : '是否删除?'
			}).then(
					function() {
						$http.post(
								$rootScope.project
										+ "/api/base/res/deleteByIds.do", {
									ids : selrows
								}).success(function(res) {
							if (res.success) {
								flush();
							} else {
								notify({
									message : res.message
								});
							}
						});
					});
		}

	}

	$scope.detail = function() {
		var id = "";
		var selrow = getSelectRow();
		if (angular.isDefined(selrow)) {
			id = selrow.id;
		} else {
			return;
		}
		var ps = {};
		ps.id = id;
		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/cmdb/modal_dtl.html',
			controller : modalcmdbdtlCtl,
			size : 'blg',
			resolve : { // 调用控制器与modal控制器中传递值
				meta : function() {
					return ps;
				}
			}
		});

		modalInstance.result.then(function(result) {
			$log.log("result", result);
			if (result == "OK") {
			}
		}, function(reason) {
			// 点击空白区域，总会输出backdrop click，点击取消，则会cancel
			$log.log("reason", reason)
		});

	}

	function getSelectRow() {
		var data = $scope.dtInstance.DataTable.rows({
			selected : true
		})[0];
		if (data.length == 0) {
			notify({
				message : "请至少选择一项"
			});
			return;
		} else if (data.length > 1) {
			notify({
				message : "请最多选择一项"
			});
			return;
		} else {
			console.log("sel:", data);
			return $scope.dtOptions.aaData[data[0]];
		}
	}

	
	// //////////////////////////save/////////////////////
	$scope.save = function(type) {
		var id;
		if (type == 1) {
			var selrow = getSelectRow();
			if (angular.isDefined(selrow)) {
				id = selrow.id;
			} else {
				return;
			}
		}
		$http
				.post($rootScope.project + "/api/base/res/queryResAllById.do", {
					id : id,
					classId : gclass_id
				})
				.success(
						function(res) {
							console.log(res.data);
							if (!res.success) {
								notify({
									message : res.message
								});
								return;
							}
							var meta = {};

							var items = [ ]; 
							items.push({
								type : "input",
								disabled : "true",
								sub_type : "text",
								required : false,
								maxlength : "50",
								placeholder : "系统自动生产",
								label : "资产编号",
								need : false,
								name : 'uuid',
								ng_model : "uuid"
							});
							
						 
							items.push({
									type : "select",
									disabled : "true",
									label : "资产类型",
									need : false,
									disable_search : "false",
									dataOpt : "classOpt",
									dataSel : "classSel"
								});
						
 					 
							items.push({
								type : "input",
								disabled : "false",
								sub_type : "text",
								required : true,
								maxlength : "50",
								placeholder : "请输型号",
								label : "资产型号",
								need : true,
								name : 'model',
								ng_model : "model"
							});
							items.push({
								type : "input",
								disabled : "false",
								sub_type : "text",
								required : false,
								maxlength : "50",
								placeholder : "请输入序列号",
								label : "序列号",
								need : false,
								name : 'sn',
								ng_model : "sn"
							});		
							
							items.push( {
								type : "select",
								disabled : "false",
								label : "资产状态",
								need : false,
								disable_search : "true",
								dataOpt : "recycelOpt",
								dataSel : "recycelSel"
							});

							items.push({
								type : "input",
								disabled : "false",
								sub_type : "number",
								required : false,
								maxlength : "50",
								placeholder : "",
								label : "资产数量",
								need : false,
								name : 'zc_cnt',
								ng_model : "zc_cnt"
							});
							
						
							items.push( {
								type : "select",
								disabled : "false",
								label : "资产品牌",
								need : false,
								disable_search : "true",
								dataOpt : "pinpOpt",
								dataSel : "pinpSel"
							}); 
						 
							items.push( {
								type : "select",
								disabled : "false",
								label : "脱保计算",
								need : false,
								disable_search : "true",
								dataOpt : "tbOpt",
								dataSel : "tbSel"
							});
							items.push( {
								type : "datetime",
								disabled : "false",
								label : "脱保时间",
								need : false,
								ng_model : "wboutdate"
							});
						 
							items.push( {
								type : "select",
								disabled : "false",
								label : "维保状态",
								false : true,
								disable_search : "true",
								dataOpt : "wbOpt",
								dataSel : "wbSel"
							});

							items.push( {
								type : "select",
								disabled : "false",
								label : "资产位置",
								need : false,
								disable_search : "true",
								dataOpt : "locOpt",
								dataSel : "locSel"
							});
						 
						 
							items.push({
								type : "datetime",
								disabled : "false",
								label : "采购时间",
								false : true,
								ng_model : "buytime"
							});
							items.push({
								type : "input",
								disabled : "false",
								sub_type : "number",
								required : false,
								maxlength : "30",
								placeholder : "请输入采购价格",
								label : "采购单价",
								need : false,
								name : 'buy_price',
								ng_model : "buy_price"
							});
							items.push({
								type : "input",
								disabled : "false",
								sub_type : "number",
								required : false,
								maxlength : "30",
								placeholder : "请输入资产净值",
								label : "资产净值",
								need : false,
								name : 'net_worth',
								ng_model : "net_worth"
							});
							items.push({
								type : "select",
								disabled : "false",
								label : "使用部门",
								need : false,
								disable_search : "false",
								dataOpt : "partOpt",
								dataSel : "partSel"
							});
							items.push({
								type : "select",
								disabled : "false",
								label : "使用人",
								need : false,
								disable_search : "false",
								dataOpt : "usedunameOpt",
								dataSel : "usedunameSel"
							});
							items.push( {
								type : "input",
								disabled : "false",
								sub_type : "text",
								required : false,
								maxlength : "50",
								placeholder : "请输入配置描述",
								label : "配置描述",
								need : false,
								name : 'confdesc',
								ng_model : "confdesc"
							});
							
							items.push({
								type : "input",
								disabled : "false",
								sub_type : "text",
								required : false,
								maxlength : "50",
								placeholder : "请输入标签1",
								label : "标签1",
								need : false,
								name : 'fs1',
								ng_model : "fs1"
							});
							
							items.push({
								type : "input",
								disabled : "false",
								sub_type : "text",
								required : false,
								maxlength : "50",
								placeholder : "请输入标签",
								label : "标签2",
								need : false,
								name : 'fs2',
								ng_model : "fs2"
							});
							
							items.push({
								type : "input",
								disabled : "false",
								sub_type : "text",
								required : false,
								maxlength : "50",
								placeholder : "",
								label : "其他资产编号",
								need : false,
								name : 'fs20',
								ng_model : "fs20"
							});
					
							
							items.push({
								type : "input",
								disabled : "false",
								sub_type : "text",
								required : false,
								maxlength : "50",
								placeholder : "请输入备注",
								label : "备注",
								need : false,
								name : 'mark',
								ng_model : "mark"
							});
						 

							var bt = moment().subtract(1, "days");
							var tbtime = moment();

							if (angular.isDefined(res.data)
									&& angular.isDefined(res.data.data)
									&& angular
											.isDefined(res.data.data.buy_timestr)) {
								bt = moment(res.data.data.buy_timestr);
							}
							if (angular.isDefined(res.data)
									&& angular.isDefined(res.data.data)
									&& angular
											.isDefined(res.data.data.wbout_datestr)) {
								tbtime = moment(res.data.data.wbout_datestr);
							}

							meta = {
								class_id : gclass_id,
								footer_hide : false,
								title : "资产-"+$state.router.globals.current.data.pageTitle,
								item : {},
								buytime : bt,
								typeOpt:[],
								typeSel:"",
								wboutdate : tbtime,
								statusOpt : [],
								statusSel : "",
								pinpOpt : [],
								pinpSel : "",
								headuserOpt : [],
								headuserSel : "",
								partOpt : [],
								partSel : "",
								classOpt:[],
								classSel:[],
								usedunameOpt : [],
								usedunameSel : "",
								locOpt : [],
								locSel : "",
								wbOpt : [],
								wbSel : "",
								envOpt : [],
								tbOpt : [],
								tbSel : "",
								envSel : "",
								jgOpt : [],
								jgSel : "",
								riskOpt : [],
								riskSel : "",
								items : items,
								sure : function(modalInstance, modal_meta) {
									console.log('sure set', modal_meta.meta)
  
									modal_meta.meta.item.class_id = modal_meta.meta.classSel.dict_item_id;
									
									if(angular.isDefined(modal_meta.meta.typeSel.dict_item_id)){
										modal_meta.meta.item.type = modal_meta.meta.typeSel.dict_item_id;
									}

									if(angular.isDefined(modal_meta.meta.partSel.partid)){
										modal_meta.meta.item.part_id = modal_meta.meta.partSel.partid;
									}
								
									if(angular.isDefined( modal_meta.meta.usedunameSel.user_id)){
										modal_meta.meta.item.used_userid = modal_meta.meta.usedunameSel.user_id;
									}
								 
									
									if(angular.isDefined( modal_meta.meta.recycelSel.dict_item_id)){
										modal_meta.meta.item.recycle = modal_meta.meta.recycelSel.dict_item_id;
									}
									
									
									if(angular.isDefined(modal_meta.meta.pinpSel.dict_item_id)){
										modal_meta.meta.item.brand = modal_meta.meta.pinpSel.dict_item_id;
									}
									
									if(angular.isDefined( modal_meta.meta.wbSel.dict_item_id)){
										modal_meta.meta.item.wb =modal_meta.meta.wbSel.dict_item_id;
									}
									
									if(angular.isDefined( modal_meta.meta.locSel.dict_item_id)){
										modal_meta.meta.item.loc = modal_meta.meta.locSel.dict_item_id;
									}

								 

									if(angular.isDefined( modal_meta.meta.tbSel.dict_item_id)){
										modal_meta.meta.item.wb_auto =modal_meta.meta.tbSel.dict_item_id ;
									}
							
								
									modal_meta.meta.item.buy_time_f = modal_meta.meta.buytime
											.format('YYYY-MM-DD');
									modal_meta.meta.item.wbout_date_f = modal_meta.meta.wboutdate
											.format('YYYY-MM-DD');
								 
									console.log('sure set', modal_meta.meta)

									// 动态参数
									if (angular.isDefined(modal_meta.meta.attr)
											&& modal_meta.meta.attr.length > 0) {
										for (var j = 0; j < modal_meta.meta.attr.length; j++) {
											var code = modal_meta.meta.attr[j].attr_code;
											modal_meta.meta.attr[j].attr_value = modal_meta.meta.item[modal_meta.meta.attr[j].attr_code];
										}
									}
									modal_meta.meta.item.attrvals = angular
											.toJson(modal_meta.meta.attr);
									console.log("par:", modal_meta.meta.item);
									$http
											.post(
													$rootScope.project
															+ "/api/base/res/addResCustom.do",
													modal_meta.meta.item)
											.success(function(res) {
												if (res.success) {
													modalInstance.close("OK");
												} else {
													notify({
														message : res.message
													});
												}
											});

								},
								init : function(modal_meta) {
									var tt = {};
									angular.copy(gdicts, tt)
									loadOpt(modal_meta, tt);

								}
							}

							if (angular.isDefined(res.data.data)
									&& angular.isDefined(res.data.data.id)) {
								meta.item = res.data.data;
								// 填充其他数据
							}

							// 补充属性数据
							if (angular.isDefined(res.data.attr)
									&& res.data.attr.length > 0) {
								meta.attr = res.data.attr;
								for (var i = 0; i < res.data.attr.length; i++) {
									if (i == 0) {
										var e = {
											type : "dashed"
										}
										items.push(e);
									}

									var attr_type = res.data.attr[i].attr_type;
									if (attr_type == "string") {
										var e = {
											type : "input",
											disabled : "false",
											sub_type : "text",
											required : true,
											maxlength : "100",
											placeholder : "请输入内容",
											label : res.data.attr[i].attr_name,
											need : false,
											name : res.data.attr[i].attr_code,
											ng_model : res.data.attr[i].attr_code,
										}
										items.push(e);
									} else if (attr_type == "number") {
										var e = {
											type : "input",
											disabled : "false",
											sub_type : "number",
											required : true,
											maxlength : "20",
											placeholder : "请输入内容",
											label : res.data.attr[i].attr_name,
											need : false,
											name : res.data.attr[i].attr_code,
											ng_model : res.data.attr[i].attr_code,
										}
										items.push(e);
									}

								}

							}

							console.log(meta);
							// 打开静态框
							var modalInstance = $uibModal
									.open({
										backdrop : true,
										templateUrl : 'views/Template/modal_simpleForm.html',
										controller : modal_simpleFormCtl,
										size : 'lg',
										resolve : { // 调用控制器与modal控制器中传递值
											meta : function() {
												return meta;
											}
										}
									});
							modalInstance.result.then(function(result) {
								$log.log("result", result);

								if (result == "OK") {
									flush();
								}
							}, function(reason) {
								// 点击空白区域，总会输出backdrop click，点击取消，则会cancel
								$log.log("reason", reason)
							});
						})
	}
};

app.register.controller('genericzcdjCtl',genericzcdjCtl);