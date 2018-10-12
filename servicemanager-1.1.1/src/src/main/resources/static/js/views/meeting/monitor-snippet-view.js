define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/meeting/monitorTemplate.html'
		  , 'views/databasemanage/database-manage-snippet-view'
		  , 'views/monitor/measurement-manage-snippet-view'
		  , 'views/monitor/tag-manage-snippet-view'
		  , 'views/monitor/sample-manage-snippet-view'
		  , 'views/monitor/retention-manage-snippet-view'
		  , 'views/monitor/rule-manage-snippet-view'],
		function($, CommunicationBaseView,Template,DatabaseManageView,MeasurementManageView,TagManageView,SampleManageView,RetentionManageView,RuleManageView) {
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
							this.views.MeasurementManageView = new MeasurementManageView({el:$(".SID-measurement-manage-snippet")});
							this.views.MeasurementManageView.render();
						},
						_selectTab :function(Event){
							var _this=$(Event.currentTarget);
							this.$el.find(".SID-bigmenu li").removeClass("active");
							_this.addClass("active");
							this.clearBusinessViews();
							
                            this.$el.find(".database-manage").hide();
                            this.$el.find(".measurement-manage").hide();
                            this.$el.find(".tag-manage").hide();
                            this.$el.find(".sample-manage").hide();
                            this.$el.find(".retention-manage").hide();
                            this.$el.find(".rule-manage").hide();
							
							if (_this.data("name")=="database-manage") {
								this.$el.find(".database-manage").show();
								this.views.DatabaseManageView = new DatabaseManageView({el:$(".SID-database-manage-snippet")});
								this.views.DatabaseManageView.render();
							} else if (_this.data("name")=="measurement-manage") {
								this.$el.find(".measurement-manage").show();
								this.views.MeasurementManageView = new MeasurementManageView({el:$(".SID-measurement-manage-snippet")});
								this.views.MeasurementManageView.render();
							} else if (_this.data("name")=="tag-manage") {
								this.$el.find(".tag-manage").show();
								this.views.TagManageView = new TagManageView({el:$(".SID-tag-manage-snippet")});
								this.views.TagManageView.render();
							} else if (_this.data("name")=="sample-manage") {
								this.$el.find(".sample-manage").show();
								this.views.SampleManageView = new SampleManageView({el:$(".SID-sample-manage-snippet")});
								this.views.SampleManageView.render();
							} else if (_this.data("name")=="retention-manage") {
								this.$el.find(".retention-manage").show();
								this.views.RetentionManageView = new RetentionManageView({el:$(".SID-retention-manage-snippet")});
								this.views.RetentionManageView.render();
							} else if (_this.data("name")=="rule-manage") {
								this.$el.find(".rule-manage").show();
								this.views.RuleManageView = new RuleManageView({el:$(".SID-rule-manage-snippet")});
								this.views.RuleManageView.render(this);
							}
							return this;
						}
					});
			return ManageSnippetView;
		});