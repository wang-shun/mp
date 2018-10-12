define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/statistics/statisticsTemplate.html'
		  , 'views/statistics/roomStat-snippet-view', 'views/statistics/userRecoed-snippet-view'],
		function($, CommunicationBaseView,Template,RoomStatView,UserRecoedView) {
			var StatisticsSnippetView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-tab' : '_selectTab'
						},
						initialize : function() {
							this.views = {};
							this.surveyId = "";
							this.title = "";
						},
						render : function(surveyId,title) {
							this.surveyId = surveyId;
							this.title = title;
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
							this.$el.find('.SID-nav').html(this.title);
							this.views.roomStatView = new RoomStatView({el:$(".SID-roomstat-snippet")});
							this.views.roomStatView.render(this.surveyId);
						},
						_selectTab :function(Event){
							var _this=$(Event.currentTarget);
							_this.parent('li').siblings('li').removeClass('active');
							_this.parent("li").addClass('active');
							var tabId = _this.attr("data-tab-id");
							this.clearBusinessViews();
							if (tabId == "0") {
								this.views.roomStatView = new RoomStatView({el:$(".SID-roomstat-snippet")});
								this.views.roomStatView.render(this.surveyId);
							} else if (tabId == "1") {
								this.views.userRecoedView = new UserRecoedView({el:$(".SID-userrecord-snippet")});
								this.views.userRecoedView.render(this.surveyId);
							}
							this.$el.find('.SID-tab-container').hide();
							var currentTabContainer = this.$el.find('.SID-tab-container').filter(function( index ) {
								return $( this ).attr("tab-content-id") === tabId;
							});
							currentTabContainer.show();
							return this;
						}
					});
			return StatisticsSnippetView;
		});