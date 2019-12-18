/**
 * INSPINIA - Responsive Admin Theme 2.7.1
 * 
 * Custom scripts
 */

$(document).ready(
		function() {

			// Full height of sidebar
			function fix_height() {
				var heightWithoutNavbar = $("#wrapper").height() - 61;
				$(".sidebar-panel").css("min-height",
						heightWithoutNavbar + "px");

				var navbarHeight = $('nav.navbar-default').height();
				var wrapperHeight = $('#page-wrapper').height();

				// $(".sidebar-panel").css("min-height", wrapperHeigh - 61 +
				// "px");

				if (navbarHeight > wrapperHeight) {
					$('#page-wrapper').css("min-height", navbarHeight + "px");
				}

				if (navbarHeight < wrapperHeight) {
					$('#page-wrapper').css("min-height",
							$(window).height() + "px");
				}

				if ($('body').hasClass('fixed-nav')) {
					if (navbarHeight > wrapperHeight) {
						$('#page-wrapper').css("min-height",
								navbarHeight + "px");
					} else {
						$('#page-wrapper').css("min-height",
								$(window).height() - 60 + "px");
					}
				}

			}

			$(window).bind("load resize scroll", function() {
				if (!$("body").hasClass('body-small')) {
					fix_height();
				}
			});

			// Move right sidebar top after scroll
			$(window).scroll(
					function() {
						if ($(window).scrollTop() > 0
								&& !$('body').hasClass('fixed-nav')) {
							$('#right-sidebar').addClass('sidebar-top');
						} else {
							$('#right-sidebar').removeClass('sidebar-top');
						}
					});

			setTimeout(function() {
				fix_height();
			})
		});

// Minimalize menu when screen is less than 768px
$(window).bind("load resize", function() {
	if ($(document).width() < 769) {
		$('body').addClass('body-small')
	} else {
		$('body').removeClass('body-small')
	}
});

/** *******************常用函数**************************** */
// 生产UUid
function getUuid() {
	var len = 32;// 32长度
	var radix = 16;// 16进制
	var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'
			.split('');
	var uuid = [], i;
	radix = radix || chars.length;
	if (len) {
		for (i = 0; i < len; i++)
			uuid[i] = chars[0 | Math.random() * radix];
	} else {
		var r;
		uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
		uuid[14] = '4';
		for (i = 0; i < 36; i++) {
			if (!uuid[i]) {
				r = 0 | Math.random() * 16;
				uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
			}
		}
	}
	return uuid.join('');
}

function prepend(arr, item) {
	// 将arr数组复制给a
	var a = arr.slice(0);
	// 使用unshift方法向a开头添加item
	a.unshift(item);
	return a;
}

/** **************按钮权限判断函数*************** */
function privCrudCompute(curd, pbtns) {
	if (!angular.isDefined(pbtns)) {
		return;
	}
	if (pbtns == "") {
		return;
	}

	var pbtns_arr = angular.fromJson(pbtns);
	if (angular.isDefined(pbtns_arr) && pbtns_arr.length > 0) {
		for (var i = 0; i < pbtns_arr.length; i++) {
			if (pbtns_arr[i].p == "root_insert") {
				curd.root_insert = true;
				continue;
			}
			if (pbtns_arr[i].p == "update") {
				curd.update = true;
				continue;
			}
			if (pbtns_arr[i].p == "remove") {
				curd.remove = true;
				continue;
			}
			if (pbtns_arr[i].p == "select") {
				curd.select = true;
				continue;
			}
			if (pbtns_arr[i].p == "insert") {
				curd.insert = true;
				continue;
			}
			if (pbtns_arr[i].p == "submit") {
				curd.submit = true;
				continue;
			}
			if (pbtns_arr[i].p == "exportfile") {
				curd.exportfile = true;
				continue;
			}
			if (pbtns_arr[i].p == "importfile") {
				curd.importfile = true;
				continue;
			}
			if (pbtns_arr[i].p == "uploadfile") {
				curd.uploadfile = true;
				continue;
			}
			if (pbtns_arr[i].p == "item_insert") {
				curd.item_insert = true;
				continue;
			}
			if (pbtns_arr[i].p == "item_select") {
				curd.item_select = true;
				continue;
			}
			if (pbtns_arr[i].p == "item_update") {
				curd.item_update = true;
				continue;
			}
			if (pbtns_arr[i].p == "item_remove") {
				curd.item_remove = true;
				continue;
			}
			if (pbtns_arr[i].p == "priv") {
				curd.priv = true;
				continue;
			}
			if (pbtns_arr[i].p == "cpwd") {
				curd.cpwd = true;
				continue;
			}
			if (pbtns_arr[i].p == "act1") {
				curd.act1 = true;
				continue;
			}

			if (pbtns_arr[i].p == "act2") {
				curd.act2 = true;
				continue;
			}
			if (pbtns_arr[i].p == "act3") {
				curd.act3 = true;
				continue;
			}

			if (pbtns_arr[i].p == "fix") {
				curd.fix = true;
				continue;
			}

		}
	}

}

function privNormalCompute(meta, pbtns) {
	console.log("#########privNormalCompute###########");
	if (!angular.isDefined(pbtns)) {
		return;
	}
	if (pbtns == "") {
		return;
	}
	var pbtns_arr = angular.fromJson(pbtns);
	if (angular.isDefined(pbtns_arr) && pbtns_arr.length > 0) {
		for (var i = 0; i < meta.length; i++) {
			for (var j = 0; j < pbtns_arr.length; j++) {
				if (meta[i].priv == pbtns_arr[j].p) {
					meta[i].show = true;
				}
			}
		}
	}
}

/** *******************datatable**************************** */
function dt_renderUDAction(data, type, full) {
	var acthtml = " <div class=\"btn-group\"> ";
	acthtml = acthtml + " <button ng-click=\"update('" + full.id
			+ "')\" class=\"btn-white btn btn-xs\">更新</button>   ";
	acthtml = acthtml + " <button ng-click=\"del('" + full.id
			+ "')\" class=\"btn-white btn btn-xs\">删除</button> ";
	acthtml = acthtml + "</div>"
	return acthtml;
}

function dt_renderMapSimple(data, type, full, map) {
	if (typeof (map) == undefined) {
		return "unknow";
	}
	for (var i = 0; i < map.length; i++) {
		if (map[i].id = data) {
			return map[i].name;
		}
	}
	return "unknow";
}
/** *******************datatable end**************************** */
/** ******************************modal 模版************************* */
// 支持:输入框,文本框,单选,日期控件
function modal_simpleFormCtl($timeout, $localStorage, notify, $log, $uibModal,
		$uibModalInstance, $scope, meta, $http, $rootScope) {
	$scope.meta = meta;
	$log.log($scope.meta);
	var formhtml = "";
	var items = meta.items;
	var select_ids = [];

	if (typeof (items) != "undefined" && items.length > 0) {
		for (var i = 0; i < items.length; i++) {
			var tmp_tpl = "";
			var obj = items[i];
			var need_col = "";
			if (obj.need) {
				need_col = "<span class=\"text-danger\">*</span>";
			}
			if (obj.type == "input") {
				var required_col = "";
				if (obj.required) {
					required_col = "required";
				}
				var maxlength_col = "";
				if (typeof (obj.maxlength) != "undefined" && obj.maxlength > 0) {
					maxlength_col = "ng-maxlength=\"" + obj.maxlength + "\"";
				}
				tmp_tpl = tmp_tpl + "<div class=\"form-group\">";
				tmp_tpl = tmp_tpl + "<label class=\"col-sm-2 control-label\">"
						+ need_col + obj.label + ":</label> ";
				tmp_tpl = tmp_tpl + "<div class=\"col-sm-10\">    ";
				tmp_tpl = tmp_tpl
						+ "	<input ng-disabled=\""
						+ obj.disabled
						+ "\" type=\""
						+ obj.sub_type
						+ "\" "
						+ required_col
						+ "  "
						+ maxlength_col
						+ "  class=\"form-control ng-pristine ng-untouched ng-valid ng-empty\" placeholder=\""
						+ obj.placeholder + "\" name=\"" + obj.name
						+ "\" ng-model=\"meta.item." + obj.ng_model + "\" > ";
				tmp_tpl = tmp_tpl
						+ "	<div class=\"text-danger\" ng-if=\"myForm."
						+ obj.name + ".$dirty && myForm." + obj.name
						+ ".$invalid\"> ";
				tmp_tpl = tmp_tpl + "		<span ng-if=\"myForm." + obj.name
						+ ".$error.required\"> 输入不能为空 </span> ";
				tmp_tpl = tmp_tpl + "		<span ng-show=\"myForm." + obj.name
						+ ".$error.maxlength\">不能超过" + obj.maxlength
						+ "个字符</span> ";
				tmp_tpl = tmp_tpl + "	</div> ";
				tmp_tpl = tmp_tpl + "</div> ";
				tmp_tpl = tmp_tpl + "</div> ";
				formhtml = formhtml + tmp_tpl;
			} else if (obj.type == "select") {
				var uid = getUuid()
				select_ids.push(uid);
				tmp_tpl = tmp_tpl + " <div class=\"form-group\">";
				tmp_tpl = tmp_tpl + "<label class=\"col-sm-2 control-label\">"
						+ need_col + obj.label + ":</label> ";
				tmp_tpl = tmp_tpl + "<div class=\"col-sm-10\"> ";
				tmp_tpl = tmp_tpl
						+ "	<select class=\"dt_select\" width=\"100\" id=\""
						+ uid
						+ "\"   chosen disable-search=\""
						+ obj.disable_search
						+ "\" class=\"chosen-select\" no-results-text=\"'没有找到相应条目'\" ng-model=\"meta."
						+ obj.dataSel
						+ "\"  data-placeholder-text-single=\"'请选择...'\" ng-options=\"item.name for item in meta."
						+ obj.dataOpt + "\"> ";
				tmp_tpl = tmp_tpl + "		<option value=\"\"></option> ";
				tmp_tpl = tmp_tpl + "	</select> ";
				tmp_tpl = tmp_tpl + "</div> ";
				tmp_tpl = tmp_tpl + "</div> ";
				formhtml = formhtml + tmp_tpl;
			} else if (obj.type == "dashed") {
				formhtml = formhtml + "<div class=\"hr-line-dashed\"></div>";
			} else if (obj.type == "textarea") {
				var required_col = "";
				if (obj.required) {
					required_col = "required";
				}
				var maxlength_col = "";
				if (typeof (obj.maxlength) != "undefined" && obj.maxlength > 0) {
					maxlength_col = "ng-maxlength=\"" + obj.maxlength + "\"";
				}
				var height_col = "";
				if (typeof (obj.height) != "undefined" && obj.height.length > 0) {
					height_col = "style=\"height:" + obj.height + "\"";
				}
				tmp_tpl = tmp_tpl + "<div class=\"form-group\">";
				tmp_tpl = tmp_tpl + "<label class=\"col-sm-2 control-label\">"
						+ need_col + obj.label + ":</label> ";
				tmp_tpl = tmp_tpl + "<div class=\"col-sm-10\">    ";
				tmp_tpl = tmp_tpl
						+ "	<textarea "
						+ height_col
						+ " ng-disabled=\""
						+ obj.disabled
						+ "\" type=\""
						+ obj.sub_type
						+ "\" "
						+ required_col
						+ "  "
						+ maxlength_col
						+ "  class=\"form-control ng-pristine ng-untouched ng-valid ng-empty\" placeholder=\""
						+ obj.placeholder + "\" name=\"" + obj.name
						+ "\" ng-model=\"meta.item." + obj.ng_model
						+ "\" > </textarea>";
				tmp_tpl = tmp_tpl
						+ "	<div class=\"text-danger\" ng-if=\"myForm."
						+ obj.name + ".$dirty && myForm." + obj.name
						+ ".$invalid\"> ";
				tmp_tpl = tmp_tpl + "		<span ng-if=\"myForm." + obj.name
						+ ".$error.required\"> 输入不能为空 </span> ";
				tmp_tpl = tmp_tpl + "		<span ng-show=\"myForm." + obj.name
						+ ".$error.maxlength\">不能超过" + obj.maxlength
						+ "个字符</span> ";
				tmp_tpl = tmp_tpl + "	</div> ";
				tmp_tpl = tmp_tpl + "</div> ";
				tmp_tpl = tmp_tpl + "</div> ";
				formhtml = formhtml + tmp_tpl;
			} else if (obj.type == "datetime") {
				tmp_tpl = tmp_tpl + "<div class=\"form-group\"> ";
				tmp_tpl = tmp_tpl + "<label class=\"col-sm-2 control-label\">"
						+ need_col + obj.label + ":</label> ";
				tmp_tpl = tmp_tpl + "<div class=\"col-sm-10\"> ";
				tmp_tpl = tmp_tpl
						+ "	<input date-time time-zone=\"Asia/Hong_Kong\" ng-model=\"meta."
						+ obj.ng_model
						+ "\" date-change=\"changeDate\" view=\"date\" auto-close=\"true\" min-view=\"date\" format=\"YYYY-MM-DD\" class=\"form-control\" type=\"datetime\">";
				tmp_tpl = tmp_tpl + "</div>";
				tmp_tpl = tmp_tpl + "</div>";
				formhtml = formhtml + tmp_tpl;
			} else if (obj.type == "pic") {
				tmp_tpl = tmp_tpl + "<div class=\"form-group\"> ";
				tmp_tpl = tmp_tpl + "	<label class=\"col-sm-2 control-label\">"
						+ need_col + obj.label + "</label> ";
				tmp_tpl = tmp_tpl + "	<div class=\"col-sm-10\"> ";
				tmp_tpl = tmp_tpl
						+ "		<div class=\"dropzone\" drop-zone dzconfig=\"meta."
						+ obj.conf
						+ "\" dzeventHandlers=\"dtldzevent\" enctype=\"multipart/form-data\"> ";
				tmp_tpl = tmp_tpl
						+ "			<div id=\"dropzone\" class=\"fallback\"> ";
				tmp_tpl = tmp_tpl
						+ "				<input name=\"file\" type=\"file\" multiple=\"\" /> ";
				tmp_tpl = tmp_tpl + "			</div> ";
				tmp_tpl = tmp_tpl + "		</div> ";
				tmp_tpl = tmp_tpl + "	</div> ";
				tmp_tpl = tmp_tpl + "</div>";
				formhtml = formhtml + tmp_tpl;
			}
		}

	}
	$scope.template = formhtml;
	$scope.sure = function() {
		meta.sure($uibModalInstance, $scope);
	};
	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	$timeout(
			function() {
				// 设置select全宽度
				for (var i = 0; i < select_ids.length; i++) {
					document.getElementById(select_ids[i] + "_chosen").style.width = "100%";
				}
			}, 200);

	$log.log("form init");
	if (typeof ($scope.meta.init) != "undefined") {
		$scope.meta.init($scope);
	}

}
/** ******************************modal end************************* */
/** ******************************simple tool table模版************************ */
// 原因:tooltable模版的table的instance无法初始化
function buildSimpleToolTableTpl() {
	return '<div class="wrapper wrapper-content animated fadeInRight">  <ng-include src="\'views/Template/simpleTool.html\'"></ng-include>   <div class="row"> <div class="col-lg-12"><div class="ibox"><div class="ibox-content">	<table datatable="ed" dt-options="dtOptions" dt-instance="dtInstance" dt-columns="dtColumns" dt-column-defs="dtColumnDefs" class="table table-hover"></table></div></div></div></div> </div> ';
}
/** ******************************simple tool table模版结束************************ */
