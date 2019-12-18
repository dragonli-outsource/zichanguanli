function modalimportdataFailCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
		$confirm, $log, notify, $scope, meta, $http, $rootScope, $uibModal,
		$uibModalInstance) {

	$scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
			'bAutoWidth', false).withOption('createdRow', function(row) {
		// Recompiling so we can bind Angular,directive to the
		$compile(angular.element(row).contents())($scope);
	});
	$scope.dtInstance = {}

	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

	$scope.dtColumns = [ DTColumnBuilder.newColumn('ct').withTitle('失败列表')
			.withOption('sDefaultContent', '') ]

	$scope.dtOptions.aaData = meta.failed_data;

}

function modalimpordocCtl(DTOptionsBuilder, DTColumnBuilder, $compile,
		$confirm, $log, notify, $scope, meta, $http, $rootScope, $uibModal,
		$window, $uibModalInstance) {

	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

	$scope.downdict = function() {
		$window.open($rootScope.project + "/api/base/res/exportDictItems.do");
	}
	$scope.downTpl = function() {		
		$window.open($rootScope.project + "/api/base/res/exportAllRes.do?loc=-1");
	}

}

function zcdataImportCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
		$log, notify, $scope, $http, $rootScope, $uibModal, $window, $timeout) {

	$scope.okbtnstatus = false;
	$scope.importOpt = [ {
		id : "insert",
		name : "新增"
	}, {
		id : "update",
		name : "更新"
	} ];
	$scope.importSel = $scope.importOpt[1];

	$scope.dzconfig = {
		url : 'fileupload.do',
		maxFilesize : 10000,
		paramName : "file",
		maxThumbnailFilesize : 2,
		// 一个请求上传多个文件
		uploadMultiple : true,
		// 当多文件上传,需要设置parallelUploads>=maxFiles
		parallelUploads : 1,
		maxFiles : 1,
		dictDefaultMessage : "点击上传需要上传的文件",
		acceptedFiles : ".xlsx,.xls",
		// 添加上传取消和删除预览图片的链接，默认不添加
		addRemoveLinks : true,
		// 关闭自动上传功能，默认会true会自动上传
		// 也就是添加一张图片向服务器发送一次请求
		autoProcessQueue : false,
		init : function() {
			$scope.myDropzone = this; // closure
		}
	};

	$scope.doc = function() {
		var modalInstance = $uibModal.open({
			backdrop : true,
			templateUrl : 'views/cmdb/modal_importdoc.html',
			controller : modalimpordocCtl,
			size : 'blg',
			resolve : { // 调用控制器与modal控制器中传递值
				meta : function() {
					return ""
				}
			}
		});
		$scope.myDropzone.removeAllFiles(true);
		modalInstance.result.then(function(result) {
		}, function(reason) {
			// 点击空白区域，总会输出backdrop click，点击取消，则会cancel
			$log.log("reason", reason)
		});

	}

	$scope.ok = function() {

		$scope.okbtnstatus = true;
		var id = getUuid();
		console.log("开始上传文件" + id);
		if ($scope.myDropzone.files.length > 0) {
			$scope.myDropzone.options.url = $rootScope.project
					+ '/api/file/fileupload.do?uuid=' + id
					+ '&bus=file&interval=10000&bus=file';
			$scope.myDropzone.uploadFile($scope.myDropzone.files[0])
		} else {
			notify({
				message : "请选择文件"
			});
		}
		$timeout(function() {
			$http.post($rootScope.project + "/api/base/res/importResData.do", {
				type : $scope.importSel.id,
				id : id
			}).success(function(res) {
				$scope.okbtnstatus = false;
				if (res.success) {
					$scope.myDropzone.removeAllFiles(true);
					notify({
						message : "操作成功！"
					});
				} else {

					var modalInstance = $uibModal.open({
						backdrop : true,
						templateUrl : 'views/cmdb/modal_importFail.html',
						controller : modalimportdataFailCtl,
						size : 'blg',
						resolve : { // 调用控制器与modal控制器中传递值
							meta : function() {
								return res.data;
							}
						}
					});
					$scope.myDropzone.removeAllFiles(true);
					modalInstance.result.then(function(result) {
					}, function(reason) {
						// 点击空白区域，总会输出backdrop click，点击取消，则会cancel
						$log.log("reason", reason)
					});

				}
			})
		}, 3000);

	}
};

app.register.controller('zcdataImportCtl', zcdataImportCtl);