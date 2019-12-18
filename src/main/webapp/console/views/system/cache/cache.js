function sysCacheCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
		$log, notify, $scope, $http, $rootScope, $uibModal,$stateParams) {

	$scope.meta = {
		tools : [
				{
					id : "1",
					label : "缓存域",
					type : "select",
					disablesearch : true,
					dataOpt : [],
					dataSel : "",
					show:true,
				},
				{
					id : "2",
					label : "查询",
					type : "btn",
					show:true,
					template : ' <button ng-click="flush()" class="btn btn-sm btn-primary" type="submit">查询</button>'

				} ]
	}

	$http.post($rootScope.project + "/api/sysApi/system/queryCacheName.do", {})
			.success(function(res) {
				if (res.success) {
					$scope.meta.tools[0].dataOpt = res.data;
					if (res.data.length > 0) {
						$scope.meta.tools[0].dataSel = res.data[0]
						flush();
					}
				} else {
					notify({
						message : res.message
					});
				}
			})

	$scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
			'createdRow', function(row) {
				// Recompiling so we can bind Angular,directive to the
				$compile(angular.element(row).contents())($scope);
			});
	$scope.dtInstance = {}

	var crud = {
		"update" : false,
		"insert" : false,
		"select" : false,
		"remove" : false,
	};
	var pbtns=$rootScope.curMemuBtns;
	privCrudCompute(crud,pbtns);

	function renderAction(data, type, full) {
		var acthtml = " <div class=\"btn-group\"> ";
		acthtml = acthtml + " <button ng-click=\"refresh('" + full.key + "','"
				+ full.cache
				+ "')\" class=\"btn-white btn btn-xs\">刷新</button>   ";
		if (crud.remove) {
			acthtml = acthtml + " <button ng-click=\"removeCacheKey('"
					+ full.key
					+ "')\" class=\"btn-white btn btn-xs\">删除</button> ";
		}
		acthtml = acthtml + " </div>";
		return acthtml;
	}

	function renderStatus(data, type, full) {

		var jobenableimg = '<img src="img/sm/unknow.png"/>';
		if (data == 'true') {
			jobenableimg = '<img src="img/sm/green.png"/>';
		} else if (data == 'false') {
			jobenableimg = '<img src="img/sm/grey.png"/>';
		}
		return jobenableimg;
	}

	function renderRunStatus(data, type, full) {

		var value = data
		if (data == 'NORMAL') {
			value = '正常'
		} else if (data == 'PAUSED') {
			value = '暂停'
		}
		return value;
	}

	$scope.dtColumns = [

			DTColumnBuilder.newColumn('key').withTitle('Key').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('ttl').withTitle('TTL').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('tti').withTitle('TTI').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('hit').withTitle('命中次数').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('ctime').withTitle('创建时间').withOption(
					'sDefaultContent', ''),
			DTColumnBuilder.newColumn('accesstime').withTitle('访问时间')
					.withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('expiretime').withTitle('过期时间')
					.withOption('sDefaultContent', ''),
			DTColumnBuilder.newColumn('key').withTitle('操作').withOption(
					'sDefaultContent', '').renderWith(renderAction) ]

	function flush() {

		$http.post($rootScope.project + "/api/sysApi/system/queryCacheKeys.do",
				{
					cache : $scope.meta.tools[0].dataSel.id
				}).success(function(res) {
			if (res.success) {
				$scope.dtOptions.aaData = res.data;
			} else {
				notify({
					message : res.message
				});
			}
		})
	}

	$scope.flush = function() {
		flush();
	}

	$scope.save = function() {
		alert("待开发");
	}

	$scope.del = function(seq) {
		alert("待开发");

	}
	$scope.removeCacheKey = function(key) {
		$http.post($rootScope.project + "/api/sysApi/system/removeCacheKey.do",
				{
					key : key,
					cache : $scope.cacheSel.id
				}).success(function(res) {
			notify({
				message : res.message
			});
			if (res.success) {
				flush();
			}
		})
	}

	$scope.refresh = function(key, cache) {
		$http.post($rootScope.project + "/api/sysApi/system/refreshCache.do", {
			key : key,
			cache : cache
		}).success(function(res) {
			notify({
				message : res.message
			});
			if (res.success) {
				flush();
			}
		})
	}

};

app.register.controller('sysCacheCtl', sysCacheCtl);