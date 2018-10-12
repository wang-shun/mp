define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/meeting/manageTemplate.html'
		  , 'views/meeting/hold-snippet-view', 'views/meeting/participate-snippet-view','views/meeting/service-snippet-view'],
		function($, CommunicationBaseView,Template,HoldView,ParticipateView,ServiceView) {
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
							this.views.HoldView = new HoldView({el:$(".SID-hold-snippet")});
							this.views.HoldView.render();
						},
						_selectTab :function(Event){
							var _this=$(Event.currentTarget);
							this.$el.find(".tablist li").removeClass("active");
							_this.addClass("active");
							this.clearBusinessViews();
							if (_this.data("name")=="hold-meet") {
								this.$el.find(".hold-meet").show();
								this.$el.find(".join-meet").hide();
								this.views.HoldView = new HoldView({el:$(".SID-hold-snippet")});
								this.views.HoldView.render();
							} else if (_this.data("name")=="join-meet") {
								this.$el.find(".join-meet").show();
								this.$el.find(".hold-meet").hide();
								this.views.ParticipateView = new ParticipateView({el:$(".SID-participate-snippet")});
								this.views.ParticipateView.render();
							} else if (_this.data("name")=="service-meet") {
								this.$el.find(".join-meet").hide();
								this.$el.find(".hold-meet").hide();
								this.$el.find(".service-meet").show();
								this.views.ServiceView = new ServiceView({el:$(".SID-service-snippet")});
								this.views.ServiceView.render();
							}
							return this;
						}
					});
			return ManageSnippetView;
		});