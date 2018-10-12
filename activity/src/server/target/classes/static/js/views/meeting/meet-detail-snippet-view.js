define(
		[ 'jquery', 'views/communication-base-view'
		  ,'views/meeting/detail-meeting-snippet-view','views/meeting/detail-sign-snippet-view'
		  ,'text!../../templates/meeting/meetingdetailTemplate.html'],
		function($, CommunicationBaseView,DetailMeeting,DetailSign,Template) {
			var MeetingDetailView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-tab' : '_selectTab'
						},
						initialize : function() {
							this.views = {};
							this.data;
						},
						render : function(ajax) {
							this.data = ajax;
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
							var commonDialog = fh.commonOpenDialog('meetDetailDialog','',900,500,this.el);
							commonDialog.addBtn('cancel','关闭',function(){
								self.destroy();
								commonDialog.cancel();
							});
							if(self.data.participant == 1)
								self.$el.find(".SID-control").addClass("hide");
							this.views.detailMeeting = new DetailMeeting({el:$(".SID-meeting-snippet")});
							this.views.detailMeeting.render(self.data);
						},
						_selectTab :function(Event){
							var self = this;
							var _this = $(Event.currentTarget);
							_this.parent('li').siblings('li').removeClass('active');
							_this.parent("li").addClass('active');
							var tabId = _this.attr("data-tab-id");
							this.clearBusinessViews();
							if (tabId == "0") {
								this.views.detailMeeting = new DetailMeeting({el:$(".SID-meeting-snippet")});
								this.views.detailMeeting.render(self.data);
							} else if (tabId == "1") {
								this.views.detailSign = new DetailSign({el:$(".SID-sign-snippet")});
								this.views.detailSign.render(self.data);
							}
							this.$el.find('.SID-tab-container').hide();
							var currentTabContainer = this.$el.find('.SID-tab-container').filter(function( index ) {
								return $( this ).attr("tab-content-id") === tabId;
							});
							currentTabContainer.show();
							return this;
						}
					});
			return MeetingDetailView;
		});