define(
		[ 'jquery', 'views/communication-base-view',
			'views/room/room-snippet-view','views/main/menu-routes'],
		function($, CommunicationBaseView, RoomView,BaseRouter) {
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
								if (msg.menuName == '我的问卷') {
									this.route.navigate("#menu/1", {trigger: true});
								}
								if (msg.menuName == '问卷列表') {
									this.route.navigate("#menu/2", {trigger: true});
								}
								if (msg.menuName == '问卷模板') {
									this.route.navigate("#menu/3", {trigger: true});
								}
								if (msg.menuName == '我的模板') {
									this.route.navigate("#temp/2/"+msg.id, {trigger: true});
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
								this.$el.find(".search-toggle span:eq(1)").text("关闭查询");
								this.$el.find(".search-toggle span:eq(0)").removeClass("fhicon-arrowD2").addClass("fhicon-arrowU2");
								
								this.$el.find(".search-modle").removeClass("hide");
							}else{
								this.$el.find(".search-toggle span:eq(1)").text("展开查询");
								this.$el.find(".search-toggle span:eq(0)").removeClass("fhicon-arrowU2").addClass("fhicon-arrowD2");
								
								this.$el.find(".search-modle").addClass("hide");
							}
						}
					});

			return MainContainerSnippent;
		});