function userRoleAdjustFormCtl($localStorage, notify, $log, $uibModal,
		$uibModalInstance, $scope, userIds, $http, $rootScope) {

	$log.warn("window in:", userIds);
	$scope.userRoles = []
	$http
			.post($rootScope.project + "/api/sysRoleInfo/roleQueryFormatKV.do",
					{})
			.success(
					function(res) {
						if (res.success) {
							$scope.userRoles = res.data;
							// 如果只有一个用户,则加载他的权限信息
							console.log("user cnt"
									+ angular.fromJson(userIds).length);
							if (angular.fromJson(userIds).length == 1) {
								$http
										.post(
												$rootScope.project
														+ "/api/sysUserInfo/queryRoles.do",
												{
													user_id : angular
															.fromJson(userIds)[0]
												}).success(function(res) {
											if (res.success) {
												$scope.roleSel = res.data;
											}
										})
							}

						}
					})

	$scope.roleSel = [];
	$scope.sure = function() {

		$log.warn(angular.toJson($scope.roleSel))
		if ($scope.roleSel.length == 0) {
			notify({
				message : "请至少选择一项角色"
			});
			return;
		}
		var ps = {};
		ps.userIds = userIds;
		ps.roles = angular.toJson($scope.roleSel);
		$http.post($rootScope.project + "/api/sysUserInfo/changeRoles.do", ps)
				.success(function(res) {
					if (res.success) {
						$uibModalInstance.close("OK");
					}
					notify({
						message : res.message
					});
				})

	}
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

}
function userPwdFormCtl($timeout, $localStorage, notify, $log, $uibModal,
		$uibModalInstance, $scope, id, $http, $rootScope) {
	
	$scope.item={pwd1:"",pwd2:""};
	$scope.item.user_id=id;
	$scope.sure = function() {
		 
		$http.post($rootScope.project + "/api/sysUserInfo/changeUserPwd.do",
				$scope.item).success(function(res) {
			if (res.success) {
				$uibModalInstance.close("OK");
			} else {

			}
			notify({
				message : res.message
			});
		});

	}
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
}
function userSaveFormCtl($timeout, $localStorage, notify, $log, $uibModal,
		$uibModalInstance, $scope, id, $http, $rootScope) {

	
	$scope.item = {}
	$scope.lockedOpt = [ {
		id : "Y",
		name : "锁定"
	}, {
		id : "N",
		name : "正常"
	} ]

	$scope.lockedSel = $scope.lockedOpt[0];

	if (!angular.isDefined(id)) {
		notify({
			message : "不存在ID"
		});
		$uibModalInstance.dismiss('cancel');
	} else {

		$http.post($rootScope.project + "/api/sysUserInfo/selectById.do", {
			id : id
		}).success(function(res) {
			$log.warn(res);
			if (res.success) {
				$scope.item = res.data;

				if (angular.isDefined(res.data.locked)) {
					if (res.data.locked == "Y") {
						$scope.lockedSel = $scope.lockedOpt[0];
					} else if (res.data.locked == "N") {
						$scope.lockedSel = $scope.lockedOpt[1];
					}
				}

			} else {
				notify({
					message : res.message
				});
			}
		})

	}

	$timeout(function() {

		var modal = document.getElementsByClassName('modal-body');
		for (var i = 0; i < modal.length; i++) {
			console.log(modal[i]);
			var adom = modal[i].getElementsByClassName('chosen-container');

			for (var j = 0; j < adom.length; j++) {
				adom[i].style.width = "100%";
			}
		}
	}, 200);

	$scope.sure = function() {
		$scope.item.locked = $scope.lockedSel.id;
		$http.post($rootScope.project + "/api/sysUserInfo/updateById.do",
				$scope.item).success(function(res) {
			if (res.success) {
				$uibModalInstance.close("OK");
			} else {

			}
			notify({
				message : res.message
			});
		});

	}
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}

function sysUserSettingCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
		$confirm, $log, notify, $scope, $http, $rootScope, $uibModal,
		$stateParams) {

	$scope.crud = {
		"update" : false,
		"insert" : false,
		"select" : false,
		"remove" : false,
		"priv" : false,
		"cpwd" : false
	};
	privCrudCompute($scope.crud, $rootScope.curMemuBtns);
	$scope.userGroupOpt = [];
	$scope.userGroupSel = "";
	$http.post($rootScope.project + "/api/sysUserGroup/selectList.do", {})
			.success(function(res) {
				if (res.success) {

					$scope.userGroupOpt = prepend(res.data, {
						groupId : "ALL",
						name : "全部"
					});
					$scope.userGroupSel = $scope.userGroupOpt[0];
				} else {
					notify({
						message : res.message
					});
				}
			});

	$scope.URL = $rootScope.project + "/api/sysUserInfo/selectPage.do";
	$scope.dtOptions = DTOptionsBuilder.fromSource($scope.URL).withDataProp(
			'data').withPaginationType('full_numbers').withDisplayLength(25)
			.withOption("ordering", false).withOption("responsive", true)
			.withOption("searching", false).withOption("paging", true)
			.withOption('bStateSave', true).withOption('bProcessing', true)
			.withOption('bFilter', false).withOption('bInfo', false)
			.withOption('serverSide', true).withOption('bAutoWidth', false)
			.withOption('aaData', $scope.tabdata).withOption(
					'headerCallback',
					function(header) {
						if ((!angular.isDefined($scope.headerCompiled))
								|| $scope.headerCompiled) {
							$scope.headerCompiled = true;
							$compile(angular.element(header).contents())
									($scope);
						}
					}).withOption('createdRow', function(row) {
				// Recompiling so we can bind Angular,directive to the
				$compile(angular.element(row).contents())($scope);
			}).withOption("select", {
				style : 'multi',
				selector : 'td:first-child'
			});
	$scope.dtInstance = {}
	$scope.reloadData = reloadData;

	function reloadData() {
		var resetPaging = true;
		$scope.dtInstance.reloadData(callback, resetPaging);
	}
	var tabdata = [];
	function callback(json) {
		tabdata = json.data;
		console.log("#####", json);

	}

	$scope.selectCheckBoxAll = function(selected) {
		if (selected) {
			$scope.dtInstance.DataTable.rows().select();
		} else {
			$scope.dtInstance.DataTable.rows().deselect();
		}
	}

	function renderAction(data, type, full) {
		var acthtml = " <div class=\"btn-group\"> ";

		acthtml = acthtml + " <button ng-click=\"row_dtl('" + full.userId
				+ "')\" class=\"btn-white btn btn-xs\">详细</button> </div> ";
		return acthtml;
	}
	function renderStatus(data, type, full) {
		var res = "正常";
		if (full.locked == "Y") {
			res = "锁定";
		}
		return res;
	}

	function renderType(data, type, full) {
		if (data == "system") {
			return "系统";
		} else if (data == "empl") {
			return "员工";
		} else {
			return data;
		}
	}
	var ckHtml = '<input ng-model="selectCheckBoxValue" ng-click="selectCheckBoxAll(selectCheckBoxValue)" type="checkbox">';

	$scope.dtColumns = [
			DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
					'select-checkbox checkbox_center').renderWith(function() {
				return ""
			}),
			DTColumnBuilder.newColumn('emplId').withTitle('员工编号').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('userName').withTitle('登录名').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('name').withTitle('姓名').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('tel').withTitle('手机号').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('userType').withTitle('用户类型').withOption(
					'sDefaultContent', '').renderWith(renderType),
			DTColumnBuilder.newColumn('userId').withTitle('状态').withOption(
					'sDefaultContent', '').renderWith(renderStatus) ]

	console.log($scope.dtColumns);

	function flush() {

		console.log('f');
		var url = "";
		if (angular.isDefined($scope.ct)) {
			url = $rootScope.project
					+ "/api/sysUserInfo/selectPage.do?none=none&ct="
					+ $scope.ct;
		} else {
			url = $rootScope.project
					+ "/api/sysUserInfo/selectPage.do?none=none";
		}

		if ($scope.userGroupSel.groupId != "ALL") {
			url = url + "&groupId=" + $scope.userGroupSel.groupId;
		}
		$scope.URL = url;
		$scope.dtOptions.ajax = $scope.URL;
		reloadData();

	}

	$scope.row_dtl = function(id) {
		notify({
			message : "待开发"
		});
	}

	$scope.del = function() {
		var data = $scope.dtInstance.DataTable.rows({
			selected : true
		})[0];

		if (data.length == 0) {
			notify({
				message : "请至少选择一个用户"
			});
			return;
		}

		var d = $scope.dtInstance.DataTable.context[0].json.data;
		// 批量删除
		var userids = [];
		for (var i = 0; i < data.length; i++) {
			// alert($scope.dtOptions.aaData[data[i]].USER_NO)
			userids.push(d[data[i]].userId);
		}
		console.log(angular.toJson(userids))
		$confirm({
			text : '是否删除选中的用户?'
		}).then(
				function() {
					$http.post(
							$rootScope.project
									+ "/api/sysUserInfo/deleteByIds.do", {
								ids : angular.toJson(userids)
							}).success(function(res) {

						if (res.success) {
							flush();
						}
						notify({
							message : res.message
						});
					});
				});

	}
	$scope.cpwd = function() {
		var data = $scope.dtInstance.DataTable.rows({
			selected : true
		})[0];
		console.log(data);

		if (data.length == 0) {
			notify({
				message : "请至少选择一个用户"
			});
			return;
		}
		if (data.length > 1) {
			notify({
				message : "只能选择一个用户"
			});
			return;
		}
		var d = $scope.dtInstance.DataTable.context[0].json.data;
		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/system/user/modal_user_pwd.html',
			controller : userPwdFormCtl,
			size : 'lg',
			resolve : { // 调用控制器与modal控制器中传递值
				id : function() {
					return d[data[0]].userId;
				}
			}
		});

		modalInstance.result.then(function(result) {
		}, function(reason) {
			// 点击空白区域，总会输出backdrop click，点击取消，则会cancel
			$log.log("reason", reason)
		});
		
	}

	$scope.update = function() {

		var data = $scope.dtInstance.DataTable.rows({
			selected : true
		})[0];
		console.log(data);

		if (data.length == 0) {
			notify({
				message : "请至少选择一个用户"
			});
			return;
		}
		if (data.length > 1) {
			notify({
				message : "只能选择一个用户"
			});
			return;
		}

		var d = $scope.dtInstance.DataTable.context[0].json.data;
		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/system/user/modal_user_save.html',
			controller : userSaveFormCtl,
			size : 'lg',
			resolve : { // 调用控制器与modal控制器中传递值
				id : function() {
					return d[data[0]].userId;
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

	$scope.updateRole = function() {

		var data = $scope.dtInstance.DataTable.rows({
			selected : true
		})[0];

		if (data.length == 0) {
			notify({
				message : "请至少选择一个用户"
			});
			return;
		}
		var d = $scope.dtInstance.DataTable.context[0].json.data;
		var userids = [];
		for (var i = 0; i < data.length; i++) {
			// alert($scope.dtOptions.aaData[data[i]].user_no)
			userids.push(d[data[i]].userId);
		}

		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/system/user/modal_user_role_save.html',
			controller : userRoleAdjustFormCtl,
			size : 'lg',
			resolve : { // 调用控制器与modal控制器中传递值
				userIds : function() {
					return angular.toJson(userids);
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

	$scope.detail = function() {
		notify({
			message : "待开发"
		});
	}

	$scope.query = function() {
		flush();
	}
	$scope.save = function(id) {
		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/system/role/modal_role_save.html',
			controller : roleSaveCtl,
			size : 'lg',
			resolve : { // 调用控制器与modal控制器中传递值
				id : function() {
					return id;
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

app.register.controller('sysUserSettingCtl', sysUserSettingCtl);