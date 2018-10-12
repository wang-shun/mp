define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/meeting/indexTemplate.html'
		  , 'views/monitor/dashboard-index-snippet-view'],
		function($, CommunicationBaseView,Template,DashBoardIndexView) {
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
							this.views.DashBoardIndexView = new DashBoardIndexView({el:$(".SID-dashboard-index-snippet")});
							this.views.DashBoardIndexView.render();
						},
						_selectTab :function(Event){
							var _this=$(Event.currentTarget);
							this.$el.find(".SID-bigmenu li").removeClass("active");
							_this.addClass("active");
							this.clearBusinessViews();
							
							this.$el.find(".dashboard-index").hide();
//                            this.$el.find(".dashboard-manage").hide();
//                            this.$el.find(".measurement-manage").hide();
//                            this.$el.find(".tag-manage").hide();
//                            this.$el.find(".sample-manage").hide();
//                            this.$el.find(".retention-manage").hide();
//                            this.$el.find(".rule-manage").hide();
//                            this.$el.find(".alertlog-manage").hide();
							
							if (_this.data("name")=="dashboard-index") {
								this.$el.find(".dashboard-index").show();
								this.views.DashBoardIndexView = new DashBoardIndexView({el:$(".SID-dashboard-index-snippet")});
								this.views.DashBoardIndexView.render();
//							} else if (_this.data("name")=="dashboard-manage") {
//								this.$el.find(".dashboard-manage").show();
//								this.views.DashBoardManageView = new DashBoardManageView({el:$(".SID-dashboard-manage-snippet")});
//								this.views.DashBoardManageView.render();
//							} else if (_this.data("name")=="measurement-manage") {
//								this.$el.find(".measurement-manage").show();
//								this.views.MeasurementManageView = new MeasurementManageView({el:$(".SID-measurement-manage-snippet")});
//								this.views.MeasurementManageView.render();
//							} else if (_this.data("name")=="tag-manage") {
//								this.$el.find(".tag-manage").show();
//								this.views.TagManageView = new TagManageView({el:$(".SID-tag-manage-snippet")});
//								this.views.TagManageView.render();
//							} else if (_this.data("name")=="sample-manage") {
//								this.$el.find(".sample-manage").show();
//								this.views.SampleManageView = new SampleManageView({el:$(".SID-sample-manage-snippet")});
//								this.views.SampleManageView.render();
//							} else if (_this.data("name")=="retention-manage") {
//								this.$el.find(".retention-manage").show();
//								this.views.RetentionManageView = new RetentionManageView({el:$(".SID-retention-manage-snippet")});
//								this.views.RetentionManageView.render();
//							} else if (_this.data("name")=="rule-manage") {
//								this.$el.find(".rule-manage").show();
//								this.views.RuleManageView = new RuleManageView({el:$(".SID-rule-manage-snippet")});
//								this.views.RuleManageView.render(this);
//							} else if (_this.data("name")=="alertlog-manage") {
//								this.$el.find(".alertlog-manage").show();
//								this.views.AlertLogManageView = new AlertLogManageView({el:$(".SID-alertlog-manage-snippet")});
//								this.views.AlertLogManageView.render(this);
							}
							return this;
						}
					});
			return ManageSnippetView;
		});