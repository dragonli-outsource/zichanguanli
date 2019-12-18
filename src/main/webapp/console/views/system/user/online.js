function sysOnlineSessionCtl( DTOptionsBuilder, DTColumnBuilder,
		$compile, $confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {

	$scope.meta ={
	tools : [  ]
} 
	$scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption('createdRow', function(row) {
		// Recompiling so we can bind Angular,directive to the
		$compile(angular.element(row).contents())($scope);
	});
	$scope.dtInstance = {}
	function renderAction(data, type, full) {
		var acthtml = " <div class=\"btn-group\"> ";

		acthtml = acthtml + " <button ng-click=\"row_dtl('" + full.user_id
				+ "')\" class=\"btn-white btn btn-xs\">详细</button> </div> ";
		return acthtml;
	}

	function renderUser(data, type, full) {
		if (angular.isDefined(data) && data.length > 0) {
			return full.userName;
		} else {
			return "游客";
		}
	}

	$scope.dtColumns = [

			DTColumnBuilder.newColumn('id').withTitle('ID').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('userId').withTitle('用户名').withOption(
					'sDefaultContent', '').renderWith(renderUser),
			DTColumnBuilder.newColumn('ip').withTitle('IP').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('startTime').withTitle('创建时间')
					.withOption('sDefaultContent', '').withClass('none'),
			DTColumnBuilder.newColumn('loginTime').withTitle('登录时间')
					.withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('lastaccess').withTitle('最后访问')
					.withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('agent').withTitle('Agent').withOption(
					'sDefaultContent', '').withClass('none') ]

	function flush() {
		var ps = {};
		$http.post($rootScope.project + "/api/sysSession/selectList.do", ps)
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

	$scope.row_dtl = function(id) {

	}

	$scope.query = function() {
		flush();
	}
	$scope.save = function(id) {

	}

};

app.register.controller('sysOnlineSessionCtl', sysOnlineSessionCtl);