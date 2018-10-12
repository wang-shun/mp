define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/privilege/rightsTemplate.html',
		  'views/privilege/privilege-leftside-snippent-view','views/privilege/privilege-rightside-snippent-view'],
		function($, CommunicationBaseView,TemplateMeetingList,LeftsideSnippentView,RightsideSnippentView) {
			var treeObj;
			var datatable;
			var TwosideSnippentView = CommunicationBaseView
					.extend({
						events : {
						},
						initialize : function() {
							this.views = {};
						},
						render : function() {
							this.setContentHTML();
							return this;
						},
						refresh : function() {
						},
						destroy : function() {
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
						},
						setContentHTML : function (){
							var template = _.template(TemplateMeetingList);
							var html = template({});
							this.$el.append(html);
							/*加载左右结构*/
							this.renderRViews();
						},
						renderBusinessViews : function() {
							var self = this;
							$.each(this.views, function(index, view) {
								view.render(self);
							});
						},
						clearBusinessViews : function() {
							$.each(this.views, function(index, view) {
								view.destroy();
							});
							this.views = {};
						},
						renderRViews:function(){
							this.clearBusinessViews();
							this.views.leftsideSnippentView = new LeftsideSnippentView({el:$(".SID-left-aside")});
							this.renderBusinessViews();
							this.views.rightsideSnippentView = new RightsideSnippentView({el:$(".SID-right-aside")});
						},
						reloadRightView:function(roomId,tabId){
							this.views.rightsideSnippentView.destroy();
							this.views.rightsideSnippentView = new RightsideSnippentView({el:$(".SID-right-aside")});
							this.views.rightsideSnippentView.render(this,roomId,tabId);
						}
					});
			return TwosideSnippentView;
		});