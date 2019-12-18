function myProcessfinishCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
		$confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $window) {

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
	privNormalCompute($scope.meta.tools, $rootScope.curMemuBtns);

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

	function renderJg(data, type, full) {
		var html = full.rackstr + "-" + full.frame;
		return html;
	}

	$scope.selectCheckBoxAll = function(selected) {

		if (selected) {
			$scope.dtInstance.DataTable.rows().select();
		} else {
			$scope.dtInstance.DataTable.rows().deselect();
		}
	}

	var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';

	$scope.dtColumns = [
			DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
					'select-checkbox checkbox_center').renderWith(function() {
				return ""
			}),
			DTColumnBuilder.newColumn('rootProcessInstanceId').withTitle('流程编号').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('subject').withTitle('标题').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('opinion').withTitle('处理意见').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('state').withTitle('任务状态').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('nodeName').withTitle('任务名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('description').withTitle('任务描述')
					.withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('createDate').withTitle('发起时间')
					.withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('endDate').withTitle('处理时间').withOption(
					'sDefaultContent', '') ]

	$scope.query = function() {
		flush();
	}

	function flush() {
		var ps = {}
		ps.search = "";
		ps.pageSize = 1000;
		ps.pageIndex = 1;
		$http
				.post(
						$rootScope.project
								+ "/api/cmdb/flow/zc/myProcessloadHistory.do",
						ps).success(function(res) {
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

		if (angular.isDefined(item) && angular.isDefined(item.businessId)) {

			$http
					.post(
							$rootScope.project
									+ "/api/flow/sysProcessDataExt/selectByBusinessId.do",
							{
								businessid : item.businessId
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

app.register.controller('myProcessfinishCtl', myProcessfinishCtl);