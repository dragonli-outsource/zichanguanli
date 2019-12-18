

/**
 * INSPINIA - Responsive Admin Theme
 * 
 * Main directives.js file Define directives for used plugin
 * 
 * 
 * Functions (directives) - sideNavigation - iboxTools - minimalizaSidebar -
 * vectorMap - sparkline - icheck - ionRangeSlider - dropZone - responsiveVideo -
 * chatSlimScroll - customValid - fullScroll - closeOffCanvas - clockPicker -
 * landingScrollspy - fitHeight - iboxToolsFullScreen - slimScroll - truncate -
 * touchSpin - markdownEditor - resizeable - bootstrapTagsinput - passwordMeter
 * 
 */

/**
 * pageTitle - Directive for set Page title - mata title
 */
function pageTitle($rootScope, $timeout, $transitions) {
	return {
		link : function(scope, element) {
			$transitions.onSuccess({
				to : '**'
			}, function(trans) {
				var current = trans.router.globals.current;
				var title = "后台管理";
				if (angular.isDefined(current.data)
						&& angular.isDefined(current.data.pageTitle)) {
					title = current.data.pageTitle;
				}
				element.text(title);
			});
		}
	}
};

/**
 * sideNavigation - Directive for run metsiMenu on sidebar navigation
 */
function sideNavigation($timeout) {
	return {
		restrict : 'A',
		link : function(scope, element) {
			// Call the metsiMenu plugin and plug it to sidebar navigation
			$timeout(function() {
				element.metisMenu();
			});

			// Colapse menu in mobile mode after click on element
			var menuElement = $('#side-menu a:not([href$="\\#"])');
			menuElement.click(function() {
				if ($(window).width() < 769) {
					$("body").toggleClass("mini-navbar");
				}
			});

			// Enable initial fixed sidebar
			if ($("body").hasClass('fixed-sidebar')) {
				var sidebar = element.parent();
				sidebar.slimScroll({
					height : '100%',
					railOpacity : 0.9,
				});
			}

		}
	};
};

/**
 * responsibleVideo - Directive for responsive video
 */
function responsiveVideo() {
	return {
		restrict : 'A',
		link : function(scope, element) {
			var figure = element;
			var video = element.children();
			video.attr('data-aspectRatio', video.height() / video.width())
					.removeAttr('height').removeAttr('width')

			// We can use $watch on $window.innerWidth also.
			$(window).resize(
					function() {
						var newWidth = figure.width();
						video.width(newWidth).height(
								newWidth * video.attr('data-aspectRatio'));
					}).resize();
		}
	}
}

/**
 * iboxTools - Directive for iBox tools elements in right corner of ibox
 */
function iboxTools($timeout) {
	return {
		restrict : 'A',
		scope : true,
		templateUrl : 'views/common/ibox_tools.html',
		controller : function($scope, $element) {
			// Function for collapse ibox
			$scope.showhide = function() {
				var ibox = $element.closest('div.ibox');
				var icon = $element.find('i:first');
				var content = ibox.children('.ibox-content');
				content.slideToggle(200);
				// Toggle icon from up to down
				icon.toggleClass('fa-chevron-up')
						.toggleClass('fa-chevron-down');
				ibox.toggleClass('').toggleClass('border-bottom');
				$timeout(function() {
					ibox.resize();
					ibox.find('[id^=map-]').resize();
				}, 50);
			};
			// Function for close ibox
			$scope.closebox = function() {
				var ibox = $element.closest('div.ibox');
				ibox.remove();
			}
		}
	};
}

/**
 * iboxTools with full screen - Directive for iBox tools elements in right
 * corner of ibox with full screen option
 */
function iboxToolsFullScreen($timeout) {
	return {
		restrict : 'A',
		scope : true,
		templateUrl : 'views/common/ibox_tools_full_screen.html',
		controller : function($scope, $element) {
			// Function for collapse ibox
			$scope.showhide = function() {
				var ibox = $element.closest('div.ibox');
				var icon = $element.find('i:first');
				var content = ibox.children('.ibox-content');
				content.slideToggle(200);
				// Toggle icon from up to down
				icon.toggleClass('fa-chevron-up')
						.toggleClass('fa-chevron-down');
				ibox.toggleClass('').toggleClass('border-bottom');
				$timeout(function() {
					ibox.resize();
					ibox.find('[id^=map-]').resize();
				}, 50);
			};
			// Function for close ibox
			$scope.closebox = function() {
				var ibox = $element.closest('div.ibox');
				ibox.remove();
			};
			// Function for full screen
			$scope.fullscreen = function() {
				var ibox = $element.closest('div.ibox');
				var button = $element.find('i.fa-expand');
				$('body').toggleClass('fullscreen-ibox-mode');
				button.toggleClass('fa-expand').toggleClass('fa-compress');
				ibox.toggleClass('fullscreen');
				setTimeout(function() {
					$(window).trigger('resize');
				}, 100);
			}
		}
	};
}

/**
 * minimalizaSidebar - Directive for minimalize sidebar
 */
function minimalizaSidebar($timeout) {
	return {
		restrict : 'A',
		template : '<a  class="navbar-minimalize minimalize-styl-2 btn btn-switch" href="" ng-click="minimalize()"><i class="fa fa-dedent fa-fw text"></i></a>',
		controller : function($scope, $element) {
			$scope.minimalize = function() {
				$("body").toggleClass("mini-navbar");

				if (!$('body').hasClass('mini-navbar')
						|| $('body').hasClass('body-small')) {
					// Hide menu in order to smoothly turn on when maximize menu
					$('#side-menu').hide();
					// For smoothly turn on menu
					setTimeout(function() {
						$('#side-menu').fadeIn(400);
					}, 200);

				} else if ($('body').hasClass('fixed-sidebar')) {
					$('#side-menu').hide();
					setTimeout(function() {
						$('#side-menu').fadeIn(400);
					}, 100);
				} else {
					// Remove all inline style from jquery fadeIn function to
					// reset menu state
					$('#side-menu').removeAttr('style');
				}
			}
		}
	};
};

function closeOffCanvas() {
	return {
		restrict : 'A',
		template : '<a class="close-canvas-menu" ng-click="closeOffCanvas()"><i class="fa fa-times"></i></a>',
		controller : function($scope, $element) {
			$scope.closeOffCanvas = function() {
				$("body").toggleClass("mini-navbar");
			}
		}
	};
}

/**
 * vectorMap - Directive for Vector map plugin
 */
function vectorMap() {
	return {
		restrict : 'A',
		scope : {
			myMapData : '=',
		},
		link : function(scope, element, attrs) {
			var map = element.vectorMap({
				map : 'world_mill_en',
				backgroundColor : "transparent",
				regionStyle : {
					initial : {
						fill : '#e4e4e4',
						"fill-opacity" : 0.9,
						stroke : 'none',
						"stroke-width" : 0,
						"stroke-opacity" : 0
					}
				},
				series : {
					regions : [ {
						values : scope.myMapData,
						scale : [ "#1ab394", "#22d6b1" ],
						normalizeFunction : 'polynomial'
					} ]
				},
			});
			var destroyMap = function() {
				element.remove();
			};
			scope.$on('$destroy', function() {
				destroyMap();
			});
		}
	}
}

/**
 * sparkline - Directive for Sparkline chart
 */
function sparkline() {
	return {
		restrict : 'A',
		scope : {
			sparkData : '=',
			sparkOptions : '=',
		},
		link : function(scope, element, attrs) {
			scope.$watch(scope.sparkData, function() {
				render();
			});
			scope.$watch(scope.sparkOptions, function() {
				render();
			});
			var render = function() {
				$(element).sparkline(scope.sparkData, scope.sparkOptions);
			};
		}
	}
};

/**
 * icheck - Directive for custom checkbox icheck
 */
// .directive('onFinishRender',['$timeout', '$parse', function ($timeout,
// $parse) {
function onFinishRender($timeout, $parse) {
	return {
		restrict : 'A',
		link : function(scope, element, attr) {
			if (scope.$last === true) {
				$timeout(function() {
					scope.$emit('ngRepeatFinished'); // 事件通知
					var fun = $scope.$eval(attrs.onFinishRender);
					if (fun && typeof (fun) == 'function') {
						fun(); // 回调函数
					}
				});
			}
		}
	}
};

function icheck($timeout) {
	return {
		restrict : 'A',
		require : 'ngModel',
		link : function($scope, element, $attrs, ngModel) {
			return $timeout(function() {
				var value;
				value = $attrs['value'];

				$scope.$watch($attrs['ngModel'], function(newValue) {
					$(element).iCheck('update');
				})

				return $(element)
						.iCheck({
							checkboxClass : 'icheckbox_square-green',
							radioClass : 'iradio_square-green'

						})
						.on(
								'ifChanged',
								function(event) {
									if ($(element).attr('type') === 'checkbox'
											&& $attrs['ngModel']) {
										$scope
												.$apply(function() {
													return ngModel
															.$setViewValue(event.target.checked);
												});
									}
									if ($(element).attr('type') === 'radio'
											&& $attrs['ngModel']) {
										return $scope
												.$apply(function() {
													return ngModel
															.$setViewValue(value);
												});
									}
								});
			});
		}
	};
}

/**
 * ionRangeSlider - Directive for Ion Range Slider
 */
function ionRangeSlider() {
	return {
		restrict : 'A',
		scope : {
			rangeOptions : '='
		},
		link : function(scope, elem, attrs) {
			elem.ionRangeSlider(scope.rangeOptions);
		}
	}
}

/**
 * dropZone - Directive for Drag and drop zone file upload plugin
 */
function dropZone() {
	return {
		restrict : 'AE',
		scope : {
			dzconfig : '=',
			dzeventhandlers : '=',
			alias : '@'
		},
		link : function(scope, element, attrs) {

			// //创建对象
			// if (scope.dzconfig.maxFiles == null) {
			// scope.dzconfig.maxFiles = 1;
			// }
			// drop files here to uploads
			console.log(scope.dzconfig);
			if (typeof (scope.dzconfig.dictDefaultMessage) == "undefined") {
				scope.dzconfig.dictDefaultMessage = "点击上传文件";
			}

			scope.dzconfig.dictFallbackMessage = "Your browser does not support drag'n'drop file uploads."
			scope.dzconfig.dictFallbackText = "Please use the fallback form below to upload your files like in the olden days."
			scope.dzconfig.dictFileTooBig = "File is too big ({{filesize}}MiB). Max filesize: {{maxFilesize}}MiB."
			scope.dzconfig.dictInvalidFileType = "不能上传此类文件"
			scope.dzconfig.dictResponseError = "服务端错误代码:{{statusCode}}"
			scope.dzconfig.dictCancelUpload = "取消上传"
			scope.dzconfig.dictCancelUploadConfirmation = "Are you sure you want to cancel this upload?"
			scope.dzconfig.dictRemoveFile = "删除"
			scope.dzconfig.dictRemoveFileConfirmation = null
			scope.dzconfig.dictMaxFilesExceeded = "超过最大文件限制"

			var dzeventhandlers = {
				'addedfile' : function(file) {
					scope.file = file;
					if (this.files[1] != null) {
						this.removeFile(this.files[0]);
					}
					scope.$apply(function() {
						scope.fileAdded = true;
					});
				},

				'success' : function(file, response) {
				},
				'maxfilesexceeded' : function(file) {
					if (scope.dropzone.options.maxFiles == 1) {
						scope.dropzone.removeAllFiles()
						scope.dropzone.addFile(file);
					} else {
						scope.dropzone.removeFile(file);
					}
					message_error("已超过文件最大限制数目,"
							+ scope.dropzone.options.maxFiles);
				}

			};

			dropzone = new Dropzone(element[0], scope.dzconfig);

			// angular.forEach(dzeventhandlers, function(handler, event) {
			//				
			// dropzone.on(event, handler);
			// });
			// 派发前前端配置监听事件
			if (scope.dzeventhandlers) {
				Object.keys(scope.dzeventhandlers).forEach(function(eventName) {
					dropzone.on(eventName, scope.dzeventhandlers[eventName]);
				});
			}

			scope.processDropzone = function() {
				dropzone.processQueue();
			};

			scope.resetDropzone = function() {
				dropzone.removeAllFiles();
			}
		}
	}
}

/**
 * chatSlimScroll - Directive for slim scroll for small chat
 */
function chatSlimScroll($timeout) {
	return {
		restrict : 'A',
		link : function(scope, element) {
			$timeout(function() {
				element.slimscroll({
					height : '234px',
					railOpacity : 0.4
				});

			});
		}
	};
}

/**
 * customValid - Directive for custom validation example
 */
function customValid() {
	return {
		require : 'ngModel',
		link : function(scope, ele, attrs, c) {
			scope.$watch(attrs.ngModel, function() {

				// You can call a $http method here
				// Or create custom validation

				var validText = "Inspinia";

				if (scope.extras == validText) {
					c.$setValidity('cvalid', true);
				} else {
					c.$setValidity('cvalid', false);
				}

			});
		}
	}
}

/**
 * fullScroll - Directive for slimScroll with 100%
 */
function fullScroll($timeout) {
	return {
		restrict : 'A',
		link : function(scope, element) {
			$timeout(function() {
				element.slimscroll({
					height : '100%',
					railOpacity : 0.9
				});

			});
		}
	};
}

/**
 * slimScroll - Directive for slimScroll with custom height
 */
function slimScroll($timeout) {
	return {
		restrict : 'A',
		scope : {
			boxHeight : '@'
		},
		link : function(scope, element) {
			$timeout(function() {
				element.slimscroll({
					height : scope.boxHeight,
					railOpacity : 0.9
				});

			});
		}
	};
}

/**
 * clockPicker - Directive for clock picker plugin
 */
function clockPicker() {
	return {
		restrict : 'A',
		link : function(scope, element) {
			element.clockpicker();
		}
	};
};

/**
 * landingScrollspy - Directive for scrollspy in landing page
 */
function landingScrollspy() {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			element.scrollspy({
				target : '.navbar-fixed-top',
				offset : 80
			});
		}
	}
}

/**
 * fitHeight - Directive for set height fit to window height
 */
function fitHeight() {
	return {
		restrict : 'A',
		link : function(scope, element) {
			element.css("height", $(window).height() + "px");
			element.css("min-height", $(window).height() + "px");
		}
	};
}

/**
 * truncate - Directive for truncate string
 */
function truncate($timeout) {
	return {
		restrict : 'A',
		scope : {
			truncateOptions : '='
		},
		link : function(scope, element) {
			$timeout(function() {
				element.dotdotdot(scope.truncateOptions);

			});
		}
	};
}

/**
 * touchSpin - Directive for Bootstrap TouchSpin
 */
function touchSpin() {
	return {
		restrict : 'A',
		scope : {
			spinOptions : '='
		},
		link : function(scope, element, attrs) {
			scope.$watch(scope.spinOptions, function() {
				render();
			});
			var render = function() {
				$(element).TouchSpin(scope.spinOptions);
			};
		}
	}
};

/**
 * markdownEditor - Directive for Bootstrap Markdown
 */
function markdownEditor() {
	return {
		restrict : "A",
		require : 'ngModel',
		link : function(scope, element, attrs, ngModel) {
			$(element).markdown({
				savable : false,
				onChange : function(e) {
					ngModel.$setViewValue(e.getContent());
				}
			});
		}
	}
};

/**
 * passwordMeter - Directive for jQuery Password Strength Meter
 */
function passwordMeter() {
	return {
		restrict : 'A',
		scope : {
			pwOptions : '='
		},
		link : function(scope, element, attrs) {
			scope.$watch(scope.pwOptions, function() {
				render();
			});
			var render = function() {
				$(element).pwstrength(scope.pwOptions);
			};
		}
	}
};
angular.module('inspinia').directive('compile', function($compile) {
	return function(scope, element, attrs) {
		scope.$watch(function(scope) {
			return scope.$eval(attrs.compile);
		}, function(value) {
			element.html(value);
			$compile(element.contents())(scope);
		});
	};
});
 
/**
 * 
 * Pass all functions into module
 */
angular.module('inspinia').directive('pageTitle', pageTitle).directive(
		'sideNavigation', sideNavigation).directive('iboxTools', iboxTools)
		.directive('minimalizaSidebar', minimalizaSidebar).directive(
				'vectorMap', vectorMap).directive('sparkline', sparkline)
		.directive('icheck', icheck)
		.directive('ionRangeSlider', ionRangeSlider).directive('dropZone',
				dropZone).directive('responsiveVideo', responsiveVideo)
		.directive('chatSlimScroll', chatSlimScroll).directive('customValid',
				customValid).directive('fullScroll', fullScroll).directive(
				'closeOffCanvas', closeOffCanvas).directive('clockPicker',
				clockPicker).directive('landingScrollspy', landingScrollspy)
		.directive('fitHeight', fitHeight).directive('iboxToolsFullScreen',
				iboxToolsFullScreen).directive('slimScroll', slimScroll)
		.directive('truncate', truncate).directive('touchSpin', touchSpin)
		.directive('markdownEditor', markdownEditor).directive('passwordMeter',
				passwordMeter).directive('onFinishRender', onFinishRender);



angular.module('inspinia').directive("bnDocumentClick",
		function($document, $parse) {
			// 将Angular的上下文链接到DOM事件
			var linkFunction = function($scope, $element, $attributes) {
				// 获得表达式
				var scopeExpression = $attributes.bnDocumentClick;
				// 使用$parse来编译表达式
				var invoker = $parse(scopeExpression);
				var event = $attributes.bnDocumentEvent;
				if (!angular.isDefined(event)) {
					event = "click";
				}
				// 绑定click事件
				$document.on(event, function(event) {
					// 当点击事件被触发时，我们需要再次调用AngularJS的上下文。再次，我们使用$apply()来确保$digest()方法在幕后被调用
					$scope.$apply(function() {
						// 在scope中调用处理函数，将jQuery时间映射到$event对象上
						invoker($scope, {
							$event : event
						});
					});
				});
				// 当父控制器被从渲染文档中移除时监听"$destory"事件
			};
			// 返回linking函数
			return (linkFunction);
		});

'use strict';

/**
 * 0.1.1 Deferred load js/css file, used for ui-jq.js and Lazy Loading. @ flatfull.com
 * All Rights Reserved. Author url: #user/flatfull
 */

angular.module('ui.load', [])
	.service('uiLoad', ['$document', '$q', '$timeout', function ($document, $q, $timeout) {

		var loaded = [];
		var promise = false;
		var deferred = $q.defer();

		/**
		 * Chain loads the given sources
		 * 
		 * @param srcs
		 *            array, script or css
		 * @returns {*} Promise that will be resolved once the sources has been
		 *          loaded.
		 */
		this.load = function (srcs) {
			srcs = angular.isArray(srcs) ? srcs : srcs.split(/\s+/);
			var self = this;
			if(!promise){
				promise = deferred.promise;
			}
      angular.forEach(srcs, function(src) {
      	promise = promise.then( function(){
      		return src.indexOf('.css') >=0 ? self.loadCSS(src) : self.loadScript(src);
      	} );
      });
      deferred.resolve();
      return promise;
		}

		/**
		 * Dynamically loads the given script
		 * 
		 * @param src
		 *            The url of the script to load dynamically
		 * @returns {*} Promise that will be resolved once the script has been
		 *          loaded.
		 */
		this.loadScript = function (src) {
			if(loaded[src]) return loaded[src].promise;

			var deferred = $q.defer();
			var script = $document[0].createElement('script');
			script.src = src;
			script.onload = function (e) {
				$timeout(function () {
					deferred.resolve(e);
				});
			};
			script.onerror = function (e) {
				$timeout(function () {
					deferred.reject(e);
				});
			};
			$document[0].body.appendChild(script);
			loaded[src] = deferred;

			return deferred.promise;
		};

		/**
		 * Dynamically loads the given CSS file
		 * 
		 * @param href
		 *            The url of the CSS to load dynamically
		 * @returns {*} Promise that will be resolved once the CSS file has been
		 *          loaded.
		 */
		this.loadCSS = function (href) {
			if(loaded[href]) return loaded[href].promise;

			var deferred = $q.defer();
			var style = $document[0].createElement('link');
			style.rel = 'stylesheet';
			style.type = 'text/css';
			style.href = href;
			style.onload = function (e) {
				$timeout(function () {
					deferred.resolve(e);
				});
			};
			style.onerror = function (e) {
				$timeout(function () {
					deferred.reject(e);
				});
			};
			$document[0].head.appendChild(style);
			loaded[href] = deferred;

			return deferred.promise;
		};
}]);

angular.module('ui.jq', ['ui.load']).
value('uiJqConfig', {}).
directive('uiJq', ['uiJqConfig', 'JQ_CONFIG', 'uiLoad', '$timeout', function uiJqInjectingFunction(uiJqConfig, JQ_CONFIG, uiLoad, $timeout) {

return {
  restrict: 'A',
  compile: function uiJqCompilingFunction(tElm, tAttrs) {

    if (!angular.isFunction(tElm[tAttrs.uiJq]) && !JQ_CONFIG[tAttrs.uiJq]) {
      throw new Error('ui-jq: The "' + tAttrs.uiJq + '" function does not exist');
    }
    var options = uiJqConfig && uiJqConfig[tAttrs.uiJq];

    return function uiJqLinkingFunction(scope, elm, attrs) {

      function getOptions(){
        var linkOptions = [];

        // If ui-options are passed, merge (or override) them onto global
		// defaults and pass to the jQuery method
        if (attrs.uiOptions) {
          linkOptions = scope.$eval('[' + attrs.uiOptions + ']');
          if (angular.isObject(options) && angular.isObject(linkOptions[0])) {
            linkOptions[0] = angular.extend({}, options, linkOptions[0]);
          }
        } else if (options) {
          linkOptions = [options];
        }
        return linkOptions;
      }

      // If change compatibility is enabled, the form input's "change" event
		// will trigger an "input" event
      if (attrs.ngModel && elm.is('select,input,textarea')) {
        elm.bind('change', function() {
          elm.trigger('input');
        });
      }

      // Call jQuery method and pass relevant options
      function callPlugin() {
        $timeout(function() {
          elm[attrs.uiJq].apply(elm, getOptions());
        }, 0, false);
      }

      function refresh(){
        // If ui-refresh is used, re-fire the the method upon every change
        if (attrs.uiRefresh) {
          scope.$watch(attrs.uiRefresh, function() {
            callPlugin();
          });
        }
      }

      if ( JQ_CONFIG[attrs.uiJq] ) {
        uiLoad.load(JQ_CONFIG[attrs.uiJq]).then(function() {
          callPlugin();
          refresh();
        }).catch(function() {
          
        });
      } else {
        callPlugin();
        refresh();
      }
    };
  }
};
}]);


app.directive('focusMe', ['$timeout', function($timeout) {  
	  return {  
	    scope: { trigger: '@focusMe' },  
	    link: function(scope, element) {  
	      scope.$watch('trigger', function(value) {  
	        if(value === "true") {  
	          $timeout(function() {  
	            element[0].focus();  
	          });  
	        }  
	      });  
	    }  
	  };  
	}]); 

app.directive('contenteditable', function() {  
	  return {  
	    require: '?ngModel',  
	    link: function(scope, element, attrs, ctrl) {  
	   
	      // Do nothing if this is not bound to a model
	      if (!ctrl) { return; }  
	   
	      // Checks for updates (input or pressing ENTER)
	      // view -> model
	      element.bind('input enterKey', function() {  
	        var rerender = false;  
	        var html = element.html();  
	   
	        if (attrs.noLineBreaks) {  
	          html = html.replace(/<div>/g, '').replace(/<br>/g, '').replace(/<\/div>/g, '');  
	          rerender = true;  
	        }  
	   
	        scope.$apply(function() {  
	          ctrl.$setViewValue(html);  
	          if(rerender) {  
	            ctrl.$render();  
	          }  
	        });  
	      });  
	   
	      element.keyup(function(e){  
	        if(e.keyCode === 13){  
	          element.trigger('enterKey');  
	        }  
	      });  
	   
	      // model -> view
	      ctrl.$render = function() {  
	        element.html(ctrl.$viewValue);  
	      };  
	   
	      // load init value from DOM
	      ctrl.$render();  
	    }  
	  };  
	}); 


 
function modalcmdbdtlCtl($timeout, $localStorage, notify, $log, $uibModal,
		$uibModalInstance, $scope, meta, $http, $rootScope, DTOptionsBuilder,
		DTColumnBuilder, $compile,$window) {

	$scope.item = {};

	$scope.dtOptions = DTOptionsBuilder.fromFnPromise().withOption(
			'createdRow', function(row) {
				// Recompiling so we can bind Angular,directive to the
				$compile(angular.element(row).contents())($scope);
			
			});
	$scope.dtInstance = {}
	
	
	function renderAttach(data, type, full) {
		if(data>0){
			var acthtml =" <button ng-click=\"attachdown('" + full.id
			+ "')\" class=\"btn-white btn btn-xs\">下载("+data+")</button>   ";
			return acthtml;
		}else{
			return data;
		}	
	}
	
	
	
	$scope.attachdown=function(faultid){
	
		$http.post($rootScope.project + "/api/base/res/queryResFaultById.do", {
			id :faultid
		}).success(function(res) {
			if (res.success) {
			 
		
	 // $window
 if(res.data.attachdata.length>0){
 for(var i=0;i<res.data.attachdata.length;i++){
 var objectUrl=$rootScope.project +"/api/file/filedown.do?id="+res.data.attachdata[i].fileid;
 $window.open(objectUrl)
 }
 }
			} else {
				notify({
					message : res.message
				});
			}
		});
	}
	
	$scope.dtColumns = [
			DTColumnBuilder.newColumn('oper_time').withTitle('操作时间')
					.withOption('sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('name').withTitle('记录人').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('uuid').withTitle('维护编号').withOption(
							'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('processuser').withTitle('维护人').withOption(
									'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('processtime').withTitle('维护时间')
							.withOption('sDefaultContent', '').withOption('width', '90'),
			DTColumnBuilder.newColumn('attach_cnt').withTitle('附件数')
							.withOption('sDefaultContent', '').withOption('width', '30').renderWith(renderAttach),
			DTColumnBuilder.newColumn('reason').withTitle('原因').withOption(
					'sDefaultContent', '')
			]

	$scope.dtOptions2 = DTOptionsBuilder.fromFnPromise().withOption(
			'createdRow', function(row) {
				// Recompiling so we can bind Angular,directive to the
				$compile(angular.element(row).contents())($scope);
			});
	$scope.dtInstance2 = {}
	
	function renderCT(data, type, full) {
		 	if(angular.isDefined(data)){
		 		return data.substr(0,100)+"...";
		 	}else{
		 		return "";
		 	}
			
	}

	
	$scope.dtColumns2 = [
			DTColumnBuilder.newColumn('oper_time').withTitle('操作时间')
					.withOption('sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('name').withTitle('操作人').withOption(
					'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('oper_type').withTitle('操作类型').withOption(
							'sDefaultContent', '').withOption('width', '30'),
			DTColumnBuilder.newColumn('fullct').withTitle('内容').withOption(
					'sDefaultContent', '').withOption('width', '100').renderWith(renderCT),
			DTColumnBuilder.newColumn('mark').withTitle('备注').withOption(
					'sDefaultContent', '') ]

	if (angular.isDefined(meta.id)) {
		// 加载数据
		$http.post($rootScope.project + "/api/base/res/queryResAllById.do", {
			id : meta.id
		}).success(function(res) {
			if (res.success) {
				$scope.item = res.data.data;
				$scope.dtOptions.aaData = res.data.faultdata;
				$scope.dtOptions2.aaData = res.data.updatadata;
			} else {
				notify({
					message : res.message
				});
			}
		});
	}

	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}


function modal_faultZcListCtl($timeout, $localStorage, notify, $log, $uibModal,
		$uibModalInstance, $scope, id, type,$http, $rootScope, DTOptionsBuilder,
		DTColumnBuilder, $compile) {
	// type:one|many
	if(!angular.isDefined(type)){
		type="many"
	}
	$scope.search = "";
	// 分类
	$scope.dtOptions = DTOptionsBuilder.fromFnPromise().withDataProp('data')
			.withPaginationType('full_numbers').withDisplayLength(100)
			.withOption("ordering", false).withOption("responsive", false)
			.withOption("searching", true).withOption('scrollY', '200px')
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
			DTColumnBuilder.newColumn(null).withTitle("").withClass(
					'select-checkbox checkbox_center').renderWith(function() {
				return ""
			}),
			DTColumnBuilder.newColumn('uuid').withTitle('编号').withOption(
					'sDefaultContent', '').withOption("width", '30'),
			DTColumnBuilder.newColumn('classname').withTitle('类型').withOption(
					'sDefaultContent', '').withOption("width", '30'),
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
			DTColumnBuilder.newColumn('recyclestr').withTitle('资产状态')
					.withOption('sDefaultContent', '')
					.withOption('width', '30'),
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

	function flush() {
		var ps = {}

		ps.search = $scope.search;

		if ($scope.search == "") {
			notify({
				message : "请输入搜索内容"
			});
			return;
		}
		$http.post($rootScope.project + "/api/base/res/queryResAll.do", ps)
				.success(function(res) {
					if (res.success) {
						$scope.dtOptions.aaData = res.data;
					} else {
						notify({
							message : res.message
						});
					}
				})
	}
	$scope.query = function() {
		flush()
	}

	$scope.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

// function getSelectRow() {
// var data = $scope.dtInstance.DataTable.rows({
// selected : true
// })[0];
// if (data.length == 0) {
// notify({
// message : "请至少选择一项"
// });
// return;
// } else if (data.length > 1) {
// notify({
// message : "请最多选择一项"
// });
// return;
// } else {
// console.log("sel:", data);
// return $scope.dtOptions.aaData[data[0]];
// }
// }

	$scope.sure = function() {
		
		var data = $scope.dtInstance.DataTable.rows({
			selected : true
		})[0];

		if (data.length == 0) {
			notify({
				message : "请至少选择一项"
			});
			return;
		}  
		
		if(type=="one"){
			if(data.length>1){
				notify({
					message : "请最多选择一项"
				});
			}
			var item=$scope.dtOptions.aaData[data[0]];
			if(angular.isDefined(item)){
				$uibModalInstance.close(item);
			}
			return ;
		}
		
		if(type=="many"){
			var res=[];
			for(var i=0;i<data.length;i++){
				var item=$scope.dtOptions.aaData[data[i]];
				res.push(item);
			}
			if(angular.isDefined(res)){
				$uibModalInstance.close(res);
			}
			return ;
		}
 
	}

}


 