define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/meeting/manageTemplate.html'
		  , 'views/databasemanage/database-manage-snippet-view'
		  , 'views/redisdatabasemanage/database-manage-snippet-view'
		  , 'views/routemanage/route-manage-snippet-view'
		  , 'views/configmanage/config-manage-snippet-view'
		  , 'views/resourcemanage/resource-manage-snippet-view'],
		function($, CommunicationBaseView,Template,DatabaseManageView,RedisDatabaseManageView,RouteManageView,ConfigManageView,ResourceManageView) {
			var ManageSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-tab' : '_selectTab'
						},
						initialize : function() {
							this.views = {};
						},
						render : function() {
							this._setUpContent();
							return this;
						},
						refresh : function() {
						},
						destroy : function() {
							this.clearBusinessViews();
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
						},
						clearBusinessViews : function() {
							$.each(this.views, function(index, view) {
								view.destroy();
							});
							this.views = {};
						},
						_setUpContent: function() { 
							var self = this;
							var html = _.template(Template);
							this.$el.append(html);
							this.views.DatabaseManageView = new DatabaseManageView({el:$(".SID-database-manage-snippet")});
							this.views.DatabaseManageView.render();
						},
						_selectTab :function(Event){
							var _this=$(Event.currentTarget);
							this.$el.find(".tablist li").removeClass("active");
							_this.addClass("active");
							this.clearBusinessViews();
							
							this.$el.find(".route-manage").hide();
							this.$el.find(".config-manage").hide();
                            this.$el.find(".redisdatabase-manage").hide();
                            this.$el.find(".database-manage").hide();
                            this.$el.find(".resource-manage").hide();
							
							if (_this.data("name")=="database-manage") {
								this.$el.find(".database-manage").show();
								this.views.DatabaseManageView = new DatabaseManageView({el:$(".SID-database-manage-snippet")});
								this.views.DatabaseManageView.render();
							} else if (_this.data("name")=="redisdatabase-manage") {
								this.$el.find(".redisdatabase-manage").show();
								this.views.RedisDatabaseManageView = new RedisDatabaseManageView({el:$(".SID-redisdatabase-manage-snippet")});
								this.views.RedisDatabaseManageView.render();
							} else if (_this.data("name")=="resource-manage") {
								this.$el.find(".resource-manage").show();
								this.views.ResourceManageView = new ResourceManageView({el:$(".SID-resource-manage-snippet")});
								this.views.ResourceManageView.render();
							} else if (_this.data("name")=="route-manage") {
                                this.$el.find(".route-manage").show();
                                this.views.RouteManageView = new RouteManageView({el:$(".SID-route-manage-snippet")});
                                this.views.RouteManageView.render();
                            }else if (_this.data("name")=="config-manage") {
								this.$el.find(".config-manage").show();
								this.views.ConfigManageView = new ConfigManageView({el:$(".SID-config-manage-snippet")});
								this.views.ConfigManageView.render();
							}
							return this;
						}
					});
			return ManageSnippetView;
		});