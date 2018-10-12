define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/meeting/traceTemplate.html'
		  , 'views/trace/trace-manage-snippet-view'
		  , 'views/trace/trace-dependancy-snippet-view'],
		function($, CommunicationBaseView,Template,TraceManageView,TraceDependancyView) {
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
							this.views.TraceManageView = new TraceManageView({el:$(".SID-trace-manage-snippet")});
							this.views.TraceManageView.render();
						},
						_selectTab :function(Event){
							var _this=$(Event.currentTarget);
							this.$el.find(".SID-bigmenu li").removeClass("active");
							_this.addClass("active");
							this.clearBusinessViews();
							
                            this.$el.find(".trace-manage").hide();
                            this.$el.find(".measurement-manage").hide();
							
							if (_this.data("name")=="trace-manage") {
								this.$el.find(".trace-manage").show();
								this.views.TraceManageView = new TraceManageView({el:$(".SID-trace-manage-snippet")});
								this.views.TraceManageView.render();
							} else if (_this.data("name")=="trace-dependancy") {
								this.$el.find(".trace-dependancy").show();
								this.views.TraceDependancyView = new TraceDependancyView({el:$(".SID-trace-dependancy-snippet")});
								this.views.TraceDependancyView.render();
							} 
							return this;
						}
					});
			return ManageSnippetView;
		});