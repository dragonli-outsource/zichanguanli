function myProcessCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
		$log, notify, $scope, $http, $rootScope, $uibModal, $window) {

	$scope.meta = {
		tablehide : false,
		tools : [
				{
					id : "1",
					label : "查询",
					type : "btn",
					show : true,
					priv : 'select',
					template : ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
				},
				{
					id : "1",
					priv : "act1",
					label : "详情",
					type : "btn",
					template : ' <button ng-click="oper()" class="btn btn-sm btn-primary" type="submit">详情</button>',
					show : true,
				} ]
	}

	$scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
			.withPaginationType('full_numbers').withDisplayLength(50)
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
				// Recompiling so we can bind Angular,directive to the
				$compile(angular.element(row).contents())($scope);
			}).withOption(
					'headerCallback',
					function(header) {
						if ((!angular.isDefined($scope.headerCompiled))
								|| $scope.headerCompiled) {
							// Use this headerCompiled field to only compile
							// header once
							$scope.headerCompiled = true;
							$compile(angular.element(header).contents())
									($scope);
						}
					}).withOption("select", {
				style : 'multi',
				selector : 'td:first-child'
			});

	$scope.dtInstance = {}

	function renderName(data, type, full) {

		var html = full.model;
		return html;

	}

	$scope.selectCheckBoxAll = function(selected) {

		if (selected) {
			$scope.dtInstance.DataTable.rows().select();
		} else {
			$scope.dtInstance.DataTable.rows().deselect();
		}
	}

	function renderStatusDtl(data, type, full) {
		var html = data;
		if (angular.isDefined(data)) {
			if (data == "submitforapproval") {
				html = "待送审";
			} else if (data == "inreview") {
				html = "审批中"
			} else if (data == "success") {
				html = "审批成功"
			} else if (data == "failed") {
				html = "审批失败"
			} else if (data == "cancel") {
				html = "审批取消"
			}
		}
		return html;
	}

	function renderType(data, type, full) {
		var html = data;
		if (angular.isDefined(data)) {
			if (data == "LY") {
				html = "资产领用流程";
			} else if (data == "JY") {
				html = "资产借用流程"
			} else if (data == "BX") {
				html = "资产报销流程"
			} else if (data == "ZY") {
				html = "资产转移流程"
			} else if (data == "WX") {
				html = "资产维修流程"
			}
		}
		return html;
	}

	var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';

	$scope.dtColumns = [
			DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
					'select-checkbox checkbox_center').renderWith(function() {
				return ""
			}),
			DTColumnBuilder.newColumn('processInstanceId').withTitle('流程编号')
					.withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('duuid').withTitle('单据号').withOption(
					'sDefaultContent', ''),
			// DTColumnBuilder.newColumn('pstatus').withTitle('状态').withOption(
			// 'sDefaultContent', '').renderWith(renderStatus),
			DTColumnBuilder.newColumn('pstatusdtl').withTitle('状态').withOption(
					'sDefaultContent', '').renderWith(renderStatusDtl),
			DTColumnBuilder.newColumn('ptitle').withTitle('标题').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('ptype').withTitle('类型').withOption(
					'sDefaultContent', '').renderWith(renderType),
			DTColumnBuilder.newColumn('createTime').withTitle('发起时间')
					.withOption('sDefaultContent', '') ]

	$scope.query = function() {
		flush();
	}

	function flush() {
		var ps = {}
		$http.post(
				$rootScope.project
						+ "/api/flow/sysProcessDataExt/selectListByMy.do", ps)
				.success(function(res) {
					$scope.dtOptions.aaData = res.data
				})
	}
	flush();

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

	function getSelectRows() {
		var data = $scope.dtInstance.DataTable.rows({
			selected : true
		})[0];
		if (data.length == 0) {
			notify({
				message : "请至少选择一项"
			});
			return;
		} else if (data.length > 100) {
			notify({
				message : "不允许超过500个"
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

	$scope.oper = function() {
		var item = getSelectRow();
		console.log(item);
		if (angular.isDefined(item) ) {

			$http
					.post(
							$rootScope.project
									+ "/api/flow/sysProcessDataExt/selectByBusinessId.do",
							{
								businessid : item.busid
							})
					.success(
							function(res) {
								if (res.success) {
									// 资产领用
									var modalInstance = $uibModal
											.open({
												backdrop : true,
												templateUrl : 'views/cmdb/modal_zcActionDtl.html',
												controller : modalzcActionDtlCtl,
												size : 'blg',
												resolve : { // 调用控制器与modal控制器中传递值
													meta : function() {
														return res.data;
													},
													task : function() {
														return item;
													},
													pagetype : function() {
														return "query";
													}
												}
											});

									modalInstance.result.then(function(result) {

									}, function(reason) {
										// 点击空白区域，总会输出backdrop
										// click，点击取消，则会cancel
										$log.log("reason", reason)
									});

								} else {
									notify({
										message : res.message
									});
								}
							})
		} else {
			notify({
				message : "该流程不存在"
			});
		}
	}

};

app.register.controller('myProcessCtl', myProcessCtl);