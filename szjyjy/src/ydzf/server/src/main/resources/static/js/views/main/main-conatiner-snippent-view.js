define(
		[ 'jquery', 'views/communication-base-view',
			'views/welcome/welcome-snippet-view','views/main/menu-routes'],
		function($, CommunicationBaseView, WelcomeView,BaseRouter) {
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
								if (msg.menuName == '现场查验') {
									this.route.navigate("#menu/1", {trigger: true});
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