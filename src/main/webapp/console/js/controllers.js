var app = {}
function MainCtrl($log, $http, $scope, $rootScope, $state, $localStorage,
		userService, notify, $timeout) {
	// 修改主题
	var cur_theme = $localStorage.get("cur_theme");
	if (angular.isDefined(cur_theme)) {
		$scope.cur_skin = cur_theme;
	} else {
		$scope.cur_skin = "default";
	}
	$scope.change_theme = function(theme) {
		console.log("change theme");
		$scope.cur_skin = theme;
		$localStorage.put("cur_theme", theme);
	}
	$scope.fullScreen = function() {
		var element = document.documentElement; // 若要全屏页面中div，var element=
		// document.getElementById("divID");
		// IE 10及以下ActiveXObject
		if (window.ActiveXObject) {
			var WsShell = new ActiveXObject('WScript.Shell')
			WsShell.SendKeys('{F11}');
		}
		// HTML W3C 提议
		else if (element.requestFullScreen) {
			element.requestFullScreen();
		}
		// IE11
		else if (element.msRequestFullscreen) {
			element.msRequestFullscreen();
		}
		// Webkit (works in Safari5.1 and Chrome 15)
		else if (element.webkitRequestFullScreen) {
			element.webkitRequestFullScreen();
		}
		// Firefox (works in nightly)
		else if (element.mozRequestFullScreen) {
			element.mozRequestFullScreen();
		}
	}
	// 退出登录
	$scope.logout = function() {
		userService.logout().then(function(result) {
			$log.warn("userService logout result", result)
			if (result.success) {
				$state.go("login");
			} else {
			}
		}, function(error) {
		}, function(progress) {
		})
	}

	$scope.changepwd = function() {
		$state.go("me.pwdreset");
	}

	// 切换系统
	$scope.switchSystem = function(id) {
		userService.switchSystem(id).then(function(result) {
			if (result.success) {
				$timeout(function() {
					$state.go("content");
				}, 800);
			} else {
				notify({
					message : result.message
				});
			}
		}, function(error) {
		}, function(progress) {
		})

	}
	// 处理菜单的logo函数
	$scope.menuLogoIsExist = function(logo) {
		if (angular.isDefined(logo) && logo != "") {
			return true;
		} else {
			return false;
		}
	}
	// 监听左边菜单数据
	$scope.$watch(function() {
		return $rootScope.dt_sys_menus;
	}, function() {
		$log.warn("watch $rootScope.dt_sys_menus change,load from this,again.",
				$rootScope.dt_sys_menus);
		if (angular.isDefined($rootScope.dt_sys_menus)
				&& $rootScope.dt_sys_menus != null) {
			$scope.menu = $rootScope.dt_sys_menus;
			$state.go($state.current, null, {
				reload : true
			});
		}
	}, true);

	// 页面刷新
	var dt_sys_menu = $localStorage.get("dt_sys_menus");
	if (angular.isDefined(dt_sys_menu)) {
		$log.warn("dt_sys_menu load from localstorage", dt_sys_menu);
		$scope.menu = dt_sys_menu;
		// fixnav();
	}

	// 监听用户数据
	$scope
			.$watch(
					function() {
						return $rootScope.dt_sys_user_info;
					},
					function() {
						$log
								.warn(
										"watch $rootScope.dt_sys_user_info change,load from this,again.",
										$rootScope.dt_sys_user_info);
						if (angular.isDefined($rootScope.dt_sys_user_info)
								&& $rootScope.dt_sys_user_info != null) {
							$scope.sys_user_info = $rootScope.dt_sys_user_info
						}
					}, true);

	// 页面刷新
	var sys_user_info = $localStorage.get("dt_sys_user_info")
	if (angular.isDefined(sys_user_info)) {
		$log.warn("user_info load from localstorage", sys_user_info);
		$scope.sys_user_info = sys_user_info;
	}

	// 列举系统
	$scope.$watch(function() {
		return $rootScope.dt_systems;
	}, function() {
		$log.warn("watch $rootScope.dt_systems change,load from this,again.",
				$rootScope.dt_systems);
		if (angular.isDefined($rootScope.dt_systems)
				&& $rootScope.dt_systems != null) {
			$scope.dt_systems = $rootScope.dt_systems
		}
	}, true);
	var dt_systems = $localStorage.get("dt_systems")
	if (angular.isDefined(dt_systems)) {
		$log.warn("dt_systems load from localstorage", dt_systems);
		$scope.dt_systems = dt_systems;
	}
};
angular.module('inspinia').controller('MainCtrl', MainCtrl);