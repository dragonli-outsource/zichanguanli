function cmdbdevsearchCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
		$confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $window) {

	// 分类
	$scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
			.withPaginationType('full_numbers').withDisplayLength(100).withDOM('frtlip')
			.withOption("ordering", false).withOption("responsive", false)
			.withOption("searching", true).withOption('scrollY', '600px')
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
	                text: 'Excel',
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
	                text: '打印',
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

	$scope.dtInstance = {}

	function renderName(data, type, full) {

		var html = full.model;
		// if (angular.isDefined(full.model)) {
		// html = html + "(" + full.model + ")";
		// }
		return html;

	}

	function renderJg(data, type, full) {

		var html = full.rackstr + "-" + full.frame;
		return html;

	}
	$scope.selectCheckBoxAll = function(selected) {

		if (selected) {
			$scope.dtInstance.DataTable.rows().select();
			console.log($scope.dtInstance.DataTable)
		} else {
			$scope.dtInstance.DataTable.rows().deselect();
			console.log($scope.dtInstance.DataTable)
		}
	}

	var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';

	$scope.dtColumns = [
			DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
					'select-checkbox checkbox_center').renderWith(function() {
				return ""
			}),
			DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
					'sDefaultContent', '').withOption("width", '30'),
			DTColumnBuilder.newColumn('classname').withTitle('资产类型').withOption(
					'sDefaultContent', '').withOption("width", '30'),
			DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('name').withTitle('型号').withOption(
					'sDefaultContent', '').withOption('width', '50')
					.renderWith(renderName),
			DTColumnBuilder.newColumn('reviewstr').withTitle('复核状态')
					.withOption('sDefaultContent', '')
					.withOption('width', '50'),
			DTColumnBuilder.newColumn('locstr').withTitle('位置').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('rackstr').withTitle('机柜').withOption(
					'sDefaultContent', '').withOption('width', '5'),
			DTColumnBuilder.newColumn('part_name').withTitle('部门').withOption(
					'sDefaultContent', '').withOption('width', '10'),
			DTColumnBuilder.newColumn('used_username').withTitle('使用人')
					.withOption('sDefaultContent', '')
					.withOption('width', '20'),
			DTColumnBuilder.newColumn('recyclestr').withTitle('状态').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('wbstr').withTitle('维保').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('envstr').withTitle('运行环境').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('riskstr').withTitle('风险等级').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('confdesc').withTitle('配置描述').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('sn').withTitle('序列号').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('buy_timestr').withTitle('采购时间')
					.withOption('sDefaultContent', ''),
			// DTColumnBuilder.newColumn('create_username').withTitle('录入人')
			// .withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('update_username').withTitle('更新人')
					.withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('review_username').withTitle('复核人')
					.withOption('sDefaultContent', '') ]

	var meta = {
		tablehide : false,
		toolsbtn : [
				{
					id : "btn",
					label : "",
					type : "btn",
					show : true,
					template : ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">搜索</button>'
				},
				{
					id : "btn2",
					label : "",
					type : "btn",
					show : true,
					priv : "export",
					template : ' <button ng-click="exportfile()" class="btn btn-sm btn-primary" type="submit">导出</button>'
				} ],
		tools : [ {
			id : "select",
			label : "位置",
			type : "select",
			disablesearch : true,
			show : true,
			dataOpt : [],
			dataSel : ""
		}, {
			id : "select",
			label : "环境",
			type : "select",
			disablesearch : true,
			show : true,
			dataOpt : [],
			dataSel : ""
		}, {
			id : "select",
			label : "维保",
			type : "select",
			disablesearch : true,
			show : true,
			dataOpt : [],
			dataSel : ""
		}, {
			id : "select",
			label : "状态",
			type : "select",
			disablesearch : true,
			show : true,
			dataOpt : [],
			dataSel : ""
		}, {
			id : "input",
			show : true,
			label : "内容",
			placeholder : "输入型号、编号、序列号",
			type : "input",
			ct : ""
		} ]
	};
	$scope.meta = meta;

	function flush() {

		var ps = {}
		ps.loc = $scope.meta.tools[0].dataSel.dict_item_id;
		ps.env = $scope.meta.tools[1].dataSel.dict_item_id;
		ps.wb = $scope.meta.tools[2].dataSel.dict_item_id;
		ps.recycle = $scope.meta.tools[3].dataSel.dict_item_id;
		ps.search = $scope.meta.tools[4].ct;

		$http.post($rootScope.project + "/api/base/res/queryResAll.do", ps)
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
	$scope.query = function() {
		flush();
	}

	$scope.exportfile = function() {
		var ps = {}
		ps.loc = $scope.meta.tools[0].dataSel.dict_item_id;
		ps.env = $scope.meta.tools[1].dataSel.dict_item_id;
		ps.wb = $scope.meta.tools[2].dataSel.dict_item_id;
		ps.recycle = $scope.meta.tools[3].dataSel.dict_item_id;
		ps.search = $scope.meta.tools[4].ct;
		$window.open($rootScope.project + "/api/base/res/exportAllRes.do?loc="
				+ ps.loc + "&env=" + ps.env + "&wb=" + ps.wb + "&recycle="
				+ ps.recycle + "&search=" + ps.search);

	}
	var gdicts = {};
	$http
			.post(
					$rootScope.project + "/api/base/res/queryDictFast.do",
					{
						dicts : "devbrand,devrisk,devenv,devrecycle,devwb,devdc,devservertype,devrack",
						parts : "Y",
						partusers : "Y"
					}).success(function(res) {
				if (res.success) {
					gdicts = res.data;
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

					tenv.unshift({
						dict_item_id : "all",
						name : "全部"
					});
					$scope.meta.tools[1].dataOpt = tenv;
					$scope.meta.tools[1].dataSel = tenv[0];

					twb.unshift({
						dict_item_id : "all",
						name : "全部"
					});
					$scope.meta.tools[2].dataOpt = twb;
					$scope.meta.tools[2].dataSel = twb[0];

					trecycle.unshift({
						dict_item_id : "all",
						name : "全部"
					});
					$scope.meta.tools[3].dataOpt = trecycle;
					$scope.meta.tools[3].dataSel = trecycle[0];

				} else {
					notify({
						message : res.message
					});
				}
			})

	$scope.detail = function(id) {

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
};

app.register.controller('cmdbdevsearchCtl', cmdbdevsearchCtl);