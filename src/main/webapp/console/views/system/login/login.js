function sysLoginCtl($timeout, $rootScope, $scope, $log, $http, userService,
		$state, $localStorage, notify, $stateParams) {

	var to = $stateParams.to;

	$scope.user = {
		user : "",
		pwd : "",
		type : "empl"
	};

	$scope.typecheck=function(v){
		$scope.user.type=v;
	}
	$scope.login = function(e) {
//		if (e == "A") {
//			$scope.user = {
//				user : "admin",
//				pwd : "admin",
//				type : "username"
//			};
//		} else if (e == "D") {
//			$scope.user = {
//				user : "dev",
//				pwd : "12",
//				type : "username"
//			};
//		} else if (e == "N") {
//			$scope.user = {
//				user : "juck",
//				pwd : "0",
//				type : "username"
//			};
//		} else if (e == "S") {
//			$scope.user = {
//				user : "sys",
//				pwd : "0",
//				type : "username"
//			};
//		}

		var luserid = $localStorage.get("app_cur_userid");
		var lsystemid = $localStorage.get("dt_cur_systemId");
		userService.login($scope.user)
				.then(
						function(result) {
							if (result.success) {
								var curuserid = result.data.user_info.userId;
								$localStorage.put("app_cur_userid", curuserid);
								
								var cur_system=result.data.cur_system;
								 //与上次登陆用户相同,并且所在子系统也相同，则考虑切换到该菜单
								if (angular.isDefined(luserid)
										&& luserid == curuserid && cur_system==lsystemid ) {
									console.log("与上次登陆用户相同,before_userId:"
											+ luserid + ",cur_userId:"
											+ curuserid);
									console.log(cur_system,lsystemid);
									$timeout(function() {
										if (angular.isDefined(to) && to != null
												&& to != 'login') {
											$log.warn("end:" + to);
											$state.go(to, {});
										} else {
											$state.go("content");
										}
									}, 500)
								} else {
									console.log("与上次登陆用户不同,before_userId:"
											+ luserid + ",cur_userId:"
											+ curuserid);
									console.log(cur_system,lsystemid);
									$state.go("content");
								}
								
								
								
							} else {
								notify({
									message : result.message
								});
							}
						})

	}
};

app.register.controller('sysLoginCtl', sysLoginCtl);