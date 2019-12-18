
function modaldevfaultCtl($timeout, $localStorage, notify, $log, $uibModal,
		$uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
		DTColumnBuilder, $compile) {

	$scope.item = {};
	$scope.data = {};
	$scope.data.mark = "";
	$scope.data.reason = "";
	$scope.dzconfig = {
		url : 'fileupload.do',
		maxFilesize : 10000,
		paramName : "file",
		maxThumbnailFilesize : 5,
		// 一个请求上传多个文件
		uploadMultiple : true,
		// 当多文件上传,需要设置parallelUploads>=maxFiles
		parallelUploads : 5,
		maxFiles : 5,
		dictDefaultMessage : "点击上传附件",
		acceptedFiles : "image/jpeg,image/png,image/gif,.xls,.zip,.rar,.doc,.pdf,.docx,.txt,.xlsx",
		// 添加上传取消和删除预览图片的链接，默认不添加
		addRemoveLinks : true,
		// 关闭自动上传功能，默认会true会自动上传
		// 也就是添加一张图片向服务器发送一次请求
		autoProcessQueue : false,
		init : function() {
			$scope.myDropzone = this; // closure
		}
	};

	$scope.selectzc = function() {

		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/cmdb/modal_devfault_zclist.html',
			controller : modal_faultZcListCtl,
			size : 'blg',
			resolve : { // 调用控制器与modal控制器中传递值
				id : function() {
					return ""
				},
				type : function() {
					return "one"
				}
			}
		});

		modalInstance.result.then(function(result) {
			$scope.item = result;
		}, function(reason) {
			// 点击空白区域，总会输出backdrop click，点击取消，则会cancel
			$log.log("reason", reason)
		});

	}

	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

	$scope.sure = function() {
		// 上传图片
		if (!angular.isDefined($scope.item.id)) {
			notify({
				message : "请先选择需要报修的资产"
			});
			return;
		}

		// 产品图片
		var file = "";
		for (var i = 0; i < $scope.myDropzone.files.length; i++) {
			var id = getUuid();
			// 判断,已经上传的不在上传
			if (typeof ($scope.myDropzone.files[i].uuid) == "undefined") {
				console.log("开始上传文件" + id);
				$scope.myDropzone.options.url = $rootScope.project
						+ '/api/file/fileupload.do?uuid=' + id
						+ '&bus=file&interval=10000&bus=file';
				$scope.myDropzone.uploadFile($scope.myDropzone.files[i])
			} else {
				// 已经上传
				console.log("已经上传" + id);
				id = $scope.myDropzone.files[i].uuid;
			}
			file = file + id + "#";
		}

		$scope.item.files = file;

		console.log($scope.item.files);
		$scope.data.f_res_id = $scope.item.id;
		$scope.data.files = $scope.item.files;
		$http.post($rootScope.project + "/api/base/res/savefault.do",
				$scope.data).success(function(res) {
			if (res.success) {
				notify({
					message : res.message
				});
				$uibModalInstance.close('ok');
			} else {
				notify({
					message : res.message
				});
			}
		})

	}
}

function cmdbfaultrecordCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
		$confirm, $log, notify, $scope, $http, $rootScope, $uibModal) {

	// 分类
	$scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
			.withPaginationType('full_numbers').withDisplayLength(100)
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
			DTColumnBuilder.newColumn(null).withTitle(ckHtml).withClass(
					'select-checkbox checkbox_center').renderWith(function() {
				return ""
			}),
			DTColumnBuilder.newColumn('f_uuid').withTitle('报修编号').withOption(
					'sDefaultContent', '').withOption("width", '30'),
			DTColumnBuilder.newColumn('f_processuser').withTitle('处理人')
					.withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('f_reason').withTitle('报修原因').withOption(
					'sDefaultContent', '').withOption("width", '30'),
			DTColumnBuilder.newColumn('f_processuser').withTitle('处理时间')
					.withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('file_cnt').withTitle('文件数').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('f_create_time').withTitle('报修录入时间')
					.withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('uuid').withTitle('资产编号').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('classname').withTitle('资产类型')
					.withOption('sDefaultContent', '')
					.withOption("width", '30'),
			DTColumnBuilder.newColumn('brandstr').withTitle('品牌').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('name').withTitle('型号').withOption(
					'sDefaultContent', '').withOption('width', '50')
					.renderWith(renderName),

			DTColumnBuilder.newColumn('locstr').withTitle('位置').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('part_name').withTitle('部门').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('used_username').withTitle('使用人')
					.withOption('sDefaultContent', '')
					.withOption('width', '30'),
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
					.withOption('sDefaultContent', '') ]

	$scope.query = function() {
		flush();

	}

	var meta = {
		tablehide : false,
		tools : [
				{
					id : "input",
					label : "内容",
					placeholder : "输入型号、编号、序列号",
					type : "input",
					show : true,
					ct : ""

				},
				{
					id : "btn",
					label : "",
					type : "btn",
					show : false,
					priv:"select",
					template : ' <button ng-click="query()" class="btn btn-sm btn-primary" type="submit">搜索</button>'
				},
				{
					id : "btn",
					label : "",
					type : "btn",
					show : false,
					priv:"remove",
					template : ' <button ng-click="del()" class="btn btn-sm btn-primary" type="submit"> 删除</button>'
				},
				{
					id : "btn2",
					label : "",
					type : "btn",
					show : false,
					priv:"fix",
					template : ' <button ng-click="fault()" class="btn btn-sm btn-primary" type="submit">申请报修</button>'
				} ]
	}
	$scope.meta = meta;
 
	privNormalCompute($scope.meta.tools, $rootScope.curMemuBtns);
	function flush() {
		var ps = {}

		ps.search = $scope.meta.tools[0].ct;
		console.log($scope.meta.tools[0].ct)

		$http
				.post($rootScope.project + "/api/base/res/queryAllResFault.do",
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

	var gdicts = {};
	$http
			.post(
					$rootScope.project + "/api/base/res/queryDictFast.do",
					{
						dicts : "devbrand,devrisk,devenv,devrecycle,devwb,devdc,devservertype,devrack"
					}).success(function(res) {
				if (res.success) {
					gdicts = res.data;
				} else {
					notify({
						message : res.message
					});
				}
			})

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

	$scope.detail = function() {
		var id = "";
		var selrow = getSelectRow();
		if (angular.isDefined(selrow)) {
			id = selrow.id;
		} else {
			return;
		}
		var ps = {};
		ps.id = id;
		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/cmdb/modal_dtl.html',
			controller : modalcmdbdtlCtl,
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

	$scope.del = function(id) {
		var id = "";
		var selrow = getSelectRow();
		if (angular.isDefined(selrow)) {
			id = selrow.f_id;
		} else {
			return;
		}
		var ps = {};
		ps.id = id;
		if (angular.isDefined(ps.id)) {
			$confirm({
				text : '是否删除?'
			}).then(
					function() {
						$http.post(
								$rootScope.project
										+ "/api/base/res/removefault.do", {
									id : ps.id
								}).success(function(res) {
							if (res.success) {
								flush();
							} else {
								notify({
									message : res.message
								});
							}
						});
					});
		}

	}

	$scope.fault = function(id) {

		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/cmdb/modal_devfault.html',
			controller : modaldevfaultCtl,
			size : 'lg',
			resolve : { // 调用控制器与modal控制器中传递值
				meta : function() {
					return "";
				}
			}
		});

		modalInstance.result.then(function(result) {
			$log.log("result", result);
			flush();
		}, function(reason) {
			// 点击空白区域，总会输出backdrop click，点击取消，则会cancel
			$log.log("reason", reason)
		});

	}

};

app.register.controller('cmdbfaultrecordCtl', cmdbfaultrecordCtl);
