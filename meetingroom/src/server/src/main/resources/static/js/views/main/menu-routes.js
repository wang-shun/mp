define(["backbone",'views/room/room-snippet-view',
		'views/reserved/reserved-snippet-view',
		'views/privilege/privilege-snippet-view',
		'views/statistics/statistics-snippet-view',
		'views/welcome/welcome-snippet-view'],function(Backbone, RoomView, ReservedView, PrivilegeView, StatisticsView,WelcomeView){
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
				this.views.roomView = new RoomView({el : $(".SID-room-snippent")});
			}else if(index == 2){
				this.views.reservedView = new ReservedView({el : $(".SID-reserved-snippent")});
			}else if(index == 3){
				this.views.privilegeView = new PrivilegeView({el : $(".SID-privilege-snippent")});
			}else if(index == 4){
				this.views.statisticsView = new StatisticsView({el : $(".SID-statistics-snippent")});
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