define(["backbone",'views/channel/channel-snippet-view',
		'views/welcome/welcome-snippet-view'],function(Backbone,ChannelView,WelcomeView){
	var BaseRouter = Backbone.Router.extend({
		initialize:function(){
			this.views = {};
		},
	  	routes: {
			  "menu/:index": "menuJump",
			  "menu/:index/:time": "menuJump1"
		},
		menuJump: function(index) {
			this._initBusinessViews();
			if(index == 1){
				this.views.ChannelView = new ChannelView({el : $(".SID-channel-snippent")});
			}
			$($(".SID-menu-snippet").find('.SID-menu').get(index-1)).click();
			this._renderBusinessViews();
		},
		menuJump1: function(index,time) {
			this._initBusinessViews();
			if (index == 0){
				$('.SID-menu').parent().removeClass('active');
				this.views.welcomeView = new WelcomeView({el : $(".SID-welcome-snippent")});
			}
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