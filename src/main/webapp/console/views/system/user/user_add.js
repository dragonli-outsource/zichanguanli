function sysUserAddCtl($confirm, $log, notify, $scope, $http, $rootScope) {

	$scope.item = {};
	$scope.item.userType="system";
	$scope.item.name = "";
	$scope.userStatusOpt = [{
				id : "Y",
				name : "锁定"

			}, {
				id : "N",
				name : "正常"

			}]
	$scope.userStatusSel = $scope.userStatusOpt[0];

	$scope.reset = function() {
		$scope.item = {};
		$scope.item.name = "";
		$scope.userStatusSel = $scope.userStatusOpt[0];
	}

	$scope.ok = function() {
		$scope.item.locked = $scope.userStatusSel.id;
		$log.info($scope.item);
		$http.post($rootScope.project + "/api/sysUserInfo/addUser.do", $scope.item).success(
				function(res) {
					if (res.success) {
						$scope.item = {};
						$scope.item.name = "";
						$scope.userStatusSel = $scope.userStatusOpt[0];
					}
					notify({
								message : res.message
							});
				})
	}

};

app.register.controller('sysUserAddCtl', sysUserAddCtl);