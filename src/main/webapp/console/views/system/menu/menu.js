//angular.isArray()	如果引用的是数组返回 true
//angular.isDate()	如果引用的是日期返回 true
//angular.isDefined()	如果引用的已定义返回 true
//angular.isElement()	如果引用的是 DOM 元素返回 true
//angular.isFunction()	如果引用的是函数返回 true
//angular.isNumber()	如果引用的是数字返回 true
//angular.isObject()	如果引用的是对象返回 true
//angular.isString()	如果引用的是字符串返回 true
//angular.isUndefined()	如果引用的未定义返回 true
//angular.equals()
function getTree(data, primaryIdName, parentIdName) {
	if (!data || data.length == 0 || !primaryIdName || !parentIdName)
		return [];
	var tree = [], rootIds = [], item = data[0], primaryKey = item[primaryIdName], treeObjs = {}, parentId, parent, len = data.length, i = 0;
	while (i < len) {
		item = data[i++];
		primaryKey = item[primaryIdName];
		treeObjs[primaryKey] = item;
		parentId = item[parentIdName];
		// console.log(parentId);
		if (parentId) {
			parent = treeObjs[parentId];

			if (parent.children) {
				parent.children.push(item);
			} else {
				parent.children = [ item ];
			}
		} else {
			rootIds.push(primaryKey);
		}
	}
	for (var i = 0; i < rootIds.length; i++) {
		tree.push(treeObjs[rootIds[i]]);
	}
	return tree;
}

function menuModuleCtl($localStorage, notify, $log, $uibModal,
		$uibModalInstance, $scope, data, $http, $rootScope) {
	$scope.item = {}
	$log.log("window in:", data);
	$scope.modulesOpt = []
	$scope.modulesSel = ""
	$scope.item = data;
	$scope.nameaction = true;

	$scope.sure = function() {

	};
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

}

function menuAclCtl($timeout, DTOptionsBuilder, DTColumnBuilder, notify, $log,
		$uibModal, $uibModalInstance, $scope, data, $http, $rootScope, $compile) {
	$scope.item = {}
	$log.log("window in:", data);
	var mid = data.node_id;
	$scope.dtOptions = DTOptionsBuilder
			.fromFnPromise()
			.withPaginationType('full_numbers')
			.withDisplayLength(10)
			.withOption("ordering", false)
			.withOption("responsive", true)
			.withOption("searching", true)
			.withOption("paging", true)
			.withOption('bStateSave', true)
			.withOption('bProcessing', true)
			.withOption('bFilter', false)
			.withOption('bInfo', false)
			.withOption('serverSide', false)
			.withOption('bAutoWidth', false)
			.withOption('aaData', $scope.tabdata)
			.withOption('createdRow', function(row) {
				// Recompiling so we can bind Angular,directive to the
				$compile(angular.element(row).contents())($scope);
			})
			.withOption(
					'initComplete',
					function(settings, conf) {
						if (angular.isDefined($scope.dtInstance.DataTable)) {
							$timeout(
									function() {
										for (var i = 0; i < $scope.dtOptions.aaData.length; i++) {
											if ($scope.dtOptions.aaData[i].selected == "Y") {
												$scope.dtInstance.DataTable
														.row(':eq(' + i + ')')
														.select();
											} else {
												continue;
											}
										}
									}, 80)
						}
					}).withOption("select", {
				style : 'multi',
				selector : 'td:first-child'
			});

	$scope.dtInstance = {}

	$scope.dtColumns = [
			DTColumnBuilder.newColumn(null).withTitle('').withClass(
					'select-checkbox').renderWith(function() {
				return '';
			}),
			DTColumnBuilder.newColumn('ctacltext').withTitle('ACL类型')
					.withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('url').withTitle('URL').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
					'sDefaultContent', '') ]
	// $scope.dtInstance.DataTable.rows('.even').select();
	function flush() {
		var ps = {};
		ps.module_id = mid
		$http
				.post($rootScope.project + "/api/module/queryModuleItemMap.do",
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
	$scope.sure = function() {
		var data = $scope.dtInstance.DataTable.rows({
			selected : true
		})[0];
		var urls = [];
		for (var j = 0; j < data.length; j++) {
			urls.push($scope.dtOptions.aaData[data[j]])
		}

		var ps = {};
		ps.items = angular.toJson(urls);
		ps.module_id = mid;
		$http.post($rootScope.project + "/api/module/updateModuleItemMap.do",
				ps).success(function(res) {
			if (res.success) {
				$uibModalInstance.dismiss('cancel');
			}
			notify({
				message : res.message
			});
		})

	};
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

}

function menuModifyCtl($localStorage, notify, $log, $uibModal,
		$uibModalInstance, $scope, data, $http, $rootScope, $timeout) {
	$log.log("window in:", data);
	$scope.item = {};
	$scope.item = angular.copy(data)

	$scope.topMenuOpt = [];
	$scope.topMenuSel = "";
	$scope.actionOpt = [ {
		id : "Y",
		name : "有效"
	}, {
		id : "N",
		name : "无效"
	} ]
	$scope.actionSel = $scope.actionOpt[0];

	$scope.showOpt = [ {
		id : "Y",
		name : "显示"
	}, {
		id : "N",
		name : "隐藏"
	} ];
	$scope.showSel = $scope.showOpt[0]

	$scope.nodeOpt = [ {
		id : "dir",
		name : "目录"
	}, {
		id : "menu",
		name : "菜单"
	}, {
		id : "btn",
		name : "按钮"
	} ];
	$scope.nodeSel = $scope.nodeOpt[0];

	if ($scope.item.actiontype == "add") {
		$scope.item.node_name = "";
		$scope.item.mark = ""
		$scope.item.keyvalue = ""
		$scope.item.logo = ""
		$scope.item.sort = 0
		$scope.item.module_id = "";
		$scope.item.menu_level = "";
		$scope.item.old_node_id = $scope.item.node_id;
		$scope.item.old_route = $scope.item.route;
	} else if ($scope.item.actiontype == "edit") {
		if ($scope.item.type == "menu") {
			$scope.nodeSel = $scope.nodeOpt[1];
		} else if ($scope.item.type == "dir") {
			$scope.nodeSel = $scope.nodeOpt[0];
		} else if ($scope.item.type == "btn") {
			$scope.nodeSel = $scope.nodeOpt[2];
		}

		if ($scope.item.is_action == "Y") {
			$scope.actionSel = $scope.actionOpt[0];
		} else if ($scope.item.is_action == "N") {
			$scope.actionSel = $scope.actionOpt[1];
		}

		if ($scope.item.is_g_show == "Y") {
			$scope.showSel = $scope.showOpt[0]
		} else if ($scope.item.is_g_show == "N") {
			$scope.showSel = $scope.showOpt[1]
		}
	} else if ($scope.item.actiontype == "addmaster") {
		$scope.item.node_name = "";
		$scope.item.mark = ""
		$scope.item.keyvalue = ""
		$scope.item.logo = ""
		$scope.item.sort = ""
		$scope.item.module_id = "";
		$scope.item.menu_level = "";
		$http.post($rootScope.project + "/api/sysMenus/selectList.do", {})
				.success(function(res) {
					if (res.success) {
						$scope.topMenuOpt = res.data;
						if (res.data.length > 0) {
							$scope.topMenuSel = res.data[0];
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
			var adom = modal[i].getElementsByClassName('chosen-container');

			for (var j = 0; j < adom.length; j++) {
				adom[i].style.width = "100%";
			}
		}
	}, 200);

	$scope.sure = function() {
		var ps = $scope.item;
		// ps.is_action = $scope.actionSel.id;
		ps.is_action = "Y";
		ps.is_g_show = $scope.showSel.id;
		ps.type = $scope.nodeSel.id;
		if (angular.isDefined($scope.topMenuSel.menuId)) {
			ps.menu_id = $scope.topMenuSel.menuId;
		}

		if ($scope.item.actiontype == "edit") {
			$log.log("修改")
			$http.post($rootScope.project + "/api/menu/updateNode.do", ps)
					.success(function(res) {
						if (res.success) {
							$localStorage.remove("dt_sys_menu");
							notify({
								message : res.message
							});
							$uibModalInstance.close("OK");
						} else {
							notify({
								message : res.message
							});
						}
					})
		} else if ($scope.item.actiontype == "add"
				|| $scope.item.actiontype == "addmaster") {
			$log.log("新增")
			$http.post($rootScope.project + "/api/menu/addNode.do", ps)
					.success(function(res) {
						if (res.success) {
							$localStorage.remove("dt_sys_menu");
							notify({
								message : res.message
							});
							$uibModalInstance.close("OK");
						} else {
							notify({
								message : res.message
							});
						}
					})

		}

	};

	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

}

function sysmenuCtl($compile,$timeout,$confirm, $log, notify, $scope, $http, $rootScope,
		$uibModal) {
	$scope.topMenuOpt = []
	$scope.topMenuSel = "";

	$scope.crud = {
		"root_insert" : false,
		"update" : false,
		"insert" : false,
		"remove" : false,
		"priv" : false,
	};

	privCrudCompute($scope.crud, $rootScope.curMemuBtns);

	var acthtml = " <div class=\"btn-group\"> ";

	if ($scope.crud.update) {
		acthtml = acthtml
				+ " <button ng-click=\"cellTemplateScope.edit(row.branch)\" class=\"btn-white btn btn-xs\">更新</button> ";
	}
	if ($scope.crud.insert) {
		acthtml = acthtml
				+ " <button ng-click=\"cellTemplateScope.add(row.branch)\" class=\"btn-white btn btn-xs\">新增</button> ";
	}
	if ($scope.crud.remove) {
		acthtml = acthtml
				+ " <button ng-click=\"cellTemplateScope.del(row.branch)\" class=\"btn-white btn btn-xs\">删除</button> ";
	}
	if ($scope.crud.priv) {
		acthtml = acthtml
				+ " <button ng-click=\"cellTemplateScope.acl(row.branch)\" class=\"btn-white  btn btn-xs\">权限</button>";
	}

	acthtml = acthtml + " </div>";


//	$scope.my_tree = {};
	
	
	$scope.tree_expand_level = 3;
	$scope.tree_data = [];
	$scope.expanding_property = {
		field : "node_name",
		displayName : "名称",
		sortable : true,
		filterable : true,
		cellTemplate : "<i>{{row.branch[expandingProperty.field]}}</i>"
	};
//
//	$timeout(function() {
	 


	$scope.col_defs = [
			{
				field : "keyvalue",
				sortable : false,
				displayName : "程序键",
				sortingType : "string"
			},
			{
				field : "typetext",
				sortable : false,
				displayName : "类型",
				sortingType : "string"
			},
			{
				field : "is_g_show_text",
				sortable : false,
				displayName : "前端显示",
				sortingType : "string"
			},
			{
				field : "acl_cnt",
				sortable : false,
				displayName : "ACL规则数",
				sortingType : "string"
			},
			{
				field : "node_id",
				displayName : "操作",
				cellTemplate : acthtml,
				cellTemplateScope : {
					setmodule : function(data) { // this works too:
						// 展示不实现
						var ps = data;
						var modalInstance = $uibModal
								.open({
									backdrop : true,
									templateUrl : 'views/system/menu/modal_menu_module.html',
									controller : menuModuleCtl,
									size : 'lg',
									resolve : { // 调用控制器与modal控制器中传递值
										data : function() {
											return ps;
										}
									}
								});

						modalInstance.result.then(function(result) {
							$log.log("result", result);
							if (result == "OK") {
								// flush();
							}
						}, function(reason) {
							// 点击空白区域，总会输出backdrop click，点击取消，则会cancel
							$log.log("reason", reason)
						});
					},
					add : function(data) { // this works too:
						// $scope.someMethod;
						console.log($scope.my_tree);
						var ps = data;
						ps.actiontype = "add";
						var modalInstance = $uibModal
								.open({
									backdrop : true,
									templateUrl : 'views/system/menu/modal_menu_save.html',
									controller : menuModifyCtl,
									size : 'lg',
									resolve : { // 调用控制器与modal控制器中传递值
										data : function() {
											return ps;
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
					},
					del : function(data) { // this works too:
						// $scope.someMethod;
						$log.log('del', data);
						var ps = {};
						ps.node_id = data.node_id;
						ps.menu_id = data.menu_id;
						$confirm({
							text : '是否删除本节点?'
						})
								.then(
										function() {
											$http
													.post(
															$rootScope.project
																	+ "/api/menu/deleteNode.do",
															ps)
													.success(
															function(res) {
																notify({
																	message : res.message
																});
																if (res.success) {
																	flush();
																	// /删除本节点,修改删除
																}
															})
										});
					},
					acl : function(data) { // this works too:
						// $scope.someMethod;
						$log.log('acl', data);
						var ps = {};
						ps.node_id = data.node_id;
						ps.menu_id = data.menu_id;

						if (data.type == "dir") {
							notify({
								message : "请选择菜单"
							});
							return;
						}
						var modalInstance = $uibModal
								.open({
									backdrop : true,
									templateUrl : 'views/system/menu/modal_menu_acl.html',
									controller : menuAclCtl,
									size : 'lg',
									resolve : { // 调用控制器与modal控制器中传递值
										data : function() {
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

					},
					edit : function(data) { // this works too:
						// $scope.someMethod;
						var ps = data;
						ps.actiontype = "edit";
						var modalInstance = $uibModal
								.open({
									backdrop : true,
									templateUrl : 'views/system/menu/modal_menu_save.html',
									controller : menuModifyCtl,
									size : 'lg',
									resolve : { // 调用控制器与modal控制器中传递值
										data : function() {
											return ps;
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
				}
			} ];

//	}, 2);
	function flush() {
		$http.post(
				$rootScope.project
						+ "/api/sysMenusNode/queryMenuNodesForStageSetting.do",
				{
					menu_id : $scope.topMenuSel.menuId
				}).success(function(res) {
			if (res.success) {
				var rawTreeData = [];
				var myTreeData = [];
				rawTreeData = res.data
				myTreeData = getTree(rawTreeData, 'node_id', 'parent_id');
				$scope.tree_data = myTreeData;

			}
		})
	}

	$scope.fff = function() {
		$log.log($scope.my_tree);
		$scope.tree_expand_level = 3;
		$scope.my_tree.expand_all();
	}
	$scope.query = function() {
		flush();
	}

	$scope.addMasterNode = function() {
		console.log($scope.my_tree);
		var ps = {};
		ps.actiontype = "addmaster";
		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/system/menu/modal_menu_save.html',
			controller : menuModifyCtl,
			size : 'lg',
			resolve : { // 调用控制器与modal控制器中传递值
				data : function() {
					return ps;
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

	$scope.my_tree_handler = function(branch) {
		console.log('you clicked on', branch)
	}

	console.log('1111', $scope.my_tree)

	$http.post($rootScope.project + "/api/sysMenus/selectList.do", {}).success(
			function(res) {
				if (res.success) {
					$scope.topMenuOpt = res.data;
					if (res.data.length > 0) {
						$scope.topMenuSel = res.data[0];
						flush();
					}
				} else {
					notify({
						message : res.message
					});
				}
			})
};

app.register.controller('sysmenuCtl', sysmenuCtl);