define(
		[ 'jquery', 'views/communication-base-view',
			'views/room/room-snippet-view',
			'views/reserved/reserved-snippet-view',
			'views/privilege/privilege-snippet-view',
			'views/statistics/statistics-snippet-view',
			'views/welcome/welcome-snippet-view','views/main/menu-routes'],
		function($, CommunicationBaseView, RoomView, ReservedView, PrivilegeView, StatisticsView,WelcomeView,BaseRouter) {
			var MainContainerSnippent = CommunicationBaseView
					.extend({
						events : {
							'click .SID-search-toggle' : 'searchToggleDisplay'
						},
						initialize : function() {
							this.preRender();
							this.views = {};
							this.route ;
						},
						render : function() {
							this.setUpContent();
							return this;
						},
						refresh : function() {
						},
						destroy : function() {
							this.undelegateEvents();
							this.unbind();
						},
						setUpContent : function() {
						},
						subscribeEvents : function() {
							var self = this;
							this.eventHub.subscribeEvent('MENU_CLICKL', function(msg) {
								self.initBusinessViews();
								if(!this.route){
									this.route = new BaseRouter();
								}
								if(!Backbone.History.started){
									Backbone.history.start();
								}
								var time = new Date().getTime();
								if (msg.menuName == '会议室维护') {
									this.route.navigate("#menu/1", {trigger: true});
								} else if (msg.menuName == '预定管理') {
									//self.views.reservedView = new ReservedView({el : $(".SID-reserved-snippent")});
									this.route.navigate("#menu/2", {trigger: true});
								} else if (msg.menuName == '权限管理') {
									//self.views.privilegeView = new PrivilegeView({el : $(".SID-privilege-snippent")});
									this.route.navigate("#menu/3", {trigger: true});
								} else if (msg.menuName == '运维统计') {
									//self.views.statisticsView = new StatisticsView({el : $(".SID-statistics-snippent")});
									this.route.navigate("#menu/4", {trigger: true});
								} else if (msg.menuName == '首页') {
									//$('.SID-menu').parent().removeClass('active');
									//this.views.welcomeView = new WelcomeView({el : $(".SID-welcome-snippent")});
									this.route.navigate("#menu/0/"+time, {trigger: true});
								}
								self.renderBusinessViews();
							});
						},
						/**
						 * init business views
						 */
						initBusinessViews : function() {
							$.each(this.views, function(index, view) {
								view.destroy();
							});
							this.views = {};
							// 清空关注用户的view
							// this.$el.find('.SID-attention-user').empty();
						},
						/**
						 * Render the business views
						 */
						renderBusinessViews : function() {
							$.each(this.views, function(index, view) {
								view.render();
							});
						},
						searchToggleDisplay : function(Event){
							if($(Event.currentTarget).find("span.fhicon-arrowD2").length==1){
								this.$el.find(".search-toggle span:eq(1)").text("关闭查询条件");
								this.$el.find(".search-toggle span:eq(0)").removeClass("fhicon-arrowD2").addClass("fhicon-arrowU2");
								
								this.$el.find(".search-modle").removeClass("hide");
							}else{
								this.$el.find(".search-toggle span:eq(1)").text("展开查询条件");
								this.$el.find(".search-toggle span:eq(0)").removeClass("fhicon-arrowU2").addClass("fhicon-arrowD2");
								
								this.$el.find(".search-modle").addClass("hide");
							}
						}
					});

			return MainContainerSnippent;
		});