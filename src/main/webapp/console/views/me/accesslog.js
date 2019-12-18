function meAccessLogCtl( DTOptionsBuilder, DTColumnBuilder, $compile,
		$confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {

	$scope.meta = {
		tools : []
	}
	$scope.URL = $rootScope.project + "/api/sysLogAccess/selectPage.do";
	$scope.dtOptions = DTOptionsBuilder.fromSource($scope.URL).withDataProp(
			'data').withPaginationType('full_numbers').withOption('serverSide',
			true).withOption('createdRow', function(row) {
		// Recompiling so we can bind Angular,directive to the
		$compile(angular.element(row).contents())($scope);
	});

	$scope.dtInstance = {}
	$scope.reloadData = reloadData;
	function reloadData() {
		var resetPaging = false;
		$scope.dtInstance.reloadData(callback, resetPaging);
	}
	function callback(json) {
	}
	function renderAction(data, type, full) {
	}
	$scope.dtColumns = [
			DTColumnBuilder.newColumn('rtime').withTitle('日期').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('ip').withTitle('IP').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('url').withTitle('访问地址').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('info').withTitle('说明').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('postorget').withTitle('参数').withOption(
					'sDefaultContent', '').withClass('none') ]

	$scope.flush = function() {
		reloadData();
	}
};

app.register.controller('meAccessLogCtl', meAccessLogCtl);