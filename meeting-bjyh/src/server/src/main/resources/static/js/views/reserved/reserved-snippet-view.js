define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/reserved/manageTemplate.html',
		  'views/reserved/reserved-leftside-snippet-view','views/reserved/reserved-rightside-snippet-view'],
		function($, CommunicationBaseView,Template,LeftsideSnippetView,RightsideSnippetView) {
			var treeObj;
			var datatable;
			var TwosideSnippentView = CommunicationBaseView
					.extend({
						events : {
						},
						initialize : function() {
							this.views = {};
							//this.preRender();
						},
						render : function() {
							this.setContentHTML();
							/*加载左右结构*/
							this.renderRViews()
							
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
							var template = _.template(Template);
							var html = template({});
							this.$el.append(html);
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
							this.views.leftsideSnippetView = new LeftsideSnippetView({el:$(".SID-left-aside")});
							this.views.rightsideSnippetView = new RightsideSnippetView({el:$(".SID-right-aside")});
							this.renderBusinessViews();
						},
						reloadLeftView:function(){
							this.views.leftsideSnippetView.destroy();
							this.views.leftsideSnippetView = new LeftsideSnippetView({el:$(".SID-left-aside")});
							this.views.leftsideSnippetView.render(this);
						},
						reloadRightView:function(roomId){
							this.views.rightsideSnippetView.destroy();
							this.views.rightsideSnippetView = new RightsideSnippetView({el:$(".SID-right-aside")});
							this.$el.find(".search-toggle span:eq(1)").text("展开查询条件");
							this.$el.find(".search-toggle span:eq(0)").removeClass("fhicon-arrowU2").addClass("fhicon-arrowD2");
							this.views.rightsideSnippetView.render(this,roomId);
						}
					});
			return TwosideSnippentView;
		});