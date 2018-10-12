define(["backbone", "util/eventhub-backbone", "util/constants", "util/cashUtil","util/progressbar", "app-views"],
	function(Backbone, EventHub, Constants,CashUtil, ProgressBar, AppViews) {
		var _instance = null;

		/**
		 * The application class
		 */
		var Portfolio = function(options) {
			if (_instance != null) {
				throw new Error("Cannot instantiate more than one App!");
			}

			this.options = options;
		};

		Portfolio.prototype = {
			/**
			 * Initialize the application environment.
			 */
			initialize: function(options) {
				// global options.
				this.options = options;
				// Constants reference.
				this.constants = Constants;
				// initialize the message bus for pub/sub.
				this.messageBus = EventHub;
				this.cashUtil = CashUtil;
			},
			/**
			 * Start the application. it will publish the "APPLICATION_START" event.
			 */
			start: function() {
				//console.log("App is start...");
				//this.publishEvent(this.constants.EVENTS.APPLICATION_START);
				
				//替换所有元素
				String.prototype.replaceAll = function(s1,s2) { 
				    return this.replace(new RegExp(s1,"gm"),s2); 
				}		
				
				$.datepicker.regional["zh-CN"] = {
					closeText: "关闭",
					prevText: "&#x3c;上月",
					nextText: "下月&#x3e;",
					currentText: "今天",
					monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
					monthNamesShort: ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"],
					dayNames: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
					dayNamesShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
					dayNamesMin: ["日", "一", "二", "三", "四", "五", "六"],
					weekHeader: "周",
					dateFormat: "yy-mm-dd",
					firstDay: 1,
					isRTL: !1,
					showMonthAfterYear: !0,
					yearSuffix: "年"
				}
				$.datepicker.setDefaults($.datepicker.regional["zh-CN"]);			

				$.ajaxSetup({
					type: "POST",
					contentType: "application/json",
					dataType: "json",
					async : true				  
				});				
			    // 备份jquery的ajax方法  
			    var _ajax=$.ajax;
			    // 重写ajax方法，先判断登录在执行success函数 
			    $.ajax=function(opt){
			    	var _success = opt && opt.success || function(a, b){};
			        var _opt = $.extend(opt, {
			        	success:function(event,xhr,options){
						      try{
							        if(event.code=="1020"){
							        //这里怎么处理在你，这里跳转的登录页面
							        	hideCover();
							        	fh.alert("会话失效，请重新登录！",false,function(){
							        		window.location.reload();
						    			});
							        }else{
							        	_success(event,xhr,options);
							        }
							      }catch(e){
							    	  console.log(e);
							      }			        		
			            },
			            timeout:600000, //10分钟超时处理
			            error:function (xhr, error, thrown) {
			            	 hideCover();
			            	if(error == "timeout"){
			            		fh.confirm("请求超时！是否重新加载？",function(){
			            			_ajax(_opt);
			            		});
			            	}
			            }  
			        });
			        _ajax(_opt);
			    };			

				this.appViews = new AppViews(this);
			},
			/**
			 * A wrapper function to publish an event.
			 */
			publishEvent: function(subject, data) {
				this.messageBus.publishEvent(subject, data);
			},
			/**
			 * A wrapper function to subscribe an event.
			 */
			subscribeEvent: function(subject, callback, context) {
				return this.messageBus.subscribeEvent(subject, callback, context);
			},
			/**
			 * A wrapper function to unsubscribe an event.
			 */
			unsubscribeEvent: function(subscription) {
				this.messageBus.unsubscribeEvent(subscription);
			}
		};

		/**
		 * Create an application instance.
		 */
		Portfolio.create = function(options) {
			if (_instance == null) {
				_instance = new Portfolio(options);
				_instance.initialize(options);
			}

			return _instance;
		};

		/**
		 * Get the application instance.
		 */
		Portfolio.getInstance = function() {
			if (_instance == null) {
				throw new Error("App instance can't be null!");
			}

			return _instance;
		};

		return Portfolio;
	}
);