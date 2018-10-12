define(["backbone",'views/room/room-snippet-view'],function(Backbone, RoomView){
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
				this.views.roomView = new RoomView({el : $(".SID-room-snippent")});
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