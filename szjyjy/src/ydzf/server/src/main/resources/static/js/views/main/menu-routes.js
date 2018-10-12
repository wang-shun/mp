define(["backbone",'views/law/law-snippet-view',
		'views/welcome/welcome-snippet-view'],function(Backbone,LawView,WelcomeView){
	var BaseRouter = Backbone.Router.extend({
		initialize:function(){
			this.views = {};
		},
	  	routes: {
			  "menu/:index": "menuJump"
		},
		menuJump: function(index) {
			this._initBusinessViews();
			if(index == 1){
				this.views.LawView = new LawView({el : $(".SID-law-snippent")});
			}
			$($(".SID-menu-snippet").find('.SID-menu').get(index-1)).click();
			this._renderBusinessViews();
		},
		/**
		 * init business views
		 */
		_initBusinessViews : function() {
			$.each(this.views, function(index, view) {
				view.destroy();
			});
			this.views = {};
			// 清空关注用户的view
		},
		/**
		 * Render the business views
		 */
		_renderBusinessViews : function() {
			$.each(this.views, function(index, view) {
				view.render();
			});
		}
		
	});
	return BaseRouter;
})