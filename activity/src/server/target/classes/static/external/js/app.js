define(["backbone", "util/eventhub-backbone", "util/constants", "util/progressbar",'routes', "util/cashUtil"],
	function(Backbone, EventHub, Constants, ProgressBar, BaseRouter,CashUtil) {
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
				this.router = {};
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
				console.log("App is start...");
				this.publishEvent(this.constants.EVENTS.APPLICATION_START);
				/*
				$.ajaxSetup({
					type: "POST",
					contentType: "application/json",
					dataType: "json",
					async : true				  
				});				
				*/
				// 备份jquery的ajax方法  
				var _ajax = $.ajax;
				// 重写ajax方法，先判断登录在执行success函数 
				$.ajax = function(opt) {
					var _success = opt && opt.success || function(a, b) {};
					var _opt = $.extend(opt, {
						success: function(event, xhr, options) {
							try {
								//判断对象的状态是交互完成
								if (options.readyState == 4) {
									//判断http的交互是否成功
									if (options.status == 200) {
										var responseBody = options.responseText;
										var resultcode = 1;
										var resultmessage = '';
										if ("" != responseBody && null != responseBody && undefined != responseBody) {
											responseBody = JSON.parse(responseBody);
										}
										if (resultcode != null && resultcode > 0) {
											_success(responseBody, resultcode, resultmessage);
										} else {

										}
									} else {
										console.log("访问后台数据异常！");
									}
								}

							} catch (e) {
								console.log(e);
							}
						}
					});
					_ajax(_opt);
				};
				this.router = new BaseRouter(this);
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