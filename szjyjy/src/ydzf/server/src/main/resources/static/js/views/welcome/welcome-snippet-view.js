define(
		[ 'jquery', 'views/communication-base-view','text!../../templates/welcome/welcomeTemplate.html'],
		function($, CommunicationBaseView,Template) {
			var RoomSnippetView = CommunicationBaseView
					.extend({
						events : {
						},
						initialize : function() {
							this.chaildView = {};
						},
						render : function() {
							this.$el.empty();
							this.setContentHTML();
							return this;
						},
						refresh : function() {
						},
						destroyBusinessViews : function(){
							$.each(this.chaildView, function(index, view) {
								 view.destroy();
							});		
							this.chaildView = {};
						},		
						destroy : function() {
							this.destroyBusinessViews()
							this.undelegateEvents();
							this.unbind();
							this.$el.empty();
						},
						setContentHTML : function (){
							var self = this;
							var template = _.template(Template);
							var html = template({});
							this.$el.append(html);

							this.initData();
						},
						initData:function(){
							var self = this;
							var appContext = self.getAppContext();
							var servicePath = appContext.cashUtil.getData('servicePath');
							var ropParam = appContext.cashUtil.getData('ropParam');
						}
					});
			return RoomSnippetView;
		});