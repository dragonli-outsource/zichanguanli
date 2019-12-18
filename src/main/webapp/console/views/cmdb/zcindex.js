function zcindexCtl(DTOptionsBuilder, DTColumnBuilder, $compile, $confirm,
		$log, notify,$scope, $http, $rootScope, $uibModal, $window,$state) {

	

	$scope.item = {};
	$scope.item.zccnt=0;
	$scope.item.zctotal=0;
	$scope.item.lycnt=0;
	$scope.item.zccnt=0;
	$scope.item.jycnt=0;
	$scope.item.bxcnt=0;
	$scope.item.zycnt=0;
	$scope.item.wbdqcnt=0;
	
	
	
	$http.post($rootScope.project + "/api/base/res/rep/dashboardindex.do", {})
			.success(function(res) {
				if (res.success) {

					$scope.item = res.data;
					var ticks = res.data.chart_meta;
					var data = res.data.chart_data;
				
					var dataset = [ {
						label : "资产数量",
						data : data,
						color : "#5482FF"
					}

					];
					var barOptions = {
						series : {
							bars : {
								show : true,
								barWidth : 0.6,
								fill : true,
								fillColor : {
									colors : [ {
										opacity : 0.8
									}, {
										opacity : 0.8
									} ]
								}
							}
						},
						bars : {
							align : "center",
							fill : 1,
							barWidth : 0.5
						},
						grid : {
							hoverable : true,
							borderWidth : 2,
							backgroundColor : {
								colors : [ "#ffffff", "#EDF5FF" ]
							}
						},
						xaxis : {
							axisLabel : "",
							axisLabelUseCanvas : true,
							axisLabelFontSizePixels : 12,
							axisLabelFontFamily : 'Verdana, Arial',
							// axisLabelPadding : 10,
							axisLabelPadding : 10,
							tickSize : 2,
							ticks : ticks

						},
//						legend : {
//							noColumns : 0,
//							labelBoxBorderColor : "#000000",
//							position : "nw"
//						},
						  legend: {
					            show: false
					        },
					        tooltip: true,
					        tooltipOpts: {
					            content: "数量: %y"
					        }
					};

					$scope.charts = {};
					$scope.charts.flotChartData = dataset;
					$scope.charts.flotBarOptions = barOptions;

				} else {
					notify({
						message : res.message
					});
				}
			})

			
};

app.register.controller('zcindexCtl',zcindexCtl);