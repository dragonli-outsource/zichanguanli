function modalpartzcCtl($timeout, $localStorage, notify, $log, $uibModal,
		$uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
		DTColumnBuilder, $compile) {
	console.log(meta);

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
			});

	$scope.dtInstance = {}

	function renderJg(data, type, full) {
		var html = full.rackstr + "-" + full.frame;
		return html;
	}

	function renderName(data, type, full) {

		var html = full.model;
		return html;

	}

	function renderReview(data, type, full) {
		if (data == "reviewed") {
			return "已复核"
		} else {
			return "未复核"
		}
	}
	$scope.dtColumns = [
			DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
					'sDefaultContent', '').withOption("width", '30'),
			DTColumnBuilder.newColumn('classname').withTitle('资产类型').withOption(
					'sDefaultContent', '').withOption('width', '50'),
			DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('name').withTitle('型号').withOption(
					'sDefaultContent', '').withOption('width', '50')
					.renderWith(renderName),
			DTColumnBuilder.newColumn('locstr').withTitle('位置').withOption(
					'sDefaultContent', '').withOption('width', '30'),
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
			DTColumnBuilder.newColumn('changestate').withTitle('复核状态')
					.withOption('sDefaultContent', '').renderWith(renderReview), ];

	var ps = {}
	ps.part_id = meta.part_id;
	$http
			.post($rootScope.project + "/api/base/res/rep/queryZcTjByOrgId.do",
					ps).success(function(res) {
				if (res.success) {
					$scope.dtOptions.aaData = res.data;
				} else {
					notify({
						message : res.message
					});
				}
			})

	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

}

function cmdbrepPartZcCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
		$confirm, $log, notify, $scope, $http, $rootScope, $uibModal, $window,$stateParams) {
	$scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
			.withPaginationType('full_numbers').withDisplayLength(50)
			.withOption("ordering", false).withOption("responsive", false)
			.withOption("searching", false).withOption('bAutoWidth', false)
			.withOption('paging', true).withOption('bStateSave', true)
			.withOption('bProcessing', false).withOption('bFilter', false)
			.withOption('bInfo', false).withOption('serverSide', false)
			.withOption('aaData', $scope.tabdata).withOption('createdRow',
					function(row) {
						// Recompiling so we can bind Angular,directive to the
						$compile(angular.element(row).contents())($scope);
					});

	$scope.dtInstance = {}

	function renderAction(data, type, full) {
		var acthtml = " <div class=\"btn-group\"> ";
		acthtml = acthtml + " <button ng-click=\"detail('" + full.part_id
				+ "')\" class=\"btn-white btn btn-xs\">详情</button> </div> ";
		return acthtml;
	}

	$scope.dtColumns = [
			DTColumnBuilder.newColumn('part_fullname').withTitle('使用部门')
					.withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('zc_cnt').withTitle('数量').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('part_id').withTitle('操作').withOption(
					'sDefaultContent', '').renderWith(renderAction) ]

	$scope.query = function() {
		flush();
	}

	var meta = {
		tablehide : false,
		toolsbtn : [],
		tools : [
				 
				{
					id : "btn",
					show : true,
					label : "",
					type : "btn",
					template : ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">查询</button>'
				} ]
	}
	$scope.meta = meta;
 
	function flush() {
		var ps = {}
		$http.post($rootScope.project + "/api/base/res/rep/queryZcTjByOrg.do",
				ps).success(function(res) {
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
	$scope.btn_query=function(){
		flush();
	}
	$scope.detail = function(id) {
		var ps = {};
		ps.part_id = id;
		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/cmdb/rep/modal_partzc.html',
			controller : modalpartzcCtl,
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

app.register.controller('cmdbrepPartZcCtl', cmdbrepPartZcCtl);