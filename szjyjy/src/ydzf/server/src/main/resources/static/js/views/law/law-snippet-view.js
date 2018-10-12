define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/law/manageTemplate.html',
		  'views/law/law-rightside-snippet-view'],
		function($, CommunicationBaseView,Template,RightsideSnippetView) {
			var treeObj;
			var datatable;
			var TwosideSnippentView = CommunicationBaseView
					.extend({
						events : {
							'click .SID-td-law' : '_onClickLaw'
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
							this.$el.find(".SID-td-defaule").addClass("active");
							this.clearBusinessViews();
							this.views.rightsideSnippetView = new RightsideSnippetView({el:$(".SID-right-aside")});
							this.renderBusinessViews();
						},
						
						reloadRightView:function(state){
							this.views.rightsideSnippetView.destroy();
							this.views.rightsideSnippetView = new RightsideSnippetView({el:$(".SID-right-aside")});
							this.$el.find(".search-toggle span:eq(1)").text("展开查询条件");
							this.$el.find(".search-toggle span:eq(0)").removeClass("fhicon-arrowU2").addClass("fhicon-arrowD2");
							this.views.rightsideSnippetView.render(this,state);
						},
						_onClickLaw:function(e){
							this.$el.find(".SID-td-law").removeClass("active");
							var target=e.target;
							var state = $(target).attr("data-state");
							if(state=='1'){
								this.$el.find(".SID-td-1").addClass("active");
							}else if(state=='2'){
								this.$el.find(".SID-td-2").addClass("active");
							}else {
								this.$el.find(".SID-td-defaule").addClass("active");
							}
								
							this.reloadRightView(state);
						}
					});
			return TwosideSnippentView;
		});