function prodCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm, $log,
		notify, $scope, $http, $rootScope, $uibModal) {
	$scope.btn_query = function() {
		console.log("query");

		$scope.text = $scope.text + "a";
	}

	$scope.btn_add = function() {

		var meta = {};
		meta = {
			footer_hide : false,
			title : "测试",
			item : {
				addr : "金"
			},
			dtldzconfig : {
				url : 'fileupload.do',
				maxFilesize : 10000,
				paramName : "file",
				maxThumbnailFilesize : 1,
				// 一个请求上传多个文件
				uploadMultiple : true,
				// 当多文件上传,需要设置parallelUploads>=maxFiles
				parallelUploads : 1,
				maxFiles : 1,
				dictDefaultMessage : "点击上传图片",
				acceptedFiles : "image/jpeg,image/png,image/gif",
				// 添加上传取消和删除预览图片的链接，默认不添加
				addRemoveLinks : true,
				// 关闭自动上传功能，默认会true会自动上传
				// 也就是添加一张图片向服务器发送一次请求
				autoProcessQueue : false,
				init : function() {
					$scope.myDropzone = this; // closure
				}
			},
			stime : moment().subtract(15, "days"),
			statusOpt : [ {
				id : 1,
				name : "1"
			}, {
				id : 2,
				name : "2"
			}, {
				id : 3,
				name : "3"
			} ],
			statusSel : "",
			items : [ {
				type : "pic",
				disabled : "false",
				label : "图片",
				need : true,
				name : 'text',
				conf : "dtldzconfig"
			}, {
				type : "datetime",
				disabled : "false",
				label : "文本",
				need : true,
				ng_model : "stime"
			}, {
				type : "textarea",
				disabled : "false",
				sub_type : "text",
				required : true,
				maxlength : "2",
				placeholder : "请输入姓名",
				label : "文本",
				need : true,
				height : "200px",
				name : 'text',
				ng_model : "text"
			}, {
				type : "input",
				disabled : "false",
				sub_type : "text",
				required : false,
				maxlength : "2",
				placeholder : "请输入姓名",
				label : "姓名",
				need : true,
				name : 'name',
				ng_model : "name"
			}, {
				type : "dashed"
			}, {
				type : "input",
				disabled : "false",
				sub_type : "number",
				required : true,
				maxlength : "10",
				placeholder : "请输入手机",
				label : "手机",
				need : true,
				name : 'mobile',
				ng_model : "mobile"
			}, {
				type : "input",
				disabled : "false",
				sub_type : "text",
				required : true,
				maxlength : "10",
				placeholder : "请输入地址",
				label : "地址",
				need : false,
				name : 'addr',
				ng_model : "addr"
			}, {
				type : "select",
				disabled : "false",
				label : "地址",
				need : false,
				disable_search : "true",
				dataOpt : "statusOpt",
				dataSel : "statusSel"
			} ],
			sure : function(modalInstance, modal_meta) {
				// 返回接口
				console.log(modal_meta);
				console.log($scope);
				console.log(modal_meta.meta.stime.format('YYYY-MM-DD'));
				var picid = getUuid();
				$scope.myDropzone.options.url = $rootScope.project
						+ '/api/file/fileupload.do?bus=prodimgs&uuid=' + picid
						+ '&type=image&interval=10000';
				$scope.myDropzone.uploadFile($scope.myDropzone.files[0])

				console.log($scope.myDropzone.files[0]);
			},
			init : function(modal_meta) {
				console.log("INIT");
				$http.post($rootScope.project + "/api/user/queryGroup.do", {})
						.success(function(res) {
							console.log(res);
							modal_meta.meta.statusOpt = res.data;
						});
			}
		}

		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/Template/modal_simpleForm.html',
			controller : modal_simpleFormCtl,
			size : 'lg',
			resolve : { // 调用控制器与modal控制器中传递值
				meta : function() {
					return meta;
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
	$scope.btn_del = function() {
		console.log("del");
	}

	$scope.btn_modify = function() {
		console.log("modify");
	}
	function a() {
		console.log(99);
	}
	var opt = [ {
		id : "1",
		name : "adfasdfasA"
	}, {
		id : "1",
		name : "B"
	}, {
		id : "1",
		name : "C"
	} ];

	var meta = {
		tablehide : false,
		tools : [
				{
					id : "datetime",
					label : "测试",
					type : "datetime"

				},
				{
					id : "input",
					label : "212",
					label_hide : true,
					type : "input",
					placeholder : "1212",
					ct : ""
				},
				{
					id : "select",
					label : "选择",
					type : "select",
					disablesearch : true,
					dataOpt : opt,
					dataSel : opt[0]
				},
				{
					id : "btn",
					label : "",
					type : "btn",
					template : ' <button ng-click="abcd1()" class="btn btn-sm btn-primary" type="submit">查询c</button>'
				}, {
					id : "btn_query",
					label : "查询",
					type : "btn_query"
				}, {
					id : "btn_add",
					fun : "",
					label : "新增",
					type : "btn_add"
				}, {
					id : "btn_del",
					fun : "",
					label : "删除",
					type : "btn_del"
				}, {
					id : "btn_modify",
					fun : "",
					label : "更新",
					type : "btn_modify"
				}, {
					id : "btn_actiona",
					fun : "",
					label : "action",
					type : "btn_actiona"
				} ]
	}

	$scope.meta = meta;

	$scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption('scrollY',
			'300px').withOption('scrollX', true).withOption('bAutoWidth', true)
			.withOption('responsive', false).withOption('scrollCollapse', true)
			.withOption('paging', true).withFixedColumns({
				leftColumns : 0,
				rightColumns : 1
			})
	$scope.dtInstance = {}
	function renderAction(data, type, full) {
		var acthtml = " <div class=\"btn-group\"> ";
		acthtml = acthtml + " <button ng-click=\"save('" + full.role_id
				+ "')\" class=\"btn-white btn btn-xs\">更新</button> ";
		// acthtml = acthtml + " <button ng-click=\"row_detail()\"
		// class=\"btn-white btn btn-xs\">详细</button> ";
		acthtml = acthtml + " <button ng-click=\"row_del('" + full.role_id
				+ "')\" class=\"btn-white btn btn-xs\">删除</button> </div> ";
		return acthtml;
	}

	$scope.dtColumns = [
			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),

			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),

			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),

			DTColumnBuilder.newColumn('role_name').withTitle('名称').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('is_action').withTitle('状态').withOption(
					'sDefaultContent', '').renderWith(
					function(data, type, full) {
						return dt_renderMapSimple(data, type, full, [ {
							id : "Y",
							name : "有效",
							id : "N",
							name : "无效"
						} ]);
					}),
			DTColumnBuilder.newColumn('role_id').withTitle('操作').withOption(
					'width', '200px').withOption('sDefaultContent', '')
					.renderWith(renderAction) ]

	function flush() {
		console.log("!!!!!!!!!!!!!!323");
		var ps = {}
		var d = [];
		var a = {
			role_id : 1
		};
		d.push({
			role_id : 2
		});
		d.push({
			role_id : 3
		});
		d.push({
			role_id : 5
		});
		d.push({
			role_id : 7
		});
		d.push({
			role_id : 8
		});
		d.push({
			role_id : 19
		});
		d.push({
			role_id : 11
		});

		$scope.dtOptions.aaData = d;
		// $http.post($rootScope.project + "/api/role/roleQuery.do",
		// ps).success(
		// function(res) {
		// if (res.success) {
		// $scope.dtOptions.aaData = res.data;
		// } else {
		// notify({
		// message : res.message
		// });
		// }
		// })
	}
	//flush();

	$scope.row_detail = function(id) {

	}
	$scope.row_del = function(id) {
		$confirm({
			text : '是否删除功能?'
		}).then(function() {
			$http.post($rootScope.project + "/api/role/roleDelete.do", {
				role_id : id
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
app.register.controller('prodCtl', prodCtl);