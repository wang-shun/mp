define(["backbone",'views/room/room-snippet-view','views/room/surveyTemplate-snippet-view'
        ,'views/mysurvey/mysurvey-snippet-view'
        ],function(Backbone, RoomView,SurveyTemplateView,MySurveyView){
	var BaseRouter = Backbone.Router.extend({
		initialize:function(){
			this.views = {};
		},
		
	  	routes: {
		    "menu/:index": "menuJump",
		    "temp/:index/:id": "menuJump1"
		},
		
		menuJump: function(index) {
			this._initBusinessViews();
			if(index == 1){
				this.views.mySurveyView = new MySurveyView({el : $(".SID-mysurvey-snippent")});
			}
			if(index == 2){
				this.views.roomView = new RoomView({el : $(".SID-room-snippent")});
			}
			if(index == 3){
				this.views.surveyView = new SurveyTemplateView({el : $(".SID-reserved-snippent")});
			}
			if(index == 4){
				this.views.roomView = new RoomView({el : $(".SID-privilege-snippent")});
			}
			$($(".SID-menu-snippet").find('.SID-menu').get(index-1)).click();
			this._renderBusinessViews();
			history.go(-1);
		},
		menuJump1: function(index,id) {
			this.views.roomView = new RoomView({el : $(".SID-room-snippent")});
			this.views.roomView._toEditInit(id);
			$($(".SID-menu-snippet").find('.SID-menu').get(1)).click();
			this._renderBusinessViews();
			history.go(-1);
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