function modalreviewProcessCtl(meta, $rootScope, $window, $scope,$uibModalInstance ) {

	var url = $rootScope.project + "uflo/diagram?processKey=" + meta;
	console.log(url);
	$scope.url = url;

	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}

function modalFlowListSelCtl($timeout, $localStorage, notify, $log, $uibModal,
		$uibModalInstance, $scope, $http, $rootScope, DTOptionsBuilder,
		DTColumnBuilder, $compile) {

	// 分类
	$scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
			.withPaginationType('full_numbers').withDisplayLength(100)
			.withOption("ordering", false).withOption("responsive", false)
			.withOption("searching", true).withOption('scrollY', '200px')
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
			})

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
			DTColumnBuilder.newColumn(null).withTitle("").withClass(
					'select-checkbox checkbox_center').renderWith(function() {
				return ""
			}),
			DTColumnBuilder.newColumn('id').withTitle('编号').withOption(
					'sDefaultContent', '').withOption("width", '30'),
			DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
					'sDefaultContent', '').withOption("width", '30'),
			DTColumnBuilder.newColumn('key').withTitle('关键字').withOption(
					'sDefaultContent', '').withOption("width", '30'),
			DTColumnBuilder.newColumn('version').withTitle('版本').withOption(
					'sDefaultContent', '').withOption("width", '30'),
			DTColumnBuilder.newColumn('createDate').withTitle('创建时间')
					.withOption('sDefaultContent', '')
					.withOption('width', '30') ]

	function flush() {
		var ps = {}

		ps.pageIndex = 1;
		ps.pageSize = 1000;

		$http.post($rootScope.project + "uflo/central/loadProcess", ps)
				.success(function(res) {
					$scope.dtOptions.aaData = res.result;

				})
	}
	$scope.query = function() {
		flush()
	}
	flush();

	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

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

	$scope.sure = function() {
		var id = getSelectRow();
		if (angular.isDefined(id)) {
			$uibModalInstance.close(id);
		}

	}
}

function modalflowmatchsaveCtl($timeout, $localStorage, notify, $log,
		$uibModal, $uibModalInstance, $scope, meta, $http, $rootScope, $compile) {

	$scope.data = {};

	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

	if (angular.isDefined(meta.id)) {

		$http.post(
				$rootScope.project
						+ "/api/flow/sysProcessClassItem/selectById.do", {
					id : meta.id
				}).success(function(res) {
			if (res.success) {
				$scope.data = res.data
			} else {
				notify({
					message : res.message
				});
			}
		});

	} else {
		$scope.data.cid = meta.cid;
	}
	$scope.selectprocess = function() {

		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/flow/modal_flowlist.html',
			controller : modalFlowListSelCtl,
			size : 'lg',
			resolve : { // 调用控制器与modal控制器中传递值

			}
		});

		modalInstance.result.then(function(result) {
			$log.log("result", result);
			$scope.data.processname = result.name;
			$scope.data.processkey = result.key;
		}, function(reason) {
			// 点击空白区域，总会输出backdrop click，点击取消，则会cancel
			$log.log("reason", reason)
		});

	}

	$scope.sure = function() {
		if (!angular.isDefined($scope.data.processkey)) {
			alert("请选择流程");
			return;
		}
		$http.post(
				$rootScope.project
						+ "/api/flow/sysProcessClassItem/insertOrUpdate.do",
				$scope.data).success(function(res) {
			if (res.success) {
				$uibModalInstance.close('OK');
			} else {
				notify({
					message : res.message
				});
			}
		});
	}

}
function sysFlowMatchCtl($window, $stateParams, DTOptionsBuilder,
		DTColumnBuilder, $compile, $confirm, $log, notify, $scope, $http,
		$rootScope, $uibModal) {
	$scope.meta = {
		tools : [
				{
					id : "select",
					label : "分组",
					type : "select",
					disablesearch : true,
					show : true,
					dataOpt : [],
					dataSel : ""
				},
				{
					id : "0",
					priv : "select",
					label : "查询",
					type : "btn_query",
					hide : false,
				},
				{
					id : "1",
					label : "新增",
					priv : "insert",
					show : false,
					type : "btn",
					template : ' <button ng-click="save()" class="btn btn-sm btn-primary" type="submit">新增</button>'

				} ]
	}

	privNormalCompute($scope.meta.tools, $rootScope.curMemuBtns);
	var crud = {
		"update" : false,
		"insert" : false,
		"select" : false,
		"remove" : false,
	};
	privCrudCompute(crud, $rootScope.curMemuBtns);

	$http.post($rootScope.project + "/api/flow/sysProcessClass/selectList.do",
			{}).success(function(res) {
		if (res.success) {
			$scope.meta.tools[0].dataOpt = res.data;
			if (res.data.length > 0) {
				$scope.meta.tools[0].dataSel = res.data[0];
				flush()
			}
		} else {
			notify({
				message : res.message
			});
		}
	})

	$scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
			'responsive', false).withOption('createdRow', function(row) {
		// Recompiling so we can bind Angular,directive to the
		$compile(angular.element(row).contents())($scope);
	});
	$scope.dtInstance = {}
	function renderAction(data, type, full) {
		var acthtml = " <div class=\"btn-group\"> ";
		if (crud.update) {
			acthtml = acthtml + " <button ng-click=\"save('" + full.id
					+ "')\" class=\"btn-white btn btn-xs\">更新</button> ";
		}

		if (crud.remove) {
			acthtml = acthtml + " <button ng-click=\"row_del('" + full.id
					+ "')\" class=\"btn-white btn btn-xs\">删除</button>";
		}
		acthtml = acthtml + " <button ng-click=\"review('" + full.processkey
				+ "')\" class=\"btn-white btn btn-xs\">流程预览</button> ";
		acthtml = acthtml + "</div>";
		return acthtml;
	}
	function renderStatus(data, type, full) {
		var res = "无效";
		if (full.isAction == "Y") {
			res = "有效";
		}
		return res;
	}

	$scope.dtColumns = [
			DTColumnBuilder.newColumn('name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('processkey').withTitle('流程编号')
					.withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('id').withTitle('操作').withOption(
					'sDefaultContent', '').renderWith(renderAction) ]

	function flush() {
		var ps = {}
		ps.cid = $scope.meta.tools[0].dataSel.id;
		$http.post(
				$rootScope.project
						+ "/api/flow/sysProcessClassItemExt/selectList.do", ps)
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
	flush();
	$scope.btn_query = function() {
		flush();
	}

	$scope.review = function(id) {

		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/flow/modal_reviewProcess.html',
			controller : modalreviewProcessCtl,
			size : 'blg',
			resolve : { // 调用控制器与modal控制器中传递值
				meta : function() {
					return id;
				}
			}
		});

		modalInstance.result.then(function(result) {
			$log.log("result", result);

		}, function(reason) {
			// 点击空白区域，总会输出backdrop click，点击取消，则会cancel
			$log.log("reason", reason)
		});

	}
	$scope.row_del = function(id) {
		$confirm({
			text : '是否删除功能?'
		})
				.then(
						function() {
							$http
									.post(
											$rootScope.project
													+ "/api/flow/sysProcessClassItem/deleteById.do",
											{
												id : id
											}).success(function(res) {
										if (res.success) {
											flush();
										}
										notify({
											message : res.message
										});
									})
						});
	}

	$scope.save = function(id) {

		var p = {};
		p.id = id;
		p.cid = $scope.meta.tools[0].dataSel.id;
		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/flow/modal_flowmatchsave.html',
			controller : modalflowmatchsaveCtl,
			size : 'lg',
			resolve : { // 调用控制器与modal控制器中传递值
				meta : function() {
					return p;
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
};

app.register.controller('sysFlowMatchCtl', sysFlowMatchCtl);