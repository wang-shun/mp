define(["backbone",'views/hotel/order-snippet-view'],
		function(Backbone, OrderView){
	var BaseRouter = Backbone.Router.extend({
		initialize:function(){
			this.views = {};
		},
	  	routes: {
			  "menu/:index": "menuJump",
			  "menu/:index/:time": "menuJump1"
		},
		menuJump: function(index) {
			//this.views.roomView = new RoomView({el : $(".SID-statistics-snippent")});
			this._initBusinessViews();
			if(index == 1){
				this.views.roomView = new  OrderView({el : $(".SID-order-snippent")});
			}else if(index == 2){
				this.views.reservedView = new ReservedView({el : $(".SID-report-snippent")});
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
			// this.$el.find('.SID-attention-user').empty();
//			$(".SID-room-snippent").empty();
//			$(".SID-reserved-snippent").empty();
//			$(".SID-privilege-snippent").empty();
//			$(".SID-statistics-snippent").empty();
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